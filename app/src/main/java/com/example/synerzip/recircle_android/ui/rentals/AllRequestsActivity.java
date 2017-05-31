package com.example.synerzip.recircle_android.ui.rentals;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.synerzip.recircle_android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.synerzip.recircle_android.models.UserRentings;
import com.example.synerzip.recircle_android.models.UserRequests;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.ui.HomeActivity;

/**
 * Created by Snehal Tembare on 19/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class AllRequestsActivity extends AppCompatActivity implements
        RequestToMeFragment.OnFragmentInteractionListener {
    private static final String TAG = "AllRequestsActivity";
    private ArrayList<UserRequests> userRequestsArrayList;
    public ArrayList<UserRentings> userRentingsArrayList;
    private ViewPagerAdapter adapter;

    private RCAPInterface service;
    private String mUserId;
    private String mAccessToken;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.progress_bar)
    public RelativeLayout mProgressBar;

    @BindView(R.id.viewpager)
    protected ViewPager mViewPager;

    @BindView(R.id.tabs)
    protected TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_requests);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.rentals));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        setUpViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);

       /* SharedPreferences sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        mUserId = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_USERID, mUserId);
        mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);

        service = ApiClient.getClient().create(RCAPInterface.class);
        Call<OrderDetails> call = service.getOrderDetails("Bearer " + mAccessToken);
        mProgressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<OrderDetails>() {
            @Override
            public void onResponse(Call<OrderDetails> call, Response<OrderDetails> response) {
                Log.v(TAG, "before isSuccessful");
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getUserRentings() != null) {
                            Log.v(TAG, " " + response.body().getUserRentings().get(0).getProduct().getProduct_title());
                        }
                    }
                } else if (response.code() == RCWebConstants.RC_ERROR_UNAUTHORISED) {
                    Log.v(TAG, "RC_ERROR_UNAUTHORISED");
                } else if (response.code() == RCWebConstants.RC_ERROR_CODE_FORBIDDEN) {
                    Log.v(TAG, "RC_ERROR_CODE_FORBIDDEN");
                }
            }

            @Override
            public void onFailure(Call<OrderDetails> call, Throwable t) {
                Log.v(TAG, "onFailure");

            }
        });*/

    }

    private void setUpViewPager(ViewPager upViewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RequestToMeFragment(), getString(R.string.user_requests_to_me));

        RequestFromMeFragment requestFromMeFragment = new RequestFromMeFragment();
        adapter.addFragment(requestFromMeFragment, getString(R.string.user_requests_from_me));

        mViewPager.setAdapter(adapter);
        mViewPager.requestLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void sendDataToActivity(ArrayList<UserRentings> userRentingsArrayList) {
        this.userRentingsArrayList = userRentingsArrayList;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFrgmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFrgmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFrgmentTitleList.get(position);
        }
    }
}
