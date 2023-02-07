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
import com.example.makeupstudioadmin.adapter.CategoryAdapter;
import com.example.makeupstudioadmin.adapter.SliderAdapter;
import com.example.makeupstudioadmin.databinding.FragmentCategoryBinding;
import com.example.makeupstudioadmin.model.Category;
import com.example.makeupstudioadmin.model.Slider;
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

public class CategoryFragment extends Fragment {

    FragmentCategoryBinding binding;
    FirebaseFirestore database;
    StorageReference storageReference;
    Uri categoryImgUri;
    String categoryImgUrl;
    ProgressDialog dialog;
    CategoryAdapter adapter;
    List<Category> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(getLayoutInflater(), container, false);
        categoryList = new ArrayList<>();
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Attaching Category");
        dialog.setCancelable(false);

        database = FirebaseFirestore.getInstance();
        binding.progressBar.setVisibility(View.VISIBLE);

        binding.addCategoryBtn.setOnClickListener(view -> {
            if (categoryImgUri != null){
                dialog.show();
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
                                    String categoryId = UUID.randomUUID().toString();

                                    String categoryName = binding.addCategory.getText().toString().trim();

                                    Map<String, Object> categoryMap = new HashMap<>();
                                    categoryMap.put("category_id", categoryId);
                                    categoryMap.put("category_name", categoryName);
                                    categoryMap.put("category_image", categoryImgUrl);
                                    database.collection("MakeUp")
                                            .document("category")
                                            .collection("categoryList")
                                            .document(categoryId).set(categoryMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        dialog.dismiss();
                                                        Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_SHORT).show();
                                                        binding.categoryImg.setImageResource(R.drawable.imagehint);
                                                        binding.addCategory.setText("");
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
                        Toast.makeText(getActivity(), "Oops...Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        database.collection("MakeUp")
                .document("category")
                .collection("categoryList").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        categoryList.clear();
                        List<Category> data = value.toObjects(Category.class);
                        categoryList.addAll(data);
                        adapter = new CategoryAdapter(getActivity(), categoryList);
                        binding.categoryRecyclerView.setAdapter(adapter);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });

        binding.addImgBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,200);
        });

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    categoryImgUri = data.getData();
                    binding.categoryImg.setImageURI(categoryImgUri);
                }
            }
        }

    }
}