package com.example.synerzip.recircle_android.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;

import java.util.ArrayList;

import butterknife.BindView;

public class MyProfileActivity extends AppCompatActivity {

    private RecentItemsAdapter adapter;
    private ArrayList<Products> productDetailsList;
    private RCAPInterface service;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.recycler_items)
    protected RecyclerView mRecyclerItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.my_profile));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.common_white));

        service=ApiClient.getClient().create(RCAPInterface.class);
//        adapter=new RecentItemsAdapter(getApplicationContext(),2);
    }
}
