package com.trustasia.fidosdk;

import androidx.fragment.app.FragmentActivity;


public interface IWebauthn {

    void init(String url);


    /**
     * 发起make请求并自动调用api返回rp
     *
     * @param activity 弹出层上下文
     */
    void make(FragmentActivity activity, String userName, String displayName, boolean rk, Callback<String> callback);


    /**
     * 发起get请求并自动调用api返回rp
     *
     * @param activity 弹出层上下文
     */
    void get(FragmentActivity activity, String userName, String displayName, boolean rk, Callback<String> callback);
}
