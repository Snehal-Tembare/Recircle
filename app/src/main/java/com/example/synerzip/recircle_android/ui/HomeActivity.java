package com.example.synerzip.recircle_android.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.user_messages.RootMessageInfo;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.ui.messages.UserQueAnsActivity;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private String mUserFirstName, mUserLastName, mUserEmail, mUserImage, mAccessToken = "";

    private RCAPInterface service;

    private int mProdRelatedMsgs;

    @BindView(R.id.progress_bar_home)
    protected RelativeLayout mProgressBar;

    @BindView(R.id.frame_layout)
    protected FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mProgressBar.setVisibility(View.VISIBLE);
        mFrameLayout.setAlpha((float) 0.6);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
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
        mUserLastName = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_FIRST_USERNAME, mUserLastName);
        mUserEmail = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_FIRST_USERNAME, mUserEmail);
        mUserImage = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_IMAGE, mUserImage);
        mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);

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

        if (isLoggedIn) {
            menu.removeItem(R.id.nav_logIn_signUp);

        } else {
            menu.removeItem(R.id.nav_settings);
            menu.removeItem(R.id.nav_payments);
            menu.removeItem(R.id.nav_logout);
            menu.removeItem(R.id.nav_faq);
        }

    }//end onCreate()

    @Override
    protected void onResume() {
        super.onResume();
        //get user messages details
        getMessageDetails();
    }

    /**
     * get details of user messages
     */
    public void getMessageDetails() {
        service = ApiClient.getClient().create(RCAPInterface.class);
        Call<RootMessageInfo> call = service.getUserMessage("Bearer " + mAccessToken);
        call.enqueue(new Callback<RootMessageInfo>() {
            @Override
            public void onResponse(Call<RootMessageInfo> call, Response<RootMessageInfo> response) {
                mProgressBar.setVisibility(View.GONE);
                mFrameLayout.setAlpha((float) 1.0);
                if (response.isSuccessful()) {
                    mProdRelatedMsgs = response.body().getOwnerProdRelatedMsgs().size();
                }
            }

            @Override
            public void onFailure(Call<RootMessageInfo> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mFrameLayout.setAlpha((float) 1.0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
                break;

            case R.id.action_messages:
                RCLog.showToast(HomeActivity.this, "menu msgs clicked");
                startActivity(new Intent(HomeActivity.this, UserQueAnsActivity.class));
                break;

            default:
                break;
        }

        return true;
    }
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_messages);
        MenuItemCompat.setActionView(item, R.layout.notification_badge);
        RelativeLayout notificationCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        TextView mTxtMsgCount = (TextView) notificationCount.findViewById(R.id.txt_notification_count);
        mTxtMsgCount.setText(String.valueOf(mProdRelatedMsgs));

        ActivityCompat.invalidateOptionsMenu(HomeActivity.this);

        MenuItem menuItemMsgs=menu.findItem(R.id.action_messages);
        MenuItem menuItemRentals=menu.findItem(R.id.action_rentals);

        if(isLoggedIn){
            menuItemMsgs.setVisible(true);
            menuItemRentals.setVisible(true);
        }

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(HomeActivity.this, UserQueAnsActivity.class));
                return true;
            }
        });

        return super.onPrepareOptionsMenu(menu);
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

            case R.id.nav_settings:
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                break;

            case R.id.nav_payments:
                RCLog.showToast(HomeActivity.this, TAG);
                break;

            case R.id.nav_faq:
                String helpUrl = "http://recirkle.com/#/help";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(helpUrl));
                startActivity(i);
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
        editor.apply();
        editor.commit();
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
