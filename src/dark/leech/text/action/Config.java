package dark.leech.text.action;

import dark.leech.text.get.Chap;
import dark.leech.text.listeners.BlurListener;
import dark.leech.text.listeners.ChangeListener;
import dark.leech.text.listeners.TableListener;
import dark.leech.text.models.Chapter;
import dark.leech.text.models.Properties;
import dark.leech.text.plugin.PluginGetter;
import dark.leech.text.plugin.PluginManager;
import dark.leech.text.ui.button.BasicButton;
import dark.leech.text.ui.material.JMDialog;
import dark.leech.text.util.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    private ArrayList<Chapter> chapList;
    private String path;
    private TableListener tableListener;
    private int index;
    private PluginGetter pluginGetter;
    private BlurListener blurListener;

    public Config(ArrayList<Chapter> chapList) {
        this.chapList = chapList;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<Chapter> checkError() {
        ArrayList<Chapter> imgErr = new ArrayList<Chapter>();
        for (int i = 0; i < chapList.size(); i++)
            if (chapList.get(i).isError())
                imgErr.add(chapList.get(i));
        return imgErr;
    }

    public ArrayList<Chapter> checkImg() {
        ArrayList<Chapter> imgList = new ArrayList<Chapter>();
        for (int i = 0; i < chapList.size(); i++) {
            if (chapList.get(i).isImageChapter())
                imgList.add(chapList.get(i));
        }
        return imgList;
    }

    public ArrayList<Chapter> checkName() {
        ArrayList<Chapter> nameList = new ArrayList<Chapter>();
        for (Chapter c : chapList)
            if (findMatchs(c.getChapName(), "^(Ch..ng|H.i) \\d+([\\+\\.-]\\d+|)") != 1)
                nameList.add(c);
            else if (findMatchs(c.getChapName(), "Ch..ng \\d+") > 1)
                nameList.add(c);
        return nameList;
    }

    private void splitPartName(Chapter chapter) throws Exception {
        if (chapter.getPartName().length() > 1) return;
        String regex = "((Quy.n |Q.|Q)\\d+\\s*[:-](.*?)*)\\s*(([Cc]h..ng|Hồi)\\s+\\d+)";
        if (findMatchs(chapter.getChapName(), "(Quy.n |Q\\.|Q)\\d+([\\+\\.-]\\d+|)") == 1) {
            String partName = splitMatchs(chapter.getChapName(), regex, 1).replaceAll("\\s*[:-]\\s*$", "");
            String chapName = chapter.getChapName();
            chapName = chapName.replace(partName, "").replaceAll("^\\s*[:-]\\s*", "");
            partName = partName.replaceAll("Q\\.|Q(\\d+)", "Quyển $1");
            chapter.setChapName(chapName);
            chapter.setPartName(partName);
        }
    }

    public void autoFixName() {
        for (int i = 0; i < chapList.size(); i++) {
            try {
                Chapter chapter = new Chapter(chapList.get(i).getUrl(), chapList.get(i).getId(), chapList.get(i).getPartName(), chapList.get(i).getChapName());
                splitPartName(chapter);
                chapter.setChapName(fixName(chapter.getChapName()));
                chapter.setPartName(fixName(chapter.getPartName()));
                tableListener.updateData(i, chapter);
            } catch (Exception e) {
            }
        }
    }

    private String fixName(String name) {
        if (name == null) return "";
        if (name.length() == 0) return name;
        name = name.replaceAll("Chương \\d+\\s*[:-]\\s*(Chương \\d+.*?$)", "$1")
                .replaceAll("^([hH]ồi|[đĐ]ệ) (\\d+)", "Chương $1")
                .replaceAll("(\\d+) [Cc]h..ng", "Chương $1")
                .replaceAll("\\s+", " ")
                .replaceAll("Chương (\\d+)\\s*[-\\+:]\\s*(\\d+)", "Chương $1+$2")
                .replaceAll("(Chương \\d+)\\s*[;:-]+\\s*", "$1: ")
                .replaceAll("(Chương \\d+\\+\\d+)\\s*[;:-]+\\s*", "$1: ");
        return name;
    }

    private String upperFirst(String name) {
        name = name.toLowerCase();
        char[] ch = name.toCharArray();
        ch[0] = Character.toUpperCase(ch[0]);
        for (int i = 1; i < ch.length; i++) {
            if (Character.toLowerCase(ch[i - 1]) == Character.toUpperCase(ch[i - 1]))
                ch[i] = Character.toUpperCase(ch[i]);
        }
        return new String(ch);
    }

    public void Optimize() {
        for (int i = 0; i < chapList.size(); i++) {
            try {
                Chapter chapter = new Chapter(chapList.get(i).getUrl(), chapList.get(i).getId(), chapList.get(i).getPartName(), chapList.get(i).getChapName());
                chapter.setChapName(Optimize(chapter.getChapName()));
                chapter.setPartName(Optimize(chapter.getPartName()));
                tableListener.updateData(i, chapter);
            } catch (Exception e) {
            }
        }
    }

    private String Optimize(String name) {
        if (name.length() == 0) return name;
        name = upperFirst(name);
        name = fixName(name);
        return name;
    }

    public void downloadChap(Properties properties) {
        pluginGetter = PluginManager.getManager().get(properties.getUrl());
        download(properties);

    }

    private synchronized void download(Properties properties) {
        new Chap()
                .clazz(pluginGetter.ChapGetter())
                .listener(new ChangeListener() {
                    @Override
                    public void doChanger() {
                        Chapter chapter = chapList.get(index);
                        if (chapter.isError()) {
                            ConfirmDialog confirmDialog = new ConfirmDialog(chapter);
                            confirmDialog.setBlurListener(blurListener);
                            confirmDialog.setConfirmListener(new ConfirmListener() {
                                @Override
                                public void confirm() {
                                    download(properties);
                                }

                                @Override
                                public void cancel() {
                                    tableListener.updateData(index, chapter);
                                    index++;
                                    if (index >= chapList.size()) {
                                        History.getHistory().save(properties);
                                        return;
                                    } else
                                        download(properties);
                                }
                            });
                            confirmDialog.open();
                        } else {
                            tableListener.updateData(index, chapter);
                            index++;
                            if (index >= chapList.size()) {
                                History.getHistory().save(properties);
                                return;
                            } else
                                download(properties);
                        }
                    }
                })
                .charset(properties.getCharset())
                .path(properties.getSavePath())
                .applyTo(chapList.get(index))
                .execute();
    }

    public void downloadImg() {
        for (int i = 0; i < chapList.size(); i++) {
            try {
                downloadImg(chapList.get(i));
                tableListener.updateData(i, chapList.get(i));
            } catch (Exception e) {
            }
        }
    }

    private void downloadImg(Chapter chapter) {
        String text = FileUtils.file2string(path + "/raw/" + chapter.getId() + ".txt");
        ArrayList<String> imgList = new ArrayList<String>();
        Pattern r = Pattern.compile("<img.*?src=\"(.*?)\"", Pattern.MULTILINE);
        Matcher m = r.matcher(text);
        while (m.find())
            imgList.add(m.group(1));
        for (int i = 0; i < imgList.size(); i++) {
            String imgPath = imgList.get(i);
            if (!imgPath.startsWith("http")) continue;
            String img = imgPath.substring(imgPath.lastIndexOf("."), imgPath.length()).toLowerCase(); //TÁch đuôi
            text = text.replace(imgPath, "../Images/" + chapter.getId() + "_" + Integer.toString(i) + img).replace("\">", "\"/>");
            img = path + "/data/Images/" + chapter.getId() + "_" + Integer.toString(i) + img;
            FileUtils.url2file(imgList.get(i), img);
        }
        FileUtils.string2file(text, path + "/raw/" + chapter.getId() + ".txt");
    }

    private int findMatchs(String src, String regex) {
        if (src == null) return 0;
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(src);
        int match = 0;
        while (m.find()) {
            match++;
        }
        return match;
    }

    private String splitMatchs(String src, String regex, int group) {
        if (src == null) return "";
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(src);
        String math = "";
        if (m.find())
            math = m.group(group);
        return math;
    }

    public void setBlurListener(BlurListener blurListener) {
        this.blurListener = blurListener;
    }

    public void addTableListener(TableListener tableListener) {
        this.tableListener = tableListener;
    }

    public interface ConfirmListener {
        void confirm();

        void cancel();
    }

    class ConfirmDialog extends JMDialog {


        private BasicButton btOk;
        private BasicButton btConfirm;
        private BasicButton btCancel;
        private ConfirmListener confirmListener;
        private Chapter chapter;

        public ConfirmDialog(Chapter chapter) {
            this.chapter = chapter;
            setSize(300, 150);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onCreate();
                }
            });
        }

        @Override
        protected void onCreate() {
            super.onCreate();
            btOk = new BasicButton();
            btConfirm = new BasicButton();
            btCancel = new BasicButton();
            JLabel label = new JLabel("Lỗi: " + chapter.getChapName());
            JLabel label1 = new JLabel("Mở Browser???");

            btOk.setText("MỞ");
            btConfirm.setText("XÁC NHẬN");
            btCancel.setText("HỦY");

            container.add(label);
            label.setBounds(30, 10, 250, 30);
            container.add(label1);
            label1.setBounds(110, 50, 100, 30);
            container.add(btOk);
            btOk.setBounds(10, 100, 100, 35);
            container.add(btConfirm);
            btConfirm.setBounds(10, 100, 100, 35);
            container.add(btCancel);
            btCancel.setBounds(190, 100, 100, 35);

            btConfirm.setVisible(false);
            btOk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URL(chapter.getUrl()).toURI());
                    } catch (Exception xe) {
                    }
                    btConfirm.setVisible(true);
                    btOk.setVisible(false);
                }
            });
            btConfirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (confirmListener != null)
                        confirmListener.confirm();
                    close();
                }
            });
            btCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (confirmListener != null)
                        confirmListener.cancel();
                    close();
                }
            });


        }

        public void setConfirmListener(ConfirmListener confirmListener) {
            this.confirmListener = confirmListener;
        }
    }
}
