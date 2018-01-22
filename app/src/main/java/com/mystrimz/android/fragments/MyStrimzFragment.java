package com.mystrimz.android.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mystrimz.android.R;
import com.mystrimz.android.adapter.RecentListAdapter;
import com.mystrimz.android.bean.Datum;
import com.mystrimz.android.http.apicallback.MyPlaylistCallBack;
import com.mystrimz.android.http.listeners.ReportContentCallBack;
import com.mystrimz.android.http.requests.MyStrimzRequest;
import com.mystrimz.android.util.AppSharedPrefrences;
import com.mystrimz.android.util.Constants;
import com.mystrimz.android.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MyStrimzFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyStrimzFragment extends Fragment implements View.OnClickListener, ReportContentCallBack {


    private View view;
    private RelativeLayout rt_recentlyPlay;
    private RelativeLayout rt_myPlayList;
    private RelativeLayout rt_playListILiked;
    private TextView tv_recentlyPlay;
    private TextView tv_myPlaylist;
    private TextView tv_playlistILiked;

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private StaggeredGridLayoutManager stagaggeredGridLayoutManager;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean isLoading= false;
    private RecentListAdapter recentListAdapter;
    private ArrayList<String> listItems = new ArrayList<>();
    private AppSharedPrefrences appSharedPrefrences;
    private ProgressDialog myDialog;
    private List<Datum> songsList;
    private String whichListActive ="";
    public MyStrimzFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyStrimzFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyStrimzFragment newInstance(String param1, String param2) {
        MyStrimzFragment fragment = new MyStrimzFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_strimz, container, false);
        init();
        onClickListener();
        whichListActive = "recentlyPlayed";
        callRecentlyPlayList("recentlyPlayed");
        return view;

    }

    private void onClickListener() {
        rt_playListILiked.setOnClickListener(this);
        rt_myPlayList.setOnClickListener(this);
        rt_recentlyPlay.setOnClickListener(this);
    }

    private void init() {
        appSharedPrefrences = AppSharedPrefrences.getInstance(getContext());
        myDialog = Utility.showProgressDialog(getActivity());
        rt_recentlyPlay = (RelativeLayout)view.findViewById(R.id.recently_play);
        rt_myPlayList = (RelativeLayout)view.findViewById(R.id.my_playList);
        rt_playListILiked = (RelativeLayout)view.findViewById(R.id.playList_ILiked);
        songsList = new ArrayList<>();
        tv_myPlaylist = (TextView)view.findViewById(R.id.tv_myPlaylist);
        tv_playlistILiked = (TextView)view.findViewById(R.id.tv_playlist_iLiked);
        tv_recentlyPlay = (TextView)view.findViewById(R.id.tv_recentlyPlay);

        rt_recentlyPlay.setBackgroundResource(R.drawable.sub_tab_shape);


        recyclerView = (RecyclerView)view.findViewById(R.id.recycleview);
        stagaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        stagaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(stagaggeredGridLayoutManager);
        recentListAdapter=null;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


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

                            //callMyPlayList();


                        }

                    }

                }

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.recently_play:
                if (Utility.getInstance().isOnline(getContext())){
                    clearList();
                    callRecentlyPlayList("recentlyPlayed");
                }else {
                    Toast.makeText(getContext(), R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.my_playList:
                if (Utility.getInstance().isOnline(getContext())){
                    clearList();
                    callMyPlayList("myPlaylist");
                }else {
                    Toast.makeText(getContext(), R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.playList_ILiked:
                if (Utility.getInstance().isOnline(getContext())){
                    clearList();
                    callPlayListILiked("playlistIliked");
                }else {
                    Toast.makeText(getContext(), R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void callPlayListILiked(String playlistIliked) {
        whichListActive = "playlistIliked";
        rt_playListILiked.setBackgroundResource(R.drawable.sub_tab_shape);
        rt_myPlayList.setBackgroundResource(0);
        rt_recentlyPlay.setBackgroundResource(0);
        tv_playlistILiked.setTextColor(getResources().getColor(R.color.color_pink));
        tv_recentlyPlay.setTextColor(getResources().getColor(R.color.color_white));
        tv_myPlaylist.setTextColor(getResources().getColor(R.color.color_white));

        myDialog.show();
        String accessToken = appSharedPrefrences.getPreference(Constants.KEY_ACCESS_TOKEN, "");
        MyStrimzRequest userRequest = MyStrimzRequest.getInstance();
        userRequest.callLikedPlayed(accessToken, new MyPlaylistCallBack() {


            @Override
            public void onSuccess(List<Datum> data) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                songsList.addAll(data);
                setAdapter(whichListActive);
            }

            @Override
            public void onError(String error) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void callMyPlayList(String myPlaylist) {
        whichListActive = "myPlaylist";
        rt_myPlayList.setBackgroundResource(R.drawable.sub_tab_shape);
        rt_playListILiked.setBackgroundResource(0);
        rt_recentlyPlay.setBackgroundResource(0);
        tv_playlistILiked.setTextColor(getResources().getColor(R.color.color_white));
        tv_recentlyPlay.setTextColor(getResources().getColor(R.color.color_white));
        tv_myPlaylist.setTextColor(getResources().getColor(R.color.color_pink));

        myDialog.show();
        String accessToken = appSharedPrefrences.getPreference(Constants.KEY_ACCESS_TOKEN, "");
        MyStrimzRequest userRequest = MyStrimzRequest.getInstance();
        userRequest.callMyPlaylist(accessToken, new MyPlaylistCallBack() {


            @Override
            public void onSuccess(List<Datum> data) {
                myDialog.dismiss();
                songsList.addAll(data);
                setAdapter(whichListActive);
            }

            @Override
            public void onError(String error) {
                myDialog.dismiss();
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void callRecentlyPlayList(String recentlyPlayed) {
        whichListActive = "recentlyPlayed";
        rt_recentlyPlay.setBackgroundResource(R.drawable.sub_tab_shape);
        rt_playListILiked.setBackgroundResource(0);
        rt_myPlayList.setBackgroundResource(0);
        tv_playlistILiked.setTextColor(getResources().getColor(R.color.color_white));
        tv_recentlyPlay.setTextColor(getResources().getColor(R.color.color_pink));
        tv_myPlaylist.setTextColor(getResources().getColor(R.color.color_white));


        myDialog.show();
        String accessToken = appSharedPrefrences.getPreference(Constants.KEY_ACCESS_TOKEN, "");
        MyStrimzRequest userRequest = MyStrimzRequest.getInstance();
        userRequest.callRecentlyPlayed(accessToken, new MyPlaylistCallBack() {


            @Override
            public void onSuccess(List<Datum> data) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                songsList.addAll(data);
                setAdapter(whichListActive);
            }

            @Override
            public void onError(String error) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void setAdapter(String whichListActive) {

        isLoading=false;

        Log.e("listsize", listItems.size()+"");
        if (recentListAdapter == null){
            recentListAdapter = new RecentListAdapter(getContext(), songsList, whichListActive);
            recyclerView.setAdapter(recentListAdapter);

        } else{
             recentListAdapter.swapItems(songsList);
        }
    }

    private void clearList() {
        if (songsList == null) {
            songsList = new ArrayList<>();
        }
        songsList.clear();
    }

    @Override
    public Void resorceValue(String type) {

        return null;
    }
}
