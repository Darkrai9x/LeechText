package dark.leech.text.gui.components.setting;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.MDialog;
import dark.leech.text.gui.components.MLabelCheckBox;
import dark.leech.text.gui.components.MPanel;
import dark.leech.text.gui.components.MScrollBar;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.gui.components.button.CircleButton;
import dark.leech.text.gui.components.button.CloseButton;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ItemStyle extends MPanel {
    private JLabel labelTitle;
    private JLabel labelInfo;
    private MLabelCheckBox labelSelected;
    private CircleButton Edit;
    private boolean selected;
    private String name;
    private String tip;
    private String style;
    private String text;
    private MouseAdapter mouse = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() != Edit)
                setSelected(!selected);
            if (e.getSource() == Edit) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EditDialog edit = new EditDialog(name, text, style);
                        text = edit.getText();
                    }
                });
                t.start();
            }
        }
    };

    public ItemStyle() {
        this("", "", false);
    }

    public ItemStyle(String name, String tip, boolean selected) {
        this.name = name;
        this.tip = tip;
        gui();
        setName(name);
        setTip(tip);
        setSelected(false);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        labelSelected.setSelected(selected);
        Edit.setVisible(selected);
        this.selected = selected;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setName(String name) {
        labelTitle.setText(name);
        this.name = name;
    }

    public void setTip(String tip) {
        labelInfo.setText(tip);
        this.tip = tip;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private void gui() {
        labelTitle = new JLabel();
        labelInfo = new JLabel();
        labelSelected = new MLabelCheckBox();

        //  setBorder(new DropShadowBorder(new Color(63, 81, 181), 3, 0.1f, 5, true, true, true, true));
        setBackground(Color.white);
        setLayout(null);

        // ---- labelTitle ----
        labelTitle.setFont(FontConstants.textBold);
        add(labelTitle);
        labelTitle.setBounds(25, 5, 290, 30);

        // ---- labelInfo ----
        add(labelInfo);
        labelInfo.setFont(FontConstants.textThin);
        labelInfo.setForeground(Color.GRAY);
        labelInfo.setBounds(25, 30, 290, 30);

        // ---- labelSelected ----
        add(labelSelected);
        labelSelected.setBounds(335, 15, 30, 30);

        // ---- labelEdit ----
        Edit = new CircleButton("\ue254");
        Edit.setForeground(ColorConstants.THEME_COLOR);
        Edit.addMouseListener(mouse);
        Edit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(Edit);
        Edit.setBound(300, 15, 30, 30);
        addMouseListener(mouse);
        setPreferredSize(new Dimension(370, 60));
    }

}

class EditDialog extends MDialog {
    private BasicButton ok;
    private BasicButton cancel;
    private RSyntaxTextArea edit;
    private String text;
    private String title;
    private String type;

    public EditDialog(String title, String text, String type) {
        this.title = title;
        this.text = text;
        this.type = type;
        setSize(360, 410);
        new Thread(new Runnable() {
            @Override
            public void run() {
                gui();
                repaint();
            }
        }).start();
        setCenter();
        display();
        setVisible(true);
    }


    private void gui() {
        JPanel panelTitle = new JPanel();
        JLabel labelTitle = new JLabel();
        CloseButton buttonClose = new CloseButton();
        ok = new BasicButton();
        cancel = new BasicButton();

        Container container = getContentPane();
        container.setBackground(Color.white);

        panelTitle.setBackground(ColorConstants.THEME_COLOR);
        panelTitle.setLayout(null);
        //---- labelTitle ----
        labelTitle.setText("    " + title);
        labelTitle.setFont(FontConstants.titleNomal);
        labelTitle.setForeground(Color.WHITE);
        panelTitle.add(labelTitle);
        labelTitle.setBounds(0, 0, 315, 45);

        //---- buttonClose ----
        buttonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickClose();
            }
        });
        panelTitle.add(buttonClose);
        buttonClose.setBound(325, 10, 25, 25);
        container.add(panelTitle);
        panelTitle.setBounds(0, 0, 360, 45);

        ok.setText("OK");
        container.add(ok);
        ok.setBound(180, 365, 70, 30);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickOk();
            }
        });
        cancel.setText("HỦY");
        container.add(cancel);
        cancel.setBound(270, 365, 70, 30);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickClose();
            }
        });

        edit = new RSyntaxTextArea();
        edit.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        edit.setSyntaxEditingStyle(type);
        edit.setLineWrap(true);
        edit.setWrapStyleWord(true);
        edit.setMarginLineEnabled(true);
        edit.setCodeFoldingEnabled(true);
        edit.setFont(FontConstants.codeFont(12f));
        edit.setText(text);


        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(edit);
        scrollPane.setBorder(null);

        JScrollBar sb = scrollPane.getVerticalScrollBar();
        sb.setUI(new MScrollBar());
        sb.setBackground(Color.WHITE);
        sb.setPreferredSize(new Dimension(10, 0));
        container.add(scrollPane);
        scrollPane.setBounds(10, 45, 348, 310);
    }

    public String getText() {
        return text;
    }


    private void clickOk() {
        text = edit.getText();
        close();
    }

    private void clickClose() {
        close();
    }


}
