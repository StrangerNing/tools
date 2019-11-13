package me.znzn.tools.utils;

/**
 * @author zhuzening
 * @date 2019/11/12
 * @since 1.0
 */
public class LongNumUtil {

    private static String charArray = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static int scale = 62;

    public static String encode(Long num) {
        StringBuilder stringBuilder = new StringBuilder();
        int remainder = 0;

        while (num > scale - 1) {
            remainder = Long.valueOf(num % scale).intValue();
            stringBuilder.append(charArray.charAt(remainder));

            num = num / scale;
        }

        stringBuilder.append(charArray.charAt(num.intValue()));
        return stringBuilder.reverse().toString();
    }

    public static Long decode(String str) {
        long num = 0;
        int index = 0;
        for (int i = 0; i < str.length(); i++) {
            index = charArray.indexOf(str.charAt(i));
            num += (long)(index * (Math.pow(scale, str.length() - i - 1)));
        }
        return num;
    }
}
