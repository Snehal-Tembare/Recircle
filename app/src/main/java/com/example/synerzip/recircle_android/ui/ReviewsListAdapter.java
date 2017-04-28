package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserProdReview;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import at.blogc.android.views.ExpandableTextView;

/**
 * Created by Snehal Tembare on 3/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.ViewHolder> {
    private static final int MAX_ROWS_DISPLAY = 4;
    private Context mContext;
    private ArrayList<UserProdReview> userProdReviewArrayList;


    ReviewsListAdapter(Context context, ArrayList<UserProdReview> userProdReviewArrayList) {
        mContext = context;
        this.userProdReviewArrayList = userProdReviewArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.item_reviews, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        UserProdReview review = userProdReviewArrayList.get(position);

        holder.reviewersName.setText(review.getUser().getFirst_name() + " " + review.getUser().getLast_name());
        Picasso.with(mContext).load(review.getUser().getUser_image_url()).into(holder.reviewersImage);
        if (null != review.getProd_rating() && review.getProd_rating() != 0) {
            holder.rating.setRating(review.getProd_rating());
            holder.avgRatingCount.setText("(" + review.getProd_rating() + ")");
        } else {
            holder.rating.setVisibility(View.GONE);
            holder.avgRatingCount.setVisibility(View.GONE);
        }
        holder.reviewComment.setText(review.getProd_review());


        holder.reviewComment.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final int lineCount = holder.reviewComment.getLayout().getLineCount();
                Log.v("Count", "onPreDraw" + lineCount);
                if (lineCount > 2) {
                    holder.reviewSeeMore.setVisibility(View.VISIBLE);
                } else {
                    holder.reviewSeeMore.setVisibility(View.GONE);
                }
                return true;
            }
        });


        holder.reviewSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.reviewComment.toggle();
                holder.reviewSeeMore.setText(holder.reviewComment.isExpanded() ? R.string.see_more : R.string.view_less);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (userProdReviewArrayList == null) {
            return 0;
        }
        return Math.min(MAX_ROWS_DISPLAY, userProdReviewArrayList.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircularImageView reviewersImage;
        TextView reviewersName;
        RatingBar rating;
        TextView avgRatingCount;
        ExpandableTextView reviewComment;
        TextView reviewSeeMore;
        CardView container;

        ViewHolder(View itemView) {
            super(itemView);
            reviewersImage = (CircularImageView) itemView.findViewById(R.id.img_reviewers);
            reviewersName = (TextView) itemView.findViewById(R.id.txt_reviewer_name);
            avgRatingCount = (TextView) itemView.findViewById(R.id.txt_datails_rating_count);
            reviewComment = (ExpandableTextView) itemView.findViewById(R.id.txt_review_comment);
            rating = (RatingBar) itemView.findViewById(R.id.ratingbar);
            reviewSeeMore = (TextView) itemView.findViewById(R.id.txt_review_see_more);
            container = (CardView) itemView.findViewById(R.id.reviews_layout);

        }
    }
}
