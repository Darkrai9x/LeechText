package dark.leech.text.gui;

import dark.leech.text.action.Settings;
import dark.leech.text.constant.Constants;

import javax.swing.*;
import java.awt.*;

public class App {

    public static MainUI mainFrame;
    private static int blurCount;

    static {
        Constants.l = "/";
        if (System.getProperty("os.name").toLowerCase().indexOf("win") != -1)
            Constants.l = "\\";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {

                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                } catch (Exception ex) {
                }
                new Settings().doLoad();
                mainFrame = new MainUI();
                mainFrame.flyIn();
                mainFrame.setVisible(true);
                Constants.LOCATION = new Point(mainFrame.getLocation().x, mainFrame.getLocation().y + 20);
            }
        });


    }

    public static void blur(boolean bl) {
        if (blurCount == 0 || blurCount == 1)
            mainFrame.getRootPane().getGlassPane().setVisible(bl);
        blurCount += bl ? 1 : -1;
    }


}
