package com.example.synerzip.recircle_android.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by Prajakta Patil on 15/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "SearchFragment";

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    protected NavigationView mNavigationView;

    @BindView(R.id.view_pager)
    protected ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    protected TabLayout mTabLayout;

    private PagerAdapter mPagerAdapter;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    private SharedPreferences sharedPreferences;

    private boolean isLoggedIn;

    private String mUserFirstName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mPagerAdapter=new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(new SearchItemFragment(), getString(R.string.tab_search));
        mPagerAdapter.addFragment(new ListItemFragment(), getString(R.string.tab_list));
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        //get data from shared preferences
        sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);
        mUserFirstName = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_FIRST_USERNAME, mUserFirstName);

        //navigation drawer layout
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        mNavigationView.bringToFront();
        mNavigationView.setNavigationItemSelectedListener(this);

        Menu menu = mNavigationView.getMenu();
        MenuItem nav_loggedInAs = menu.findItem(R.id.nav_loggedInAs);

        if (isLoggedIn) {
            menu.removeItem(R.id.nav_logIn_signUp);
            nav_loggedInAs.setVisible(true);
            nav_loggedInAs.setTitle(getString(R.string.logged_in_as) + mUserFirstName);
        } else {
            menu.removeItem(R.id.nav_messages);
            menu.removeItem(R.id.nav_rentals);
            menu.removeItem(R.id.nav_items);
            menu.removeItem(R.id.nav_payments);
            menu.removeItem(R.id.nav_logout);
            menu.removeItem(R.id.nav_loggedInAs);
        }

    }//end onCreate()

    /**
     * PagerAdapter Class
     */
    class PagerAdapter extends FragmentPagerAdapter{
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
    /**
     * navigation drawer item selection
     *
     * @param item
     * @return
     */
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.nav_logIn_signUp:
                startActivity(new Intent(HomeActivity.this, LogInActivity.class));
                break;

            case R.id.nav_logout: {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getResources().getString(R.string.logout_alert_title));
                alertDialog.setPositiveButton(getResources().getString(R.string.logout), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int arg1) {
                        dialogInterface.dismiss();
                        clearData();
                        Intent intent = new Intent(HomeActivity.this, LogInActivity.class);
                        startActivity(intent);
                    }
                });

                alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int arg1) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
                break;
            }
            //TODO functionality yet to be completed for below menu items
            case R.id.nav_messages:
                setVisible(true);
                RCLog.showToast(HomeActivity.this, TAG);
                break;
            case R.id.nav_items:
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                break;
            case R.id.nav_payments:
                RCLog.showToast(HomeActivity.this, TAG);
                break;
            case R.id.nav_rentals:
                RCLog.showToast(HomeActivity.this, TAG);
                if (isLoggedIn){
                    startActivity(new Intent(this,AllRequestsActivity.class));
                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * clears Application data
     */
    private void clearData() {
        SharedPreferences sharedPreferences = getApplicationContext().
                getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        }
        return false;
    }

    /**
     * navigation drawer back button
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
