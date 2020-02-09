package com.example.dev_epicture_2019;

import android.content.Context;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiHandler {

    public Request buildGetRequest(String imgurApiUrl, String userAccessToken)
    {
        Request request = new Request.Builder()
                .url(imgurApiUrl)
                .method("GET", null)
                .header("Authorization", "Bearer " + userAccessToken)
                .build();
        return (request);
    }

    public RequestBody buildPostRequestBody(String description, MediaType mediaType, File sourceFile)
    {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", description, RequestBody.create(sourceFile, mediaType))
                .build();
        return (body);
    }

    public Request buildPostFavorite(String url, String accesToken)
    {
        RequestBody body = RequestBody.create(null, "");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .header("Authorization", "Bearer " + accesToken)
                .build();
        return request;
    }

    public void addAFavorite(String imageHash, String mtype, String accesToken) {
        String url = "https://api.imgur.com/3/" + mtype + "/" + imageHash + "/favorite";
        OkHttpClient httpClient;
        //runOnUiThread(() -> {
            httpClient = new OkHttpClient.Builder().build();
            Request request = new ApiHandler().buildPostFavorite(url, accesToken);
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response_fav) throws IOException {
                }
            });
        //});
    }

}
