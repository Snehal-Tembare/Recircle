package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.models.UserProductDetails;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity {

    private static final long TIMER = 800;
    private static final String TAG = "MyProfileActivity";
    private ItemAdapter adapter;
    public static boolean isMyProfile;
    private ArrayList<Products> productDetailsList;
    private RCAPInterface service;
    private String user_id;
    private SharedPreferences sharedPreferences;
    private ArrayList<UserProductDetails> userProductDetailsList;
    public static boolean isItemEdit;


    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.recycler_items)
    protected RecyclerView mRecyclerItems;

    @BindView(R.id.img)
    protected CircularImageView mImg;

    @BindView(R.id.txt_name)
    protected TextView mTxtName;

    @BindView(R.id.rating_bar)
    protected RatingBar mRatingBar;

    @BindView(R.id.txt_no_of_reviews)
    protected TextView mTxtReviewsCount;

    @BindView(R.id.progress_bar)
    public RelativeLayout mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.my_profile));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        user_id = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_USERID, user_id);
    }

    private void getMyProfileData() {
        if (ApiClient.getClient(this)!=null){
        service = ApiClient.getClient(this).create(RCAPInterface.class);
        Call<User> call = service.getUserProfile(user_id);
        mProgressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    mProgressBar.setVisibility(View.GONE);
                    if (response.body() != null) {
                        User user = response.body();
                        if (user!=null){
                            Picasso.with(getApplicationContext())
                                    .load(R.drawable.ic_user)
                                    .placeholder(R.drawable.ic_user).into(mImg);

                            if (user.getUser_image_url() != null) {
                                Picasso.with(getApplicationContext())
                                        .load(user.getUser_image_url())
                                        .placeholder(R.drawable.ic_user).into(mImg);
                            }

                            if (user.getFirst_name() != null || user.getLast_name() != null) {
                                mTxtName.setText(user.getFirst_name() + " " + user.getLast_name());
                            }

                            if (user.getUser_avg_rating() != null) {
                                mRatingBar.setRating(Float.parseFloat(user.getUser_avg_rating()));
                                mTxtReviewsCount.setText("(" + user.getUser_avg_rating() + ")");
                            }

                            if (user.getUserProductDetails() != null
                                    && user.getUserProductDetails().size() != 0) {
                                userProductDetailsList = user.getUserProductDetails();
                                Log.v(TAG, "***" + user.getFirst_name() + " " + user.getLast_name());
                                Log.v(TAG, "***No. of products" + user.getUserProductDetails().size());
                                adapter = new ItemAdapter(getApplicationContext(), userProductDetailsList, new OnItemClickListener() {
                                    @Override
                                    public void onProductClick(UserProductDetails userProductDetails) {
                                        Log.v(TAG, "onProductClick");
                                        Intent intent = new Intent(MyProfileActivity.this, DetailsActivity.class);
                                        if (userProductDetails.getUser_product_id() != null) {
                                            intent.putExtra(getString(R.string.product_id), userProductDetails.getUser_product_id());
                                        }
                                        isMyProfile=true;
                                        startActivity(intent);
                                    }
                                });

                                mRecyclerItems.setLayoutManager(new GridLayoutManager(MyProfileActivity.this, 2));
                                mRecyclerItems.setAdapter(adapter);
                            }

                        }
                    }
                }else if (response.code()== RCWebConstants.RC_ERROR_CODE_BAD_REQUEST){
                    RCLog.showToast(getApplicationContext(),getString(R.string.user_not_authenticated));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },TIMER);
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                RCLog.showToast(getApplicationContext(),getString(R.string.something_went_wrong));
            }
        });
    }else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyProfileData();
    }

    /**
     * OnClick of home button
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            isItemEdit=false;
            finish();
        }
        return true;
    }

    public interface OnItemClickListener {
        void onProductClick(UserProductDetails userProductDetails);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isItemEdit=false;
    }
}
