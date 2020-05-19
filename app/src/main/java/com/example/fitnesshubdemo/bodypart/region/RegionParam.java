package com.example.fitnesshubdemo.bodypart.region;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by angelo on 2015/2/26.
 */
public class RegionParam {
    public static final int REGION_FRONT = 0x00000002;
    public static final int REGION_BACK = 0x00000003;

    /**
     * Body Picture Standard Length in pixels
     */
    public static final int standardHeight = 1378;

    /**
     * Body picture standard width in pixels
     */
    public static final int standardWidth = 693;

    /**
     * Top transparent part offset
     */
    public static final int standardOffsetY = 8;

    /**
     * Round part width  in dp
     */
    public static final int REGION_WIDTH = 50;

    /**
     * Ordinate offset between parts  in dp
     */
    public static final int STANDARD_OFFSET_Y = 20;

    /**
     * The ordinate offset of the part after being converted to px  in pixels
     */
    public static int OFFSET_Y = 90;

    /**
     * Path horizontal axis vertex offset in dp
     */
    public static final float PATH_OFFSET_X = REGION_WIDTH + 20f;
    public static int LEFT_REGION_X = 50;
    public static int RIGHT_REGION_X = 50;

    public static final Map<Integer, Region[]> regionItems = new HashMap<>();

    static {
        regionItems.put(REGION_FRONT, new Region[]{Region.CHEST,Region.FOREARM,Region.ABS,Region.SHOULDERS,Region.UPPER_LEGS,Region.BICEPS});
        regionItems.put(REGION_BACK, new Region[]{Region.GLUTES, Region.BACK,Region.TRICEPS,Region.LOWER_LEGS});
    }
}
