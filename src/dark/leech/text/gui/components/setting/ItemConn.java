package dark.leech.text.gui.components.setting;

import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.MPanel;
import dark.leech.text.gui.components.MTextField;

import javax.swing.*;
import java.awt.*;

public class ItemConn extends MPanel {
    private String title;
    private MTextField textField;

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
        textField = new MTextField(300, 3, 60, 35);
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
