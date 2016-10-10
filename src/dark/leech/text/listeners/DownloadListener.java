package dark.leech.text.listeners;

import dark.leech.text.enums.State;

/**
 * Created by Long on 9/1/2016.
 */
public interface DownloadListener {
    void updateDownload(int downloaded, State state);
}
