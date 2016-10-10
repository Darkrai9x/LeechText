package dark.leech.text.gui.components.setting;

import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.MDialog;
import dark.leech.text.gui.components.MPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Long on 10/7/2016.
 */
public class Theme extends MPanel {
    private Color color;
    private JPanel colorPn;

    public Theme(Color color) {
        this.color = color;
        gui();
    }

    private void gui() {
        setBackground(Color.white);
        setLayout(null);
        JLabel labelName = new JLabel("Chủ đề");
        labelName.setFont(FontConstants.textBold);
        add(labelName);
        labelName.setBounds(25, 5, 250, 30);
        colorPn = new JPanel();
        colorPn.setBackground(color);
        colorPn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colorPn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickChooserColor();
            }
        });
        add(colorPn);
        colorPn.setBounds(335, 5, 30, 30);
        setPreferredSize(new Dimension(370, 40));
    }

    private void clickChooserColor() {
        ChooserColor chooserColor = new ChooserColor(color);
        chooserColor.setVisible(true);
        this.color = chooserColor.getChooserColor();
        colorPn.setBackground(color);

    }

    public void setThemeColor(Color color) {
        colorPn.setBackground(color);
        this.color = color;
    }

    public String getHexColor() {
        String hex = Integer.toHexString(color.getRGB() & 0xffffff);
        if (hex.length() < 6) {
            hex = "0" + hex;
        }
        hex = "#" + hex;
        return hex;
    }
}

class ChooserColor extends MDialog {
    private final Color DARK_THEME = new Color(38, 50, 56);
    private final Color RED_THEME = new Color(244, 67, 54);
    private final Color INDIGO_THEME = new Color(63, 81, 181);
    private final Color BLUE_THEME = new Color(33, 150, 243);
    private final Color PURPLE_THEME = new Color(156, 39, 176);
    private final Color DEEP_PURPLE_THEME = new Color(103, 58, 183);
    private final Color TEAL_THEME = new Color(0, 150, 136);
    private final Color GREEN_THEME = new Color(76, 175, 80);
    private final Color BROWN_THEME = new Color(121, 85, 72);
    private Color color;

    public ChooserColor(Color color) {
        this.color = color;
        setLayout(new FlowLayout());
        add(new ColorPane(DARK_THEME));
        add(new ColorPane(RED_THEME));
        add(new ColorPane(INDIGO_THEME));
        add(new ColorPane(BLUE_THEME));
        add(new ColorPane(PURPLE_THEME));
        add(new ColorPane(DEEP_PURPLE_THEME));
        add(new ColorPane(TEAL_THEME));
        add(new ColorPane(GREEN_THEME));
        add(new ColorPane(BROWN_THEME));
        setSize(160, 160);
        setCenter();
        display();
    }


    public Color getChooserColor() {
        return color;
    }

    public class ColorPane extends JPanel {
        public ColorPane(Color color) {
            setPreferredSize(new Dimension(45, 45));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    choose();
                }
            });
            setBackground(color);
        }

        private void choose() {
            color = getBackground();
            close();
        }
    }
}
