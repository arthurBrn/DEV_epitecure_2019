package com.example.dev_epicture_2019;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Callback;
import okhttp3.Call;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends Common {

    OkHttpClient httpClient;
    String token = getAccesToken();
    String profilRequestUrl = "https://api.imgur.com/3/account/me";
    // Textviews here
    // userimge idUserImage
    ImageView useravatar = (ImageView) findViewById(R.id.idUserImage);
    TextView username = findViewById(R.id.idUserName);
    TextView userbio = findViewById(R.id.idUserBio);
    TextView userrep = findViewById(R.id.idUserReputation);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.changeActivity(navigationBar, 3, getApplicationContext());
        overridePendingTransition(0, 0);

        //fetchProfilData();
    }

    /*public void fetchProfilData()
    {
        httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(profilRequestUrl)
                .method("GET", null)
                .header("Authorzation", "Bearer " + token)
                .header("User-agent", "DEV_epicture_2019")
                .build();
        // enqueue run the request in a background thread
        httpClient.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {e.printStackTrace();}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    // We verify that the server give us what we want
                    if (response.isSuccessful()) {
                        JSONObject data = new JSONObject(response.body().string());
                        //JSONArray items = data.getJSONArray("data");
                        //JSONObject item = items.getJSONObject(0);


                        //UserFactory usr = UserFactory.createUser(item.getString("url"), item.getString("bio"), item.getString("avatar"), item.getString("reputation"));
                        //runOnUiThread(() -> render(usr));
                        Profile.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                username.setText("ol");
                                userbio.setText("ola");
                                userrep.setText("olala");
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

    public void render(UserFactory usr)
    {

    }

}
