package dark.leech.text.gui;

import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.MDialog;
import dark.leech.text.gui.components.MTextField;
import dark.leech.text.gui.components.button.BasicButton;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddURL extends MDialog {

    private BasicButton ok;
    private BasicButton cancel;
    private MTextField textField;
    private JLabel label1;
    private boolean add;
    private String url;

    public AddURL() {
        setSize(255, 120);
        new Thread(new Runnable() {
            @Override
            public void run() {
                gui();

            }
        }).start();
        setCenter();
        display();
    }

    private void gui() {
        ok = new BasicButton();
        cancel = new BasicButton();
        textField = new MTextField(15, 25, 220, 37);
        label1 = new JLabel();
        Container dialog2ContentPane = getContentPane();
        dialog2ContentPane.setLayout(null);

        // ---- button1 ----
        ok.setText("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                url = textField.getText();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        checkURL();
                    }
                }).start();
            }
        });

        dialog2ContentPane.add(ok);
        ok.setBound(15, 70, 110, 35);

        // ---- button2 ----
        cancel.setText("HỦY");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        dialog2ContentPane.add(cancel);
        cancel.setBound(125, 70, 110, 35);

        textField.setText(getClipboard());
        textField.setFont(FontConstants.textNomal);
        dialog2ContentPane.add(textField);

        // ---- label1 ----
        label1.setText("Nhập URL");
        label1.setFont(FontConstants.textBold);
        dialog2ContentPane.add(label1);
        label1.setBounds(15, 0, 110, 25);

        dialog2ContentPane.setBackground(Color.WHITE);

    }

    public boolean isOk() {
        return add;
    }

    public String getUrl() {
        if (url.lastIndexOf("/") == url.length() - 1)
            url = url.substring(0, url.length() - 1);
        return url;
    }

    private void checkURL() {
        boolean err = true;
        if (url.toLowerCase().startsWith("http")) {

            String[] list = new String[]{"truyenyy.com", "truyenfull.vn", "isach.info", "banlong.us", "webtruyen.com",
                    "goctruyen.com", "hixx.info", "truyencv.com", "bachngocsach.com", "truyencuatui.net",
                    "www.5book.vn", "sstruyen.com", "santruyen.com", "tuchangioi.net", "tangthuvien.vn", "truyenvl.net", "wikidich.com"};
            for (int j = 0; j < list.length; j++)
                if (url.toLowerCase().indexOf(list[j]) != -1) {
                    err = false;
                    break;
                }
        }
        if (err)
            textField.addError("Liên kết này không được hỗ trợ!");

        else {
            add = true;
            close();
        }
    }

    public String getClipboard() {
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        try {
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String result = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                if (result.toLowerCase().startsWith("http"))
                    return result;
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }


}
