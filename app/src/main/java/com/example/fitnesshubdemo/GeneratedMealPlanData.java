package com.example.fitnesshubdemo;

public class GeneratedMealPlanData {
    String day, type, id, imagetype, title;
    int slot; // the slot 0 for breakfast 1 for lunch and 2 for dinner

    @Override
    public String toString() {
        return "mealplanData{" +
                "day='" + day + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", imagetype='" + imagetype + '\'' +
                ", title='" + title + '\'' +
                ", slot=" + slot +
                '}';
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagetype() {
        return imagetype;
    }

    public void setImagetype(String imagetype) {
        this.imagetype = imagetype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
