package com.example.devhub.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devhub.AdaptersClasses.GetFavoriteImageStatusAdapter;
import com.example.devhub.ModelClasses.Model_FavoriteImageStatus;
import com.example.devhub.ModelClasses.Model_FavoriteTextStatus;
import com.example.devhub.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class favoriteImageStatusFragment extends Fragment {

    //Java object for XML
    private RecyclerView objectRecyclerView;

    //Firebase Objects
    FirebaseAuth objectFirebaseAuth;
    FirebaseFirestore objectFirebaseFirestore;

    //Class Objects
    private View parent;
    GetFavoriteImageStatusAdapter objectGetFavoriteImageStatusAdapter;

    public favoriteImageStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_favorite_image_status, container, false);
        initializeJavaObjects();
        getStatusIntoRV();
        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        objectGetFavoriteImageStatusAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        objectGetFavoriteImageStatusAdapter.stopListening();
    }

    private void getStatusIntoRV() {
        try {
            objectFirebaseAuth=FirebaseAuth.getInstance();
            if (objectFirebaseAuth != null) {
                String currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail().toString();
                Query objectQuery = objectFirebaseFirestore.collection("UserFavorite")
                        .document(currentLoggedInUser)
                        .collection("FavouriteImageStatus")
                        .orderBy("currentdatetime", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Model_FavoriteImageStatus> objectOptions
                        = new FirestoreRecyclerOptions.Builder<Model_FavoriteImageStatus>()
                        .setQuery(objectQuery, Model_FavoriteImageStatus.class).build();

                objectGetFavoriteImageStatusAdapter = new GetFavoriteImageStatusAdapter(objectOptions);
                objectRecyclerView.setAdapter(objectGetFavoriteImageStatusAdapter);
                objectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            } else {
                Toast.makeText(getContext(), R.string.no_user_online, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeJavaObjects() {
        try {
            objectRecyclerView = parent.findViewById(R.id.favorite_ImageStatus_RV);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}