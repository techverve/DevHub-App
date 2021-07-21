package com.example.devhub.AdaptersClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.devhub.ModelClasses.Model_GetImageComments;
import com.example.devhub.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class GetImageCommentsAdapter extends FirestoreRecyclerAdapter<Model_GetImageComments, GetImageCommentsAdapter.GetImageCommentsViewHolder>
{
    public GetImageCommentsAdapter(@NonNull FirestoreRecyclerOptions<Model_GetImageComments> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GetImageCommentsViewHolder holder, int position, @NonNull Model_GetImageComments model) {
        holder.userEmailTV.setText(model.getCommentperson());
        holder.commentDateTV.setText(model.getCurrentdatetime());
        holder.commentTV.setText(model.getComment());
        String profileImageUrl = model.getProfilepicurl();

        Glide.with(holder.userProfileIV.getContext())
                .load(profileImageUrl).into(holder.userProfileIV);
    }

    @NonNull
    @Override
    public GetImageCommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetImageCommentsViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.model_image_comments, parent, false
        ));
    }

    public class GetImageCommentsViewHolder extends RecyclerView.ViewHolder
    {
        ImageView userProfileIV;
        TextView userEmailTV, commentDateTV, commentTV;
        public GetImageCommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfileIV = itemView.findViewById(R.id.model_addImagecomment_profilePicIV);
            userEmailTV = itemView.findViewById(R.id.model_addImagecomment_userNameTV);

            commentDateTV = itemView.findViewById(R.id.model_addImagecomment_currentDateTimeTV);
            commentTV = itemView.findViewById(R.id.model_addImagecomment_commentTV);

        }
    }
}
