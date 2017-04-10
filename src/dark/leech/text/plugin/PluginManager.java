package dark.leech.text.plugin;

import dark.leech.text.util.AppUtils;
import dark.leech.text.util.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Long on 1/11/2017.
 */
public class PluginManager {
    private static PluginManager manager;
    private static ArrayList<PluginGetter> pluginList;

    private PluginManager() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                pluginList = new ArrayList<>();
                File[] files = new File(FileUtils.validate(AppUtils.curDir + "/tools/plugins")).listFiles();
                if(files == null) return;
                String js = FileUtils.file2string(AppUtils.curDir + "/tools/plugins/plugin.json");
                JSONObject obj = null;
                if (js != null)
                    obj = new JSONObject(js);
                for (File f : files) {
                    if (f.getName().endsWith(".jar"))
                        try {
                            pluginList.add(new PluginGetter(f, obj.getBoolean(f.getName())));
                        } catch (Exception e) {
                            try {
                                pluginList.add(new PluginGetter(f, true));
                            } catch (Exception ge) {
                            }
                        }
                }
                PluginUpdate.getUpdate().checkUpdate();
            }
        }).start();

    }

    public static PluginManager getManager() {
        if (manager == null) manager = new PluginManager();
        return manager;
    }

    public void add(String path) {
        pluginList.add(new PluginGetter(new File(path), true));
    }

    public PluginGetter get(String url) {
        for (PluginGetter pluginGetter : pluginList)
            if (pluginGetter.isMatch(url)) return pluginGetter;
        return null;
    }

    public ArrayList<PluginGetter> list() {
        return pluginList;
    }

}


