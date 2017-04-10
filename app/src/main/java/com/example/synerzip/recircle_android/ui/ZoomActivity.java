package com.example.synerzip.recircle_android.ui;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserProdImages;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZoomActivity extends AppCompatActivity {

    private ArrayList<UserProdImages> userProdImagesArrayList;
    private Bundle bundle;

    private FragmentStatePagerAdapter adapter;

    private int selectedImgPosition;

    public ImageAdapter mImageAdapter;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.view_pager)
    public ViewPager mViewPager;

    @BindView(R.id.img_previous)
    public ImageView mImgPrev;

    @BindView(R.id.img_next)
    public ImageView mImgNext;

    @BindView(R.id.recycler_images)
    public RecyclerView mReImages;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.recircle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));


        bundle = getIntent().getExtras();

        userProdImagesArrayList = bundle.getParcelableArrayList(getString(R.string.image_urls_array));
        selectedImgPosition = bundle.getInt(getString(R.string.selected_image_position), 0);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), userProdImagesArrayList);

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(selectedImgPosition);

        mImageAdapter = new ImageAdapter(getApplicationContext(), selectedImgPosition,userProdImagesArrayList, new ImageAdapter.OnImageItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onImageClick(int position, UserProdImages userProdImages) {

                mViewPager.setCurrentItem(position);

                View view = mReImages.getChildAt(position);

                view.setBackground(getDrawable(R.drawable.selected_image_background));

                for (int i = 0; i < userProdImagesArrayList.size(); i++) {
                    view = mReImages.getChildAt(i);
                    if (i != position) {
                        view.setBackground(getDrawable(R.drawable.custom_imageview));
                    }
                }
            }
        });

        mReImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mReImages.setAdapter(mImageAdapter);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.img_previous)
    public void showPreviousImage() {
        if (mViewPager.getCurrentItem() > 0) {
          mReImages.setVisibility(View.VISIBLE);

            int position=mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);

            View view=mReImages.getChildAt(mViewPager.getCurrentItem());
            view.setBackground(getDrawable(R.drawable.selected_image_background));

            for (int i = 0; i < userProdImagesArrayList.size(); i++) {
                view = mReImages.getChildAt(i);
                if (i == position) {
                    view.setBackground(getDrawable(R.drawable.custom_imageview));
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.img_next)
    public void showNextImage() {
        if (mViewPager.getCurrentItem() < mViewPager.getAdapter().getCount() - 1) {
            mReImages.setVisibility(View.VISIBLE);
            int position=mViewPager.getCurrentItem();

            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);

            View view=mReImages.getChildAt(mViewPager.getCurrentItem());
            view.setBackground(getDrawable(R.drawable.selected_image_background));

            for (int i = 0; i < userProdImagesArrayList.size(); i++) {
                view = mReImages.getChildAt(i);
                if (i == position) {
                    view.setBackground(getDrawable(R.drawable.custom_imageview));
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
