package dark.leech.text.gui.components.setting;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ItemCheckBox extends Panel {
    private JLabel labelName;
    private JLabel labelSelected;
    private boolean selected;
    private String name;

    public ItemCheckBox(String name) {
        this.name = name;
        GUI();
        setSelected(false);

    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        labelSelected.setText(selected ? "\ue834" : "\ue835");
        this.selected = selected;
    }

    private void GUI() {
        setBackground(Color.white);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelected(!selected);
            }
        });
        setLayout(null);
        labelName = new JLabel(name);
        labelSelected = new JLabel();
        labelName.setFont(FontConstants.textBold);
        labelName.setBounds(25, 5, 310, 30);
        labelSelected.setForeground(ColorConstants.THEME_COLOR);
        labelSelected.setFont(FontConstants.iconNomal);
        labelSelected.setHorizontalAlignment(SwingConstants.CENTER);
        labelSelected.setBounds(335, 5, 30, 30);
        add(labelName);
        add(labelSelected);
        setPreferredSize(new Dimension(370, 40));
    }
}
