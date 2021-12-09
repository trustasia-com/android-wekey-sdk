package com.trustasia.fidosdk.exception;


public class ServerException extends BaseException {
    public int code;

    public ServerException(Throwable cause) {
        super(cause);
        if (cause instanceof BaseException) {
            code = ((BaseException) cause).getCode();
        } else {
            code = 0;
        }
    }

}
