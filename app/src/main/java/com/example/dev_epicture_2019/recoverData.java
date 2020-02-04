package com.example.dev_epicture_2019;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class recoverData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_data);

        String url = getIntent().getData().toString();
        String access_token = url.substring(url.indexOf('=') + 1, url.indexOf('&'));

        if (access_token != null) {
            Common.setAccesToken(access_token);
            Common.createIntent(this, Home.class);
        } else {
            Common.createIntent(this, LoginAuthenticator.class);
            finish();
        }
    }
}
