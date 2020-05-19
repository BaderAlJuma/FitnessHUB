package com.example.fitnesshubdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {
    ImageView DisplayPictureIV;
    EditText DisplayNameET, AgeET, HeightET, WeightET;
    RadioGroup GenderOptionsRG, GoalOptionsRG, ActivityOptionsRG;
    Uri PictureURL = null;
    final int PICK_IMAGE_REQUEST = 1995;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 3;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        DisplayPictureIV = findViewById(R.id.DisplayPicture);
        DisplayNameET = findViewById(R.id.DisplayName);
        AgeET = findViewById(R.id.Age);
        HeightET = findViewById(R.id.Height);
        WeightET = findViewById(R.id.Weight);
        GenderOptionsRG = findViewById(R.id.GenderOptions);
        GoalOptionsRG = findViewById(R.id.WeightGoalOptions);
        ActivityOptionsRG = findViewById(R.id.ActivityLevelOptions);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        if (mAuth.getUid() == null) {
            //yesno to signout or reauth?
            return;
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        db.collection("Users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if ((documentSnapshot.get("DisplayName") + "").equals("null")) {
                    return;
                }
                if (!(documentSnapshot.get("DisplayPicture") + "").equals("null")) {
                    Glide.with(getBaseContext()).load(documentSnapshot.get("DisplayPicture") + "").into(DisplayPictureIV);
                }
                DisplayNameET.setText("" + documentSnapshot.get("DisplayName"));
                AgeET.setText("" + documentSnapshot.get("Age"));
                HeightET.setText("" + documentSnapshot.get("Height"));
                WeightET.setText("" + documentSnapshot.get("Weight"));

                String gender = documentSnapshot.get("Gender") + "";
                String weight = documentSnapshot.get("WeightGoal") + "";
                String activity = documentSnapshot.get("ActivityLevel") + "";
                if (gender.equals("Male")) {
                    GenderOptionsRG.check(R.id.MaleOption);
                } else {
                    GenderOptionsRG.check(R.id.FemaleOption);
                }

                if (weight.equals("Lose")) {
                    GoalOptionsRG.check(R.id.LoseOption);
                } else if (weight.equals("Maintain")) {
                    GoalOptionsRG.check(R.id.MaintainOption);
                } else if (weight.equals("Gain")) {
                    GoalOptionsRG.check(R.id.GainOption);
                }

                if (activity.equals("Beginner")) {
                    ActivityOptionsRG.check(R.id.BeginnerOption);
                } else if (activity.equals("Intermediate")) {
                    ActivityOptionsRG.check(R.id.IntermediateOption);
                } else if (activity.equals("Advanced")) {
                    ActivityOptionsRG.check(R.id.AdvancedOption);
                }
                ExercisesListActivity.myActivityLevel = activity;

            }
        });
    }

    public void uploadPicture(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            PictureURL = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), PictureURL);
                DisplayPictureIV.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
                mAuth.getCurrentUser().linkWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }


    public void submitProfile(View view) {
        if (DisplayNameET.getText().toString().isEmpty() ||
                AgeET.getText().toString().isEmpty() ||
                HeightET.getText().toString().isEmpty() ||
                WeightET.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }


        if (PictureURL == null) {
            //TODO
            uploadProfile(null);
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(PictureURL)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getBaseContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                    String image = task.getResult().toString();
                                    uploadProfile(image);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }

    }

    void uploadProfile(String image) {

        RadioButton gender = findViewById(GenderOptionsRG.getCheckedRadioButtonId());
        RadioButton goal = findViewById(GoalOptionsRG.getCheckedRadioButtonId());
        RadioButton activity = findViewById(ActivityOptionsRG.getCheckedRadioButtonId());


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("DisplayName", DisplayNameET.getText().toString());
        hashMap.put("Age", AgeET.getText().toString());
        hashMap.put("Weight", WeightET.getText().toString());
        hashMap.put("Height", HeightET.getText().toString());
        hashMap.put("Gender", gender.getText().toString());
        hashMap.put("WeightGoal", goal.getText().toString());
        hashMap.put("ActivityLevel", activity.getText().toString());
        if (image != null) {
            hashMap.put("DisplayPicture", image);
        }
        db.collection("Users").document(mAuth.getUid()).set(hashMap, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    public void signout() {
        mAuth.signOut();
        Intent intent = new Intent(getBaseContext(), LandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opt1) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else if (item.getItemId() == R.id.opt2) {
            signout();
        }
        return super.onOptionsItemSelected(item);
    }
}
