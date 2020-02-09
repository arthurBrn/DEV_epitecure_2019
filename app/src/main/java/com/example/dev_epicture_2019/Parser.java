package com.example.dev_epicture_2019;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class Parser {
    public List<Photo> getData(Response response) throws JSONException, IOException {
        JSONObject data;
        JSONArray items;
        final List<Photo> photos = new ArrayList<>();
        try {
            data = new JSONObject((response.body().string()));
            items = data.getJSONArray("data");

            for (int i = 0; i < items.length(); i++) {
                try {
                    JSONObject item = items.getJSONObject(i);
                    Photo photo = new Photo();
                    if (item.has("is_album")) {
                        if (item.getBoolean("is_album")) {
                            photo.link = item.getString("cover");
                            photo.type = "album";
                        } else {
                            photo.link = item.getString("id");
                            photo.type = "image";
                        }
                    }
                    photo.id = item.getString("id");
                    photo.title = item.getString("title");
                    photo.description = item.getString("description");
                    photo.favorite = item.getBoolean("favorite");

                    photos.add(photo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photos;
    }

    public Gallery getGallery(JSONObject data) throws JSONException, IOException {
        Gallery gallery = new Gallery();
        gallery.id = data.getString("id");
        gallery.title = data.getString("title");
        gallery.description = data.getString("description");
        gallery.datetime = data.getInt("datetime");
        gallery.images_count = data.getInt("images_count");
        gallery.ups = data.getInt("ups");
        gallery.downs = data.getInt("downs");
        return gallery;
    }

    public List<Image> getImages(JSONObject data) throws IOException, JSONException {

        List<Image> images = new ArrayList<>();
        JSONArray img;
        if (data.has("images")) {
            img = data.getJSONArray("images");
            for (int i = 0; i < img.length(); i++) {
                JSONObject item = img.getJSONObject(i);
                Image image = new Image();
                image.id = item.getString("id");
                image.description = item.getString("description");
                image.title = item.getString("title");
                image.favorite = item.getBoolean("favorite");
                Log.d("TAG", "getImages: " + image.id + image.title + image.title);
                images.add(image);
            }
        }
        return images;
    }
}
