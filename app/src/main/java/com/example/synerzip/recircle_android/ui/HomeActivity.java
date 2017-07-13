package com.example.synerzip.recircle_android.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.user_messages.RootMessageInfo;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.ui.messages.AllMessagesActivity;
import com.example.synerzip.recircle_android.ui.rentals.AllRequestsActivity;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.pkmmte.view.CircularImageView;

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
public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    protected NavigationView mNavigationView;

    @BindView(R.id.view_pager)
    protected ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    protected TabLayout mTabLayout;

    protected ListItemFragment listItemFragment;
    protected SearchItemFragment searchItemFragment;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    private boolean isLoggedIn;

    private String mUserFirstName, mUserLastName, mUserEmail, mUserImage, mAccessToken = "";

    private RootMessageInfo rootMessageInfo;

    private int mProdRelatedMsgs;

    @BindView(R.id.progress_bar_home)
    protected RelativeLayout mProgressBar;

    @BindView(R.id.frame_layout)
    protected FrameLayout mFrameLayout;

    private Menu menu;

    private View mNavHeader;

    private int renterMsgsCount, ownerMsgsCount;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //To display custom ActionBar title
        TextView tv= new TextView(this);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        String re="<font color='#236894' size='30px;'>re</font>";
        String circ="<font color='#D67C6E'>circ</font>";
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
        tv.setText(Html.fromHtml(re+circ,Html.FROM_HTML_MODE_LEGACY));}
        else {
            tv.setText(Html.fromHtml(re+circ,Build.VERSION.SDK_INT));
        }
        tv.setTextSize(30);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);

        mProgressBar.setVisibility(View.VISIBLE);
        mFrameLayout.setAlpha((float) 0.6);

        searchItemFragment = new SearchItemFragment();
        listItemFragment = new ListItemFragment();

        PagerAdapter mPagerAdapter;
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(searchItemFragment, getString(R.string.tab_search));
        mPagerAdapter.addFragment(listItemFragment, getString(R.string.tab_list));
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

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
        menu = mNavigationView.getMenu();

        //Edit product details
        if (MyProfileActivity.isItemEdit) {
            Bundle bundle = getIntent().getExtras();
            listItemFragment.setArguments(bundle);
            mViewPager.setCurrentItem(1);
        }

    }//end onCreate()


    @Override
    protected void onResume() {
        super.onResume();

        //get data from shared preferences
        sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);
        mUserFirstName = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_FIRSTNAME, mUserFirstName);
        mUserLastName = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_LASTNAME, mUserLastName);
        mUserEmail = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_EMAIL, mUserEmail);
        mUserImage = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_IMAGE, mUserImage);
        mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);

        //get user messages details
        getMessageDetails();
        mNavHeader = mNavigationView.getHeaderView(0);
        if (mAccessToken != null) {
            if (isLoggedIn) {
                menu.setGroupVisible(R.id.grp_nav_logIn_signUp, false);

                //To show user info for navigation header
                mNavHeader.setVisibility(View.VISIBLE);
                menu.setGroupVisible(R.id.grp_ic_nav_settings, true);
                menu.setGroupVisible(R.id.grp_nav_payments, true);
                menu.setGroupVisible(R.id.grp_ic_nav_msgs, true);
                menu.setGroupVisible(R.id.grp_nav_logout, true);
                menu.setGroupVisible(R.id.grp_nav_faq, true);

                TextView mTxtUserName = (TextView) mNavHeader.findViewById(R.id.txt_nav_user_name);
                TextView mTxtUserEmail = (TextView) mNavHeader.findViewById(R.id.txt_nav_user_email);
                CircularImageView mImgUser = (CircularImageView) mNavHeader.findViewById(R.id.img_nav_user_profile);
                mTxtUserName.setText(mUserFirstName + " " + mUserLastName);
                mTxtUserEmail.setText(mUserEmail);

                mNavHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeActivity.this, MyProfileActivity.class));
                    }
                });

            } else {
                mNavHeader.setVisibility(View.GONE);
                menu.setGroupVisible(R.id.grp_nav_logIn_signUp, true);
                menu.setGroupVisible(R.id.grp_ic_nav_settings, false);
                menu.setGroupVisible(R.id.grp_nav_payments, false);
                menu.setGroupVisible(R.id.grp_ic_nav_msgs, false);
                menu.setGroupVisible(R.id.grp_nav_logout, false);
                menu.setGroupVisible(R.id.grp_nav_faq, true);
            }
        } else {
            RCLog.showToast(this, getString(R.string.session_expired));
        }


    }

    /**
     * get details of user messages
     */
    public void getMessageDetails() {
        RCAPInterface service;
        if (ApiClient.getClient(this) != null) {
            service = ApiClient.getClient(this).create(RCAPInterface.class);
            Call<RootMessageInfo> call = service.getUserMessage("Bearer " + mAccessToken);
            call.enqueue(new Callback<RootMessageInfo>() {
                @Override
                public void onResponse(Call<RootMessageInfo> call,
                                       Response<RootMessageInfo> response) {
                    mProgressBar.setVisibility(View.GONE);
                    mFrameLayout.setAlpha((float) 1.0);
                    if (response.isSuccessful()) {
                        rootMessageInfo = response.body();
                        if (!response.body().getOwnerRequestMsgs().isEmpty() &&
                                !response.body().getOwnerProdRelatedMsgs().isEmpty()) {
                            ownerMsgsCount = response.body().getOwnerProdRelatedMsgs().size();
                            renterMsgsCount = response.body().getOwnerRequestMsgs().size();
                            mProdRelatedMsgs = ownerMsgsCount + renterMsgsCount;
                        }
                    }
                }

                @Override
                public void onFailure(Call<RootMessageInfo> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    mFrameLayout.setAlpha((float) 1.0);
                }
            });
        }
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
                Intent intent = new Intent(HomeActivity.this, AllMessagesActivity.class);
                intent.putExtra("rootMessageInfo", rootMessageInfo);
                startActivity(intent);
                return true;

            case R.id.action_rentals:
                startActivity(new Intent(HomeActivity.this, AllRequestsActivity.class));
                break;
            default:
                break;
        }

        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem menuItemMsgs = menu.findItem(R.id.action_messages);
        final MenuItem menuItemRentals = menu.findItem(R.id.action_rentals);
        MenuItemCompat.setActionView(menuItemMsgs, R.layout.notification_badge);
        View actionViewNotification = MenuItemCompat.getActionView(menuItemMsgs);
        TextView mTxtMsgCount = (TextView) actionViewNotification.findViewById(R.id.txt_notification_count);
        RelativeLayout notificationCount = (RelativeLayout) menuItemMsgs.getActionView();

        if (isLoggedIn) {
            if (mProdRelatedMsgs == 0) {
                mTxtMsgCount.setVisibility(View.GONE);
            }
        }

        menuItemMsgs.setVisible(true);
        menuItemRentals.setVisible(true);
        mTxtMsgCount.setText(String.valueOf(mProdRelatedMsgs));
        ActivityCompat.invalidateOptionsMenu(HomeActivity.this);

        notificationCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,AllMessagesActivity.class));
            }
        });
        return true;
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
                Intent intent = new Intent(HomeActivity.this, LogInActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_logout: {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getResources().getString(R.string.logout_alert_title));
                alertDialog.setPositiveButton(getResources().getString(R.string.logout), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int arg1) {
                        dialogInterface.dismiss();
                        SharedPreferences sharedPreferences = getApplicationContext().
                                getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
                        isLoggedIn = false;

                        menu.setGroupVisible(R.id.grp_nav_logIn_signUp, true);
                        menu.setGroupVisible(R.id.grp_nav_faq, true);

                        mNavHeader.setVisibility(View.GONE);
                        menu.setGroupVisible(R.id.grp_ic_nav_settings, false);
                        menu.setGroupVisible(R.id.grp_ic_nav_msgs, false);
                        menu.setGroupVisible(R.id.grp_nav_payments, false);
                        menu.setGroupVisible(R.id.grp_nav_logout, false);

                        RCLog.showToast(getApplicationContext(), getString(R.string.user_logged_out));

                    }
                });

                alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int arg1) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
                clearData();
                break;
            }

            case R.id.nav_settings:
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                break;

            case R.id.nav_payments:
                RCLog.showToast(this, getString(R.string.payments));
                break;

            case R.id.nav_faq:
                String helpUrl = "http://recirc.com/#/help";
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(helpUrl));
                startActivity(intent);
                break;
            case R.id.nav_msgs:
                startActivity(new Intent(HomeActivity.this, AllMessagesActivity.class));
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
