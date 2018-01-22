package com.mystrimz.android.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mystrimz.android.R;
import com.mystrimz.android.bean.Datum;
import com.mystrimz.android.http.apicallback.SimpleMessageCallBack;
import com.mystrimz.android.http.requests.UserRequest;
import com.mystrimz.android.util.AppSharedPrefrences;
import com.mystrimz.android.util.Constants;
import com.mystrimz.android.util.Utility;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by manishjoshi on 8/1/18.
 */

public class RecentListAdapter extends RecyclerView.Adapter<RecentListAdapter.MyViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private List<Datum> list;
    private AppSharedPrefrences appSharedPrefrences;
    private String access_token;
    private ProgressDialog myDialog;
    private String albumId = "";
    private String shareLink ="https://www.mystrimz.com/"+ albumId;
    private String activeList="";
    public RecentListAdapter(Context context, List<Datum> list, String whichListActive) {
        this.mContext = context;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
        appSharedPrefrences = AppSharedPrefrences.getInstance(mContext);
        access_token = appSharedPrefrences.getPreference(Constants.KEY_ACCESS_TOKEN);
        myDialog = Utility.showProgressDialog(mContext);
        this.activeList = whichListActive;
    }


    @Override
    public RecentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (activeList.equals("myPlaylist")) {
            view = inflater.inflate(R.layout.recent_list, parent, false);
        } else {
            view = inflater.inflate(R.layout.liked_list_view, parent, false);
        }
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecentListAdapter.MyViewHolder holder, final int position) {

       setImage(holder.playlistImage, list.get(position).getCoverImg());
       if (list.get(position).getUserLike().equals("")){
           holder.likeAndDislike.setImageResource(R.drawable.like_btn);
       }else {
           holder.likeAndDislike.setImageResource(R.drawable.fav);
       }
        holder.playListName.setText(list.get(position).getTitle());
        holder.userName.setText(list.get(position).getUserName()+ " ");
        holder.totalLoves.setText(list.get(position).getLikes());
        holder.totalViews.setText(list.get(position).getLikes());
        if (list.get(position).getTracks() != null) {
            int totalTime = 0;
            for (int i = 0; i < list.get(position).getTracks().size(); i++){
                totalTime = totalTime + Integer.parseInt(list.get(position).getTracks().get(i).getDuration());
            }
            int time[] = splitToComponentTimes(BigDecimal.valueOf(totalTime));

            holder.totalTacksWithTime.setText(list.get(position).getTracks().size()+" " + "tracks" +
                    ", " +"total " + time[0]+"h " + time[1]+"m");
        }

        holder.likeAndDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.getInstance().isOnline(mContext)) {
                    if (!list.get(position).getUserLike().equals("")) {

                        callLikeDisLikeApi(false, list.get(position).getId(), holder.likeAndDislike, position);
                    } else {
                        holder.likeAndDislike.setImageResource(R.drawable.like_btn);
                        callLikeDisLikeApi(true, list.get(position).getId(), holder.likeAndDislike, position);
                    }
                }else {
                    Toast.makeText(mContext, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CustomDialogClass cdd=new CustomDialogClass(mContext, position);
               // cdd.show();
                showDilog(list.get(position).getId(), position);
            }
        });
    }

    public static int[] splitToComponentTimes(BigDecimal biggy)
    {
        long longVal = biggy.longValue();
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins , secs};
        return ints;
    }

    private void callLikeDisLikeApi(final boolean isLiked, final String id, final ImageView adapterPosition, final int position) {

        myDialog.show();
        UserRequest userRequest = UserRequest.getInstance();
        userRequest.callLikeDisLike(access_token, id, new SimpleMessageCallBack() {

            @Override
            public void onSuccess(String message) {
                myDialog.dismiss();
                if (isLiked){
                    adapterPosition.setImageResource(R.drawable.fav);
                    list.get(position).setUserLike("id");
                } else {
                    adapterPosition.setImageResource(R.drawable.like_btn);
                    list.get(position).setUserLike("");
                }
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                myDialog.dismiss();
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void showDilog(final String id, final int position) {
        // custom dialog
        final TextView sharePlaylist, reportContent, editPlaylist, deletePlaylist;
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.report_view);

        // set the custom dialog components - text, image and button
        sharePlaylist = (TextView) dialog.findViewById(R.id.sharePlaylist);
        reportContent = (TextView) dialog.findViewById(R.id.reportContent);
        editPlaylist = (TextView) dialog.findViewById(R.id.editPlaylist);
        deletePlaylist = (TextView) dialog.findViewById(R.id.deletePlaylist);

        sharePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    //String sAux = "\nLet me recommend you this application\n\n";
                    String sAux = "https://www.mystrimz.com/" + id;
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    mContext.startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
        reportContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText;
                Button buttonOk,buttoncancle;
                final Dialog dialog2 = new Dialog(mContext);
                dialog2.setContentView(R.layout.open_content_view);
                editText=dialog2.findViewById(R.id.edit_Text_popup);
                buttonOk=dialog2.findViewById(R.id.ok_button);
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Utility.getInstance().isOnline(mContext)) {
                            if (dialog != null) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                            }

                            myDialog.show();
                            UserRequest userRequest = UserRequest.getInstance();
                            userRequest.callShareContent(access_token, id, editText.getText().toString().trim(), new SimpleMessageCallBack() {

                                @Override
                                public void onSuccess(String message) {
                                    if (myDialog.isShowing()){
                                        myDialog.dismiss();
                                    }

                                    if (dialog2 != null) {
                                        if (dialog2.isShowing()) {
                                            dialog2.dismiss();
                                        }
                                    }

                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onError(String error) {
                                    if (myDialog.isShowing()){
                                        myDialog.dismiss();
                                    }
                                    Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                                }

                            });
                        }else {
                            Toast.makeText(mContext, R.string.check_internet, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                buttoncancle = dialog2.findViewById(R.id.cancel2);
                buttoncancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();
                    }
                });
                dialog2.show();

            }
        });
        editPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.getInstance().isOnline(mContext)){
                    Toast.makeText(mContext, "Under development", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });
        deletePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(mContext);
                alertDialog2.setMessage("Are you sure you want delete this list?");
                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogs, int which) {

                                if (Utility.getInstance().isOnline(mContext)){
                                    myDialog.show();
                                    UserRequest userRequest = UserRequest.getInstance();
                                    userRequest.callDeleteList(access_token, id, new SimpleMessageCallBack() {

                                        @Override
                                        public void onSuccess(String message) {
                                            if (myDialog.isShowing()){
                                                myDialog.dismiss();
                                            }
                                            if (dialog != null) {
                                                if (dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                            }
                                            for (int i = 0; i <list.size(); i++){
                                                if (list.get(i).getId().equals(id)){
                                                    list.remove(i);
                                                    notifyDataSetChanged();
                                                }
                                            }
                                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onError(String error) {
                                            if (myDialog.isShowing()){
                                                myDialog.dismiss();
                                            }
                                            if (dialog != null) {
                                                if (dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                            }
                                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                                        }

                                    });
                                }else {
                                    Toast.makeText(mContext, R.string.check_internet, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog2, int which) {
                                // Write your code here to execute after dialog

                                dialog2.cancel();
                            }
                        });

                alertDialog2.show();
            }
        });


        dialog.show();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void swapItems(List<Datum> songsList) {
        list = songsList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView playListName, userName;
        private ImageView playlistImage;
        private TextView totalViews, totalLoves;
        private TextView totalTacksWithTime;
        private ImageView menu;
        private ImageView likeAndDislike;

        private ImageView userImage;
        private TextView recentPlayUserNAme;

        public MyViewHolder(View itemView) {
            super(itemView);
            playListName = (TextView)itemView.findViewById(R.id.playListName);
            playlistImage = (ImageView) itemView.findViewById(R.id.playList_imagee);
            userName = (TextView) itemView.findViewById(R.id.userName);
            totalViews = (TextView)itemView.findViewById(R.id.totalViews);
            totalLoves = (TextView) itemView.findViewById(R.id.totalLove);
            totalTacksWithTime = (TextView)itemView.findViewById(R.id.totalTrack);
            likeAndDislike = (ImageView)itemView.findViewById(R.id.likeAndDislike);
            menu = (ImageView)itemView.findViewById(R.id.menu);

            if (!activeList.equals("myPlaylist")){

            }
        }
    }

    private void setImage(ImageView imageView, String url) {
        Log.e("My image url",url);
        Glide.with(mContext)
                .load(url)
                .crossFade()
                .centerCrop()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String  model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.e("RESOURCE EXCEPTION",e.toString()) ;
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.e("RESOURCE","Ready") ;
                        return false;
                    }
                })
                .into(imageView);

    }
}
