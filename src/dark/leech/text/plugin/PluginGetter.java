package dark.leech.text.plugin;

import dark.leech.text.util.ZipUtils;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Created by Dark on 1/11/2017.
 */
public class PluginGetter {
    private File plugin;
    private String classInfoGetter;
    private String classListGetter;
    private String classChapGetter;
    private String classPageGetter;
    private String info;
    private String icon;
    public double version;
    private boolean forum;
    private boolean checked = true;
    private String match;
    private String demoUrl;
    private Class infoGetter;
    private Class listGetter;
    private Class chapGetter;
    private Class pageGetter;

    public PluginGetter(File plugin, boolean checked) {
        this.plugin = plugin;
        this.checked = checked;
        load();
    }

    public void load() {
        JSONObject js = new JSONObject(ZipUtils.readInZipAsString(plugin, "plugin.json"));
        JSONObject metadata = js.getJSONObject("metadata");
        info = metadata.getString("info");
        version = metadata.getDouble("version");
        match = metadata.getString("regex");
        try {
            icon = metadata.getString("icon");
        } catch (Exception e) {
        }
        try {
            demoUrl = metadata.getString("demo");
        } catch (Exception e) {
        }
        try {
            forum = metadata.getBoolean("forum");
        } catch (Exception e) {
        }
        JSONObject manifest = js.getJSONObject("class");
        classInfoGetter = manifest.getString("InfoGetter");
        classListGetter = manifest.getString("ListGetter");
        if (!isForum())
            classChapGetter = manifest.getString("ChapGetter");
        else
            classPageGetter = manifest.getString("PageGetter");
    }

    public boolean isMatch(String url) {
        return url.matches(match);
    }

    public String getName() {
        return plugin.getName();
    }

    public String getInfo() {
        return info;
    }

    public String getVersion() {
        return Double.toString(version);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public File getFile() {
        return plugin;
    }

    public InputStream getIcon() {
        try {
            return new ByteArrayInputStream(ZipUtils.readInZipAsByte(plugin, icon));
        } catch (Exception e) {
            return null;
        }

    }

    public boolean isForum() {
        return forum;
    }


    public String getDemoUrl() {
        if (demoUrl == null) demoUrl = getName();
        return demoUrl;
    }

    public Class InfoGetter() {
        if (infoGetter == null) {
            PluginLoader pluginLoader = new PluginLoader(ZipUtils.readInZipAsByte(plugin, classInfoGetter));
            infoGetter = pluginLoader.loadClass(parseClassName(classInfoGetter));
        }
        return infoGetter;
    }

    public Class ListGetter() {
        if (listGetter == null) {
            PluginLoader pluginLoader = new PluginLoader(ZipUtils.readInZipAsByte(plugin, classListGetter));
            listGetter = pluginLoader.loadClass(parseClassName(classListGetter));
        }
        return listGetter;
    }

    public Class ChapGetter() {
        if (chapGetter == null) {
            PluginLoader pluginLoader = new PluginLoader(ZipUtils.readInZipAsByte(plugin, classChapGetter));
            chapGetter = pluginLoader.loadClass(parseClassName(classChapGetter));
        }
        return chapGetter;
    }

    public Class PageGetter() {

        if (pageGetter == null) {
            PluginLoader pluginLoader = new PluginLoader(ZipUtils.readInZipAsByte(plugin, classPageGetter));
            pageGetter = pluginLoader.loadClass(parseClassName(classPageGetter));
        }
        return pageGetter;
    }


    private String parseClassName(String path) {
        path = path.replaceAll("(.*?/|^)(.*?).class", "$2");
        return path;
    }
}
