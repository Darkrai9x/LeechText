package dark.leech.text.gui.components;

import dark.leech.text.constant.ColorConstants;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;


public class ScrollPane extends JScrollPane {
    public ScrollPane(Component view) {
        super(view);
        JScrollBar sb = getVerticalScrollBar();
        sb.setUI(new iScrollBar());
        sb.setBackground(Color.WHITE);
        sb.setPreferredSize(new Dimension(10, 0));
        setBorder(new LineBorder(ColorConstants.THEME_COLOR));
        getVerticalScrollBar().setUnitIncrement(20);
    }

    public ScrollPane() {
        JScrollBar sb = getVerticalScrollBar();
        sb.setUI(new iScrollBar());
        sb.setBackground(Color.WHITE);
        sb.setPreferredSize(new Dimension(10, 0));
        setBorder(new LineBorder(ColorConstants.THEME_COLOR));
        getVerticalScrollBar().setUnitIncrement(20);
    }
}

class iScrollBar extends BasicScrollBarUI {
    private static final int SCROLL_BAR_ALPHA_ROLLOVER = 150;
    private static final int SCROLL_BAR_ALPHA = 100;
    private static final int THUMB_BORDER_SIZE = 2;
    private static final int THUMB_SIZE = 8;
    private static final Color THUMB_COLOR = ColorConstants.THEME_COLOR;
    private JButton b = new JButton() {

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(0, 0);
        }

    };

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        int alpha = isThumbRollover() ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
        int orientation = scrollbar.getOrientation();
        int x = thumbBounds.x + THUMB_BORDER_SIZE;
        int y = thumbBounds.y + THUMB_BORDER_SIZE;

        int width = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : thumbBounds.width - (THUMB_BORDER_SIZE * 2);
        width = Math.max(width, THUMB_SIZE);

        int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height - (THUMB_BORDER_SIZE * 2) : THUMB_SIZE;
        height = Math.max(height, THUMB_SIZE);


        Graphics2D graphics2D = (Graphics2D) g.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(new Color(THUMB_COLOR.getRed(), THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));
        graphics2D.fillRect(x, y, width, height);
        graphics2D.dispose();
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return b;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return b;
    }
}