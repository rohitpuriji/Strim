package com.mystrimz.android.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mystrimz.android.R;
import com.mystrimz.android.adapter.NewPlayListAdapter;
import com.mystrimz.android.adapter.RecentListAdapter;
import com.mystrimz.android.bean.Model;
import com.mystrimz.android.bean.Tagsbean;
import com.mystrimz.android.bean.TrackList;
import com.mystrimz.android.bean.YoutubeData;
import com.mystrimz.android.http.apicallback.SimpleMessageCallBack;
import com.mystrimz.android.http.apicallback.SongsListCallBack;
import com.mystrimz.android.http.requests.UserRequest;
import com.mystrimz.android.util.AppPreferences;
import com.mystrimz.android.util.AppSharedPrefrences;
import com.mystrimz.android.util.Constants;
import com.mystrimz.android.util.Utility;
import com.mystrimz.android.util.Validation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewPlayListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private StaggeredGridLayoutManager stagaggeredGridLayoutManager;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean isLoading= false;
    private NewPlayListAdapter newPlayListAdapter;
    private ImageView img_next;
    private TextView saveList;
    private EditText playlistName;
    private RelativeLayout bottom_child_view;
    private ImageView addMoreList;
    private ImageView imgBackButton;
    private String mImagePath = "";
    private ArrayList<String> tagsList;
    private ImageView featured_image;
    private final int MY_PERMISSIONS = 25;
    private String IMAGE_NAME;
    private static final int REQ_CAMERA = 1010;
    private static final int REQ_GALLERY = 1009;
    private TextView tags_text;
    private ImageView edit_tags;
    private String totalTags = "";
    private ImageView img_deletePlaylist;
    private List<TrackList> youTubeList;
    private AppSharedPrefrences appSharedPrefrences;
    private ProgressDialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_play_list);
        init();
        onClickListener();
        hideAndShowBottomView();

        EventBus.getDefault().register(this);

        if (!mImagePath.equals("")) {
            //pbImage.setVisibility(View.VISIBLE);
            setImageInImageView(mImagePath);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void setImageInImageView(String value) {

        Glide.with(this).load(value).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                // pbImage.setVisibility(View.GONE);
                mImagePath = "";
                Toast.makeText(NewPlayListActivity.this, "Failed to get photo from server..Please try again", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                //pbImage.setVisibility(View.GONE);
                mImagePath = Constants.KEY_IMAGE_PATH_FROM_FACEBOOK;
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().error(R.drawable.gallery).into(featured_image);
    }

    private void hideAndShowBottomView() {

        if (youTubeList.size() > 0){
            img_next.setVisibility(View.GONE);
            bottom_child_view.setVisibility(View.VISIBLE);
            saveList.setVisibility(View.VISIBLE);
            addMoreList.setVisibility(View.VISIBLE);
        } else {
            img_next.setVisibility(View.VISIBLE);
            bottom_child_view.setVisibility(View.GONE);
            addMoreList.setVisibility(View.GONE);
            saveList.setVisibility(View.GONE);
        }
    }

    private void onClickListener() {
        img_next.setOnClickListener(this);
        saveList.setOnClickListener(this);
        imgBackButton.setOnClickListener(this);
        featured_image.setOnClickListener(this);
        edit_tags.setOnClickListener(this);
        addMoreList.setOnClickListener(this);
        img_deletePlaylist.setOnClickListener(this);
    }


    private void init() {
        myDialog = Utility.showProgressDialog(this);
        appSharedPrefrences = AppSharedPrefrences.getInstance(this);
        img_next = (ImageView)findViewById(R.id.next);
        saveList = (TextView)findViewById(R.id.save_list);
        playlistName = (EditText)findViewById(R.id.playList_name);
        youTubeList = new ArrayList<>();
        tagsList = new ArrayList<>();
        bottom_child_view = (RelativeLayout)findViewById(R.id.bottom_view_child);
        addMoreList = (ImageView)findViewById(R.id.add_moreitem);
        imgBackButton = (ImageView)findViewById(R.id.imgBackArrow);
        featured_image = (ImageView)findViewById(R.id.feature_image);
        tags_text = (TextView)findViewById(R.id.tags_text);
        edit_tags = (ImageView)findViewById(R.id.edit_tags);
        img_deletePlaylist = (ImageView)findViewById(R.id.remove_playlist);
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        stagaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        stagaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(stagaggeredGridLayoutManager);

    }

    private void setAdapter() {

        if (newPlayListAdapter == null){
            newPlayListAdapter = new NewPlayListAdapter(this, youTubeList);
            recyclerView.setAdapter(newPlayListAdapter);
        } else {
            newPlayListAdapter.swapeItem(youTubeList);
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.public_list:
                if (checked){
                    Log.e("checked list", "public");
                }

                break;
            case R.id.friends_only:
                if (checked){
                    Log.e("checked list", "friens");
                }
                    // Ninjas rule
                break;
            case R.id.private_list:{
                if (checked){
                    Log.e("checked list", "private");
                }
                break;
            }
        }
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.next:
                if (Utility.getInstance().isOnline(this)){
                    clearList();
                    callAddnewListActivity();

                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edit_tags:
                callTagScreen();

                break;
            case R.id.imgBackArrow:
                finish();
                break;
            case R.id.feature_image:
                requestPermission();
                break;
            case R.id.add_moreitem:
                if (Utility.getInstance().isOnline(this)){
                    //clearList();
                    callAddnewListActivity();
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.remove_playlist:
                if (Utility.getInstance().isOnline(this)){

                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.save_list:
                if (Utility.getInstance().isOnline(this)){
                        callSaveMyPlaylist();
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void callSaveMyPlaylist() {

        String albumTitle = playlistName.getText().toString().trim();
        String accessToken = appSharedPrefrences.getPreference(Constants.KEY_ACCESS_TOKEN, "");

        Model model = new Model();
        model.setAccessToken(accessToken);
        model.setAlbumTitle(albumTitle);
        model.setTracks(youTubeList);

        myDialog.show();
        UserRequest userRequest = UserRequest.getInstance();
        userRequest.callAddPlaylist(model, new SimpleMessageCallBack() {


            @Override
            public void onSuccess(String message) {
                myDialog.dismiss();
                finish();
                Toast.makeText(NewPlayListActivity.this, message, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(String error) {
                myDialog.dismiss();
                Toast.makeText(NewPlayListActivity.this, error, Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void requestPermission() {

        if (ContextCompat.checkSelfPermission(NewPlayListActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(NewPlayListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewPlayListActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS);
        } else
            imageSelector();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case MY_PERMISSIONS:

                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();

                } else
                    imageSelector();

                break;

            default:
        }
    }

    private void imageSelector() {

        final Dialog imageSelectorDialog = new Dialog(NewPlayListActivity.this);
        imageSelectorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imageSelectorDialog.setContentView(R.layout.custom_image_selector);
        imageSelectorDialog.show();

        final TextView tvCamera = (TextView) imageSelectorDialog.findViewById(R.id.tvCamera);
        final TextView tvGallery = (TextView) imageSelectorDialog.findViewById(R.id.tvGallery);

        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = AppPreferences.getInstance().getProfilePicFile();
                IMAGE_NAME = f.getName();
                Log.d("image path is", f.getAbsolutePath());
                Uri fileUri = FileProvider.getUriForFile(NewPlayListActivity.this, getApplicationContext().getPackageName() + ".provider", f);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, REQ_CAMERA);
                imageSelectorDialog.dismiss();

            }
        });

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                } else {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                }
                startActivityForResult(intent, REQ_GALLERY);
                imageSelectorDialog.dismiss();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("REQUEST CODE IS ", requestCode + "");

        if (requestCode == REQ_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                mImagePath = AppPreferences.getPath(uri, this);
                if (mImagePath == null) {
                    mImagePath = AppPreferences.getImageUrlWithAuthority(this, uri);
                }
                try {
                    if (mImagePath != null) {
                        //mImagePath = AppPreferences.getInstance().setPic(imgProfilePic, mImagePath);
                        createFileFromImagePath(mImagePath);
                    } else {
                        // show toast
                        Log.e("Selected", "Problem in getting image from gallery");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(this, "Image Not exist in device folder", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Failed to select image from gallery", Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == REQ_CAMERA && resultCode == Activity.RESULT_OK) {

            File directory = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
            for (File temp : directory.listFiles()) {
                if (temp.getName().equals(IMAGE_NAME)) {
                    mImagePath = temp.getAbsolutePath();
                    /*mImagePath = AppPreferences.getInstance().setPic(imgProfilePic, mImagePath);
                    Bitmap bitmap = AppPreferences.getInstance().decodeSampledBitmapFromFile(mImagePath, 350, 350);
                    imgProfilePic.setImageBitmap(null);
                    imgProfilePic.setImageBitmap(bitmap);*/
                    createFileFromImagePath(mImagePath);

                    break;
                }
            }

        }
    }

    private void createFileFromImagePath(String imagePath) {
        File file = new File(imagePath);
        long length = file.length();
        length = length / 1024;
        System.out.println("File Path : " + file.getPath() + ", File size : " + length + " KB");

        if (length > 2000) {
            // mImagePath = AppPreferences.getInstance().setPic(null, mImagePath);
            imagePath = AppPreferences.getInstance().compressImage(imagePath);
            // calling method as recursive
            createFileFromImagePath(imagePath);

        } else {
            mImagePath = imagePath;
            Log.e("imagePath", mImagePath);
            Bitmap bitmap = AppPreferences.getInstance().decodeSampledBitmapFromFile(mImagePath, 350, 350);
            //featured_image.setImageBitmap(null);
            //featured_image.setImageBitmap(bitmap);
        }
    }

    private void callTagScreen() {

        Intent intent = new Intent(NewPlayListActivity.this, TagsActivity.class);
        intent.putStringArrayListExtra("stock_list", tagsList);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
    }

    private void callAddnewListActivity() {

        if (!Validation.getInstance().isEmpty(playlistName.getText().toString().trim())){
            Toast.makeText(this, "Please enter your playlist name", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent intent = new Intent(NewPlayListActivity.this, AddNewPlaylistActivity.class);
            startActivity(intent);
        }
    }

    @Subscribe
    public void onEvent(Tagsbean tagsbean) {
        tagsList.clear();
        totalTags ="";
        tagsList.addAll(tagsbean.getTagsList());
        if (tagsList.size() > 0){
             for (int i = 0; i < tagsList.size(); i++){
                 totalTags = totalTags+tagsList.get(i)+", ";

             }
        }
        tags_text.setText(totalTags);
    }

    @Subscribe
    public void onEvent(List<TrackList> list) {
        tags_text.setText("");
        tagsList.clear();
        youTubeList.clear();
        setAdapter();
        youTubeList.addAll(list);
        hideAndShowBottomView();
        setAdapter();

    }

    private void clearList() {
        if (youTubeList == null) {
            youTubeList = new ArrayList<>();
        }
        youTubeList.clear();
    }
}
