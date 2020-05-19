package com.example.fitnesshubdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    private ArrayList<MyTouchListener> mTouchListeners = new ArrayList<>();
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(getBaseContext(), LandingActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPageAdapter vpa = new ViewPageAdapter(getSupportFragmentManager());
        vpa.addFragment(new BodyFragment());
        vpa.addFragment(new RecipeFragment());
        vpa.addFragment(new ExercisesLogFragment());

        viewPager.setAdapter(vpa);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.opt1:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.opt2:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.opt3:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    public void registerTouchListener(MyTouchListener listener) {
        mTouchListeners.add(listener);
    }

    public void unRegisterTouchListener(MyTouchListener listener) {
        mTouchListeners.remove(listener);
    }

    public interface MyTouchListener {
        void onTouchEvent(MotionEvent event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        for (MyTouchListener listener : mTouchListeners) {
            listener.onTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opt1) {
            startActivity(new Intent(getBaseContext(), ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
