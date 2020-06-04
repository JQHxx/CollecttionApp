package com.aes_util;

/**
 * 加密密码异常
 */
public class EncryptException extends RuntimeException {

    public EncryptException(Throwable cause) {
        super(cause);
    }

    public EncryptException(String message) {
        super(message);
    }

    public EncryptException(String message, Throwable cause) {
        super(message, cause);
    }
}
