package me.david.sploty4.util;

public final class StringUtil {

    public static boolean isEmpty(String str){
        return str == null || str.length() == 0;
    }

    public static String humanReadableBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = ("kMGTPE").charAt(exp-1);
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
