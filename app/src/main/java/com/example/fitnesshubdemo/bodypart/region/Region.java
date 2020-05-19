package com.example.fitnesshubdemo.bodypart.region;

/**
 * Created by angelo on 2015/2/28.
 */
public enum Region {
    CHEST("Chest", 111, LayoutSide.LEFT, 30, 380, 200),
    FOREARM("Forearm", 111, LayoutSide.LEFT, 200, 600, 450),
    ABS("Abs", 13, LayoutSide.LEFT, 0, 581, 700),
    SHOULDERS("Shoulders", 111, LayoutSide.RIGHT, 125, 300, 200),
    UPPER_LEGS("Upper Legs", 15, LayoutSide.RIGHT, 83, 877, 1026),
    BICEPS("Biceps", 222, LayoutSide.RIGHT, 150, 400, 400),

    GLUTES("Glutes", 19, LayoutSide.LEFT, 54, 666, 538),
    BACK("Back", 18, LayoutSide.LEFT, 0, 320, 168),
    TRICEPS("Triceps", 222, LayoutSide.RIGHT, 150, 400, 400),
    LOWER_LEGS("Lower Legs", 111, LayoutSide.RIGHT, 60, 1080, 850),
    SKIN("Skin", 17, LayoutSide.RIGHT, 0, 0, 0, 1),
    ;


    private final String name;
    private final int value;
    private final int layoutSide;
    private final int offsetSX, offsetSY; // path start coordinate point
    private final int offsetDY; // reference vertical coordinate of the center of the part
    private int startX, startY;
    private final int offSetNum; // Number of vertical offsets between parts
    private int destinationY; // = offsetDY + RegionParam.OFFSET_Y * offsetNum

    Region(final String name, final int value, final int layoutSide, final int offsetSX, final int offsetSY, final int offsetDY) {
        this(name, value, layoutSide, offsetSX, offsetSY, offsetDY, 0, 0, 0, 0);
    }

    Region(final String name, final int value, final int layoutSide, final int offsetSX, final int offsetSY, final int offsetDY,
           final int offSetNum) {
        this(name, value, layoutSide, offsetSX, offsetSY, offsetDY, 0, 0, offSetNum, 0);
    }

    Region(final String name, final int value, final int layoutSide, final int offsetSX, final int offsetSY, final int offsetDY,
           int startX, int startY, int offSetNum, int destinationY) {
        this.name = name;
        this.value = value;
        this.layoutSide = layoutSide;
        this.offsetSX = offsetSX;
        this.offsetSY = offsetSY;
        this.offsetDY = offsetDY;
        this.startX = startX;
        this.startY = startY;
        this.offSetNum = offSetNum;
        this.destinationY = destinationY;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static String getName(int value) {
        for (Region c : Region.values()) {
            if (c.getValue() == value) {
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int getLayoutSide() {
        return layoutSide;
    }

    public int getOffsetSX() {
        return offsetSX;
    }

    public int getOffsetSY() {
        return offsetSY;
    }

    public int getOffsetDY() {
        return offsetDY;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getOffSetNum() {
        return offSetNum;
    }

    public int getDestinationY() {
        return destinationY;
    }

    public void setDestinationY(int destinationY) {
        this.destinationY = destinationY;
    }


    public static class LayoutSide {
        public final static int LEFT = 0;
        public final static int RIGHT = 1;
    }
}
