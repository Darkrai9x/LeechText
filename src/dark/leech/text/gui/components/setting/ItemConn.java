package dark.leech.text.gui.components.setting;

import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.Panel;
import dark.leech.text.gui.components.TextField;

import javax.swing.*;
import java.awt.*;

public class ItemConn extends Panel {
    private String title;
    private TextField textField;

    public ItemConn(String title, String text) {
        this.title = title;
        gui();
        setText(text);
    }

    private void gui() {
        setBackground(Color.white);
        setLayout(null);
        JLabel labelName = new JLabel(title);
        labelName.setFont(FontConstants.textBold);
        add(labelName);
        labelName.setBounds(25, 5, 250, 30);
        textField = new TextField(300, 3, 60, 35);
        add(textField);
        setPreferredSize(new Dimension(370, 40));
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }
}
