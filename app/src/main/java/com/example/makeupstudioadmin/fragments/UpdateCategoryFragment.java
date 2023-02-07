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

import com.bumptech.glide.Glide;
import com.example.makeupstudioadmin.R;
import com.example.makeupstudioadmin.adapter.CategoryAdapter;
import com.example.makeupstudioadmin.databinding.FragmentUpdateCategoryBinding;
import com.example.makeupstudioadmin.model.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UpdateCategoryFragment extends Fragment {

    FragmentUpdateCategoryBinding binding;
    FirebaseFirestore database;
    StorageReference storageReference;
    Uri categoryImgUri;
    String categoryImgUrl;
    ProgressDialog dialog;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateCategoryBinding.inflate(getLayoutInflater(), container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Updating Category");
        dialog.setCancelable(false);
        intent = getActivity().getIntent();

        String categoryId = intent.getStringExtra("categoryId");
        String categoryName = intent.getStringExtra("categoryName");
        String categoryImg = intent.getStringExtra("categoryImg");

        binding.addCategory.setText(categoryName);
        Glide.with(getActivity()).load(categoryImg).placeholder(R.drawable.imagehint).into(binding.categoryImg);

        database = FirebaseFirestore.getInstance();

        binding.updateCategoryBtn.setOnClickListener(view -> {
            dialog.show();
            if (categoryName != null){
                String updateCategoryName = binding.addCategory.getText().toString().trim();
                Map<String, Object> categoryMap = new HashMap<>();
                categoryMap.put("category_id", categoryId);
                categoryMap.put("category_name", updateCategoryName);
                database.collection("MakeUp")
                        .document("category")
                        .collection("categoryList")
                        .document(categoryId).update(categoryMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            if (categoryImgUri != null){
                storageReference = FirebaseStorage.getInstance().getReference("MakeUp").child("category").child("category"+System.currentTimeMillis());
                storageReference.putFile(categoryImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){
                            dialog.dismiss();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    categoryImgUrl = String.valueOf(uri);
                                    Map<String, Object> categoryMap = new HashMap<>();
                                    categoryMap.put("category_image", categoryImgUrl);
                                    database.collection("MakeUp")
                                            .document("category")
                                            .collection("categoryList")
                                            .document(categoryId).update(categoryMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        dialog.dismiss();
                                                    }
                                                }
                                            });
                                }
                            });
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Oops...Update Failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        binding.addImgBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,2000);
        });

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    categoryImgUri = data.getData();
                    binding.categoryImg.setImageURI(categoryImgUri);
                }
            }
        }
    }
}