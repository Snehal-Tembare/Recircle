package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Prajakta Patil on 12/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mListUploadImagePath;
    private ArrayList<UserProdImages> uploadImageObjectList;

/*    public UploadImageAdapter(Context mContext, ArrayList<String> mListUploadImagePath) {
        this.mContext = mContext;
        this.mListUploadImagePath = mListUploadImagePath;
    }*/

    public UploadImageAdapter(Context mContext, ArrayList<UserProdImages> uploadImageObjectList) {
        this.mContext = mContext;
        this.uploadImageObjectList = uploadImageObjectList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row_images = LayoutInflater.from(mContext).inflate(R.layout.upload_image_item, parent, false);
        return new ViewHolder(row_images);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File file;
        if (position == 0) {
            holder.txtCoverImage.setVisibility(View.VISIBLE);
            holder.imgItem.setAlpha(0.7f);
        }

        file = new File(uploadImageObjectList.get(position).getUser_prod_image_url());
        Picasso.with(mContext).load(file).into(holder.imgItem);

        if (uploadImageObjectList.get(position).getUser_prod_image_url().startsWith("http")) {
            Picasso.with(mContext).load(uploadImageObjectList.get(position).getUser_prod_image_url()).into(holder.imgItem);
        } else {
            Picasso.with(mContext).load(file).into(holder.imgItem);
        }
    }

    @Override
    public int getItemCount() {
        return uploadImageObjectList.size();
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
                        uploadImageObjectList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), uploadImageObjectList.size());
                        if (ListItemSummaryActivity.isSummaryActivity){
                            imgItemCancel.setVisibility(View.GONE);
                        }else {
                            imgItemCancel.setVisibility(View.VISIBLE);

                        }
                        Log.v("UploadImages Adapter","size"+UploadImgActivity.uploadImageObjectList.size());

                    }
                }
            });
        }

    }
}
