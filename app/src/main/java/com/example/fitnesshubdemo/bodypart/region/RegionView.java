package com.example.fitnesshubdemo.bodypart.region;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesshubdemo.ExercisesListActivity;
import com.example.fitnesshubdemo.R;
import com.example.fitnesshubdemo.bodypart.view.WaveEffectLayout;

import java.util.ArrayList;
import java.util.List;

public class RegionView {

    private final WaveEffectLayout container;
    private final Context mContext;
    private FrameLayout leftRegionLayout, rightRegionLayout;
    private LayoutInflater layoutInflater;
    private Region[] regions;
    private List<View> regionViews = new ArrayList<>();

    public RegionView(WaveEffectLayout container, Context context) {
        this.container = container;
        this.mContext = context;
        this.layoutInflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        leftRegionLayout = container.findViewById(R.id.left_region_layout);
        rightRegionLayout = container.findViewById(R.id.right_region_layout);

    }

    public void setAdapter(int regionType) {
        regionViews.clear();
        if (-1 == regionType) {
            if (leftRegionLayout != null) {
                leftRegionLayout.removeAllViews();
            }
            if (rightRegionLayout != null) {
                rightRegionLayout.removeAllViews();
            }
            return;
        }

        regions = RegionParam.regionItems.get(regionType);
        if (regions == null) {
            return;
        }
        for (Region region : regions) {
            regionViews.add(getItem(region));
        }

        regionViews.add(getItem(Region.SKIN));
        refresh();
    }

    private View getItem(final Region region) {
        View itemView = layoutInflater.inflate(R.layout.region_item, null);
        final TextView textView = (TextView) itemView.findViewById(R.id.text_view);
        textView.setText(region.getName());
        itemView.setTag(String.valueOf(region.getValue()));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ExercisesListActivity.class);
                intent.putExtra("ID", region.getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return itemView;
    }

    public void refresh() {

        if (leftRegionLayout == null || rightRegionLayout == null)
            return;

        leftRegionLayout.removeAllViews();
        rightRegionLayout.removeAllViews();
        int size = regionViews.size() - 1;
        if (size > 0) {
            int columnSize = size / 2 + size % 2;

            for (int i = 0; i < columnSize; i++) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 0;
                params.topMargin = regions[i].getDestinationY();
                leftRegionLayout.addView(regionViews.get(i), params);
            }

            for (int i = columnSize; i < size; i++) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 0;
                params.topMargin = regions[i].getDestinationY();
                rightRegionLayout.addView(regionViews.get(i), params);
            }
        }
    }

}

