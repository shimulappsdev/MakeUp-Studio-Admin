package com.example.makeupstudioadmin.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.makeupstudioadmin.R;
import com.example.makeupstudioadmin.activity.ContainerActivity;
import com.example.makeupstudioadmin.databinding.FragmentUpdateMakeupItemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdateMakeupItemFragment extends Fragment {

    FragmentUpdateMakeupItemBinding binding;
    FirebaseFirestore database;
    StorageReference storageReference;
    Uri makeupItemImgUri;
    String makeupItemImgUrl, categoryId, makeupItemId, makeupItemName, makeupItemAbout, makeupItemProcedure, makeupItemRemove;
    ProgressDialog dialog;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateMakeupItemBinding.inflate(getLayoutInflater(), container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Updating MakeUp Item");
        dialog.setCancelable(false);

        intent = getActivity().getIntent();
        categoryId = intent.getStringExtra("categoryId");
        makeupItemId = intent.getStringExtra("makeUpItemId");
        makeupItemName = intent.getStringExtra("makeItemName");
        makeupItemAbout = intent.getStringExtra("makeupItemAbout");
        makeupItemProcedure = intent.getStringExtra("makeupItemProcedure");
        makeupItemRemove = intent.getStringExtra("makeupItemRemove");

        binding.makeupItemName.setText(makeupItemName);
        binding.makeupItemAbout.setText(makeupItemAbout);
        binding.makeupItemProcedure.setText(makeupItemProcedure);
        binding.makeupItemRemove.setText(makeupItemRemove);

        database = FirebaseFirestore.getInstance();

        binding.updateMakeupItemBtn.setOnClickListener(view -> {
            dialog.show();
            String itemName = binding.makeupItemName.getText().toString().trim();
            String itemAbout = binding.makeupItemAbout.getText().toString().trim();
            String itemProcedure = binding.makeupItemProcedure.getText().toString().trim();
            String itemRemove = binding.makeupItemRemove.getText().toString().trim();

            if (itemName.equals("")){
                binding.makeupItemName.setError("Required");
                dialog.dismiss();
            }else if (itemAbout.equals("")){
                binding.makeupItemAbout.setError("Required");
                dialog.dismiss();
            }else if (itemProcedure.equals("")){
                binding.makeupItemProcedure.setError("Required");
                dialog.dismiss();
            }else if (itemRemove.equals("")){
                binding.makeupItemRemove.setError("Required");
                dialog.dismiss();
            }else {

                Map<String, Object> makeupItemMap = new HashMap<>();
                makeupItemMap.put("makeupItem_id", makeupItemId);
                makeupItemMap.put("category_id", categoryId);
                makeupItemMap.put("makeupItem_name", itemName);
                makeupItemMap.put("makeupItem_about", itemAbout);
                makeupItemMap.put("makeupItem_procedure", itemProcedure);
                makeupItemMap.put("makeupItem_remove", itemRemove);

                database.collection("MakeUp")
                        .document("category")
                        .collection("categoryList")
                        .document(categoryId)
                        .collection("makeupItemList")
                        .document(makeupItemId).update(makeupItemMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    Intent intent = new Intent(getActivity(), ContainerActivity.class);
                                    intent.putExtra("itemSlider", "itemSlider");
                                    intent.putExtra("categoryId", categoryId);
                                    intent.putExtra("makeupItemId", makeupItemId);
                                    startActivity(intent);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Oops...Failed to Update", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
            }

            if (makeupItemImgUri != null){
                storageReference = FirebaseStorage.getInstance().getReference("MakeUp").child("category").child(categoryId).child("makeupItem"+System.currentTimeMillis());
                storageReference.putFile(makeupItemImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    makeupItemImgUrl = String.valueOf(uri);
                                    Map<String, Object> makeupItemMap = new HashMap<>();
                                    makeupItemMap.put("makeupItem_image", makeupItemImgUrl);
                                    database.collection("MakeUp")
                                            .document("category")
                                            .collection("categoryList")
                                            .document(categoryId)
                                            .collection("makeupItemList")
                                            .document(makeupItemId).update(makeupItemMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        dialog.dismiss();
                                                        binding.makeupItemImg.setImageResource(R.drawable.imagehint);
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                    }
                });
            }

        });

        binding.addImgBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,6000);
        });

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6000) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    makeupItemImgUri = data.getData();
                    binding.makeupItemImg.setImageURI(makeupItemImgUri);
                }
            }
        }
    }
}