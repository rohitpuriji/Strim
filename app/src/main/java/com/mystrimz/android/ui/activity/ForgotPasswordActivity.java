package com.mystrimz.android.ui.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mystrimz.android.R;
import com.mystrimz.android.bean.RegisterBean;
import com.mystrimz.android.http.apicallback.SimpleMessageCallBack;
import com.mystrimz.android.http.apicallback.UserCallBack;
import com.mystrimz.android.http.requests.UserRequest;
import com.mystrimz.android.util.Utility;
import com.mystrimz.android.util.Validation;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_resetPassword;
    private EditText et_email;
    private Validation validation;
    private ImageView im_backArrow;
    private ProgressDialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        init();
        onClickLisener();
    }

    private void onClickLisener() {
        tv_resetPassword.setOnClickListener(this);
        im_backArrow.setOnClickListener(this);
    }

    private void init() {
        validation = Validation.getInstance();
        et_email = (EditText)findViewById(R.id.email);
        tv_resetPassword = (TextView)findViewById(R.id.resetPassword);
        im_backArrow = (ImageView)findViewById(R.id.imgBackArrow);
        myDialog= Utility.showProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.resetPassword:
                if (Utility.getInstance().isOnline(this)){
                    callResetPassword();
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgBackArrow:
                finish();
                break;
        }

    }

    private void callResetPassword() {

        if (!validation.isEmpty(et_email.getText().toString().trim())){
            et_email.setError(getString(R.string.error_emptyEmail));
            return;

        } else if (!validation.isValidEmail(et_email.getText().toString().trim())){
            et_email.setError(getString(R.string.error_email));
            return;

        } else{
            callForgotPasswordAPi();
        }
    }

    private void callForgotPasswordAPi() {

        String email = et_email.getText().toString().trim();

        myDialog.show();

        UserRequest userRequest = UserRequest.getInstance();
        userRequest.callForgotPassApi(email, new SimpleMessageCallBack() {

            @Override
            public void onSuccess(String message) {
                myDialog.dismiss();
                saveUserDetails(message);
            }

            @Override
            public void onError(String error) {
                myDialog.dismiss();
                Toast.makeText(ForgotPasswordActivity.this, error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void saveUserDetails(String message) {

        Toast.makeText(ForgotPasswordActivity.this, message , Toast.LENGTH_LONG).show();
        finish();
    }
}
