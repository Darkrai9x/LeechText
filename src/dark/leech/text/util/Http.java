package dark.leech.text.util;

import dark.leech.text.action.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Dark on 1/12/2017.
 */
public class Http {
    private Http() {
    }

    public static Connection connect(String url) {
        try {
            String cookies = CookiesUtils.getCookies(url);
            if (cookies == null)
                return Jsoup.connect(url)
                        .userAgent(SettingUtils.USER_AGENT)
                        .ignoreContentType(true)
                        .followRedirects(true)
                        .maxBodySize(0)
                        .timeout(SettingUtils.TIMEOUT);
            else return Jsoup.connect(url)
                    .userAgent(SettingUtils.USER_AGENT)
                    .header("Cookie", cookies)
                    .ignoreContentType(true)
                    .followRedirects(true)
                    .maxBodySize(0)
                    .timeout(SettingUtils.TIMEOUT);
        } catch (Exception e) {
            Log.add(e);
            return null;
        }
    }


    public static Document get(String url) {
        try {
            return connect(url).get();
        } catch (IOException e) {
            Log.add(e);
            return null;
        }
    }


}
