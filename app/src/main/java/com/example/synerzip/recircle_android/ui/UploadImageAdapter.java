package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserProdImages;

import java.util.ArrayList;

/**
 * Created by Prajakta Patil on 12/4/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<UserProdImages> mListUserProdImages;

    public UploadImageAdapter(Context mContext, ArrayList<UserProdImages> mListUserProdImages) {
        this.mContext = mContext;
        this.mListUserProdImages = mListUserProdImages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row_images = LayoutInflater.from(mContext).inflate(R.layout.upload_image_item, parent, false);
        return new ViewHolder(row_images);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //creates drawable from file path
        holder.imgItem.setImageDrawable(Drawable.createFromPath(mListUserProdImages.get(position).getUser_prod_image_url()));
    }

    @Override
    public int getItemCount() {
        return mListUserProdImages.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        ImageView imgItemCancel;

        public ViewHolder(View itemView) {
            super(itemView);

            imgItem = (ImageView) itemView.findViewById(R.id.img_upload_item);
            imgItemCancel = (ImageView) itemView.findViewById(R.id.img_upload_cancel);
        }
    }
}
