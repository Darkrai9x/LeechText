package dark.leech.text.gui.export;

import dark.leech.text.action.Config;
import dark.leech.text.action.History;
import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.Constants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.*;
import dark.leech.text.gui.components.MenuItem;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.gui.components.button.CloseButton;
import dark.leech.text.item.Chapter;
import dark.leech.text.item.FileAction;
import dark.leech.text.item.Properties;
import dark.leech.text.listeners.BlurListener;
import dark.leech.text.listeners.ChangeListener;
import dark.leech.text.listeners.TableListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by Long on 9/10/2016.
 */
public class ConfigUI extends MDialog implements BlurListener, ChangeListener {
    private JPanel title;
    private JLabel labelTitle;
    private CloseButton buttonClose;
    private BasicButton buttonOk;
    private JLabel labelName;
    private JLabel labelImg;
    private JLabel labelError;
    private BasicButton buttonName;
    private BasicButton buttonError;
    private BasicButton buttonImg;
    private BasicButton buttonList;
    protected Properties properties;
    private ArrayList<Chapter> chapList;
    private Config config;
    private ArrayList<Chapter> nameList;
    private ArrayList<Chapter> imgList;
    private ArrayList<Chapter> errorList;

    public ConfigUI(Properties properties) {
        this.properties = properties;
        this.chapList = properties.getChapList();
        setSize(295, 260);
        new Thread(new Runnable() {
            @Override
            public void run() {
                gui();
                setCenter();
                display();
                loadErr();
            }
        }).start();
    }

    private void gui() {
        title = new JPanel();
        labelTitle = new JLabel();
        buttonClose = new CloseButton();
        buttonOk = new BasicButton();
        labelName = new JLabel();
        labelImg = new JLabel();
        labelError = new JLabel();
        buttonName = new BasicButton();
        buttonError = new BasicButton();
        buttonImg = new BasicButton();
        buttonList = new BasicButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== title ========

        title.setBackground(ColorConstants.THEME_COLOR);
        title.setLayout(null);
        //---- labelTitle ----
        labelTitle.setText("Hiệu Chỉnh");
        labelTitle.setFont(FontConstants.titleNomal);
        labelTitle.setForeground(Color.WHITE);
        title.add(labelTitle);
        labelTitle.setBounds(15, 0, 95, 45);

        //---- buttonClose ----

        buttonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        title.add(buttonClose);
        buttonClose.setBound(265, 10, 25, 25);

        contentPane.add(title);
        title.setBounds(0, 0, 295, 45);

        //---- buttonOk ----
        buttonOk.setText("Ho\u00e0n T\u1ea5t");
        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProperties();
                close();
            }
        });
        contentPane.add(buttonOk);
        buttonOk.setBound(170, 210, 100, 35);

        //
        buttonList.setText("Xem DS");
        buttonList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Optimize();
            }
        });
        contentPane.add(buttonList);
        buttonList.setBound(10, 210, 100, 35);
        //---- labelName ----
        // labelName.setText("T\u00ean ch\u01b0\u01a1ng: 300 ch\u01b0\u01a1ng l\u1ed7i");
        labelName.setFont(FontConstants.textNomal);
        contentPane.add(labelName);
        labelName.setBounds(10, 60, 175, 35);

        //---- labelImg ----
        // labelImg.setText("Ch\u01b0\u01a1ng \u1ea3nh: 300 ch\u01b0\u01a1ng");
        labelImg.setFont(FontConstants.textNomal);
        contentPane.add(labelImg);
        labelImg.setBounds(10, 105, 175, 35);

        //---- labelError ----
        //  labelError.setText("Ch\u01b0\u01a1ng tr\u1ed1ng: 3 ch\u01b0\u01a1ng");
        labelError.setFont(FontConstants.textNomal);
        contentPane.add(labelError);
        labelError.setBounds(10, 150, 175, 35);

        //---- buttonName ----
        buttonName.setText("H.Ch\u1ec9nh");
        buttonName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editName();
            }
        });
        contentPane.add(buttonName);
        buttonName.setBound(185, 60, 95, 35);

        //---- buttonError ----
        buttonError.setText("H.Ch\u1ec9nh");
        buttonError.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editError();
            }
        });
        contentPane.add(buttonError);
        buttonError.setBound(185, 150, 95, 35);

        //---- buttonImg ----
        buttonImg.setText("H.Ch\u1ec9nh");
        buttonImg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editImg();
            }
        });
        contentPane.add(buttonImg);
        buttonImg.setBound(185, 105, 95, 35);
    }

    private void loadErr() {
        config = new Config(chapList);
        nameList = config.checkName();
        labelName.setText("Tên chương: " + Integer.toString(nameList.size()) + " không hợp lệ");
        if (nameList.size() == 0) buttonName.setVisible(false);
        imgList = config.checkImg();
        labelImg.setText("Chương ảnh: " + Integer.toString(imgList.size()) + " chương");
        if (imgList.size() == 0) buttonImg.setVisible(false);
        errorList = config.checkError();
        labelError.setText("Chương lỗi: " + Integer.toString(errorList.size()) + " chương");
        if (errorList.size() == 0) buttonError.setVisible(false);
    }

    private void editName() {
        ListUI listName = new ListUI(nameList, "Tên chương không hợp lệ", properties.getSavePath());
        listName.addBlurListener(this);
        listName.setAction(0);
        listName.setVisible(true);
    }

    private void editImg() {
        ListUI listImg = new ListUI(imgList, "Chương ảnh", properties.getSavePath());
        listImg.addBlurListener(this);
        listImg.setAction(1);
        listImg.setVisible(true);
    }

    private void editError() {
        ListUI listError = new ListUI(errorList, "Chương lỗi", properties.getSavePath());
        listError.addBlurListener(this);
        listError.setAction(2);
        listError.setVisible(true);
    }

    private void Optimize() {
        ListUI list = new ListUI(chapList, "Danh sách chương", properties.getSavePath());
        list.addBlurListener(this);
        list.setAction(3);
        list.setVisible(true);
    }

    private void saveProperties() {
        new History().save(properties);
    }


    @Override
    public void setBlur(boolean b) {
        getGlassPane().setVisible(b);
    }

    @Override
    public void doChanger() {
        repaint();
    }
}

class ListUI extends MDialog implements TableListener {
    private ArrayList<Chapter> chapList;
    private MTable tableList;
    private DefaultTableModel tableModel;
    private DropShadowPopupMenu popup;
    private String name;
    private String[] nameButton = new String[]{"Auto Fix", "Tải ảnh", "", "Optimize"};
    private BasicButton button3;
    private MProgressBar progressBar;
    private int action;
    private String path;

    public ListUI(ArrayList<Chapter> chapList, String name) {
        this.chapList = chapList;
        this.name = name;
        setSize(500, 430);
        setLocation(Constants.LOCATION.x - 150 > 0 ? Constants.LOCATION.x - 150 : 0, Constants.LOCATION.y - 20 > 0 ? Constants.LOCATION.y - 20 : 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                gui();
            }
        }).start();
        display();

    }

    public ListUI(ArrayList<Chapter> chapList, String name, String path) {
        this.chapList = chapList;
        this.name = name;
        this.path = path;
        setSize(500, 430);
        setLocation(Constants.LOCATION.x - 150 > 0 ? Constants.LOCATION.x - 150 : 0, Constants.LOCATION.y - 20 > 0 ? Constants.LOCATION.y - 20 : 0);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui();
            }
        });
        display();
    }

    private void gui() {
        tableList = new MTable(chapList);
        JPanel title = new JPanel();
        JLabel labelTitle = new JLabel();
        CloseButton buttonClose = new CloseButton();
        JScrollPane scrollPane1 = new JScrollPane();
        tableList = new MTable(chapList);
        tableModel = (DefaultTableModel) tableList.getModel();
        BasicButton buttonCancel = new BasicButton();
        BasicButton buttonOk = new BasicButton();
        button3 = new BasicButton();
        progressBar = new MProgressBar();
        popup = new DropShadowPopupMenu();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        title.setBackground(ColorConstants.THEME_COLOR);
        title.setLayout(null);
        //---- labelTitle ----
        labelTitle.setText(name);
        labelTitle.setFont(FontConstants.titleNomal);
        labelTitle.setForeground(Color.WHITE);
        title.add(labelTitle);
        labelTitle.setBounds(15, 0, 430, 45);

        //---- buttonClose ----
        buttonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        title.add(buttonClose);
        buttonClose.setBound(470, 10, 25, 25);

        contentPane.add(title);
        title.setBounds(0, 0, 500, 45);


        scrollPane1.setViewportView(tableList);
        JScrollBar sc = scrollPane1.getVerticalScrollBar();
        sc.setUI(new MScrollBar());
        sc.setPreferredSize(new Dimension(10, 0));
        sc.setBackground(Color.WHITE);
        scrollPane1.setBorder(new EmptyBorder(1, 1, 1, 1));
        scrollPane1.getVerticalScrollBar().setUnitIncrement(20);
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(0, 45, 500, 335);

        //---- buttonCancel ----
        buttonCancel.setText("H\u1ee6Y");
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        contentPane.add(buttonCancel);
        buttonCancel.setBound(380, 385, 100, 35);

        //---- buttonOk ----
        buttonOk.setText("OK");
        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doClick();
            }
        });
        contentPane.add(buttonOk);
        buttonOk.setBound(270, 385, 90, 35);

        //---- button3 ----
        // button3.setText("Fix t\u00ean t\u1ef1 d\u1ed9ng");
        if (action == 2)
            button3.setVisible(false);
        button3.setText(nameButton[action]);
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doAction();
            }
        });
        contentPane.add(button3);
        button3.setBound(10, 385, 160, 35);
        progressBar.setPercent(0);
        contentPane.add(progressBar);
        progressBar.setBounds(10, 385, 160, 35);
        progressBar.setVisible(false);
        MenuItem menuItemEdit = new MenuItem("Sửa");
        menuItemEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doEdit();
            }
        });
        popup.add(menuItemEdit);
        popup.setBorder(new LineBorder(ColorConstants.THEME_COLOR.brighter()));
        tableList.addMouseListener(new MouseAdapter() {
            private void showIfPopupTrigger(MouseEvent mouseEvent) {
                if (mouseEvent.isPopupTrigger())
                    popup.show(mouseEvent.getComponent(),
                            mouseEvent.getX(), mouseEvent.getY());
            }

            public void mousePressed(MouseEvent mouseEvent) {
                showIfPopupTrigger(mouseEvent);
            }

            public void mouseReleased(MouseEvent mouseEvent) {
                showIfPopupTrigger(mouseEvent);
            }
        });
    }

    private void doAction() {
        button3.setVisible(false);
        progressBar.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (action == 0) fixName();
                if (action == 1) downImg();
                if (action == 3) Optimize();
            }
        }).start();

    }

    private void doEdit() {
        int row = tableList.getSelectedRow();
        Chapter chapter = chapList.get(row);
        Edit edit = new Edit(chapter);
        edit.addBlurListener(this);
        edit.showGui();
        edit.setVisible(true);
        tableList.setValueAt(chapter.getPartName(), row, 1);
        tableList.setValueAt(chapter.getChapName(), row, 2);
    }

    public void setAction(int action) {
        this.action = action;
    }

    private void doClick() {
        for (int i = 0; i < tableList.getRowCount(); i++) {
            if (tableList.getCellEditor() != null) tableList.getCellEditor().stopCellEditing();
            chapList.get(i).setPartName((String) tableList.getValueAt(i, 1));
            chapList.get(i).setChapName((String) tableList.getValueAt(i, 2));
        }
        close();
    }

    private void fixName() {
        Config config = new Config(chapList);
        config.addTableListener(this);
        config.autoFixName();
    }

    private void downImg() {
        Config config = new Config(chapList);
        config.addTableListener(this);
        config.setPath(path);
        config.downloadImg();
    }

    private void Optimize() {
        Config config = new Config(chapList);
        config.addTableListener(this);
        config.Optimize();
    }

    private class Edit extends MDialog {
        private JPanel title;
        private MTextField tfPart;
        private MTextField tfChap;
        private JScrollPane scrollPane1;
        private JTextArea taText;
        private JLabel lbPart;
        private JLabel lbChap;
        private BasicButton buttonOk;
        private BasicButton buttonCancel;
        private Chapter chapter;

        public Edit(Chapter chapter) {
            this.chapter = chapter;
        }

        public void showGui() {
            setSize(310, 400);
            title = new JPanel();
            tfPart = new MTextField(80, 55, 220, 30);
            tfChap = new MTextField(80, 95, 220, 30);
            scrollPane1 = new JScrollPane();
            taText = new JTextArea();
            lbPart = new JLabel();
            lbChap = new JLabel();
            buttonOk = new BasicButton();
            buttonCancel = new BasicButton();

            //======== this ========
            Container contentPane = getContentPane();
            contentPane.setLayout(null);

            {
                title.setBackground(ColorConstants.THEME_COLOR);
                title.setLayout(null);
                //---- labelTitle ----
                JLabel labelTitle = new JLabel();
                labelTitle.setText(chapter.getChapName());
                labelTitle.setFont(FontConstants.titleNomal);
                labelTitle.setForeground(Color.WHITE);
                title.add(labelTitle);
                labelTitle.setBounds(15, 0, 250, 45);

                //---- buttonClose ----
                CloseButton buttonClose = new CloseButton();
                buttonClose.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        close();
                    }
                });
                title.add(buttonClose);
                buttonClose.setBound(280, 10, 25, 25);

            }
            contentPane.add(title);
            title.setBounds(0, 0, 310, 45);
            contentPane.add(tfPart);
            tfPart.setText(chapter.getPartName());
            contentPane.add(tfChap);
            tfChap.setText(chapter.getChapName());

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(taText);
                JScrollBar sb = scrollPane1.getVerticalScrollBar();
                sb.setUI(new MScrollBar());
                sb.setBackground(Color.WHITE);
                sb.setPreferredSize(new Dimension(10, 0));
            }
            taText.setLineWrap(true);
            taText.setWrapStyleWord(true);
            taText.setFont(FontConstants.codeFont(12f));
            taText.setForeground(ColorConstants.THEME_COLOR);
            taText.setText(new FileAction().file2string(path + Constants.l + "raw" + Constants.l + Integer.toString(chapter.getId()) + ".txt"));
            contentPane.add(scrollPane1);
            scrollPane1.setBounds(10, 135, 290, 220);

            //---- lbPart ----
            lbPart.setText("Quy\u1ec3n");
            contentPane.add(lbPart);
            lbPart.setBounds(5, 55, 70, 30);

            //---- lbChap ----
            lbChap.setText("Ch\u01b0\u01a1ng");
            contentPane.add(lbChap);
            lbChap.setBounds(5, 95, 70, 30);
            buttonOk.setText("OK");
            contentPane.add(buttonOk);
            buttonOk.setBound(125, 360, 75, 30);
            buttonOk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    doClick();
                }
            });
            buttonCancel.setText("HỦY");
            contentPane.add(buttonCancel);
            buttonCancel.setBound(225, 360, 75, 30);
            buttonCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    close();
                }
            });
            setCenter();
            display();
        }

        private void doClick() {
            chapter.setPartName(tfPart.getText());
            chapter.setChapName(tfChap.getText());
            new FileAction().string2file(taText.getText(), path + Constants.l + "raw" + Constants.l + Integer.toString(chapter.getId()) + ".txt");
            close();
        }

    }

    @Override
    public void updateData(int row, Chapter chapter) {
        if (tableList.getCellEditor() != null) tableList.getCellEditor().stopCellEditing();
        tableModel.setValueAt(Integer.toString(chapter.getId()) + "√", row, 0);
        tableModel.setValueAt(chapter.getPartName(), row, 1);
        tableModel.setValueAt(chapter.getChapName(), row, 2);
        tableModel.fireTableCellUpdated(row, 0);
        tableModel.fireTableCellUpdated(row, 1);
        tableModel.fireTableCellUpdated(row, 2);
        progressBar.setPercent((row + 1) * 100 / chapList.size());
        if (row + 1 == chapList.size()) progressBar.setVisible(false);
    }
}

