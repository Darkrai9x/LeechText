package dark.leech.text.item;

import dark.leech.text.constant.SettingConstants;
import org.jsoup.Jsoup;

public class Connect {
    public String getHtml(String url) {
        try {
            Thread.sleep(SettingConstants.DELAY);
            return Jsoup.connect(url)
                    .userAgent(
                            "Mozilla")
                    .followRedirects(true).timeout(SettingConstants.TIMEOUT).get().toString();
        } catch (Exception e) {
            return "";
        }
    }

    public String postHtml(String url, String[] post) {
        try {
            return Jsoup.connect(url)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)  Chrome/32.0.1700.107 Safari/537.36")
                    .data(post)
                    .ignoreContentType(true).timeout(SettingConstants.TIMEOUT).post().toString();
        } catch (Exception e) {
            return "";
        }
    }
}
