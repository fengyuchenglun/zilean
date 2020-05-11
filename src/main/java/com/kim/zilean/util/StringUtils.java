package com.kim.zilean.util;

/**
 * @author duanledexianxianxian
 * @date 2020/5/10 12:17
 * @since 1.0.0
 */
public class StringUtils {
    /**
     * 空字符
     */
    public static final String EMPTY = "";
    /**
     * 下划线字符
     */
    public static final char UNDERLINE = '_';

    public static boolean isBlank(final CharSequence cs) {
        if (cs == null) {
            return true;
        }
        int l = cs.length();
        if (l > 0) {
            for (int i = 0; i < l; i++) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 字符串下划线转驼峰格式
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String underlineToCamel(String param) {
        if (isBlank(param)) {
            return EMPTY;
        }
        String temp = param.toLowerCase();
        int len = temp.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = temp.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(temp.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
