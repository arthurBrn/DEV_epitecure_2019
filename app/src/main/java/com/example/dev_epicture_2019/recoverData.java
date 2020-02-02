package com.example.dev_epicture_2019;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class recoverData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_data);

        // We recover the data sent back in the URL
        Uri uri = getIntent().getData();
        TextView str = findViewById(R.id.dataId);
        TextView accessToken = findViewById(R.id.accessToken);
        str.setText(String.valueOf(uri));

        // We cut the the string
        accessToken.setText(cutUrl(String.valueOf(uri)));
    }

    public String cutUrl(String url)
    {
        return (url.substring((url.indexOf('=') + 1), url.indexOf('&')));
    }
}
