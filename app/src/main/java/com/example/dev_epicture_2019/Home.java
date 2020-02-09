package com.example.dev_epicture_2019;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Home extends Common {

    private OkHttpClient httpClient;
    private String url = "https://api.imgur.com/3/gallery/user/viral";
    private int index;

    private static class PhotoVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;
        ImageButton favBtn;
        int is_fav = 0;

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
        this.index = 1;
        fetchData();
    }

    private void fetchData() {
        httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .header("Authorization", "Bearer " + accesToken)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                final List<Photo> photos = new Parser().getData(response);
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
                PhotoVH vh = new PhotoVH(getLayoutInflater().inflate(R.layout.card, null));
                vh.photo = vh.itemView.findViewById(R.id.image);
                vh.title = vh.itemView.findViewById(R.id.title);
                vh.favBtn = vh.itemView.findViewById(R.id.favBtn);
                return vh;
            }

            @Override
            public void onBindViewHolder(@NonNull PhotoVH holder, int position) {
                String path = "https://i.imgur.com/" + photos.get(position).link + ".jpg";
                Picasso.get().load(path).into(holder.photo);
                holder.title.setText(photos.get(position).title);
                String name = photos.get(position).title;
                Boolean fav = photos.get(position).favorite;
                String debug = name + fav;
                Log.d("TAG", "onBindViewHolder: " + debug);
                if (photos.get(position).favorite == true) {
                    holder.is_fav = 1;
                    checkHeart(holder);
                } else {
                    holder.is_fav = 0;
                    uncheckHeart(holder);
                }
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setIndex(position);
                        Intent intent = new Intent(getApplicationContext(), Details.class);
                        intent.putExtra("galleryId", photos.get(position).id);
                        intent.putExtra("photo", photos.get(position).type);
                        intent.putExtra("accessToken", getAccesToken());
                        startActivity(intent);
                    }
                });
                holder.favBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.is_fav == 0) {
                            Toast.makeText(getApplicationContext(), "Hello toast!!", Toast.LENGTH_LONG).show();
                            checkHeart(holder);
                            addAFavorite(photos.get(position).id, photos.get(position).type);
                        } else if (holder.is_fav == 1) {
                            Toast.makeText(getApplicationContext(), "Hello toast!!", Toast.LENGTH_LONG).show();
                            uncheckHeart(holder);
                            addAFavorite(photos.get(position).id, photos.get(position).type);
                        }
                    }
                });
            }
            @Override
            public int getItemCount() {
                return photos.size();
            }
        };
        rv.setAdapter(adapter);
    }

    public void checkHeart(@NonNull PhotoVH holder) {
        holder.favBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
        holder.is_fav = 1;
    }

    public void uncheckHeart(@NonNull PhotoVH holder) {
        holder.favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        holder.is_fav = 0;
    }

    public void click_fav(View view) {
        TextView txt_all = findViewById(R.id.id_all);
        TextView txt_fav = findViewById(R.id.id_fav);
        txt_all.setTypeface(null, Typeface.NORMAL);
        txt_fav.setTypeface(null, Typeface.BOLD);
        url = "https://api.imgur.com/3/account/me/favorites";
        fetchData();
    }

    public void click_all(View view) {
        TextView txt_all = findViewById(R.id.id_all);
        TextView txt_fav = findViewById(R.id.id_fav);
        txt_all.setTypeface(null, Typeface.BOLD);
        txt_fav.setTypeface(null, Typeface.NORMAL);
        url = "https://api.imgur.com/3/gallery/user/viral";
        fetchData();
    }

    public void click_card(View view) {
        createIntent(this, Details.class);
    }

    public void addAFavorite(String imageHash, String mtype) {
        String url = "https://api.imgur.com/3/" + mtype + "/" + imageHash + "/favorite";
        Log.d("TAG", "addAFavorite: " + url);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                httpClient = new OkHttpClient.Builder().build();
                RequestBody body = RequestBody.create(null, "");
                Request request = new Request.Builder()
                        .url(url)
                        .method("POST", body)
                        .header("Authorization", "Bearer " + accesToken)
                        .build();
                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response_fav) throws IOException {
                    }
                });
            }
        });
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
