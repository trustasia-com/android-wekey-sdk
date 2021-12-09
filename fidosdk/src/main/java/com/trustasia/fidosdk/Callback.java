package com.trustasia.fidosdk;

public interface Callback<T> {
    void onResp(T data);

    void onError(Throwable throwable);
}
