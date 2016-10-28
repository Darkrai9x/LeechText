package dark.leech.text.action.export;

import dark.leech.text.constant.Constants;
import dark.leech.text.constant.SettingConstants;
import dark.leech.text.models.Chapter;
import dark.leech.text.models.FileAction;
import dark.leech.text.models.Properties;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Long on 9/17/2016.
 */
public class TableOfContent {
    private int id;
    private Properties properties;
    private ArrayList<Chapter> chapList;
    private boolean autoSplit;
    private StringBuilder toc;
    private StringBuilder content;
    private StringBuilder muclucHtml;
    private ArrayList<String> partList;
    private ArrayList<String> namePartList;
    private boolean includeImg;


    public TableOfContent(Properties properties) {
        this.properties = properties;
        this.chapList = properties.getChapList();
        toc = new StringBuilder();
        content = new StringBuilder();
        muclucHtml = new StringBuilder();
        partList = new ArrayList<String>();
        namePartList = new ArrayList<String>();
    }

    public void setIncludeImg(boolean includeImg) {
        this.includeImg = includeImg;
    }

    public void setAutoSplit(boolean autoSplit) {
        this.autoSplit = autoSplit;
    }


    public ArrayList<Integer> splitPart() {
        ArrayList<Integer> part = new ArrayList<Integer>();
        int index, c1;
        //
        for (index = 0; index < chapList.size(); index++) {
            if (chapList.get(index).getPartName().length() != 0) {
                part.add(index);
                break;
            }
            if (search(chapList.get(index).getChapName(), "(Ch..ng \\d+)", 1).length() != 0) {
                part.add(index);
                break;
            }
        }
        c1 = index + 1;
        //Lấy quyển chia sẳn
        //Lấy các quyển tiếp
        int nextPart = 2;
        for (index = c1; index < chapList.size(); index++) {
            if (toInt(search(chapList.get(index).getPartName(), "(Q|Quy.n )(\\d+)", 2)) == nextPart) {
                part.add(index);
                nextPart++;
            }
        }
        if (part.size() > 1) return part;
        //Quyển không chia sẳn

        //Chia tự động theo c1
        for (index = c1; index < chapList.size(); index++)
            if (toInt(search(chapList.get(index).getChapName(), "Ch..ng\\s+(\\d+)", 1)) == 1) {
                if (toInt(search(chapList.get(index - 1).getChapName(), "Ch..ng (\\d+)", 1)) != 1)
                    part.add(index);
            }

        if (part.size() > 1) return part;
        //Chia 100c/Quyển
        if (chapList.size() < 300) return part;
        int partNum = 100;
        for (index = c1; index < chapList.size(); index++) {
            int i = Math.abs(partNum - toInt(search(chapList.get(index).getChapName(), "Ch..ng\\s+(\\d+)", 1)));
            if (i >= 0 && i <= 10) {
                int vt = findAround(index, 20, partNum);
                if (vt == -1)
                    continue;
                else index = vt;
                part.add(index);
                partNum += 100;
            }
        }
        return part;
    }

    //Tìm kiếm chương xung quanh
    private int findAround(int point, int range, int value) {
        for (int i = 0; i < range; i++)
            if (i + point < chapList.size())
                if (value < toInt(search(chapList.get(i + point).getChapName(), "Ch..ng (\\d+)", 1))) {
                    return i + point;
                }
        return -1;
    }

    private int toInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public void makeTableOfContent() {

        id = 1;
        chapList = properties.getChapList();
        muclucHtml.append("\n<h4>Mục lục</h4>\n");
        //Không chia quyển
        if (!autoSplit) {
            makePart(0, chapList.size(), "\t");
            saveData();
            return;
        }
        ArrayList<Integer> part = splitPart();
        if (part.size() < 2) {
            makePart(0, chapList.size(), "\t");
            saveData();
            return;
        }
        //Chia quyển
        makePart(0, part.get(0), "    ");
        for (int i = 0; i < part.size(); i++) {
            String namePart = chapList.get(part.get(i)).getPartName();
            if (namePart.length() == 0)
                namePart = "Quyển " + Integer.toString(i + 1);
            String s = "    <navPoint id=\"toc-" + Integer.toString(id) + "\" playorder=\"" + Integer.toString(id)
                    + "\">\n" + "      <navLabel>\n" + "        <text>" + namePart + "</text>\n"
                    + "      </navLabel>\n" + "      <content src=\"Text/Q" + Integer.toString(i + 1) + ".html\"/>\n";
            toc.append(s);
            content.append("\t" + "<models id=\"Q" + Integer.toString(i + 1) + "\" href=\"Text/Q" + Integer.toString(i + 1) + ".html\" media-type=\"application/xhtml+xml\"/>\n");
            namePartList.add(namePart);
            id++;
            if (i == part.size() - 1)
                makePart(part.get(i), chapList.size(), "      ");
            else
                makePart(part.get(i), part.get(i + 1), "      ");
            toc.append("    </navPoint>\n");
        }
        saveData();
    }

    private void makePart(int start, int end, String tab) {
        StringBuilder pa = new StringBuilder();
        for (int i = start; i < end; i++) {
            String s = tab + "<navPoint id=\"toc-" + Integer.toString(id) + "\" playorder=\"" + Integer.toString(id)
                    + "\">\n" + tab + "  <navLabel>\n" + tab + "    <text>" + chapList.get(i).getChapName() + "</text>\n"
                    + tab + "  </navLabel>\n" + tab + "  <content src=\"Text/" + Integer.toString(i) + ".html\"/>\n"
                    + tab + "</navPoint>\n";
            pa.append("<div class=\"toc-lv1\"><a href=\"../Text/" + Integer.toString(chapList.get(i).getId()) + ".html\">" + chapList.get(i).getChapName() + "</a></div>\n");
            content.append("\t" + "<models id=\"C" + Integer.toString(i) + "\" href=\"Text/" + Integer.toString(i) + ".html\" media-type=\"application/xhtml+xml\"/>\n");
            toc.append(s);
            id++;
        }
        partList.add(pa.toString());
    }

    private void saveData() {
        FileAction fileAction = new FileAction();
        //toc.ncx
        String ncx = fileAction.stream2string("/dark/leech/res/toc.ncx");
        ncx = ncx.replace("[NAME]", properties.getName())
                .replace("[AUTHOR]", properties.getAuthor())
                .replace("[NAVPOINT]", new String(toc));
        fileAction.string2file(ncx, properties.getSavePath() + Constants.l + "data" + Constants.l + "toc.ncx");
        //content.opf
        String opf = fileAction.stream2string("/dark/leech/res/content.opf");
        opf = opf.replace("[NAME]", properties.getName())
                .replace("[AUTHOR]", properties.getAuthor());
        Date todaysDate = new Date();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        opf = opf.replace("[DATE]", df.format(todaysDate));
        String manifest = new String(content);
        opf = opf.replace("[MANIFEST]", manifest)
                .replace("[IMAGE]", addImg());
        manifest = manifest.replaceAll("<item id=(.*?)\\s*href=.*?/>", "<itemref idref=$1/>");
        opf = opf.replace("[NCX]", manifest);
        fileAction.string2file(opf, properties.getSavePath() + Constants.l + "data" + Constants.l + "content.opf");
        //mucluc.html
        String head = SettingConstants.HTML;
        head = head.replaceAll("(?s)(.*?<body.*?>).*", "$1");
        if (!autoSplit) {
            muclucHtml.append(partList.get(0));
        } else {
            if (partList.get(0) != null)
                muclucHtml.append(partList.get(0));
            for (int i = 1; i < partList.size(); i++) {
                muclucHtml.append("<div class=\"toc-lv1\"><a href=\"../Text/Q" + Integer.toString(i) + ".html\">" + namePartList.get(i - 1) + "</a></div>\n");
                String Q = head;
                Q = Q.replaceAll("<title>.*?</title>", "<title>" + namePartList.get(i - 1) + "</title>");
                Q += "\n<h4>" + namePartList.get(i - 1) + "</h4>\n";
                Q += partList.get(i);
                Q += "</body>\n</html>";
                fileAction.string2file(Q, properties.getSavePath() + Constants.l + "data" + Constants.l + "Text" + Constants.l + "Q" + Integer.toString(i) + ".html");
            }
        }
        String mucluc = head.replaceAll("<title>.*?</title>", "<title>Mục lục</title>")
                + muclucHtml.toString()
                + "</body>\n</html>";
        fileAction.string2file(mucluc, properties.getSavePath() + Constants.l + "data" + Constants.l + "Text" + Constants.l + "mucluc.html");
    }

    private String addImg() {
        if (!includeImg) return "\n";
        StringBuilder img = new StringBuilder();
        File file = new File(properties.getSavePath() + Constants.l + "data" + Constants.l + "Images");
        String[] files = file.list();
        for (String fn : files) {
            if (fn.endsWith(".png"))
                img.append("<models id=\"" + fn + "\" href=\"Images/" + fn + "\" media-type=\"image/png\"/>\n");
            if (fn.endsWith(".jpg") || fn.endsWith(".jpeg"))
                img.append("<models id=\"" + fn + "\" href=\"Images/" + fn + "\" media-type=\"image/jpeg\"/>\n");
            if (fn.endsWith(".gif"))
                img.append("<models id=\"" + fn + "\" href=\"Images/" + fn + "\" media-type=\"image/gif\"/>\n");
        }
        return new String(img);
    }

    protected String search(String source, String regex, int group) {
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
