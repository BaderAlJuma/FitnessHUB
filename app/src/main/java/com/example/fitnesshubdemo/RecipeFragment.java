package com.example.fitnesshubdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RecipeFragment extends Fragment {
    ListView listView;
    private ArrayList<MealData> fullArrayList, arrayList;
    RecipesAdapter adapter;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    HashMap<String, ArrayList<GeneratedMealPlanData>> hashMap;
    int currentDay = 1;
    FloatingActionButton FloatingMenuButton, GeneratePlanFAB, searchFAB;
    boolean isOpen = false;
    Date CurrentDate;
    TextView DayTitleTV, BeforeButton, AfterButton, SearchTextView, GenerateTextView;
    private boolean comingFromSearch = false;
    int Calories = 2000;

    public RecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        SearchTextView = view.findViewById(R.id.SearchTextView);
        GenerateTextView = view.findViewById(R.id.GenerateTextView);
        CurrentDate = new Date();
        hashMap = new HashMap<>();
        DayTitleTV = view.findViewById(R.id.CurrentShowingDayTextView);
        listView = view.findViewById(R.id.Listview);
        fullArrayList = new ArrayList<>();
        arrayList = new ArrayList<>();
        adapter = new RecipesAdapter(getActivity(), arrayList);
        listView.setAdapter(adapter);
        FloatingMenuButton = view.findViewById(R.id.FloatingActionButton);
        GeneratePlanFAB = view.findViewById(R.id.FloatingActionButton2);
        searchFAB = view.findViewById(R.id.FloatingActionButton3);
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
        FloatingMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlFloatingButton();
            }
        });
        GeneratePlanFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlFloatingButton();
                generateMealPlan();
            }
        });
        searchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlFloatingButton();
                startActivity(new Intent(getContext(), SearchRecipeActivity.class));
                comingFromSearch = true;
            }
        });
        loadDB();
        return view;
    }

    private void controlFloatingButton() {
        if (isOpen) {
            GeneratePlanFAB.setVisibility(View.GONE);
            searchFAB.setVisibility(View.GONE);
            SearchTextView.setVisibility(View.GONE);
            GenerateTextView.setVisibility(View.GONE);
        } else {
            GeneratePlanFAB.setVisibility(View.VISIBLE);
            searchFAB.setVisibility(View.VISIBLE);
            SearchTextView.setVisibility(View.VISIBLE);
            GenerateTextView.setVisibility(View.VISIBLE);
        }
        isOpen = !isOpen;
    }

    private void loadDB() {
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!(documentSnapshot.get("Age") + "").equals("null")) {
                    int age = Integer.parseInt(documentSnapshot.get("Age") + "");
                    int height = Integer.parseInt(documentSnapshot.get("Height") + "");
                    int weight = Integer.parseInt(documentSnapshot.get("Weight") + "");
                    String gender = documentSnapshot.get("Gender") + "";
                    String weightGoal = documentSnapshot.get("WeightGoal") + "";
                    String activityLevel = documentSnapshot.get("ActivityLevel") + "";
                    if (gender.equals("Male")) {
                        Calories = (int) ((10 * weight) + (6.25 * height) - (5 * age) + 5);
                    } else {
                        Calories = (int) ((10 * weight) + (6.25 * height) - (5 * age) - 161);
                    }
                    if (weightGoal.equals("Lose")) {
                        Calories -= 300;
                    } else if (weightGoal.equals("Gain")) {
                        Calories += 300;
                    }
                    if (activityLevel.equals("Intermediate")) {
                        Calories += 200;
                    } else if (activityLevel.equals("Advanced")) {
                        Calories += 500;
                    }
                } else {
                    Calories = 2000;
                }
            }
        });
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Food").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                fullArrayList.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    MealData data = new MealData();
                    data.setDocumentID(documentSnapshot.getId());
                    data.setMealID(documentSnapshot.get("MealID") + "");
                    data.setType(documentSnapshot.get("Type") + "");
                    data.setImage(documentSnapshot.get("Image") + "");
                    data.setTitle(documentSnapshot.get("Title") + "");
                    data.setGenerated(documentSnapshot.getBoolean("isGenerated"));
                    data.setDate(documentSnapshot.get("Date") + "");
                    data.setSlot(Integer.parseInt(documentSnapshot.get("Slot") + ""));
                    fullArrayList.add(data);
                }
                loadData();

            }
        });
    }


    private void loadData() {
        arrayList.clear();
        for (MealData data : fullArrayList) {
            if (formatDate(CurrentDate).equals(data.getDate())) {
                arrayList.add(data);
            }
        }
        Collections.sort(arrayList);
        adapter.notifyDataSetChanged();
    }


    int counter = 0;

    private void generateMealPlan() {
        if (hashMap.isEmpty() || currentDay == 7) {
            for (int i = 1; i <= 7; i++) {
                hashMap.put("" + i, new ArrayList<GeneratedMealPlanData>());
            }
            currentDay = 1;
            final OkHttpClient client = new OkHttpClient();
            String timeFrame = "week";
            String dietType = "";
            String exclude = "Pork, Alcohol";
            Request request = new Request.Builder()
                    .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/mealplans/generate?targetCalories=" + Calories + "&timeFrame=" + timeFrame + "&&exclude=" + exclude + "%252C%20olives%22&includeNutrition=true")
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

                    final String myResponse = response.body().string();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(myResponse);
                                JSONArray array = object.getJSONArray("items");
                                for (int i = 0; i < array.length(); i++) {
                                    String day = array.getJSONObject(i).getString("day");
                                    String value = array.getJSONObject(i).getString("value");
                                    String slot = array.getJSONObject(i).getString("slot");
                                    String type = array.getJSONObject(i).getString("type");
                                    JSONObject json = new JSONObject(value);
                                    String id = json.getString("id");
                                    String imageType = json.getString("imageType");
                                    String title = json.getString("title");
                                    GeneratedMealPlanData data = new GeneratedMealPlanData();
                                    data.setDay(day);
                                    data.setId(id);
                                    data.setImagetype(imageType);
                                    data.setSlot(Integer.parseInt(slot));
                                    data.setTitle(title);
                                    data.setType(type);
                                    hashMap.get(day).add(data);
                                }
                                generateMealPlan();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            });
        } else {
            final ArrayList<GeneratedMealPlanData> currentMealPlan = hashMap.get(currentDay + "");
            for (MealData data : fullArrayList) {
                if (data.isGenerated()) {
                    db.collection("Users").document(mAuth.getCurrentUser().getUid())
                            .collection("Food").document(data.getDocumentID()).delete();
                }
            }
            for (GeneratedMealPlanData data : currentMealPlan) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("MealID", data.getId());
                hm.put("Type", data.getType());
                hm.put("Image", "https://spoonacular.com/recipeImages/" + data.getId() + "-90x90." + data.getImagetype());
                hm.put("Title", data.getTitle());
                hm.put("isGenerated", true);
                hm.put("Date", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                hm.put("Slot", data.getSlot());
                db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Food").add(hm).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        counter++;
                        if (counter == currentMealPlan.size()) {
                            loadDB();
                            currentDay++;
                            counter = 0;
                        }
                    }
                });
            }
        }

    }


    void dontDelete() {
        db.collection("Response").document("Weekly plan").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String myResponse = documentSnapshot.get("Response") + "";
                try {
                    JSONObject object = new JSONObject(myResponse);
                    JSONArray array = object.getJSONArray("items");
                    for (int i = 0; i < array.length(); i++) {
                        String day = array.getJSONObject(i).getString("day");
                        String value = array.getJSONObject(i).getString("value");
                        String slot = array.getJSONObject(i).getString("slot");
                        String type = array.getJSONObject(i).getString("type");
                        JSONObject json = new JSONObject(value);
                        String id = json.getString("id");
                        String imageType = json.getString("imageType");
                        String title = json.getString("title");
                        GeneratedMealPlanData data = new GeneratedMealPlanData();
                        data.setDay(day);
                        data.setId(id);
                        data.setImagetype(imageType);
                        data.setSlot(Integer.parseInt(slot));
                        data.setTitle(title);
                        data.setType(type);
                        hashMap.get(day).add(data);
                    }
                    generateMealPlan();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);
    }

    private Date modifyDate(Date date, int value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, value);
        return cal.getTime();
    }

    public class RecipesAdapter extends ArrayAdapter<MealData> {

        private final Activity context;
        private final ArrayList<MealData> arrayList;

        public RecipesAdapter(Activity context, ArrayList<MealData> arrayList) {
            super(context, R.layout.row_recipe, arrayList);
            this.arrayList = arrayList;
            this.context = context;

        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.row_recipe, null, true);
            if (position == 0 || arrayList.get(position).getSlot() != arrayList.get(position - 1).getSlot()) {
                TextView topTitle = view.findViewById(R.id.TopTitle);
                topTitle.setVisibility(View.VISIBLE);
                int slot = arrayList.get(position).getSlot();
                switch (slot) {
                    case 1:
                        topTitle.setText("Breakfast");
                        break;
                    case 2:
                        topTitle.setText("Lunch");
                        break;
                    case 3:
                        topTitle.setText("Dinner");
                        break;
                    case 4:
                        topTitle.setText("Snack");
                        break;
                }

            }


            TextView TitleText = view.findViewById(R.id.titleTV);
            ImageView imageView = view.findViewById(R.id.imageView);
            TitleText.setText(arrayList.get(position).getTitle());
            Glide.with(getContext()).load(arrayList.get(position).getImage()).into(imageView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ViewRecipeDetails.class);
                    intent.putExtra("ID", arrayList.get(position).getMealID());
                    startActivity(intent);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/" + arrayList.get(position).getMealID() + "/similar")
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

                                            final String myResponse = response.body().string();

                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        final MealData previousMeal = arrayList.get(position);
                                                        JSONArray array = new JSONArray(myResponse);
                                                        JSONObject jsonObject = array.getJSONObject(1);
                                                        int slot = previousMeal.getSlot();
                                                        String type = previousMeal.getType();
                                                        String id = jsonObject.getString("id");
                                                        String title = jsonObject.getString("title");
                                                        String image = "https://spoonacular.com/recipeImages/" + title.toLowerCase().replace(" ", "-") + "-" + id + "." + jsonObject.getString("imageType");
                                                        final MealData data = new MealData();
                                                        data.setMealID(id);
                                                        data.setImage(image);
                                                        data.setSlot(slot);
                                                        data.setTitle(title);
                                                        data.setType(type);
                                                        data.setDate(previousMeal.getDate());
                                                        fullArrayList.remove(previousMeal);
                                                        db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Food").document(previousMeal.getDocumentID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                HashMap<String, Object> hm = new HashMap<>();
                                                                hm.put("MealID", data.getMealID());
                                                                hm.put("Type", data.getType());
                                                                hm.put("Image", data.getImage());
                                                                hm.put("Title", data.getTitle());
                                                                hm.put("isGenerated", true);
                                                                hm.put("Date", data.getDate());
                                                                hm.put("Slot", data.getSlot());
                                                                db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Food").add(hm).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        loadDB();
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }

                                    });

                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Food")
                                            .document(arrayList.get(position).getDocumentID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            arrayList.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    });
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("You can find similar recipe").setPositiveButton("Similar", dialogClickListener)
                            .setNegativeButton("Delete", dialogClickListener).show();

                    return true;
                }
            });
            return view;

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (comingFromSearch) {
            loadDB();
            comingFromSearch = false;
        }
    }
}
