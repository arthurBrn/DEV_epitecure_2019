package com.example.dev_epicture_2019;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends Common{

    private HashMap<String, String> mItems;
    private TextView _response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.changeActivity(navigationBar, 0, getApplicationContext());
        overridePendingTransition(0, 0);
        /*RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mItems = new HashMap<>();
        mItems.put("usa", "https://i.imgur.com/jBZeio3.jpg");
        mItems.put("shakira", "https://i.imgur.com/dytJ00U.jpg");
        mItems.put("sister and husband", "https://i.imgur.com/dLI6MGF.png");
        mItems.put("wholesome", "https://i.imgur.com/VCKTWCB.jpg");
        mItems.put("tweet", "https://i.imgur.com/rVdiMur.png");
        Adapter mAdapter = new Adapter(mItems);

        mRecyclerView.setAdapter(mAdapter);*/
        _response = findViewById(R.id.response);
            String url = "https://api.imgur.com/3/account/me";
        RequestApi myRequest = new RequestApi(url, getAccesToken());
        myRequest.askApi();
        String msg = getApiResponse();
        Log.d("ldswhfwuoooiiihhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh", "lol " + msg);

        _response.setText(super.getApiResponse());
        }
}
