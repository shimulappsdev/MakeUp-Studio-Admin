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
import com.example.makeupstudioadmin.adapter.ProductAdapter;
import com.example.makeupstudioadmin.databinding.FragmentProductBinding;
import com.example.makeupstudioadmin.model.Category;
import com.example.makeupstudioadmin.model.Product;
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

public class ProductFragment extends Fragment {

    FragmentProductBinding binding;
    FirebaseFirestore database;
    StorageReference storageReference;
    Uri productImgUri;
    String productImgUrl;
    ProgressDialog dialog;
    ProductAdapter adapter;
    List<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(getLayoutInflater(), container, false);
        productList = new ArrayList<>();
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Attaching Product");
        dialog.setCancelable(false);

        database = FirebaseFirestore.getInstance();
        binding.progressBar.setVisibility(View.VISIBLE);

        binding.addProductBtn.setOnClickListener(view -> {
            if (productImgUri != null){
                dialog.show();
                storageReference = FirebaseStorage.getInstance().getReference("MakeUp").child("product").child("product"+System.currentTimeMillis());
                storageReference.putFile(productImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){
                            dialog.dismiss();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    productImgUrl = String.valueOf(uri);
                                    String productId = UUID.randomUUID().toString();
                                    String productName = binding.addProductName.getText().toString().trim();
                                    String descriptionName = binding.addProductDescription.getText().toString().trim();

                                    Map<String, Object> productMap = new HashMap<>();
                                    productMap.put("product_id", productId);
                                    productMap.put("product_name", productName);
                                    productMap.put("product_description", descriptionName);
                                    productMap.put("product_image", productImgUrl);
                                    database.collection("MakeUp")
                                            .document("product")
                                            .collection("productList")
                                            .document(productId).set(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        dialog.dismiss();
                                                        Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_SHORT).show();
                                                        binding.productImg.setImageResource(R.drawable.imagehint);
                                                        binding.addProductName.setText("");
                                                        binding.addProductDescription.setText("");
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
                .document("product")
                .collection("productList").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        productList.clear();
                        List<Product> data = value.toObjects(Product.class);
                        productList.addAll(data);
                        adapter = new ProductAdapter(getActivity(), productList);
                        binding.productRecyclerView.setAdapter(adapter);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });

        binding.addImgBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,400);
        });


        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    productImgUri = data.getData();
                    binding.productImg.setImageURI(productImgUri);
                }
            }
        }
    }
}