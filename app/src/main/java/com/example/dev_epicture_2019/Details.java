package com.example.dev_epicture_2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Details extends AppCompatActivity {

    private OkHttpClient httpClient;
    private String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        String id = intent.getStringExtra("galleryId");
        this.accessToken = intent.getStringExtra("accessToken");
        showDetails(id);
    }
    private void showDetails(String img_id) {
        String url = "https://api.imgur.com/3/gallery/" + img_id;
        Request request = new ApiHandler().buildGetRequest(url, accessToken);
        httpClient = new OkHttpClient.Builder().build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Image> images = new ArrayList<>();
                try {
                    JSONObject res = new JSONObject(response.body().string());
                    JSONObject data = res.getJSONObject("data");
                    final Gallery gallery = new Parser().getGallery(data);
                    final List<Image> finalListImage = getImageList(data);
                    runOnUiThread(() -> {
                        render(finalListImage);
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public List<Image> getImageList(JSONObject data) throws IOException, JSONException {
        List<Image> images = new Parser().getImages(data);
        return images;
    }

    private void render(final List<Image> images) {
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

                String path = "https://i.imgur.com/" + images.get(position).id + ".jpg";
                Picasso.get().load(path).into(holder.photo);
                holder.title.setText(images.get(position).title);
                String name = images.get(position).title;
                Boolean fav = images.get(position).favorite;
                String debug = name + fav;
                Log.d("TAG", "onBindViewHolder: " + debug);
                if (images.get(position).favorite == true) {
                    holder.is_fav = 1;
                    checkHeart(holder);
                } else {
                    holder.is_fav = 0;
                    uncheckHeart(holder);
                }
                holder.favBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.is_fav == 0) {
                            checkHeart(holder);
                            new ApiHandler().addAFavorite(images.get(position).id, "image", accessToken);
                        } else if (holder.is_fav == 1) {
                            uncheckHeart(holder);
                            new ApiHandler().addAFavorite(images.get(position).id, "image", accessToken);
                        }
                    }
                });
            }


            @Override
            public int getItemCount() {
                return images.size();
            }
        };
        rv.setAdapter(adapter);
    }
    public void checkHeart(@NonNull Adapter.PhotoVH holder) {
        holder.favBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
        holder.is_fav = 1;
    }

    public void uncheckHeart(@NonNull Adapter.PhotoVH holder) {
        holder.favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        holder.is_fav = 0;
    }


}
