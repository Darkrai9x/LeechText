package dark.leech.text.ui.main;

import dark.leech.text.plugin.PluginManager;
import dark.leech.text.ui.notification.Toast;
import dark.leech.text.util.AppUtils;
import dark.leech.text.util.SettingUtils;

import javax.swing.*;
import java.awt.*;

public class App {

    private static MainUI mainFrame;
    private static int openCount;
    private static int closeCount;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                } catch (Exception ex) {
                }
                AppUtils.doLoad();
                SettingUtils.doLoad();
                mainFrame = new MainUI();
                mainFrame.flyIn();
                mainFrame.setVisible(true);
                AppUtils.LOCATION = new Point(mainFrame.getLocation().x, mainFrame.getLocation().y + 20);
            }
        });
    }

    public static MainUI getMain() {
        return mainFrame;
    }


}
