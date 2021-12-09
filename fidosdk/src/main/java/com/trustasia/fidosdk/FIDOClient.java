package com.trustasia.fidosdk;

import android.net.Uri;
import android.util.Base64;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.trustasia.fidosdk.exception.BaseException;
import com.trustasia.fidosdk.http.RequestManager;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

@SuppressWarnings("ALL")
public class FIDOClient implements IWebauthn {
    private String baseUrl = null;
    private String rpId = null;
    private Gson gson;
    private static FIDOClient INSTANCE;

    public static synchronized IWebauthn getInstance() {
        if (INSTANCE == null) INSTANCE = new FIDOClient();
        return INSTANCE;
    }

    private FIDOClient() {
    }

    private CallbackFragment fragment;

    @Override
    public void init(String url) {
        if (url == null || url.isEmpty()) {
            throw new RuntimeException("url must not bet null or emtpy");
        }
        if (!url.endsWith("/")) {
            throw new RuntimeException("url must end with \"/\"");
        }
        this.baseUrl = url;
        RequestManager.getInstance().init();
        this.rpId = Uri.parse(url).getHost();
        this.gson = new GsonBuilder().registerTypeAdapter(byte[].class, (JsonSerializer<byte[]>) (src, typeOfSrc, context) -> {
            if (src == null) return null;
            String bas64 = Base64.encodeToString(src, Base64.NO_PADDING | Base64.URL_SAFE | Base64.NO_WRAP);
            return new JsonPrimitive(bas64);
        }).create();
    }

    @Override
    public void make(FragmentActivity activity, String userName, String displayName, boolean rk, Callback<String> callback) {
        if (baseUrl == null) throw new RuntimeException("must init first");
        fragment = CallbackFragment.newInstance(baseUrl);
        fragment.showNow(activity.getSupportFragmentManager(), "make");
        fragment.requestMake(rk, userName, displayName, callback);
    }


    @Override
    public void get(FragmentActivity activity, String userName, String displayName, boolean rk, Callback<String> callback) {
        if (baseUrl == null) throw new RuntimeException("must init first");
        fragment = CallbackFragment.newInstance(baseUrl);
        fragment.showNow(activity.getSupportFragmentManager(), "get");
        fragment.requestGet(userName, displayName, callback);
    }


    /**
     * check server exception by http code or json code
     *
     * @param response
     */
    private String valdateResponse(Response response) {
        if (response.code() != 200) {
            throw new BaseException(new IOException("网络错误，错误码:" + response.code()), response.code());
        }
        JSONObject jsonObject;
        String json = null;
        try {
            json = response.body().string();
            jsonObject = new JSONObject(json);
        } catch (Exception e) {
            throw new BaseException(new IOException("网络数据错误，错误码:-1"), -1);
        }
        int code = jsonObject.optInt("code");
        if (code != 0) {
            String message = jsonObject.optString("error");
            throw new BaseException(new IOException(message), code);
        }
        return json;
    }
}
