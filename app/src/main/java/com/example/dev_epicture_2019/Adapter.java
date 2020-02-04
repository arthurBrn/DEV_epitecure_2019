package com.example.dev_epicture_2019;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    private HashMap<String, String> mItems;

    Adapter(HashMap<String, String> items) {
        mItems = items;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String key = (String) mItems.keySet().toArray()[position];
        holder.textView.setText(key);
        Picasso.get().load(mItems.get(key)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
