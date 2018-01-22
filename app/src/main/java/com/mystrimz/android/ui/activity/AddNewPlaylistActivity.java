package com.mystrimz.android.ui.activity;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.mystrimz.android.R;
import com.mystrimz.android.adapter.AddNewPlaylistAdapter;
import com.mystrimz.android.bean.TrackList;
import com.mystrimz.android.bean.YoutubeData;
import com.mystrimz.android.http.apicallback.SongsListCallBack;
import com.mystrimz.android.http.listeners.MethordCallBack;
import com.mystrimz.android.http.requests.UserRequest;
import com.mystrimz.android.util.Constants;
import com.mystrimz.android.util.Utility;
import com.mystrimz.android.util.Validation;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddNewPlaylistActivity extends AppCompatActivity implements View.OnClickListener, MethordCallBack, YouTubePlayer.OnInitializedListener {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private StaggeredGridLayoutManager stagaggeredGridLayoutManager;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean isLoading= false;
    private AddNewPlaylistAdapter addNewPlaylistAdapter;
    private ArrayList<String> listItems = new ArrayList<>();

    private TextView tv_myStrimz, tv_fromtheweb;
    private RadioGroup radioGroup;
    private LinearLayout ln_checkboxGroup;
    private ImageView imgBackButton;
    private TextView tv_done;
    private ImageView youtube, soundClould, playlist;
    private Boolean youtube_selected=true, soundClould_selected = false, playlist_selected=false;
    private View small_player_view;
    private ImageView cancel;
    private ProgressDialog myDialog;
    private String playlistValue = "0";
    private EditText et_search;
    private ImageView search_button;
    private List<TrackList> youTubeList;
    private Boolean isWebselected = false;
    private String paginationyou = "0";
    private String paginationclound = "0";
    private List<TrackList> myPlaylist;
    private FrameLayout frameLayoutYoutybe;
    private YouTubePlayerFragment mYoutubePlayerFragment;
    private String videoId="";
    private TextView playSongTitle;
    private ImageView forwordPlay, reversPlay, playPause;
    private YouTubePlayer youTubePlayer;
    private Boolean isVideoPlay = true;
    private int position =0;
    private int flag1 = 1;
    private int flag2 = 1;
    private int set_activity_time = 1000 * 3;
    private Handler handler;
    private MediaPlayer mediaPlayer;
    private String audioId ="106702850";
    private String url_clould ="https://api.soundcloud.com/tracks/"+ audioId +"/stream?client_id=894ac68b7d91ae58786971a9d68aadf4";
    private ImageView sound_cloudImage;
    private Boolean isBothSeleceted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_playlist);

        init();
        onClickListener();

    }


    private void onClickListener() {
        tv_myStrimz.setOnClickListener(this);
        tv_fromtheweb.setOnClickListener(this);
        tv_done.setOnClickListener(this);
        imgBackButton.setOnClickListener(this);
        cancel.setOnClickListener(this);
        youtube.setOnClickListener(this);
        soundClould.setOnClickListener(this);
        playlist.setOnClickListener(this);
        search_button.setOnClickListener(this);

        forwordPlay.setOnClickListener(this);
        reversPlay.setOnClickListener(this);
        playPause.setOnClickListener(this);
    }

    private void init() {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        myDialog = Utility.showProgressDialog(this);
        tv_fromtheweb = (TextView)findViewById(R.id.from_theWeb);
        tv_myStrimz = (TextView)findViewById(R.id.my_strimz_list);
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        ln_checkboxGroup = (LinearLayout)findViewById(R.id.checkBox_grop);
        imgBackButton = (ImageView)findViewById(R.id.imgBackArrow);
        tv_done = (TextView)findViewById(R.id.done_list);
        tv_done.setVisibility(View.GONE);
        small_player_view = (View) findViewById(R.id.bottom_player_view);
        frameLayoutYoutybe = (FrameLayout) findViewById(R.id.frameLayout);
        playSongTitle = (TextView)findViewById(R.id.song_title);
        forwordPlay = (ImageView)findViewById(R.id.forwardPlay);
        reversPlay = (ImageView)findViewById(R.id.reversePlay);
        playPause = (ImageView)findViewById(R.id.play_pause);
        cancel = (ImageView)findViewById(R.id.cancel_small_player);
        sound_cloudImage = (ImageView)findViewById(R.id.sound_clouldImage);
        youtube = (ImageView)findViewById(R.id.youtube);
        soundClould = (ImageView)findViewById(R.id.sound_clould);
        playlist = (ImageView)findViewById(R.id.playlist);
        myPlaylist = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        stagaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        stagaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(stagaggeredGridLayoutManager);

        tv_myStrimz.setBackgroundResource(R.drawable.type_list_shape);
        tv_myStrimz.setTextColor(getResources().getColor(R.color.color_white));
        ln_checkboxGroup.setVisibility(View.GONE);

        et_search = (EditText)findViewById(R.id.search_song);
        search_button = (ImageView)findViewById(R.id.search);

        youTubeList = new ArrayList<>();
        handler = new Handler();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                forwordPlay.setEnabled(true);
                reversPlay.setEnabled(true);

                stagaggeredGridLayoutManager.invalidateSpanAssignments();
                if (dy > 0) // check for the scroll down
                {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = stagaggeredGridLayoutManager.getItemCount();
                    int[] firstVisibleItems = null;
                    firstVisibleItems = stagaggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                    if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                        pastVisibleItems = firstVisibleItems[0];
                    }

                    if (!isLoading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLoading = true;
                         checkImptyValidation();


                        }

                    }

                }

            }
        });
        
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (charSequence.length() > 3){
                            clearList();
                            paginationyou ="";
                            paginationclound = "";
                            checkImptyValidation();
                        }

                    }
                }, set_activity_time);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setAdapter() {
        isLoading=false;

        if (addNewPlaylistAdapter == null){
            addNewPlaylistAdapter = new AddNewPlaylistAdapter(this, youTubeList);
            recyclerView.setAdapter(addNewPlaylistAdapter);
                
        } else {
            addNewPlaylistAdapter.swapeItem(youTubeList);
        }

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.my_strimz_list:

                if (Utility.getInstance().isOnline(this)){
                    mystrimzTabSelected();
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.from_theWeb:
                if (Utility.getInstance().isOnline(this)){
                    webTabSelected();
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.done_list:

                if (Utility.getInstance().isOnline(this)){
                    SavePlaylist();
                } else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.imgBackArrow:
                finish();
                break;

            case R.id.cancel_small_player:
                closePlayer();
                break;

            case R.id.youtube:
                setCheckBox("youtube");
                break;

            case R.id.sound_clould:
                setCheckBox("soundClould");
                break;

            case R.id.playlist:
                setCheckBox("playerlist");
                break;
            case R.id.search:
                if (Utility.getInstance().isOnline(this)){
                    clearList();
                    paginationyou ="";
                    paginationclound = "";
                    if (et_search.getText().toString().trim().length() > 3) {
                        checkImptyValidation();
                    } else {
                        Toast.makeText(this, "please enter valid name", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.play_pause:
                callPlayAndPouseVideo();
                break;
            case R.id.forwardPlay:
                callForwordVideo();
                break;
            case R.id.reversePlay:
                callReversVideo();
                break;

        }

    }

    private void callReversVideo() {
        forwordPlay.setEnabled(true);
        if (youTubeList.size() > 0){
            if (position > 0){
                position = position - 1;
                //hide revers button if you have to play first song
                if (position == 0){
                    reversPlay.setEnabled(false);
                } else {
                    reversPlay.setEnabled(true);
                }
                //get host id pay previous song
                for (int i = 0; i < youTubeList.size(); i++){
                    if (position == i){
                        if (youTubeList.get(i).getHoster().equals("soundcloud")) {
                            audioId = youTubeList.get(i).getHosterTrackId();
                            playAudio();
                        } else {
                            videoId = youTubeList.get(i).getHosterTrackId();
                            playVideo();
                        }
                    }
                }
            } else {
                reversPlay.setEnabled(false);
            }
            playSongTitle.setText(youTubeList.get(position).getTitle());

        } else {
            Toast.makeText(this, "Something happens wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void callForwordVideo() {
        reversPlay.setEnabled(true);
        if (youTubeList.size() > 0){

            if (position < youTubeList.size()){
                position = position + 1;

                if (position == youTubeList.size()){
                    forwordPlay.setEnabled(false);
                } else {
                    forwordPlay.setEnabled(true);
                }

                //get host id pay previous song
                for (int i = 0; i < youTubeList.size(); i++){
                    if (position == i){
                        if (youTubeList.get(i).getHoster().equals("soundcloud")) {
                            audioId = youTubeList.get(i).getHosterTrackId();
                            playAudio();
                        } else {
                            videoId = youTubeList.get(i).getHosterTrackId();
                            playVideo();
                        }
                    }
                }
            } else {
                forwordPlay.setEnabled(false);
            }
            playSongTitle.setText(youTubeList.get(position).getTitle());


        }else {
            Toast.makeText(this, "Something happens wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void playVideo() {
        if (sound_cloudImage.getVisibility() == View.VISIBLE){
            sound_cloudImage.setVisibility(View.GONE);
        }
        if(frameLayoutYoutybe.getVisibility() == View.GONE){
            youTubePlayer.pause();
            frameLayoutYoutybe.setVisibility(View.VISIBLE);
        }
        youTubePlayer.loadVideo(videoId);
    }

    private void playAudio() {
        if(frameLayoutYoutybe.getVisibility() == View.VISIBLE){
            youTubePlayer.pause();
            frameLayoutYoutybe.setVisibility(View.GONE);
        }
        if (sound_cloudImage.getVisibility() == View.GONE){
            sound_cloudImage.setVisibility(View.VISIBLE);
        }

        try {
            mediaPlayer.setDataSource(url_clould);
            mediaPlayer.prepare();

        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

    }

    private void callPlayAndPouseVideo() {
        if (isVideoPlay) {
            isVideoPlay = false;
            playPause.setImageResource(R.drawable.play_plyr);
            if (youTubePlayer.isPlaying()) {
                youTubePlayer.pause();
            } else {
                mediaPlayer.pause();
            }
        } else {
            isVideoPlay = true;
            playPause.setImageResource(R.drawable.pause);

            if (!youTubePlayer.isPlaying()) {
                youTubePlayer.play();
            } else {
                mediaPlayer.start();
            }
        }
    }


    private void checkImptyValidation() {
        if (!Validation.getInstance().isEmpty(et_search.getText().toString().trim())){
            Toast.makeText(this, R.string.enter_song_title, Toast.LENGTH_SHORT).show();
            return;
        }else {
            if (isWebselected && youtube_selected) {
                getyoutubePlaylist(playlistValue);
            } else if (isWebselected && soundClould_selected){
                getSoundCloud(playlistValue);
            } else {
                Toast.makeText(this, "Please select atleast one youtube or soundClould", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getSoundCloud(String isPlaylist) {
        String title = et_search.getText().toString();

        myDialog.show();
        UserRequest userRequest = UserRequest.getInstance();
        userRequest.callSoundClouldsearch(title, paginationclound, isPlaylist, new SongsListCallBack() {


            @Override
            public void onSuccess(YoutubeData youtubeData) {
                myDialog.dismiss();
                youTubeList.addAll(youtubeData.getTracks());
                if (youtubeData.getPlaylists().size() > 0){
                    youTubeList.addAll(youtubeData.getPlaylists());
                }
                paginationclound= youtubeData.getPagination_key();
                setAdapter();
            }

            @Override
            public void onError(String error) {
                myDialog.dismiss();
                Toast.makeText(AddNewPlaylistActivity.this, error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getyoutubePlaylist(String isplaylist) {


        String title = et_search.getText().toString();
        if (!soundClould_selected) {
            myDialog.show();
        }
        UserRequest userRequest = UserRequest.getInstance();
        userRequest.callYoutubesearch(title, paginationyou, isplaylist, new SongsListCallBack() {


            @Override
            public void onSuccess(YoutubeData youtubeData) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                paginationyou = youtubeData.getPagination_key();
                youTubeList.addAll(youtubeData.getTracks());
                if (youtubeData.getPlaylists().size() > 0){
                    youTubeList.addAll(youtubeData.getPlaylists());
                }
                if (soundClould_selected){
                    getSoundCloud(playlistValue);
                } else {
                    setAdapter();
                }
            }

            @Override
            public void onError(String error) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                Toast.makeText(AddNewPlaylistActivity.this, error, Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * enable and disable list
     * @param
     */
    private void setCheckBox(String text) {

        if (text.equals("youtube")){
            if (youtube_selected){
                youtube_selected= false;
                youtube.setImageResource(R.drawable.uncheck);
            } else {
                youtube_selected=true;
                youtube.setImageResource(R.drawable.check);
            }
            clearList();
            paginationyou ="";
            paginationclound = "";
            checkImptyValidation();
        } else if (text.equals("soundClould")){
            if (soundClould_selected){
                soundClould_selected = false;
                soundClould.setImageResource(R.drawable.uncheck);
            } else {
                soundClould_selected = true;
                soundClould.setImageResource(R.drawable.check);
            }
            clearList();
            paginationyou ="";
            paginationclound = "";
            checkImptyValidation();
        } else if (text.equals("playerlist")){

            if (playlist_selected){
                playlist_selected = false;
                playlistValue = "0";
                playlist.setImageResource(R.drawable.uncheck);
            } else {
                playlist_selected = true;
                playlistValue = "1";
                playlist.setImageResource(R.drawable.check);
            }
            clearList();
            paginationyou ="";
            paginationclound = "";
            checkImptyValidation();
        } else {
            Toast.makeText(this, "Something happens wrong", Toast.LENGTH_SHORT).show();

        }

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.youtube:
                if (checked){

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

    private void closePlayer() {

        if (small_player_view.getVisibility() == View.VISIBLE) {
            if (youTubePlayer.isPlaying()){
                youTubePlayer.pause();
            }else {
                mediaPlayer.pause();
            }
            small_player_view.setVisibility(View.GONE);
        }
    }


    private void webTabSelected() {
        isWebselected = true;
        tv_fromtheweb.setBackgroundResource(R.drawable.type_list_shape);
        tv_fromtheweb.setTextColor(getResources().getColor(R.color.color_white));
        tv_myStrimz.setBackgroundResource(0);
        tv_myStrimz.setTextColor(getResources().getColor(R.color.color_orange));
        ln_checkboxGroup.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.GONE);

    }

    private void mystrimzTabSelected() {
        isWebselected = false;
        tv_myStrimz.setBackgroundResource(R.drawable.type_list_shape);
        tv_myStrimz.setTextColor(getResources().getColor(R.color.color_white));
        tv_fromtheweb.setBackgroundResource(0);
        tv_fromtheweb.setTextColor(getResources().getColor(R.color.color_orange));
        ln_checkboxGroup.setVisibility(View.GONE);
        radioGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void saveList(int position, Boolean selected) {
        Log.e("clicked", "list cliked");

        for (int i = 0; i < youTubeList.size(); i++) {
            if (i == position) {
                if (selected) {
                    myPlaylist.add(youTubeList.get(i));
                } else {
                    myPlaylist.remove(youTubeList.get(i));
                }
            }
        }

        if (myPlaylist.size() > 0){
            tv_done.setVisibility(View.VISIBLE);
        } else {
            tv_done.setVisibility(View.GONE);
        }

    }

    private void SavePlaylist() {
        EventBus.getDefault().post(myPlaylist);
        myPlaylist.clear();
        finish();
    }
    @Override
    public void openPlayer() {

    }


    private void clearList() {
        if (youTubeList == null) {
            youTubeList = new ArrayList<>();
        }
        if (youTubeList.size() > 0) {
            closePlayer();
        }
        youTubeList.clear();

    }

    @Override
    public void playYoutube(String hosterId, String title, int position, String hoster) {

        if (position == 0){
            reversPlay.setEnabled(false);
        } else if (position == youTubeList.size()){
            forwordPlay.setEnabled(false);
        }
        this.position = position;

        if (small_player_view.getVisibility() == View.GONE) {
            small_player_view.setVisibility(View.VISIBLE);
        }
        if (hoster.equals("soundcloud")) {
            audioId = hosterId;
            playSongTitle.setText(title);
            if(frameLayoutYoutybe.getVisibility() == View.VISIBLE){
                youTubePlayer.pause();
                frameLayoutYoutybe.setVisibility(View.GONE);
            }
            if (sound_cloudImage.getVisibility() == View.GONE){
                sound_cloudImage.setVisibility(View.VISIBLE);
            }
            try {
                mediaPlayer.setDataSource(url_clould);
                mediaPlayer.prepare();

            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        } else {
            if (sound_cloudImage.getVisibility() == View.VISIBLE){
                mediaPlayer.stop();
                sound_cloudImage.setVisibility(View.GONE);
            }
            if(frameLayoutYoutybe.getVisibility() == View.GONE){
                frameLayoutYoutybe.setVisibility(View.VISIBLE);
            }

            videoId = hosterId;
            playSongTitle.setText(title);
            mYoutubePlayerFragment = new YouTubePlayerFragment();
            mYoutubePlayerFragment.initialize(Constants.API_KEY, AddNewPlaylistActivity.this);
            FragmentManager fragmentManager = getFragmentManager();
            android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, mYoutubePlayerFragment);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.youTubePlayer = youTubePlayer;
        System.out.println("mVideoId onInitistizeSucess: " + videoId);
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        youTubePlayer.getFullscreenControlFlags();
        youTubePlayer.setShowFullscreenButton(false);
        if (videoId!=null)
            //youTubePlayer.cueVideo(videoId);
            youTubePlayer.loadVideo(videoId);

        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
