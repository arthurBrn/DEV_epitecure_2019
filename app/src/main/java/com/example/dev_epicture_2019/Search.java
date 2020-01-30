package com.example.dev_epicture_2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Menu menu = navigationBar.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        navigationBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Intent intent = null;
                        switch (item.getItemId()) {
                            case R.id.nav_home :
                                intent = new Intent(Search.this, Home.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_search :
                                break;
                            case R.id.nav_add :
                                intent = new Intent(Search.this, Add.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_profile :
                                intent = new Intent(Search.this, Profile.class);
                                startActivity(intent);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + item.getItemId());
                        }
                        return false;
                    }
                });
    }
}
