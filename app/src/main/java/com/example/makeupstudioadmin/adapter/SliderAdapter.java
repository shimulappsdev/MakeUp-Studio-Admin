package com.example.makeupstudioadmin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.makeupstudioadmin.R;
import com.example.makeupstudioadmin.model.Slider;
import com.example.makeupstudioadmin.viewholder.SliderViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderViewHolder> {
    private Context context;
    public List<Slider> sliderList;
    FirebaseFirestore database;

    public SliderAdapter(Context context, List<Slider> sliderList) {
        this.context = context;
        this.sliderList = sliderList;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(context).inflate(R.layout.slider_img_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Slider slider = sliderList.get(position);
        Glide.with(context).load(slider.getSlider_image()).placeholder(R.drawable.imagehint).into(holder.sliderImg);
        database = FirebaseFirestore.getInstance();
        String sliderId = slider.getSlider_id();
        holder.removeImg.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Remove");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    database.collection("MakeUp")
                            .document("slider_img")
                            .collection("slider")
                            .document(sliderId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        });

    }

    @Override
    public int getItemCount() {
        return sliderList.size();
    }
}
