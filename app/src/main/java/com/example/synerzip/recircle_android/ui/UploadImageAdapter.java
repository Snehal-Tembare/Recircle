package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Prajakta Patil on 12/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mListUserProdImages;

    public UploadImageAdapter(Context mContext, ArrayList<String> mListUserProdImages) {
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
        if (position == 0) {
            holder.txtCoverImage.setVisibility(View.VISIBLE);
        }

        File file = new File(mListUserProdImages.get(position));
        Picasso.with(mContext).load(file).into(holder.imgItem);
        holder.imgItem.setAlpha(0.7f);
    }

    @Override
    public int getItemCount() {
        return mListUserProdImages.size();
    }

    /**
     * View holder class
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        ImageView imgItemCancel;
        TextView txtCoverImage;

        public ViewHolder(View itemView) {
            super(itemView);

            imgItem = (ImageView) itemView.findViewById(R.id.img_upload_item);
            imgItemCancel = (ImageView) itemView.findViewById(R.id.img_upload_cancel);
            txtCoverImage = (TextView) itemView.findViewById(R.id.txt_cover_image);

            imgItemCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == imgItemCancel.getId()) {
                        String itemLabel = mListUserProdImages.get(getAdapterPosition());
                        mListUserProdImages.remove(itemLabel);
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mListUserProdImages.size());

                    }
                }
            });
        }

    }
}
