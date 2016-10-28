package dark.leech.text.gui.components;

/**
 * Created by Long on 9/3/2016.
 */

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.models.Chapter;
import dark.leech.text.models.Pager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MListDialog extends MDialog {

    private JScrollPane scrollPane1;
    private MTable MTable;
    private BasicButton buttonCancel;
    private BasicButton buttonOk;
    private BasicButton buttonSelect;
    private JLabel labelTitle;
    private ArrayList<Chapter> chapList;
    private ArrayList<Pager> pageList;
    private String parseList;
    private boolean selected = true;
    private boolean forum;


    public MListDialog(ArrayList<Pager> pageList, String parseList, boolean forum) {
        this.pageList = pageList;
        this.parseList = parseList;
        this.forum = forum;
        setSize(350, 520);
        setCenter();
        display();
        setModal(true);
        Thread t = new Thread() {
            public void run() {
                ui();
            }
        };
        t.start();
    }

    public MListDialog(ArrayList<Chapter> chapList, String parseList) {
        this.parseList = parseList;
        this.chapList = chapList;
        forum = false;
        setSize(350, 520);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ui();
                setCenter();
                display();
            }
        });
    }

    private void ui() {
        scrollPane1 = new JScrollPane();
        buttonCancel = new BasicButton();
        buttonOk = new BasicButton();
        buttonSelect = new BasicButton();
        labelTitle = new JLabel();

        //======== this ========
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== scrollPane1 ========
        Object[] columnNames = {"", "Tên chương"};
        Object[][] data = null;
        if (forum) {
            data = new Object[pageList.size()][2];
            for (int i = 0; i < pageList.size(); i++) {
                data[i][0] = false;
                data[i][1] = "Trang " + Integer.toString(i + 1);
            }
        } else {
            data = new Object[chapList.size()][2];
            for (int i = 0; i < chapList.size(); i++) {
                data[i][0] = false;
                data[i][1] = chapList.get(i).getChapName();
            }
        }
        MTable = new MTable();
        doUpdatedata(parseList, data);
        MTable.setModel(new DefaultTableModel(data, columnNames) {
            Class<?>[] columnTypes = new Class<?>[]{Boolean.class, String.class};

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        doUpdatedata(parseList, data);
        TableColumnModel cm = MTable.getColumnModel();
        cm.getColumn(0).setMaxWidth(30);
        MTable.setFont(FontConstants.textThin);
        MTable.setTableHeader(null);
        scrollPane1.setViewportView(MTable);
        JScrollBar sc = scrollPane1.getVerticalScrollBar();
        sc.setUI(new MScrollBar());
        sc.setPreferredSize(new Dimension(10, 0));
        sc.setBackground(Color.WHITE);
        scrollPane1.setBorder(new EmptyBorder(1, 1, 1, 1));
        scrollPane1.getVerticalScrollBar().setUnitIncrement(20);
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(0, 50, 348, 420);

        //---- buttonCancel ----
        buttonCancel.setText("HỦY");
        buttonCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                close();
            }
        });
        contentPane.add(buttonCancel);
        buttonCancel.setBound(275, 475, 65, 30);

        //---- buttonOk ----
        buttonOk.setText("OK");
        contentPane.add(buttonOk);
        buttonOk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                okClick();
            }
        });
        buttonOk.setBound(210, 475, 60, 30);

        //---- buttonSelect ----
        buttonSelect.setText("BỎ CHỌN");
        contentPane.add(buttonSelect);
        buttonSelect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doSelect();
            }
        });
        buttonSelect.setBound(5, 475, 100, 30);

        //---- labelTitle ----
        labelTitle.setText("   Danh sách chương");
        labelTitle.setFont(FontConstants.titleNomal);
        labelTitle.setForeground(Color.WHITE);
        labelTitle.setBackground(ColorConstants.THEME_COLOR);
        labelTitle.setOpaque(true);
        contentPane.add(labelTitle);
        labelTitle.setBounds(0, 0, 350, 45);

        contentPane.setPreferredSize(new Dimension(350, 525));

    }


    private void okClick() {
        ArrayList<Integer> c = new ArrayList<Integer>();
        DefaultTableModel tb = (DefaultTableModel) MTable.getModel();
        for (int i = 0; i < MTable.getRowCount(); i++) {
            if (MTable.getCellEditor() != null) MTable.getCellEditor().stopCellEditing();
            if (!forum) chapList.get(i).setChapName((String) MTable.getValueAt(i, 1));
            if ((boolean) MTable.getValueAt(i, 0)) c.add(i + 1);
        }
        if (c.size() == 0) {

            return;
        }
        doParseList(c);
        close();
    }

    private void doSelect() {
        selected = !selected;
        buttonSelect.setText(selected ? "BỎ CHỌN" : "CHỌN");
        if (MTable.getSelectedRowCount() != 0) {
            int[] sl = MTable.getSelectedRows();

            for (int i = 0; i < sl.length; i++)
                MTable.setValueAt(selected, sl[i], 0);
        } else {
            for (int i = 0; i < MTable.getRowCount(); i++)
                MTable.setValueAt(selected, i, 0);
        }
    }


    public String getParseList() {
        return parseList;
    }

    public void doUpdatedata(String parseList, Object[][] data) {
        parseList = parseList.replaceAll("\\s+", "");
        String[] c = parseList.split(",");
        for (int i = 0; i < c.length; i++) {
            String[] cc = c[i].split("-");
            for (int j = Integer.parseInt(cc[0]); j <= Integer
                    .parseInt(cc[cc.length - 1]); j++)
                data[j - 1][0] = true;
        }
//        if (parseList.equals("1-" + i2s(chapList.size())))
//            return;

    }

    // Loc chuong rut gon
    public void doParseList(ArrayList<Integer> intMang) {
        parseList = "";
        for (int i = 0; i < intMang.size() - 1; i++)
            if (intMang.get(i) + 1 == intMang.get(i + 1))
                parseList += i2s(intMang.get(i)) + "-";
            else
                parseList += i2s(intMang.get(i)) + ",";
        parseList += i2s(intMang.get(intMang.size() - 1));
        parseList = parseList.replaceAll("(\\d+-).*?(\\d+(,|$))", "$1$2");

    }

    private String i2s(int i) {
        return Integer.toString(i);
    }

}


