package com.example.dev_epicture_2019;

import android.graphics.Canvas;
import android.graphics.Rect;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends Common{

    private OkHttpClient httpClient;

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
        setContentView(R.layout.activity_home);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.changeActivity(navigationBar, 0, getApplicationContext());
        overridePendingTransition(0, 0);
        this.fetchdata();

    }

    private void fetchdata() {
        String url = "https://api.imgur.com/3/account/me/favorites";
        httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .header("Authorization", "Bearer " + accesToken)
                .header("User-agent", "DEV_epicture_2019")
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject data = new JSONObject((response.body().string()));
                    JSONArray items = data.getJSONArray("data");
                    final List<Photo> photos = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        Photo photo = new Photo();
                        photo.id = item.getString("id");
                        photo.title = item.getString("title");
                        photo.link = item.getString("cover");
                        photos.add(photo);
                    }
                    runOnUiThread(() -> render(photos));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void render(final List<Photo> photos) {
        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter<PhotoVH> adapter = new RecyclerView.Adapter<PhotoVH>() {
            @NonNull
            @Override
            public PhotoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                PhotoVH vh = new PhotoVH(getLayoutInflater().inflate(R.layout.row, null));
                vh.photo = vh.itemView.findViewById(R.id.image);
                vh.title = vh.itemView.findViewById(R.id.title);
                return vh;
            }

            @Override
            public void onBindViewHolder(@NonNull PhotoVH holder, int position) {
                String path = "https://i.imgur.com/" + photos.get(position).link + ".jpg";
                Picasso.get().load(path).into(holder.photo);
                holder.title.setText(photos.get(position).title);
            }

            @Override
            public int getItemCount() {
                return photos.size();
            }
        };
        rv.setAdapter(adapter);
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 16; // Gap of 16px
            }
        });
    }

}
