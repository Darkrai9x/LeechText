package dark.leech.text.item;

public class Chapter {

    private String url; // Đường dẩn
    private String partName; // Tên quyển
    private String chapName; // Tên chương
    private boolean get; // Cho phép lấy chương này
    private boolean completed; //Đã tải hoàn tất
    private boolean error; // Lỗi
    private boolean imgChap;
    private int id; // ID chương

    public Chapter() {
        this("");
    }

    public Chapter(String url) {
        this(url, "");
    }

    public Chapter(String url, String name) {
        this(url, -1, "", name);
    }

    public Chapter(String url, int id, String partName, String chapName) {
        this(url, id, partName, chapName, false, false, false);
    }

    public Chapter(String url, int id, String partName, String chapName, boolean get, boolean completed, boolean error) {
        this.url = url;
        this.id = id;
        this.partName = partName;
        this.chapName = chapName;
        this.get = get;
        this.error = error;
        this.completed = completed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChapName() {
        return chapName;
    }

    public void setChapName(String chapName) {
        this.chapName = chapName;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isImgChap() {
        return imgChap;
    }

    public void setImgChap(boolean imgChap) {
        this.imgChap = imgChap;
    }

    public boolean isGet() {
        return get;
    }

    public void setGet(boolean get) {
        this.get = get;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
