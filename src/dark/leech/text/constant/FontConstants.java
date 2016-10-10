package dark.leech.text.constant;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontConstants {

    private static Font iconFont(float size) {
        try {
            InputStream in = Class.class.getResourceAsStream("/dark/leech/res/font/icon.ttf");
            return Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(size);
        } catch (IOException e) {
        } catch (FontFormatException e) {

        }
        return null;
    }

    private static Font titleFont(float size) {
        try {
            InputStream in = Class.class.getResourceAsStream("/dark/leech/res/font/title.ttf");
            // create the font to use. Specify the size!
            return Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(size);
        } catch (IOException e) {
        } catch (FontFormatException e) {

        }
        return null;

    }

    private static Font textFont(float size, int type) {
        try {
            InputStream in = Class.class.getResourceAsStream(
                    "/dark/leech/res/font/" + ((type == Font.BOLD) ? "textbold.ttf" : "textregular.ttf"));
            // create the font to use. Specify the size!
            return Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(size);
        } catch (IOException e) {
        } catch (FontFormatException e) {

        }
        return null;
    }

    public static Font codeFont(float size) {
        try {
            InputStream in = Class.class.getResourceAsStream("/dark/leech/res/font/code.ttf");
            // create the font to use. Specify the size!
            return Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(size);
        } catch (IOException e) {

        } catch (FontFormatException e) {

        }
        return null;
    }
    public static final Font titleBig = titleFont(30f);
    public static final Font titleNomal = titleFont(24f);
    public static final Font iconNomal = iconFont(23f);
    public static final Font textNomal = textFont(14f, Font.PLAIN);
    public static final Font textBold = textFont(16f, Font.BOLD);
    public static final Font textThin = textFont(12f, Font.PLAIN);
}
