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
import com.example.makeupstudioadmin.model.Product;
import com.example.makeupstudioadmin.viewholder.ProductViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    FirebaseFirestore database;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.product_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = productList.get(position);
        holder.productName.setText(product.getProduct_name());
        Glide.with(context).load(product.getProduct_image()).placeholder(R.drawable.imagehint).into(holder.productImg);
        String productId = product.getProduct_id();
        String productName = product.getProduct_name();
        String productDescription = product.getProduct_description();
        String productImg = product.getProduct_image();

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("updateProduct", "updateProduct");
            intent.putExtra("productId", productId);
            intent.putExtra("productName", productName);
            intent.putExtra("productDescription", productDescription);
            intent.putExtra("productImg", productImg);
            context.startActivity(intent);
        });

        database = FirebaseFirestore.getInstance();
        holder.removeBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(productName+" Remove");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    database.collection("MakeUp")
                            .document("product")
                            .collection("productList")
                            .document(productId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context, productName+" Removed", Toast.LENGTH_SHORT).show();
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
        return productList.size();
    }
}
