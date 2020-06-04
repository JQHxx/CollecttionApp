package com.aes_util;


/**
 * AES工具类，密钥必须是16位字符串
 * liqq
 */
public class AESUtils {

    static AesEncryptHandleImpl aesEncrypt = new AesEncryptHandleImpl(EncryptConstants.ALGORITHM_AES_ECB_PKCS5);

    public static String encrypt(String content, String key) {
        try {
            return aesEncrypt.encrypt(content, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String content, String key) {
        return aesEncrypt.decrypt(content, key);
    }

   /* public static void main(String[] args) {
        String plainText = AESUtils.encrypt("jpc", Constants.AUTHCODE);
        System.out.println("aes加密后: " + plainText);
        System.out.println("aes解密后: " + AESUtils.decrypt(plainText, Constants.AUTHCODE));

    }*/

}
