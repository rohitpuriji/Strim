package com.mystrimz.android.ui.activity;

import android.graphics.Color;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.AutoLabelUISettings;
import com.dpizarro.autolabel.library.Label;
import com.mystrimz.android.R;
import com.mystrimz.android.bean.Tagsbean;
import com.mystrimz.android.util.Validation;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class TagsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;

    private ArrayList<String > tagsList;
    private LinearLayout parentLinearLayout;
    private GridView gridView;
    private ImageView add_btn;
    private ImageView close_btn;
    private AutoLabelUI mAutoLabel;
    private AutoLabelUISettings autoLabelUISettings;
    private List<Label> labels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);

        init();
        onClickListener();
        addTags();

    }

    private void onClickListener() {
        add_btn.setOnClickListener(this);
        close_btn.setOnClickListener(this);
    }


    private void init() {
        tagsList = new ArrayList<>();
        if (getIntent() != null){
             tagsList = getIntent().getStringArrayListExtra("stock_list");
        }
        editText = (EditText)findViewById(R.id.et_tags);
       // parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);

        add_btn = (ImageView)findViewById(R.id.add_tag);
        close_btn = (ImageView)findViewById(R.id.cross);
        //labels = new ArrayList<>();

        mAutoLabel = (AutoLabelUI)findViewById(R.id.label_view);
        labels = mAutoLabel.getLabels();
        autoLabelUISettings = new AutoLabelUISettings.Builder()
                .withMaxLabels(100)
                .withIconCross(R.drawable.cross)
                .withLabelsClickables(false)
                .withShowCross(true)
                .withTextColor(getResources().getColor(R.color.color_white))
                .withTextSize(R.dimen.label_title_size)
                //.withBackgroundResource(Color.GREEN)
                .withLabelPadding(20)
                .build();

        mAutoLabel.setSettings(autoLabelUISettings);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.add_tag:
                addTagsrunTime();
                break;

            case R.id.cross:
                saveTags();
        }
    }

    /**
     * save tags
     */
    private void saveTags() {
        labels = mAutoLabel.getLabels();
        tagsList.clear();
        if (labels.size() > 0){
            for (int i = 0; i < labels.size(); i++) {
                tagsList.add(labels.get(i).getText());
            }
        }
        Log.e("listSize", tagsList.size()+"");
        Tagsbean tagsbean = new Tagsbean();
        tagsbean.setTagsList(tagsList);
        EventBus.getDefault().post(tagsbean);
        finish();
    }

    private void addTagsrunTime() {

        //final AutoLabelUI mAutoLabel = (AutoLabelUI)findViewById(R.id.label_view);
        //final List<Label> labels = mAutoLabel.getLabels();
        /*AutoLabelUISettings autoLabelUISettings = new AutoLabelUISettings.Builder()
                .withMaxLabels(100)
                .withIconCross(R.drawable.cross)
                .withLabelsClickables(false)
                .withShowCross(true)
                .withTextColor(getResources().getColor(R.color.color_white))
                .withTextSize(R.dimen.label_title_size)
                //.withBackgroundResource(Color.GREEN)
                .withLabelPadding(20)
                .build();

        mAutoLabel.setSettings(autoLabelUISettings);*/
        if (Validation.getInstance().isEmpty(editText.getText().toString().trim())) {
            mAutoLabel.addLabel(editText.getText().toString().trim());
            editText.setText("");
        } else {
            Toast.makeText(this, "Please enter the tag", Toast.LENGTH_SHORT).show();
        }

       /*mAutoLabel.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
           @Override
           public void onClickLabel(View v) {
               Toast.makeText(TagsActivity.this, "maniiiii" +
                       ""+((Label) v).getText(), Toast.LENGTH_SHORT).show();
           }
       });
        mAutoLabel.setOnRemoveLabelListener(new AutoLabelUI.OnRemoveLabelListener() {
            @Override
            public void onRemoveLabel(View view, int position) {

                Log.e("list", position+"");
            }
        });

        Log.e("list", labels.size()+"");*/
    }

    private void addTags() {

       if (tagsList.size() > 0){
           for (int i = 0; i <tagsList.size(); i++){
               mAutoLabel.addLabel(tagsList.get(i));
           }
       }

        /*LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       *//* LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);*//*
        final View addView = vi.inflate(R.layout.tags_view, null);
        TextView textOut = (TextView)addView.findViewById(R.id.tags_title);
        textOut.setText(editText.getText().toString());

       *//* RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(16, 10, 16, 10);
        parentLinearLayout.setLayoutParams(layoutParams);*//*

        ImageView buttonRemove = (ImageView) addView.findViewById(R.id.remove_tag);
        buttonRemove.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ((LinearLayout)addView.getParent()).removeView(addView);
            }});

        //parentLinearLayout.addView(addView);
        parentLinearLayout.addView(addView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveTags();
    }
}
