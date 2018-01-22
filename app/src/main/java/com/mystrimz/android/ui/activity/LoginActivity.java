package com.mystrimz.android.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mystrimz.android.R;
import com.mystrimz.android.bean.RegisterBean;
import com.mystrimz.android.http.apicallback.UserCallBack;
import com.mystrimz.android.http.requests.UserRequest;
import com.mystrimz.android.util.AppSharedPrefrences;
import com.mystrimz.android.util.Constants;
import com.mystrimz.android.util.Utility;
import com.mystrimz.android.util.Validation;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_forgetPassword;
    private EditText et_email;
    private EditText et_password;
    private TextView tv_signIn;
    private Validation validation;
    private ImageView im_backArrow;
    private ProgressDialog myDialog;
    private AppSharedPrefrences appSharedPrefrences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        init();
        onclickListener();
    }

    private void onclickListener() {
        tv_forgetPassword.setOnClickListener(this);
        tv_signIn.setOnClickListener(this);
        im_backArrow.setOnClickListener(this);
    }

    private void init() {
        tv_forgetPassword =(TextView)findViewById(R.id.forgot_password);
        et_email = (EditText)findViewById(R.id.email);
        et_password = (EditText)findViewById(R.id.password);
        tv_signIn = (TextView)findViewById(R.id.singIn);
        validation = Validation.getInstance();
        im_backArrow = (ImageView)findViewById(R.id.imgBackArrow);

        myDialog = Utility.showProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgot_password:
                if (Utility.getInstance().isOnline(this)) {
                    callResetPasswordScreen();

                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.singIn:
                if (Utility.getInstance().isOnline(this)) {
                    callsignInApi();
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgBackArrow:
                finish();
                break;
        }
    }

    private void callResetPasswordScreen() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void callsignInApi() {

        if (!validation.isEmpty(et_email.getText().toString().trim())){
            et_email.setError(getString(R.string.error_emptyEmail));
            return;

        } else if (!validation.isValidEmail(et_email.getText().toString().trim())){
            et_email.setError(getString(R.string.error_email));
            return;

        } else if (!validation.isEmpty(et_password.getText().toString().trim())){
            et_password.setError(getString(R.string.error_emptyPassword));
            return;

        }else if (!validation.isValidPassword(et_password.getText().toString().trim())){
            et_password.setError(getString(R.string.error_password));
            return;

        } else{
            CallLoginApi();

        }

    }

    private void CallLoginApi() {

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String devicetype = Constants.DEVICE_TYPE_ANDROID;
        String devicetoken = FirebaseInstanceId.getInstance().getToken();

        myDialog.show();

        UserRequest registerUserRequest = UserRequest.getInstance();
        registerUserRequest.callLoginApi(email, password, devicetype, devicetoken, new UserCallBack() {

            @Override
            public void onSuccess(RegisterBean baseBean) {
                myDialog.dismiss();
                saveValueOfLogin(baseBean);
            }
            @Override
            public void onError(String error) {
                myDialog.dismiss();
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void saveValueOfLogin(RegisterBean baseBean) {
        appSharedPrefrences = AppSharedPrefrences.getInstance(LoginActivity.this);

        appSharedPrefrences.setPreference(Constants.KEY_ACCESS_TOKEN, baseBean.getAccessToken());
        appSharedPrefrences.setPreference(Constants.KEY_USER_ID, baseBean.getUserId());
        appSharedPrefrences.setPreference(Constants.KEY_USER_NAME, baseBean.getFirstname()+ " "+ baseBean.getLastname());
        appSharedPrefrences.setPreference(Constants.KEY_EMAIL, baseBean.getEmail());


        Intent welcomeIntent = new Intent(LoginActivity.this, DiscoverBaseActivity.class);
        welcomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(welcomeIntent);
        finish();
    }
}
