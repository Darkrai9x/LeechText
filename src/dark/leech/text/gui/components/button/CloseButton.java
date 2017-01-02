package dark.leech.text.gui.components.button;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.constant.StringConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Long on 9/30/2016.
 */
public class CloseButton extends JButton {
    public CloseButton() {
        setText(StringConstants.CLOSE);
        setForeground(Color.WHITE);
        setFont(FontConstants.iconNomal);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        ButtonUI styledButton = new ButtonUI(true);
        Color bc = ColorConstants.BUTTON_CLICK;
        Color rolloverBackground = new Color(bc.getRed(), bc.getGreen(), bc.getBlue(), 200);
        styledButton.setRolloverBackground(rolloverBackground);
        styledButton.setPressedBackground(rolloverBackground);
        setUI(styledButton);
    }

}
