package com.example.dev_epicture_2019;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends Common {

    GoogleSignInClient mGoogleSignInClient;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.changeActivity(navigationBar, 3, getApplicationContext());
        overridePendingTransition(0, 0);


    }
}
