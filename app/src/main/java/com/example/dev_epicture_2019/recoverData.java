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

public class recoverData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_data);

        String url = getIntent().getData().toString();
        TextView str = findViewById(R.id.dataId);
        TextView accessToken = findViewById(R.id.accessToken);
        str.setText(url);

        String access = url.substring(url.indexOf('=') + 1, url.indexOf('&'));
        str.setText(access);

        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}
