package com.example.fitnesshubdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SearchRecipeActivity extends AppCompatActivity {
    EditText QueryEditText;
    ImageButton SearchButton;
    ListView listView;
    ArrayList<MealData> arrayList;
    RecipesAdapter adapter;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        QueryEditText = findViewById(R.id.QueryEditText);
        SearchButton = findViewById(R.id.SearchButton);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchButton();
            }
        });
        arrayList = new ArrayList<>();
        listView = findViewById(R.id.Listview);
        adapter = new RecipesAdapter(SearchRecipeActivity.this, arrayList);
        listView.setAdapter(adapter);
    }

    private void SearchButton() {
        String query = QueryEditText.getText().toString();
        final OkHttpClient client = new OkHttpClient();
        String exclude = "Pork, Alcohol";

        Request request = new Request.Builder()
                .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/search?excludeIngredients=" + exclude + "&query=" + query)
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
//        final String myResponse = "{\"results\":[{\"id\":185877,\"title\":\"Apple-Date Compote with Apple-Cider Yogurt Cheese\",\"readyInMinutes\":45,\"servings\":6,\"sourceUrl\":\"http://www.epicurious.com/recipes/food/views/Apple-Date-Compote-with-Apple-Cider-Yogurt-Cheese-5696\",\"openLicense\":0,\"image\":\"Apple-Date-Compote-with-Apple-Cider-Yogurt-Cheese-185877.jpg\"},{\"id\":515137,\"title\":\"Apple Party! From the Vault – Apple Craisin Bread Pudding\",\"readyInMinutes\":25,\"servings\":10,\"sourceUrl\":\"http://www.cafeterrablog.com/2012/09/29/apple-party-from-the-vault-apple-craisin-bread-pudding/\",\"openLicense\":0,\"image\":\"Apple-Party-From-the-Vault--Apple-Craisin-Bread-Pudding-515137.jpg\"},{\"id\":616320,\"title\":\"Spiced Apple Muffins with Apple Cinnamon Glaze\",\"readyInMinutes\":20,\"servings\":12,\"sourceUrl\":\"http://www.brighteyedbaker.com/2014/10/07/spiced-apple-muffins-with-apple-cinnamon-glaze/\",\"openLicense\":0,\"image\":\"spiced-apple-muffins-with-apple-cinnamon-glaze-616320.jpg\"},{\"id\":822282,\"title\":\"Apple-Cardamom Cakes with Apple Cider Icing\",\"readyInMinutes\":60,\"servings\":24,\"sourceUrl\":\"http://www.delish.com/cooking/recipe-ideas/recipes/a23995/apple-cardamom-cakes-cider-icing/\",\"openLicense\":0,\"image\":\"apple-cardamom-cakes-with-apple-cider-icing-822282.jpg\"},{\"id\":47800,\"title\":\"Apple Tart\",\"readyInMinutes\":45,\"servings\":12,\"sourceUrl\":\"http://www.marthastewart.com/334356/apple-tart\",\"openLicense\":0,\"image\":\"apple-tart-2-47800.jpg\"},{\"id\":123878,\"title\":\"Apple Stuffing\",\"readyInMinutes\":40,\"servings\":8,\"sourceUrl\":\"http://www.food.com/recipe/apple-stuffing-258808\",\"openLicense\":0,\"image\":\"apple_stuffing-123878.jpg\"},{\"id\":271929,\"title\":\"Apple Torte\",\"readyInMinutes\":245,\"servings\":12,\"sourceUrl\":\"http://www.kraftrecipes.com/recipes/apple-torte-50146.aspx\",\"openLicense\":0,\"image\":\"apple-torte-271929.jpg\"},{\"id\":280944,\"title\":\"Apple Fritters\",\"readyInMinutes\":45,\"servings\":36,\"sourceUrl\":\"http://www.myrecipes.com/recipe/apple-fritters-10000000257834/\",\"openLicense\":0,\"image\":\"apple-fritters-280944.jpg\"},{\"id\":295615,\"title\":\"Apple Salsa\",\"readyInMinutes\":15,\"servings\":15,\"sourceUrl\":\"http://www.myrecipes.com/recipe/apple-salsa-50400000133882/\",\"openLicense\":0,\"image\":\"apple-salsa-295615.jpg\"},{\"id\":301595,\"title\":\"Apple Tart\",\"readyInMinutes\":115,\"servings\":6,\"sourceUrl\":\"http://www.foodnetwork.com/recipes/paula-deen/apple-tart-recipe.html\",\"openLicense\":0,\"image\":\"apple-tart-301595.jpeg\"}],\"baseUri\":\"https://spoonacular.com/recipeImages/\",\"offset\":0,\"number\":10,\"totalResults\":853,\"processingTimeMs\":544,\"expires\":1588622437929,\"isStale\":false}\n";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("Response", myResponse);
                try {
                    JSONObject object = new JSONObject(myResponse);
                    JSONArray array = object.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        String title = array.getJSONObject(i).getString("title");
                        String id = array.getJSONObject(i).getString("id");
                        String image = "https://spoonacular.com/recipeImages/" + array.getJSONObject(i).getString("image");
                        MealData data = new MealData();
                        data.setGenerated(false);
                        data.setImage(image);
                        data.setMealID(id);
                        data.setType("RECIPE");
                        data.setTitle(title);
                        arrayList.add(data);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
            }

        });

        request = new Request.Builder()
                .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/food/ingredients/autocomplete?number=20&query=" + query)
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

                final String myResponse2 = response.body().string(); // uncomment this and previous ones then comment the next line
//        final String myResponse2 = "[{\"name\":\"apple\",\"image\":\"apple.jpg\"},{\"name\":\"applesauce\",\"image\":\"applesauce.png\"},{\"name\":\"apple juice\",\"image\":\"apple-juice.jpg\"},{\"name\":\"apple cider\",\"image\":\"apple-cider.jpg\"},{\"name\":\"apple jelly\",\"image\":\"apple-jelly.jpg\"},{\"name\":\"apple butter\",\"image\":\"apple-jelly.jpg\"},{\"name\":\"apple pie spice\",\"image\":\"garam-masala.jpg\"},{\"name\":\"apple pie filling\",\"image\":\"apple-pie-slice.jpg\"},{\"name\":\"apple cider vinegar\",\"image\":\"apple-cider-vinegar.jpg\"},{\"name\":\"applewood smoked bacon\",\"image\":\"raw-bacon.png\"},{\"name\":\"scrapple\",\"image\":\"spam.png\"},{\"name\":\"pineapple\",\"image\":\"pineapple.jpg\"},{\"name\":\"red apple\",\"image\":\"red-delicious-apples.png\"},{\"name\":\"pineapple juice\",\"image\":\"pineapple-juice.jpg\"},{\"name\":\"pineapple rings\",\"image\":\"pineapple-rings.png\"},{\"name\":\"pineapple chunks\",\"image\":\"pineapple-with-can.png\"},{\"name\":\"pineapple tidbits\",\"image\":\"pineapple-with-can.png\"},{\"name\":\"pineapple in juice\",\"image\":\"pineapple-with-can.png\"},{\"name\":\"pineapple preserves\",\"image\":\"pineapple-preserves.jpg\"},{\"name\":\"tart apple\",\"image\":\"grannysmith-apple.png\"}]\n";

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, Object> hm = new HashMap<>();
                        hm.put("Response", myResponse2);
                        try {
                            JSONArray array = new JSONArray(myResponse2);
                            for (int i = 0; i < array.length(); i++) {
                                String title = array.getJSONObject(i).getString("name");
                                String image = "https://spoonacular.com/cdn/ingredients_100x100/" + array.getJSONObject(i).getString("image");
                                MealData data = new MealData();
                                data.setGenerated(false);
                                data.setImage(image);
                                data.setTitle(title);
                                data.setType("INGREDIENT");
                                arrayList.add(data);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
    }


            public class RecipesAdapter extends ArrayAdapter<MealData> {

                private final Activity context;
                private final ArrayList<MealData> arrayList;

                public RecipesAdapter(Activity context, ArrayList<MealData> arrayList) {
                    super(context, R.layout.row_search_recipe, arrayList);
                    this.arrayList = arrayList;
                    this.context = context;

                }

                public View getView(final int position, View view, ViewGroup parent) {
                    LayoutInflater inflater = context.getLayoutInflater();
                    view = inflater.inflate(R.layout.row_search_recipe, null, true);
                    TextView TitleText = view.findViewById(R.id.titleTV);
                    TextView addButton = view.findViewById(R.id.addButton);
                    ImageView imageView = view.findViewById(R.id.imageView);
                    TitleText.setText(arrayList.get(position).getTitle());
                    Glide.with(getBaseContext()).load(arrayList.get(position).getImage()).into(imageView);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (arrayList.get(position).getType().equals("RECIPE")) {
                                Intent intent = new Intent(context, ViewRecipeDetails.class);
                                intent.putExtra("ID", arrayList.get(position).getMealID());
                                startActivity(intent);
                            } else {
                                Toast.makeText(SearchRecipeActivity.this, "Information not available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addToMealPlan(arrayList.get(position));
                        }
                    });
                    return view;
                }

                private void addToMealPlan(final MealData mealData) {
                    LayoutInflater factory = LayoutInflater.from(context);
                    final View view = factory.inflate(R.layout.pop_up_take_food, null);
                    final AlertDialog dialog = new AlertDialog.Builder(context).create();
                    dialog.setView(view);
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
                                    Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
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
                    dialog.show();
                }
            }

        }
