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
import com.example.makeupstudioadmin.adapter.SliderAdapter;
import com.example.makeupstudioadmin.databinding.FragmentMakeUpItemSliderBinding;
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

public class MakeUpItemSliderFragment extends Fragment {

    FragmentMakeUpItemSliderBinding binding;
    FirebaseFirestore database;
    StorageReference storageReference;
    Uri itemSliderImgUri;
    String itemSliderImgUrl, categoryId, makeupItemId;
    ProgressDialog dialog;
    List<Slider> sliderList;
    SliderAdapter adapter;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMakeUpItemSliderBinding.inflate(getLayoutInflater(), container, false);

        intent = getActivity().getIntent();
        categoryId = intent.getStringExtra("categoryId");
        makeupItemId = intent.getStringExtra("makeupItemId");

        sliderList = new ArrayList<>();
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Attaching Slider");
        dialog.setCancelable(false);

        database = FirebaseFirestore.getInstance();
        binding.progressBar.setVisibility(View.VISIBLE);

        binding.addSliderImgBtn.setOnClickListener(view -> {
            if (itemSliderImgUri != null){
                dialog.show();
                storageReference = FirebaseStorage.getInstance().getReference("MakeUp").child("category").child(categoryId).child(makeupItemId).child("slider").child("slider"+System.currentTimeMillis());
                storageReference.putFile(itemSliderImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){
                            dialog.dismiss();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    itemSliderImgUrl = String.valueOf(uri);
                                    String sliderId = UUID.randomUUID().toString();
                                    Map<String, Object> sliderMap = new HashMap<>();
                                    sliderMap.put("slider_id", sliderId);
                                    sliderMap.put("slider_image", itemSliderImgUrl);
                                    database.collection("MakeUp")
                                            .document("category")
                                            .collection("categoryList")
                                            .document(categoryId)
                                            .collection("makeupItemList")
                                            .document(makeupItemId)
                                            .collection("slider")
                                            .document(sliderId).set(sliderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        dialog.dismiss();
                                                        Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_SHORT).show();
                                                        binding.sliderImg.setImageResource(R.drawable.imagehint);
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
                .collection("categoryList")
                .document(categoryId)
                .collection("makeupItemList")
                .document(makeupItemId)
                .collection("slider").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        sliderList.clear();
                        List<Slider> data = value.toObjects(Slider.class);
                        sliderList.addAll(data);
                        adapter = new SliderAdapter(getActivity(), sliderList);
                        binding.sliderImgRecyclerView.setAdapter(adapter);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });

        binding.addImg.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,700);
        });

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 700) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    itemSliderImgUri = data.getData();
                    binding.sliderImg.setImageURI(itemSliderImgUri);
                }
            }
        }
    }
}