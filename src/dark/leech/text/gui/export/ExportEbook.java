package dark.leech.text.gui.export;

import dark.leech.text.action.export.Ebook;
import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.Constants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.constant.TypeConstants;
import dark.leech.text.gui.components.MCheckBox;
import dark.leech.text.gui.components.MDialog;
import dark.leech.text.gui.components.MProgressBar;
import dark.leech.text.gui.components.MSelectBox;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.gui.components.button.CloseButton;
import dark.leech.text.gui.components.notification.BasicNotification;
import dark.leech.text.item.Properties;
import dark.leech.text.listeners.BlurListener;
import dark.leech.text.listeners.ChangeListener;
import dark.leech.text.listeners.ProgressListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Long on 9/10/2016.
 */
public class ExportEbook extends MDialog implements BlurListener, ProgressListener {

    private JPanel title;
    private JLabel labelTitle;
    private CloseButton buttonClose;
    private MSelectBox panelTool;
    private MSelectBox panelType;
    private MSelectBox panelComp;
    private MCheckBox cbSplit;
    private MCheckBox cbInclude;
    private BasicButton buttonOk;
    private MProgressBar progressBar;
    private JLabel labelProgress;
    private Properties properties;

    public ExportEbook(Properties properties) {
        this.properties = properties;
        setSize(245, 300);
        new Thread(new Runnable() {
            @Override
            public void run() {
                gui();
            }
        }).start();
        setCenter();
        display();
    }

    private void gui() {
        title = new JPanel();
        labelTitle = new JLabel();
        buttonClose = new CloseButton();
        buttonOk = new BasicButton();
        progressBar = new MProgressBar();
        labelProgress = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== title ========
        {
            title.setBackground(ColorConstants.THEME_COLOR);
            title.setLayout(null);
            //---- labelTitle ----
            labelTitle.setText("Tạo Ebook");
            labelTitle.setFont(FontConstants.titleNomal);
            labelTitle.setForeground(Color.WHITE);
            title.add(labelTitle);
            labelTitle.setBounds(15, 0, 95, 45);

            //---- buttonClose ----
            buttonClose.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    close();
                }
            });
            title.add(buttonClose);
            buttonClose.setBound(215, 10, 25, 25);

        }
        contentPane.add(title);
        title.setBounds(0, 0, 245, 45);

        //---- panelType2 ----

        panelTool = new MSelectBox("Công cụ", new String[]{"Mặc định", "Calibre"}, 0);
        panelTool.addChangeListener(new ChangeListener() {
            @Override
            public void doChanger() {
                ToolChanger();
            }
        });
        panelTool.addBlurListener(this);
        contentPane.add(panelTool);
        panelTool.setBounds(10, 90, 215, 30);

        //---- panelType ----
        panelType = new MSelectBox("Định dạng xuất", new String[]{"EPUB", "MOBI", "AZW3", "PDF"}, 0);
        panelType.addChangeListener(new ChangeListener() {
            @Override
            public void doChanger() {
                TypeChanger();
                ToolChanger();
            }
        });
        panelType.addBlurListener(this);
        contentPane.add(panelType);
        panelType.setBounds(10, 55, 215, 30);


        //---- panelType3 ----
        panelComp = new MSelectBox("Mức nén", new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"}, 8);
        panelComp.addBlurListener(this);
        contentPane.add(panelComp);
        panelComp.setBounds(10, 125, 215, 30);

        //---- checkBox1 ----
        cbSplit = new MCheckBox("Tự động chia quyển");
        contentPane.add(cbSplit);
        cbSplit.setBounds(10, 160, 215, 30);

        //
        cbInclude = new MCheckBox("Chèn chương ảnh");
        contentPane.add(cbInclude);
        cbInclude.setBounds(10, 195, 215, 30);
//
        labelProgress.setText("Đang xử lý...");
        labelProgress.setFont(FontConstants.textThin);
        contentPane.add(labelProgress);
        labelProgress.setBounds(15, 225, 210, 25);
        labelProgress.setVisible(false);
        //---- buttonOk ----
        buttonOk.setText("OK");
        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doClick();
            }
        });
        contentPane.add(buttonOk);
        buttonOk.setBound(15, 250, 210, 35);
        contentPane.add(progressBar);
        progressBar.setBounds(15, 250, 210, 35);
        progressBar.setVisible(false);
    }

    private void doClick() {
        buttonOk.setVisible(false);
        progressBar.setVisible(true);
        labelProgress.setVisible(true);
        Ebook ebook = new Ebook(properties);
        ebook.addProgressListener(this);
        ebook.setData(panelType.getSelectIndex(), panelTool.getSelectText(), panelComp.getSelectText(), cbSplit.isSelected(), true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ebook.export();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
                buttonOk.setVisible(true);
                progressBar.setVisible(false);
                progressBar.setPercent(0);
                labelProgress.setVisible(false);
                labelProgress.setText("Đang xử lý...");
            }
        }).start();
    }

    private void TypeChanger() {
        switch (panelType.getSelectIndex()) {
            case TypeConstants.EPUB:
                panelTool.setModel(new String[]{"Mặc định", "Calibre"}, 0);
                break;
            case TypeConstants.AZW3:
            case TypeConstants.DOC:
            case TypeConstants.PDF:
                panelTool.setModel(new String[]{"Calibre"}, 0);
                break;
            case TypeConstants.MOBI:
                panelTool.setModel(new String[]{"Calibre", "Kindlegen"}, 0);
                break;
        }
    }

    private void ToolChanger() {
        String toolName = panelTool.getSelectText();
        if (toolName.equals("Mặc định")) {
            panelComp.setVisible(true);
            panelComp.setModel(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"}, 8);
        }
        if (toolName.equals("Calibre"))
            switch (panelType.getSelectIndex()) {
                case TypeConstants.EPUB:
                case TypeConstants.AZW3:
                    panelComp.setVisible(false);
                    break;
                case TypeConstants.MOBI:
                    panelComp.setVisible(true);
                    panelComp.setModel(new String[]{"old", "both", "new"}, 1);
                    break;
                case TypeConstants.PDF:
                    panelComp.setVisible(true);
                    panelComp.setModel(new String[]{"a4", "a5", "a6"}, 0);
                    break;
            }
        if (toolName.equals("Kindlegen"))
            switch (panelType.getSelectIndex()) {
                case TypeConstants.MOBI:
                    panelComp.setModel(new String[]{"-c0", "-c1", "-c2"}, 2);
            }
    }

    @Override
    public void setBlur(boolean b) {
        getGlassPane().setVisible(b);
    }


    @Override
    public void setProgress(int value, String string) {
        progressBar.setPercent(value);
        labelProgress.setText(string);
        if (string.equals("Hoàn tất!")) {
            try {
                BufferedImage image = null;
                File file = new File(properties.getSavePath() + Constants.l + "data" + Constants.l + "cover.jpg");
                if (file.exists())
                    image = ImageIO.read(new File(properties.getSavePath() + Constants.l + "data" + Constants.l + "cover.jpg"));
                else
                    image = ImageIO.read(getClass().getResourceAsStream("/dark/leech/res/icon.png"));

                new BasicNotification(properties.getName(), "Đã tạo ebook xong!", (Icon) (new ImageIcon(image.getScaledInstance(54, 81,
                        BufferedImage.SCALE_SMOOTH))), 10000).showNoti();
            } catch (IOException e) {
            }
        }
    }
}
