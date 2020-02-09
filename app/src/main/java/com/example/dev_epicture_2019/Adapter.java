package com.example.dev_epicture_2019;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Adapter {
    public static class PhotoVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;
        ImageButton favBtn;
        int is_fav = 0;

        public PhotoVH(View itemView) {
            super(itemView);
        }
    }


}
