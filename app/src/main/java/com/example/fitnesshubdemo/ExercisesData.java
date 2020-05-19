package com.example.fitnesshubdemo;


import java.io.Serializable;
import java.util.Arrays;

public class ExercisesData implements Serializable {
    String ID, Title, Difficulty, MuscleGroup, Steps, Date;
    String Equipments[];
    String Images[];

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


    public ExercisesData(String title, String difficulty, String muscleGroup, String steps, String[] equipments, String[] images) {
        Title = title;
        Difficulty = difficulty;
        MuscleGroup = muscleGroup;
        Steps = steps;
        Equipments = equipments;
        Images = images;
    }

    public ExercisesData() {
        Difficulty = "";
        MuscleGroup = "";
        Steps = "";
        Title = "";
        ID = "";
        Equipments = null;
        Images = null;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(String difficulty) {
        Difficulty = difficulty;
    }

    public String getMuscleGroup() {
        return MuscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        MuscleGroup = muscleGroup;
    }

    public String getSteps() {
        return Steps;
    }

    public void setSteps(String steps) {
        Steps = steps;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String[] getEquipments() {
        return Equipments;
    }

    public void setEquipments(String[] equipments) {
        Equipments = equipments;
    }

    public String[] getImages() {
        return Images;
    }

    public void setImages(String[] images) {
        Images = images;
    }

    @Override
    public String toString() {
        return "ExercisesData{" +
                "Difficulty='" + Difficulty + '\'' +
                ", MuscleGroup='" + MuscleGroup + '\'' +
                ", Steps='" + Steps + '\'' +
                ", Title='" + Title + '\'' +
                ", Equipments=" + Arrays.toString(Equipments) +
                ", Images=" + Arrays.toString(Images) +
                '}';
    }

}
