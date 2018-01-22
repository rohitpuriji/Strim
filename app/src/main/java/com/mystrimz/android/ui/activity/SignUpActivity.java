package com.mystrimz.android.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_firstName;
    private EditText et_lastName;
    private EditText et_email;
    private EditText et_password;
    private TextView tv_signUp;
    private Validation validation;
    private TextView termsAndPrivacy;
    private ImageView check_box;
    private boolean isChecked = false;
    private ImageView im_backArror;
    private AppSharedPrefrences appSharedPrefrences;
    private ProgressDialog myDialog;
    private EditText et_conPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        onClickListener();
        makeClickableSpan();

    }

    private void makeClickableSpan() {

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this, "span clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, TermsAndServicesActivity.class);
                startActivity(intent);
            }
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);

            }
        };
        String terms_and_policy = getResources().getString(R.string.terms_and_services);
        SpannableString ss = new SpannableString(terms_and_policy);
        int termStart = terms_and_policy.indexOf("Terms and Services");
        int termStop = termStart + "Terms and Services".length();

        ss.setSpan(clickableSpan, termStart, termStop, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.button_color)),
                termStart, termStop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsAndPrivacy.setText(ss);
        termsAndPrivacy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void onClickListener() {
        et_lastName.setOnClickListener(this);
        et_firstName.setOnClickListener(this);
        et_email.setOnClickListener(this);
        et_password.setOnClickListener(this);
        tv_signUp.setOnClickListener(this);
        et_conPassword.setOnClickListener(this);
        check_box.setOnClickListener(this);
        im_backArror.setOnClickListener(this);
    }

    private void init() {
        et_firstName = (EditText)findViewById(R.id.firstName);
        et_lastName = (EditText)findViewById(R.id.lastName);
        et_email = (EditText)findViewById(R.id.email);
        et_password = (EditText)findViewById(R.id.password);
        tv_signUp = (TextView)findViewById(R.id.signUp);
        validation = Validation.getInstance();
        termsAndPrivacy = (TextView)findViewById(R.id.terms_privacy);
        check_box = (ImageView)findViewById(R.id.checkBox);
        im_backArror = (ImageView)findViewById(R.id.imgBackArrow);
        et_conPassword = (EditText)findViewById(R.id.con_password);
        appSharedPrefrences = AppSharedPrefrences.getInstance(this);

        myDialog = Utility.showProgressDialog(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.signUp:
                if (Utility.getInstance().isOnline(this)) {

                    checkValidation();
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.checkBox:
                isCheckBoxChecked();
                break;
            case R.id.imgBackArrow:
                finish();
                break;
        }
    }

    private void isCheckBoxChecked() {
        if (!isChecked){
            isChecked = true;
            check_box.setImageResource(R.drawable.tick);
        } else{
            isChecked = false;
            check_box.setImageResource(R.drawable.box);
        }
    }

    private void checkValidation() {

        if (!validation.isEmpty(et_firstName.getText().toString().trim())){
            et_firstName.setError(getString(R.string.error_firstName));
            return;

        }else if (!validation.isEmpty(et_lastName.getText().toString().trim())){
            et_lastName.setError(getString(R.string.error_lastName));
            return;

        } else if (!validation.isEmpty(et_email.getText().toString().trim())){
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

        } else if(!validation.isEmpty(et_conPassword.getText().toString().trim())){
            et_conPassword.setError(getString(R.string.confirm_password_empty));
            return;

        } else if(!validation.isValidPassword(et_conPassword.getText().toString().trim())){
            et_conPassword.setError(getString(R.string.error_password));
            return;

        }else if(!et_conPassword.getText().toString().trim().equals(et_password.getText().toString().trim())){
            et_conPassword.setError("Confirm password do not match");
            return;

        }else if (!isChecked){
            Toast.makeText(this, "Make sure you are agree with terms and services", Toast.LENGTH_SHORT).show();
            return;

        } else{

            callSignUpApi();
        }
    }

    private void callSignUpApi() {
        String firstName = et_firstName.getText().toString().trim();
        String lastName = et_lastName.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString();
        String devicetype = Constants.DEVICE_TYPE_ANDROID;
        String devicetoken = FirebaseInstanceId.getInstance().getToken();
        String registration_type = Constants.KEY_REG_TYPE_SITE;

        myDialog.show();

        Log.e("details", devicetoken);
        UserRequest userRequest = UserRequest.getInstance();
        userRequest.callRegisterApi(firstName, lastName, email, password, devicetype, devicetoken, registration_type, new UserCallBack() {

            @Override
            public void onSuccess(RegisterBean baseBean) {
             myDialog.dismiss();
                saveUserDetails(baseBean);
            }
            @Override
            public void onError(String error) {
               myDialog.dismiss();
                Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void saveUserDetails(RegisterBean baseBean) {

        Toast.makeText(SignUpActivity.this,baseBean.getmessage(), Toast.LENGTH_LONG).show();
        appSharedPrefrences = AppSharedPrefrences.getInstance(SignUpActivity.this);
        et_firstName.setText("");
        et_lastName.setText("");
        et_email.setText("");
        et_password.setText("");
        if (!isChecked){
            isChecked = true;
            check_box.setImageResource(R.drawable.tick);
        } else{
            isChecked = false;
            check_box.setImageResource(R.drawable.box);
        }
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}
