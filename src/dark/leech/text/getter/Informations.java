package dark.leech.text.getter;

import dark.leech.text.item.Connect;
import dark.leech.text.item.Properties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Informations {
    private Properties properties;
    private String url;
    private String html;
    private Connect Conn;

    public Informations(String url) {
        properties = new Properties();
        Conn = new Connect();
        this.url = url;
        try {
            html = Conn.getHtml(url);
            get();
        } catch (Exception e) {
        }
    }

    public Properties getProperties() {
        return properties;
    }

    private void get() throws Exception {
        properties.setForum(false);
        if (url.indexOf("bachngocsach.com/reader") != -1) {
            BNS();
            return;
        }
        switch (regex(url, "://(.*?)/", 1)) {
            case "truyenyy.com":
                TYY();
                return;
            case "truyenfull.vn":
                TFV();
                return;
            case "truyencuatui.net":
                TCT();
                return;
            case "5book.vn":
            case "www.5book.vn":
                W5V();
                return;
            case "goctruyen.com":
                GTC();
                return;
            case "webtruyen.com":
                WTC();
                return;
            case "truyen.hixx.info":
            case "convert.hixx.info":
                HIXX();
                return;
            case "santruyen.com":
                STC();
                return;
            case "sstruyen.com":
                SST();
                return;
            case "isach.info":
                ISI();
                return;
            case "truyencv.com":
                TCV();
                return;
            case "tuchangioi.net":
                TCG();
                return;
            case "truyenvl.net":
            case "www.truyenvl.net":
                TVL();
                return;
            default:
                break;
        }
        if (url.indexOf("bachngocsach.com/forum") != -1 || url.indexOf("tangthuvien.vn") != -1 || url.indexOf("banlong.us") != -1 || url.indexOf("4vn.eu") != -1) {
            FORUM();
            return;
        }
    }

    // progress
    // truyenyy.com
    private void TYY() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1").text());
        properties.setAuthor(doc.select("div.lww p span a").text());
        properties.setCover(doc.select("div.thumbnail img").attr("src"), "http://truyenyy.com");
    }

    // truyenfull.vn
    private void TFV() {
        try {
            html = Jsoup.connect(url)
                    .data(Double.toString(Math.random()), "").ignoreContentType(true).timeout(10000).get().toString();
        } catch (IOException e) {
            html = "";
        }
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h3.title").text());
        properties.setCover(doc.select("div.book img").attr("src"), "http://truyenfull.vn");
        properties.setAuthor(doc.select("div.info div a").first().text()); // Tác giả
    }

    // truyencuatui.net
    private void TCT() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1.title").text());
        properties.setAuthor(doc.select("[itemprop=author]").text().replace("Tác giả:", ""));
        properties.setCover(doc.select("img.img-responsive.img-rounded").attr("src"), "http://truyencuatui.net");
        System.out.println(properties.getCover());
    }

    // 5book.vn
    private void W5V() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1[itemprop=name]").text());
        properties.setAuthor(doc.select("div.tacgia a").first().text());
        properties.setCover(doc.select("img[itemprop=image]").attr("src"), "http://www.5book.vn");
    }

    // goctruyen.com
    private void GTC() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1.title a").first().text());
        properties.setAuthor(doc.select("div.author p strong").first().text());
        properties.setCover(doc.select("img.thumb").attr("src"), "http://goctruyen.com");
    }

    // webtruyen.com
    private void WTC() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1 > a").text().trim());
        properties.setAuthor(doc.select("ul.w3-ul").first().select("a").first().text().trim());
        properties.setCover(doc.select("div.w3-col.s4.m12.l12.detail-thumbnail img").attr("src"), "http://webtruyen.com");
    }

    // hixx.info
    private void HIXX() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("div.title h1").text());
        properties.setAuthor(doc.select("div.truyen_info div.author a").first().text());
        properties.setCover(doc.select("div.image img").attr("src"), "http://hixx.info");
    }

    // santruyen.com
    private void STC() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1 a").text());
        Elements el = doc.select("div.d-s-col").first().select("p");
        for (Element e : el) {
            String s1 = e.text().toLowerCase().replace(" ", "");
            if (s1.indexOf("tácgiả:") != -1)
                properties.setAuthor(e.text().replaceAll("[Tt]ác [Gg]iả:", "").trim());
        }
        properties.setCover(doc.select("div.detail-thumb img").attr("src"), "http://santruyen.com");
    }

    // sstruyen.com
    private void SST() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1.title").text());
        properties.setAuthor(doc.select("div.truyeninfo div ul li div.cp2").first().text());
        properties.setCover(doc.select("img[itemprop=image]").attr("src"), "http://sstruyen.com");
    }

    // isach.info
    private void ISI() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("div.ms_title a").text());
        properties.setAuthor(doc.select("div.ms_author a").first().text());
        properties.setCover(doc.select("img.ms_image").attr("src"), "http://isach.info");
    }

    // truyencv.com
    private void TCV() {
        properties.setName(Jsoup.parse(html).select("h1[itemprop=name]").text());
        properties.setAuthor(regex(html, "<strong>T.c gi.:</strong>(.*?)</p>", 1).replaceAll("<a.*?>|</a>", "").trim());
        properties.setCover(Jsoup.parse(html).select("img.poster.img-responsive").attr("src"),
                "http://truyencv.com");
    }

    // bachngocsach/reader
    private void BNS() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1#truyen-title").text());
        properties.setAuthor(doc.select("div#tacgia a").text());
        properties.setCover(doc.select("div#anhbia img").attr("src"), "http://bachngocsach.com");
    }

    //tuchangioi
    private void TCG() {
        Elements doc = Jsoup.parse(html).select("div.book_info");
        properties.setName(doc.select("h1").text());
        properties.setAuthor(regex(doc.select("div.item-list").html(), "T.c gi.:\\s+(.*?)<", 1));

    }

    //truyenvl.net
    private void TVL() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h3.title").text());
        properties.setAuthor(doc.select("div.info div a").first().text());
        properties.setCover(doc.select("div.book img").attr("src"), "http://truyenvl.net");
    }

    //banlong.us, tangthuvien, bacngocsach, 4vn
    private void FORUM() {
        String title = Jsoup.parse(html).select("title").text();
        properties.setName(title);
        properties.setForum(true);
    }

    private String regex(String s, String re, int group) {
        Pattern r = Pattern.compile(re, Pattern.MULTILINE);
        Matcher m = r.matcher(s);
        if (m.find())
            return m.group(group);
        else
            return "";
    }

}
