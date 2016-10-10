package dark.leech.text.item;
/**
 * Code by Darkrai on 8/21/2016.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Syntax {
    private String[] dau = new String[]{"̀â", "ấ", "ậ", "ẩ", "ẫ", "à", "á", "ạ", "ả", "ã", "â", "ằ", "ắ", "ặ", "ẳ", "ẵ", "ă", "ề", "ế", "ệ", "ể", "ễ", "è", "é", "ẹ", "ẻ", "ẽ", "ê", "ì", "í", "ị", "ỉ", "ĩ", "ồ", "ố", "ộ", "ổ", "ỗ", "ò", "ó", "ọ", "ỏ", "õ", "ô", "ờ", "ớ", "ợ", "ở", "ỡ", "ơ", "ù", "ú", "ụ", "ủ", "ũ", "ừ", "ứ", "ự", "ử", "ữ", "ư", "ỳ", "ý", "ỵ", "ỷ", "ỹ", "đ", "Ầ", "Ấ", "Ậ", "Ẩ", "Ẫ", "À", "Á", "Ạ", "Ả", "Ã", "Â", "Ằ", "Ắ", "Ặ", "Ẳ", "Ẵ", "Ă", "Ề", "Ế", "Ệ", "Ể", "Ễ", "È", "É", "Ẹ", "Ẻ", "Ẽ", "Ê", "Ì", "Í", "Ị", "Ỉ", "Ĩ", "Ồ", "Ố", "Ộ", "Ổ", "Ỗ", "Ò", "Ó", "Ọ", "Ỏ", "Õ", "Ô", "Ờ", "Ớ", "Ợ", "Ở", "Ỡ", "Ơ", "Ù", "Ú", "Ụ", "Ủ", "Ũ", "Ừ", "Ứ", "Ự", "Ử", "Ữ", "Ư", "Ỳ", "Ý", "Ỵ", "Ỷ", "Ỹ", "Đ"};
    private String[] src = new String[]{"ầ", "ấ", "ậ", "ẩ", "ẫ", "à", "á", "ạ", "ả", "ã", "â", "ằ", "ắ", "ặ", "ẳ", "ẵ", "ă", "ề", "ế", "ệ", "ể", "ễ", "è", "é", "ẹ", "ẻ", "ẽ", "ê", "ì", "í", "ị", "ỉ", "ĩ", "ồ", "ố", "ộ", "ổ", "ỗ", "ò", "ó", "ọ", "ỏ", "õ", "ô", "ờ", "ớ", "ợ", "ở", "ỡ", "ơ", "ù", "ú", "ụ", "ủ", "ũ", "ừ", "ứ", "ự", "ử", "ữ", "ư", "ỳ", "ý", "ỵ", "ỷ", "ỹ", "đ", "Ầ", "Ấ", "Ậ", "Ẩ", "Ẫ", "À", "Á", "Ạ", "Ả", "Ã", "Â", "Ằ", "Ắ", "Ặ", "Ẳ", "Ẵ", "Ă", "Ề", "Ế", "Ệ", "Ể", "Ễ", "È", "É", "Ẹ", "Ẻ", "Ẽ", "Ê", "Ì", "Í", "Ị", "Ỉ", "Ĩ", "Ồ", "Ố", "Ộ", "Ổ", "Ỗ", "Ò", "Ó", "Ọ", "Ỏ", "Õ", "Ô", "Ờ", "Ớ", "Ợ", "Ở", "Ỡ", "Ơ", "Ù", "Ú", "Ụ", "Ủ", "Ũ", "Ừ", "Ứ", "Ự", "Ử", "Ữ", "Ư", "Ỳ", "Ý", "Ỵ", "Ỷ", "Ỹ", "Đ"};
    private String[] xoa = new String[]{"a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "i", "i", "i", "i", "i", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u", "y", "y", "y", "y", "y", "d", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "I", "I", "I", "I", "I", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "U", "U", "U", "U", "U", "U", "U", "U", "U", "U", "U", "Y", "Y", "Y", "Y", "Y", "D"};

    public String search(String source, String regex, int group) {
        try {
            Pattern r = Pattern.compile(regex, Pattern.MULTILINE);
            Matcher m = r.matcher(source);
            if (m.find())
                return m.group(group);
            else
                return "";
        } catch (Exception e) {
            return "";
        }
    }

    // convert string
    public String covertString(String source) {
        for (int i = 0; i < src.length; i++)
            source = source.replace(dau[i], src[i]);
        return source;
    }

    // xóa dấu
    public String xoaDau(String source) {
        source = covertString(source);
        for (int i = 0; i < src.length; i++)
            source = source.replace(src[i], xoa[i]);
        return source;
    }

    //fix name
    public String fixName(String source) {
        source = covertString(source);
        source = source.trim();
        source = source.replaceAll(":+", ":").replaceAll("\\.+", ".");
        source = source.replaceAll(" 0{1,2}(\\d+)", " $1");
        return source;
    }
}
