package dark.leech.text.gui.components;

import dark.leech.text.constant.FontConstants;
import dark.leech.text.item.Chapter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Long on 9/10/2016.
 */
public class MTable extends JTable {

    public MTable(ArrayList<Chapter> chapter) {
        this();
        Object[][] data = new Object[chapter.size()][4];
        for (int i = 0; i < chapter.size(); i++) {
            data[i][0] = Integer.toString(chapter.get(i).getId());
            data[i][1] = chapter.get(i).getPartName();
            data[i][2] = chapter.get(i).getChapName();
            data[i][3] = chapter.get(i).getUrl();
        }
        Object[] head = {"", "", "", ""};
        setModel(new DefaultTableModel(data, head) {
            Class<?>[] columnTypes = new Class<?>[]{String.class, String.class, String.class, String.class};
            boolean[] columnEditable = new boolean[]{
                    false, true, true, true
            };

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnEditable[columnIndex];
            }
        });
        {
            TableColumnModel cm = getColumnModel();
            cm.getColumn(0).setMaxWidth(40);
            cm.getColumn(1).setMinWidth(100);
            cm.getColumn(1).setMaxWidth(80);
            cm.getColumn(3).setMinWidth(150);
            cm.getColumn(3).setMaxWidth(80);
        }
    }

    public MTable() {
        setTableHeader(null);
        setShowGrid(false);
        setRowHeight(25);
        setOpaque(false);
        setFont(FontConstants.textThin);
        // ((JComponent) getDefaultRenderer(Object.class)).setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        Graphics2D g2 = (Graphics2D) g;
        Paint oldPaint = g2.getPaint();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        g2.setPaint(oldPaint);
        for (int row : getSelectedRows()) {
            Rectangle start = getCellRect(row, 0, true);
            Rectangle end = getCellRect(row, getColumnCount() - 1, true);
            g2.setColor(Color.GRAY);
            // g2.setPaint(new GradientPaint(start.x, 0, controlColor, (int) ((end.x + end.width - start.x) * 1.25), 0, Color.LIGHT_GRAY));
            g2.drawRect(start.x, start.y, end.x + end.width - start.x, start.height);
        }
        super.paintComponent(g);
    }
}
