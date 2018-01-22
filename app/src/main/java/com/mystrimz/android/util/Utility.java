package com.mystrimz.android.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mystrimz.android.R;

/**
 * Created by manishjoshi on 4/1/18.
 */

public class Utility {
    private static final Utility ourInstance = new Utility();

    public static Utility getInstance() {
        return ourInstance;
    }

    private Utility() {
    }

    /**
     * check internet availability
     * @param context
     * @return
     */
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static ProgressDialog showProgressDialog(Context context){
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(context.getString(R.string.please_wait));
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        return m_Dialog;
    }
}
