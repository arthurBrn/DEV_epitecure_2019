package com.example.dev_epicture_2019;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
    TextView imagesDescription;
    UserFactory usr;
    RecyclerView profilRv;


    private static class ProfilVh extends RecyclerView.ViewHolder
    {
        ImageView profilPictures;
        TextView pictureTitle;

        public ProfilVh(View itemView){ super(itemView); }
    }


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
        profilRv = findViewById(R.id.profilRecyclerView);

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
                            Picasso.get().load(usr.getUserAvatar()).centerCrop() .resize(300,300).into(useravatar);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchUserImages()
    {
        OkHttpClient cli = new OkHttpClient.Builder().build();
        Request req = new Request.Builder()
                .url(urlRecoverMeImages)
                .method("GET", null)
                .header("Authorization", "Bearer " + token)
                .header("User-agent", "DEV_epicture_2019")
                .build();
        cli.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {e.getMessage(); e.getStackTrace();}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                profilRv = findViewById(R.id.profilRecyclerView);
                JSONObject data;
                JSONArray items;
                List<Photo> mphotos = new ArrayList<>();
                try {
                    data = new JSONObject(response.body().string());
                    username.setText(String.valueOf(data));
                    items = data.getJSONArray("data");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        Photo thisPic = new Photo();
                        thisPic.id = item.getString("id");
                        thisPic.title = item.getString("title");
                        thisPic.description = item.getString("description");
                        //thisPic.favorite = item.getBoolean("favorite");
                        mphotos.add(thisPic);
                    }
                    runOnUiThread(() -> displayPicMethod(mphotos, profilRv));
                } catch(JSONException e) {
                    e.getMessage();
                    e.getStackTrace();
                }
            }
        });
    }

    private void displayPicMethod(final List<Photo> photos, RecyclerView profilRv) {
        profilRv.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter<ProfilVh> adapter = new RecyclerView.Adapter<ProfilVh>() {
            @NonNull
            @Override
            public ProfilVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ProfilVh vh = new ProfilVh(getLayoutInflater().inflate(R.layout.card, null));
                vh.profilPictures = vh.itemView.findViewById(R.id.image);
                vh.pictureTitle = vh.itemView.findViewById(R.id.title);
                return vh;
            }

            @Override
            public void onBindViewHolder(@NonNull ProfilVh holder    , int position) {
                String path = "https://i.imgur.com/" + photos.get(position).id + ".jpg";
                Picasso.get().load(path).into(holder.profilPictures);
                holder.pictureTitle.setText(photos.get(position).title);
                /*holder.profilPictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setIndex(position);
                        Intent intent = new Intent(getApplicationContext(), Details.class);
                        intent.putExtra("galeryId", photos.get(position).id);
                        startActivity(intent);
                    }
                });*/
            }
            @Override
            public int getItemCount() {
                return photos.size();
            }
        };
        profilRv.setAdapter(adapter);
    }




    /* public void initialize()
    {
        profilRV = findViewById(R.id.rvProfil);
        // Initialize a gridLayoutManager
        GridLayoutManager glm = new GridLayoutManager(this, 2);
        profilRV.setLayoutManager(glm);
    } */

    public void setProfilHeader(UserFactory usr) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                username.setText(usr.getUserUrl());
            }
        });
    }
}


