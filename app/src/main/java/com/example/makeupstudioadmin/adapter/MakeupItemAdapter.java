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
import com.example.makeupstudioadmin.model.MakeupItem;
import com.example.makeupstudioadmin.viewholder.MakeUpItemViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MakeupItemAdapter extends RecyclerView.Adapter<MakeUpItemViewHolder> {

    private Context context;
    private List<MakeupItem> makeupItemList;
    FirebaseFirestore database;

    public MakeupItemAdapter(Context context, List<MakeupItem> makeupItemList) {
        this.context = context;
        this.makeupItemList = makeupItemList;
    }

    @NonNull
    @Override
    public MakeUpItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MakeUpItemViewHolder(LayoutInflater.from(context).inflate(R.layout.makeup_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MakeUpItemViewHolder holder, int position) {

        MakeupItem makeupItem = makeupItemList.get(position);
        holder.makeupItemName.setText(makeupItem.getMakeupItem_name());
        holder.makeupItemAbout.setText(makeupItem.getMakeupItem_about());
        Glide.with(context).load(makeupItem.getMakeupItem_image()).placeholder(R.drawable.imagehint).into(holder.makeUpItemImg);

        String makeItemName = makeupItem.getMakeupItem_name();
        String makeItemId = makeupItem.getMakeupItem_id();
        String categoryId = makeupItem.getCategory_id();
        String makeupItemAbout = makeupItem.getMakeupItem_about();
        String makeupItemProcedure = makeupItem.getMakeupItem_procedure();
        String makeupItemRemove = makeupItem.getMakeupItem_remove();
        String makeupItemImg = makeupItem.getMakeupItem_image();

        database = FirebaseFirestore.getInstance();

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("details", "details");
            intent.putExtra("categoryId", categoryId);
            intent.putExtra("makeItemId", makeItemId);
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("updateMakeupItem", "updateMakeupItem");
            intent.putExtra("categoryId", categoryId);
            intent.putExtra("makeUpItemId", makeItemId);
            context.startActivity(intent);
            return true;
        });

        holder.removeBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(makeItemName+" Remove");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    database.collection("MakeUp")
                            .document("category")
                            .collection("categoryList")
                            .document(categoryId)
                            .collection("makeupItemList")
                            .document(makeItemId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context, makeItemName+" Removed", Toast.LENGTH_SHORT).show();
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
        return makeupItemList.size();
    }
}
