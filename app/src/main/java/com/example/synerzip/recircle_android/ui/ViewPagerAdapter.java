package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.synerzip.recircle_android.models.UserProdImages;

import java.util.ArrayList;

/**
 * Created by Snehal Tembare on 7/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<UserProdImages> userProdImagesArrayList;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<UserProdImages> userProdImagesArrayList ) {
        super(fm);
        this.userProdImagesArrayList = userProdImagesArrayList;
        this.userProdImagesArrayList.addAll(userProdImagesArrayList);
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.getInstance(userProdImagesArrayList.get(position).getUser_prod_image_url());
    }

    @Override
    public int getCount() {
        return userProdImagesArrayList.size();
    }
}
