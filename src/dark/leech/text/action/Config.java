package dark.leech.text.action;

import dark.leech.text.constant.Constants;
import dark.leech.text.listeners.TableListener;
import dark.leech.text.models.Chapter;
import dark.leech.text.models.FileAction;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    private ArrayList<Chapter> chapList;
    private String path;
    private TableListener tableListener;


    public Config(ArrayList<Chapter> chapList) {
        this.chapList = chapList;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<Chapter> checkError() {
        ArrayList<Chapter> imgErr = new ArrayList<Chapter>();
        for (int i = 0; i < chapList.size(); i++)
            if (chapList.get(i).isError())
                imgErr.add(chapList.get(i));
        return imgErr;
    }

    public ArrayList<Chapter> checkImg() {
        ArrayList<Chapter> imgList = new ArrayList<Chapter>();
        for (int i = 0; i < chapList.size(); i++) {
            if (chapList.get(i).isImgChap())
                imgList.add(chapList.get(i));
        }
        return imgList;
    }

    public ArrayList<Chapter> checkName() {
        ArrayList<Chapter> nameList = new ArrayList<Chapter>();
        for (Chapter c : chapList)
            if (findMatchs(c.getChapName(), "^(Ch..ng|H.i) \\d+([\\+\\.-]\\d+|)") != 1)
                nameList.add(c);
            else if (findMatchs(c.getChapName(), "Ch..ng \\d+") > 1)
                nameList.add(c);
        return nameList;
    }

    private void splitPartName(Chapter chapter) {
        if (chapter.getPartName().length() > 1) return;
        String regex = "((Quy.n |Q.|Q)\\d+\\s*[:-](.*?)*)\\s*(([Cc]h..ng|Hồi)\\s+\\d+)";
        if (findMatchs(chapter.getChapName(), "(Quy.n |Q\\.|Q)\\d+([\\+\\.-]\\d+|)") == 1) {
            String partName = splitMatchs(chapter.getChapName(), regex, 1).replaceAll("\\s*[:-]\\s*$", "");
            String chapName = chapter.getChapName();
            chapName = chapName.replace(partName, "").replaceAll("^\\s*[:-]\\s*", "");
            partName = partName.replaceAll("Q\\.|Q(\\d+)", "Quyển $1");
            chapter.setChapName(chapName);
            chapter.setPartName(partName);
        }
    }

    public void autoFixName() {
        for (int i = 0; i < chapList.size(); i++) {
            Chapter chapter = new Chapter(chapList.get(i).getUrl(), chapList.get(i).getId(), chapList.get(i).getPartName(), chapList.get(i).getChapName());
            splitPartName(chapter);
            chapter.setChapName(fixName(chapter.getChapName()));
            chapter.setPartName(fixName(chapter.getPartName()));
            tableListener.updateData(i, chapter);
        }
    }

    private String fixName(String name) {
        if (name.length() == 0) return name;
        name = name.replaceAll("Chương \\d+\\s*[:-]\\s*(Chương \\d+.*?$)", "$1")
                .replaceAll("^Hồi (\\d+)", "Chương $1")
                .replaceAll("(\\d+) [Cc]h..ng", "Chương $1")
                .replaceAll("\\s+", " ")
                .replaceAll("Chương (\\d+)\\s*[-\\+]\\s*(\\d+)", "Chương $1+$2")
                .replaceAll("(Chương \\d+)\\s*[;:-]+\\s*", "$1: ")
                .replaceAll("(Chương \\d+\\+\\d+)\\s*[;:-]+\\s*", "$1: ");
        return name;
    }

    private String UpperFirst(String name) {
        name = name.toLowerCase();
        char[] ch = name.toCharArray();
        ch[0] = Character.toUpperCase(ch[0]);
        for (int i = 1; i < ch.length; i++) {
            if (Character.toLowerCase(ch[i - 1]) == Character.toUpperCase(ch[i - 1]))
                ch[i] = Character.toUpperCase(ch[i]);
        }
        return new String(ch);
    }

    public void Optimize() {
        for (int i = 0; i < chapList.size(); i++) {
            Chapter chapter = new Chapter(chapList.get(i).getUrl(), chapList.get(i).getId(), chapList.get(i).getPartName(), chapList.get(i).getChapName());
            chapter.setChapName(Optimize(chapter.getChapName()));
            chapter.setPartName(Optimize(chapter.getPartName()));
            tableListener.updateData(i, chapter);
        }
    }

    private String Optimize(String name) {
        if (name.length() == 0) return name;
        name = UpperFirst(name);
        name = fixName(name);
        return name;
    }

    public void downloadImg() {
        for (int i = 0; i < chapList.size(); i++) {
            downloadImg(chapList.get(i));
            tableListener.updateData(i, chapList.get(i));
        }
    }

    private void downloadImg(Chapter chapter) {
        FileAction fa = new FileAction();
        String text = fa.file2string(path + Constants.l + "raw" + Constants.l + chapter.getId() + ".txt");
        ArrayList<String> imgList = new ArrayList<String>();
        Pattern r = Pattern.compile("<img.*?src=\"(.*?)\"", Pattern.MULTILINE);
        Matcher m = r.matcher(text);
        while (m.find())
            imgList.add(m.group(1));
        for (int i = 0; i < imgList.size(); i++) {
            String imgPath = imgList.get(i);
            if (!imgPath.startsWith("http")) continue;
            String img = imgPath.substring(imgPath.lastIndexOf("."), imgPath.length()).toLowerCase(); //TÁch đuôi
            text = text.replace(imgPath, "../Images/" + Integer.toString(chapter.getId()) + "_" + Integer.toString(i) + img).replace("\">", "\"/>");
            img = path + Constants.l + "data" + Constants.l + "Images" + Constants.l + Integer.toString(chapter.getId()) + "_" + Integer.toString(i) + img;
            fa.url2file(imgList.get(i), img);
        }
        fa.string2file(text, path + Constants.l + "raw" + Constants.l + chapter.getId() + ".txt");
    }

    private int findMatchs(String src, String regex) {
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(src);
        int match = 0;
        while (m.find()) {
            match++;
        }
        return match;
    }

    private String splitMatchs(String src, String regex, int group) {
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(src);
        String math = "";
        if (m.find())
            math = m.group(group);
        return math;
    }

    public void addTableListener(TableListener tableListener) {
        this.tableListener = tableListener;
    }
}
