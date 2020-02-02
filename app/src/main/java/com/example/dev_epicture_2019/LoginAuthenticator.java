package com.example.dev_epicture_2019;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginAuthenticator extends AppCompatActivity {

    public final String clientId= "7fb2f979f6fbb0b";
    public final String clientSecret= "34bc17179c221f5d640de7615bc8ffcc7f6bb2dc";
    public final String callBack= "epicture://imgur";


    TextView str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_authenticator);


        String url = "https://api.imgur.com/oauth2/authorize";
        String apiUrl = url + "?client_id=" + clientId + "&response_type=" + "token";
        Intent connectToApi = new Intent(Intent.ACTION_VIEW);
        connectToApi.setData(Uri.parse(apiUrl));

        Button btn = findViewById(R.id.connexionButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(connectToApi);
            }
        });
    }
}
