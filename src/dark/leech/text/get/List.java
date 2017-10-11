package dark.leech.text.get;

import dark.leech.text.action.Log;
import dark.leech.text.listeners.ChangeListener;
import dark.leech.text.models.Chapter;
import dark.leech.text.models.Pager;
import dark.leech.text.models.Properties;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Dark on 1/18/2017.
 */
public class List extends SwingWorker {
    private ListGetter listGetter;
    private ChangeListener changeListener;
    private Properties properties;
    private boolean success;

    public List clazz(Class cl) {
        try {
            listGetter = (ListGetter) cl.newInstance();
        } catch (InstantiationException e) {
            Log.add(e);
        } catch (IllegalAccessException e) {
            Log.add(e);
        }

        return this;
    }

    public List listener(ChangeListener changeListener) {
        this.changeListener = changeListener;
        return this;
    }

    public List applyTo(Properties properties) {
        this.properties = properties;
        return this;
    }


    @Override
    protected Void doInBackground() throws Exception {
        try {
            listGetter.getter(properties);
            success = true;
        } catch (Exception e) {
            Log.add(e);
        }
        return null;
    }

    @Override
    public void done() {
        if (success) {
            if (properties.isForum()) {
                ArrayList<Pager> pageList = properties.getPageList();
                for (int i = 0; i < pageList.size(); i++) {
                    Pager pager = pageList.get(i);
                    if (pager.getName() == null)
                        pager.setName("Trang " + Integer.toString(i + 1));
                    pager.setId(i);
                }
            } else {
                ArrayList<Chapter> chapList = properties.getChapList();
                for (int i = 0; i < chapList.size(); i++)
                    chapList.get(i).setId(i);
            }
        }
        changeListener.doChanger();
    }
}
