package dark.leech.text.plugin;

import dark.leech.text.action.Log;
import dark.leech.text.ui.notification.Toast;
import dark.leech.text.util.AppUtils;
import dark.leech.text.util.FileUtils;
import dark.leech.text.util.Http;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Dark on 2/24/2017.
 */
public class PluginUpdate {
    private static final String URL = "https://onedrive.live.com/download?cid=BE45FD5F0AA9EF73&resid=BE45FD5F0AA9EF73%211197&authkey=AGigJWKcZMfwZow";
    private static PluginUpdate pluginUpdate;

    private PluginUpdate() {

    }

    public static PluginUpdate getUpdate() {
        if (pluginUpdate == null) pluginUpdate = new PluginUpdate();
        return pluginUpdate;
    }


    public void checkUpdate() {
        try {
            String js = new String(Http.connect(URL)
                    .execute()
                    .bodyAsBytes(), "UTF-8");
            JSONArray objArr = new JSONArray(js);
            for (int i = 0; i < objArr.length(); i++) {
                JSONObject obj = objArr.getJSONObject(i);
                boolean have = false;
                for (PluginGetter pluginGetter : PluginManager.getManager().list()) {
                    if (obj.getString("name").equals(pluginGetter.getName())) {
                        have = true;
                        if (obj.getDouble("version") > pluginGetter.version) {
                            FileUtils.url2file(obj.getString("url"), AppUtils.curDir + "/tools/plugins/" + pluginGetter.getName());
                            pluginGetter.load();
                            Toast.Build()
                                    .content("Đã update plugin " + pluginGetter.getName().replace(".jar", "") + " v" + pluginGetter.getVersion())
                                    .time(3000)
                                    .open();
                        }
                    }
                }
                if (have == false) {
                    FileUtils.url2file(obj.getString("url"), AppUtils.curDir + "/tools/plugins/" + obj.getString("name"));
                    PluginManager.getManager().add(AppUtils.curDir + "/tools/plugins/" + obj.getString("name"));
                    Toast.Build()
                            .content("Đã tải xuống plugin " + obj.getString("name").replace(".jar", ""))
                            .time(3000)
                            .open();
                }
            }
        } catch (IOException e) {
            Log.add(e);
        }

    }


}
