package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.synerzip.recircle_android.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListedItemActivity extends AppCompatActivity {
    private String productId;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_item);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.list_an_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        productId = getIntent().getExtras().getString(getString(R.string.product_id));
    }

    /**
     * button to view an item
     *
     * @param view
     */
    @OnClick(R.id.btn_view_item)
    public void btnViewItem(View view) {
        Intent intent = new Intent(ListedItemActivity.this, Details.class);
        intent.putExtra(getString(R.string.product_id), productId);
        startActivity(intent);
    }

    /**
     * button to list new item
     *
     * @param view
     */
    @OnClick(R.id.btn_list_item)
    public void btnListItem(View view) {
        startActivity(new Intent(ListedItemActivity.this, ListAnItemActivity.class));
    }

    /**
     * action bar back button
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
