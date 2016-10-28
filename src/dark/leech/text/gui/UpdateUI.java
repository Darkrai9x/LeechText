package dark.leech.text.gui;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.MDialog;
import dark.leech.text.gui.components.MProgressBar;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.gui.components.button.CloseButton;
import dark.leech.text.item.Base64;
import dark.leech.text.item.Connect;
import org.jsoup.Jsoup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Long on 9/16/2016.
 */
public class UpdateUI extends MDialog {
    private final String oldVersion = "3.2.2";
    private MProgressBar downloadProgress;
    private BasicButton update;
    private BasicButton cancel;
    private JLabel labelLoad;
    private JLabel labelText;
    private JLabel labelStatus;
    private String url;
    private String newVersion;
    private int downloaded = 0;
    private boolean haveUpdate;
    private int size = -1;

    public UpdateUI() {

    }

    private void gui() {
        JPanel panelTitle = new JPanel();
        CloseButton buttonClose = new CloseButton();
        JLabel labelTitle = new JLabel();
        JLabel labelVersion = new JLabel();
        labelLoad = new JLabel();
        labelText = new JLabel();
        update = new BasicButton();
        cancel = new BasicButton();
        labelStatus = new JLabel();
        downloadProgress = new MProgressBar();

        Container container = getContentPane();
        container.setBackground(Color.white);

        panelTitle.setBackground(ColorConstants.THEME_COLOR);
        panelTitle.setLayout(null);
        //---- labelTitle ----
        labelTitle.setText("    Cập nhật");
        labelTitle.setFont(FontConstants.titleNomal);
        labelTitle.setForeground(Color.WHITE);
        panelTitle.add(labelTitle);
        labelTitle.setBounds(0, 0, 255, 45);

        //---- buttonClose ----

        buttonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        panelTitle.add(buttonClose);
        buttonClose.setBound(270, 10, 25, 25);
        container.add(panelTitle);
        panelTitle.setBounds(0, 0, 300, 45);

        labelVersion.setText("Phiên bản hiện tại: " + oldVersion);
        labelVersion.setFont(FontConstants.textNomal);
        container.add(labelVersion);
        labelVersion.setBounds(80, 60, labelVersion.getPreferredSize().width, 30);
        labelLoad.setIcon(new ImageIcon(getClass().getResource("/dark/leech/res/img/load40px.gif")));
        container.add(labelLoad);
        labelLoad.setBounds(30, 100, 50, 50);

        labelText.setText("Đang kiểm tra cập nhật...");
        labelText.setFont(FontConstants.textNomal);
        container.add(labelText);
        labelText.setBounds(90, 100, 180, 50);

        labelStatus.setText("Đang tải...");
        labelStatus.setFont(FontConstants.textNomal);
        container.add(labelStatus);
        labelStatus.setBounds(40, 90, 250, 30);
        labelStatus.setVisible(false);

        container.add(downloadProgress);
        downloadProgress.setBounds(30, 120, 240, 30);
        downloadProgress.setVisible(false);
        update.setText("CẬP NHẬT");
        container.add(update);
        update.setBound(70, 155, 80, 35);
        update.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                labelLoad.setVisible(false);
                labelText.setVisible(false);
                downloadProgress.setVisible(true);
                downloadProgress.setPercent(0);
                update.setVisible(false);
                cancel.setVisible(false);
                labelStatus.setVisible(true);
                download();
            }
        });
        update.setVisible(false);
        cancel.setText("HỦY");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        container.add(cancel);
        cancel.setBound(170, 155, 80, 35);
        cancel.setVisible(false);
    }

    public void isHaveUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setSize(300, 200);
                gui();
                checkUpdate();
                if (haveUpdate) {
                    setCenter();
                    display();
                    setVisible(true);
                }
            }
        }).start();

    }

    public void checkUpdteUi() {
        setSize(300, 200);
        setCenter();
        display();
        gui();
        new Thread(new Runnable() {
            @Override
            public void run() {
                checkUpdate();
            }
        }).start();
    }

    public boolean checkUpdate() {
        Connect connect = new Connect();
        String urlUpdate = (new StringBuilder("aHR0cDovL3NvZnR2aC53YXBr").append("YS5tb2JpL3NpdGVfNzIueGh0bWw=")).toString();
        String html = connect.getHtml(Base64.decode(urlUpdate));
        newVersion = Jsoup.parse(html).select("div.version").text();
        if (newVersion == null || newVersion.length() == 0) {
            labelLoad.setIcon(new ImageIcon(getClass().getResource("/dark/leech/res/img/error.png")));
            labelText.setText("Có lỗi khi kiểm tra...");
        } else if (!newVersion.equals(oldVersion)) {
            this.url = Jsoup.parse(html).select("div.url").text();
            if (url.length() == 0) {
                labelLoad.setIcon(new ImageIcon(getClass().getResource("/dark/leech/res/img/error.png")));
                labelText.setText("Có lỗi khi kiểm tra...");
                return false;
            }
            labelLoad.setIcon(new ImageIcon(getClass().getResource("/dark/leech/res/img/info.png")));
            labelText.setText("Có phiên bản mới: " + newVersion);
            haveUpdate = true;
            update.setVisible(true);
            cancel.setVisible(true);
            return true;
        } else {
            labelLoad.setIcon(new ImageIcon(getClass().getResource("/dark/leech/res/img/info.png")));
            labelText.setText("Không có phiên bản mới!");
        }
        return false;
    }

    private void download() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL u = null;
                int status = 1;
                String fileName = "LeechText_" + newVersion + ".exe";
                try {
                    u = new URL(url);
                } catch (MalformedURLException e) {
                }
                RandomAccessFile file = null;
                InputStream stream = null;
                try {
                    // Open connection to URL.
                    HttpURLConnection connection = (HttpURLConnection) u.openConnection();
                    // Specify what portion of file to download.
                    connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
                    // Connect to server.
                    connection.connect();
                    // Make sure response code is in the 200 range.
                    if (connection.getResponseCode() / 100 != 2) {
                        error();
                    }
                    // Check for valid content length.
                    int contentLength = connection.getContentLength();
                    if (contentLength < 1) {
                        error();
                    }
        /* Set the size for this download if it
        hasn't been already set. */
                    if (size == -1) {
                        size = contentLength;
                        stateChanged();
                    }
                    // Open file and seek to the end of it.
                    file = new RandomAccessFile(fileName, "rw");
                    file.seek(downloaded);
                    stream = connection.getInputStream();
                    while (status == 1) {
            /* Size buffer according to how much of the
            file is left to download. */
                        byte buffer[];
                        if (size - downloaded > 1024) {
                            buffer = new byte[1024];
                        } else {
                            buffer = new byte[size - downloaded];
                        }

                        // Read from server into buffer.
                        int read = stream.read(buffer);
                        if (read == -1) {
                            break;
                        }
                        // Write buffer to file.
                        file.write(buffer, 0, read);
                        downloaded += read;
                        stateChanged();
                    }

        /* Change status to complete if this point was
        reached because downloading has finished. */
                    if (status == 1) {
                        status = 2;
                        stateChanged();
                    }
                } catch (Exception e) {
                    error();
                } finally {
                    // Close file.
                    if (file != null) {
                        try {
                            file.close();
                        } catch (Exception e) {
                        }
                    }
                    // Close connection to server.
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }).start();
    }

    private void error() {
        labelStatus.setText("Có lỗi xảy ra khi cập nhật!");
        downloadProgress.setVisible(false);
    }

    private void stateChanged() {
        labelStatus.setText(((downloaded == size) ? "Hoàn tất: " : "Đang tải: ") + Integer.toString(downloaded * 100 / size) + "% (" + Integer.toString(downloaded / 1024) + "KB/" + Integer.toString(size / 1024) + "KB)");
        downloadProgress.setPercent(downloaded * 100 / size);

    }

}
