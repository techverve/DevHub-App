package com.example.devhub.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devhub.AdaptersClasses.ImageStatusAdapterClass;
import com.example.devhub.ModelClasses.Model_ImageStatus;
import com.example.devhub.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ImageThoughts extends Fragment {

    public ImageThoughts() {
        // Required empty public constructor
    }

    //XML Variables

    //Class Variables
    private View parent;
    private RecyclerView objectRecyclerview;
    private ImageStatusAdapterClass objectImageStatusAdapterClass;

    //  Firebase Variables
    private FirebaseFirestore objectFirebaseFirestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_image_thoughts, container, false);

        attachJAVAObjectsToXML();
        getImageStatuses();
        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            objectImageStatusAdapterClass.startListening();
        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            objectImageStatusAdapterClass.stopListening();
        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void getImageStatuses(){
        try {
            Query objectQuery = objectFirebaseFirestore.collection("ImageStatus").orderBy("currentdatetime", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<Model_ImageStatus> objectOptions =
                    new FirestoreRecyclerOptions.Builder<Model_ImageStatus>()
                    .setQuery(objectQuery, Model_ImageStatus.class).build();

            objectImageStatusAdapterClass = new ImageStatusAdapterClass(objectOptions);
            objectRecyclerview.setAdapter(objectImageStatusAdapterClass);

            objectRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void attachJAVAObjectsToXML(){
        try {
            objectRecyclerview = parent.findViewById(R.id.imageStatus_RV);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}