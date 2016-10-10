package dark.leech.text.gui.components.button;

import dark.leech.text.constant.ColorConstants;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

/**
 * Created by Long on 9/30/2016.
 */
class StyledButtonUI extends BasicButtonUI {
    private Dimension size;
    private boolean round;
    private Color rolloverBackground;
    private Color pressedBackground;
    private Color defaultBackground;

    public StyledButtonUI() {
        Color bc = ColorConstants.BUTTON_CLICK;
        rolloverBackground = new Color(bc.getRed(), bc.getGreen(), bc.getBlue());
        pressedBackground = new Color(bc.getRed(), bc.getGreen(), bc.getBlue(), 100);
        defaultBackground = new Color(0, 0, 0, 0);
        round = false;
    }

    public StyledButtonUI(Dimension size, boolean round) {
        this();
        this.round = round;
        this.size = size;
    }

    public void setRound(boolean round) {
        this.round = round;
    }

    public void setRolloverBackground(Color rolloverBackground) {
        this.rolloverBackground = rolloverBackground;
    }

    public void setPressedBackground(Color pressedBackground) {
        this.pressedBackground = pressedBackground;
    }

    public void setDefaultBackground(Color defaultBackground) {
        this.defaultBackground = defaultBackground;
    }


    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        AbstractButton button = (AbstractButton) c;
        if (size == null) size = c.getSize();
        button.setSize(size);
        button.setOpaque(false);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        paintBackground(g, b, b.getModel().isRollover(), b.getModel().isPressed(), b.getModel().isSelected());
        super.paint(g, c);
    }

    private void paintBackground(Graphics g, JComponent c, boolean rollover, boolean pressed, boolean clicked) {
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(rh);
        g2.setColor(defaultBackground);
        if (rollover)
            g2.setColor(rolloverBackground);
        if (clicked || pressed)
            g2.setColor(pressedBackground);
        if (round) g.fillOval(size.width / 2 - size.height / 2, 0, size.height, size.height);
        else
            g2.fillRoundRect(0, 0, size.width, size.height, 5, 5);
        // g2.dispose();
    }

}
