package com.example.devhub.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devhub.AdaptersClasses.GetFavoriteTextStatusAdapter;
import com.example.devhub.ModelClasses.Model_FavoriteTextStatus;
import com.example.devhub.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class favoriteTextStatusFragment extends Fragment {

    //Class Variables
    private View parent;
    private RecyclerView objectRecyclerView;

    GetFavoriteTextStatusAdapter objectGetFavoriteTextStatusAdapter;
    //Firebase variables
    FirebaseAuth objectFirebaseAuth;
    FirebaseFirestore objectFirebaseFirestore;

    public favoriteTextStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent=inflater.inflate(R.layout.fragment_favorite_text_status, container, false);
        initializeVariables();
        getStatusIntoRV();
        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        objectGetFavoriteTextStatusAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        objectGetFavoriteTextStatusAdapter.stopListening();
    }

    private void getStatusIntoRV(){
        try{
            objectFirebaseAuth=FirebaseAuth.getInstance();
            if(objectFirebaseAuth!=null){
                String currentLoggedInUser=objectFirebaseAuth.getCurrentUser().getEmail();
                Query objectQuery=objectFirebaseFirestore.collection("UserFavorite")
                        .document(currentLoggedInUser).collection("FavoriteTechStatus")
                        .orderBy("currentdatetime", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Model_FavoriteTextStatus> objectOptions=
                        new FirestoreRecyclerOptions.Builder<Model_FavoriteTextStatus>().
                                setQuery(objectQuery,Model_FavoriteTextStatus.class).build();
                objectGetFavoriteTextStatusAdapter=new GetFavoriteTextStatusAdapter(objectOptions);
                objectRecyclerView.setAdapter(objectGetFavoriteTextStatusAdapter);

                objectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
            else{
                Toast.makeText(getContext(),R.string.no_user_online,Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void initializeVariables(){
        try{
            objectRecyclerView=parent.findViewById(R.id.favoriteTS_RV);
            objectFirebaseFirestore=FirebaseFirestore.getInstance();
        }
        catch(Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}