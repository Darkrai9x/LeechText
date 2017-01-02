package dark.leech.text.gui;

import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.Dialog;
import dark.leech.text.gui.components.TextField;
import dark.leech.text.gui.components.button.BasicButton;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddURL extends Dialog {

    private BasicButton ok;
    private BasicButton cancel;
    private TextField textField;
    private JLabel label1;
    private boolean add;
    private String url;

    public AddURL() {
       onCreate();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        ok = new BasicButton();
        cancel = new BasicButton();
        textField = new TextField(15, 25, 220, 37);
        label1 = new JLabel();

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

        container.add(ok);
        ok.setBounds(15, 70, 110, 35);

        // ---- button2 ----
        cancel.setText("HỦY");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        container.add(cancel);
        cancel.setBounds(125, 70, 110, 35);

        textField.setText(getClipboard());
        textField.setFont(FontConstants.textNomal);
        container.add(textField);

        // ---- label1 ----
        label1.setText("Nhập URL");
        label1.setFont(FontConstants.textBold);
        container.add(label1);
        label1.setBounds(15, 0, 110, 25);
        container.setBackground(Color.WHITE);
        setSize(255, 120);
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
