package com.example.dev_epicture_2019;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ProfilView>
{
    @NonNull
    @Override
    public ProfilView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // We're gonna create a view here
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profil_card, parent, false);

        return new ProfilView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ProfilView extends RecyclerView.ViewHolder
    {

        public ProfilView(@NonNull View itemView) {
            super(itemView);
        }
    }

}