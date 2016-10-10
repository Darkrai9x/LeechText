package dark.leech.text.constant;

import java.awt.*;

/**
 * Created by Long on 10/3/2016.
 */
public class ColorConstants {
    public static Color THEME_COLOR = SettingConstants.THEME_COLOR;
    public static Color BAR = new Color(getValue(THEME_COLOR.getRed() - 20), getValue(THEME_COLOR.getGreen() - 20), getValue(THEME_COLOR.getBlue() - 15));
    public static Color BUTTON_TEXT = THEME_COLOR;
    public static Color BUTTON_CLICK = BUTTON_TEXT.brighter();

    private static int getValue(int i) {
        return (i > 0) ? i : 0;
    }
}
