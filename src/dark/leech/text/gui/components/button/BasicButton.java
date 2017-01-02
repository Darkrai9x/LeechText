package dark.leech.text.gui.components.button;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Long on 9/30/2016.
 */
public class BasicButton extends JButton {
    public BasicButton() {
        setFont(FontConstants.textBold);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setBackground(Color.WHITE);
        setForeground(ColorConstants.BUTTON_TEXT);
    }


    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        ButtonUI styledButtonUI = new ButtonUI();
        styledButtonUI.setRolloverBackground(new Color(235, 235, 235));
        setUI(styledButtonUI);
    }
}
