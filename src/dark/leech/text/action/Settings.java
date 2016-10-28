package dark.leech.text.action;


import dark.leech.text.models.Base64;
import dark.leech.text.models.FileAction;
import dark.leech.text.models.Trash;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.util.ArrayList;

import static dark.leech.text.constant.SettingConstants.*;

/**
 * Code by Darkrai on 8/23/2016.
 */
public class Settings {

    public Settings() {
        doLoad();
    }

    public void doLoad() {
        doLoad(true);
    }

    public void doLoad(boolean g) {
        doDefault();
        String content = new FileAction().file2string("setting.xml");
        if (content.length() != 0)
            load(content, g);
    }

    public void doDefault() {
        load(new FileAction().stream2string("/dark/leech/res/setting.xml"), true);
        WORKPATH = System.getProperty("user.dir");
        THEME_COLOR = new Color(38, 50, 56);
    }

    private void load(String content, boolean g) {
        Document text = Jsoup.parse(content);
        MAX_CONN = Integer.parseInt(text.select("integer#numConn").text());
        RECONN = Integer.parseInt(text.select("integer#reConn").text());
        DELAY = Integer.parseInt(text.select("integer#delay").text());
        TIMEOUT = Integer.parseInt(text.select("integer#timeout").text());

        //
        isCSS = !text.select("string#css").attr("value").equals("0");
        if (isCSS || g)
            CSS = Base64.decode(text.select("string#css").text());
        isHTML = !text.select("string#html").attr("value").equals("0");
        if (isHTML || g)
            HTML = Base64.decode(text.select("string#html").text());
        isTXT = !text.select("string#txt").attr("value").equals("0");
        if (isTXT || g)
            TXT = Base64.decode(text.select("string#txt").text());
        isDROP = !text.select("string#drop").attr("value").equals("0");
        if (isDROP || g)
            DROP = Base64.decode(text.select("string#drop").text());
        //
        WORKPATH = Base64.decode(text.select("string#workPath").text());
        CALIBRE = Base64.decode(text.select("string#calibrePath").text());
        KINDLEGEN = Base64.decode(text.select("string#kindlegenPath").text());
        ArrayList<Trash> trash = new ArrayList<Trash>();
        Elements el = text.select("string#trash models");
        for (Element e : el)
            trash.add(new Trash(Base64.decode(e.attr("src")), Base64.decode(e.attr("to")), Base64.decode(e.attr("tip")),
                    !e.attr("replace").equals("0")));
        TRASH = trash;
        String color = text.select("color#theme").text();
        if (color != null)
            if (color.length() > 0)
                THEME_COLOR = Color.decode(color);

    }
}
