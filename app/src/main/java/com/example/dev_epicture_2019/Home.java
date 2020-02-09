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
        Request request = new ApiHandler().buildGetRequest(url, getAccesToken());
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
        RecyclerView.Adapter<Adapter.PhotoVH> adapter = new RecyclerView.Adapter<Adapter.PhotoVH>() {
            @NonNull
            @Override
            public Adapter.PhotoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Adapter.PhotoVH vh = new Adapter.PhotoVH(getLayoutInflater().inflate(R.layout.card, null));
                vh.photo = vh.itemView.findViewById(R.id.image);
                vh.title = vh.itemView.findViewById(R.id.title);
                vh.favBtn = vh.itemView.findViewById(R.id.favBtn);
                return vh;
            }

            @Override
            public void onBindViewHolder(@NonNull Adapter.PhotoVH holder, int position) {
                String path = "https://i.imgur.com/" + photos.get(position).link + ".jpg";
                Picasso.get().load(path).into(holder.photo);
                holder.title.setText(photos.get(position).title);
                if (photos.get(position).favorite == true) {
                    holder.is_fav = 1;
                    new Adapter().checkHeart(holder);
                } else {
                    holder.is_fav = 0;
                    new Adapter().uncheckHeart(holder);
                }
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (photos.get(position).type.equals("album")) {
                            Intent intent = new Intent(getApplicationContext(), Details.class);
                            intent.putExtra("galleryId", photos.get(position).id);
                            intent.putExtra("photo", photos.get(position).type);
                            intent.putExtra("accessToken", getAccesToken());
                            startActivity(intent);
                        }
                    }
                });
                holder.favBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.is_fav == 0) {
                            new Adapter().checkHeart(holder);
                            new ApiHandler().addAFavorite(photos.get(position).id, photos.get(position).type, getAccesToken());
                        } else if (holder.is_fav == 1) {
                            new Adapter().uncheckHeart(holder);
                            new ApiHandler().addAFavorite(photos.get(position).id, photos.get(position).type, getAccesToken());
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

    @Override
    protected void onResume() {
        super.onResume();
    }
}