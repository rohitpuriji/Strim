package com.mystrimz.android.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.mystrimz.android.R;
import com.mystrimz.android.util.AppSharedPrefrences;
import com.mystrimz.android.util.Constants;

public class SplashActivty extends AppCompatActivity {

    private int set_activity_time = 1000 * 3;
    private Handler handler;
    private AppSharedPrefrences appSharedPrefrences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_activty);

        init();
        nextActivity();
    }

    private void nextActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (appSharedPrefrences.getPreference(Constants.KEY_ACCESS_TOKEN, "").equals("")) {
                    callActivity(WelcomeActivity.class);

                }else {
                    callActivity(DiscoverBaseActivity.class);
                }

            }
        }, set_activity_time);
    }

    /**
     * call activity
     * @param activity
     */
    private void callActivity(Class activity) {
        Intent intent = new Intent(SplashActivty.this, activity);
        startActivity(intent);
        finish();
    }

    private void init() {
        handler = new Handler();
        appSharedPrefrences = AppSharedPrefrences.getInstance(this);
    }
}
