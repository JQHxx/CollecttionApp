package com.huateng.phone.collection.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static String TWO_BYTE_STR = "[^\\x00-\\xff]*";
    public static String CHINESE_ENGLISH_NUM = "[\\u4e00-\\u9fa5|\\w]*";
    public static String EMAIL = "[\\w\\.-]+@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    //	public static String EMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    public static String BANK = "[\\u4e00-\\u9fa5|\\w]*";
    public static String BANK_ACCOUNT = "[\\d]*";
    public static String MOBILE = "^\\+?[\\d]{7,15}$";
    public static String ADDRESS = "[\\u4e00-\\u9fa5|\\w|\\-]*";
    public static String ZIPCODE = "[\\w|\\-]{4,8}"; //"^[\\d]{4,6}$";
    public static String IDENTITY_CARD = "(^\\d{15}$)|(\\d{17}(?:\\d|x|X)$)";
    //public static String PWD = "([\\w`~!@#\\$%\\^&\\*\\(\\)_\\+\\-=\\[\\]\\{\\};:'\",<\\.>/\\?]{6,20})";
    public static String DOMAIN = "([a-z0-9]{5,15})";
    public static String SITE_DOMAIN = "^(?!\\d+$)[0-9a-z]{5,15}$";
    public static String NUMBER = "^[0-9]+$";
    public static String VERFIY = "^[0-9a-f]+$";
    public static String VERFIY_FORGET = "^[0-9a-zA-Z]{4}$";
    public static String PWD = "^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z][a-zA-Z0-9_]{5,17}$";


    /**
     * 匹配格式： 11位手机号码 3-4位区号，7-8位直播号码，1－4位分机号 如：12345678901、1234-12345678-1234
     */
    public static String TEL = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";


    public static boolean matches(String str, String regex) {
        if (str == null)
            return false;
        str = str.trim();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        boolean b = m.matches();
        return b;
    }

    /**
     * 真实姓名的格式验证,中文,字母,数字
     *
     * @param realName
     * @return
     */
    public static boolean realNameCheck(String realName) {
        if (realName == null)
            return false;
        //Unicode里中文、日文与韩文的汉字\\p{InCJK Unified Ideographs
//		String cn_words = "[\\w\\p{InCJK Unified Ideographs}\\P{Cn}]" + "{2," + length + "}";
        String cn_words = "^(?![0-9]+$)[\\w\\u4e00-\\u9fa5]+";// + "{2," + length + "}";//^(?![0-9]+$)表示不能全部是数字
        Pattern p = Pattern.compile(cn_words);
        Matcher m = p.matcher(realName);
        return m.matches();
    }

    public static boolean emailCheck(String email) {
        return Validation.matches(email, Validation.EMAIL);
    }

    public static boolean bankCheck(String bank) {
        if (bank == null)
            return false;
        bank = bank.trim();
        String cn_words = "[\\u4e00-\\u9fa5|\\w]*";
        Pattern p = Pattern.compile(cn_words);
        Matcher m = p.matcher(bank);
        return m.matches();
    }

    public static boolean bankAccountCheck(String account) {
        if (account == null)
            return false;
        account = account.trim();
        if (account.length() != 16 && account.length() != 17
                && account.length() != 19)
            return false;
        String cn_words = "[\\d]*";
        Pattern p = Pattern.compile(cn_words);
        Matcher m = p.matcher(account);
        return m.matches();
    }


    public static boolean mobileCheck(String mobile) {
        return Validation.matches(mobile, Validation.MOBILE);
    }

    public static boolean imCheck(String im) {
        if (im == null)
            return false;
        im = im.trim();
        if (Validation.emailCheck(im))
            return true;
        Pattern p = Pattern.compile("[\\d]{5,14}");
        Matcher m = p.matcher(im);
        return m.matches();
    }

    public static boolean telCheck(String tel) {
        return Validation.matches(tel, Validation.TEL);
    }

    public static boolean addressCheck(String address) {
        return Validation.matches(address, Validation.ADDRESS);
    }

    public static boolean zipcodeCheck(String zipcode) {
        return Validation.matches(zipcode, Validation.ZIPCODE);
    }

    public static boolean identityCardCheck(String card) {
        return Validation.matches(card, Validation.IDENTITY_CARD);
    }

    public static boolean pwdCheck(String pwd) {
        return Validation.matches(pwd, Validation.PWD);
    }

    public static boolean domainCheck(String domain) {
        return Validation.matches(domain, Validation.DOMAIN);
    }

    public static boolean siteDomainCheck(String domain) {
        return Validation.matches(domain, Validation.SITE_DOMAIN);
    }

    public static boolean numberCheck(String domain) {
        return Validation.matches(domain, Validation.NUMBER);
    }

    public static boolean verifyCheck(String verify) {
        return Validation.matches(verify, Validation.VERFIY);
    }

    public static boolean verifyForgetCheck(String verify) {
        return Validation.matches(verify, Validation.VERFIY_FORGET);
    }

    public static boolean staffPasswordCheck(String password) {
        if (password.length() < 8 || password.length() > 20)
            return false;

        Pattern ptn = Pattern.compile(".*([\\d]+).*");
        Matcher m = ptn.matcher(password);
        if (!m.matches())
            return false;

        ptn = Pattern.compile(".*([a-z]+).*");
        m = ptn.matcher(password);
        if (!m.matches())
            return false;

        ptn = Pattern.compile(".*([A-Z]+).*");
        m = ptn.matcher(password);
        if (!m.matches())
            return false;

        ptn = Pattern.compile(".*([`~!@#\\$%\\^&\\*\\(\\)_\\+\\-=\\[\\]\\{\\};:'\",<\\.>/\\?]+).*");
        m = ptn.matcher(password);
        if (!m.matches())
            return false;
        return matches(password, "([\\w`~!@#\\$%\\^&\\*\\(\\)_\\+\\-=\\[\\]\\{\\};:'\",<\\.>/\\?]{8,20})");
//		return Validation.matches(password, "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?![a-zA-Z]+$)[\\w]+");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
    }

}
