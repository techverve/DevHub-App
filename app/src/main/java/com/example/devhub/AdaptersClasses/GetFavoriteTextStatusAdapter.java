package com.example.devhub.AdaptersClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.devhub.ModelClasses.Model_FavoriteTextStatus;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.example.devhub.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class GetFavoriteTextStatusAdapter extends FirestoreRecyclerAdapter<Model_FavoriteTextStatus, GetFavoriteTextStatusAdapter.GetFavoriteTextStatusViewHolder>{
    public GetFavoriteTextStatusAdapter(@NonNull FirestoreRecyclerOptions<Model_FavoriteTextStatus> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GetFavoriteTextStatusViewHolder holder, int position, @NonNull Model_FavoriteTextStatus model) {
        holder.userEmailTV.setText(model.getUseremail());
        holder.dateOfStatusTV.setText(model.getCurrentdatetime());
        holder.statusTV.setText(model.getStatus());
        String linkOfProfilePic=model.getProfileurl();
        Glide.with(holder.profilePicIV.getContext())
                .load(linkOfProfilePic).into(holder.profilePicIV);
        holder.removeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objectFirebaseAuth=FirebaseAuth.getInstance();
                if(objectFirebaseAuth!=null){
                    String currentLoggedInUser=objectFirebaseAuth.getCurrentUser().getEmail();
                    FirebaseFirestore objectFirebaseFirestore=FirebaseFirestore.getInstance();

                    objectFirebaseFirestore.collection("UserFavorite").document(currentLoggedInUser)
                            .collection("FavoriteTechStatus")
                            .document(
                                    getSnapshots().getSnapshot(holder.getAdapterPosition()).getId()
                            )
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(holder.removeIV.getContext(), R.string.status_deleted_successfully,Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(holder.removeIV.getContext(), R.string.fails_to_remove_status,Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else{
                    Toast.makeText(holder.removeIV.getContext(),R.string.no_user_online,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @NonNull
    @Override
    public GetFavoriteTextStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetFavoriteTextStatusViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.model_favorite_ts,parent,false
        ));
    }

    public class GetFavoriteTextStatusViewHolder extends RecyclerView.ViewHolder{
        ImageView removeIV,profilePicIV;
        TextView userEmailTV,dateOfStatusTV,statusTV;

        public GetFavoriteTextStatusViewHolder(@NonNull View itemView){
            super(itemView);
            removeIV=itemView.findViewById(R.id.model_favorite_ts_removeStatusIV);
            profilePicIV=itemView.findViewById(R.id.model_favorite_ts_profilePicIV);
            userEmailTV=itemView.findViewById(R.id.model_favorite_ts_emailTV);
            dateOfStatusTV=itemView.findViewById(R.id.model_favorite_ts_dateTV);
            statusTV=itemView.findViewById(R.id.model_favorite_ts_statusTV);
        }
    }
}
