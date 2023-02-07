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
import com.example.makeupstudioadmin.adapter.PopularMakeupAdapter;
import com.example.makeupstudioadmin.databinding.FragmentPopularMakeUpBinding;
import com.example.makeupstudioadmin.model.Category;
import com.example.makeupstudioadmin.model.PopularMakeup;
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

public class PopularMakeUpFragment extends Fragment {

    FragmentPopularMakeUpBinding binding;
    FirebaseFirestore database;
    StorageReference storageReference;
    Uri popularMakeupImgUri;
    String popularMakeupImgUrl;
    ProgressDialog dialog;
    PopularMakeupAdapter adapter;
    List<PopularMakeup> popularMakeupList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPopularMakeUpBinding.inflate(getLayoutInflater(), container, false);

        popularMakeupList = new ArrayList<>();
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Attaching Popular MakeUp");
        dialog.setCancelable(false);

        database = FirebaseFirestore.getInstance();
        binding.progressBar.setVisibility(View.VISIBLE);

        binding.addPopularMakeUpBtn.setOnClickListener(view -> {
            if (popularMakeupImgUri != null){
                dialog.show();
                storageReference = FirebaseStorage.getInstance().getReference("MakeUp").child("popularMakeup").child("popularMakeup"+System.currentTimeMillis());
                storageReference.putFile(popularMakeupImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){
                            dialog.dismiss();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    popularMakeupImgUrl = String.valueOf(uri);
                                    String popularMakeupId = UUID.randomUUID().toString();

                                    String popularMakeupName = binding.addPopularMakeUpName.getText().toString().trim();
                                    String popularMakeupDescription = binding.addPopularMakeUpDescription.getText().toString().trim();

                                    Map<String, Object> popularMakeUpMap = new HashMap<>();
                                    popularMakeUpMap.put("popularMakeUp_id", popularMakeupId);
                                    popularMakeUpMap.put("popularMakeUp_name", popularMakeupName);
                                    popularMakeUpMap.put("popularMakeUp_image", popularMakeupImgUrl);
                                    popularMakeUpMap.put("popularMakeUp_description", popularMakeupDescription);

                                    database.collection("MakeUp")
                                            .document("popularMakeup")
                                            .collection("popularMakeupList")
                                            .document(popularMakeupId).set(popularMakeUpMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        dialog.dismiss();
                                                        Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_SHORT).show();
                                                        binding.popularMakeupImg.setImageResource(R.drawable.imagehint);
                                                        binding.addPopularMakeUpName.setText("");
                                                        binding.addPopularMakeUpDescription.setText("");
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
                .document("popularMakeup")
                .collection("popularMakeupList").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        popularMakeupList.clear();
                        List<PopularMakeup> data = value.toObjects(PopularMakeup.class);
                        popularMakeupList.addAll(data);
                        adapter = new PopularMakeupAdapter(getActivity(), popularMakeupList);
                        binding.popularMakeUpRecyclerView.setAdapter(adapter);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });

        binding.addImgBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,300);
        });

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 300) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    popularMakeupImgUri = data.getData();
                    binding.popularMakeupImg.setImageURI(popularMakeupImgUri);
                }
            }
        }

    }
}