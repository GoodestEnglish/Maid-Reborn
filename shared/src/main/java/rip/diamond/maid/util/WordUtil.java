package rip.diamond.maid.util;

public class WordUtil {

    public static String translate(boolean bool) {
        return translate(bool, "是", "否");
    }

    public static String translate(boolean bool, String yes, String no) {
        return bool ? yes : no;
    }
}
