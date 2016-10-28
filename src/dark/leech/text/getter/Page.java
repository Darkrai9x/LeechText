package dark.leech.text.getter;

import dark.leech.text.constant.Constants;
import dark.leech.text.constant.SettingConstants;
import dark.leech.text.item.Chapter;
import dark.leech.text.item.Connect;
import dark.leech.text.item.FileAction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Long on 9/16/2016.
 */
public class Page {
    private ArrayList<Chapter> chapList;
    private String url;
    private String saveDir;
    private int id;
    private String html;

    public Page(ArrayList<Chapter> chapList, String url, String saveDir, int id) {
        this.chapList = chapList;
        this.url = url;
        this.id = id;
        this.saveDir = saveDir;
    }

    public void getData() {
        for (int i = 0; i < SettingConstants.RECONN; i++) {
            html = new Connect().getHtml(url);
            if (html.length() > 100) break;
        }
        if (html.length() == 0) return;
        switch (search(url, "://(.*?)/", 1, true)) {
            case "banlong.us":
                BLU();
                break;
            case "www.tangthuvien.vn":
                TTV();
                break;
            case "bachngocsach.com":
                BNS();
                break;
            default:
                break;
        }
    }

    private void BLU() {
        Elements el = Jsoup.parse(html).select("blockquote");
        for (Element e : el) {

            String[] sp = e.html().split("SpoilerTarget bbCodeSpoilerText");
            if (sp.length == 1) continue;
            Chapter chapter = new Chapter();
            searchTitle(sp[0], chapter);
            String text = e.select("div.SpoilerTarget.bbCodeSpoilerText").last().html();
            text = Optimize(text);
            chapter.setId(id);
            if (e.select("input.VNXF_PayNRead.button.credits_charge") == null) {
                if (text.length() < 1000) {
                    if (text.split("<img ").length > 0) {
                        chapter.setImgChap(true);
                        new FileAction().string2file(text, saveDir + Constants.l + "raw" + Constants.l + Integer.toString(chapter.getId()) + ".txt");
                    } else {
                        chapter.setError(true);
                    }
                } else
                    new FileAction().string2file(text, saveDir + Constants.l + "raw" + Constants.l + Integer.toString(chapter.getId()) + ".txt");
                chapter.setGet(true);
                chapter.setUrl(url);
                chapter.setCompleted(true);
            } else chapter.setError(true);
            chapList.add(chapter);
            id++;
        }
    }


    private void BNS() {

    }

    private void TTV() {
        Elements el = Jsoup.parse(html).select("li.postbit.postbitim.postcontainer.old");
        for (Element e : el) {
            String[] sp = e.html().split("<input.*?value=\"\\s+(.iet.hrase|vp|VP)\\s+\"");
            if (sp.length == 1)
                sp = e.html().split("<input.*?button");
            for (int j = 0; j < sp.length - 1; j++) {
                Chapter chapter = new Chapter();
                searchTitle(sp[j], chapter);
                String text = "";
                text = search(sp[j + 1], "(?s)<div\\s*style=\"display:\\s*none;\">(.*?)</div>", 1, true);
                text = text.replaceAll("(</{0,1}blockquote.*?>|<p>\\s*</p>|<center>|</center>|<font.*?>|</font>|<div.*?>|<a .*?</a>|<input.*?\">|<img.*?\">|<!--.*?-->|)", "");
                text = text.replace(" .", ".");
                text = Optimize(text);
                chapter.setId(id);
                if (text.length() < 1000) {
                    if (text.split("<img ").length > 0) {
                        chapter.setImgChap(true);
                        new FileAction().string2file(text, saveDir + Constants.l + "raw" + Constants.l + Integer.toString(chapter.getId()) + ".txt");
                    } else {
                        chapter.setError(true);
                    }
                } else
                    new FileAction().string2file(text, saveDir + Constants.l + "raw" + Constants.l + Integer.toString(chapter.getId()) + ".txt");
                chapter.setGet(true);
                chapter.setUrl(url);
                chapter.setCompleted(true);
                chapList.add(chapter);
                id++;
            }
        }
    }

    //Tìm tiêu đề
    private void searchTitle(String html, Chapter chapter) {
        String partName = search(html, "(?s)((Q|Quy.n)\\s+\\d+.*?)<", 1, false);
        String chapName = search(html, "(?s)((H.i|[cC]h..ng)\\s+\\d+.*?)<", 1, false);
        partName = partName.replaceAll("(^\\s+|\\s+$)", "");
        chapName = chapName.replaceAll("(^\\s+|\\s+$)", "");
        chapter.setPartName(partName);
        chapter.setChapName(chapName);
    }

    //Tối ưu text
    private String Optimize(String src) {
        src = src.replaceAll(" +", " ");
        src = src.replaceAll("(</{0,1}div>|</{0,1}p>|<br>|</br>|<span.*?>|</span>)", "\n");
        src = src.replaceAll(" +[\n\r]", "\n");
        src = src.replaceAll("[\n\r]( +)", "\n");
        src = src.replaceAll("[\n\r]+", "\n");
        src = src.replaceAll("^\\s+|\\s+$", "");
        return src;
    }

    //Tìm văn bản theo regex
    private String search(String src, String regex, int group, boolean first) {
        Pattern r = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = r.matcher(src);
        String math = "";
        if (first) {
            if (m.find())
                math = m.group(group);

        } else while (m.find())
            math = m.group(group);
        return math;
    }
}
