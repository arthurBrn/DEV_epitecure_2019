package com.example.dev_epicture_2019;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginAuthenticator extends AppCompatActivity {

    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_authenticator);

        signInButton = findViewById(R.id.signInButton);

        // Configure sign in to request the user basic ingformation.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Build google sign in client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Will triger the sign in process
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // requestcde is the code sent back by google after the login attempt
        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /**
     * We handle the case where the login is unsuccessfull
     * @param taskCompleted
     */
    private void handleSignInResult(Task<GoogleSignInAccount> taskCompleted)
    {
        try {
            GoogleSignInAccount account = taskCompleted.getResult(ApiException.class);
            Intent redirectHome = new Intent(getApplicationContext(), Home.class);
            startActivity(redirectHome);
        } catch(ApiException e)
        {
            Toast.makeText(getApplicationContext(), "Failed login", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * We handle the case where a user is already logged in.
     */
    @Override
    protected void onStart()
    {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null)
            startActivity(new Intent(getApplicationContext(), Profile.class));
        super.onStart();
    }
}
