package com.pixel.okhttp.others;

/**
 * @author Administrator
 * @date 2017/11/29 0029
 */

public class HttpException extends Exception {

    // 错误描述
    private String describe = "";

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, String describe) {
        super(message);
        this.describe = describe;
    }

    public HttpException(String message, Throwable cause, String describe) {
        super(message, cause);
        this.describe = describe;
    }

    public HttpException(Throwable cause, String describe) {
        super(cause);
        this.describe = describe;
    }

    @Override
    public String toString() {
        return "HttpException{" +
                "describe='" + describe + '\'' +
                "message='" + getMessage() + '\'' +
                "LocalizedMessage='" + getLocalizedMessage() + '\'' +
                '}';
    }
}
