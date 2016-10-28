package dark.leech.text.action.export;

import dark.leech.text.action.Log;
import dark.leech.text.constant.Constants;
import dark.leech.text.constant.SettingConstants;
import dark.leech.text.constant.TypeConstants;
import dark.leech.text.gui.components.notification.AlertNotification;
import dark.leech.text.listeners.ProgressListener;
import dark.leech.text.models.FileAction;
import dark.leech.text.models.Properties;
import dark.leech.text.models.Syntax;
import dark.leech.text.models.ZipEngine;
import org.zeroturnaround.zip.ZipUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Ebook {
    private FileAction fileAction;
    private Properties properties;
    private boolean autoSplit;
    private String compressLevel;
    private String tool;
    private boolean includeImg;
    private int type;
    private ProgressListener progressListener;


    public Ebook(Properties properties) {
        this.properties = properties;
        fileAction = new FileAction();
    }

    public void setData(int type, String tool, String compressLevel, boolean autoSplit, boolean includeImg) {
        this.type = type;
        this.tool = tool;
        this.compressLevel = compressLevel;
        this.autoSplit = autoSplit;
        this.includeImg = includeImg;
    }

    public void export() {
        fileAction.string2file(SettingConstants.CSS, properties.getSavePath() + Constants.l + "data" + Constants.l + "stylesheet.css");
        new Text(properties, TypeConstants.HTML, false, 0).export();
        createToc();
        switch (type) {
            case TypeConstants.EPUB:
                exportEpub(tool);
                break;
            case TypeConstants.MOBI:
                exportMobi(tool);
                break;
            case TypeConstants.AZW3:
                exportAzw3();
                break;
            case TypeConstants.PDF:
                exportPdf();
                break;
            default:
                break;
        }
    }

    private void exportEpub(String tool) {
        if (tool.equals("Mặc định")) try {
            exportEpub();
        } catch (Exception e) {
        }
        else {
            tool = SettingConstants.CALIBRE;
            if (!checkTool(tool)) {
                new AlertNotification("Đường dẫn Calibre (ebook-convert.exe) không hợp lệ!\nXem lại thiết lập trong cài đặt!").setVisible(true);
                return;
            }
            String fileName = properties.getName() + " - " + properties.getAuthor() + ".epub";
            fileName = fileName.replaceAll("[:/\\?\\*]", "");
            String cmd = tinyCmd(tool)
                    + " "
                    + tinyCmd(properties.getSavePath() + Constants.l + "data" + Constants.l + "content.opf")
                    + " "
                    + tinyCmd(properties.getSavePath() + Constants.l + "out" + Constants.l + fileName);
            runCmd(cmd);
        }
    }

    private void exportMobi(String tool) {
        String cmd = "";
        if (tool.equals("Calibre")) {
            tool = SettingConstants.CALIBRE;
            if (!checkTool(tool)) {
                new AlertNotification("Đường dẫn Calibre (ebook-convert.exe) không hợp lệ!\nXem lại thiết lập trong cài đặt!").setVisible(true);
                return;
            }
            String fileName = properties.getName() + " - " + properties.getAuthor() + ".mobi";
            fileName = fileName.replaceAll("[:/\\?\\*]", "");
            cmd = tinyCmd(tool)
                    + " "
                    + tinyCmd(properties.getSavePath() + Constants.l + "data" + Constants.l + "content.opf")
                    + " "
                    + tinyCmd(properties.getSavePath() + Constants.l + "out" + Constants.l + fileName)
                    + " --mobi-file-type=" + compressLevel
                    + " --no-inline-toc --share-not-sync";
            runCmd(cmd);
        } else {
            tool = SettingConstants.KINDLEGEN;
            if (!checkTool(tool)) {
                new AlertNotification("Đường dẫn Kindlegen (Kindlegen.exe) không hợp lệ!\nXem lại thiết lập trong cài đặt!").setVisible(true);
                return;
            }
            String fileName = properties.getName() + " - " + properties.getAuthor() + ".mobi";
            fileName = fileName.replaceAll("[:/\\?\\*]", "");
            cmd = tinyCmd(tool)
                    + " "
                    + tinyCmd(properties.getSavePath() + Constants.l + "data" + Constants.l + "content.opf")
                    + " "
                    + compressLevel;
            runCmd(cmd);
            fileAction.cutFile(properties.getSavePath() + Constants.l + "data" + Constants.l + "content.mobi", properties.getSavePath() + Constants.l + "out" + Constants.l + fileName);
        }
    }

    private void exportEpub() throws Exception {
        InputStream in = getClass().getResourceAsStream("/dark/leech/res/untitled.epub");
        String fileName = properties.getName() + " - " + properties.getAuthor() + ".epub";
        fileName = fileName.replaceAll("[:/\\?\\*]", "");
        fileName = properties.getSavePath() + Constants.l + "out" + Constants.l + fileName;
        fileAction.byte2file(fileAction.stream2byte(in), fileName);
        ZipUtil.setDefaultCompressionLevel(Integer.parseInt(compressLevel));
        ZipEngine.addFolders(fileName, properties.getSavePath() + Constants.l + "data" + Constants.l + "Text", "Text");
        progressListener.setProgress(90, "Sắp xong");
        ZipEngine.addFile(fileName, properties.getSavePath() + Constants.l + "data" + Constants.l + "content.opf");
        ZipEngine.addFile(fileName, properties.getSavePath() + Constants.l + "data" + Constants.l + "toc.ncx");
        ZipEngine.addFile(fileName, properties.getSavePath() + Constants.l + "data" + Constants.l + "stylesheet.css");
        ZipEngine.addFile(fileName, properties.getSavePath() + Constants.l + "data" + Constants.l + "cover.jpg");
        if (includeImg)
            ZipEngine.addFolders(fileName, properties.getSavePath() + Constants.l + "data" + Constants.l + "Images");
        progressListener.setProgress(100, "Hoàn tất!");

    }

    private void exportAzw3() {
        tool = SettingConstants.CALIBRE;
        if (!checkTool(tool)) {
            new AlertNotification("Đường dẫn Calibre (ebook-convert.exe) không hợp lệ!\nXem lại thiết lập trong cài đặt!").setVisible(true);
            return;
        }
        String fileName = properties.getName() + " - " + properties.getAuthor() + ".azw3";
        fileName = fileName.replaceAll("[:/\\?\\*]", "");
        String cmd = tinyCmd(tool)
                + " "
                + tinyCmd(properties.getSavePath() + Constants.l + "data" + Constants.l + "content.opf")
                + " "
                + tinyCmd(properties.getSavePath() + Constants.l + "out" + Constants.l + fileName)
                + " --share-not-sync --no-inline-toc";
        runCmd(cmd);
    }

    private void exportPdf() {
        tool = SettingConstants.CALIBRE;
        if (!checkTool(tool)) {
            new AlertNotification("Đường dẫn Calibre (ebook-convert.exe) không hợp lệ!\nXem lại thiết lập trong cài đặt!").setVisible(true);
            return;
        }
        String fileName = properties.getName() + " - " + properties.getAuthor() + ".pdf";
        fileName = fileName.replaceAll("[:/\\?\\*]", "");
        String cmd = tinyCmd(tool)
                + " "
                + tinyCmd(properties.getSavePath() + Constants.l + "data" + Constants.l + "content.opf")
                + " "
                + tinyCmd(properties.getSavePath() + Constants.l + "out" + Constants.l + fileName)
                + " --paper-size=" + compressLevel;
        runCmd(cmd);

    }

    public void createToc() {
        TableOfContent tableOfContent = new TableOfContent(properties);
        tableOfContent.setAutoSplit(autoSplit);
        tableOfContent.setIncludeImg(includeImg);
        tableOfContent.makeTableOfContent();
    }

    private boolean checkTool(String tool) {
        if (tool == null) return false;
        if (tool.length() < 2) return false;
        File file = new File(tool);
        if (!file.exists()) return false;
        if (file.isDirectory()) return false;
        if (!tool.endsWith(".exe")) return false;
        return true;
    }

    private void runCmd(String cmd) {
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
            InputStream s = p.getInputStream();

            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(s, "UTF-8"));
            String temp;
            int percent = 0;
            while ((temp = in.readLine()) != null) {
                String pe = new Syntax().search(temp, "(^\\d+)%", 1);
                if (pe.length() != 0)
                    percent = Integer.parseInt(pe);
                progressListener.setProgress(percent, temp.replaceAll("[\n\r]", " "));
            }
            progressListener.setProgress(100, "Hoàn tất!");

        } catch (Exception e) {
            Log.add("Lỗi CMD: " + cmd);
        }
    }

    private String tinyCmd(String cmd) {
        if (cmd.indexOf(" ") != -1)
            cmd = "\"" + cmd + "\"";
        return cmd;
    }

    public void addProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

}

