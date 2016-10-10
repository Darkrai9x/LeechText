package dark.leech.text.gui.components.button;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Long on 9/30/2016.
 */
public class CircleButton extends JButton {
    public CircleButton(String text, float fs) {
        setText(text);
        setForeground(Color.WHITE);
        setFont(FontConstants.iconNomal.deriveFont(Font.BOLD, fs));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }
    public CircleButton(String text) {
        setText(text);
        setForeground(Color.WHITE);
        setFont(FontConstants.iconNomal);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    public void setBound(int x, int y, int w, int h) {
        setBounds(x, y, w, h);
        Color bc = ColorConstants.BUTTON_CLICK;
        Color background = new Color(bc.getRed(), bc.getGreen(), bc.getBlue(), 100);
        StyledButtonUI styledButton = new StyledButtonUI(new Dimension(w, h), true);
        styledButton.setRolloverBackground(background);
        setUI(styledButton);
    }
}
