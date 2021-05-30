package com.abishek.comida.review;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abishek.comida.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_IMAGE;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private ArrayList<ReviewModel> reviewList;
    private Context context;

    public ReviewAdapter(ArrayList<ReviewModel> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_recycler_design,parent,false);

        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {

        ReviewModel reviewItem = reviewList.get(position);

        holder.nameView.setText(reviewItem.getCustomerName());
        holder.commentView.setText(reviewItem.getComment());
        holder.ratingBar.setRating(Integer.parseInt(reviewItem.getRating()));
        Picasso.get().load(BASE_IMAGE+reviewItem.getCustomerImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ReviewHolder extends RecyclerView.ViewHolder{

        private TextView nameView,commentView;
        private ImageView imageView;
        private RatingBar ratingBar;

        public ReviewHolder(@NonNull View v) {
            super(v);

            nameView = v.findViewById(R.id.customer_name);
            imageView = v.findViewById(R.id.customer_image);
            commentView = v.findViewById(R.id.comment);
            ratingBar = v.findViewById(R.id.rating_bar);
        }
    }
}
