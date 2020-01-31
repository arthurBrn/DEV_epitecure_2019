package com.example.dev_epicture_2019;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import okhttp3.OkHttpClient;

public class Home extends Common{

    private OkHttpClient httpclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.change_activity(navigationBar, 0, getApplicationContext());
        overridePendingTransition(0,0);
    }
}
