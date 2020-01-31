package com.example.dev_epicture_2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends Common{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.change_activity(navigationBar, 0, getApplicationContext());
        overridePendingTransition(0,0);

        Button btn = findViewById(R.id.launchLogin);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginAuthenticator.class);
                startActivity(intent);
            }
        });
    }
}
