package com.example.makeupstudioadmin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.makeupstudioadmin.R;
import com.example.makeupstudioadmin.activity.ContainerActivity;
import com.example.makeupstudioadmin.model.Brand;
import com.example.makeupstudioadmin.viewholder.BrandViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandViewHolder> {

    private Context context;
    private List<Brand> brandList;
    FirebaseFirestore database;

    public BrandAdapter(Context context, List<Brand> brandList) {
        this.context = context;
        this.brandList = brandList;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BrandViewHolder(LayoutInflater.from(context).inflate(R.layout.brand_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {

        Brand brand = brandList.get(position);
        holder.brandName.setText(brand.getBrand_name());
        Glide.with(context).load(brand.getBrand_image()).placeholder(R.drawable.imagehint).into(holder.brandImg);
        String brandId = brand.getBrand_id();
        String brandName = brand.getBrand_name();
        String brandImage = brand.getBrand_image();

        database = FirebaseFirestore.getInstance();

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("updateBrand", "updateBrand");
            intent.putExtra("brandId", brandId);
            intent.putExtra("brandName", brandName);
            intent.putExtra("brandImage", brandImage);
            context.startActivity(intent);
        });

        holder.removeBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(brandName+" Remove");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    database.collection("MakeUp")
                            .document("brand")
                            .collection("brandList")
                            .document(brandId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context, brandName+" Removed", Toast.LENGTH_SHORT).show();
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
        return brandList.size();
    }
}
