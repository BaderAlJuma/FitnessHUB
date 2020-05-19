package com.example.fitnesshubdemo;


import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LandingPageImageFragment extends Fragment {

    int ImgRes = 0;

    public LandingPageImageFragment(int imgRes) {
        // Required empty public constructor
        this.ImgRes = imgRes;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing_page_image, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageDrawable(getResources().getDrawable(ImgRes));
        return view;
    }

}
