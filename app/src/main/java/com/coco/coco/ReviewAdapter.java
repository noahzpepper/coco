package com.coco.coco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coco.coco.model.Review;
import com.coco.coco.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private ArrayList<Review> reviews;

    public ReviewAdapter() {
        reviews = new ArrayList<>();
    }

    public void setData(ArrayList<Review> data) {
        reviews = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_review, parent, false);
        return new ReviewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Review review = reviews.get(position);
        FirebaseDatabase.getInstance().getReference().child("users").child(review.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) { return; }
                holder.mReviewName.setText(user.getName());
                holder.mReviewText.setText(review.getReviewText());
                holder.mReviewOverallRating.setText(String.format(Locale.US, "%d     overall", review.getOverallRating()));
                holder.mReviewSkintoneRating.setText(String.format(Locale.US, "%d     skin tone match", review.getSkinToneRating()));
                user.loadUserPhotoIntoImageView(holder.mReviewPic);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public int getItemCount() {
        if (reviews == null) { return 0; }
        return reviews.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mReviewPic;
        TextView mReviewName, mReviewOverallRating, mReviewSkintoneRating, mReviewText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mReviewPic = itemView.findViewById(R.id.mReviewPic);
            mReviewName = itemView.findViewById(R.id.mReviewName);
            mReviewOverallRating = itemView.findViewById(R.id.mReviewOverallRating);
            mReviewSkintoneRating = itemView.findViewById(R.id.mReviewSkintoneRating);
            mReviewText = itemView.findViewById(R.id.mReviewText);
        }

    }
}
