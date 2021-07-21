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
import com.example.devhub.ModelClasses.Model_FavoriteImageStatus;
import com.example.devhub.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class GetFavoriteImageStatusAdapter extends FirestoreRecyclerAdapter<Model_FavoriteImageStatus, GetFavoriteImageStatusAdapter.GetFavoriteImageStatusViewHolder>{

    public GetFavoriteImageStatusAdapter(@NonNull FirestoreRecyclerOptions<Model_FavoriteImageStatus> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GetFavoriteImageStatusViewHolder holder, int position, @NonNull Model_FavoriteImageStatus model) {
        String linkOfImageStatus=model.getStatusimageurl();
        String linkOfProfilePic=model.getProfileurl();

        holder.userEmailTV.setText(model.getUseremail());
        holder.dateOfStatusTV.setText(model.getCurrentdatetime());
        holder.statusDescriptionTV.setText(model.getStatus());

        Glide.with(holder.statusIV.getContext())
                .load(linkOfImageStatus).into(holder.statusIV);
        Glide.with(holder.profilePicIV.getContext())
                .load(linkOfProfilePic).into(holder.profilePicIV);

        holder.removeStatusIV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseAuth objectFirebaseAuth=FirebaseAuth.getInstance();
                if(objectFirebaseAuth!=null){
                    FirebaseFirestore objectFirebaseFirestore=FirebaseFirestore.getInstance();
                    objectFirebaseFirestore.collection("UserFavorite").document(
                            objectFirebaseAuth.getCurrentUser().getEmail().toString()
                    ).collection("FavouriteImageStatus")
                            .document(getSnapshots().getSnapshot(holder.getAdapterPosition()).getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(holder.removeStatusIV.getContext(),R.string.status_deleted_successfully,Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(holder.removeStatusIV.getContext(),R.string.fails_to_remove_status,Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else{
                    Toast.makeText(holder.removeStatusIV.getContext(),R.string.no_user_online,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @NonNull
    @Override
    public GetFavoriteImageStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetFavoriteImageStatusViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.model_favorite_is,parent,false
        ));
    }

    public class GetFavoriteImageStatusViewHolder extends RecyclerView.ViewHolder{
        ImageView statusIV, profilePicIV,removeStatusIV;
        TextView userEmailTV,dateOfStatusTV, statusDescriptionTV;

        public GetFavoriteImageStatusViewHolder(@NonNull View itemView){
            super(itemView);
            statusIV=itemView.findViewById(R.id.model_favorite_is_StatusIV);
            profilePicIV=itemView.findViewById(R.id.model_favorite_is_profilePicIV);
            removeStatusIV=itemView.findViewById(R.id.model_favorite_is_removeStatusIV);

            userEmailTV=itemView.findViewById(R.id.model_favorite_is_userEmailTV);
            dateOfStatusTV=itemView.findViewById(R.id.model_favorite_is_dateOfStatusTV);
            statusDescriptionTV=itemView.findViewById(R.id.model_favorite_is_statusDescriptTV);
        }
    }
}
