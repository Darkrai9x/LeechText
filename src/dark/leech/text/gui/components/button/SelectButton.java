package dark.leech.text.gui.components.button;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Long on 10/20/2016.
 */
public class SelectButton extends JButton {
    private boolean selected;

    public SelectButton() {
        setText("\ue834");
        setBackground(Color.WHITE);
        setForeground(ColorConstants.BUTTON_TEXT);
        setFont(FontConstants.iconNomal);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    public void setSelected(boolean selected) {
        setText(selected ? "\ue834" : "\ue835");
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
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
