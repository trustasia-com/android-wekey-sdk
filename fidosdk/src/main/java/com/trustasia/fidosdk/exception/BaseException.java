package com.trustasia.fidosdk.exception;

public class BaseException extends RuntimeException {


    protected int code = 0;

    public BaseException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public BaseException(Throwable cause, int code) {
        super(cause.getMessage(), cause);
        this.code = code;
    }

    public BaseException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public int getCode() {
        return code;
    }
}
