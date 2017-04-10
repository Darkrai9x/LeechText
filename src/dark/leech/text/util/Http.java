package dark.leech.text.util;

import dark.leech.text.action.Log;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
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
            AppUtils.pause(SettingUtils.DELAY);
            return HttpConnection.connect(url)
                    .userAgent(SettingUtils.USER_AGENT)
                    .ignoreContentType(true)
                    .followRedirects(true)
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
