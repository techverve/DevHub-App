package com.example.devhub.AdaptersClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.devhub.ModelClasses.Model_GetTextComments;
import com.example.devhub.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class GetTextCommentAdapter extends FirestoreRecyclerAdapter<Model_GetTextComments, GetTextCommentAdapter.GetTextCommentsViewHolder> {

    public GetTextCommentAdapter(@NonNull FirestoreRecyclerOptions<Model_GetTextComments> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GetTextCommentsViewHolder holder, int position, @NonNull Model_GetTextComments model) {
        holder.userEmailTV.setText(model.getCommentperson());
        holder.commentDateTV.setText(model.getCurrendatetime());
        holder.commentTV.setText(model.getComment());
        String profileImageUrl = model.getProfilepicurl();

        Glide.with(holder.userProfileIV.getContext())
                .load(profileImageUrl).into(holder.userProfileIV);
    }

    @NonNull
    @Override
    public GetTextCommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetTextCommentsViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.model_text_comments, parent,false));
    }

    public class GetTextCommentsViewHolder extends RecyclerView.ViewHolder {

        ImageView userProfileIV;
        TextView userEmailTV, commentDateTV, commentTV;

        public GetTextCommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfileIV = itemView.findViewById(R.id.model_addcomment_profilePicIV);
            userEmailTV = itemView.findViewById(R.id.model_addcomment_userNameTV);
            commentDateTV = itemView.findViewById(R.id.model_addcomment_currentDateTimeTV);
            commentTV = itemView.findViewById(R.id.model_addcomment_commentTV);

        }
    }
}
