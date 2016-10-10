package dark.leech.text.action;

import dark.leech.text.constant.SettingConstants;
import dark.leech.text.enums.State;
import dark.leech.text.getter.Chap;
import dark.leech.text.getter.Page;
import dark.leech.text.item.Chapter;
import dark.leech.text.item.Pager;
import dark.leech.text.item.Properties;
import dark.leech.text.listeners.DownloadListener;

import java.util.ArrayList;

import static dark.leech.text.constant.SettingConstants.*;

public class Download {
    private Properties properties;
    private int downloaded = 0;
    private State state;
    private ArrayList<Chapter> chapList;
    private ArrayList<Pager> pageList;
    private int size;
    private int next;
    private DownloadListener downloadListener;

    public Download(Properties properties) {
        this.properties = properties;
        this.chapList = properties.getChapList();
        this.pageList = properties.getPageList();
        this.size = properties.getSize();
    }

    public void addDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    // Status action
    public void pause() {
        state = State.PAUSE;
        downloadListener.updateDownload(downloaded, state);
    }

    public void cancel() {
        state = State.CANCEL;
        downloadListener.updateDownload(downloaded, state);
    }

    public void resume() {
        state = State.DOWNLOADING;
        startDownload();
        downloadListener.updateDownload(downloaded, state);
    }

    // Bắt đầu quá trình
    public void startDownload() {
        state = State.DOWNLOADING;
        next = MAX_CONN;
        if (properties.isForum()) {
            doForumDownload();
        } else
            for (int i = 0; i < MAX_CONN; i++)
                doDownload(i);
    }

    private synchronized int getNext() {
        return (next = next + 1);
    }

    //download với web thường
    private void doDownload(final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                web(index);
            }
        }).start();
    }

    //download với forrum
    private void doForumDownload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < size; i++) {
                    forum(i);
                }
            }
        }).start();
    }

    private void forum(final int index) {
        if (index >= pageList.size()) return;
        if (pageList.get(index).isGet() && !pageList.get(index).isCompleted())
            try {
                if (state == State.DOWNLOADING) {
                    new Page(chapList, pageList.get(index).getUrl(), properties.getSavePath(), chapList.size()).getData();
                    updateDownload();
                    pageList.get(index).setCompleted(true);
                }
            } catch (Exception e) {
                Log.add("Lỗi: " + e.toString());
            }
    }

    private void web(final int index) {
        if (index >= chapList.size())
            return;
        if (chapList.get(index).isGet() && !chapList.get(index).isCompleted()) {
            try {
                chapList.set(index,
                        new Chap(chapList.get(index), properties.getSavePath()).getChapter());
                if (state == State.DOWNLOADING) {
                    updateDownload();
                    chapList.get(index).setCompleted(true);
                } else return;
            } catch (Exception e) {
                Log.add("Lỗi: ID = " + Integer.toString(index) + " - " + e.toString());
            }
        }
        web(getNext() - 1);
    }

    private synchronized void updateDownload() {
        downloaded++;
        if (downloaded >= size) {
            downloaded = size;
            state = State.CHECKING;
            check();
        }
        downloadListener.updateDownload(downloaded, state);
    }


    private void check() {
        if (!properties.isForum())
            for (int i = 0; i < downloaded; i++)
                if (!chapList.get(i).isCompleted()) doDownload(i);
        state = State.COMPLETED;
        downloadListener.updateDownload(downloaded, state);
    }
}

