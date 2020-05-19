package com.example.fitnesshubdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignupActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 3;
    EditText EmailET, PasswordET, ConfirmPasswordET;
    private FirebaseAuth mAuth;
    String TAG = "SignupActivity";
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        EmailET = findViewById(R.id.Email);
        PasswordET = findViewById(R.id.Password);
        ConfirmPasswordET = findViewById(R.id.ConfirmPassword);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    public void Sign_up(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        String email = EmailET.getText().toString();
        String password = PasswordET.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Log.d(TAG, "createUserWithEmail:success");
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.cancel();
                    }
                });
    }

    public void sign_up_google(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Signing Up...");
            progressDialog.show();
            try {
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
                mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignupActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            } finally {
                progressDialog.cancel();
            }
        }

    }
}
