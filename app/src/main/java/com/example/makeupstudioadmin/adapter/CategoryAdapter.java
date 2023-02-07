package com.example.makeupstudioadmin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.makeupstudioadmin.R;
import com.example.makeupstudioadmin.activity.ContainerActivity;
import com.example.makeupstudioadmin.model.Category;
import com.example.makeupstudioadmin.viewholder.CategoryViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;
    FirebaseFirestore database;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getCategory_name());
        Glide.with(context).load(category.getCategory_image()).placeholder(R.drawable.imagehint).into(holder.categoryImg);
        String categoryId = category.getCategory_id();
        String categoryName = category.getCategory_name();
        String categoryImg = category.getCategory_image();

        database = FirebaseFirestore.getInstance();

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("makeupItem", "makeupItem");
            intent.putExtra("categoryId", categoryId);
            intent.putExtra("categoryName", categoryName);
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("updateCategory", "updateCategory");
            intent.putExtra("categoryId", categoryId);
            intent.putExtra("categoryName", categoryName);
            intent.putExtra("categoryImg", categoryImg);
            context.startActivity(intent);
            return true;
        });

        holder.removeBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(categoryName+" Remove");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    database.collection("MakeUp")
                            .document("category")
                            .collection("categoryList")
                            .document(categoryId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context, categoryName+" Removed", Toast.LENGTH_SHORT).show();
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
        return categoryList.size();
    }
}
