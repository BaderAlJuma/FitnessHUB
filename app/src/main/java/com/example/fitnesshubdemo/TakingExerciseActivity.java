package com.example.fitnesshubdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class TakingExerciseActivity extends AppCompatActivity {

    ExercisesData data;
    int numOfSets, numOfReps, totalSets, totalReps;
    boolean timer;
    TextView SetsTV, RepsTV;
    Button TakeButton;
    String type = "Reps";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taking_exercise);
        SetsTV = findViewById(R.id.SetsTV);
        RepsTV = findViewById(R.id.RepsTV);
        TakeButton = findViewById(R.id.TakeButton);
        Intent intent = getIntent();
        timer = intent.getBooleanExtra("TimerBased", false);
        totalSets = Integer.parseInt(intent.getStringExtra("Sets"));
        totalReps = Integer.parseInt(intent.getStringExtra("Reps"));
        numOfReps = totalReps;
        numOfSets = 1;
        data = (ExercisesData) intent.getSerializableExtra("Data");
        getSupportActionBar().setTitle(data.getTitle());
        updateTVs();
        if (timer) {
            numOfSets = 0;
            type = "TimeBased";
            TakeButton.setText("Start");
        } else {
            RepsTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (numOfReps == 0) {
                        if (numOfSets != totalSets) {
                            TakeButton(TakeButton);
                        }
                        return;
                    }
                    numOfReps--;
                    updateTVs();
                }
            });
        }
    }

    private void updateTVs() {
        RepsTV.setText(numOfReps + "");
        SetsTV.setText(numOfSets + "/" + totalSets);
    }

    public void TakeButton(View view) {
        if (numOfSets == totalSets) {
            uploadToDB(data, totalSets, totalReps, type);
            finish();
            return;
        }
        if (timer) {
            CountDownTimer timer = new CountDownTimer(numOfReps * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    numOfReps--;
                    updateTVs();
                }

                @Override
                public void onFinish() {
                    TakeButton.setEnabled(true);
                    numOfReps = totalReps;
                }
            };
            TakeButton.setEnabled(false);
            timer.start();
        } else {
            numOfReps = totalReps;

        }
        numOfSets++;
        updateTVs();
        if (numOfSets == totalSets) {
            TakeButton.setText("Done");
        }
    }

    public static void uploadToDB(ExercisesData data, int sets, int reps, String type) {
        HashMap<String, Object> hm = new HashMap<>();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        hm.put("Image", data.getImages()[0]);
        hm.put("Sets", sets);
        hm.put(type, reps);
        hm.put("Title", data.getTitle());
        hm.put("ID", data.getID());
        hm.put("Date", currentDate);
        hm.put("Time", currentTime);

        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("TakenExercises").add(hm);
    }
}
