package com.example.makeupstudioadmin.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeupstudioadmin.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class BrandViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView brandImg;
    public ImageView removeBtn;
    public TextView brandName;

    public BrandViewHolder(@NonNull View itemView) {
        super(itemView);

        brandImg = itemView.findViewById(R.id.brandImg);
        removeBtn = itemView.findViewById(R.id.removeBtn);
        brandName = itemView.findViewById(R.id.brandName);

    }
}
