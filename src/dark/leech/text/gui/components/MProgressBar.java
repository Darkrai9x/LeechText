package dark.leech.text.gui.components;

import dark.leech.text.constant.ColorConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Long on 9/9/2016.
 */
public class MProgressBar extends JPanel {
    private int percent = 0;

    public MProgressBar() {
        setBackground(Color.WHITE);
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        int loadw = percent * w / 100;
        g.setColor(new Color(204, 204, 255));
        g.fillRect(0, 0, w, h);
        g.setColor(ColorConstants.THEME_COLOR);
        g.fillRect(0, 0, loadw, h);
    }
}
