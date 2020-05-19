package com.example.fitnesshubdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExercisesLogFragment extends Fragment {
    ListView listview;
    ArrayList<ExercisesData> fullArrayList, arrayList;
    ExercisesLogAdapter adapter;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    Date CurrentDate;
    TextView DayTitleTV, BeforeButton, AfterButton;
    boolean comingFromSearch = false;

    public ExercisesLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercises_log, container, false);
        listview = view.findViewById(R.id.Listview);
        CurrentDate = new Date();
        fullArrayList = new ArrayList<>();
        arrayList = new ArrayList<>();
        DayTitleTV = view.findViewById(R.id.CurrentShowingDayTextView);
        BeforeButton = view.findViewById(R.id.BeforeButton);
        AfterButton = view.findViewById(R.id.AfterButton);
        AfterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AfterButtonClick();
            }
        });
        BeforeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeforeButtonClick();
            }
        });
        updateDb();
        return view;
    }

    private void updateDb() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("TakenExercises").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                fullArrayList.clear();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    ExercisesData data = new ExercisesData();
                    data.setID(doc.get("ID") + "");
                    data.setDate(doc.get("Date") + "");
                    data.setTitle(doc.get("Title") + "");
                    data.setImages((doc.get("Image") + "").split(" "));
                    fullArrayList.add(data);
                }
                adapter = new ExercisesLogAdapter(getActivity(), arrayList);
                listview.setAdapter(adapter);
                loadData();
            }
        });
    }

    public void BeforeButtonClick() {
        Date now = new Date();
        CurrentDate = modifyDate(CurrentDate, -1);
        Date yesterdayDate = modifyDate(now, -1);
        Date tomorrowDate = modifyDate(now, 1);
        if (formatDate(now).compareTo(formatDate(CurrentDate)) == 0) {
            DayTitleTV.setText("Today");
        } else if (formatDate(yesterdayDate).compareTo(formatDate(CurrentDate)) == 0) {
            DayTitleTV.setText("Yesterday");
        } else if (formatDate(tomorrowDate).compareTo(formatDate(CurrentDate)) == 0) {
            DayTitleTV.setText("Tomorrow");
        } else {
            DayTitleTV.setText(formatDate(CurrentDate));
        }
        loadData();
    }

    public void AfterButtonClick() {
        Date now = new Date();
        CurrentDate = modifyDate(CurrentDate, 1);
        Date yesterdayDate = modifyDate(now, -1);
        Date tomorrowDate = modifyDate(now, 1);
        if (formatDate(now).compareTo(formatDate(CurrentDate)) == 0) {
            DayTitleTV.setText("Today");
        } else if (formatDate(yesterdayDate).compareTo(formatDate(CurrentDate)) == 0) {
            DayTitleTV.setText("Yesterday");
        } else if (formatDate(tomorrowDate).compareTo(formatDate(CurrentDate)) == 0) {
            DayTitleTV.setText("Tomorrow");
        } else {
            DayTitleTV.setText(formatDate(CurrentDate));
        }
        loadData();
    }

    private void loadData() {
        arrayList.clear();
        for (ExercisesData data : fullArrayList) {
            if (formatDate(CurrentDate).equals(data.getDate())) {
                arrayList.add(data);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);
    }

    private Date modifyDate(Date date, int value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, value);
        return cal.getTime();
    }

    public static class ExercisesLogAdapter extends ArrayAdapter<ExercisesData> {

        private final Activity context;
        private final ArrayList<ExercisesData> arrayList;

        public ExercisesLogAdapter(Activity context, ArrayList<ExercisesData> arrayList) {
            super(context, R.layout.row_image_and_name, arrayList);
            this.arrayList = arrayList;
            this.context = context;

        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.row_image_and_name, null, true);

            TextView TitleText = view.findViewById(R.id.ExerciseTV);
            TextView addButton = view.findViewById(R.id.addButton);

            ImageView imageView = view.findViewById(R.id.ExerciseIV);
            TitleText.setText(arrayList.get(position).getTitle());
            Glide.with(context).load(arrayList.get(position).getImages()[0]).into(imageView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore.getInstance().collection("Exercises").document(arrayList.get(position).getID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Intent intent = new Intent(getContext(), ViewExerciseActivity.class);
                            ExercisesData exerciseData = new ExercisesData();
                            exerciseData.setMuscleGroup(documentSnapshot.get("MuscleGroup") + "");
                            exerciseData.setDifficulty(documentSnapshot.get("Difficulty") + "");
                            exerciseData.setTitle(documentSnapshot.get("Title") + "");
                            exerciseData.setSteps(documentSnapshot.get("Steps") + "");
                            exerciseData.setID(documentSnapshot.getId());
                            List<String> temp = (List<String>) documentSnapshot.get("Equipments");
                            String[] equipments = Arrays.copyOf(temp.toArray(), temp.size(), String[].class);
                            equipments[0] = equipments[0].substring(equipments[0].indexOf(':') + 2);
                            exerciseData.setEquipments(equipments);
                            temp = (List<String>) documentSnapshot.get("Images");
                            String[] Images = Arrays.copyOf(temp.toArray(), temp.size(), String[].class);
                            exerciseData.setImages(Images);
                            intent.putExtra("Exercise", exerciseData);
                            getContext().startActivity(intent);
                        }
                    });
                }
            });
            return view;

        }

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        updateDb();
    }
}
