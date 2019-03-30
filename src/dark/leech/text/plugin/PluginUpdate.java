package dark.leech.text.plugin;

import com.google.gson.Gson;
import dark.leech.text.action.Log;
import dark.leech.text.enities.PluginEntity;
import dark.leech.text.ui.notification.Toast;
import dark.leech.text.util.AppUtils;
import dark.leech.text.util.FileUtils;
import dark.leech.text.util.Http;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Dark on 2/24/2017.
 */
public class PluginUpdate {
    private static final String URL = "https://dl.dropboxusercontent.com/s/nfsg7x1f1hvdzjb/plugins.json?dl=1";
    private static PluginUpdate pluginUpdate;

    private PluginUpdate() {

    }

    public static PluginUpdate getUpdate() {
        if (pluginUpdate == null) pluginUpdate = new PluginUpdate();
        return pluginUpdate;
    }


    public void checkUpdate() {
        try {
            String js = Http.request(URL).string();
            JSONArray objArr = new JSONArray(js);
            for (int i = 0; i < objArr.length(); i++) {
                JSONObject obj = objArr.getJSONObject(i);
                boolean have = false;
                for (PluginEntity pluginGetter : PluginManager.getManager().list()) {
                    if (obj.getString("uuid").equals(pluginGetter.getUuid())) {
                        have = true;
                        if (obj.getDouble("version") > pluginGetter.getVersion()) {
                            String path = AppUtils.curDir
                                    + "/tools/plugins/"
                                    + pluginGetter.getUuid()
                                    + ".plugin";
                            String json = Http.request(obj.getString("url")).string();

                            PluginEntity entity = new Gson().fromJson(json, PluginEntity.class);
                            entity.setChecked(true);
                            FileUtils.string2file(new Gson().toJson(entity), path);
                            pluginGetter.apply(entity);
                            Toast.Build()
                                    .content("Đã update plugin " + pluginGetter.getName() + " v" + pluginGetter.getVersion())
                                    .time(3000)
                                    .open();
                        }
                    }
                }
                if (have == false) {
                    String path = AppUtils.curDir
                            + "/tools/plugins/"
                            + obj.getString("uuid")
                            + ".plugin";
                    String json = Http.request(obj.getString("url")).string();

                    PluginEntity entity = new Gson().fromJson(json, PluginEntity.class);
                    entity.setChecked(true);
                    FileUtils.string2file(new Gson().toJson(entity), path);
                    PluginManager.getManager().add(path);
                    Toast.Build()
                            .content("Đã tải xuống plugin " + obj.getString("name"))
                            .time(3000)
                            .open();
                }
            }
        } catch (Exception e) {
            Log.add(e);
        }

    }


}
