package dark.leech.text.gui.components;

import javax.swing.*;
import java.awt.*;

public class Animation {

    public static void go(JPanel from, JPanel to) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 20; i >= 0; i--) {
                    from.setLocation(i - 390, 25);
                    to.setLocation(i, 25);
                    try {
                        Thread.sleep(50 / (i + 1));
                    } catch (Exception e) {
                    }
                }
            }
        }).start();

    }

    public static void fadeIn(JDialog dialog) {
        final int x = dialog.getLocation().x;
        final int y = dialog.getLocation().y - 20;
        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i <= 20; i++) {
                    dialog.setLocation(x, y + i);
                    dialog.repaint();
                    try {
                        dialog.setOpacity((float) 0.05 * i);
                        Thread.sleep(i * 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        th.start();
    }

    public static void fadeOut(JDialog dialog) {
        dialog.setModal(false);
        final int x = dialog.getLocation().x;
        final int y = dialog.getLocation().y;
        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 1; i <= 20; i++) {
                    dialog.setLocation(x, y + i);
                    dialog.repaint();
                    try {
                        dialog.setOpacity((float) 0.05 * (20 - i));
                        Thread.sleep(i * 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dispose();
            }
        });
        th.start();
    }

}
