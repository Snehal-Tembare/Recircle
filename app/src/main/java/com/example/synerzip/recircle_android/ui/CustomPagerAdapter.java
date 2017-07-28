package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Snehal Tembare on 12/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class CustomPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<UserProdImages> userProdImagesArrayList;

    public CustomPagerAdapter(Context applicationContext, ArrayList<UserProdImages> userProdImagesArrayList) {
        mContext = applicationContext;
        this.userProdImagesArrayList = userProdImagesArrayList;
    }

    @Override
    public int getCount() {
        return userProdImagesArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_page, container, false);

        TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.page_image);
        imageView.isZoomed();
        if (userProdImagesArrayList.get(position).getUser_prod_image_url() != null) {
            Picasso.with(mContext).load(userProdImagesArrayList.get(position).getUser_prod_image_url())
                   .into(imageView);
        }

        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
