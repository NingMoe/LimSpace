package com.tyiti.easycommerce.util;

import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;

/**
 * 字符串处理的常用方法
 * @author rainyhao
 * @since 2015-3-15 下午4:40:40
 */
public final class XString {

    private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z' };

    /**
     * 判断一个字符串是否为null或者trim之后是否为空字符串
     * @param src 要检查的字符串
     * @return
     */
    public static final boolean isNullOrEmpty(String src) {
        return null == src || "".equals(src.trim());
    }

    /**
     * 将指定的值的数组用空连接成字符串 数组元素中的空值会被替换成空字符串
     * @param args 要连接的值
     * @return
     */
    public static final String join(Object... args) {
        String result = "";
        for (int i = 0; i < args.length; i++) {
            result += null == args[i] ? "" : args[i];
        }
        return result;
    }

    /**
     * 将指定的值的数组用split连接成字符串 数组元素中的空值会被替换成空字符串
     * @param split
     * @param args
     * @return
     * @author hanyao
     * @since 2015年9月23日 下午5:16:36
     */
    public static final String joinWith(char split, Object... args) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            result.append(null == args[i] ? "" : args[i]).append(split);
        }
        return result.toString().substring(0, result.length() - 1);
    }

    /**
     * 使用utf-8对字符串进行url转码
     * @author rainyhao
     * @since 2015-3-17 上午11:58:16
     * @param value 字符串
     * @return
     */
    public static final String encode(String value) {
        return encode(value, "UTF-8");
    }

    /**
     * 使用指定的字符集对字符串进行url转码
     * @author rainyhao
     * @since 2015-3-17 上午11:58:19
     * @param value 字符串
     * @param charset 字符集
     * @return
     */
    public static final String encode(String value, String charset) {
        String ret = "";
        if (null == value) {
            return ret;
        }
        try {
            ret = URLEncoder.encode(value, charset);
        } catch (UnsupportedEncodingException ignore) {
            ret = "转码错误, 未知字符编码: " + charset;
        }
        return ret;
    }

    /**
     * 使用utf-8对做过url转码的字符串进行还原
     * @author rainyhao
     * @since 2015-3-17 下午12:03:05
     * @param value 转码过的字符串
     * @return
     */
    public static final String decode(String value) {
        return decode(value, "UTF-8");
    }

    /**
     * 使用指定的字符集对做过url转码的字符串进行还原
     * @author rainyhao
     * @since 2015-3-17 下午12:03:50
     * @param value 转码过的字符串
     * @param charset 字符集
     * @return
     */
    public static final String decode(String value, String charset) {
        String ret = "";
        if (null == value) {
            return ret;
        }
        try {
            ret = URLDecoder.decode(value, charset);
        } catch (UnsupportedEncodingException ignore) {
            ret = "解码错误, 未知字符编码: " + charset;
        }
        return ret;
    }

    /**
     * 从0-9,a-z,A-Z里随机生成8位长度的字符串
     * @author rainyhao
     * @since 2015-3-18 下午5:33:48
     * @return
     */
    public static final String random() {
        return random(8);
    }

    /**
     * 从0-9,a-z,A-Z里随机生成指定长度的字符串
     * @author rainyhao
     * @since 2015-3-18 下午5:36:29
     * @param length 字符串长度
     * @return
     */
    public static final String random(int length) {
        char[] c = new char[length];
        Random random = new Random();
        for (int i = 0; i < c.length; i++) {
            int index = random.nextInt(CHARS.length);
            c[i] = CHARS[index];
        }
        return new String(c);
    }

    /**
     * 从0-9里随机生成指定长度的字符串
     * @param length 字符串长度
     * @return
     * @author hanyao
     * @since 2015年6月15日 下午5:55:37
     */
    public static final String randomCode(int length) {
        char[] c = new char[length];
        Random random = new Random();
        for (int i = 0; i < c.length; i++) {
            int index = random.nextInt(10);
            c[i] = CHARS[index];
        }
        return new String(c);
    }

    /**
     * 生成随机字母字符串
     * @param length 字符串长度
     * @return
     * @author hanyao
     * @since 2015年6月26日 下午4:34:09
     */
    public static final String randomChar(int length) {
        char[] c = new char[length];
        Random random = new Random();
        for (int i = 0; i < c.length; i++) {
            int index = random.nextInt(CHARS.length - 10) + 10;
            c[i] = CHARS[index];
        }
        return new String(c);
    }

    /**
     * 将字符串数组转换为整型数组
     * @param arr 字符串数组
     * @return
     * @author hanyao
     * @since 2015年6月12日 下午2:35:49
     */
    public static Integer[] stringToIntegers(String... arr) {
        Integer[] intArr = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            intArr[i] = Integer.parseInt(arr[i]);
        }
        return intArr;
    }

    /**
     * 获取字符串长度(一个汉字长度为2)
     * @param str
     * @return
     * @author hanyao
     * @since 2015年7月14日 下午12:23:40
     */
    public static final int getLength(String str) {
        return str.replaceAll("/[^\\x00-\\xff]/g", "aa").length();
    }

    /**
     * 过滤非英文字符
     * @param inStr
     * @return
     * @author hanyao
     * @since 2015年10月24日 下午1:42:50
     */
    public static String trimNotEn(String inStr) {
        char[] myBuffer = inStr.toCharArray();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inStr.length(); i++) {
            UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
            if (ub == UnicodeBlock.BASIC_LATIN) {
                // 英文及数字等
                sb.append(myBuffer[i]);
            }
        }
        return sb.toString();
    }
}
