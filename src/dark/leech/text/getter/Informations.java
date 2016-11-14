package dark.leech.text.getter;

import dark.leech.text.models.Connect;
import dark.leech.text.models.Properties;
import dark.leech.text.models.Syntax;
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
            case "wikidich.com":
                WKD();
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
        properties.setGioiThieu(Optimize(doc.select("div.desc-text.desc-text-full").html()));
    }

    // truyencuatui.net
    private void TCT() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1.title").text());
        properties.setAuthor(doc.select("[itemprop=author]").text().replace("Tác giả:", ""));
        properties.setCover(doc.select("img.img-responsive.img-rounded").attr("src"), "http://truyencuatui.net");
        properties.setGioiThieu(Optimize(doc.select("div.contentt").html()));
    }

    // 5book.vn
    private void W5V() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1[itemprop=name]").text());
        properties.setAuthor(doc.select("div.tacgia a").first().text());
        properties.setCover(doc.select("img[itemprop=image]").attr("src"), "http://www.5book.vn");
        properties.setGioiThieu(Optimize(doc.select("article#gioithieu div div").html()));
    }

    // goctruyen.com
    private void GTC() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1 a").first().text());
        properties.setAuthor(doc.select("h2[itemprop=author] span").first().text());
        properties.setCover(doc.select("img[itemprop=image]").attr("src"), "http://goctruyen.com");
        properties.setGioiThieu(Optimize(doc.select("div#summary").html()));
    }

    // webtruyen.com
    private void WTC() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1 > a").text().trim());
        properties.setAuthor(doc.select("ul.w3-ul").first().select("a").first().text().trim());
        properties.setCover(doc.select("div.w3-col.s4.m12.l12.detail-thumbnail img").attr("src"), "http://webtruyen.com");
        properties.setGioiThieu(Optimize(doc.select("article").html()));
    }

    // hixx.info
    private void HIXX() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("div.title h1").text());
        properties.setAuthor(doc.select("div.truyen_info div.author a").first().text());
        properties.setCover(doc.select("div.image img").attr("src"), "http://hixx.info");
        properties.setGioiThieu(Optimize(doc.select("div.desc").html()));
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
        properties.setGioiThieu(Optimize(doc.select("div.desc-story").html()));
    }

    // sstruyen.com
    private void SST() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1.title").text());
        properties.setAuthor(doc.select("div.truyeninfo div ul li div.cp2").first().text());
        properties.setCover(doc.select("img[itemprop=image]").attr("src"), "http://sstruyen.com");
        properties.setGioiThieu(Optimize(doc.select("div.story_description").html()));
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
        properties.setGioiThieu(Optimize(Jsoup.parse(html).select("div.desc").html()));
    }

    // bachngocsach/reader
    private void BNS() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h1#truyen-title").text());
        properties.setAuthor(doc.select("div#tacgia a").text());
        properties.setCover(doc.select("div#anhbia img").attr("src"), "http://bachngocsach.com");
        properties.setGioiThieu(Optimize(doc.select("div#gioithieu").html()));
    }

    //tuchangioi
    private void TCG() {
        Elements doc = Jsoup.parse(html).select("div.book_info");
        properties.setName(doc.select("h1").text());
        properties.setAuthor(regex(doc.select("div.models-list").html(), "T.c gi.:\\s+(.*?)<", 1));
        properties.setGioiThieu(Optimize(doc.select("div.description").html()));

    }

    //truyenvl.net
    private void TVL() {
        Document doc = Jsoup.parse(html);
        properties.setName(doc.select("h3.title").text());
        properties.setAuthor(doc.select("div.info div a").first().text());
        properties.setCover(doc.select("div.book img").attr("src"), "http://truyenvl.net");
        properties.setGioiThieu(Optimize(doc.select("div.desc-text").html()));
    }

    //wikidich.com
    private void WKD() {
        Elements doc = Jsoup.parse(html).select("div.book-info");
        properties.setName(doc.select("div.cover-info h2").text());
        properties.setAuthor(regex(doc.html(), "/tac-gia/.*?\">(.*?)</a>", 1));
        properties.setCover(doc.select("div.cover-wrapper img").attr("src"), "http://wikidich.com");
        properties.setGioiThieu(Optimize(doc.select("div.book-desc").html().replaceAll("<a.*?</a>","")));
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
    private String Optimize(String src) {
        src = new Syntax().covertString(src);
        src = src.replaceAll("\\s+", " ");
        src = src.replaceAll("(</{0,1}div>|</{0,1}p>|<br.*?>|</br>)", "\n");
        src = src.replaceAll("<.*?>|<span.*?>|</span>|&nbsp;", "");
        src = src.replaceAll(" +[\n\r]", "\n");
        src = src.replaceAll("[\n\r]( +)", "\n");
        src = src.replaceAll("[\n\r]+", "\n");
        src = src.replaceAll("^\\s+|\\s+$", "");
        src = "<p>"+src.replace("\n", "</p>\n<p>")+"</p>";
        return src;
    }

}
