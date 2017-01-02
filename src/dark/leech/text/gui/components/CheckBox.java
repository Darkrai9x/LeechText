package dark.leech.text.gui.components;

import dark.leech.text.constant.FontConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Long on 9/10/2016.
 */
public class CheckBox extends Panel {
    private String name;
    private MLabelCheckBox checkbox;

    public CheckBox(String name) {
        this.name = name;
        gui();
    }

    private void gui() {
        setLayout(new BorderLayout());
        JLabel labelName = new JLabel(name);
        labelName.setFont(FontConstants.textNomal);
        add(labelName, BorderLayout.WEST);

        checkbox = new MLabelCheckBox();
        checkbox.setFont(FontConstants.iconNomal);
        add(checkbox, BorderLayout.EAST);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelected(!isSelected());
            }
        });
    }

    public boolean isSelected() {
        return checkbox.isSelected();
    }

    public void setSelected(boolean selected) {
        checkbox.setSelected(selected);
    }
}
