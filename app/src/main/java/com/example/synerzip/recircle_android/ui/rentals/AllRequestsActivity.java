package com.example.synerzip.recircle_android.ui.rentals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.synerzip.recircle_android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.synerzip.recircle_android.models.OrderDetails;
import com.example.synerzip.recircle_android.models.UserRentings;
import com.example.synerzip.recircle_android.models.UserRequests;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.ui.HomeActivity;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;

/**
 * Created by Snehal Tembare on 19/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class AllRequestsActivity extends AppCompatActivity{
    private static final String TAG = "AllRequestsActivity";
    public ArrayList<UserRequests> userRequestsArrayList;
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
    RequestToMeFragment requestToMeFragment;
    RequestFromMeFragment requestFromMeFragment ;



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

        requestToMeFragment = new RequestToMeFragment();
        requestFromMeFragment = new RequestFromMeFragment();

        SharedPreferences sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
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
                        if (response.body().getUserRequests() != null
                                && response.body().getUserRequests().size() != 0) {
                            userRequestsArrayList = response.body().getUserRequests();
                            Log.v(TAG, " " + response.body().getUserRequests().get(0).getProduct().getProduct_title());
                            requestToMeFragment.communicateWithActivity(userRequestsArrayList);
                            mProgressBar.setVisibility(View.GONE);
                        }
                        if (response.body().getUserRentings() != null
                                && response.body().getUserRentings().size() != 0) {
                            userRentingsArrayList = response.body().getUserRentings();
                            Log.v(TAG, " " + response.body().getUserRentings().get(0).getProduct().getProduct_title());
                            requestFromMeFragment.communicateWithActivity(userRentingsArrayList);
                            mProgressBar.setVisibility(View.GONE);
                        }


                    }
                }else if (response.code() != RCWebConstants.RC_ERROR_CODE_FORBIDDEN ||
                        response.code() == RCWebConstants.RC_ERROR_UNAUTHORISED) {
                    mProgressBar.setVisibility(View.GONE);
                    RCLog.showToast(getApplicationContext(), getString(R.string.something_went_wrong));
                    //TODO
                    //loginDialog();
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    RCLog.showToast(getApplicationContext(), getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<OrderDetails> call, Throwable t) {
                Log.v(TAG, "onFailure");
                mProgressBar.setVisibility(View.GONE);
                RCLog.showToast(getApplicationContext(), getString(R.string.something_went_wrong));
            }
        });

        setUpViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setUpViewPager(ViewPager upViewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(requestToMeFragment, getString(R.string.user_requests_to_me));
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
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mFragmentList.remove(position);
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
