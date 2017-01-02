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
public class EditDialog extends Dialog {
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
        onCreate();
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        JPanel panelTitle = new JPanel();
        JLabel labelTitle = new JLabel();
        CloseButton buttonClose = new CloseButton();
        ok = new BasicButton();
        cancel = new BasicButton();

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
                close();
            }
        });
        panelTitle.add(buttonClose);
        buttonClose.setBounds(325, 10, 25, 25);
        container.add(panelTitle);
        panelTitle.setBounds(0, 0, 360, 45);

        ok.setText("OK");
        container.add(ok);
        ok.setBounds(180, 365, 70, 30);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickOk();
            }
        });
        cancel.setText("HỦY");
        container.add(cancel);
        cancel.setBounds(270, 365, 70, 30);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
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

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setViewportView(edit);
        container.add(scrollPane);
        scrollPane.setBounds(10, 45, 348, 310);
        this.setSize(360, 410);
    }




    public String getText() {
        return text;
    }


    private void clickOk() {
        text = edit.getText();
        close();
    }



}
