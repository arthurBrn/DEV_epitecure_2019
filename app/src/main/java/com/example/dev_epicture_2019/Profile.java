package com.example.dev_epicture_2019;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Callback;
import okhttp3.Call;
import okhttp3.Response;

import org.json.JSONObject;

public class Profile extends Common {

    final String accountRequestUrl = "https://api.imgur.com/3/account/me";
    String urlRecoverMeImages = "https://api.imgur.com/3/account/me/images";
    OkHttpClient httpClient;
    String token;
    ImageView useravatar;
    TextView username;
    TextView userbio;
    TextView userrep;
    UserFactory usr;
    RecyclerView profilRv;
    //RvAdapter rvAdapter;
    //RecyclerView.Adapter<ProfilView> rvProfilAdapter;


    public class ProfilView extends RecyclerView.ViewHolder {
        ImageView picture;

        public ProfilView(@NonNull View itemView) {
            super(itemView);
            picture = findViewById(R.id.profil_images_id);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.changeActivity(navigationBar, 3, getApplicationContext());
        overridePendingTransition(0, 0);

        useravatar = findViewById(R.id.idUserImage);
        username = findViewById(R.id.idUserName);
        userbio = findViewById(R.id.idUserBio);
        userrep = findViewById(R.id.idUserReputation);
        token = getAccesToken();
        username = findViewById(R.id.idUserName);
        profilRv = findViewById(R.id.profilRecyclerView);
        //rvAdapter = new RvAdapter();

        fetchProfilData();
        fetchUserImages();
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
                            Picasso.get().load(usr.getUserAvatar()).centerCrop().resize(300, 300).into(useravatar);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchUserImages() {
        httpClient = new OkHttpClient.Builder().build();
        Request req = new Request.Builder()
                .url("https://api.imgur.com/3/account/me/images")
                .method("GET", null)
                .header("Authorization", "Bearer " + getAccesToken())
                .header("User-agent", "DEV_epicture_2019")
                .build();
        httpClient.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                profilRv = findViewById(R.id.profilRecyclerView);
                JSONObject fullObject;
                JSONArray arrayOfObject;
                JSONObject oneImageObject;
                List<Photo> mPictures = new ArrayList<>();
                try {
                    fullObject = new JSONObject(response.body().string());
                    arrayOfObject = fullObject.getJSONArray("data");

                    Log.d("TAG", "ARRAY LENGTH : " + arrayOfObject.length());
                    for (int j = 0; j < arrayOfObject.length(); j++) {
                        Log.d("FIRSTLIGNFOR", "WERE IN THE FOR ");
                        Photo photoObject = new Photo();
                        oneImageObject = arrayOfObject.getJSONObject(j);
                        photoObject.id = oneImageObject.getString("id");
                        mPictures.add(photoObject);
                        Log.d("TAG", "MyImagesProfile: " + photoObject.id);
                        Log.d("mPictures", "mpitcures size : " + mPictures.size());
                    }
                    runOnUiThread(() -> renderGridLayout(mPictures, profilRv));
                } catch (JSONException e) {
                    e.getStackTrace();
                }
            }
        });
    }

    public void renderGridLayout(final List<Photo> pics, RecyclerView profilRv)
    {
        Log.d("mPictures", "mpitcures size : " + pics.size());
        profilRv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        Log.d("mPictures", "mpitcures size : " + pics.size());
        RecyclerView.Adapter<ProfilView> rvProfilAdapter = new RecyclerView.Adapter<ProfilView>() {
            @NonNull
            @Override
            public ProfilView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ProfilView pv = new ProfilView(getLayoutInflater().inflate(R.layout.profil_card, null));
                pv.picture = pv.itemView.findViewById(R.id.profil_images_id);
                return (pv);
            }

            @Override
            public void onBindViewHolder(@NonNull ProfilView holder, int position)
            {
                String requestUrl = "https://i.imgur.com/" + pics.get(position).id + ".jpg";
                Picasso.get().load(requestUrl).into(holder.picture);
                holder.picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "image info", Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public int getItemCount() {return 0;}
        };
        profilRv.setAdapter(rvProfilAdapter);
    }

}


