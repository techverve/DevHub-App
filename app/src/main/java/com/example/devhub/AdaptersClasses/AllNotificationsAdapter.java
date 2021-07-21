package com.example.devhub.AdaptersClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.devhub.ModelClasses.Model_AllNotifications;
import com.example.devhub.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AllNotificationsAdapter extends FirestoreRecyclerAdapter<Model_AllNotifications, AllNotificationsAdapter.AllNotificationsViewHolder> {

    FirebaseFirestore objectFirebaseFirestore=FirebaseFirestore.getInstance();
    public AllNotificationsAdapter(@NonNull FirestoreRecyclerOptions<Model_AllNotifications> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AllNotificationsViewHolder holder, int position, @NonNull Model_AllNotifications model) {
        holder.userEmailTV.setText(model.getEmail());
        String actionOfUser=model.getAction();
        String type=model.getType();
        String finalStatus=actionOfUser+" your "+type;
        holder.userActionTV.setText(finalStatus);
        objectFirebaseFirestore.collection("UserProfileData")
                .document(model.getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String linkOfProfilePic=documentSnapshot.getString("profileimageurl");
                        Glide.with(holder.userProfilePicIV.getContext())
                                .load(linkOfProfilePic).into(holder.userProfilePicIV);
                    }
                });
    }

    @NonNull
    @Override
    public AllNotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllNotificationsViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.model_all_notifications,parent,false));
    }

    public class AllNotificationsViewHolder extends RecyclerView.ViewHolder{
        ImageView userProfilePicIV;
        TextView userEmailTV,userActionTV;

        public AllNotificationsViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfilePicIV=itemView.findViewById(R.id.model_allNotifications_profilePicIV);
            userEmailTV=itemView.findViewById(R.id.model_allNotifications_userEmail);
            userActionTV=itemView.findViewById(R.id.model_allNotifications_action);
        }
    }
}
