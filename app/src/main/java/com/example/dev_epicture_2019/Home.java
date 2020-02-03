package com.example.dev_epicture_2019;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends Common{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.change_activity(navigationBar, 0, getApplicationContext());
        overridePendingTransition(0,0);
        Bundle extras = getIntent().getExtras();
        String acces_token = extras.getString("access_token");
        super.acces_token = acces_token;
    }

    /*public boolean onTouchEvent(MotionEvent touchEvent) {
        swipe(touchEvent);
        if (x1 > x2)
            create_intent(getApplicationContext(), Search.class);
        return false;
    }*/
}
