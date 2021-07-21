package com.example.devhub.Fragments;

import android.app.DownloadManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.service.voice.AlwaysOnHotwordDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devhub.AdaptersClasses.TechStatusAdapterClass;
import com.example.devhub.ModelClasses.Model_TechStatus;
import com.example.devhub.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TechThoughts extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    public TechThoughts(){

    }

    //XML Varibles
    private RecyclerView objectRecyclerView;
    //Class Variables
    private View parent;
    private TechStatusAdapterClass objectTechStatusAdapterClass;

    //Firebase Variables
    FirebaseFirestore objectFirebaseFirestore;

    // TODO: Rename and change types and number of parameters
    /*public static TechThoughts newInstance(String param1, String param2) {
        TechThoughts fragment = new TechThoughts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent=inflater.inflate(R.layout.fragment_tech_thoughts, container, false);
        objectRecyclerView=parent.findViewById(R.id.techStatus_RV);
        objectFirebaseFirestore=FirebaseFirestore.getInstance();
        addStatusToRV();
        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {

            objectTechStatusAdapterClass.startListening();

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            objectTechStatusAdapterClass.stopListening();

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addStatusToRV()
    {
        try{
           Query objectQuery =objectFirebaseFirestore.collection("TextStatus")
                    .orderBy("currentdatetime",Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<Model_TechStatus> options=new FirestoreRecyclerOptions.Builder<Model_TechStatus>()
                    .setQuery(objectQuery, Model_TechStatus.class).build();
            objectTechStatusAdapterClass=new TechStatusAdapterClass(options);
            objectRecyclerView.setAdapter(objectTechStatusAdapterClass);
            objectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        catch(Exception e)
        {
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}