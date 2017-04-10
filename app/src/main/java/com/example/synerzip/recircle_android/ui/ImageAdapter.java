package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * mRecyclerImages
 * Created by Snehal Tembare on 6/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<UserProdImages> userProdImagesArrayList;
    private OnImageItemClickListener onImageItemClickListener;
    private int selectedImgPosition;

    ImageAdapter(Context context, int selectedImgPosition, ArrayList<UserProdImages> userProdImagesArrayList, OnImageItemClickListener onImageItemClickListener) {
        this.mContext = context;
        this.userProdImagesArrayList = userProdImagesArrayList;
        this.onImageItemClickListener = onImageItemClickListener;
        this.selectedImgPosition = selectedImgPosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserProdImages userProdImages = userProdImagesArrayList.get(position);

        Picasso.with(mContext).load(userProdImages.getUser_prod_image_url()).into(holder.imageView);

        holder.bind(position, userProdImagesArrayList.get(position), onImageItemClickListener);

        if (selectedImgPosition == position) {
            holder.layout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selected_image_background));
        }
    }

    @Override
    public int getItemCount() {
        return userProdImagesArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recycler_img);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }

        public void bind(final int position, final UserProdImages userProdImages, final OnImageItemClickListener onImageItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    onImageItemClickListener.onImageClick(position, userProdImages);
                }
            });
        }
    }

    interface OnImageItemClickListener {
        void onImageClick(int position, UserProdImages userProdImages);
    }
}
