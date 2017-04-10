package dark.leech.text.plugin;

import dark.leech.text.action.Log;

/**
 * Created by Long on 1/11/2017.
 */
public class PluginLoader extends ClassLoader {
    byte[] plugin;

    public PluginLoader(byte[] plugin) {
        this.plugin = plugin;
    }

    public Class loadClass(String name) {
        return loadClass(name, true);
    }

    public Class loadClass(String classname, boolean resolve) {
        try {
            Class c = findLoadedClass(classname);
            if (c == null)
                try {
                    c = findSystemClass(classname);
                } catch (Exception ex) {
                }
            if (c == null)
                c = defineClass(classname, plugin, 0, plugin.length);
            if (resolve)
                resolveClass(c);
            return c;
        } catch (Exception ex) {
            Log.add(ex);
            return null;
        }
    }
}
