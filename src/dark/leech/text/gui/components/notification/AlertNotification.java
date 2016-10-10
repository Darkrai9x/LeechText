package dark.leech.text.gui.components.notification;

import dark.leech.text.constant.Constants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.gui.components.button.CloseButton;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Long on 9/20/2016.
 */
public class AlertNotification extends JDialog {
    private CloseButton labelClose;
    private JLabel labelTitle;
    private BasicButton buttonOk;
    private JTextPane textPane;
    private String text;

    public AlertNotification(String text) {
        this.text = text;
        setAlwaysOnTop(true);
        setModal(true);
        setUndecorated(true);
        gui();

    }

    private void gui() {

        Container panel = getContentPane();
        labelTitle = new JLabel();
        buttonOk = new BasicButton();
        textPane = new JTextPane();

        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        //---- labelClose ----
        labelClose = new CloseButton();
        labelClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(labelClose);
        labelClose.setBound(290, 5, 25, 25);

        //---- labelTitle ----
        labelTitle.setText("Có lỗi xảy ra!");
        labelTitle.setFont(FontConstants.textBold);
        panel.add(labelTitle);
        labelTitle.setBounds(15, 0, 245, 35);

        //---- buttonOk ----
        buttonOk.setText("OK");
        buttonOk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        panel.add(buttonOk);
        buttonOk.setBound(10, 110, 280, 35);

        //---- text ----
        textPane.setText(text);
        textPane.setBorder(null);
        textPane.setBackground(Color.WHITE);
        textPane.setEditable(false);
        textPane.setFont(FontConstants.textNomal);
        panel.add(textPane);
        textPane.setBounds(20, 35, 285, 65);
        panel.setPreferredSize(new Dimension(320, 155));
        getRootPane().setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        pack();
        Point pointLocation = Constants.LOCATION;
        setLocation(pointLocation.x + 390 / 2 - getWidth() / 2, pointLocation.y + 600 / 2 - getHeight() / 2);
    }
}
