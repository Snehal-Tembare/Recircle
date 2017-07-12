package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.synerzip.recircle_android.R;

import java.util.ArrayList;

/**
 * Created by Prajakta Patil on 24/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

class ListItemImageAdapter extends RecyclerView.Adapter<ListItemImageAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mListUserProdImages;
    private ListItemImageAdapter.OnImageItemClickListener onImageItemClickListener;
    private int selectedImgPosition;

    ListItemImageAdapter(Context context, int selectedImgPosition, ArrayList<String> mListUserProdImages,
                         ListItemImageAdapter.OnImageItemClickListener onImageItemClickListener) {
        this.mContext = context;
        this.mListUserProdImages = mListUserProdImages;
        this.onImageItemClickListener = onImageItemClickListener;
        this.selectedImgPosition = selectedImgPosition;
    }

    @Override
    public ListItemImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.upload_image_recycler_view, parent, false);
        return new ListItemImageAdapter.ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bind(position, mListUserProdImages.get(position), onImageItemClickListener);
        holder.imageView.setBackground(Drawable.createFromPath(mListUserProdImages.get(position)));

        if (selectedImgPosition == position) {
            holder.layout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selected_image_background));
        }

    }

    @Override
    public int getItemCount() {
        return mListUserProdImages.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recycler_upload_img);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_upload_image);
        }

        public void bind(final int position, final String userProdImages,
                         final ListItemImageAdapter.OnImageItemClickListener onImageItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageItemClickListener.onImageClick(position, userProdImages);
                }
            });
        }

    }

    interface OnImageItemClickListener {
        void onImageClick(int position, String userProdImages);
    }
}
