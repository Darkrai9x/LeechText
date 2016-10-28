package dark.leech.text.getter;

import dark.leech.text.item.Chapter;
import dark.leech.text.item.Connect;
import dark.leech.text.item.Pager;
import dark.leech.text.item.Syntax;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class List extends Syntax {
    private String url;
    private ArrayList<Chapter> chapList;
    private ArrayList<Pager> pageList;
    private Connect Conn;

    public List(String url) {
        this.url = url;
        Conn = new Connect();
        chapList = new ArrayList<Chapter>();
        pageList = new ArrayList<Pager>();
        try {
            get();
        } catch (Exception e) {
        }
        if (chapList.size() != 0)
            for (int i = 0; i < chapList.size(); i++)
                chapList.get(i).setId(i);
        else for (int i = 0; i < pageList.size(); i++)
            pageList.get(i).setId(i);
    }

    public ArrayList<Chapter> getChapList() {
        return chapList;
    }

    public ArrayList<Pager> getPageList() {
        return pageList;
    }

    private void get() throws Exception {
        switch (search(url, "://(.*?)/", 1)) {
            case "truyenyy.com":
                TYY();
                break;
            case "truyenfull.vn":
                TFV();
                break;
            case "truyencuatui.net":
                TCT();
                break;
            case "5book.vn":
            case "www.5book.vn":
                W5V();
                break;
            case "goctruyen.com":
                GTC();
                break;
            case "webtruyen.com":
                WTC();
                break;
            case "truyen.hixx.info":
            case "convert.hixx.info":
                HIXX();
                break;
            case "santruyen.com":
                STC();
                break;
            case "sstruyen.com":
                SST();
                break;
            case "isach.info":
                ISI();
                break;
            case "truyencv.com":
                TCV();
                break;
            case "tuchangioi.net":
                TCG();
                break;
            case "truyenvl.net":
            case "www.truyenvl.net":
                TVL();
                break;
            case "wikidich.com":
                WKD();
                break;
            default:
                if (url.indexOf("bachngocsach.com/reader") != -1)
                    BNS();
                if (url.indexOf("banlong.us") != -1)
                    BLU();
                if (url.indexOf("tangthuvien.vn") != -1)
                    TTV();
                break;
        }
    }

    // progress

    // truyenyy
    private void TYY() {

        String html = Conn.getHtml(url);
        Elements te = Jsoup.parse(html).select("div.paging div ul li a");
        int total_page = Integer.parseInt(te.get(te.size() - 2).text());
        System.out.println(total_page);
        // Get link
        for (int i = 1; i <= total_page; i++) {
            html = Conn.getHtml(url + "/?page=" + Integer.toString(i));
            Elements el = Jsoup.parse(html).select("div.chaplist div a");
            for (Element e : el) {
                chapList.add(new Chapter(e.attr("href"), fixName(e.text())));
            }

        }
    }

    // truyencv
    private void TCV() {

        String html = Conn.getHtml(url);
        Elements el = Jsoup.parse(html).select("div#table-cm p.latestchaper a");
        for (Element e : el)
            chapList.add(new Chapter(e.attr("href"), fixName(search(e.text(), "\\d+ - (.*)", 1))));
        String pos = "";
        try {
            pos = Jsoup.parse(html).select("a.btn.btn-sm.btn-read.pull-right").last().attr("onclick");
        } catch (Exception eh) {
        }
        if (pos.length() == 0)
            return;
        String[] st = pos.split("'");
        html = Conn.postHtml("http://truyencv.com/index.php", new String[]{"showChapter", "1", "media_id", st[1], "number", st[3], "page", st[5], "type", st[7]});
        el = Jsoup.parse(html).select("div#table-cm p.latestchaper a");
        for (Element e : el)
            chapList.add(new Chapter(url + "/" + search(e.attr("href"), "http://truyencv.com/(.*)/(.*)/", 2),
                    fixName(search(e.text(), "\\d+ - (.*)", 1))));

    }

    // Truyenfull
    private void TFV() {

        String html = null;
        try {
            html = Jsoup.connect(url)
                    .data(Double.toString(Math.random()), "").ignoreContentType(true).timeout(10000).get().toString();
        } catch (IOException e) {
            html = "";
        }
        int total_page = Integer.parseInt(Jsoup.parse(html).select("input#total-page").attr("value"));
        //int total_page = 2;
        // Get link
        for (int i = 1; i <= total_page; i++) {
            try {
                html = Jsoup.connect(url + "/trang-" + Integer.toString(i))
                        .data(Double.toString(Math.random()), "").ignoreContentType(true).timeout(10000).get().toString();
            } catch (IOException e) {
                html = "";
            }
            Elements el = Jsoup.parse(html).select("div#list-chapter div.row ul li a");
            for (Element e : el)
                chapList.add(new Chapter(e.attr("href"), fixName(e.text())));
        }
    }

    // webtruyen.com
    private void WTC() {

        String html = Conn.getHtml(url);
        String lastPage = Jsoup.parse(html).select("ul.w3-pagination.paging li").last().select("a").attr("title").replace("page-", "");
        int total_page = 1;
        if (lastPage.length() != 0)
            total_page = Integer.parseInt(lastPage);

        for (int i = 1; i <= total_page; i++) {
            html = Conn.getHtml(url + "/" + Integer.toString(i));
            Elements el = Jsoup.parse(html).select("div#divtab ul li h4 a");
            for (Element e : el) {
                chapList.add(new Chapter(e.attr("href"), e.text()));
            }
        }
    }

    // Goctruyen.com
    private void GTC() {
        String html = Conn.getHtml(url);
        int total_page = Integer.parseInt(Jsoup.parse(html).select("input#hdlpage").attr("value"));
        for (int i = 2; i <= total_page; i++) {
            html = Conn.getHtml(url + "/" + Integer.toString(i));
            Elements el = Jsoup.parse(html).select("table").first().select("tbody tr");
            for (int j = 1; j < el.size(); j++) {
                String nameChap = el.get(j).select("td a").text();
                nameChap = (nameChap.length() == 0) ? "" : (": " + nameChap);
                nameChap = nameChap.replaceAll("Chương (\\d+)", "");
                chapList.add(new Chapter(el.get(j).select("td a").attr("href"), fixName(
                        el.get(j).select("td.text-left").text().replace("C.", "Chương") + nameChap)));

            }
        }
    }

    // isach.info
    private void ISI() {

        String html = Conn.getHtml(url + "&chapter=0000");
        html = html.substring(html.indexOf("<div id=\"c0000"), html.indexOf("<div class=\"navigator_bottom\">"));
        Elements el = Jsoup.parse(html).select("[class=right_menu_item] > a");
        for (Element e : el) {
            String linkChap = e.attr("href");
            linkChap = (linkChap.startsWith("http") ? linkChap : ("http://isach.info/" + linkChap));
            chapList.add(new Chapter(linkChap, fixName(e.text())));
        }
    }

    // Bachngocsach/Reader
    private void BNS() {

        String html = Conn.getHtml(url + "/muc-luc");
        Elements el = Jsoup.parse(html).select("div.mucluc-chuong a");
        for (Element e : el) {
            String linkChap = e.attr("href");
            linkChap = (linkChap.startsWith("http") ? linkChap : ("http://bachngocsach.com" + linkChap));
            chapList.add(new Chapter(linkChap, fixName(e.text())));
        }
        String page = Jsoup.parse(html).select("li.pager-last.last a").attr("href");
        if (page.length() == 0)
            return;
        int total_page = Integer.parseInt(page.substring(page.indexOf("page=") + 5, page.length()));
        for (int i = 1; i <= total_page; i++) {
            html = Conn.getHtml(url + "/muc-luc?page=" + Integer.toString(i));
            el = Jsoup.parse(html).select("div.mucluc-chuong a");
            for (Element e : el) {
                String linkChap = e.attr("href");
                linkChap = (linkChap.startsWith("http") ? linkChap : ("http://bachngocsach.com" + linkChap));
                chapList.add(new Chapter(linkChap, fixName(e.text())));
            }
        }
    }

    // truyencuatui
    private void TCT() {

        String html = Conn.getHtml(url);
        html = Jsoup.parse(html).select("ul.pagination li a").last().attr("href");
        int total_page = Integer.parseInt(html.substring(html.indexOf("page=") + 5, html.length()));
        // System.out.println(total_page);
        for (int i = 1; i <= total_page; i++) {
            Elements el = Jsoup.parse(Conn.getHtml(url + "?page=" + Integer.toString(i)))
                    .select("div#danh-sach-chuong div").first().select("a");
            for (Element e : el) {
                String linkChap = e.attr("href");
                linkChap = (linkChap.startsWith("http") ? linkChap : ("http://truyencuatui.net" + linkChap));
                chapList.add(new Chapter(linkChap, fixName(e.text())));
            }
        }
    }

    // 5book
    private void W5V() {

        String html = Conn.getHtml(url + "/chapter");
        Elements el = Jsoup.parse(html).select("li.ellipsis a");
        for (Element e : el) {
            String linkChap = e.attr("href");
            linkChap = (linkChap.startsWith("http") ? linkChap : ("http://www.5book.vn" + linkChap));
            chapList.add(new Chapter(linkChap, fixName(e.text())));
        }
        String page = "";
        if (html.indexOf("page-first-last") != -1)
            page = Jsoup.parse(html).select("a.page-first-last").first().attr("rel");
        if (page.length() == 0)
            return;
        int total_page = Integer.parseInt(page);
        for (int i = 2; i <= total_page; i++) {
            html = Conn.getHtml(url + "/chapter?p=" + Integer.toString(i));
            el = Jsoup.parse(html).select("li.ellipsis a");
            for (Element e : el) {
                String linkChap = e.attr("href");
                linkChap = (linkChap.startsWith("http") ? linkChap : ("http://www.5book.vn" + linkChap));
                chapList.add(new Chapter(linkChap, fixName(e.text())));
            }
        }
    }

    // HIXX
    private void HIXX() {

        String html = Conn.getHtml(url);
        int total_page = 1;
        if (html.indexOf("class=\"bt_pagination\">") != -1) {
            String page = Jsoup.parse(html).select("[class=bt_pagination]").first().html();
            total_page = Integer.parseInt(search(page, "Trang 1(.*?)(\\d+)", 2));
        }

        for (int i = 1; i <= total_page; i++) {
            html = Conn.getHtml(url.replace(".html", "/index" + Integer.toString(i) + ".html"));
            html = html.substring(html.indexOf("class=\"bt_pagination\">") + 22,
                    html.lastIndexOf("<div class=\"bt_pagination\">"));
            Elements el = Jsoup.parse(html).select("div.danh_sach a");
            for (Element e : el) {
                chapList.add(
                        new Chapter(e.attr("href"), fixName(e.text().replaceAll("Đọc truyện Online -|--\\d+--", "").trim())));
                // System.out.println(e.attr("href"));
            }
        }
    }

    // sstruyen
    private void SST() {
        String linkChap = "";
        String html = Conn.getHtml(url);
        int vt = html.lastIndexOf("#chaplist\">") + 11;
        String tt = html.substring(vt, html.indexOf("<", vt));
        int total_page = 0;
        try {
            total_page = Integer.parseInt(tt);
        } catch (Exception e) {
        }
        Elements el = Jsoup.parse(html).select("div.chuongmoi").last().select("div a");
        for (Element e : el) {
            linkChap = e.attr("href");
            linkChap = (linkChap.startsWith("http") ? linkChap : ("http://sstruyen.com" + linkChap));
            chapList.add(new Chapter(linkChap, fixName(e.attr("title"))));
        }
        String u = linkChap.replaceAll("http://sstruyen.com/doc-truyen/(.*?)/(.*?)/(.*?)/(.*?).html", "doc-truyen/$1");

        for (int i = 1; i < total_page; i++) {
            html = Conn
                    .getHtml(url.replace(".html", "/page-" + Integer.toString(i) + ".html").replace("doc-truyen", u));
            el = Jsoup.parse(html).select("div.chuongmoi").last().select("div a");
            for (Element e : el) {
                linkChap = e.attr("href");
                linkChap = (linkChap.startsWith("http") ? linkChap : ("http://sstruyen.com" + linkChap));
                chapList.add(new Chapter(linkChap, fixName(e.attr("title"))));
            }
        }
    }

    // santruyen
    private void STC() {

        String html = Conn.getHtml(url);
        Elements el = Jsoup.parse(html).select("div.list-chap-wrap div.list-chap ul p a");
        for (Element e : el) {
            String linkChap = e.attr("href");
            linkChap = (linkChap.startsWith("http") ? linkChap : ("http://santruyen.com" + linkChap));
            chapList.add(new Chapter(linkChap, fixName(e.text())));
        }
    }

    //tuchangioi
    private void TCG() {
        String html = Conn.getHtml(url);
        Elements el = Jsoup.parse(html).select("div.pagination.pagination-centered ul li");
        int totalPage = Integer.parseInt(el.get(el.size() - 2).select("a").text());
        for (int i = 1; i <= totalPage; i++) {
            html = Conn.getHtml(url + "/?page=" + Integer.toString(i));
            el = Jsoup.parse(html).select("div.chap-list div.ip5 a");
            for (Element e : el) {
                String linkChap = e.attr("href");
                linkChap = (linkChap.startsWith("http") ? linkChap : ("http://tuchangioi.net" + linkChap));
                chapList.add(new Chapter(linkChap, fixName(e.text())));
            }
        }

    }

    //truyenvl.net
    private void TVL() {
        String html = Conn.getHtml(url);
        int totalPage = Integer.parseInt(Jsoup.parse(html).select("input#total-page").attr("value"));
        for (int i = 1; i <= totalPage; i++) {
            html = Conn.getHtml(url + "/trang-" + Integer.toString(i));
            Elements el = Jsoup.parse(html).select("div#list-chapter div.row ul li");
            for (Element e : el) {
                String linkChap = e.select("a").attr("href");
                linkChap = (linkChap.startsWith("http") ? linkChap : ("http://truyenvl.net" + linkChap));
                chapList.add(new Chapter(linkChap, fixName(e.select("a").text())));
            }
        }
    }

    //wikidich.com
    private void WKD() {
        String html = Conn.getHtml(url);
        Elements el = Jsoup.parse(html).select("div.row ul li a");
        for (Element e : el) {
            String linkChap = e.select("a").attr("href");
            linkChap = (linkChap.startsWith("http") ? linkChap : ("http://wikidich.com" + linkChap));
            chapList.add(new Chapter(linkChap, fixName(e.select("a").text())));
        }
    }

    //forum
    //banlong.us
    private void BLU() {
        String html = Conn.getHtml(url);
        int totalPage = Integer.parseInt(Jsoup.parse(html).select("div[class=PageNav]").last().attr("data-last"));
        for (int i = 1; i <= totalPage; i++)
            pageList.add(new Pager(url + "/page-" + Integer.toString(i)));

    }

    //bachngocsach/forum
    private void BNF() {
        String html = Conn.getHtml(url);
        int totalPage = Integer.parseInt(Jsoup.parse(html).select("div[class=PageNav]").last().attr("data-last"));
        for (int i = 1; i <= totalPage; i++)
            pageList.add(new Pager(url + "/page-" + Integer.toString(i)));

    }

    //tangthuvien
    private void TTV() {
        String html = Conn.getHtml(url);
        String pageLast = Jsoup.parse(html).select("[class=first_last]").last().select("a").attr("href");
        int totalPage = Integer.parseInt(pageLast.replaceAll(".*page=(\\d+).*", "$1"));
        for (int i = 1; i <= totalPage; i++)
            pageList.add(new Pager(url + "&page=" + Integer.toString(i)));
    }

}
