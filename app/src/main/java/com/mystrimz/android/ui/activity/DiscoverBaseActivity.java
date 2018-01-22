package com.mystrimz.android.ui.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mystrimz.android.R;
import com.mystrimz.android.fragments.DiscoverMainFragment;
import com.mystrimz.android.fragments.MyStrimzFragment;
import com.mystrimz.android.util.AppSharedPrefrences;
import com.mystrimz.android.util.Constants;
import com.mystrimz.android.util.Utility;

public class DiscoverBaseActivity extends AppCompatActivity
        implements  View.OnClickListener {

    private TextView textView;
    private Toolbar toolbar;
    private ImageView im_burger;
    private DrawerLayout drawer;
    private RelativeLayout rl_border_left_top, rl_border_right_bottom, rl_border_right_top;
    private RelativeLayout rl_rightTab, rl_leftTab;
    public FrameLayout frameLayout;
    private DiscoverMainFragment discoverMainFragment;
    private MyStrimzFragment myStrimzFragment;
    private FragmentManager fm;
    private ImageView im_newPlayList;

    private ImageView im_everyOnefollow, im_approveMenualy, im_lovedPlaylist, im_followMe;
    private Boolean iseveryOnefollow = true, isapproveMenualy=false, islovedPlaylist=false, isfollowMe=true;

    private ImageView profile_image;
    private TextView userName;
    private TextView updateInfo;
    private TextView logOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerNavigaltion();
        init();
        onClickListener();


    }

    private void onClickListener() {
        im_burger.setOnClickListener(this);
        rl_rightTab.setOnClickListener(this);
        rl_leftTab.setOnClickListener(this);
        im_newPlayList.setOnClickListener(this);

        im_everyOnefollow.setOnClickListener(this);
        im_approveMenualy.setOnClickListener(this);
        im_lovedPlaylist.setOnClickListener(this);
        im_followMe.setOnClickListener(this);

        updateInfo.setOnClickListener(this);
        profile_image.setOnClickListener(this);
        logOut.setOnClickListener(this);
    }

    private void drawerNavigaltion() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();


    }

    private void init() {
        im_burger  = toolbar.findViewById(R.id.burger);
        im_newPlayList = toolbar.findViewById(R.id.new_playlist);
        rl_border_left_top = (RelativeLayout)findViewById(R.id.border_left_top);
        rl_border_right_top = (RelativeLayout)findViewById(R.id.border_right_top);
        rl_border_right_bottom = (RelativeLayout)findViewById(R.id.border_right_bottom);

        im_everyOnefollow = (ImageView)findViewById(R.id.every_one_can_follow);
        im_approveMenualy = (ImageView)findViewById(R.id.approve_menualy);
        im_lovedPlaylist = (ImageView)findViewById(R.id.love_my_playlist);
        im_followMe = (ImageView)findViewById(R.id.followMe);
        profile_image = (ImageView)findViewById(R.id.profile_image);
        userName = (TextView)findViewById(R.id.user_name);
        updateInfo = (TextView)findViewById(R.id.update_info);

        rl_leftTab = (RelativeLayout)findViewById(R.id.leftTab);
        rl_rightTab = (RelativeLayout)findViewById(R.id.rightTab);
        logOut = (TextView) findViewById(R.id.logOut);
        frameLayout= (FrameLayout)findViewById(R.id.frame_layout);
        discoverMainFragment = new DiscoverMainFragment();
        myStrimzFragment = new MyStrimzFragment();
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame_layout, discoverMainFragment, "fragmentone").commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.discover, menu);
//
//        return true;
//    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.burger:
                drawer.openDrawer(Gravity.START);
                break;
            case R.id.rightTab:
                if(Utility.getInstance().isOnline(this)) {
                    callRightTabFragment();
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.leftTab:
                if (Utility.getInstance().isOnline(this)) {
                    callLeftTabFragment();
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.new_playlist:
                if (Utility.getInstance().isOnline(this)) {
                    callNewPlaylistactivity();
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.every_one_can_follow:
                checkSettings("everyOneCanfollow");
                break;

            case R.id.approve_menualy:
                checkSettings("approvedMenually");
                break;

            case R.id.love_my_playlist:
                checkSettings("loveMyPlaylist");
                break;

            case R.id.followMe:
                checkSettings("followMe");
                break;
            case R.id.logOut:
                callLogOut();
                break;
        }
    }

    private void callLogOut() {

        AppSharedPrefrences appSharedPrefrences = AppSharedPrefrences.getInstance(this);
        appSharedPrefrences.setPreference(Constants.KEY_ACCESS_TOKEN, "");
        Intent intent = new Intent(DiscoverBaseActivity.this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void checkSettings(String text) {

        if (text.equals("everyOneCanfollow")){
            if (iseveryOnefollow){
                iseveryOnefollow = false;
                im_everyOnefollow.setImageResource(R.drawable.off);
            } else {
                iseveryOnefollow = true;
                im_everyOnefollow.setImageResource(R.drawable.on);
            }
        } else if (text.equals("approvedMenually")){
            if (isapproveMenualy){
                isapproveMenualy = false;
                im_approveMenualy.setImageResource(R.drawable.off);
            } else {
                isapproveMenualy = true;
                im_approveMenualy.setImageResource(R.drawable.on);
            }
        } else if (text.equals("loveMyPlaylist")){
            if (islovedPlaylist){
                islovedPlaylist = false;
                im_lovedPlaylist.setImageResource(R.drawable.toggle);
            } else {
                islovedPlaylist = true;
                im_lovedPlaylist.setImageResource(R.drawable.toggle_on);
            }
        } else if (text.equals("followMe")){
            if (isfollowMe){
                isfollowMe = false;
                im_followMe.setImageResource(R.drawable.toggle);
            } else {
                isfollowMe = true;
                im_followMe.setImageResource(R.drawable.toggle_on);
            }
        } else {
            Toast.makeText(this, "Something happens wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void callNewPlaylistactivity() {
        Intent intent = new Intent(DiscoverBaseActivity.this, NewPlayListActivity.class);
        startActivity(intent);
    }

    private void callLeftTabFragment() {

        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame_layout, discoverMainFragment, "fragmentone").commit();
        rl_border_right_bottom.setBackgroundColor(getResources().getColor(R.color.color_orange));
        rl_border_right_top.setBackgroundColor(getResources().getColor(R.color.color_toolbar));
        rl_border_left_top.setBackgroundColor(getResources().getColor(R.color.color_orange));

//        Intent intent = new Intent(DiscoverBaseActivity.this, StrimzDiscoverActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP );
//        startActivity(intent);


    }

    private void callRightTabFragment() {

        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame_layout, myStrimzFragment, "fragmentone").commit();
        rl_border_right_bottom.setBackgroundColor(getResources().getColor(R.color.color_pink));
        rl_border_right_top.setBackgroundColor(getResources().getColor(R.color.color_pink));
        rl_border_left_top.setBackgroundColor(getResources().getColor(R.color.color_toolbar));

//        Intent intent = new Intent(DiscoverBaseActivity.this, MyStrimzActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP );
//        startActivity(intent);

    }

}
