package dark.leech.text.gui.components.notification;

import dark.leech.text.constant.FontConstants;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Long on 9/17/2016.
 */
public class BasicNotification extends JWindow {
    public static int pointX;
    public static int pointY;
    private String name;
    private String noti;
    private Icon cover;
    private int time;
    private boolean close = false;

    public BasicNotification(String name, String noti, Icon cover, int time, String path) {
        this.name = name;
        this.noti = noti;
        this.cover = cover;
        this.time = time;
        gui();
    }

    public BasicNotification(String name, String noti, Icon cover) {

        this.name = name;
        this.noti = noti;
        this.cover = cover;
        gui();
    }

    public BasicNotification(String name, String noti, Icon cover, int time) {
        this.name = name;
        this.noti = noti;
        this.cover = cover;
        this.time = time;
        gui();
    }

    private void gui() {

        setAlwaysOnTop(true);
        setSize(280, 90);
        JLabel labelCover = new JLabel();
        JLabel labelName = new JLabel();
        JLabel labelClose = new JLabel();
        JLabel labelNoti = new JLabel();
        JLabel labelTime = new JLabel();
        Container panel1 = getContentPane();
        panel1.setBackground(Color.WHITE);
        panel1.setLayout(null);
        //---- labelCover ----
        labelCover.setIcon(cover);
        labelCover.setOpaque(true);
        panel1.add(labelCover);
        labelCover.setBounds(5, 5, 54, 81);

        //---- labelName ----
        labelName.setText(name);
        labelName.setFont(FontConstants.textNomal);
        panel1.add(labelName);
        labelName.setBounds(70, 5, 175, 30);

        //---- labelClose ----
        labelClose.setText("\ue5cd");
        labelClose.setFont(FontConstants.iconNomal);
        labelClose.setHorizontalAlignment(SwingConstants.CENTER);
        labelClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doClose();
            }
        });
        panel1.add(labelClose);
        labelClose.setBounds(255, 0, 25, 25);

        //---- labelNoti ----
        labelNoti.setText(noti);
        labelNoti.setFont(FontConstants.textNomal);
        panel1.add(labelNoti);
        labelNoti.setBounds(70, 35, 170, 30);

        //---- labelTime ----
        Date todaysDate = new Date();
        DateFormat df = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
        labelTime.setText(df.format(todaysDate));
        labelTime.setFont(FontConstants.textThin);
        labelTime.setHorizontalAlignment(SwingConstants.RIGHT);
        panel1.add(labelTime);
        labelTime.setBounds(130, 65, 140, 20);
        getRootPane().setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
    }


    public void showNoti() {
        setVisible(true);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        if (pointX == 0) pointX = width;
        if (pointY == 0) pointY = height - getHeight() - 50;
        int x = pointX;
        int y = pointY;
        pointY -= getHeight() + 5;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < getWidth() + 10; i++) {
                    setLocation(x - i, y);
                    repaint();
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                    }
                }
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                }
                if (!close) doClose();
            }
        }).start();
    }

    private void doClose() {
        if (close) return;
        close = true;
        int x = getLocation().x;
        int y = getLocation().y;
        pointY = y;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < getWidth() + 10; i++) {
                    setLocation(x + i, y);
                    repaint();
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                    }
                }
                dispose();
            }
        }).start();
    }

}
