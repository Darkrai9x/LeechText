package dark.leech.text.action;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Long on 9/7/2016.
 */
public class Login {
    private Map<String, String> cookies;

    public void doLogin(String login, String password) {

    }

    public Map<String, String> getCookies(String login, String password) {
        try {
            return Jsoup.connect("http://banlong.us/login/login")
                    .header("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36")
                    .data("login", login)
                    .data("password", password)
                    .header("X-Requested-With", "XMLHttpRequest").ignoreContentType(true)
                    .method(Connection.Method.POST).execute().cookies();
        } catch (IOException e) {
            return null;
        }
    }
}
