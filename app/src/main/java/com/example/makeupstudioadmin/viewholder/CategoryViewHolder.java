package com.example.makeupstudioadmin.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeupstudioadmin.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView categoryImg;
    public ImageView removeBtn;
    public TextView categoryName;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryImg = itemView.findViewById(R.id.categoryImg);
        removeBtn = itemView.findViewById(R.id.removeBtn);
        categoryName = itemView.findViewById(R.id.categoryName);

    }
}
