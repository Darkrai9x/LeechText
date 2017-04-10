package dark.leech.text.ui.download;

import dark.leech.text.listeners.AddListener;
import dark.leech.text.plugin.PluginGetter;
import dark.leech.text.plugin.PluginManager;
import dark.leech.text.ui.button.BasicButton;
import dark.leech.text.ui.material.JMDialog;
import dark.leech.text.ui.material.JMTextField;
import dark.leech.text.util.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddURL extends JMDialog {

    private BasicButton btOk;
    private BasicButton btCancel;
    private JMTextField tfUrl;
    private JLabel lbUrl;
    private String url;
    private AddListener addListener;

    public AddURL() {
        onCreate();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        btOk = new BasicButton();
        btCancel = new BasicButton();
        tfUrl = new JMTextField();
        lbUrl = new JLabel();
        // ---- button1 ----
        btOk.setText("OK");
        btOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                url = tfUrl.getText();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkURL();
                    }
                });
            }
        });

        container.add(btOk);
        btOk.setBounds(15, 70, 110, 35);

        // ---- button2 ----
        btCancel.setText("HỦY");
        btCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        container.add(btCancel);
        btCancel.setBounds(125, 70, 110, 35);

        tfUrl.setText(getClipboard());
        tfUrl.setFont(FontUtils.TEXT_NORMAL);
        tfUrl.setBounds(15, 25, 220, 37);
        container.add(tfUrl);

        // ---- lbUrl ----
        lbUrl.setText("Nhập URL");
        lbUrl.setFont(FontUtils.TEXT_BOLD);
        container.add(lbUrl);
        lbUrl.setBounds(15, 0, 110, 25);
        container.setBackground(Color.WHITE);
        setSize(255, 120);
    }

    public String getUrl() {
        if (url.lastIndexOf("/") == url.length() - 1)
            url = url.substring(0, url.length() - 1);
        return url;
    }

    private void checkURL() {
        PluginGetter pluginGetter = PluginManager.getManager().get(url);
        if (pluginGetter == null)
            tfUrl.addError("Liên kết này không được hỗ trợ!");
        else if (pluginGetter.isChecked()) {
            AddDialog add = new AddDialog(getUrl());
            add.setAddListener(addListener);
            close();
            add.open();
        } else {
            tfUrl.addError("Plugin cho trang này đã bị tắt!");
        }
    }

    private String getClipboard() {
        try {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String result = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                if (result.toLowerCase().startsWith("http"))
                    return result;
            }
        } catch (Exception e) {
        }
        return "";
    }

    public void setAddListener(AddListener addListener) {
        this.addListener = addListener;
    }
}
