package dark.leech.text.gui;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.Constants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.MDialog;
import dark.leech.text.gui.components.MTextField;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.gui.components.button.CircleButton;
import dark.leech.text.gui.components.button.CloseButton;
import dark.leech.text.gui.export.ConfigUI;
import dark.leech.text.gui.export.ExportEbook;
import dark.leech.text.gui.export.ExportText;
import dark.leech.text.item.FileAction;
import dark.leech.text.item.Properties;
import dark.leech.text.listeners.BlurListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class InfoUI extends MDialog implements BlurListener {
    private JPanel title;
    private JLabel labelTitle;
    private CloseButton buttonClose;
    private JPanel panelCover;
    private CircleButton labelEdit;
    private JLabel labelCover;
    private BasicButton buttonConfig;
    private BasicButton buttonText;
    private BasicButton buttonExport;
    private JLabel labelName;
    private MTextField textFieldName;
    private JLabel labelAuthor;
    private MTextField textFieldAuthor;
    private JLabel labelStatus;

    private Properties properties;

    public InfoUI(Properties properties) {
        this.properties = properties;
        setSize(340, 265);
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
        panelCover = new JPanel();
        labelCover = new JLabel();
        buttonConfig = new BasicButton();
        buttonText = new BasicButton();
        buttonExport = new BasicButton();
        labelName = new JLabel();
        labelAuthor = new JLabel();
        labelStatus = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        title.setBackground(ColorConstants.THEME_COLOR);
        title.setLayout(null);
        //---- labelTitle ----
        labelTitle.setText(properties.getName());
        labelTitle.setFont(FontConstants.titleNomal);
        labelTitle.setForeground(Color.WHITE);
        title.add(labelTitle);
        labelTitle.setBounds(15, 0, 265, 45);

        //---- buttonClose ----
        buttonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        title.add(buttonClose);
        buttonClose.setBound(310, 10, 25, 25);

        contentPane.add(title);
        title.setBounds(0, 0, 340, 45);

        panelCover.setLayout(null);

        //---- labelEdit ----
        labelEdit = new CircleButton("\ue254", 16f);
        labelEdit.setForeground(Color.BLACK);
        labelEdit.setBackground(Color.darkGray);
        labelEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog file = new FileDialog((Frame) null, "Chọn ảnh", FileDialog.LOAD);
                file.setLocation(Constants.LOCATION);
                file.setModal(true);
                file.setVisible(true);
                if (file.getFile() != null)
                    try {
                        new FileAction().copyFile(file.getDirectory() + file.getFile(), properties.getSavePath() + Constants.l + "data" + Constants.l + "cover.jpg");
                        setCover(new FileInputStream(properties.getSavePath() + Constants.l + "data" + Constants.l + "cover.jpg"));
                    } catch (FileNotFoundException e1) {
                    }
            }
        });
        panelCover.add(labelEdit);
        labelEdit.setBound(70, 120, 30, 30);

        //---- labelCover ----
        panelCover.add(labelCover);
        labelCover.setBounds(0, 0, 100, 150);


        contentPane.add(panelCover);
        panelCover.setBounds(5, 50, 100, 150);

        //---- buttonConfig ----
        buttonConfig.setText("Hiệu Chỉnh");
        buttonConfig.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doConfig();
            }
        });
        contentPane.add(buttonConfig);
        buttonConfig.setBound(10, 215, 100, 35);

        //---- buttonText ----
        buttonText.setText("Xuất Text");
        buttonText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exportText();
            }
        });
        contentPane.add(buttonText);
        buttonText.setBound(115, 215, 100, 35);

        //---- buttonExport ----
        buttonExport.setText("Xuất Ebook");
        buttonExport.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exportEbook();
            }
        });
        contentPane.add(buttonExport);
        buttonExport.setBound(225, 215, 100, 35);

        //---- labelName ----
        labelName.setText("Tên truyện");
        labelName.setFont(FontConstants.textNomal);
        contentPane.add(labelName);
        labelName.setBounds(115, 50, 220, 20);
        textFieldName = new MTextField(115, 75, 220, 30);
        textFieldName.setText(properties.getName());
        contentPane.add(textFieldName);


        //---- labelAuthor ----
        labelAuthor.setText("Tác giả");
        labelAuthor.setFont(FontConstants.textNomal);
        contentPane.add(labelAuthor);
        labelAuthor.setBounds(115, 110, 220, 20);
        textFieldAuthor = new MTextField(115, 135, 220, 30);
        textFieldAuthor.setText(properties.getAuthor());
        contentPane.add(textFieldAuthor);


        //---- labelStatus ----
        labelStatus.setFont(FontConstants.textNomal);
        contentPane.add(labelStatus);
        labelStatus.setBounds(115, 170, 220, 30);
        try {
            if (new File(properties.getSavePath() + Constants.l + "data" + Constants.l + "cover.jpg").exists())
                setCover(new FileInputStream(properties.getSavePath() + Constants.l + "data" + Constants.l + "cover.jpg"));
        } catch (FileNotFoundException e) {
        }

    }

    private void doConfig() {
        ConfigUI configUI = new ConfigUI(properties);
        configUI.addBlurListener(this);
        setBlur(true);
        configUI.setVisible(true);
    }

    private void exportText() {
        ExportText export = new ExportText(properties);
        export.addBlurListener(this);
        setBlur(true);
        export.setVisible(true);
    }

    private void exportEbook() {
        ExportEbook export = new ExportEbook(properties);
        export.addBlurListener(this);
        setBlur(true);
        export.setVisible(true);
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
            labelCover.setIcon(icon);
        } catch (Exception ex) {
        }
    }

    @Override
    public void setBlur(boolean b) {
        getGlassPane().setVisible(b);
    }
}

