package com.example.fitnesshubdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class LandingActivity extends AppCompatActivity {

    ViewPager viewPager;
    DotsIndicator dotsIndicator;
    ViewPageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        viewPager = findViewById(R.id.ViewPager);
        dotsIndicator = findViewById(R.id.dots_indicator);
        adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new LandingPageImageFragment(R.drawable.landing_page_1));
        adapter.addFragment(new LandingPageImageFragment(R.drawable.img2));
        adapter.addFragment(new LandingPageImageFragment(R.drawable.img3));
        adapter.addFragment(new LandingPageImageFragment(R.drawable.img4));
        adapter.addFragment(new LandingPageImageFragment(R.drawable.img5));
        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager(viewPager);

    }

    public void Sign_in(View view) {
        Intent intent = new Intent(getBaseContext(), SigninActivity.class);
        startActivity(intent);
    }

    public void sign_up(View view) {
        Intent intent = new Intent(getBaseContext(), SignupActivity.class);
        startActivity(intent);
    }

    public void Guest(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });
    }
}
