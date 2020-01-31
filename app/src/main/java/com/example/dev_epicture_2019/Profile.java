package com.example.dev_epicture_2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends Common {

    GoogleSignInClient mGoogleSignInClient;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.change_activity(navigationBar, 3, getApplicationContext());
        overridePendingTransition(0,0);

        // Recover the intent from after login
        userName = findViewById(R.id.userNameField);

        // recover basic user information ?
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(Profile.this);

        if (acc != null)
        {
            String name = acc.getDisplayName();

            userName.setText(name);
        }
        else {
            userName.setText("couldn't recover data");
        }
    }
}
