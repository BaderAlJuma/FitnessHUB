package com.example.fitnesshubdemo;

public class MealData implements Comparable<MealData> {
    String MealID, DocumentID, Type, Image, Title, Date;
    int Slot;
    boolean isGenerated;

    public String getMealID() {
        return MealID;
    }

    public void setMealID(String mealID) {
        MealID = mealID;
    }

    public String getDocumentID() {
        return DocumentID;
    }

    public void setDocumentID(String documentID) {
        DocumentID = documentID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getSlot() {
        return Slot;
    }

    public void setSlot(int slot) {
        Slot = slot;
    }

    public boolean isGenerated() {
        return isGenerated;
    }

    public void setGenerated(boolean generated) {
        isGenerated = generated;
    }

    @Override
    public int compareTo(MealData o) {
        return getSlot() - o.getSlot();
    }
}
