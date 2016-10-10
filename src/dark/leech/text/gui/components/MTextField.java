package dark.leech.text.gui.components;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.MinimalBalloonStyle;
import net.java.balloontip.utils.TimingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class MTextField extends JPanel {
    private JLabel label;
    private JTextField textField;

    public MTextField() {
        gui();
    }

    public MTextField(int x, int y, int w, int h) {
        gui();
        setBounds(x, y, w, h);
        textField.setBounds(0, 0, w, h - 3);
        label.setBounds(0, h - 3, w, 2);

    }

    public void gui() {
        setLayout(null);
        setBackground(new Color(0, 0, 0, 0));
        textField = new JTextField();
        //  textField.setBackground(new Color(0, 0, 0, 0));
        label = new JLabel();
        textField.setBorder(null);
        textField.setFont(FontConstants.textNomal);
        textField.setForeground(Color.GRAY);
        textField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                textField.setForeground(ColorConstants.THEME_COLOR);
                label.setBackground(ColorConstants.THEME_COLOR);
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setForeground(Color.GRAY);
                label.setBackground(Color.LIGHT_GRAY);
            }
        });
        add(textField);

        // ---- label6 ----
        label.setBackground(Color.LIGHT_GRAY);
        label.setOpaque(true);
        add(label);
    }

    public void addError(String err) {
        Color color = ColorConstants.THEME_COLOR;
        BalloonTipStyle style = new MinimalBalloonStyle(new Color(color.getRed(), color.getGreen(),color.getBlue(), 200), 5);
        final BalloonTip balloonTip = new BalloonTip(
                textField,
                new JLabel("<html><font color=\"#ffffff\">" + err + "</font></html>"),
                style,
                BalloonTip.Orientation.LEFT_ABOVE,
                BalloonTip.AttachLocation.ALIGNED,
                30, 10,
                false
        );

        // Add a close button that hides the balloon tip, rather than permanently close it
        //  balloonTip.setCloseButton(BalloonTip.getDefaultCloseButton(), true);

        TimingUtils.showTimedBalloon(balloonTip, 3000);
        // label.setBackground(Color.RED);
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);

    }

    public void setTextFieldEnabled(boolean enabled) {
        textField.setVisible(enabled);
    }
}


