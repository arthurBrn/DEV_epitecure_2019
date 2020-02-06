package com.example.dev_epicture_2019;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import okhttp3.Response;

public class Home extends Common {

    private OkHttpClient httpClient;
    private String url = "https://api.imgur.com/3/gallery/hot/viral/all/0?showViral=true&mature=true&album_previews=true";

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
        //this.fetchdata();
        fetchdata();
    }

    private void fetchdata() {
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
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        Photo photo = new Photo();
                        photo.id = item.getString("id");
                        photo.title = item.getString("title");
                        photo.link = item.getString("cover");
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
                return vh;
            }

            @Override
            public void onBindViewHolder(@NonNull PhotoVH holder, int position) {
                String path = "https://i.imgur.com/" + photos.get(position).link + ".gif";
                Picasso.get().load(path).into(holder.photo);
                holder.title.setText(photos.get(position).title);
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
        fetchdata();

    }

    public void click_all(View view) {
        TextView txt_all = findViewById(R.id.id_all);
        TextView txt_fav = findViewById(R.id.id_fav);
        txt_all.setTypeface(null, Typeface.BOLD);
        txt_fav.setTypeface(null, Typeface.NORMAL);
        url = "https://api.imgur.com/3/gallery/hot/viral/all/0?showViral=true&mature=true&album_previews=true";
        fetchdata();
    }

    public void click_card(View view) {
        createIntent(this, Details.class);
    }


    public void clickableFavorites() {
        TextView text = findViewById(R.id.title);

        text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motion) {
                final int LEFT = 0;
                final int TOP = 1;
                final int RIGHT = 2;
                final int bottom = 3;

                if (motion.getAction() == MotionEvent.ACTION_UP) {
                    if (motion.getX() >= text.getRight() - text.getCompoundDrawables()[RIGHT].getBounds().width()) {
                        Drawable redFavIcon = getResources().getDrawable((R.drawable.ic_favorite_border_black_24dp));
                        redFavIcon.setBounds(0, 0, 0, 60);
                        text.setCompoundDrawables(null, null, null, redFavIcon);
                    }
                }
                return (false);
            }
        });
    }

    public void addFavRequest() {
        //
    }

}
