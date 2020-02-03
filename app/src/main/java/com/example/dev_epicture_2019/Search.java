package com.example.dev_epicture_2019;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Search extends Common {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.change_activity(navigationBar, 1, getApplicationContext());
        overridePendingTransition(0, 0);
    }

    /*public boolean onTouchEvent(MotionEvent touchEvent) {
        swipe(touchEvent);
        if (x1 < x2)
            create_intent(getApplicationContext(), Home.class);
        else
            create_intent(getApplicationContext(), Add.class);
        return false;
    }*/
}
