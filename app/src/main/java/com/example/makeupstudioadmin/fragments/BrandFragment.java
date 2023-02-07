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
import com.example.makeupstudioadmin.adapter.BrandAdapter;
import com.example.makeupstudioadmin.adapter.CategoryAdapter;
import com.example.makeupstudioadmin.databinding.FragmentBrandBinding;
import com.example.makeupstudioadmin.model.Brand;
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

public class BrandFragment extends Fragment {

    FragmentBrandBinding binding;
    FirebaseFirestore database;
    StorageReference storageReference;
    Uri brandImgUri;
    String brandImgUrl;
    ProgressDialog dialog;
    BrandAdapter adapter;
    List<Brand> brandList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBrandBinding.inflate(getLayoutInflater(), container, false);

        brandList = new ArrayList<>();
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Attaching Brand");
        dialog.setCancelable(false);

        binding.progressBar.setVisibility(View.VISIBLE);

        database = FirebaseFirestore.getInstance();

        binding.addBrandBtn.setOnClickListener(view -> {
            if (brandImgUri != null){
                dialog.show();
                storageReference = FirebaseStorage.getInstance().getReference("MakeUp").child("brand").child("brand"+System.currentTimeMillis());
                storageReference.putFile(brandImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){
                            dialog.dismiss();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    brandImgUrl = String.valueOf(uri);
                                    String brandId = UUID.randomUUID().toString();

                                    String brandName = binding.addBrand.getText().toString().trim();

                                    Map<String, Object> brandMap = new HashMap<>();
                                    brandMap.put("brand_id", brandId);
                                    brandMap.put("brand_name", brandName);
                                    brandMap.put("brand_image", brandImgUrl);
                                    database.collection("MakeUp")
                                            .document("brand")
                                            .collection("brandList")
                                            .document(brandId).set(brandMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        dialog.dismiss();
                                                        Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_SHORT).show();
                                                        binding.brandImg.setImageResource(R.drawable.imagehint);
                                                        binding.addBrand.setText("");
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
                .document("brand")
                .collection("brandList").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        brandList.clear();
                        List<Brand> data = value.toObjects(Brand.class);
                        brandList.addAll(data);
                        adapter = new BrandAdapter(getActivity(), brandList);
                        binding.brandRecyclerView.setAdapter(adapter);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });

        binding.addImgBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,500);
        });

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    brandImgUri = data.getData();
                    binding.brandImg.setImageURI(brandImgUri);
                }
            }
        }

    }
}