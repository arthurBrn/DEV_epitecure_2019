package com.example.dev_epicture_2019;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestApi {

    private String url;
    private String accesToken;

    RequestApi(String url, String accesToken) {
        this.url = url;
        this.accesToken = accesToken;
    }

    public void askApi() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .header("Authorization", "Bearer "+ accesToken)
                .header("User-agent", "DEV_epicture_2019")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    Common.setApiResponse(myResponse);
                }
            }
        });
    }
}
