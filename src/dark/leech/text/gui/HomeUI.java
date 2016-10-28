package dark.leech.text.gui;

import dark.leech.text.action.History;
import dark.leech.text.gui.components.MScrollBar;
import dark.leech.text.listeners.AddListener;
import dark.leech.text.listeners.RemoveListener;
import dark.leech.text.models.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

public class HomeUI extends JPanel implements AddListener, RemoveListener, DropTargetListener{
    private JPanel download;
    private JScrollPane ScrollPane;

    public HomeUI() {
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        download = new JPanel(new GridBagLayout());
        download.setBackground(Color.white);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        ScrollPane = new JScrollPane(download);
        ScrollPane.setBackground(Color.WHITE);
        JScrollBar sb = ScrollPane.getVerticalScrollBar();
        sb.setUI(new MScrollBar());
        sb.setPreferredSize(new Dimension(10, 0));

        JPanel demo = new JPanel();
        demo.setBackground(Color.WHITE);
        download.add(demo, gbc);

        ScrollPane.setBorder(null);
        add(ScrollPane);
        ScrollPane.setBounds(0, 55, 390, 475);
        new DropTarget(this, this);
    }

    public void actionAdd(Properties properties) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        DownloadUI dl = new DownloadUI();
        dl.addDownload(properties);
        dl.addRemoveListener(this);
        download.add(dl, gbc, 0);
        repaint();
    }

    @Override
    public void removeComponent(Component comp) {
        download.remove(comp);
        download.updateUI();
    }

    @Override
    public void drop(DropTargetDropEvent event) {
        event.acceptDrop(DnDConstants.ACTION_COPY);
        Transferable transferable = event.getTransferable();
        DataFlavor[] flavors = transferable.getTransferDataFlavors();
        for (DataFlavor flavor : flavors) {
            try {
                if (flavor.isFlavorJavaFileListType()) {
                    List<File> files = (List) transferable.getTransferData(flavor);
                    for (File file : files) {
                        String path = file.getPath();
                        new AddDialog(new History().load(path)).setVisible(true);
                    }
                }
            } catch (Exception e) {
            }
        }
        event.dropComplete(true);
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {

    }

    @Override
    public void addDownload(Properties properties) {
        actionAdd(properties);
    }
}


