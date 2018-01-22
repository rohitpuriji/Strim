/**
 * @Module Name/Class		:	AppSharedPrefrences
 * @Author Name            :	Rohit Puri
 * @Date :	Sept 16, 2016
 * @Purpose :	This class is used to save and return customer data in/from shared preferences
 */

package com.mystrimz.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;


public class AppSharedPrefrences {

    private static String key_customer_info = "customer";
    private static String key_cart_info = "cart";
    private static String key_checkout_info = "checkout";
    private static String key_customer_token_info = "token";
    private static String key_server_customer_info = "server_customer";
    private static String key_payment_info = "payment";
    private static String key_user_name = "user_name";

    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    private static Gson mGson;
    private static AppSharedPrefrences prefManager;

    public AppSharedPrefrences(Context context) {
        String PREFS_FILE = context.getPackageName();
        mSharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);


    }

    public boolean checkExistKey(String key){

        if(mSharedPreferences.contains(key)){
            return true;
        }else{
            return false;
        }
    }


    public static AppSharedPrefrences getInstance(Context context) {
        if (prefManager == null) {
            prefManager = new AppSharedPrefrences(context);
        }
        return prefManager;
    }


    public void clearCustomerData(Activity aActivity) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(aActivity);
        mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.commit();
    }


    /**
     * Manjeet
     */

    public void setPreference(String key, String value) {
        if (mSharedPreferences != null) {
            mSharedPreferences.edit().putString(key, value).commit();
        }
    }

    public String getPreference(String key, String... defValue) {


        String value = defValue.length > 0 ? defValue[0] : null;
        if (mSharedPreferences != null) {
            return mSharedPreferences.getString(key, value);
        }
        return value;
    }


    public boolean setPreferenceBoolean(Context c, String key, boolean value, String prefrenceName) {
        SharedPreferences settings = c.getSharedPreferences(prefrenceName, 0);
        settings = c.getSharedPreferences(prefrenceName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("value", value);
        return editor.commit();
    }

    public boolean getPreferenceBoolean(Context c, String key, String prefrenceName) {
        SharedPreferences settings = c.getSharedPreferences(prefrenceName, 0);
        settings = c.getSharedPreferences(prefrenceName, 0);
        Boolean value = settings.getBoolean("value", false);
        return value;
    }

}
