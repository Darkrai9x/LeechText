package dark.leech.text.gui;

import dark.leech.text.action.Settings;
import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.constant.SettingConstants;
import dark.leech.text.gui.components.MScrollBar;
import dark.leech.text.gui.components.Notification;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.gui.components.setting.*;
import dark.leech.text.models.Base64;
import dark.leech.text.models.FileAction;
import dark.leech.text.models.Trash;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SettingUI extends JPanel {
    private JPanel body;
    private JScrollPane scrollPane;
    // connection
    private ItemConn maxConn;
    private ItemConn reConn;
    private ItemConn timeConn;
    private ItemConn delayConn;
    // style
    private ItemStyle htmlStyle;
    private ItemStyle txtStyle;
    private ItemStyle cssStyle;
    private ItemStyle dropStyle;
    // other
    private TrashPane trash;
    private ToolPane kindlegen;
    private ToolPane calibre;
    private ToolPane workPath;
    private Theme theme;

    private BasicButton defaultButton;

    public SettingUI() {
        setLayout(null);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui();
            }
        });
    }

    public void load() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateStatus();
            }
        }).start();
    }

    private void gui() {
        setBackground(Color.WHITE);
        body = new JPanel(new GridBagLayout());
        body.setBackground(Color.white);
        GridBagConstraints gi = new GridBagConstraints();
        gi.gridwidth = GridBagConstraints.REMAINDER;
        gi.weightx = 1;
        gi.weighty = 1;
        scrollPane = new JScrollPane(body);
        JScrollBar sb = scrollPane.getVerticalScrollBar();
        sb.setUI(new MScrollBar());
        sb.setBackground(Color.WHITE);
        sb.setPreferredSize(new Dimension(10, 0));

        JPanel demo = new JPanel();
        demo.setBackground(Color.WHITE);
        body.add(demo, gi);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(scrollPane);
        scrollPane.setBounds(0, 55, 390, 440);
        //
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // body.
        maxConn = new ItemConn("Số kết nối tối đa", "5");
        reConn = new ItemConn("Số kết nối lại khi bị lỗi", "3");
        timeConn = new ItemConn("Thời gian kết nối tối đa", "10000");
        delayConn = new ItemConn("Thời gian chờ giữa các kết nối", "100");

        htmlStyle = new ItemStyle("Tùy chỉnh HTML", "Tùy chỉnh cấu trúc HTML khi lưu", false);
        htmlStyle.setStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        txtStyle = new ItemStyle("Tùy chỉnh TXT", "Tùy chỉnh cấu trúc TXT khi lưu", false);
        cssStyle = new ItemStyle("Tùy chỉnh CSS", "Tùy chỉnh cấu trúc CSS khi lưu", false);
        cssStyle.setStyle(SyntaxConstants.SYNTAX_STYLE_CSS);
        dropStyle = new ItemStyle("Tùy chỉnh DropCaps", "Tùy chỉnh cấu trúc DropCaps khi lưu", false);
        dropStyle.setStyle(SyntaxConstants.SYNTAX_STYLE_HTML);

        trash = new TrashPane(SettingConstants.TRASH);
        kindlegen = new ToolPane("Đường dẫn Kindlegen", "", FileDialog.LOAD);
        calibre = new ToolPane("Đường dẫn Calibre", "", FileDialog.LOAD);
        workPath = new ToolPane("Thư mục lưu trữ", "", FileDialog.SAVE);
        workPath.setSelectDirectory(true);
        theme = new Theme(SettingConstants.THEME_COLOR);

        body.add(theme, gbc, 0);
        body.add(calibre, gbc, 0);
        body.add(kindlegen, gbc, 0);
        body.add(workPath, gbc, 0);
        body.add(trash, gbc, 0);
        body.add(Label("Khác"), gbc, 0);
        body.add(cssStyle, gbc, 0);
        body.add(txtStyle, gbc, 0);
        body.add(htmlStyle, gbc, 0);
        body.add(dropStyle, gbc, 0);
        body.add(Label("Cấu trúc lưu"), gbc, 0);
        body.add(delayConn, gbc, 0);
        body.add(reConn, gbc, 0);
        body.add(timeConn, gbc, 0);
        body.add(maxConn, gbc, 0);
        body.add(Label("Kết nối"), gbc, 0);

        //
        defaultButton = new BasicButton();
        defaultButton.setText("Khôi phục mặc định");
        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Settings().doDefault();
                updateStatus();
                new Notification("Đã khôi phục mặc định!").setVisible(true);
            }
        });
        add(defaultButton);
        defaultButton.setBound(0, 495, 390, 35);
        // repaint();
    }

    private JPanel Label(String name) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(name);
        label.setForeground(ColorConstants.THEME_COLOR);
        label.setFont(FontConstants.titleNomal);
        panel.add(label);
        label.setBounds(10, 0, 200, 30);
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(370, 30));
        return panel;
    }

    private void updateStatus() {
        maxConn.setText(toString(SettingConstants.MAX_CONN));
        reConn.setText(toString(SettingConstants.RECONN));
        timeConn.setText(toString(SettingConstants.TIMEOUT));
        delayConn.setText(toString(SettingConstants.DELAY));
        //
        htmlStyle.setSelected(SettingConstants.isHTML);
        htmlStyle.setText(SettingConstants.HTML);
        txtStyle.setSelected(SettingConstants.isTXT);
        txtStyle.setText(SettingConstants.TXT);
        cssStyle.setSelected(SettingConstants.isCSS);
        cssStyle.setText(SettingConstants.CSS);
        dropStyle.setSelected(SettingConstants.isDROP);
        dropStyle.setText(SettingConstants.DROP);
        //
        trash.setTrash(SettingConstants.TRASH);
        workPath.setPath(SettingConstants.WORKPATH);
        calibre.setPath(SettingConstants.CALIBRE);
        kindlegen.setPath(SettingConstants.KINDLEGEN);
        theme.setThemeColor(SettingConstants.THEME_COLOR);
    }


    public void doSave() {
        String o = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n";
        o += getItem("integer", "numConn", maxConn.getText());
        o += getItem("integer", "reConn", reConn.getText());
        o += getItem("integer", "timeout", timeConn.getText());
        o += getItem("integer", "delay", delayConn.getText());
        o += getItem("string", "drop", dropStyle.isSelected(), Base64.encode(dropStyle.getText()));
        o += getItem("string", "html", htmlStyle.isSelected(), Base64.encode(htmlStyle.getText()));
        o += getItem("string", "txt", txtStyle.isSelected(), Base64.encode(txtStyle.getText()));
        o += getItem("string", "css", cssStyle.isSelected(), Base64.encode(cssStyle.getText()));
        o += getItem("string", "workPath", Base64.encode(workPath.getPath()));
        o += getItem("string", "calibrePath", Base64.encode(calibre.getPath()));
        o += getItem("string", "kindlegenPath", Base64.encode(kindlegen.getPath()));
        o += getItem("color", "theme", theme.getHexColor());
        o += "	<string id=\"trash\">\n";
        ArrayList<Trash> tr = trash.getTrash();
        for (Trash t : tr)
            o += "		<models src=\"" + Base64.encode(t.getSrc()) + "\" to=\""
                    + Base64.encode(t.getTo()) + "\" tip=\"" + Base64.encode(t.getTip())
                    + "\" replace=\"" + (t.isReplace() ? "1" : "0") + "\" />\n";
        o += "	</string>\n";
        o += "</resources>";
        FileAction io = new FileAction();
        io.string2file(o, "setting.xml");
        new Settings().doLoad();
        new Notification("Đã lưu cài đặt!").setVisible(true);
    }

    private void validateTF() {

    }

    private String toString(int i) {
        return Integer.toString(i);
    }

    private int toInt(String s) {

        return Integer.parseInt(s);
    }

    private String getItem(String type, String id, String text) {
        return "	<" + type + " id=\"" + id + "\">" + text + "</" + type + ">\n";
    }

    private String getItem(String type, String id, boolean value, String text) {
        return "	<" + type + " id=\"" + id + "\" value=\"" + (value ? "1" : "0") + "\">" + text + "</" + type
                + ">\n";
    }

    private String getItem(String type, String id, boolean value) {
        return "	<" + type + " id=\"" + id + "\" value=\"" + (value ? "1" : "0") + "\" />\n";
    }

}
