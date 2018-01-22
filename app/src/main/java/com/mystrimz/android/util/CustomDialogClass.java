package com.mystrimz.android.util;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mystrimz.android.R;
import com.mystrimz.android.bean.Datum;
import com.mystrimz.android.fragments.MyStrimzFragment;
import com.mystrimz.android.http.apicallback.MyPlaylistCallBack;
import com.mystrimz.android.http.requests.UserRequest;
import com.mystrimz.android.ui.activity.AddNewPlaylistActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {
    AppSharedPrefrences appSharedPrefrences;
    public Context c;
    public Dialog d;
    public int position;
    public TextView sharePlaylist, reportContent, editPlaylist, deletePlaylist;

    public CustomDialogClass(Context a, int position) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.position = position;
        appSharedPrefrences = AppSharedPrefrences.getInstance(c);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.report_view);

        sharePlaylist = (TextView) findViewById(R.id.sharePlaylist);
        reportContent = (TextView) findViewById(R.id.reportContent);
        editPlaylist = (TextView) findViewById(R.id.editPlaylist);
        deletePlaylist = (TextView) findViewById(R.id.deletePlaylist);

        sharePlaylist.setOnClickListener(this);
        reportContent.setOnClickListener(this);
        editPlaylist.setOnClickListener(this);
        deletePlaylist.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sharePlaylist:
                callSharePlaylist();
                break;
            case R.id.reportContent:
                callReportContent();
                break;
            case R.id.editPlaylist:
                callEditPlayList();
                break;
            case R.id.deletePlaylist:
                calldeletePlaylist();
            default:
                break;
        }
        dismiss();
    }

    public void calldeletePlaylist() {

    }

    public void callReportContent() {

    }

    public void callSharePlaylist() {
        Toast.makeText(c, "manni", Toast.LENGTH_SHORT).show();
    }

    public void callEditPlayList() {
    }
}