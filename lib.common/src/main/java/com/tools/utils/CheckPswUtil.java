package com.tools.utils;

/**
 * @author huangyang
 * @since 创建时间
 * <br><b>密码格式检查工具</b></br>
 */
public class CheckPswUtil {

    public static boolean checkWord(String passWord) {

        boolean resultNum = passWord.matches("[0-9]+");//true表示是纯数字
        boolean resultStr = passWord.matches("[a-zA-Z]+");//表示是纯英文

        return (!resultNum) && (!resultStr);

    }


    public static boolean checkPassword(String pass){
       // return pass.matches("/^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)]|[\\(\\)])+$)");

        return pass.matches("^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)\\S{6,20}$");


    }

}