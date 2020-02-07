package com.example.dev_epicture_2019;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        String id = intent.getStringExtra("galeryId");
        TextView txt = findViewById(R.id.id_image);
        txt.setText(id);
    }
}
