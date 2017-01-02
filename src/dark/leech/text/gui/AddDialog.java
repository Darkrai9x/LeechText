package dark.leech.text.gui;

import dark.leech.text.action.History;
import dark.leech.text.action.Log;
import dark.leech.text.constant.Constants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.constant.SettingConstants;
import dark.leech.text.getter.Informations;
import dark.leech.text.getter.List;
import dark.leech.text.gui.components.CircleWait;
import dark.leech.text.gui.components.Dialog;
import dark.leech.text.gui.components.MListDialog;
import dark.leech.text.gui.components.TextField;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.listeners.AddListener;
import dark.leech.text.models.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddDialog extends Dialog {

    private JLabel labelName;
    private JLabel labelAuthor;
    private JLabel labelChap;
    private TextField tfName;
    private TextField tfAuthor;
    private TextField tfChap;
    private BasicButton showChap;
    private BasicButton ok;
    private BasicButton cancel;
    private CircleWait circleWait;
    private String url;
    private Informations informations;
    private Properties properties;
    private AddListener addListener;

    public AddDialog(String url) {
        this.url = url;
        onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                doLoad();
            }
        }).start();
    }


    public AddDialog(Properties properties) {
        this.properties = properties;
        onCreate();

        tfName.setTextFieldEnabled(true);
        tfName.setText(properties.getName());
        tfAuthor.setTextFieldEnabled(true);
        tfAuthor.setText(properties.getAuthor());
        showChap.setVisible(true);
        tfChap.setTextFieldEnabled(true);
        tfChap.setText("1-" + Integer.toString(properties.getSize()));
        showChap.setVisible(true);
        ok.setEnabled(true);
        circleWait.stopWait();

    }

    @Override
    protected void onCreate() {
        super.onCreate();
        labelName = new JLabel();
        labelAuthor = new JLabel();
        labelChap = new JLabel();
        tfName = new TextField(10, 35, 280, 37);
        tfAuthor = new TextField(10, 100, 280, 37);
        tfChap = new TextField(10, 165, 200, 37);
        circleWait = new CircleWait(getPreferredSize());
        JLayer<JPanel> layer = circleWait.getJlayer();
        add(layer);
        layer.setBounds(0, 0, 300, 260);

        circleWait.startWait();
        // ---- button1 ----
        ok = new BasicButton();
        ok.setText("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionAdd();
            }
        });
        container.add(ok);
        ok.setEnabled(false);
        ok.setBounds(10, 210, 110, 35);

        // ---- button2 ----
        cancel = new BasicButton();
        cancel.setText("HỦY");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        container.add(cancel);
        cancel.setBounds(180, 210, 110, 35);

        labelName.setText("Tên truyện");
        labelName.setFont(FontConstants.textNomal);
        container.add(labelName);
        labelName.setBounds(10, 10, 280, 25);
        tfName.setTextFieldEnabled(false);
        container.add(tfName);

        labelAuthor.setText("Tác giả");
        labelAuthor.setFont(FontConstants.textNomal);
        container.add(labelAuthor);
        labelAuthor.setBounds(10, 75, 280, 25);
        tfAuthor.setTextFieldEnabled(false);
        container.add(tfAuthor);

        labelChap.setText("Danh sách chương");
        labelChap.setFont(FontConstants.textNomal);
        container.add(labelChap);
        labelChap.setBounds(10, 140, 280, 25);
        tfChap.setTextFieldEnabled(false);
        container.add(tfChap);
        showChap = new BasicButton();
        showChap.setText("Xem DS");
        showChap.setBounds(220, 165, 70, 37);
        showChap.setVisible(false);
        showChap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionShow();
            }
        });
        container.add(showChap);
        this.setSize(300, 260);
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
        circleWait.stopWait();
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
        list.setBlurListener(this);
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