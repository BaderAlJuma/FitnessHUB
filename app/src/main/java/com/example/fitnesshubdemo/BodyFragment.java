package com.example.fitnesshubdemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitnesshubdemo.bodypart.region.RegionView;
import com.example.fitnesshubdemo.bodypart.view.HumanBodyWidget;
import com.example.fitnesshubdemo.bodypart.view.WaveEffectLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BodyFragment extends Fragment {

    private WaveEffectLayout containery;
    private HumanBodyWidget bodyWidget;
    //    private ImageView manIv, womanIv;
//    private TextView manTv, womanTv, flipFrontTv, flipBackTv;
    private FloatingActionButton fab;
    private boolean isShowingFront = true;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    public BodyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_body, container, false);
        containery = view.findViewById(R.id.container);
//        manIv = view.findViewById(R.id.man_icon);
//        manTv = view.findViewById(R.id.man_text);
//        womanIv = view.findViewById(R.id.woman_icon);
//        womanTv = view.findViewById(R.id.woman_text);
//        flipFrontTv = view.findViewById(R.id.flipFront);
//        flipBackTv = view.findViewById(R.id.flipBack);
        bodyWidget = new HumanBodyWidget(getContext(), containery, savedInstanceState);
        init();
        fab = view.findViewById(R.id.FloatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodyWidget.flipBody(isShowingFront);
                isShowingFront = !isShowingFront;
            }
        });
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String gender = documentSnapshot.get("Gender")+"";
                if(gender.equals("Female")){
                    bodyWidget.toggleBodyGenderImage(false);
                }
            }
        });
        return view;
    }

    public void init() {
        containery.setRegionView(new RegionView(containery, getContext()));
    }

    @Override
    public void onResume() {
        init();
        super.onResume();
    }
    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        for (BodyActivity.MyTouchListener listener : mTouchListeners) {
//            listener.onTouchEvent(event);
//        }
//        return super.dispatchTouchEvent(event);
//    }
//
//    public void genderClick(View view) {
//        switch (view.getId()) {
//            case R.id.man_btn:
//                if (bodyWidget.toggleBodyGenderImage(true)) {
//                    view.findViewById(R.id.man_btn).setBackgroundColor(getResources().getColor(R.color.colorLightBlue));
//                    findViewById(R.id.woman_btn).setBackgroundColor(Color.TRANSPARENT);
//                    manIv.setImageResource(R.mipmap.icon_man_pressed);
//                    manTv.setTextColor(Color.WHITE);
//                    womanIv.setImageResource(R.mipmap.icon_woman);
//                    womanTv.setTextColor(getResources().getColor(R.color.colorLightBlue));
//                }
//                break;
//            case R.id.woman_btn:
//                if (bodyWidget.toggleBodyGenderImage(false)) {
//                    findViewById(R.id.man_btn).setBackgroundColor(Color.TRANSPARENT);
//                    findViewById(R.id.woman_btn).setBackgroundColor(getResources().getColor(R.color.colorLightBlue));
//                    manIv.setImageResource(R.mipmap.icon_man);
//                    manTv.setTextColor(getResources().getColor(R.color.colorLightBlue));
//                    womanIv.setImageResource(R.mipmap.icon_woman_pressed);
//                    womanTv.setTextColor(Color.WHITE);
//                }
//                break;
//        }
//    }

//    public void sideClick(View view) {
//
//        switch (view.getId()) {
//            case R.id.flipFront:
//                if (bodyWidget.flipBody(false)) {
//                    flipFrontTv.setBackgroundColor(getResources().getColor(R.color.colorLightBlue));
//                    flipBackTv.setBackgroundColor(Color.TRANSPARENT);
//                    flipFrontTv.setTextColor(Color.WHITE);
//                    flipBackTv.setTextColor(getResources().getColor(R.color.colorLightBlue));
//                }
//                break;
//            case R.id.flipBack:
//                if (bodyWidget.flipBody(true)) {
//                    flipFrontTv.setBackgroundColor(Color.TRANSPARENT);
//                    flipBackTv.setBackgroundColor(getResources().getColor(R.color.colorLightBlue));
//                    flipFrontTv.setTextColor(getResources().getColor(R.color.colorLightBlue));
//                    flipBackTv.setTextColor(Color.WHITE);
//                }
//                break;
//        }
//    }
}
