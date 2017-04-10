package dark.leech.text.util;

import dark.leech.text.action.Log;
import org.zeroturnaround.zip.FileSource;
import org.zeroturnaround.zip.ZipEntrySource;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Long on 10/1/2016.
 */
public class ZipUtils {
    private ZipUtils() {
    }

    public static void addFile(String zip, String file) {
        addFile(new File(FileUtils.validate(zip)), new File(FileUtils.validate(file)));
    }

    public static void addFile(File zip, File file) {
        addFile(zip, file, "");
    }

    public static void addFile(File zip, File file, String path) {
        if (!file.exists()) return;
        ZipUtil.addEntry(zip, (path.length() == 0 ? "" : path + "/") + file.getName(), file);
    }

    public static void addFolders(String zip, String dir) {
        addFolders(zip, dir, "");
    }

    public static void addFolders(String zip, String dir, String path) {
        addFolders(new File(FileUtils.validate(zip)), new File(FileUtils.validate(dir)), path);
    }

    public static void addFolders(File zip, File dir) {
        addFolders(zip, dir, "");
    }

    public static void addFolders(File zip, File dir, String path) {
        ArrayList<ZipEntrySource> listEntry = new ArrayList<ZipEntrySource>();
        getListEntrySource(listEntry, dir, path);
        ZipUtil.addEntries(zip, listEntry.toArray(new ZipEntrySource[listEntry.size()]));
    }

    private static void getListEntrySource(ArrayList<ZipEntrySource> listEntrySources, File dir, String path) {
        File[] listFile = dir.listFiles();
        for (File f : listFile) {
            if (f.isDirectory())
                getListEntrySource(listEntrySources, f, path + "/" + f.getName());
            else
                listEntrySources.add(new FileSource(path + "/" + f.getName(), f));
        }
    }

    public static byte[] readInZipAsByte(File zipfile, String filepath) {
        return ZipUtil.unpackEntry(zipfile, filepath);
    }

    public static String readInZipAsString(File zipfile, String filepath) {
        try {
            return new String(readInZipAsByte(zipfile, filepath), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.add(e.toString());
            return "";
        }
    }
}

