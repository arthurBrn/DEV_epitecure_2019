package com.example.dev_epicture_2019;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Home extends Common {

    private OkHttpClient httpClient;
    private String url = "https://api.imgur.com/3/gallery/user/rising/0.json";
    private int index;

    private static class PhotoVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;
        ImageButton favBtn;
        int favTouched = 0;

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
        //this.fetchdata();
        fetchData();
    }

    private void fetchData() {
        httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .header("Authorization", "Bearer " + accesToken)
                .header("User-ageant", "DEV_epicture_2019")
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                JSONObject data;
                String msg2 = response.body().toString();
                JSONArray items;
                Log.d("====================item vcount", "onResponse: " + msg2);
                final List<Photo> photos = new ArrayList<>();
                try {
                    data = new JSONObject((response.body().string()));
                    items = data.getJSONArray("data");
                    //String msg2 = data.toString();
                    Log.d("====================item vcount", "onResponse: " + msg2);
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        Photo photo = new Photo();
                        photo.id = item.getString("id");
                        photo.title = item.getString("title");
                        photo.link = item.getString("cover");
                        photo.description = item.getString("description");
                        photos.add(photo);
                        runOnUiThread(() -> render(photos));
                    }

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
            public void onBindViewHolder(@NonNull PhotoVH holder    , int position) {
                String path = "https://i.imgur.com/" + photos.get(position).link + ".jpg";
                Picasso.get().load(path).into(holder.photo);
                holder.title.setText(photos.get(position).title);
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setIndex(position);
                        Intent intent = new Intent(getApplicationContext(), Details.class);
                        intent.putExtra("galeryId", photos.get(position).id);
                        startActivity(intent);
                    }
                });
                holder.favBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.favTouched == 0) {
                            holder.favBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
                            holder.favTouched = 1;
                            //addAFavorite(photos.get(position).id);
                        } else if (holder.favTouched == 1) {
                            holder.favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            holder.favTouched = 0;
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
        url = "https://api.imgur.com/3/gallery/hot/viral/all/0?showViral=true&mature=true&album_previews=true";
        fetchData();
    }

    public void click_card(View view) {
        createIntent(this, Details.class);
    }

    public void addAFavorite(String imageHash) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/image/" + imageHash + "/favorite")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + getAccesToken())
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
