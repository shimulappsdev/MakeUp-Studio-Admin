package com.example.makeupstudioadmin.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeupstudioadmin.R;

public class MakeUpItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView makeUpItemImg, removeBtn;
    public TextView makeupItemName, makeupItemAbout;

    public MakeUpItemViewHolder(@NonNull View itemView) {
        super(itemView);

        makeUpItemImg = itemView.findViewById(R.id.makeUpItemImg);
        removeBtn = itemView.findViewById(R.id.removeBtn);
        makeupItemName = itemView.findViewById(R.id.makeupItemName);
        makeupItemAbout = itemView.findViewById(R.id.makeupItemAbout);

    }
}
