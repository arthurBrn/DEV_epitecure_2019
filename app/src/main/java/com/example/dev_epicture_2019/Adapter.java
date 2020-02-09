package com.example.dev_epicture_2019;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

    public void checkHeart(@NonNull Adapter.PhotoVH holder) {
        holder.favBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
        holder.is_fav = 1;
    }

    public void uncheckHeart(@NonNull Adapter.PhotoVH holder) {
        holder.favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        holder.is_fav = 0;
    }

}
