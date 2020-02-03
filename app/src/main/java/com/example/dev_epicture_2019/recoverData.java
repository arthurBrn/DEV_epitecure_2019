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
        TextView str = findViewById(R.id.dataId);
        String reg = "^[a-z0-9]+$";
        String access = url.substring(url.indexOf('=') + 1, url.indexOf('&'));

        if (Pattern.matches(reg, access)) {
            Common.create_intent(this, Home.class);
            finish();
        } else {
            Common.create_intent(this, LoginAuthenticator.class);
            finish();
        }
    }
}
