package com.tools.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具
 */
public class StringUtils {

    /**
     * 设置手机号中间四个隐藏
     */
    public static String hideMobile(String mobile) {
        return mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
    }

    /**
     * 设置银行卡号只显示最后四位
     */
    public static String hideBankCard(String string) {
        String idCard = string.toString().trim();
        String endCard = idCard.substring(idCard.length() - 4, idCard.length());
        StringBuffer hideCard = new StringBuffer();
        for (int i = 0; i <= idCard.length() - 4; i++) {
            hideCard.append("*");
        }
        string = hideCard + endCard;
        StringBuilder builder = new StringBuilder(string);
        int lenth = string.length();
        int offset = lenth / 4;
        for (int i = 1; i < offset; i++) {
            builder.insert(5 * i - 1, " ");
        }
        return builder.toString();
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean isEmpty(String... str) {
        if (str == null) {
            return true;
        }
        for (String s : str) {
            if (s == null || s.isEmpty() || s.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String... str) {
        if (str == null) {
            return false;
        }
        for (String s : str) {
            if (isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一样的
     */
    public static boolean isEquals(String src, String target) {
        if (isEmpty(src, target)) {
            return false;
        }
        return src.equals(target);
    }

    /**
     * 判断是不是一样的
     */
    public static boolean isEqualsIgnoreCase(String src, String target) {
        if (isEmpty(src, target)) {
            return false;
        }
        return src.equalsIgnoreCase(target);
    }

    /**
     * 以一种简单的方式格式化字符串
     * 例如 String s = Strings.format("{0} is {1}", "apple", "fruit");
     * 输出 apple is fruit.
     *
     * @param pattern
     * @param args
     * @return
     */
    public static String format(String pattern, Object... args) {
        for (int i = 0; i < args.length; i++) {
            pattern = pattern.replace("{" + i + "}", args[i].toString());
        }
        return pattern;
    }

    /**
     * 随机的UUID
     *
     * @return
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }


    /**
     * 首字母变为大写，其他保持不变
     *
     * @param str
     * @return
     */
    public static String capitalize(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }

        final char firstChar = str.charAt(0);
        final char newChar = Character.toUpperCase(firstChar);
        if (firstChar == newChar) {
            // already capitalized
            return str;
        }

        char[] newChars = new char[strLen];
        newChars[0] = newChar;
        str.getChars(1, strLen, newChars, 1);
        return String.valueOf(newChars);
    }


    /**
     * 从assets获取string
     *
     * @param context
     * @param fileName 相对文件名, 支持多层结构, 如"pro_gg.txt", "images/pro/11/details.json",
     * @return
     */
    public static String getStringFromAssets(Context context, String fileName) {
        if (context == null || TextUtils.isEmpty(fileName)) {
            return null;
        }

        StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从raw获取string
     *
     * @param context
     * @param resId   res/raw/下的资源id
     * @return
     */
    public static String getStringFromRaw(Context context, int resId) {
        if (context == null) {
            return null;
        }

        StringBuilder s = new StringBuilder();
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从assets获取多行string
     *
     * @param context
     * @param fileName 相对文件名, 支持多层结构, 如"pro_gg.txt", "images/pro/11/details.json",
     * @return
     */
    public static List<String> getStringListFromAssets(Context context, String fileName) {
        if (context == null || TextUtils.isEmpty(fileName)) {
            return null;
        }

        List<String> fileContent = new ArrayList<String>();
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                fileContent.add(line);
            }
            br.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从raw获取多行string
     *
     * @param context
     * @param resId   res/raw/下的资源id
     * @return
     */
    public static List<String> getStringListFromRaw(Context context, int resId) {
        if (context == null) {
            return null;
        }

        List<String> fileContent = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
            reader = new BufferedReader(in);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String repairDouble(int number) {
        DecimalFormat df = new DecimalFormat("00");
        String str = df.format(number);
        return str;
    }

    //"###,###,###.00";
    public static String decimalFormat(double decimal) {
        String style = "###########0.00";
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(style);
        String result = df.format(decimal);
        return result;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
