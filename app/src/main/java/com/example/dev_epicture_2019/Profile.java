package com.example.dev_epicture_2019;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Callback;
import okhttp3.Call;
import okhttp3.Response;
import org.json.JSONObject;

public class Profile extends Common {

    final String accountRequestUrl = "https://api.imgur.com/3/account/me";
    OkHttpClient httpClient;
    String token;
    ImageView useravatar;
    TextView username;
    TextView userbio;
    TextView userrep;
    UserFactory usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.changeActivity(navigationBar, 3, getApplicationContext());
        overridePendingTransition(0, 0);

        useravatar = (ImageView) findViewById(R.id.idUserImage);
        username = findViewById(R.id.idUserName);
        userbio = findViewById(R.id.idUserBio);
        userrep = findViewById(R.id.idUserReputation);
        token = getAccesToken();
        username = findViewById(R.id.idUserName);

        fetchProfilData();
    }

    public void fetchProfilData() {
        httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/account/me")
                .method("GET", null)
                .header("Authorization", "Bearer " + token)
                .header("User-agent", "DEV_epicture_2019")
                .build();
        // enqueue run the request in a background thread
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject data = new JSONObject(response.body().string());
                    JSONObject sndobj = data.getJSONObject("data");
                    String st = sndobj.getString("url");
                    usr = UserFactory.createUser(sndobj.getString("url"), sndobj.getString("bio"), sndobj.getString("avatar"), sndobj.getString("reputation"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            username.setText(usr.getUserUrl());
                            if (usr.getUserBio() != null)
                                userbio.setText(usr.getUserBio());
                            else
                                userbio.setText(" ");
                            userrep.setText(usr.getUserReputation());
                            Picasso.get().load(usr.getUserAvatar()).centerCrop() .resize(300,300).into(useravatar);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void setProfilHeader(UserFactory usr) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                username.setText(usr.getUserUrl());
            }
        });
    }
}


