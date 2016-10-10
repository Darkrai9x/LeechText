package dark.leech.text.gui.components.setting;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.Constants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.gui.App;
import dark.leech.text.gui.components.MPanel;
import dark.leech.text.gui.components.button.CircleButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Code by Darkrai on 8/23/2016.
 */
public class ToolPane extends MPanel {
    private JLabel labelPath;
    private boolean selectDirectory = false;


    public ToolPane(String name, String path, int type) {
        JLabel labelName = new JLabel();
        labelPath = new JLabel();

        labelName.setText(name);
        labelName.setFont(FontConstants.textBold);
        labelName.setBounds(25, 5, 290, 30);
        add(labelName);

        labelPath.setText(path);
        labelPath.setFont(FontConstants.textThin);
        labelPath.setForeground(Color.GRAY);
        labelPath.setBounds(25, 30, 290, 30);
        add(labelPath);

        CircleButton buttonEdit = new CircleButton("\ue254", 23f);
        buttonEdit.setForeground(ColorConstants.THEME_COLOR);
        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog f = new FileDialog(App.M, "Chọn đường dẫn", type);
                if (type == FileDialog.SAVE)
                    f.setFile("LeechText");
                f.setModal(true);
                if (getPath().equals("Chưa đặt")) f.setDirectory(System.getProperty("user.dir"));
                else f.setDirectory(getPath());
                f.setVisible(true);
                if (f.getDirectory() != null)
                    setPath(f.getDirectory() + ((selectDirectory) ? "" : f.getFile()));
            }
        });
        add(buttonEdit);
        buttonEdit.setBound(335, 15, 30, 30);
        setBackground(Color.white);
        setPreferredSize(new Dimension(370, 60));
        setLayout(null);
    }

    public void setSelectDirectory(boolean selectDirectory) {
        this.selectDirectory = selectDirectory;
    }

    public String getPath() {
        return labelPath.getText();
    }

    public void setPath(String path) {
        if (path.length() != 0)
            if (path.lastIndexOf(Constants.l) == path.length() - 1)
                path = path.substring(0, path.length() - 1);
        labelPath.setText(path);
    }

}

