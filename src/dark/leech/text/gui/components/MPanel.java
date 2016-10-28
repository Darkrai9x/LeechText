package dark.leech.text.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MPanel extends JPanel {
    private int x;
    private int y;
    private int w;
    private int h;
    private Thread t;
    private Point p;
    private double radius;

    public MPanel() {
        setLayout(null);
        setBackground(Color.WHITE);
        x = y = w = h = 0;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickAnimation();
            }
        });
    }

    private void clickAnimation() {
        int add = 2;
        new Thread() {
            public void run() {
                radius = 2 * Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight() - 4, 2));
                p = getMousePosition();
                final int ad = (p.x < getWidth() / 2) ? 1 : -1;
                w = 0;
                h = 0;
                x = -w / 2 + p.x;
                y = -h / 2 + p.y;
                if (t != null) {
                    if (t.isAlive())
                        return;
                }
                t = new Thread() {
                    public void run() {

                        while (h < radius) {
                            w += add;
                            h += add;
                            x = -w / 2 + p.x + ad;
                            y = -h / 2 + p.y;
                            repaint();
                            try {
                                Thread.sleep(1);
                            } catch (Exception e) {
                            }
                        }
                        w = h = x = y = 0;
                        validate();
                        repaint();
                    }
                };
                t.start();
            }
        }.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(rh);

        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillOval(x, y, w, h);

    }
}
