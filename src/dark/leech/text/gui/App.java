package dark.leech.text.gui;

import dark.leech.text.action.Settings;
import dark.leech.text.constant.Constants;

import javax.swing.*;
import java.awt.*;

public class App {

    public static MainUI M;

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
                M = new MainUI();
                M.flyIn();
                M.setVisible(true);
                Constants.LOCATION = new Point(M.getLocation().x, M.getLocation().y + 20);
            }
        });


    }

    public static void blur(boolean bl) {
        M.getRootPane().getGlassPane().setVisible(bl);
    }


}
