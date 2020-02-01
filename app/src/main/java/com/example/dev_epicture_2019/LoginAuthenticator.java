package com.example.dev_epicture_2019;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.net.URI;

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
        String finalUrl = url + "?client_id=" + clientId + "&response_type=" + "token";
        Intent conn = new Intent(Intent.ACTION_VIEW);
        conn.setData(Uri.parse(finalUrl));

        Button btn = findViewById(R.id.con_btn);

        str = findViewById(R.id.loginAvant);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(conn);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        // The following line will return "appSchema://appName.com".
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith("epicture")){
            String access_token = uri.getQueryParameter("access_token");
            str.setText(access_token);
        }
        //str.setText("BOUYA");
        //Intent in = new Intent(getApplicationContext(), Home.class);
        //startActivity(in);
    }
}
