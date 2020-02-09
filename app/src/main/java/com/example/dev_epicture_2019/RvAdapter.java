package com.example.dev_epicture_2019;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ProfilView>
{
    @NonNull
    @Override
    public ProfilView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        // On crée un layout inflateur
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // On crée notre vue
        View view = inflater.inflate(R.layout.profil_card, parent, false);

        ProfilView pv = new ProfilView(view);
        return (pv);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class ProfilView extends RecyclerView.ViewHolder
    {
        // On récupère nos éléments ici
        TextView pictureTitle;
        ImageView picture;

        public ProfilView(@NonNull View itemView)
        {
            super(itemView);
            picture = itemView.findViewById(R.id.profil_images_id);
            //pictureTitle = itemView.findViewById(R.id.profil_image_description);
        }
    }

}