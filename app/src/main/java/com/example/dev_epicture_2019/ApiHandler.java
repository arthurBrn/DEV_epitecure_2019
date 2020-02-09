package com.example.dev_epicture_2019;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

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


}
