package com.trustasia.fidosdk.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.trustasia.fidosdk.Callback;
import com.trustasia.fidosdk.FIDOClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Callback<String> {
    private static final String RP_BASE_URL = "https://api-dev.wekey.com/ta-webauthn-demo/";
    private TextView mEtUserName;
    private TextView mEtDisplayUserName;
    private CheckBox mRKCheckBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        doBusiness();
    }

    private void initView() {
        mEtUserName = findViewById(R.id.etUserName);
        mEtDisplayUserName = findViewById(R.id.etDisplayName);
        mRKCheckBox = findViewById(R.id.checkbox_rk);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    private boolean checkNullOrEmpty(String field, String error) {
        if (field == null || field.isEmpty()) {
            showToast(error);
            return false;
        }
        return true;
    }


    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void doBusiness() {
        FIDOClient.getInstance().init(RP_BASE_URL);
    }

    @Override
    public void onClick(View view) {
        String userName = mEtUserName.getText().toString().trim();
        String displayName = mEtDisplayUserName.getText().toString().trim();
        boolean rk = mRKCheckBox.isChecked();
        if (!checkNullOrEmpty(userName, "User name required!") || !checkNullOrEmpty(displayName, "Display user name required!")) {
            return;
        }
        if (view.getId() == R.id.btn_login) {
            FIDOClient.getInstance().get(this, userName, displayName, rk, this);
        } else {
            FIDOClient.getInstance().make(this, userName, displayName, rk, this);
        }
    }

    @Override
    public void onResp(String data) {
        Log.d("TAG", data);
        showToast("Success");
    }

    @Override
    public void onError(Throwable throwable) {
        String msg = throwable.getMessage();
        if (msg == null || msg.isEmpty()) {
            msg = "System error";
        }
        showToast(msg);
        FIDOClient.getInstance().init("https://api-dev.wekey.com/ta-webauthn-demo/");
    }

}
