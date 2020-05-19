package com.example.fitnesshubdemo.bodypart.region;

import android.graphics.Path;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.example.fitnesshubdemo.R;
import com.example.fitnesshubdemo.bodypart.UIUtil;
import com.example.fitnesshubdemo.bodypart.view.AnimatedPathView.AnimatedPathView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angelo on 2015/2/28.
 */
public class RegionPathView {

    public static AnimatedPathView pathView;

    private FrameLayout container;
    private List<Path> paths = new ArrayList<>();
    private Region[] regions;
    private int regionRadius = 0;
    private int pathOffsetX = 0;

    public RegionPathView(FrameLayout container) {
        this.container = container;
        init();
    }

    private void init() {
        pathView = container.findViewById(R.id.animatedPathView);
        regionRadius = UIUtil.dip2px(RegionParam.REGION_WIDTH / 2f);
        pathOffsetX = UIUtil.dip2px(RegionParam.PATH_OFFSET_X / 2f);
    }

    public void setAdapter(int regionType) {
        if (-1 == regionType) {
            pathView.setToClear(true);
            pathView.invalidate();
            return;
        }

        regions = RegionParam.regionItems.get(regionType);

        if (regions == null || regions.length == 0)
            return;

        for (Region region : regions) {
            paths.add(createPath(region));
        }
        perform();
    }


    private Path createPath(Region region) {
        final Path path = new Path();
        final int destinationY = region.getDestinationY() + regionRadius;

        path.moveTo(region.getStartX(), region.getStartY());
        if (region.getLayoutSide() == Region.LayoutSide.LEFT) {
            path.lineTo(RegionParam.LEFT_REGION_X + pathOffsetX, destinationY);
            path.lineTo(RegionParam.LEFT_REGION_X, destinationY);
        } else {
            path.lineTo(RegionParam.RIGHT_REGION_X - pathOffsetX, destinationY);
            path.lineTo(RegionParam.RIGHT_REGION_X, destinationY);
        }
//        path.close();
        return path;
    }

    public void perform() {

        pathView.setRegions(regions);
        pathView.setPaths(paths);
        pathView.setFillAfter(true);
        pathView.useNaturalColors();

        pathView.getPathAnimator().
                delay(100).
                duration(600).
                interpolator(new AccelerateDecelerateInterpolator()).
                start();

    }

    public AnimatedPathView getPathView() {
        return pathView;
    }

}
