package dark.leech.text.gui.components;

import dark.leech.text.constant.Constants;
import dark.leech.text.gui.App;
import dark.leech.text.listeners.BlurListener;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public abstract class Dialog extends JDialog implements BlurListener {
    private Point pointLocation;
    private BlurListener blurListener;
    protected Container container;

    public Dialog() {
        super(App.mainFrame);
    }

    public Dialog(Dialog owner) {
        super(owner);
    }

    protected void onCreate(Dialog owner) {
        container = this.getContentPane();
        container.setLayout(null);
        container.setBackground(Color.WHITE);
        this.setGlassPane(new JComponent() {
            public void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 50));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        });
        this.setModal(true);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setOpacity(0.0f);
    }

    protected void onCreate() {
        container = this.getContentPane();
        container.setLayout(null);
        container.setBackground(Color.WHITE);
        this.setGlassPane(new JComponent() {
            public void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 50));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        });
        this.setModal(true);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setOpacity(0.0f);
    }


    public void open() {

        if (blurListener != null)
            blurListener.setBlur(true);
        else
            App.blur(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5));
        pointLocation = Constants.LOCATION;
        setLocation(pointLocation.x + 390 / 2 - getWidth() / 2, pointLocation.y + 600 / 2 - getHeight() / 2);
        Animation.fadeIn(this);
        super.setVisible(true);
    }


    protected void close() {
        if (blurListener != null)
            blurListener.setBlur(false);
        else
            App.blur(false);
        Animation.fadeOut(this);
    }

    public void setBlurListener(BlurListener blurListener) {
        this.blurListener = blurListener;
    }

    public void setBlur(boolean b) {
        getGlassPane().setVisible(b);
    }
}
