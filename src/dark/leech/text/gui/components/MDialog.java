package dark.leech.text.gui.components;

import dark.leech.text.constant.Constants;
import dark.leech.text.gui.App;
import dark.leech.text.listeners.BlurListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class MDialog extends JDialog implements BlurListener {
    private Point pointLocation;
    private BlurListener blurListener;


    public MDialog() {
        super(App.M);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        setGlassPane(new JComponent() {
            public void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 40));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        });
        setModal(true);
        setResizable(false);
        setUndecorated(true);
        setOpacity(0.0f);
    }

    public void setCenter() {
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5));
        pointLocation = Constants.LOCATION;
        setLocation(pointLocation.x + 390 / 2 - getWidth() / 2, pointLocation.y + 600 / 2 - getHeight() / 2);
    }


    public void display() {

        if (blurListener != null)
            blurListener.setBlur(true);
        else
            App.blur(true);
        Animation.fadeIn(this);
    }

    public void addBlurListener(BlurListener blurListener) {
        this.blurListener = blurListener;
    }

    public void close() {
        if (blurListener != null)
            blurListener.setBlur(false);
        else
            App.blur(false);
        Animation.fadeOut(this);
    }

    @Override
    public void setBlur(boolean b) {
        getGlassPane().setVisible(b);
    }
}
