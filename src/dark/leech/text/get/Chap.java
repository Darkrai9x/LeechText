package dark.leech.text.get;

import dark.leech.text.action.Log;
import dark.leech.text.listeners.ChangeListener;
import dark.leech.text.models.Chapter;
import dark.leech.text.util.AppUtils;
import dark.leech.text.util.FileUtils;
import dark.leech.text.util.SettingUtils;
import dark.leech.text.util.SyntaxUtils;

import javax.swing.*;

/**
 * Created by Dark on 1/18/2017.
 */
public class Chap extends SwingWorker<String, Void> {
    private ChapGetter chapGetter;
    private Chapter chapter;
    private ChangeListener changeListener;
    private String text;
    private String savepath;
    private String chset = "utf-8";

    @Override
    protected String doInBackground() throws Exception {
        AppUtils.pause(SettingUtils.DELAY);
        return chapGetter.getter(chapter.getUrl());
    }

    @Override
    protected void done() {
        try {
            text = get();
            chapter.setError(false);
            chapter.setEmpty(false);
            if (text == null || text.length() == 0)
                chapter.setError(true);
            else if (text.length() < 1000)
                if (text.split("<img ").length > 1)
                    chapter.setImageChapter(true);
                else chapter.setEmpty(true);
            chapter.setCompleted(true);
            if (!(chapter.isEmpty() && chapter.isError()))
                write();
        } catch (Exception e) {
            chapter.setError(true);
        }
        changeListener.doChanger();

    }

    public Chap clazz(Class cl) {
        try {
            chapGetter = (ChapGetter) cl.newInstance();
        } catch (InstantiationException e) {
            Log.add(e);
        } catch (IllegalAccessException e) {
            Log.add(e);
        }
        return this;
    }

    public Chap listener(ChangeListener changeListener) {
        this.changeListener = changeListener;
        return this;
    }

    public Chap path(String savepath) {
        this.savepath = savepath;
        return this;
    }

    public Chap charset(String chset) {
        this.chset = chset;
        return this;
    }

    public Chap applyTo(Chapter chapter) {
        this.chapter = chapter;
        return this;
    }

    private void write() {
        FileUtils.string2file(Optimize(text), savepath + "/raw/" + chapter.getId() + ".txt", chset);
    }


    private String Optimize(String src) {
        if (SyntaxUtils.REPLACE_FROM != null)
            for (int i = 0; i < SyntaxUtils.REPLACE_FROM.length; i++)
                src = src.replaceAll(SyntaxUtils.REPLACE_FROM[i], SyntaxUtils.REPLACE_TO[i]);
        return src;
    }
}
