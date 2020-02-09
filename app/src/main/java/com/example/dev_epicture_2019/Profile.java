package com.example.dev_epicture_2019;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import java.io.IOException;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Callback;
import okhttp3.Call;
import okhttp3.Response;
import org.json.JSONObject;

public class Profile extends Common {

    String USER_INFO_REQUEST = "https://api.imgur.com/3/account/me";
    String USER_IMAGES_REQUEST = "https://api.imgur.com/3/account/me/images";
    String USER_TOKEN;
    OkHttpClient httpClient;
    ImageView useravatar;
    TextView username;
    TextView userbio;
    UserFactory usr;
    PictureFactory pic;
    RecyclerView profilRv;
    ApiHandler apiHandler;

    private static class PhotoVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;

        public PhotoVH(View itemView) {
            super(itemView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.changeActivity(navigationBar, 3, getApplicationContext());
        overridePendingTransition(0, 0);

        USER_TOKEN = getAccesToken();
        apiHandler = new ApiHandler();
        useravatar = findViewById(R.id.idUserImage);
        username = findViewById(R.id.idUserName);
        userbio = findViewById(R.id.idUserBio);
        username = findViewById(R.id.idUserName);

        fetchUserDataForProfilHeader();
        fetchUserImages();
    }

    public void fetchUserDataForProfilHeader()
    {
        httpClient = new OkHttpClient.Builder().build();
        Request request = apiHandler.buildGetRequest(USER_INFO_REQUEST, USER_TOKEN);
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
                    usr = UserFactory.createUser(sndobj.getString("url"), sndobj.getString("bio"), sndobj.getString("avatar"), sndobj.getString("reputation"));
                    runOnUiThread(() -> {
                        username.setText(usr.getUserUrl());
                        if (usr.getUserBio().isEmpty())
                            userbio.setVisibility(View.INVISIBLE);
                        else
                            userbio.setText(usr.getUserBio());
                        Picasso.get().load(usr.getUserAvatar()).centerCrop().resize(300, 300).into(useravatar);
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchUserImages() {
        httpClient = new OkHttpClient.Builder().build();
        Request request = apiHandler.buildGetRequest(USER_IMAGES_REQUEST, USER_TOKEN);
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final List<Photo> photos = new Parser().getData(response);
                    runOnUiThread(() -> renderGridLayout(photos));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void renderGridLayout(final List<Photo> pics)
    {
        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter<Profile.PhotoVH> adapter = new RecyclerView.Adapter<Profile.PhotoVH>() {

            @NonNull
            @Override
            public PhotoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Profile.PhotoVH vh = new Profile.PhotoVH(getLayoutInflater().inflate(R.layout.card, null));
                vh.photo = vh.itemView.findViewById(R.id.image);
                vh.title = vh.itemView.findViewById(R.id.title);
                return vh;
            }

            @Override
            public void onBindViewHolder(@NonNull PhotoVH holder, int position) {
                String path = "https://i.imgur.com/" + pics.get(position).id + ".jpg";
                Picasso.get().load(path).into(holder.photo);
                holder.title.setText(pics.get(position).title);
            }

            @Override
            public int getItemCount() {
                return pics.size();
            }
        };
        rv.setAdapter(adapter);
    }
}


