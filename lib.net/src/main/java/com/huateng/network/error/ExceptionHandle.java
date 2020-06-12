package com.huateng.network.error;

import com.google.gson.JsonParseException;
import com.tools.utils.NetworkUtils;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;


/**
 * Created by Sumincy on 2018/8/25.
 */
public class ExceptionHandle {
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;


    public static ResponeThrowable handleException(Throwable e) {
        ResponeThrowable ex;
      //  Logger.i("e.toString = " + e.toString()+":");

        if (!NetworkUtils.isConnected()) {
            ex = new ResponeThrowable(e, ERROR.NETWORK_ERROR);
            ex.message = "网络连接不可用,请检查网络情况";
            return ex;
        }

        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponeThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:

                    break;
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.message = "网络异常";
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponeThrowable(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
            /*|| e instanceof ParseException*/) {
            ex = new ResponeThrowable(e, ERROR.PARSE_ERROR);
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponeThrowable(e, ERROR.NETWORK_ERROR);
            ex.message = "连接失败";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponeThrowable(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            ex = new ResponeThrowable(e, ERROR.NETWORK_ERROR);
            ex.message = "网络异常,请稍后重试";
            return ex;
        } else {
            if (null != e.getMessage() && e.getMessage().contains("Timeout")) {
                ex = new ResponeThrowable(e, ERROR.TIMEOUT);
                ex.message = "登录超时,请重新登录";
            }else if (null != e.getMessage() && e.getMessage().contains("tokenOverdue")) {
                ex = new ResponeThrowable(e, ERROR.TOKEN_OVERDUE);
                ex.message = "Token过期,请重新登录";
            }else {
                ex = new ResponeThrowable(e, ERROR.UNKNOWN);
                ex.message = "网络异常,请稍后重试";
            }
            return ex;
        }
    }


    /**
     * 约定异常
     */
    public class ERROR {

        /**
         * 登录超时
         */
        public static final int TIMEOUT = -1;


        public static final int TOKEN_OVERDUE = 302;

        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORK_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;
    }

    public static class ResponeThrowable extends Exception {
        public int code;
        public String message;

        public ResponeThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }

        public ResponeThrowable(String message, int code) {
            this.code = code;
            this.message = message;
        }
    }

    /**
     * ServerException发生后，将自动转换为ResponeThrowable返回
     */
   public static class ServerException extends RuntimeException {
        int code;
        String message;
/*
        public ServerException() {

        }

        public ServerException(int code, String message) {
            this.code = code;
            this.message = message;
        }*/
    }

}
