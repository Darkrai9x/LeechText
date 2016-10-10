package dark.leech.text.gui.components;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;

import javax.swing.*;

public class MLabelCheckBox extends JLabel {
    private boolean selected;

    public MLabelCheckBox() {
        this(false);
    }

    public MLabelCheckBox(boolean selected) {
        this.selected = selected;
        setSelected(selected);
        setForeground(ColorConstants.THEME_COLOR);
        setFont(FontConstants.iconNomal);
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        setText(selected ? "\ue834" : "\ue835");
        this.selected = selected;
    }


}
