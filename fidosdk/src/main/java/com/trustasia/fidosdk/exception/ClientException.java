package com.trustasia.fidosdk.exception;


public class ClientException extends BaseException {

    public ClientException(Throwable cause) {
        super(cause);
        if (cause instanceof BaseException) {
            code = ((BaseException) cause).getCode();
        } else {
            code = 0x7f;
        }
    }
}
