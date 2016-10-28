package dark.leech.text.gui;

import dark.leech.text.action.History;
import dark.leech.text.action.Log;
import dark.leech.text.constant.Constants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.constant.SettingConstants;
import dark.leech.text.getter.Informations;
import dark.leech.text.getter.List;
import dark.leech.text.gui.components.CircleWait;
import dark.leech.text.gui.components.MDialog;
import dark.leech.text.gui.components.MListDialog;
import dark.leech.text.gui.components.MTextField;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.item.*;
import dark.leech.text.listeners.AddListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddDialog extends MDialog {

    private JLabel labelName;
    private JLabel labelAuthor;
    private JLabel labelChap;
    private MTextField tfName;
    private MTextField tfAuthor;
    private MTextField tfChap;
    private BasicButton showChap;
    private BasicButton ok;
    private BasicButton cancel;
    private CircleWait Wait;
    private String url;
    private Informations informations;
    private Properties properties;
    private AddListener addListener;

    public AddDialog(String url) {
        this.url = url;
        setSize(300, 260);
        setCenter();
        display();
        new Thread(new Runnable() {
            @Override
            public void run() {
                gui();
                repaint();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                doLoad();
            }
        }).start();
    }

    public AddDialog(Properties properties) {
        this.properties = properties;
        setSize(300, 260);
        setCenter();
        display();
        new Thread(new Runnable() {
            @Override
            public void run() {
                gui();
                tfName.setTextFieldEnabled(true);
                tfName.setText(properties.getName());
                tfAuthor.setTextFieldEnabled(true);
                tfAuthor.setText(properties.getAuthor());
                showChap.setVisible(true);
                tfChap.setTextFieldEnabled(true);
                tfChap.setText("1-" + Integer.toString(properties.getSize()));
                showChap.setVisible(true);
                ok.setEnabled(true);
                Wait.stopWait();
            }
        }).start();
    }

    private void gui() {
        labelName = new JLabel();
        labelAuthor = new JLabel();
        labelChap = new JLabel();
        tfName = new MTextField(10, 35, 280, 37);
        tfAuthor = new MTextField(10, 100, 280, 37);
        tfChap = new MTextField(10, 165, 200, 37);
        Container ContentPane = getContentPane();
        ContentPane.setLayout(null);
        Wait = new CircleWait(getPreferredSize());
        JLayer<JPanel> layer = Wait.getJlayer();
        add(layer);
        layer.setBounds(0, 0, 300, 260);

        Wait.startWait();
        // ---- button1 ----
        ok = new BasicButton();
        ok.setText("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionAdd();
            }
        });
        ContentPane.add(ok);
        ok.setEnabled(false);
        ok.setBound(10, 210, 110, 35);

        // ---- button2 ----
        cancel = new BasicButton();
        cancel.setText("HỦY");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        ContentPane.add(cancel);
        cancel.setBound(180, 210, 110, 35);

        labelName.setText("Tên truyện");
        labelName.setFont(FontConstants.textNomal);
        ContentPane.add(labelName);
        labelName.setBounds(10, 10, 280, 25);
        tfName.setTextFieldEnabled(false);
        ContentPane.add(tfName);

        labelAuthor.setText("Tác giả");
        labelAuthor.setFont(FontConstants.textNomal);
        ContentPane.add(labelAuthor);
        labelAuthor.setBounds(10, 75, 280, 25);
        tfAuthor.setTextFieldEnabled(false);
        ContentPane.add(tfAuthor);

        labelChap.setText("Danh sách chương");
        labelChap.setFont(FontConstants.textNomal);
        ContentPane.add(labelChap);
        labelChap.setBounds(10, 140, 280, 25);
        tfChap.setTextFieldEnabled(false);
        ContentPane.add(tfChap);
        showChap = new BasicButton();
        showChap.setText("Xem DS");
        showChap.setBound(220, 165, 70, 37);
        showChap.setVisible(false);
        showChap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionShow();
            }
        });
        ContentPane.add(showChap);
        ContentPane.setBackground(Color.WHITE);
    }

    public Properties getProperties() {
        return properties;
    }

    private void doLoad() {
        informations = new Informations(url);
        properties = informations.getProperties();
        tfName.setTextFieldEnabled(true);
        tfName.setText(properties.getName());
        tfAuthor.setTextFieldEnabled(true);
        tfAuthor.setText(properties.getAuthor());
        List list = new List(url);
        properties.setPageList(list.getPageList());
        properties.setChapList(list.getChapList());
        if (properties.isForum())
            properties.setSize(list.getPageList().size());
        else
            properties.setSize(list.getChapList().size());

        tfChap.setTextFieldEnabled(true);
        tfChap.setText("1-" + Integer.toString(properties.getSize()));
        showChap.setVisible(true);
        ok.setEnabled(true);
        Wait.stopWait();
    }

    private void actionAdd() {
        Syntax syntax = new Syntax();
        properties.setName(syntax.covertString(tfName.getText().trim()));
        properties.setAuthor(syntax.covertString(tfAuthor.getText().trim()));
        if (properties.getName().length() == 0) {
            tfName.addError("Tên truyện không được để trống!");
            return;
        }
        if (properties.getAuthor().length() == 0) {
            tfAuthor.addError("Tên tác giả không được để trống");
            return;
        }
        String savePath = syntax.xoaDau(tfName.getText());
        savePath = savePath.replaceAll("[^a-zA-Z0-9_]", "_");
        savePath = SettingConstants.WORKPATH + Constants.l + savePath;
        properties.setSavePath(savePath);
        properties.setUrl(url);
        FileAction.mkdir(savePath);
        History history = new History();
        history.parse(properties);
        parseListChap();
        addListener.addDownload(properties);
        close();
    }

    private void actionShow() {
        MListDialog list = null;
        if (properties.isForum())
            list = new MListDialog(properties.getPageList(), tfChap.getText(), true);
        else
            list = new MListDialog(properties.getChapList(), tfChap.getText());
        list.addBlurListener(this);
        list.setVisible(true);
        tfChap.setText(list.getParseList());
    }

    private void parseListChap() {
        int size = 0;
        if (properties.isForum()) {
            ArrayList<Pager> pageList = properties.getPageList();
            try {
                String[] list = tfChap.getText().replace(" ", "").split(",");

                for (int i = 0; i < list.length; i++) {
                    String[] c = list[i].split("-");
                    int c0 = Integer.parseInt(c[0]);
                    if (c.length == 1) {
                        pageList.get(c0 - 1).setGet(true);
                        size++;
                        continue;
                    }
                    for (int j = c0; j <= Integer.parseInt(c[1]); j++) {
                        size++;
                        pageList.get(j - 1).setGet(true);
                    }
                }
                properties.setPageList(pageList);
            } catch (Exception e) {
                Log.add("Lỗi tạo danh sách chương");
            }
        } else {
            ArrayList<Chapter> chapList = properties.getChapList();
            try {
                String[] list = tfChap.getText().replace(" ", "").split(",");
                for (int i = 0; i < list.length; i++) {
                    String[] c = list[i].split("-");
                    int c0 = Integer.parseInt(c[0]);
                    if (c.length == 1) {
                        chapList.get(c0 - 1).setGet(true);
                        size++;
                        continue;
                    }
                    for (int j = c0; j <= Integer.parseInt(c[1]); j++) {
                        chapList.get(j - 1).setGet(true);
                        size++;
                    }
                }
                properties.setChapList(chapList);
            } catch (Exception e) {
                Log.add("Lỗi tạo danh sách chương");
            }
            properties.setSize(size);
        }
    }

    public void addAddListener(AddListener addListener) {
        this.addListener = addListener;
    }

}