package com.example.synerzip.recircle_android.ui;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZoomActivity extends AppCompatActivity {

    private static final String TAG = "ZoomActivity";
    private ArrayList<UserProdImages> userProdImagesArrayList;
    private Bundle bundle;

    private FragmentStatePagerAdapter adapter;

    public ImageView containerImage;
    private int selectedImgPosition;

    @BindView(R.id.toolbar)
    public Toolbar mTollbar;

    @BindView(R.id.view_pager)
    public ViewPager mViewPager;

    @BindView(R.id.img_previous)
    public ImageView mImgPrev;

    @BindView(R.id.img_next)
    public ImageView mImgNext;

    @BindView(R.id.container)
    public LinearLayout mHorizontalContainer;

    @BindView(R.id.horizontalscrollview)
    public HorizontalScrollView mHorizontalScrollView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        ButterKnife.bind(this);

        setSupportActionBar(mTollbar);
        getSupportActionBar().setTitle(R.string.recircle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTollbar.setTitleTextColor(ContextCompat.getColor(this,R.color.common_white));


        bundle = getIntent().getExtras();

        userProdImagesArrayList = bundle.getParcelableArrayList("images_array");
        selectedImgPosition = bundle.getInt("position", 0);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), userProdImagesArrayList);

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(selectedImgPosition);


        inflateImages();

    }

    @OnClick(R.id.img_previous)
    public void showPreviousImage() {
        if (mViewPager.getCurrentItem() > 0) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    @OnClick(R.id.img_next)
    public void showNextImage() {
        if (mViewPager.getCurrentItem() < mViewPager.getAdapter().getCount() - 1) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void inflateImages() {
        for (int i = 0; i < userProdImagesArrayList.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.item_image, null);

            containerImage = (ImageView) view.findViewById(R.id.recycler_img);

            view.setOnClickListener(OnPageChangeListener(i));

            Picasso.with(getApplicationContext())
                    .load(userProdImagesArrayList.get(i).getUser_prod_image_url())
                    .into(containerImage);

            mHorizontalContainer.addView(view);
        }
    }

    private View.OnClickListener OnPageChangeListener(final int i) {
        return new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(i);
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
