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
import com.example.makeupstudioadmin.model.PopularMakeup;
import com.example.makeupstudioadmin.viewholder.PopularMakeUpViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PopularMakeupAdapter extends RecyclerView.Adapter<PopularMakeUpViewHolder> {

    private Context context;
    private List<PopularMakeup> popularMakeupList;
    FirebaseFirestore database;

    public PopularMakeupAdapter(Context context, List<PopularMakeup> popularMakeupList) {
        this.context = context;
        this.popularMakeupList = popularMakeupList;
    }

    @NonNull
    @Override
    public PopularMakeUpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularMakeUpViewHolder(LayoutInflater.from(context).inflate(R.layout.popular_makeup_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMakeUpViewHolder holder, int position) {
        PopularMakeup popularMakeup = popularMakeupList.get(position);
        holder.popularMakeupName.setText(popularMakeup.getPopularMakeUp_name());
        Glide.with(context).load(popularMakeup.getPopularMakeUp_image()).placeholder(R.drawable.imagehint).into(holder.popularMakeupImg);
        String popularMakeupId = popularMakeup.getPopularMakeUp_id();
        String popularMakeupName = popularMakeup.getPopularMakeUp_name();
        String popularMakeupImg= popularMakeup.getPopularMakeUp_image();
        String popularMakeupDescription = popularMakeup.getPopularMakeUp_description();

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("updatePopularMakeup", "updatePopularMakeup");
            intent.putExtra("popularMakeupId", popularMakeupId);
            intent.putExtra("popularMakeupName", popularMakeupName);
            intent.putExtra("popularMakeupImg", popularMakeupImg);
            intent.putExtra("popularMakeupDescription", popularMakeupDescription);
            context.startActivity(intent);
        });

        database = FirebaseFirestore.getInstance();
        holder.removeBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(popularMakeupName+" Remove");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    database.collection("MakeUp")
                            .document("popularMakeup")
                            .collection("popularMakeupList")
                            .document(popularMakeupId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context, popularMakeupName+" Removed", Toast.LENGTH_SHORT).show();
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
        return popularMakeupList.size();
    }
}
