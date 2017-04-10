package dark.leech.text.ui;

import dark.leech.text.util.AppUtils;

import javax.swing.*;

public class Animation {

    public static void go(JPanel from, JPanel to) {
        final int y = from.getY();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 20; i >= 0; i--) {
                    from.setLocation(i - 390, y);
                    to.setLocation(i, y);
                    AppUtils.pause(50 / (i + 1));
                }
            }
        }).start();

    }

    public static void fadeIn(JDialog dialog) {
        final int x = dialog.getLocation().x;
        final int y = dialog.getLocation().y - 20;

        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i <= 20; i++) {
                    dialog.setLocation(x, y + i);
                    dialog.repaint();
                    dialog.setOpacity((float) 0.05 * i);
                    AppUtils.pause(i * 2);

                }
            }

        }).start();
    }

    public static void fadeOut(JDialog dialog) {
        dialog.setModal(false);
        final int x = dialog.getLocation().x;
        final int y = dialog.getLocation().y;
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 1; i <= 20; i++) {
                    dialog.setLocation(x, y + i);
                    dialog.repaint();
                    dialog.setOpacity((float) 0.05 * (20 - i));
                    AppUtils.pause(i * 2);
                }
                dialog.dispose();
            }
        }).start();
    }

}
