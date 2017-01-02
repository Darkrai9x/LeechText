package dark.leech.text.gui;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.Constants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.Dialog;
import dark.leech.text.gui.components.EditDialog;
import dark.leech.text.gui.components.Panel;
import dark.leech.text.gui.components.TextField;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.gui.components.button.CircleButton;
import dark.leech.text.gui.components.button.CloseButton;
import dark.leech.text.gui.components.button.SelectButton;
import dark.leech.text.gui.export.ConfigUI;
import dark.leech.text.gui.export.ExportEbook;
import dark.leech.text.gui.export.ExportText;
import dark.leech.text.listeners.BlurListener;
import dark.leech.text.models.FileAction;
import dark.leech.text.models.Properties;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

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

public class InfoUI extends Dialog implements BlurListener {
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
    private TextField textFieldName;
    private JLabel labelAuthor;
    private TextField textFieldAuthor;
    private JLabel labelStatus;
    private GioiThieu gioiThieu;

    private Properties properties;

    public InfoUI(Properties properties) {
        this.properties = properties;
        onCreate();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
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
        gioiThieu = new GioiThieu(properties);


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
                hide();
            }
        });
        title.add(buttonClose);
        buttonClose.setBounds(310, 10, 25, 25);

        container.add(title);
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
        labelEdit.setBounds(70, 120, 30, 30);

        //---- labelCover ----
        panelCover.add(labelCover);
        labelCover.setBounds(0, 0, 100, 150);


        container.add(panelCover);
        panelCover.setBounds(5, 50, 100, 150);

        //---- buttonConfig ----
        buttonConfig.setText("Hiệu Chỉnh");
        buttonConfig.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doConfig();
            }
        });
        container.add(buttonConfig);
        buttonConfig.setBounds(10, 215, 100, 35);

        //---- buttonText ----
        buttonText.setText("Xuất Text");
        buttonText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exportText();
            }
        });
        container.add(buttonText);
        buttonText.setBounds(115, 215, 100, 35);

        //---- buttonExport ----
        buttonExport.setText("Xuất Ebook");
        buttonExport.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exportEbook();
            }
        });
        container.add(buttonExport);
        buttonExport.setBounds(225, 215, 100, 35);

        //---- labelName ----
        labelName.setText("Tên truyện");
        labelName.setFont(FontConstants.textNomal);
        container.add(labelName);
        labelName.setBounds(115, 50, 220, 20);
        textFieldName = new TextField(115, 75, 220, 30);
        textFieldName.setText(properties.getName());
        container.add(textFieldName);


        //---- labelAuthor ----
        labelAuthor.setText("Tác giả");
        labelAuthor.setFont(FontConstants.textNomal);
        container.add(labelAuthor);
        labelAuthor.setBounds(115, 110, 220, 20);
        textFieldAuthor = new TextField(115, 135, 220, 30);
        textFieldAuthor.setText(properties.getAuthor());
        container.add(textFieldAuthor);
        //----GioiThieu----
        container.add(gioiThieu);
        gioiThieu.addBlurListener(this);
        gioiThieu.setBounds(115, 170, 220, 30);

        //---- labelStatus ----
        labelStatus.setFont(FontConstants.textNomal);
        container.add(labelStatus);
        labelStatus.setBounds(115, 170, 220, 30);
        try {
            if (new File(properties.getSavePath() + Constants.l + "data" + Constants.l + "cover.jpg").exists())
                setCover(new FileInputStream(properties.getSavePath() + Constants.l + "data" + Constants.l + "cover.jpg"));
        } catch (FileNotFoundException e) {
        }
        this.setSize(340, 265);
    }

    private void doConfig() {
        properties.setName(textFieldName.getText());
        properties.setAuthor(textFieldAuthor.getText());
        ConfigUI configUI = new ConfigUI(properties);
        configUI.setBlurListener(this);
        setBlur(true);
        configUI.setVisible(true);
    }

    private void exportText() {
        properties.setName(textFieldName.getText());
        properties.setAuthor(textFieldAuthor.getText());
        ExportText export = new ExportText(properties);
        export.setBlurListener(this);
        setBlur(true);
        export.setVisible(true);
    }

    private void exportEbook() {
        properties.setName(textFieldName.getText());
        properties.setAuthor(textFieldAuthor.getText());
        ExportEbook export = new ExportEbook(properties);
        export.setBlurListener(this);
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

class GioiThieu extends Panel {
    private JLabel labelName;
    private CircleButton buttonEdit;
    private SelectButton btSelect;
    private Properties properties;
    private BlurListener blurListener;

    public GioiThieu(Properties properties) {
        this.properties = properties;
        gui();
    }

    private void gui() {
        setBackground(Color.white);
        setLayout(null);
        labelName = new JLabel();
        btSelect = new SelectButton();

        labelName.setText("Thêm trang giới thiệu");
        labelName.setFont(FontConstants.textNomal);
        add(labelName);
        labelName.setBounds(0, 0, 150, 30);

        buttonEdit = new CircleButton("\ue254");
        buttonEdit.setForeground(ColorConstants.THEME_COLOR);
        buttonEdit.setToolTipText("Sửa");

        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doEdit();
            }
        });
        add(buttonEdit);
        buttonEdit.setBounds(160, 0, 30, 30);
        buttonEdit.setVisible(false);
        btSelect.setSelected(false);
        add(btSelect);
        btSelect.setBounds(190, 0, 30, 30);
        btSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSelect();
            }
        });


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doSelect();
            }
        });
        //setBorder(new DropShadowBorder(new Color(63, 81, 181), 3, 0.4f, 15, true, true, true, true));
        setPreferredSize(new Dimension(300, 40));
    }

    private void doSelect() {
        btSelect.setSelected(!btSelect.isSelected());
        buttonEdit.setVisible(btSelect.isSelected());
        properties.setAddGt(btSelect.isSelected());
        if (properties.isAddGt())
            if (!new File(properties.getGioiThieu(), properties.getSavePath() + Constants.l + "raw" + Constants.l + "gioithieu.txt").exists())
                new FileAction().string2file(properties.getGioiThieu(), properties.getSavePath() + Constants.l + "raw" + Constants.l + "gioithieu.txt");
    }

    private void doEdit() {
        EditDialog editDialog = new EditDialog("Giới thiệu", properties.getGioiThieu(), SyntaxConstants.SYNTAX_STYLE_HTML);
        editDialog.setBlurListener(blurListener);
        editDialog.setVisible(true);
        properties.setGioiThieu(editDialog.getText());
        if (properties.isAddGt())
            new FileAction().string2file(properties.getGioiThieu(), properties.getSavePath() + Constants.l + "raw" + Constants.l + "gioithieu.txt");
    }

    public void addBlurListener(BlurListener blurListener) {
        this.blurListener = blurListener;
    }
}
