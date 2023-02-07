package com.example.makeupstudioadmin.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeupstudioadmin.R;

public class SliderViewHolder extends RecyclerView.ViewHolder {
    public ImageView sliderImg, removeImg;
    public SliderViewHolder(@NonNull View itemView) {
        super(itemView);
        sliderImg = itemView.findViewById(R.id.sliderImg);
        removeImg = itemView.findViewById(R.id.removeImg);
    }
}
