package dark.leech.text.gui.export;

import dark.leech.text.action.export.Text;
import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.Constants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.CheckBox;
import dark.leech.text.gui.components.Dialog;
import dark.leech.text.gui.components.ProgressBar;
import dark.leech.text.gui.components.SelectBox;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.gui.components.button.CloseButton;
import dark.leech.text.gui.components.notification.BasicNotification;
import dark.leech.text.listeners.BlurListener;
import dark.leech.text.listeners.ChangeListener;
import dark.leech.text.listeners.ProgressListener;
import dark.leech.text.models.Properties;

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
public class ExportText extends Dialog implements BlurListener, ProgressListener, ChangeListener {
    private JPanel title;
    private JLabel labelTitle;
    private CloseButton buttonClose;
    private SelectBox panelType;
    private SelectBox panelType2;
    private CheckBox checkBoxToc;
    private BasicButton buttonOk;
    private ProgressBar progressBar;
    private Properties properties;

    public ExportText(Properties properties) {
        this.properties = properties;
        setSize(245, 255);

                gui();



    }

    private void gui() {
        title = new JPanel();
        labelTitle = new JLabel();
        buttonClose = new CloseButton();
        buttonOk = new BasicButton();
        progressBar = new ProgressBar();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== title ========
        title.setBackground(ColorConstants.THEME_COLOR);
        title.setLayout(null);
        //---- labelTitle ----
        labelTitle.setText("Xuất Text");
        labelTitle.setFont(FontConstants.titleNomal);
        labelTitle.setForeground(Color.WHITE);
        title.add(labelTitle);
        labelTitle.setBounds(15, 0, 95, 45);

        //---- buttonClose ----
        buttonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hide();
            }
        });
        title.add(buttonClose);
        buttonClose.setBounds(215, 10, 25, 25);

        contentPane.add(title);
        title.setBounds(0, 0, 245, 45);

        //---- panelType ----
        panelType = new SelectBox("Định dạng xuất", new String[]{"HTML", "TXT"}, 0);
        panelType.addChangeListener(this);
        panelType.addBlurListener(this);
        contentPane.add(panelType);
        panelType.setBounds(15, 60, 200, 30);


        //---- panelType2 ----
        panelType2 = new SelectBox("Kiểu xuất", new String[]{"Tách", "Gộp"}, 0);
        panelType2.addBlurListener(this);
        contentPane.add(panelType2);
        panelType2.setBounds(15, 95, 200, 30);

        checkBoxToc = new CheckBox("Đính kèm mục lục");
        contentPane.add(checkBoxToc);
        checkBoxToc.setBounds(15, 130, 200, 30);
        checkBoxToc.setVisible(panelType.getSelectIndex() == 0 ? true : false);
        //---- buttonOk ----
        buttonOk.setText("XUẤT");
        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doExport();
                    }
                }).start();
            }
        });
        contentPane.add(buttonOk);
        buttonOk.setBounds(15, 210, 210, 35);
        progressBar.setPercent(0);
        contentPane.add(progressBar);
        progressBar.setBounds(15, 210, 210, 35);
        progressBar.setVisible(false);
    }

    private void doExport() {
        progressBar.setVisible(true);
        buttonOk.setVisible(false);
        Text exportText = new Text(properties, panelType.getSelectIndex(), checkBoxToc.isSelected(), panelType2.getSelectIndex());
        exportText.addProgressListener(this);
        exportText.export();
        try {
            BufferedImage image = null;
            File file = new File(properties.getSavePath() + Constants.l + "data" + Constants.l + "cover.jpg");
            if (file.exists())
                image = ImageIO.read(new File(properties.getSavePath() + Constants.l + "data" + Constants.l + "cover.jpg"));
            else
                image = ImageIO.read(getClass().getResourceAsStream("/dark/leech/res/icon.png"));

            new BasicNotification(properties.getName(), "Đã xuất text xong!", (Icon) (new ImageIcon(image.getScaledInstance(54, 81,
                    BufferedImage.SCALE_SMOOTH))), 10000).showNoti();
        } catch (IOException e) {
        }
    }

    @Override
    public void setBlur(boolean b) {
        getGlassPane().setVisible(b);
    }

    @Override
    public void setProgress(int value, String string) {
        progressBar.setPercent(value);
        if (value == 100)
            hide();
    }

    @Override
    public void doChanger() {
        checkBoxToc.setVisible(panelType.getSelectIndex() == 0 ? true : false);
    }
}
