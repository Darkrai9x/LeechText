package dark.leech.text.gui.components.setting;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.constant.StringConstants;
import dark.leech.text.gui.components.EditDialog;
import dark.leech.text.gui.components.Panel;
import dark.leech.text.gui.components.button.CircleButton;
import dark.leech.text.gui.components.button.SelectButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ItemStyle extends Panel {
    private JLabel labelTitle;
    private JLabel labelInfo;
    private SelectButton btSelect;
    private CircleButton Edit;
    private boolean selected;
    private String name;
    private String tip;
    private String style;
    private String text;

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
        btSelect.setSelected(selected);
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
        btSelect = new SelectButton();

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
        add(btSelect);
        btSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSelected(!selected);
            }
        });
        btSelect.setBounds(335, 15, 30, 30);

        // ---- labelEdit ----
        Edit = new CircleButton(StringConstants.EDIT);
        Edit.setForeground(ColorConstants.THEME_COLOR);
        Edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditDialog edit = new EditDialog(name, text, style);
                edit.open();
                text = edit.getText();
            }
        });
        Edit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(Edit);
        Edit.setBounds(300, 15, 30, 30);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelected(!selected);
            }
        });
        setPreferredSize(new Dimension(370, 60));
    }

}
