package dark.leech.text.gui.components.button;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Long on 9/30/2016.
 */
public class CloseButton extends JButton {
    public CloseButton() {
        setText("");
        setForeground(Color.WHITE);
        setFont(FontConstants.iconNomal);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    public void setBound(int x, int y, int w, int h) {
        setBounds(x, y, w, h);
        StyledButtonUI styledButton = new StyledButtonUI(new Dimension(w, h), true);
        Color bc = ColorConstants.BUTTON_CLICK;
        Color rolloverBackground = new Color(bc.getRed(), bc.getGreen(), bc.getBlue(), 200);
        styledButton.setRolloverBackground(rolloverBackground);
        styledButton.setPressedBackground(rolloverBackground);
        setUI(styledButton);
    }

}
