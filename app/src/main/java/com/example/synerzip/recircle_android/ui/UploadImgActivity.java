package com.example.synerzip.recircle_android.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Discounts;
import com.example.synerzip.recircle_android.utilities.RCLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by Prajakta Patil on 9/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class UploadImgActivity extends AppCompatActivity {

    public static ArrayList<String> listUploadGalleryImage;

    @BindView(R.id.recycler_view_upload_img)
    protected RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_img);
        ButterKnife.bind(this);

        listUploadGalleryImage = new ArrayList<>();

    }
    @OnClick(R.id.img_proceed)
    public void btnProceed(View view){
        if(listUploadGalleryImage.size()!=0) {
            Intent intent = new Intent(UploadImgActivity.this, AdditionalDetailsActivity.class);
            intent.putExtra(getString(R.string.uplaod_image_gallery), listUploadGalleryImage);

            startActivity(intent);
        }else {
            RCLog.showToast(UploadImgActivity.this,getString(R.string.upload_img));
        }
    }
    @OnClick(R.id.img_gallery)
    public void imgGalleryImg(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }
    @OnClick(R.id.img_camera)
    public void imgCameraImg(View view){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && resultCode == RESULT_OK) {
            UploadImageAdapter mUploadImageAdapter;
            mUploadImageAdapter = new UploadImageAdapter(UploadImgActivity.this, listUploadGalleryImage);
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri tempUri = getImageUri(getApplicationContext(), photo);
                File cameraFilePath = new File(getRealPathFromURI(tempUri));
                listUploadGalleryImage.add(cameraFilePath.toString());

                mUploadImageAdapter.notifyDataSetChanged();
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                mRecyclerView.setAdapter(mUploadImageAdapter);
        }
        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            UploadImageAdapter mUploadImageAdapter;
            mUploadImageAdapter = new UploadImageAdapter(UploadImgActivity.this, listUploadGalleryImage);
            if (data.getData() != null) {
                Uri uri = data.getData();
                String filepath = getPath(UploadImgActivity.this, uri);
                listUploadGalleryImage.add(filepath);
                mUploadImageAdapter.notifyDataSetChanged();
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                mRecyclerView.setAdapter(mUploadImageAdapter);
            } else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        String filepath = getPath(UploadImgActivity.this, uri);
                        listUploadGalleryImage.add(filepath);
                    }
                    mUploadImageAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mUploadImageAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                }
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    /**
     * get file path from uri
     *
     * @param context
     * @param uri
     * @return
     */
    public String getPath(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);
        String id = wholeID.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
    /**
     * textview cancel to go to previous activity
     *
     * @param view
     */
    @OnClick(R.id.txt_cancel)
    public void txtCancel(View view) {
        finish();
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
