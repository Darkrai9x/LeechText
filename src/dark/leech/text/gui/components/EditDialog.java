package dark.leech.text.gui.components;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.gui.components.button.CloseButton;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Long on 10/28/2016.
 */
public class EditDialog extends MDialog {
    private BasicButton ok;
    private BasicButton cancel;
    private RSyntaxTextArea edit;
    private String text;
    private String title;
    private String type;

    public EditDialog(String title, String text, String type) {
        this.title = title;
        this.text = text;
        this.type = type;
        setSize(360, 410);
        new Thread(new Runnable() {
            @Override
            public void run() {
                gui();
                repaint();
            }
        }).start();
        setCenter();
        display();
        setVisible(true);
    }


    private void gui() {
        JPanel panelTitle = new JPanel();
        JLabel labelTitle = new JLabel();
        CloseButton buttonClose = new CloseButton();
        ok = new BasicButton();
        cancel = new BasicButton();

        Container container = getContentPane();
        container.setBackground(Color.white);
        container.setLayout(null);
        panelTitle.setBackground(ColorConstants.THEME_COLOR);
        panelTitle.setLayout(null);
        //---- labelTitle ----
        labelTitle.setText("    " + title);
        labelTitle.setFont(FontConstants.titleNomal);
        labelTitle.setForeground(Color.WHITE);
        panelTitle.add(labelTitle);
        labelTitle.setBounds(0, 0, 315, 45);

        //---- buttonClose ----
        buttonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickClose();
            }
        });
        panelTitle.add(buttonClose);
        buttonClose.setBound(325, 10, 25, 25);
        container.add(panelTitle);
        panelTitle.setBounds(0, 0, 360, 45);

        ok.setText("OK");
        container.add(ok);
        ok.setBound(180, 365, 70, 30);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickOk();
            }
        });
        cancel.setText("HỦY");
        container.add(cancel);
        cancel.setBound(270, 365, 70, 30);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickClose();
            }
        });

        edit = new RSyntaxTextArea();
        edit.setText(text);
        edit.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        edit.setSyntaxEditingStyle(type);
        edit.setLineWrap(true);
        edit.setWrapStyleWord(true);
        edit.setMarginLineEnabled(true);
        edit.setCodeFoldingEnabled(true);
        edit.setFont(FontConstants.codeFont(12f));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(edit);
        scrollPane.setBorder(null);

        JScrollBar sb = scrollPane.getVerticalScrollBar();
        sb.setUI(new MScrollBar());
        sb.setBackground(Color.WHITE);
        sb.setPreferredSize(new Dimension(10, 0));
        container.add(scrollPane);
        scrollPane.setBounds(10, 45, 348, 310);
    }

    public String getText() {
        return text;
    }


    private void clickOk() {
        text = edit.getText();
        close();
    }

    private void clickClose() {
        close();
    }


}
