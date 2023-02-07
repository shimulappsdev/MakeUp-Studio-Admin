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
import com.example.makeupstudioadmin.adapter.BrandAdapter;
import com.example.makeupstudioadmin.databinding.FragmentUpdateBrandBinding;
import com.example.makeupstudioadmin.model.Brand;
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

public class UpdateBrandFragment extends Fragment {

    FragmentUpdateBrandBinding binding;
    FirebaseFirestore database;
    StorageReference storageReference;
    Uri brandImgUri;
    String brandImgUrl, brandId, brandName, brandImage;
    ProgressDialog dialog;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateBrandBinding.inflate(getLayoutInflater(), container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Updating Brand");
        dialog.setCancelable(false);

        intent = getActivity().getIntent();

        brandId = intent.getStringExtra("brandId");
        brandName = intent.getStringExtra("brandName");
        brandImage = intent.getStringExtra("brandImage");

        binding.addBrand.setText(brandName);
        Glide.with(getActivity()).load(brandImage).placeholder(R.drawable.imagehint).into(binding.brandImg);

        database = FirebaseFirestore.getInstance();

        binding.updateBrandBtn.setOnClickListener(view -> {
            dialog.show();
            if (brandName != null){
                String brandName = binding.addBrand.getText().toString().trim();
                Map<String, Object> brandMap = new HashMap<>();
                brandMap.put("brand_id", brandId);
                brandMap.put("brand_name", brandName);
                database.collection("MakeUp")
                        .document("brand")
                        .collection("brandList")
                        .document(brandId).update(brandMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Successfully Update", Toast.LENGTH_SHORT).show();
                                    binding.addBrand.setText("");
                                }
                            }
                        });
            }

            if (brandImgUri != null){
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
                                    Map<String, Object> brandMap = new HashMap<>();
                                    brandMap.put("brand_image", brandImgUrl);
                                    database.collection("MakeUp")
                                            .document("brand")
                                            .collection("brandList")
                                            .document(brandId).update(brandMap);
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
            startActivityForResult(intent,5000);
        });

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5000) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    brandImgUri = data.getData();
                    binding.brandImg.setImageURI(brandImgUri);
                }
            }
        }
    }
}