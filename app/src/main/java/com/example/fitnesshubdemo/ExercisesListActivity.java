package com.example.fitnesshubdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExercisesListActivity extends AppCompatActivity {
    String TargetMuscleGroup;
    ListView listView;
    private ArrayList<ExercisesData> arrayList;
    ExerciseAdapter adapter;
    static String myActivityLevel = "Beginner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_list);
        TargetMuscleGroup = getIntent().getStringExtra("ID");
        getSupportActionBar().setTitle(TargetMuscleGroup);
//        myLevel = getSharedPreferences()
        listView = findViewById(R.id.Listview);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (myActivityLevel == null || myActivityLevel.isEmpty()) {
            myActivityLevel = "Beginner";
        }
        db.collection("Exercises").whereEqualTo("MuscleGroup", TargetMuscleGroup).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                arrayList = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    ExercisesData data = new ExercisesData();
                    if (!TargetMuscleGroup.toLowerCase().equals((doc.get("MuscleGroup") + "").toLowerCase())
                            || !myActivityLevel.toLowerCase().equals((doc.get("Difficulty") + "").toLowerCase())) {
                        continue;
                    }

                    data.setMuscleGroup(doc.get("MuscleGroup") + "");
                    data.setDifficulty(doc.get("Difficulty") + "");
                    data.setTitle(doc.get("Title") + "");
                    data.setSteps(doc.get("Steps") + "");
                    data.setID(doc.getId());
                    List<String> temp = (List<String>) doc.get("Equipments");
                    String[] equipments = Arrays.copyOf(temp.toArray(), temp.size(), String[].class);
                    equipments[0] = equipments[0].substring(equipments[0].indexOf(':') + 2);
                    data.setEquipments(equipments);
                    temp = (List<String>) doc.get("Images");
                    String[] Images = Arrays.copyOf(temp.toArray(), temp.size(), String[].class);
                    data.setImages(Images);
                    arrayList.add(data);
                }
                adapter = new ExerciseAdapter(ExercisesListActivity.this, arrayList);
                listView.setAdapter(adapter);
            }
        });
    }

    public static class ExerciseAdapter extends ArrayAdapter<ExercisesData> {

        private final Activity context;
        private final ArrayList<ExercisesData> arrayList;

        public ExerciseAdapter(Activity context, ArrayList<ExercisesData> arrayList) {
            super(context, R.layout.row_image_and_name, arrayList);
            this.arrayList = arrayList;
            this.context = context;

        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.row_image_and_name, null, true);

            TextView TitleText = view.findViewById(R.id.ExerciseTV);
            ImageView imageView = view.findViewById(R.id.ExerciseIV);
            TitleText.setText(arrayList.get(position).getTitle());
            Glide.with(context).load(arrayList.get(position).getImages()[0]).into(imageView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ViewExerciseActivity.class);
                    intent.putExtra("Exercise", arrayList.get(position));
                    getContext().startActivity(intent);
                }
            });
            return view;

        }

    }

}
