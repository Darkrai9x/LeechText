package dark.leech.text.get;

import dark.leech.text.action.Log;
import dark.leech.text.listeners.ChangeListener;
import dark.leech.text.models.Properties;
import dark.leech.text.util.SyntaxUtils;

import javax.swing.*;

/**
 * Created by Dark on 1/18/2017.
 */
public class Info extends SwingWorker {
    private InfoGetter infoGetter;
    private ChangeListener changeListener;
    private Properties properties;
    private boolean success;

    public Info clazz(Class cl) {
        try {
            infoGetter = (InfoGetter) cl.newInstance();
        } catch (InstantiationException e) {
            Log.add(e);
        } catch (IllegalAccessException e) {
            Log.add(e);
        }
        return this;
    }


    public Info listener(ChangeListener changeListener) {
        this.changeListener = changeListener;
        return this;
    }

    public Info applyTo(Properties properties) {
        this.properties = properties;
        return this;
    }


    @Override
    protected Void doInBackground() throws Exception {
        try {
            infoGetter.getter(properties);
            success = true;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected void done() {
        if (success)
            if (properties.getGioiThieu() != null)
                properties.setGioiThieu("<p>" + SyntaxUtils.Optimize(properties.getGioiThieu()).replace("\n", "</p>\n<p>") + "</p>");
        changeListener.doChanger();
    }
}
