package com.example.dev_epicture_2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Menu menu = navigationBar.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        navigationBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.nav_home :
                        break;
                    case R.id.nav_search :
                        intent = new Intent(Home.this, Search.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_add :
                        intent = new Intent(Home.this, Add.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_profile :
                        intent = new Intent(Home.this, Profile.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                return false;
            }
        });
    }
}
