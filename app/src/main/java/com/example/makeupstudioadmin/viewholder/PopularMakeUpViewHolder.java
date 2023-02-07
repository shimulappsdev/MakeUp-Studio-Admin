package com.example.makeupstudioadmin.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeupstudioadmin.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopularMakeUpViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView popularMakeupImg;
    public ImageView removeBtn;
    public TextView popularMakeupName;

    public PopularMakeUpViewHolder(@NonNull View itemView) {
        super(itemView);

        popularMakeupImg = itemView.findViewById(R.id.popularMakeupImg);
        removeBtn = itemView.findViewById(R.id.removeBtn);
        popularMakeupName = itemView.findViewById(R.id.popularMakeupName);

    }
}
