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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

    private static class ImageVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;
        ImageButton favBtn;
        int is_fav = 0;

        public ImageVH(View itemView) {
            super(itemView);
        }
    }

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
        httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/gallery/" + img_id)
                .method("GET", null)
                .header("Authorization", "Bearer " + accessToken)
                .build();
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
                    int nb = gallery.images_count;
                    images = getImageList(data);
                    final List<Image> finalListImage = images;
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
        RecyclerView.Adapter<ImageVH> adapter = new RecyclerView.Adapter<ImageVH>() {
            @NonNull
            @Override
            public ImageVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ImageVH vh = new ImageVH(getLayoutInflater().inflate(R.layout.card, null));
                vh.photo = vh.itemView.findViewById(R.id.image);
                vh.title = vh.itemView.findViewById(R.id.title);
                vh.favBtn = vh.itemView.findViewById(R.id.favBtn);
                return vh;
            }

            @Override
            public void onBindViewHolder(@NonNull ImageVH holder, int position) {

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
                            new Home().addAFavorite(images.get(position).id, "image");
                        } else if (holder.is_fav == 1) {
                            uncheckHeart(holder);
                            new Home().addAFavorite(images.get(position).id, "image");
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
    public void checkHeart(@NonNull ImageVH holder) {
        holder.favBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
        holder.is_fav = 1;
    }

    public void uncheckHeart(@NonNull ImageVH holder) {
        holder.favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        holder.is_fav = 0;
    }


}
