package dark.leech.text.gui.components.setting;

import dark.leech.text.constant.ColorConstants;
import dark.leech.text.constant.FontConstants;
import dark.leech.text.constant.StringConstants;
import dark.leech.text.gui.components.Dialog;
import dark.leech.text.gui.components.Panel;
import dark.leech.text.gui.components.ScrollPane;
import dark.leech.text.gui.components.TextField;
import dark.leech.text.gui.components.button.BasicButton;
import dark.leech.text.gui.components.button.CircleButton;
import dark.leech.text.gui.components.button.SelectButton;
import dark.leech.text.listeners.BlurListener;
import dark.leech.text.listeners.RemoveListener;
import dark.leech.text.models.Trash;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TrashPane extends Panel {

    private static final long serialVersionUID = 1L;
    private ArrayList<Trash> trash;
    private CircleButton buttonEdit;

    public TrashPane() {
        this(null);
    }

    public TrashPane(ArrayList<Trash> trash) {
        this.trash = trash;
        JLabel labelName = new JLabel();
        JLabel labelTip = new JLabel();

        labelName.setText("Lọc rác");
        labelName.setFont(FontConstants.textBold);
        labelName.setBounds(25, 5, 290, 30);
        add(labelName);

        labelTip.setText("Tùy chỉnh lọc rác khi gettext");
        labelTip.setFont(FontConstants.textThin);
        labelTip.setForeground(Color.GRAY);
        labelTip.setBounds(25, 30, 290, 30);
        add(labelTip);

        buttonEdit = new CircleButton("\ue254");
        buttonEdit.setForeground(ColorConstants.THEME_COLOR);
        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionEdit();
            }
        });
        add(buttonEdit);
        buttonEdit.setBounds(335, 15, 30, 30);
        //  setBorder(new DropShadowBorder(new Color(63, 81, 181), 3, 0.4f, 15, true, true, true, true));
        setBackground(Color.white);
        setPreferredSize(new Dimension(370, 60));
        setLayout(null);
    }

    public ArrayList<Trash> getTrash() {
        return trash;
    }

    public void setTrash(ArrayList<Trash> trash) {
        this.trash = trash;
    }

    private void actionEdit() {
        TrashUI trashUI = new TrashUI(trash);
        if (trashUI.isDone())
            this.trash = trashUI.getTrash();
    }

}

class TrashUI extends Dialog implements RemoveListener, BlurListener {
    public int numTrash = 0;
    private ArrayList<Trash> trash;
    private BasicButton add;
    private BasicButton ok;
    private BasicButton cancel;
    private JPanel body;
    private GridBagConstraints gbc;
    private boolean done;

    public TrashUI(ArrayList<Trash> trash) {
        done = false;
        numTrash = 0;
        this.trash = trash;
        setSize(330, 370);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                load();
            }
        });
        onCreate();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Container co = getContentPane();
        co.setBackground(Color.WHITE);
        co.setLayout(null);
        add = new BasicButton();
        ok = new BasicButton();
        cancel = new BasicButton();
        body = new JPanel(new GridBagLayout());
        body.setForeground(Color.WHITE);
        body.setBackground(Color.white);
        setLayout(null);
        JLabel labelName = new JLabel("    Lọc rác");
        labelName.setFont(FontConstants.titleNomal);
        labelName.setForeground(Color.WHITE);
        labelName.setBackground(ColorConstants.THEME_COLOR);
        labelName.setOpaque(true);
        co.add(labelName);
        labelName.setBounds(0, 0, 330, 45);

        body.setBackground(Color.white);
        GridBagConstraints gi = new GridBagConstraints();
        gi.gridwidth = GridBagConstraints.REMAINDER;
        gi.weightx = 1;
        gi.weighty = 1;
        ScrollPane scrollPane = new ScrollPane(body);

        JPanel demo = new JPanel();
        demo.setBackground(Color.WHITE);
        body.add(demo, gi);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        co.add(scrollPane);
        scrollPane.setBounds(0, 45, 327, 270);
        //
        add.setText("THÊM");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });
        co.add(add);
        add.setBounds(10, 320, 100, 30);

        ok.setText("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                done = true;
                hide();
            }
        });
        co.add(ok);
        ok.setBounds(170, 320, 70, 30);

        cancel.setText("HỦY");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        co.add(cancel);
        cancel.setBounds(250, 320, 70, 30);
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
    }

    public void load() {
        for (int i = 0; i < trash.size(); i++)
            addItem(trash.get(i));
    }

    public ArrayList<Trash> getTrash() {
        trash = new ArrayList<Trash>();
        for (int i = 0; i < body.getComponentCount() - 1; i++)
            trash.add(((TrashItem) body.getComponent(i)).getTrash());
        return trash;
    }

    private void addItem() {
        TrashItemDialog trashItemDialog = new TrashItemDialog();
        trashItemDialog.setBlurListener(this);
        trashItemDialog.setVisible(true);
        if (trashItemDialog.isDone())
            addItem(trashItemDialog.getTrash());
    }

    private void addItem(Trash trash) {
        TrashItem tr = new TrashItem(trash);
        tr.addBlurListener(this);
        tr.addRemoveListener(this);
        body.add(tr, gbc, numTrash);
        validate();
        repaint();
        numTrash++;
    }

    public boolean isDone() {
        return done;
    }


    @Override
    public void removeComponent(Component comp) {
        body.remove(comp);
        body.updateUI();
        numTrash--;
    }

    @Override
    public void setBlur(boolean b) {
        getGlassPane().setVisible(b);
    }
}

class TrashItem extends Panel {
    private JLabel labelName;
    private CircleButton buttonEdit;
    private CircleButton buttonDelete;
    private SelectButton btSelect;
    private Trash trash;
    private RemoveListener removeListener;
    private BlurListener blurListener;

    public TrashItem(Trash trash) {
        this.trash = trash;
        gui();
    }

    private void gui() {
        setBackground(Color.white);
        setLayout(null);
        labelName = new JLabel();
        btSelect = new SelectButton();

        labelName.setText(trash.getTip());
        labelName.setFont(FontConstants.textNomal);
        add(labelName);
        labelName.setBounds(10, 5, 180, 30);

        buttonEdit = new CircleButton(StringConstants.EDIT);
        buttonEdit.setForeground(ColorConstants.THEME_COLOR);
        buttonEdit.setToolTipText("Sửa");

        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doEdit();
            }
        });
        add(buttonEdit);
        buttonEdit.setBounds(220, 5, 30, 30);

        btSelect.setSelected(trash.isReplace());
        add(btSelect);
        btSelect.setBounds(250, 5, 30, 30);
        btSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btSelect.setSelected(!btSelect.isSelected());
            }
        });

        buttonDelete = new CircleButton(StringConstants.DELETE);
        buttonDelete.setForeground(ColorConstants.THEME_COLOR);
        buttonDelete.setToolTipText("Xóa");
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionDelete();
            }
        });
        add(buttonDelete);
        buttonDelete.setBounds(280, 5, 30, 30);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                btSelect.setSelected(!btSelect.isSelected());
            }
        });
        //setBorder(new DropShadowBorder(new Color(63, 81, 181), 3, 0.4f, 15, true, true, true, true));
        setPreferredSize(new Dimension(300, 40));
    }

    public void addRemoveListener(RemoveListener removeListener) {
        this.removeListener = removeListener;
    }

    public void addBlurListener(BlurListener blurListener) {
        this.blurListener = blurListener;
    }

    public Trash getTrash() {
        trash.setReplace(btSelect.isSelected());
        return trash;
    }

    private void actionDelete() {
        removeListener.removeComponent(this);
    }

    private void doEdit() {
        TrashItemDialog trashItemDialog = new TrashItemDialog(trash);
        trashItemDialog.setBlurListener(blurListener);
        trashItemDialog.setVisible(true);
        if (trashItemDialog.isDone()) {
            this.trash = trashItemDialog.getTrash();
            labelName.setText(trash.getTip());
        }


    }

}

class TrashItemDialog extends Dialog {
    private TextField textSrc;
    private TextField textTo;
    private TextField textTip;
    private BasicButton ok;
    private BasicButton cancel;
    private Trash trash;
    private boolean done = false;

    public TrashItemDialog() {
        this(new Trash());
    }

    public TrashItemDialog(Trash trash) {
        setSize(new Dimension(300, 250));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui();
                show();
                setTrash(trash);
            }
        });
    }

    private void gui() {
        Container co = getContentPane();
        co.setBackground(Color.WHITE);
        co.setLayout(null);
        JLabel labelSrc = new JLabel();
        JLabel labelTo = new JLabel();
        JLabel labelTip = new JLabel();
        textSrc = new TextField(10, 35, 280, 37);
        textTo = new TextField(10, 100, 280, 37);
        textTip = new TextField(10, 160, 280, 37);
        ok = new BasicButton();
        cancel = new BasicButton();
        done = false;
        //
        labelSrc.setText("Tìm");
        labelSrc.setFont(FontConstants.textNomal);
        co.add(labelSrc);
        co.add(textSrc);
        labelSrc.setBounds(10, 10, 280, 25);
        labelTo.setText("Thay thế bởi");
        labelTo.setFont(FontConstants.textNomal);
        co.add(labelTo);
        co.add(textTo);
        labelTo.setBounds(10, 75, 280, 25);
        labelTip.setText("Mô tả");
        labelTip.setFont(FontConstants.textNomal);
        co.add(labelTip);
        co.add(textTip);
        labelTip.setBounds(10, 135, 280, 25);
        ok.setText("XONG");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                check();
            }
        });
        co.add(ok);
        ok.setBounds(105, 210, 90, 30);
        cancel.setText("HỦY");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        co.add(cancel);
        cancel.setBounds(200, 210, 90, 30);
    }

    public Trash getTrash() {
        trash.setSrc(textSrc.getText());
        trash.setTo((textTo.getText() == null ? "" : textTo.getText()));
        trash.setTip(textTip.getText());
        trash.setReplace(trash.isReplace());
        return trash;
    }

    public void setTrash(Trash trash) {
        this.trash = trash;
        textSrc.setText(trash.getSrc());
        textTo.setText(trash.getTo());
        textTip.setText(trash.getTip());
    }

    public boolean isDone() {
        return done;
    }


    private void check() {
        if (textSrc.getText().length() == 0) {
            textSrc.addError("Nội dung không được để trống!");
            return;
        }
        done = true;
        close();
    }
}

