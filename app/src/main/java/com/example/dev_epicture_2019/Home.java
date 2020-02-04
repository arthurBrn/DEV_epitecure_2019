package com.example.dev_epicture_2019;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends Common{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView txt = findViewById(R.id.txt_home);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.changeActivity(navigationBar, 0, getApplicationContext());
        overridePendingTransition(0,0);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/account/me")
                .method("GET", null)
                .addHeader("Authorization", getAccesToken())
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

    }

    /*public boolean onTouchEvent(MotionEvent touchEvent) {
        swipe(touchEvent);
        if (x1 > x2)
            createIntent(getApplicationContext(), Search.class);
        return false;
    }*/
}
