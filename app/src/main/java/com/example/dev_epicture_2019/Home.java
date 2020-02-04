package com.example.dev_epicture_2019;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class Home extends Common{

    private HashMap<String, String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.changeActivity(navigationBar, 0, getApplicationContext());
        overridePendingTransition(0, 0);
        //Picasso.get().load("https://i.imgur.com/jBZeio3.jpg").into(image);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
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

        mRecyclerView.setAdapter(mAdapter);
        }
        /*OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/account/me")
                .method("GET", null)
                .addHeader("Authorization", getAccesToken())
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


            }
        });
    }*/

    /*public boolean onTouchEvent(MotionEvent touchEvent) {
        swipe(touchEvent);
        if (x1 > x2)
            createIntent(getApplicationContext(), Search.class);
        return false;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
