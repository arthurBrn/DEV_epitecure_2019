package com.example.dev_epicture_2019;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Set;

public class recoverData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_data);

        Uri uri = getIntent().getData();
        TextView str = findViewById(R.id.dataId);
        TextView accessToken = findViewById(R.id.accessToken);
        str.setText(String.valueOf(uri));
        accessToken.setText(String.valueOf(uri.getQueryParameters("access_token")));


        Uri newUri = Uri.parse(String.valueOf(uri));


        String protocol = newUri.getScheme();
        String server = newUri.getAuthority();


        Set<String> args = newUri.getQueryParameterNames();
        String limit = newUri.getQueryParameter("refresh_token");


        accessToken.setText("");
    }
}
