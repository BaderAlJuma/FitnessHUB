package com.example.fitnesshubdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ViewRecipeDetails extends AppCompatActivity {
    TextView ProtienTV, FatTV, CarbsTV, CaloriesTV, StepsTV;
    ImageView imageView;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FloatingActionButton floatingActionButton;
    MealData mealData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_details);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mealData = new MealData();

        floatingActionButton = findViewById(R.id.floatingActionButton);
        ProtienTV = findViewById(R.id.ProtienTV);
        FatTV = findViewById(R.id.FatTV);
        CarbsTV = findViewById(R.id.CarbsTV);
        CaloriesTV = findViewById(R.id.CaloriesTV);
        StepsTV = findViewById(R.id.StepsTV);
        imageView = findViewById(R.id.imageView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToMealPlan(mealData);
            }
        });
        getData();
    }

    private void getData() {
        final OkHttpClient client = new OkHttpClient();
        String id = getIntent().getStringExtra("ID");
        mealData.setMealID(id);
        mealData.setType("RECIPE");
        mealData.setGenerated(false);
        Request request = new Request.Builder()
                .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/" + id + "/information")
                .get()
                .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "9690bd9213mshba713b8c138f638p1bb84ejsne23754df42f0")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                final String myResponse = response.body().string(); // uncomment this and previous ones then comment the next line

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, Object> hm = new HashMap<>();
                        hm.put("Response", myResponse);
                        try {
                            JSONObject object = new JSONObject(myResponse);
                            String title = object.getString("title");
                            String image = object.getString("image");
                            String instructions = object.getString("instructions");
                            mealData.setImage(image);
                            mealData.setTitle(title);
                            getSupportActionBar().setTitle(title);
                            Glide.with(getBaseContext()).load(image).into(imageView);
                            StepsTV.setText(Html.fromHtml(instructions).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

        request = new Request.Builder()
                .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/" + id + "/nutritionWidget.json")
                .get()
                .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "9690bd9213mshba713b8c138f638p1bb84ejsne23754df42f0")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                final String myResponse = response.body().string(); // uncomment this and previous ones then comment the next line

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, Object> hm = new HashMap<>();
                        hm.put("Response", myResponse);
                        try {
                            JSONObject object = new JSONObject(myResponse);
                            String calories = object.getString("calories");
                            String carbs = object.getString("carbs");
                            String fat = object.getString("fat");
                            String protein = object.getString("protein");
                            CaloriesTV.setText(calories);
                            CarbsTV.setText(carbs);
                            FatTV.setText(fat);
                            ProtienTV.setText(protein);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
    }

    void addToMealPlan(final MealData mealData) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.pop_up_take_food, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(view);
        dialog.show();
        RadioGroup radioGroup = view.findViewById(R.id.RadioGroup);
        final RadioButton[] chosedButton = {view.findViewById(radioGroup.getCheckedRadioButtonId())};
        Button button = view.findViewById(R.id.SubmitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slot = -1;
                switch (chosedButton[0].getId()) {
                    case R.id.opt1:
                        slot = 1;
                        break;
                    case R.id.opt2:
                        slot = 2;
                        break;
                    case R.id.opt3:
                        slot = 3;
                        break;
                    case R.id.opt4:
                        slot = 4;
                        break;
                }
                mealData.setSlot(slot);


                HashMap<String, Object> hm = new HashMap<>();
                hm.put("MealID", mealData.getMealID());
                hm.put("Type", mealData.getType());
                hm.put("Image", mealData.getImage());
                hm.put("Title", mealData.getTitle());
                hm.put("isGenerated", mealData.isGenerated());
                hm.put("Date", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                hm.put("Slot", mealData.getSlot());
                db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Food").add(hm).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getBaseContext(), "Added", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        finish();
                    }
                });
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                chosedButton[0] = view.findViewById(checkedId);
            }
        });
    }
}

