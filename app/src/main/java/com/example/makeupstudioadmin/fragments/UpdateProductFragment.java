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
import com.example.makeupstudioadmin.adapter.ProductAdapter;
import com.example.makeupstudioadmin.databinding.FragmentUpdateProductBinding;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UpdateProductFragment extends Fragment {

    FragmentUpdateProductBinding binding;
    FirebaseFirestore database;
    StorageReference storageReference;
    Uri productImgUri;
    String productImgUrl, productId, productName, productDescription, productImg;
    ProgressDialog dialog;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateProductBinding.inflate(getLayoutInflater(), container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Updating Product");
        dialog.setCancelable(false);

        intent = getActivity().getIntent();

        productId = intent.getStringExtra("productId");
        productName = intent.getStringExtra("productName");
        productDescription = intent.getStringExtra("productDescription");
        productImg = intent.getStringExtra("productImg");

        binding.addProductName.setText(productName);
        binding.addProductDescription.setText(productDescription);
        Glide.with(getActivity()).load(productImg).placeholder(R.drawable.imagehint).into(binding.productImg);

        database = FirebaseFirestore.getInstance();

        binding.updateProductBtn.setOnClickListener(view -> {

            if (productName != null){
                String productName = binding.addProductName.getText().toString().trim();
                String descriptionName = binding.addProductDescription.getText().toString().trim();

                Map<String, Object> productMap = new HashMap<>();
                productMap.put("product_id", productId);
                productMap.put("product_name", productName);
                productMap.put("product_description", descriptionName);
                database.collection("MakeUp")
                        .document("product")
                        .collection("productList")
                        .document(productId).update(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

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
                                    Map<String, Object> productMap = new HashMap<>();
                                    productMap.put("product_image", productImgUrl);
                                    database.collection("MakeUp")
                                            .document("product")
                                            .collection("productList")
                                            .document(productId).update(productMap);
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
            startActivityForResult(intent,4000);
        });


        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4000) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    productImgUri = data.getData();
                    binding.productImg.setImageURI(productImgUri);
                }
            }
        }
    }
}