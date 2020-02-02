package com.example.dev_epicture_2019;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class  Common extends AppCompatActivity {

    private String callBackUrl;
    static protected float x1;
    static protected float x2;


    public String getCallBackUrl()
    {
        return (callBackUrl);
    }

    public void setCallBackUrl(String newCallBackUrl)
    {
        callBackUrl = newCallBackUrl;
    }

    public String recoverData()
    {
        String accessToken = "";

        Uri uri = getIntent().getData();
        accessToken = recoverAccessTokenFromUrl(String.valueOf(uri));
        return (accessToken);
    }

    public String recoverAccessTokenFromUrl(String currentCallBackUrl)
    {
        String accessToken = "";

        if (!(currentCallBackUrl.isEmpty()))
            accessToken = currentCallBackUrl.substring((currentCallBackUrl.indexOf('=') + 1), currentCallBackUrl.indexOf('&'));
        return (accessToken);
    }

    static private void select_current_activity(BottomNavigationView navigationBar, int index) {
        Menu menu = navigationBar.getMenu();
        MenuItem menuItem = menu.getItem(index);
        menuItem.setChecked(true);
    }

    static public void create_intent(Context mContext, Class C) {
        Intent intent = new Intent(mContext, C);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    static public void change_activity(BottomNavigationView navigationBar, int index, Context mContext) {
        select_current_activity(navigationBar, index);
        navigationBar.setOnNavigationItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.nav_home :
                        create_intent(mContext, Home.class);
                        break;
                    case R.id.nav_search :
                        create_intent(mContext, Search.class);
                        break;
                    case R.id.nav_add :
                        create_intent(mContext, Add.class);
                        break;
                    case R.id.nav_profile :
                        create_intent(mContext, Profile.class);
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
}
