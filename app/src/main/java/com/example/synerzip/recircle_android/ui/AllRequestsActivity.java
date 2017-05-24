package com.example.synerzip.recircle_android.ui;

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

import com.example.synerzip.recircle_android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Snehal Tembare on 19/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class AllRequestsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

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
        getSupportActionBar().setTitle(getString(R.string.pending_requests));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.common_white));

        setUpViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setUpViewPager(ViewPager upViewPager) {
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RequestToMeFragment(),"Request to me");
        adapter.addFragment(new RequestFromMeFragment(),"Request from me");
        mViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

     private class ViewPagerAdapter extends FragmentPagerAdapter {
         private final List<Fragment> mFragmentList=new ArrayList<>();
         private final List<String> mFrgmentTitleList=new ArrayList<>();

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

        public void addFragment(Fragment fragment,String title){
            mFragmentList.add(fragment);
            mFrgmentTitleList.add(title);
        }

         @Override
         public CharSequence getPageTitle(int position) {
             return mFrgmentTitleList.get(position);
         }
     }
}
