package com.example.dev_epicture_2019;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Details extends AppCompatActivity {

    private OkHttpClient httpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        String id = intent.getStringExtra("galleryId");
        TextView txt = findViewById(R.id.id_image);
        txt.setText(id);
        String accessToken = intent.getStringExtra("accessToken");
        showDetails(id, accessToken);
    }
    private void showDetails(String img_id, String accessToken) {
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
                try {
                    JSONObject res = new JSONObject(response.body().string());
                    JSONObject data = res.getJSONObject("data");
                    final Gallery gallery = new Parser().getGallery(data);
                    List<Image> images;
                    int nb = gallery.images_count;
                    if (gallery.images_count > 1)
                        images = getImageList(data);
                    runOnUiThread(() -> {

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
}
