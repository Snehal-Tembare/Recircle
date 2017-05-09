package com.example.synerzip.recircle_android.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserProdImages;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdditionalDetailsActivity extends AppCompatActivity {
    private ArrayList<UserProdImages> listUploadItemImage;
    private ArrayList<String> uploadGalleryImages;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.list_an_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        uploadGalleryImages = new ArrayList<>();

        listUploadItemImage = (ArrayList<UserProdImages>) getIntent().getSerializableExtra(getString(R.string.upload_image));
        uploadGalleryImages = (ArrayList<String>) getIntent().getSerializableExtra(getString(R.string.uplaod_image_gallery));
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
