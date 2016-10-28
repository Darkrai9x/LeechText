package dark.leech.text.gui;

import dark.leech.text.action.Download;
import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.Constants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.enums.State;
import dark.leech.text.gui.components.CircleWait;
import dark.leech.text.gui.components.MPanel;
import dark.leech.text.gui.components.MProgressBar;
import dark.leech.text.gui.components.button.CircleButton;
import dark.leech.text.gui.components.notification.BasicNotification;
import dark.leech.text.listeners.DownloadListener;
import dark.leech.text.listeners.RemoveListener;
import dark.leech.text.models.FileAction;
import dark.leech.text.models.Properties;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DownloadUI extends MPanel implements DownloadListener {
    private MProgressBar load;
    private JLabel labelName;
    private JLabel labelStatus;
    private JLabel labelPercent;
    private JLabel labelProgress;
    private JLabel panelCover;
    private CircleButton labelDelete;
    private CircleButton labelPR;
    private CircleButton labelInfo;
    private boolean pause;
    private State state = State.DOWNLOADING;

    private int max;
    private Properties properties;
    private Download download;
    private RemoveListener removeListener;

    public DownloadUI() {
        gui();
        pause = false;
    }

    public void addDownload(Properties properties) {
        this.properties = properties;
        this.max = properties.getSize();
        doCreateFolder();
        download = new Download(properties);
        download.addDownloadListener(this);
        download.startDownload();
        labelName.setText(properties.getName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadCover();
            }
        }).start();
    }

    public void addRemoveListener(RemoveListener removeListener) {
        this.removeListener = removeListener;
    }

    // Tạm dừng
    private void pause() {
        download.pause();
    }

    // Tiếp tục
    @SuppressWarnings("unused")
    private void resume() {
        download.resume();
    }

    // Xóa
    @SuppressWarnings("unused")
    private void delete() {
        download.cancel();
        removeListener.removeComponent(this);
    }

    // Kết thúc quá trình
    private void doAfterFinish() {
        labelPR.setVisible(false);
        labelInfo.setVisible(true);
        new BasicNotification(properties.getName(), "Đã gettext xong!", panelCover.getIcon(), 5000, properties.getSavePath()).showNoti();
    }

    // Khi bị lỗi
    private void doAfterError() {
        pause();
        labelInfo.setVisible(true);
    }

    // get dữ liệu đầu vào
    public Properties getProperties() {
        return properties;
    }

    // Đặt giá trị
    private void setValue(int value) {
        load.setPercent(value * 100 / max);
        labelProgress.setText(Integer.toString(value) + "/" + Integer.toString(max));
        labelPercent.setText(Integer.toString(load.getPercent()) + "%");
    }


    // Trạng thái
    public String getStatus(State state) {
        switch (state) {
            case DOWNLOADING:
                labelPR.setText("");
                return "Đang tải...";
            case PAUSE:
                labelPR.setText("");
                return "Tạm dừng";
            case ERROR:
                doAfterError();
                return "Lỗi";
            case COMPLETED:
                doAfterFinish();
                return "Hoàn tất";
            case CHECKING:
                return "Kiểm tra";
            default:
                return "";
        }
    }

    private void gui() {
        panelCover = new JLabel();
        load = new MProgressBar();
        labelName = new JLabel();
        labelStatus = new JLabel();
        labelPercent = new JLabel();
        labelProgress = new JLabel();

        setBackground(Color.white);
        setLayout(null);
        // ======== panelCover ========
        panelCover.setLayout(null);
        add(panelCover);
        panelCover.setBounds(5, 5, 55, 80);
        // ======== load ========
        load.setOpaque(true);
        load.setLayout(null);
        add(load);
        load.setBounds(65, 70, 305, 4);
        // ---- labelName ----
        labelName.setFont(FontConstants.textBold);
        labelName.setForeground(ColorConstants.THEME_COLOR);
        add(labelName);
        labelName.setBounds(65, 5, 230, 30);

        // ---- labelStatus ----
        labelStatus.setText("Đang tải...");
        labelStatus.setFont(FontConstants.textNomal);
        labelStatus.setForeground(ColorConstants.THEME_COLOR);
        labelStatus.setHorizontalAlignment(SwingConstants.LEFT);
        add(labelStatus);
        labelStatus.setBounds(65, 40, 95, labelStatus.getPreferredSize().height);

        // ---- labelPercent ----
        labelPercent.setFont(FontConstants.textNomal);
        labelPercent.setHorizontalAlignment(SwingConstants.RIGHT);
        labelPercent.setForeground(ColorConstants.THEME_COLOR);
        add(labelPercent);
        labelPercent.setBounds(330, 40, 40, 30);

        // ---- labelProgress ----
        labelProgress.setHorizontalAlignment(SwingConstants.RIGHT);
        labelProgress.setFont(FontConstants.textNomal);
        labelProgress.setForeground(ColorConstants.THEME_COLOR);
        add(labelProgress);
        labelProgress.setBounds(200, 40, 90, 30);

        // ---- labelPR ----
        labelPR = new CircleButton("\ue034", 20f);
        labelPR.setForeground(ColorConstants.THEME_COLOR);
        labelPR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pause = !pause;
                if (pause)
                    pause();
                else
                    resume();
            }
        });
        add(labelPR);
        labelPR.setBound(310, 5, 30, 30);

        // ---- labelInfo ----
        labelInfo = new CircleButton("", 20f);
        labelInfo.setForeground(ColorConstants.THEME_COLOR);
        labelInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InfoUI infoUI = new InfoUI(properties);
                infoUI.setVisible(true);
            }
        });
        add(labelInfo);
        labelInfo.setBound(310, 5, 30, 30);
        labelInfo.setVisible(false);
        // ---- labelDelete ----
        labelDelete = new CircleButton("\ue5cd", 20f);
        labelDelete.setForeground(ColorConstants.THEME_COLOR);
        labelDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });
        add(labelDelete);
        labelDelete.setBound(345, 5, 30, 30);
        setPreferredSize(new Dimension(375, 90));
    }

    private void loadCover() {
        FileAction file = new FileAction();
        String cover = properties.getCover();
        if (cover != null)
            if (cover.length() != 0) {
                try {
                    CircleWait circleWait = new CircleWait(panelCover.getPreferredSize());
                    JLayer<JPanel> layer = circleWait.getJlayer();
                    panelCover.add(layer);
                    layer.setBounds(0, 0, 55, 80);
                    circleWait.startWait();
                    file.url2file(cover, properties.getSavePath() + Constants.l + "data" + Constants.l + "cover.jpg");
                    circleWait.stopWait();
                    setCover(new FileInputStream(properties.getSavePath() + Constants.l + "data" + Constants.l + "cover.jpg"));
                } catch (FileNotFoundException e) {
                }
                return;
            }
        setCover(getClass().getResourceAsStream("/dark/leech/res/img/unknown.png"));
    }

    private void setCover(InputStream in) {
        try {
            BufferedImage image = ImageIO.read(in);
            int x = panelCover.getSize().width;
            int y = panelCover.getSize().height;
            int ix = image.getWidth();
            int iy = image.getHeight();
            int dx = 0;
            int dy = 0;
            if (x / y > ix / iy) {
                dy = y;
                dx = dy * ix / iy;
            } else {
                dx = x;
                dy = dx * iy / ix;
            }
            ImageIcon icon = new ImageIcon(image.getScaledInstance(dx, dy,
                    BufferedImage.SCALE_SMOOTH));
            panelCover.setIcon(icon);
        } catch (Exception ex) {
        }
    }

    private void doCreateFolder() {
        FileAction.mkdir(properties.getSavePath() + Constants.l + "raw");
        FileAction.mkdir(properties.getSavePath() + Constants.l + "data");
        FileAction.mkdir(properties.getSavePath() + Constants.l + "data" + Constants.l + "Images");
        FileAction.mkdir(properties.getSavePath() + Constants.l + "data" + Constants.l + "Text");
        FileAction.mkdir(properties.getSavePath() + Constants.l + "out");
    }

    @Override
    public void updateDownload(int downloaded, State state) {
        setValue(downloaded);
        if (state != this.state) {
            this.state = state;
            labelStatus.setText(getStatus(state));
        }
    }

}
