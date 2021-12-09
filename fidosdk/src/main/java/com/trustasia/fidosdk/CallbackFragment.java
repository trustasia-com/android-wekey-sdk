package com.trustasia.fidosdk;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.trustasia.fidosdk.bean.GetReq;
import com.trustasia.fidosdk.bean.MakeReq;
import com.trustasia.fidosdk.bean.Selection;
import com.trustasia.fidosdk.exception.BaseException;
import com.trustasia.fidosdk.exception.ServerException;
import com.trustasia.fidosdk.http.RequestManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CallbackFragment extends DialogFragment {
    private Callback<String> callback;
    private String baseUrl;
    private String rpId = null;
    private Gson gson;
    private static final String KEY_DATA = "key_webauthn_payload";
    private static final String KEY_RP_ID = "key_rp_id";
    private static final String KEY_RESULT = "key_webauthn_result";
    private static final int REQUEST_WEBAUTHN_GET = 2021;
    private static final int REQUEST_WEBAUTHN_MAKE = 2022;
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static CallbackFragment newInstance(String baseUrl) {
        Bundle args = new Bundle();
        CallbackFragment fragment = new CallbackFragment();
        args.putString("base_url", baseUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext(), R.style.BaseDialog);
        dialog.getWindow().setDimAmount(0f);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        this.baseUrl = getArguments().getString("base_url");
        this.rpId = Uri.parse(baseUrl).getHost();
        this.gson = new GsonBuilder().registerTypeAdapter(byte[].class, (JsonSerializer<byte[]>) (src, typeOfSrc, context) -> {
            if (src == null) return null;
            String bas64 = Base64.encodeToString(src, Base64.NO_PADDING | Base64.URL_SAFE | Base64.NO_WRAP);
            return new JsonPrimitive(bas64);
        }).create();
    }

    /**
     * request rp then call wekey app to return the get result
     *
     * @param callback callback the result or error
     */

    public void requestGet(String userName, String displayName, Callback<String> callback) {
        this.callback = callback;
        CompletableFuture.supplyAsync(() -> {
            GetReq getReq = new GetReq(displayName, userName);
            String reqJSON = gson.toJson(getReq);
            RequestBody requestBody = RequestBody.create(reqJSON, MediaType.parse("application/json"));
            String url = baseUrl + "assertion/options";
            try {
                String json;
                try {
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    Response response = RequestManager.getInstance().getClient().newCall(request).execute();
                    json = validateResponse(response);
                } catch (Exception e) {
                    throw new ServerException(e);
                }
                Intent intent = new Intent().setComponent(new ComponentName("com.trustasia.wekey", "com.trustasia.main.feature.selector.SelectorActivity"));
                intent.putExtra(KEY_DATA, json);
                intent.putExtra(KEY_RP_ID, rpId);
                startActivityForResult(intent, REQUEST_WEBAUTHN_GET);
            } catch (Exception e) {
                dismiss();
                e.printStackTrace();
                mainHandler.post(() -> callback.onError(e));
            }
            return true;
        });
    }


    /**
     * request rp then call wekey app to return the make result
     *
     * @param rk          weather to user rk
     * @param userName    user name
     * @param displayName display name
     * @param callback    callback the result or error
     */
    public void requestMake(boolean rk, String userName, String displayName, Callback<String> callback) {
        this.callback = callback;
        CompletableFuture.supplyAsync(() -> {
            MakeReq makeReq = new MakeReq("direct", new Selection(rk), displayName, userName);
            String reqJSON = gson.toJson(makeReq);
            RequestBody requestBody = RequestBody.create(reqJSON, MediaType.parse("application/json"));
            String url = baseUrl + "attestation/options";
            try {
                String json;
                try {
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    Response response = RequestManager.getInstance().getClient().newCall(request).execute();
                    json = validateResponse(response);
                } catch (Exception e) {
                    throw new ServerException(e);
                }
                Intent intent = new Intent().setComponent(new ComponentName("com.trustasia.wekey", "com.trustasia.main.feature.selector.SelectorActivity"));
                intent.putExtra(KEY_DATA, json);
                intent.putExtra(KEY_RP_ID, rpId);
                startActivityForResult(intent, REQUEST_WEBAUTHN_MAKE);
            } catch (Exception e) {
                dismiss();
                e.printStackTrace();
                mainHandler.post(() -> callback.onError(e));
            }
            return true;
        });
    }


    private void submitResult(String path, String data) {
        CompletableFuture.supplyAsync(() -> {
            try {
                //submit result to rp
                String submitUrl = baseUrl + path;
                RequestBody submitBody = RequestBody.create(data, MediaType.parse("application/json"));
                Request submitRequest = new Request.Builder().url(submitUrl).post(submitBody).build();
                Response submitResponse = RequestManager.getInstance().getClient().newCall(submitRequest).execute();
                String result = validateResponse(submitResponse);
                mainHandler.post(() -> {
                    if (callback != null) {
                        callback.onResp(result);
                        callback = null;
                    }
                });
            } catch (Exception e) {
                ServerException exception = new ServerException(e);
                mainHandler.post(() -> {
                    if (callback != null) {
                        callback.onError(exception);
                        callback = null;
                    }
                });
                throw exception;
            }
            return true;
        });
    }


    /**
     * check server exception by http code or json code
     *
     * @param response
     */
    private String validateResponse(Response response) {
        if (response.code() != 200) {
            throw new BaseException(new IOException("网络错误，错误码:" + response.code()), response.code());
        }
        JSONObject jsonObject;
        String json = null;
        try {
            json = Objects.requireNonNull(response.body()).string();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dismiss();
        if (callback == null) return;
        if (requestCode == REQUEST_WEBAUTHN_GET || requestCode == REQUEST_WEBAUTHN_MAKE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) return;
                String result = data.getStringExtra(KEY_RESULT);
                if (requestCode == REQUEST_WEBAUTHN_GET) {
                    submitResult("assertion/result", result);
                } else {
                    submitResult("attestation/result", result);
                }
            } else {
                callback.onError(new BaseException(new RuntimeException("error code: " + resultCode), resultCode));
            }
        }
    }
}
