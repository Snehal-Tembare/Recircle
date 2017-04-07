package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.AllProductInfo;
import com.example.synerzip.recircle_android.models.Discounts;
import com.example.synerzip.recircle_android.models.ListAnItemRequest;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.HideKeyboard;
import com.example.synerzip.recircle_android.utilities.NetworkUtility;
import com.example.synerzip.recircle_android.utilities.RCLog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;

/**
 * Created by Prajakta Patil on 31/3/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
public class ListAnItemActivity extends AppCompatActivity {

    @BindView(R.id.edit_item_search)
    public EditText mEditTxtItemSearch;

    @BindView(R.id.edit_enter_price)
    public EditText mEditTxtEnterPrice;

    @BindView(R.id.edit_min_rental)
    public EditText mEditMinRental;

    @BindView(R.id.edit_item_desc)
    public EditText mEditTxtItemDesc;

    @BindView(R.id.edit_enter_zip)
    public EditText mEditTxtZipcode;

    @BindView(R.id.input_layout_search_item)
    public TextInputLayout mInputLayoutSearchItem;

    @BindView(R.id.input_layout_item_desc)
    public TextInputLayout mInputLayoutItemDesc;

    @BindView(R.id.input_layout_enter_zipcode)
    public TextInputLayout mInputLayoutZipcode;

    @BindView(R.id.input_layout_min_rental)
    public TextInputLayout mInputLayoutMinRental;

    @BindView(R.id.input_layout_rental_price)
    public TextInputLayout mInputLayoutPrice;

    private String mItemDesc,productId;

    private int mZipcode, mItemPrice, mMinRental;

    ArrayList<Discounts> listDiscounts;

    ArrayList<UserProdImages> listUploadItemImage;

    ArrayList<String> mItemAvailability;

    private static int RESULT_LOAD_IMAGE = 1;

    @BindView(R.id.img_upload_photos)
    public ImageView mImgUploadPhotos;

    RCAPInterface service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_an_item);
        ButterKnife.bind(this);

        mEditTxtItemSearch.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mEditTxtItemSearch));

        mEditTxtEnterPrice.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mEditTxtEnterPrice));

        mEditMinRental.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mEditMinRental));

        mEditTxtItemDesc.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mEditTxtItemDesc));

        mEditTxtZipcode.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mEditTxtZipcode));
    }

    /**
     * list an item submit button
     *
     * @param v
     */
    @OnClick(R.id.btn_save_and_continue)
    public void btnSaveAndContinue(View v) {
        submitForm();
        HideKeyboard.hideKeyBoard(ListAnItemActivity.this);
        if (NetworkUtility.isNetworkAvailable(this)) {
            mItemPrice = Integer.parseInt(mEditTxtEnterPrice.getText().toString().trim());
            mMinRental = Integer.parseInt(mEditMinRental.getText().toString().trim());
            mItemDesc = mEditTxtItemDesc.getText().toString().trim();
            mZipcode = Integer.parseInt(mEditTxtZipcode.getText().toString().trim());
            getListAnItem();

        } else {
            RCLog.showToast(this, getResources().getString(R.string.err_network_available));
        }
    }

    private void getListAnItem() {
        ListAnItemRequest listAnItemRequest = new ListAnItemRequest(productId,mItemPrice,mMinRental,
                mItemDesc,listDiscounts,listUploadItemImage,mItemAvailability,mZipcode);
        service = ApiClient.getClient().create(RCAPInterface.class);
        Call<AllProductInfo> call = service.listAnItem(listAnItemRequest);
        call.enqueue(new Callback<AllProductInfo>() {
            @Override
            public void onResponse(Call<AllProductInfo> call, Response<AllProductInfo> response) {

            }

            @Override
            public void onFailure(Call<AllProductInfo> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.edit_calendar_availability)
    public void calendarAvailability(View view) {
        startActivity(new Intent(ListAnItemActivity.this, CalendarAvailabilityActivity.class));
    }

    @OnClick(R.id.img_upload_photos)
    public void imgUploadPhotos(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            mImgUploadPhotos.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }

    }

    /**
     * Validating form
     */
    private void submitForm() {

        if (!validateSearchItem()) {
            return;
        }
        if (!validateMinRental()) {
            return;
        }
        if (!validateItemDesc()) {
            return;
        }

        if (!validateZipcode()) {
            return;
        }

        if (!validatePrice()) {
            return;
        }

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_item_search:
                    validateSearchItem();
                    break;

                case R.id.edit_min_rental:
                    validateMinRental();
                    break;
                case R.id.edit_item_desc:
                    validateItemDesc();
                    break;

                case R.id.edit_enter_zip:
                    validateZipcode();
                    break;
                case R.id.edit_enter_price:
                    validatePrice();
                    break;
            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateSearchItem() {
        if (mEditTxtItemSearch.getText().toString().trim().isEmpty()) {
            mInputLayoutSearchItem.setError(getString(R.string.validate_item_search));
            requestFocus(mEditTxtItemSearch);
            return false;
        } else {
            mInputLayoutSearchItem.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMinRental() {
        if (mEditMinRental.getText().toString().trim().isEmpty() || mEditMinRental.getText().toString().length() == 0) {
            mInputLayoutMinRental.setError(getString(R.string.validate_min_rental_days));
            requestFocus(mEditMinRental);
            return false;
        } else {
            mInputLayoutMinRental.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateItemDesc() {
        if (mEditTxtItemDesc.getText().toString().trim().isEmpty()) {
            mInputLayoutItemDesc.setError(getString(R.string.validate_item_desc));
            requestFocus(mEditTxtItemDesc);
            return false;
        } else {
            mInputLayoutItemDesc.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateZipcode() {
        if (mEditTxtZipcode.getText().toString().trim().isEmpty() || mEditTxtZipcode.length() < 6) {
            mInputLayoutZipcode.setError(getString(R.string.validate_otp));
            requestFocus(mEditTxtZipcode);
            return false;
        } else {
            mInputLayoutZipcode.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePrice() {
        if (mEditTxtEnterPrice.getText().toString().trim().isEmpty()) {
            mInputLayoutPrice.setError(getString(R.string.validate_price));
            requestFocus(mEditTxtEnterPrice);
            return false;
        } else {
            mInputLayoutPrice.setErrorEnabled(false);
        }
        return true;
    }

}
