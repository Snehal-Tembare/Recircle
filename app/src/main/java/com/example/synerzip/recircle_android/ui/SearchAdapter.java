package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Snehal Tembare on 24/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Products> productsArrayList;
    private boolean[] favorite;
    private OnItemClickListener onItemClickListener;

    public SearchAdapter(ResultActivity context, OnItemClickListener onItemClick, ArrayList<Products> productsArrayList) {
        mContext = context;
        this.productsArrayList = productsArrayList;
        onItemClickListener = onItemClick;
        favorite = new boolean[productsArrayList.size()];
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.item_searched, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Products product = productsArrayList.get(position);
        holder.productName.setText(product.getProduct_info().getProduct_title());
        holder.pricePerDay.setText("$" + product.getUser_product_info().getPrice_per_day() + "/day");

        if (product.getUser_product_info().getUser_prod_images().size() != 0
                && product.getUser_product_info().getUser_prod_images().get(0).getUser_prod_image_url() != null
                ) {
            Picasso.with(mContext)
                    .load(product.getUser_product_info().getUser_prod_images()
                            .get(0).getUser_prod_image_url())
                    .placeholder(R.drawable.ic_camera)
                    .into(holder.imgProduct);
        }

        holder.ownerName.setText(product.getUser_info().getFirst_name() + " "
                + product.getUser_info().getLast_name());
        holder.ratings.setRating(Float.parseFloat(product.getUser_product_info()
                .getProduct_avg_rating()));

        holder.imgFavorite.setTag(position);

        holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                ImageView i = (ImageView) v;

                if (favorite[position]) {
                    i.setImageResource(R.drawable.ic_favorite_white);
                    favorite[position] = false;
                } else {
                    i.setImageResource(R.drawable.ic_favorite_red);
                    favorite[position] = true;
                }
            }
        });

        holder.bind(productsArrayList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView productName;
        TextView pricePerDay;
        TextView ownerName;
        RatingBar ratings;
        ImageView imgFavorite;

        ViewHolder(View itemView) {
            super(itemView);
            imgProduct = (ImageView) itemView.findViewById(R.id.img);
            imgFavorite = (ImageView) itemView.findViewById(R.id.img_favorite);
            productName = (TextView) itemView.findViewById(R.id.txt_name);
            pricePerDay = (TextView) itemView.findViewById(R.id.txt_rate);
            ownerName = (TextView) itemView.findViewById(R.id.txt_user_name);
            ratings = (RatingBar) itemView.findViewById(R.id.ratingbar);
        }

        void bind(final Products products, final OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(products);
                }
            });
        }
    }

}
