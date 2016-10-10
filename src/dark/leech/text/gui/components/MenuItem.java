package dark.leech.text.gui.components;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;

import javax.swing.*;
import java.awt.*;

public class MenuItem extends JMenuItem {
    private String text;
    private JLabel label;

    public MenuItem(String text) {
        this.text = text;
        label = new JLabel();
        label.setText(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(FontConstants.textNomal);
        setBackground(Color.white);
        setBorderPainted(false);
        setLayout(new GridLayout());
        setPreferredSize(new Dimension(90, 30));
        add(label);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        label.setForeground(ColorConstants.THEME_COLOR);
        if (isArmed()) {
            g2.setColor(ColorConstants.THEME_COLOR);
            label.setForeground(Color.WHITE);
        }
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

}
