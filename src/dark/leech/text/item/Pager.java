package dark.leech.text.item;

import java.util.ArrayList;

/**
 * Created by Long on 9/15/2016.
 */
public class Pager {
    private String url;
    private ArrayList<Chapter> chapter;
    private int id;
    private boolean get = false;
    private boolean completed = false;

    public Pager(String url, int id) {
        this.url = url;
        this.id = id;
    }

    public Pager(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Chapter> getChapter() {
        return chapter;
    }

    public void setChapter(ArrayList<Chapter> chapter) {
        this.chapter = chapter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isGet() {
        return get;
    }

    public void setGet(boolean get) {
        this.get = get;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
