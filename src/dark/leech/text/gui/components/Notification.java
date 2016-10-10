package dark.leech.text.gui.components;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.Constants;
import dark.leech.text.constant.FontConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Notification extends JWindow {
    public Notification(String text) {
        setLayout(new GridLayout());
        setAlwaysOnTop(true);
        getContentPane().setBackground(ColorConstants.THEME_COLOR);
        JLabel label = new JLabel("  " + text + "  ");
        label.setForeground(Color.white);
        label.setFont(FontConstants.titleNomal);
        getContentPane().add(label);
        pack();
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5));
        showNotification();
    }

    public Notification(String image, String text) {

    }

    public Notification(JPanel panel) {

    }

    private void showNotification() {

        final int x = Constants.LOCATION.x + 390 / 2 - getWidth() / 2;
        final int y = Constants.LOCATION.y + 80;
        Thread show = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    setLocation(x, y + i);
                    try {
                        setOpacity((float) i / 10);
                        Thread.sleep(20);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                for (int i = 0; i < 10; i++) {
                    setLocation(x, y - i);
                    try {
                        setOpacity((float) (10 - i) / 10);
                        Thread.sleep(20);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                dispose();
            }

        });
        show.start();
    }

}
