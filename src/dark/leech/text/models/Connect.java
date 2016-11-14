package dark.leech.text.models;

import dark.leech.text.constant.SettingConstants;
import org.jsoup.Jsoup;

public class Connect {
    public String getHtml(String url) {
        try {
            Thread.sleep(SettingConstants.DELAY);
            return Jsoup.connect(url)
                    .userAgent(SettingConstants.USER_AGENT)
                    .followRedirects(true)
                    .timeout(SettingConstants.TIMEOUT)
                    .get()
                    .toString();
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }

    public String postHtml(String url, String[] post) {
        try {
            return Jsoup.connect(url)
                    .userAgent(SettingConstants.USER_AGENT).data(post)
                    .ignoreContentType(true).timeout(SettingConstants.TIMEOUT)
                    .post()
                    .toString();
        } catch (Exception e) {
            return "";
        }
    }
}
