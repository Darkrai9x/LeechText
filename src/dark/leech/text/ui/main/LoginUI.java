package dark.leech.text.ui.main;


import dark.leech.text.util.ColorUtils;
import dark.leech.text.util.FontUtils;
import dark.leech.text.ui.material.JMCheckBox;
import dark.leech.text.ui.material.JMDialog;
import dark.leech.text.ui.material.JMTextField;
import dark.leech.text.ui.button.BasicButton;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Long on 11/2/2016.
 */
public class LoginUI extends JMDialog {
    private JLabel lbTitle;
    private JMTextField tfUser;
    private JMTextField tfPass;
    private JMCheckBox cbAuto;
    private JLabel lbUser;
    private JLabel lbPass;
    private BasicButton btCancel;
    private BasicButton btLogin;

    public LoginUI() {
        setUndecorated(true);
        setModal(true);
        onCreate();
    }

    protected void onCreate() {
        super.onCreate();
        lbTitle = new JLabel();
        tfUser = new JMTextField();
        tfPass = new JMTextField();
        cbAuto = new JMCheckBox("Ghi nhớ");
        cbAuto.setVisible(false);
        lbUser = new JLabel();
        lbPass = new JLabel();
        btCancel = new BasicButton();
        btLogin = new BasicButton();

        //======== this ========


        //---- lbTitle ----
        lbTitle.setText("Đăng nhập");
        lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lbTitle.setBackground(ColorUtils.THEME_COLOR);
        lbTitle.setOpaque(true);
        lbTitle.setFont(FontUtils.TITLE_NORMAL);
        lbTitle.setForeground(Color.white);
        container.add(lbTitle);
        lbTitle.setBounds(0, 0, 235, 60);
        container.add(tfUser);
        container.add(tfPass);

        //---- cbAuto ----
        container.add(cbAuto);
        cbAuto.setBounds(30, 205, 100, 25);

        //---- lbUser ----
        lbUser.setText("Tài khoản");
        tfUser.setBounds(30, 100, 175, 30);
        container.add(lbUser);
        lbUser.setBounds(30, 70, 175, 25);

        //---- lbPass ----
        lbPass.setText("Mật khẩu");
        tfPass.setBounds(30, 165, 175, 30);
        container.add(lbPass);
        lbPass.setBounds(30, 135, 175, 25);
        //---- btCancel ----
        btCancel.setText("Hủy");
        btCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        container.add(btCancel);
        btCancel.setBounds(35, 240, 75, 30);

        //---- btLogin ----
        btLogin.setText("Login");
        btLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();

            }
        });
        container.add(btLogin);
        btLogin.setBounds(125, 240, 80, 30);
        setSize(235, 290);
    }

    private void doLogin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = Jsoup.connect("http://goctruyen.com/login/")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                        .data("txtuser", tfUser.getText())
                        .data("txtpass", tfPass.getText())
                        .data("remember", "on")
                        .data("login", "Login")
                        .followRedirects(true)
                        .ignoreContentType(true);
                try {
                    Map<String, String> cookies = connection.method(Connection.Method.POST).execute().cookies();
                   // Constants.Connection.cookies = cookies;
                    close();
                } catch (IOException e) {
                }
            }
        }).start();

    }

    public void setStatus(String err) {
        if (err.length() == 0) close();
        else if (err.indexOf("không tồn tại") > -1)
            tfUser.addError("Tài khoản không tồn tại");
        else tfPass.addError("Mật khẩu sai");
    }
}

