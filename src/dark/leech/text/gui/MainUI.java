package dark.leech.text.gui;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.Constants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.Animation;
import dark.leech.text.gui.components.DropShadowPopupMenu;
import dark.leech.text.gui.components.MenuItem;
import dark.leech.text.gui.components.button.CircleButton;
import dark.leech.text.gui.components.button.CloseButton;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainUI extends JFrame {

    // global panel
    private HomeUI home;
    private SettingUI setting;
    private JPanel mainHeader;
    private CircleButton addButton;
    private CircleButton menuButton;
    // header panel
    private JPanel headerBar;
    private CircleButton backButton;
    private CircleButton okButton;
    private JLabel labelLogo;
    private JPanel panelHeader;
    // Menu
    private JPopupMenu menu;
    private MenuItem panelSetting;
    private MenuItem panelHelp;
    private MenuItem panelUpdate;

    //
    private CloseButton exit;
    private JLabel status;

    private Container contentPane;
    private Point initialClick;

    private ActionListener mouse = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == menuButton) {
                menu.show(menuButton, menuButton.getWidth() / 2 - 70, menuButton.getHeight() / 2 - 20);
            }
            if (e.getSource() == exit)
                actionExit();

            if (e.getSource() == addButton) {
                actionAdd();
            }

            if (e.getSource() == backButton) {
                Animation.go(setting, home);
            }
            if (e.getSource() == okButton) {
                setting.doSave();
                Animation.go(setting, home);
            }
        }

    };

    public MainUI() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        setLocation(width - 420, height - 650);
        setSize(390, 555);
        getRootPane().setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        setUndecorated(true);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/dark/leech/res/icon.png")));
        new Thread(new Runnable() {
            @Override
            public void run() {
                gui();
            }
        }).start();
    }

    private void gui() {
        setGlassPane(new JComponent() {
            public void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 30));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        });
        // JPanel mainPanel = new JPanel();
        contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.setBackground(Color.WHITE);
        createHeaderBar();
        createMainHeaderUI();
        createPanelHeaderUI();
        createPopupMenu();
        home = new HomeUI();
        home.add(mainHeader);
        mainHeader.setBounds(0, 0, 390, 55);
        contentPane.add(home);
        home.setBounds(0, 25, 390, 530);
        setting = new SettingUI();
        setting.add(panelHeader);
        setting.setBounds(390, 25, 390, 530);
        panelHeader.setBounds(0, 0, 390, 55);
        contentPane.add(setting);
        new UpdateUI().isHaveUpdate();
    }

    private void actionExit() {
        flyOut();
    }

    private void actionAdd() {
        AddURL addURL = new AddURL();
        addURL.setVisible(true);
        if (addURL.isOk()) {
            AddDialog addDialog = new AddDialog(addURL.getUrl());
            addDialog.addAddListener(home);
            addDialog.setVisible(true);
        }
    }

    //Thanh tiêu đề
    private void createHeaderBar() {
        headerBar = new JPanel();
        headerBar.setBackground(ColorConstants.BAR);
        headerBar.setLayout(null);
        // ---- exit ----

        exit = new CloseButton();
        exit.addActionListener(mouse);
        headerBar.add(exit);
        exit.setBound(360, 0, 25, 25);
        // ---- status ----
        status = new JLabel();
        status.setForeground(Color.white);
        status.setFont(FontConstants.textNomal);
        headerBar.add(status);
        status.setBounds(10, 0, 315, 25);
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                status.setText(dateFormat.format(date));
            }
        });
        timer.start();
        contentPane.add(headerBar);
        headerBar.setBounds(0, 0, 390, 25);
        headerBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });
        headerBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                movieWindows(e);
            }
        });
    }

    //Thanh Title
    private void createMainHeaderUI() {

        mainHeader = new JPanel();
        mainHeader.setBackground(ColorConstants.THEME_COLOR);
        mainHeader.setLayout(null);

        addButton = new CircleButton("", 25f);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(mouse);
        mainHeader.add(addButton);
        addButton.setBound(305, 5, 45, 45);
        // ---- logo ----
        JLabel logo;
        logo = new JLabel();
        logo.setText("Leech Text");
        logo.setFont(FontConstants.titleBig);
        logo.setForeground(Color.white);
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        mainHeader.add(logo);
        logo.setBounds(20, 0, logo.getPreferredSize().width, 55);

        menuButton = new CircleButton("", 25f);
        menuButton.setForeground(Color.WHITE);
        menuButton.addActionListener(mouse);
        mainHeader.add(menuButton);
        menuButton.setBound(355, 5, 30, 45);

    }

    private void createPanelHeaderUI() {
        panelHeader = new JPanel();
        panelHeader.setBackground(ColorConstants.THEME_COLOR);
        panelHeader.setLayout(null);

        backButton = new CircleButton("", 25f);
        backButton.addActionListener(mouse);
        backButton.setForeground(Color.WHITE);
        panelHeader.add(backButton);
        backButton.setBound(5, 5, 45, 45);
        // ---- logo ----
        labelLogo = new JLabel();
        labelLogo.setText("Cài đặt");
        labelLogo.setFont(FontConstants.titleBig);
        labelLogo.setForeground(Color.white);
        labelLogo.setHorizontalAlignment(SwingConstants.CENTER);
        panelHeader.add(labelLogo);
        labelLogo.setBounds(55, 0, 100, 55);

        okButton = new CircleButton("", 25f);
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(mouse);
        panelHeader.add(okButton);
        okButton.setBound(335, 5, 45, 45);
    }

    private void createPopupMenu() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == panelUpdate) {
                    UpdateUI updateUI = new UpdateUI();
                    updateUI.checkUpdteUi();
                    updateUI.setVisible(true);
                }
                if (e.getSource() == panelSetting) {
                    Animation.go(home, setting);
                    setting.load();
                }
                if (e.getSource() == panelHelp) {
                    try {
                        Desktop.getDesktop().browse(new URL("http://banlong.us/threads/tool-get-text-cua-mot-so-web-truyen.12471").toURI());
                    } catch (Exception xe) {
                    }
                }
            }
        };
        menu = new DropShadowPopupMenu();
        //  menu.setBorderPainted(true);
        menu.setOpaque(true);
        panelSetting = new MenuItem("Cài đặt");
        panelSetting.addActionListener(actionListener);
        menu.add(panelSetting);
        panelHelp = new MenuItem("Trợ giúp");
        panelHelp.addActionListener(actionListener);
        menu.add(panelHelp);
        panelUpdate = new MenuItem("Update");
        panelUpdate.addActionListener(actionListener);
        menu.add(panelUpdate);
        contentPane.add(menu);
        menu.setBorder(new LineBorder(ColorConstants.THEME_COLOR.brighter()));
      //  menu.setSize(new Dimension(80, 90));

    }

    private void movieWindows(MouseEvent e) {
        int thisX = getLocation().x;
        int thisY = getLocation().y;
        int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
        int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);
        int X = thisX + xMoved;
        int Y = thisY + yMoved;
        setLocation(X, Y);
        Constants.LOCATION = getLocation();
    }

    public void flyIn() {
        final int x = getLocation().x;
        final int y = getLocation().y - 20;
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 1; i <= 20; i++) {
                    setLocation(x, y + i);
                    try {
                        setOpacity((float) 0.05 * i);
                        Thread.sleep(i * 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();

    }

    public void flyOut() {
        final int x = getLocation().x;
        final int y = getLocation().y;
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 1; i <= 10; i++) {
                    setLocation(x, y + i);
                    try {
                        setOpacity((float) 0.05 * (10 - i));
                        Thread.sleep(i * 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.exit(0);
            }
        }).start();
    }


}
