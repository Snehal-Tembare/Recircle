package com.example.synerzip.recircle_android.ui.messages;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.ui.HomeActivity;
import com.example.synerzip.recircle_android.ui.ListItemFragment;
import com.example.synerzip.recircle_android.ui.SearchItemFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.PUT;

/**
 * Created by Prajakta Patil on 7/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class AllMessagesActivity extends AppCompatActivity {

    @BindView(R.id.progress_bar)
    protected RelativeLayout mProgressBar;

    @BindView(R.id.frame_layout)
    protected FrameLayout mFrameLayout;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.view_pager_msgs)
    protected ViewPager mViewPager;

    @BindView(R.id.tab_layout_msgs)
    protected TabLayout mTabLayout;

    //TODO screen not implememted

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_messages);
        ButterKnife.bind(this);

        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mToolbar.setTitle(R.string.toolbar_msgs);

        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(new OwnerMsgFragment(), getString(R.string.tab_msgs_owner));
        mPagerAdapter.addFragment(new RenterMsgFragment(), getString(R.string.tab_msgs_renter));
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }
    /**
     * PagerAdapter Class
     */
    class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
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
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

}
