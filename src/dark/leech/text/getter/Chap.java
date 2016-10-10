package dark.leech.text.getter;

import dark.leech.text.constant.Constants;
import dark.leech.text.constant.SettingConstants;
import dark.leech.text.item.Chapter;
import dark.leech.text.item.Connect;
import dark.leech.text.item.FileAction;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chap {
    private String url;
    private boolean error;
    private Connect Conn;
    private String saveDir;
    private Chapter chapter;
    private int id;

    public Chap(Chapter chapter, String saveDir) {
        Conn = new Connect();
        this.chapter = chapter;
        this.url = chapter.getUrl();
        this.saveDir = saveDir;
        this.id = chapter.getId();
    }

    // web

    // forum
    public Chap(String url, String saveDir, int id) {
        Conn = new Connect();
        this.url = url;
        this.saveDir = saveDir;
        error = false;
    }

    public Chapter getChapter() {
        boolean error = false;
        for (int i = 0; i < SettingConstants.RECONN; i++) {
            String text = "";
            try {
                text = Optimize(getData());
            } catch (Exception e) {
            }
            if (text.length() < 1000) {
                if (text.split("<img ").length > 1) {
                    error = false;
                    chapter.setImgChap(true);
                    new FileAction().string2file(text, saveDir + Constants.l + "raw" + Constants.l + Integer.toString(chapter.getId()) + ".txt");
                } else
                    error = true;
            } else {
                error = false;
                new FileAction().string2file(text, saveDir + Constants.l + "raw" + Constants.l + Integer.toString(chapter.getId()) + ".txt");
            }
            chapter.setError(error);
            if (!error)
                break;
        }
        return chapter;
    }

    private String getData() throws Exception {
        switch (search(url, "://(.*?)/", 1)) {
            case "truyenyy.com":
                return TYY();
            case "truyencv.com":
                return TCV();
            case "truyenfull.vn":
                return TFV();
            case "webtruyen.com":
                return WTC();
            case "truyencuatui.net":
                return TCT();
            case "goctruyen.com":
                return GTC();
            case "isach.info":
                return ISI();
            case "www.5book.vn":
                return W5V();
            case "truyen.hixx.info":
            case "convert.hixx.info":
                return HIXX();
            case "santruyen.com":
                return STC();
            case "sstruyen.com":
                return SST();
            case "tuchangioi.net":
                return TCG();
            case "truyenvl.net":
            case "www.truyenvl.net":
                return TVL();
            default:
                if (url.indexOf("bachngocsach.com/reader") != -1)
                    return BNS();
                return "";
        }
    }

    // progress
    // Truyenfull
    private String TFV() {
        String html = "";
        try {
            html = Jsoup.connect(url)
                    .data(Double.toString(Math.random()), "").ignoreContentType(true).timeout(10000).get().toString();
        } catch (IOException e) {
            html = "";
        }
        return Jsoup.parse(html).select("div.chapter-c").html();
    }

    // TruyenYY
    private String TYY() {
        return Jsoup.parse(Conn.getHtml(url)).select("div.text-truyen").html()
                .replaceAll("<span.*?</span>", "\n");
    }

    // TruyenCuaTui
    private String TCT() {
        return Jsoup.parse(Conn.getHtml(url)).select("div.content.chapter-content").html()
                .replaceAll("(?s)<div class.*</div>|<!--.*?-->", "");
    }

    // goctruyen
    private String GTC() {
        return Jsoup.parse(Conn.getHtml(url)).select("p#detailcontent").html();
    }

    // isach.info
    private String ISI() {
        String html = Conn.getHtml(url);
        html = html.substring(html.lastIndexOf("<div class=\"ms_chapter\">"),
                html.indexOf("<div class=\"navigator_bottom\">"));
        html = html.substring(html.indexOf("</div>") + 5, html.length());
        String chap = "";
        for (Element e : Jsoup.parse(html).select("div"))
            chap += e.text() + "\n";
        return chap.replaceAll("^(.) ", "$1");
    }

    // truyencv
    private String TCV() {
        return Jsoup.parse(Conn.getHtml(url)).select("div.chapter").html().replaceAll("\n|\r", "")
                .replaceAll("<script.*?</script>", "").replaceAll("<font.*?</font>", "");
    }

    //5book
    private String W5V() {
        return Jsoup.parse(Conn.getHtml(url)).select("div#chapter-content").html()
                .replaceAll("<h1.*?</h1>|<span.*?</span>", "\n");
    }

    // webtruyen
    private String WTC() {
        String html = Jsoup.parse(Conn.getHtml(url)).select("div#content").html();
        html = html.replace("\n", "").replaceAll("<div.*?</div>", "");
        html = html.replaceAll("<!--.*?-->", "");
        return html;
    }

    // bachngocsach/reader
    private String BNS() {
        return Jsoup.parse(Conn.getHtml(url)).select("div#noidung").html();
    }

    // hixx.info
    private String HIXX() {
        String html = Jsoup.parse(Conn.getHtml(url)).select("td.chi_tiet").html();
        return html.replaceAll("(?s)<div style.*?</div>", "");
    }

    //santruyen
    private String STC() {
        return Jsoup.parse(Conn.getHtml(url)).select("div.contents-comic").html();
    }

    // sstruyen
    private String SST() {
        try {
            // Lấy Cookie
            String cookieValue = "";

            URL url = new URL(this.url);
            URLConnection conn = url.openConnection();
            Map<String, List<String>> headerFields = conn.getHeaderFields();
            Set<String> headerFieldsSet = headerFields.keySet();
            Iterator<String> hearerFieldsIter = headerFieldsSet.iterator();
            while (hearerFieldsIter.hasNext()) {
                String headerFieldKey = hearerFieldsIter.next();
                if ("Set-Cookie".equalsIgnoreCase(headerFieldKey)) {
                    List<String> headerFieldValue = headerFields.get(headerFieldKey);
                    for (String headerValue : headerFieldValue) {
                        String[] fields = headerValue.split(";\\s*");
                        cookieValue = fields[0];
                    }
                }
            }

            // Lấy link
            conn.connect();
            InputStream is = conn.getInputStream();

            // Lấy dữ liệu sang bytearray
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            int c = 0;
            byte[] bf = new byte[4096];

            while ((c = is.read(bf)) > 0)
                dos.write(bf, 0, c);

            bf = bos.toByteArray();
            dos.close();
            bos.close();

            String s = new String(bf, "UTF-8");
            String nChaptId = search(s, "nChaptId.*?(\\d+)", 1);
            String szChapterTime = search(s, "szChapterTime.*?\"(.*?)\"", 1).replaceAll("\\D", "");
            s = "http://cf.sstruyen.com/doc-truyen/index.php?ajax=ct&id=" + nChaptId + "&t=" + szChapterTime;
            // Lấy nội dung
            Connection co = Jsoup.connect(s)
                    .header("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36")
                    .header("X-Requested-With", "XMLHttpRequest").header("Cookie", cookieValue).ignoreContentType(true);
            return Jsoup.parse(co.get().toString()).select("div").html();
        } catch (Exception e) {
            return null;
        }
    }

    // tuchangioi.net
    private String TCG() {
        return Jsoup.parse(Conn.getHtml(url)).select("div.text-truyen").html();
    }

    //truyenvl.net
    private String TVL() {
        return Jsoup.parse(Conn.getHtml(url)).select("div.chapter-content").html();
    }


    private String Optimize(String src) {
        src = src.replaceAll(" +", " ");
        src = src.replaceAll("<br>|<br/>|<br />|<p>|</p>", "\n")
                .replaceAll("<a .*?>|&nbsp;", "")
                .replaceAll(">\n+", ">")
                .replaceAll("\n+<", "<")
                .replaceAll("<span .*?</span>", "")
                .replaceAll("</{0,1}[a-zA-Z0-9]+>", "")
                .replaceAll(" +[\n\r]", "\n")
                .replaceAll("[\n\r]( +)", "\n")
                .replaceAll("[\n\r]+", "\n")
                .replaceAll("^\\s+|\\s+$", "");
        return src;
    }

    private String search(String source, String regex, int group) {
        try {
            Pattern r = Pattern.compile(regex, Pattern.MULTILINE);
            Matcher m = r.matcher(source);
            if (m.find())
                return m.group(group);
            else
                return "";
        } catch (Exception e) {
            return "";
        }
    }
}
