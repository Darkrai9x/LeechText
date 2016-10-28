package dark.leech.text.action;

import dark.leech.text.constant.Constants;
import dark.leech.text.item.Chapter;
import dark.leech.text.item.FileAction;
import dark.leech.text.item.Properties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class History extends FileAction {
    private Properties newProperties;
    private Properties oldProperties;


    public Properties load(String path) {
        if (!(new File(path).exists())) return null;
        Document doc = Jsoup.parse(file2string(path));
        Properties properties = new Properties();
        properties.setName(doc.select("string#name").text());
        properties.setAuthor(doc.select("string#author").text());
        properties.setUrl(doc.select("string#url").text());
        properties.setCover(doc.select("string#cover").text(), "");
        properties.setSize(Integer.parseInt(doc.select("string#size").text()));
        properties.setSavePath(doc.select("string#path").text());

        Elements el = doc.select("list chapter");
        ArrayList<Chapter> list = new ArrayList<Chapter>();
        for (Element e : el)
            list.add(new Chapter(e.select("url").text(), Integer.parseInt(e.attr("id")), e.select("part").text(), e.select("name").text(),
                    true, true, Boolean.parseBoolean(e.select("error").text())));
        properties.setChapList(list);
        return properties;
    }

    public boolean parse(Properties newProperties) {
        this.newProperties = newProperties;
        oldProperties = load(newProperties.getSavePath() + Constants.l + "properties.xml");
        if (oldProperties == null) return false;
        if (oldProperties.getSize() < newProperties.getSize()) return true;
        else return false;
    }

    public Properties getProperties() {
        return newProperties;
    }


    public void overwrite() {
        ArrayList<Chapter> oldList = oldProperties.getChapList();
        ArrayList<Chapter> newList = newProperties.getChapList();

        for (int i = 0; i < oldList.size(); i++)
            newList.set(i, oldList.get(i));
        newProperties.setChapList(newList);
        newProperties.setSize(newList.size());
    }

    public void save(Properties properties) {
        File file = new File(properties.getSavePath() + Constants.l + "properties.xml");
        if (file.exists()) file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
        }
        add2file("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n", file);
        add2file(genItem("url", properties.getUrl(), "  "), file);
        add2file(genItem("name", properties.getName(), "  "), file);
        add2file(genItem("author", properties.getAuthor(), "  "), file);
        add2file(genItem("cover", properties.getCover(), "  "), file);
        add2file(genItem("size", Integer.toString(properties.getSize()), "  "), file);
        add2file(genItem("path", properties.getSavePath(), "  "), file);

        // danh sách chương
        add2file("  <list>\n", file);
        ArrayList<Chapter> list = properties.getChapList();
        for (Chapter c : list)
            add2file(genItem(Integer.toString(c.getId()),
                    c.getUrl(), c.getPartName(),
                    c.getChapName(),
                    c.isError(),
                    "    "),
                    file);
        add2file("  </list>\n</resource>", file);
    }

    private String genItem(String id, String text, String tab) {
        return tab + "<string" + " id=\"" + id + "\">" + text + "</string>\n";
    }

    private String genItem(String id, String url, String part, String chap, boolean status, String tab) {
        return tab + "<chapter" + " id=\"" + id + "\">\n"
                + tab + "  <error>" + Boolean.toString(status) + "</error>\n"
                + tab + "  <part>" + part + "</part>\n"
                + tab + "  <name>" + chap + "</name>\n"
                + tab + "  <url>" + url + "</url>\n"
                + tab + "</chapter>\n";
    }
}


