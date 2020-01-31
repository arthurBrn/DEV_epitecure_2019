package com.example.dev_epicture_2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends Common{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.change_activity(navigationBar, 0, getApplicationContext());
        overridePendingTransition(0,0);
    }

    public boolean onTouchEvent(MotionEvent touchEvent) {
        swipe(touchEvent);
        if (x1 > x2)
            create_intent(getApplicationContext(), Search.class);
        return false;
    }
}
