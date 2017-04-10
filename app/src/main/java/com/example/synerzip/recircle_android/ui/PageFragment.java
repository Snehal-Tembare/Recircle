package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.synerzip.recircle_android.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Snehal Tembare on 7/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class PageFragment extends Fragment {

    private String imageUrl;

    private PhotoViewAttacher attacher;

    public ImageView mImgMain;

    public static PageFragment getInstance(String imageUrl) {
        PageFragment pf = new PageFragment();
        Bundle args = new Bundle();
        args.putString("url", imageUrl);
        pf.setArguments(args);
        return pf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUrl = getArguments().getString("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mImgMain = (ImageView) view.findViewById(R.id.image);
        attacher = new PhotoViewAttacher(mImgMain);
        attacher.update();

        Picasso.with(getActivity()).load(imageUrl).into(mImgMain);

        mImgMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && ((ZoomActivity) getActivity()).mReImages.getVisibility() == View.VISIBLE) {
                    ((ZoomActivity) getActivity()).mReImages.setVisibility(View.GONE);
                }else if (event.getAction() == MotionEvent.ACTION_DOWN && ((ZoomActivity) getActivity()).mReImages.getVisibility() == View.GONE) {
                    ((ZoomActivity) getActivity()).mReImages.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

    }
}
