package com.example.makeupstudioadmin.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeupstudioadmin.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView productImg;
    public ImageView removeBtn;
    public TextView productName;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productImg = itemView.findViewById(R.id.productImg);
        removeBtn = itemView.findViewById(R.id.removeBtn);
        productName = itemView.findViewById(R.id.productName);

    }
}
