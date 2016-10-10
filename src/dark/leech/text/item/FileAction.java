package dark.leech.text.item;

import dark.leech.text.action.Log;
import dark.leech.text.constant.Constants;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class FileAction {

    //Tao thu muc
    public static void mkdir(String dir) {
        File file = new File(dir);
        if (file.exists())
            if (file.isDirectory())
                return;
        String[] path = dir.split(Pattern.quote(Constants.l));
        if (path.length == 1)
            return;
        dir = path[0];
        for (int i = 1; i < path.length; i++) {
            dir += Constants.l + path[i];
            file = new File(dir);
            if (!file.exists()) file.mkdir();
        }
    }

    public String file2string(String path) {
        try {
            return new String(file2byte(path), "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

    public byte[] stream2byte(InputStream in) {
        try {
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            return bytes;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    public String stream2string(String path) {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream(path);
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            return "";
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    public byte[] file2byte(File file) {
        FileInputStream fi = null;
        try {
            if (!file.exists())
                return null;
            fi = new FileInputStream(file);
            byte[] b = new byte[(int) file.length()];
            fi.read(b);
            return b;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (fi != null) fi.close();
            } catch (IOException e) {
            }
        }
    }

    public byte[] file2byte(String path) {
        return file2byte(new File(path));
    }

    public void string2file(String source, String savepath) {
        try {
            byte2file(source.getBytes("UTF-8"), savepath);
        } catch (Exception e) {
            Log.add(source + e);
        }

    }

    public void byte2file(byte[] source, String savepath) {
        FileOutputStream fo = null;
        try {
            File f = new File(savepath);
            if (!f.exists())
                f.createNewFile();
            fo = new FileOutputStream(f);
            fo.write(source);
        } catch (Exception e) {
        } finally {
            try {
                if (fo != null) fo.close();
            } catch (IOException e) {
            }
        }
    }

    public void add2file(String str, File file) {
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream(file, true);
            fo.write(str.getBytes("UTF-8"));
        } catch (Exception e) {
        } finally {
            try {
                if (fo != null) fo.close();
            } catch (IOException e) {
            }
        }
    }

    public void add2file(File a, File b) {
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream(b, true);
            fo.write(file2byte(a));
        } catch (Exception e) {
        } finally {
            try {
                if (fo != null) fo.close();
            } catch (IOException e) {
            }
        }
    }

    public void copyFile(String from, String to) {
        byte2file(file2byte(from), to);
    }

    public void cutFile(String from, String to) {
        copyFile(from, to);
        deleteFile(from);
    }

    public void url2file(String url, String savePath) {
        ByteArrayOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            HttpURLConnection hc = (HttpURLConnection) (new URL(url))
                    .openConnection();
            hc.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36");
            hc.setRequestProperty("Accept",
                    "application/json, text/html, */*n");
            hc.setRequestProperty("Connection", "keep-alive");
            hc.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            hc.connect();
            InputStream is = hc.getInputStream();

            // Lấy dữ liệu sang bytearray
            bos = new ByteArrayOutputStream();
            dos = new DataOutputStream(bos);
            int c = 0;
            byte[] bf = new byte[4096];

            while ((c = is.read(bf)) > 0)
                dos.write(bf, 0, c);

            bf = bos.toByteArray();
            byte2file(bf, savePath);
        } catch (Exception e) {
        } finally {
            try {
                if (bos != null) bos.close();
                if (dos != null) dos.close();
            } catch (Exception e) {
            }
        }
    }

    public void deleteFile(String path) {
        try {
            File f = new File(path);
            if (f.exists())
                f.delete();
        } catch (Exception e) {
        }
    }
}
