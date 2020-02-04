package com.example.dev_epicture_2019;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class recoverData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_data);

        String url = getIntent().getData().toString();
        String access_token = url.substring(url.indexOf('=') + 1, url.indexOf('&'));

        if (access_token != null) {
            Common.set_acces_token(access_token);
            Common.create_intent(this, Home.class);
        } else {
            Common.create_intent(this, LoginAuthenticator.class);
            finish();
        }
    }
}
