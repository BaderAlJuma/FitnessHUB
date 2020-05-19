package com.example.fitnesshubdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewExerciseActivity extends AppCompatActivity {
    ExercisesData data;
    TextView EquipmentTV, StepsTV;
    RecyclerView RecyclerView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);
        data = (ExercisesData) getIntent().getSerializableExtra("Exercise");
        getSupportActionBar().setTitle(data.getTitle());
        EquipmentTV = findViewById(R.id.EquipmentTV);
        StepsTV = findViewById(R.id.StepsTV);
        RecyclerView = findViewById(R.id.RecyclerView);
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(ViewExerciseActivity.this);
                final View view = factory.inflate(R.layout.pop_up_workout_take_view, null);
                final AlertDialog dialog = new AlertDialog.Builder(ViewExerciseActivity.this).create();
                dialog.setView(view);
                RadioGroup radioGroup = view.findViewById(R.id.RadioGroup);
                final RadioButton[] chosedButton = {view.findViewById(radioGroup.getCheckedRadioButtonId())};
                final EditText setsET = view.findViewById(R.id.Sets);
                final EditText repsET = view.findViewById(R.id.Reps);
                Button button = view.findViewById(R.id.SubmitButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chosedButton[0].getId() == R.id.opt1) {
                            TakingExerciseActivity.uploadToDB(data, 0, 0, "Quick");
                            dialog.cancel();
                            finish();
                        } else {
                            Intent intent = new Intent(getBaseContext(), TakingExerciseActivity.class);
                            intent.putExtra("Sets", setsET.getText().toString());
                            intent.putExtra("Reps", repsET.getText().toString());
                            intent.putExtra("TimerBased", chosedButton[0].getId() == R.id.opt2);
                            intent.putExtra("Data", data);
                            startActivity(intent);
                            dialog.cancel();
                            finish();
                            //Check then take
                        }
                    }
                });
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.opt1) {
                            setsET.setVisibility(View.GONE);
                            repsET.setVisibility(View.GONE);
                        } else {
                            if (checkedId == R.id.opt2) {
                                repsET.setHint("Time in Seconds");
                            } else {
                                repsET.setHint("Reps");
                            }
                            setsET.setVisibility(View.VISIBLE);
                            repsET.setVisibility(View.VISIBLE);
                        }
                        chosedButton[0] = view.findViewById(checkedId);
                    }
                });
                dialog.show();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView.setLayoutManager(layoutManager);
        String equipments = "";
        for (int i = 0; i < data.getEquipments().length; i++) {
            equipments += data.getEquipments()[i];
            if (i != data.getEquipments().length - 1) {
                equipments += ",";
            }
        }
        EquipmentTV.setText(equipments);
        StepsTV.setText(data.getSteps() + "\n\n\n");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(Arrays.asList(data.getImages()));
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).toLowerCase().contains("gif")) {
                arrayList.add(0, arrayList.remove(i));
            }
        }
        ViewExercisesImagesAdapter adapter = new ViewExercisesImagesAdapter(arrayList);
        RecyclerView.setAdapter(adapter);
    }


    public class ViewExercisesImagesAdapter extends RecyclerView.Adapter<ViewExercisesImagesAdapter.MyViewHolder> {

        private ArrayList<String> arr;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView Image;

            public MyViewHolder(View view) {
                super(view);
                Image = view.findViewById(R.id.ExerciseIV);
            }
        }


        public ViewExercisesImagesAdapter(ArrayList<String> arraylist) {
            this.arr = arraylist;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_image, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Glide.with(getBaseContext()).load(arr.get(position)).into(holder.Image);
        }

        @Override
        public int getItemCount() {
            return arr.size();
        }
    }

}
