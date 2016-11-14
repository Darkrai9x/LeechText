package dark.leech.text.action.export;

import dark.leech.text.constant.Constants;
import dark.leech.text.constant.SettingConstants;
import dark.leech.text.constant.TypeConstants;
import dark.leech.text.listeners.ProgressListener;
import dark.leech.text.models.*;

import java.util.ArrayList;

/**
 * Created by Long on 9/17/2016.
 */
public class Text extends Syntax {
    private Properties properties;
    private int type;
    private boolean makeToc;
    private int tach;
    private ProgressListener progressListener;
    private String syntax;
    private FileAction fileAction;

    public Text(Properties properties, int type, boolean makeToc, int tach) {
        this.properties = properties;
        this.type = type;
        this.makeToc = makeToc;
        this.tach = tach;
        fileAction = new FileAction();
    }

    public void export() {
        if (type == TypeConstants.HTML) {
            syntax = SettingConstants.HTML;
            exportTach(".html");
        } else {
            syntax = SettingConstants.TXT;
            exportTach(".txt");
        }


    }

    public void exportTach(String duoi) {
        ArrayList<Chapter> chapList = properties.getChapList();
        StringBuffer gop = new StringBuffer();
        //Tạo file gộp tach == 1

        if (tach == 1) {

            if (type == TypeConstants.HTML) {
                String head = syntax;
                head = head.replaceAll("<title>.*?</title>", "<title>" + properties.getName() + "</title>");
                head = head.replaceAll("(?s)(.*?<body.*?>).*", "$1");
                gop.append(head);
            }
        }
        if (properties.isAddGt()) {
            Chapter gt = new Chapter();
            gt.setChapName("Giới Thiệu");
            gt.setID("gioithieu");
            gt.setGet(true);
            String text = "";
            try {
                text = chapterReplace(gt, false);
            } catch (Exception e) {
            }
            if (tach == 0) {
                String savePath = properties.getSavePath() + Constants.l + "data" + Constants.l + "Text" + Constants.l + gt.getID() + duoi;
                fileAction.string2file(text, savePath);
            } else {
                if (type == TypeConstants.HTML)
                    text = text.replaceAll("(?s).*?<body.*?>(.*?)</body>.*", "$1");
                else text += "\n\n";
                gop.append(text);
            }
        }
        //Tạo mục lục
        if (makeToc && type == TypeConstants.HTML) {
            String toc = "\n<h4>Mục lục</h4>\n";
            if (tach == 0) {
                if (properties.isAddGt())
                    toc += "<div class=\"toc-lv2\"><a href=\"../Text/gioithieu.html\">Giới Thiệu</a></div>\n";
                for (Chapter ch : chapList)
                    if (ch.isGet())
                        toc += "<div class=\"toc-lv2\"><a href=\"../Text/" + Integer.toString(ch.getId()) + ".html\">" + (ch.getPartName().length() == 0 ? "" : ch.getPartName() + " - ") + ch.getChapName() + "</a></div>\n";
                String head = syntax;
                head = head.replaceAll("<title>.*?</title>", "<title>" + properties.getName() + "</title>");
                head = head.replaceAll("(?s)(.*?<body.*?>).*", "$1");
                toc = head + toc + "</body>\n</html>";
                fileAction.string2file(toc, properties.getSavePath() + Constants.l + "data" + Constants.l + "Text" + Constants.l + "mucluc.html");

            } else {
                if (properties.isAddGt())
                    toc += "<div class=\"toc-lv2\"><a href=\"#toc-gioithieu\">Giới Thiệu</a></div>\n";
                for (Chapter ch : chapList) {
                    if (ch.isGet())
                        toc += "<div class=\"toc-lv2\"><a href=\"#toc-" + Integer.toString(ch.getId()) + "\">" + (ch.getPartName().length() == 0 ? "" : ch.getPartName() + " - ") + ch.getChapName() + "</a></div>\n";
                }
                gop.append(toc);
            }
        }
        //Thay thế text
        int value = 0;

        for (Chapter ch : chapList) {
            if (ch.isGet() && ch.isCompleted()) {
                String text = "";
                try {
                    text = chapterReplace(ch);
                } catch (Exception e) {
                }
                if (tach == 0) {
                    String savePath = properties.getSavePath() + Constants.l + "data" + Constants.l + "Text" + Constants.l + Integer.toString(ch.getId()) + duoi;
                    fileAction.string2file(text, savePath);
                } else {
                    if (type == TypeConstants.HTML)
                        text = text.replaceAll("(?s).*?<body.*?>(.*?)</body>.*", "$1");
                    else text += "\n\n";
                    gop.append(text);
                }
                value++;
                if (progressListener != null) progressListener.setProgress(value * 100 / properties.getSize(), "");
            }
        }
        if (tach == 1) {
            if (type == TypeConstants.HTML)
                gop.append("</body>\n</html>");
            fileAction.string2file(gop.toString(), properties.getSavePath() + Constants.l + "out" + Constants.l + "text" + duoi);
        }

    }

    private String clearText(String text) {
        for (Trash tr : SettingConstants.TRASH)
            if (tr.isReplace()) {
                String src = tr.getSrc().replace("\\n", "\n").replace("\\r", "\r").replace("\\t", "\t");
                String to = tr.getTo().replace("\\n", "\n").replace("\\r", "\r").replace("\\t", "\t");
                text = text.replaceAll(src, to);
            }
        return text;
    }

    private String chapterReplace(Chapter chapter) throws Exception {
        return chapterReplace(chapter, true);
    }

    private String chapterReplace(Chapter chapter, boolean pp) throws Exception {
        String nd = fileAction.file2string(properties.getSavePath() + Constants.l + "raw" + Constants.l + chapter.getID() + ".txt");
        nd = clearText(nd);
        String text = syntax;
        //
        text = replaceString(text, properties.getName(), "\\[1\\]", "NAME");
        text = replaceString(text, properties.getAuthor(), "\\[2\\]", "AUTHOR");
        text = replaceString(text, chapter.getPartName(), "\\[3\\]", "NAME_PART");
        text = replaceString(text, chapter.getChapName(), "\\[4\\]", "NAME_CHAP");
        text = text.replace("[ID]", chapter.getID());
        //replace paragraph
        String firstTag = search(syntax, "\\[5\\](.*)\\[PARAGRAPH\\](.*)\\[5\\]", 1);
        String lastTag = search(syntax, "\\[5\\](.*)\\[PARAGRAPH\\](.*)\\[5\\]", 2);
        nd = replaceDrop(nd);
        if (pp) {
            nd = nd.replace("\n", lastTag + "\n" + firstTag);
            nd = firstTag + nd + lastTag;
        }
        text = text.replace(search(text, "(\\[5\\].*\\[PARAGRAPH\\].*\\[5\\])", 1), nd);

        return text;
    }

    private String replaceString(String src, String replace, String tag, String key) {
        if (replace == null || replace.length() < 2)
            return src.replaceAll("\\s*" + tag + ".*?" + tag, "");
        src = src.replaceAll(tag + "(.*?" + "\\[" + key + "\\]" + ".*?)" + tag, "$1").replace("[" + key + "]", replace);
        return src;
    }

    private String replaceDrop(String text) {
        if (text.length() == 0) return text;
        char drop = text.charAt(0);
        String newt;
        if (SettingConstants.isDROP && type == TypeConstants.HTML)
            if (drop != Character.toLowerCase(drop)) {
                newt = (SettingConstants.DROP).replace("[DROP]", String.valueOf(drop));
                text = newt + text.substring(1, text.length());
            }
        return text;
    }

    public void addProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

}
