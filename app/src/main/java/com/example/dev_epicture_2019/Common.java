package com.example.dev_epicture_2019;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Common extends AppCompatActivity {

    static protected float x1;
    static protected float x2;
    static protected String accesToken;
    static protected String ApiResponse;

    static private void select_current_activity(BottomNavigationView navigationBar, int index) {
        Menu menu = navigationBar.getMenu();
        MenuItem menuItem = menu.getItem(index);
        menuItem.setChecked(true);
    }

    static public void createIntent(Context mContext, Class C) {
        Intent intent = new Intent(mContext, C);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    static public void changeActivity(BottomNavigationView navigationBar, int index, Context mContext) {
        select_current_activity(navigationBar, index);
        navigationBar.setOnNavigationItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.nav_home :
                        createIntent(mContext, Home.class);
                        break;
                    case R.id.nav_search :
                        createIntent(mContext, Search.class);
                        break;
                    case R.id.nav_add :
                        createIntent(mContext, Add.class);
                        break;
                    case R.id.nav_profile :
                        createIntent(mContext, Profile.class);
                        break;
                }
                return false;
            });
    }

    static public void swipe(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                break;
        }
    }

    static public void setAccesToken(String macces_token) {
        accesToken = macces_token;
    }

    static public String getAccesToken() {
        return accesToken;
    }

    static public String getApiResponse() { return ApiResponse; }

    static public void setApiResponse(String myResponse) { ApiResponse = myResponse; }
}
