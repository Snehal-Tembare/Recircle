package com.example.synerzip.recircle_android.ui;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.AllProductInfo;
import com.example.synerzip.recircle_android.models.Discounts;
import com.example.synerzip.recircle_android.models.ListAnItemRequest;
import com.example.synerzip.recircle_android.models.LogInRequest;
import com.example.synerzip.recircle_android.models.Product;
import com.example.synerzip.recircle_android.models.ProductsData;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.example.synerzip.recircle_android.models.UserProductUnAvailability;
import com.example.synerzip.recircle_android.models.ZipcodeRoot;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.HideKeyboard;
import com.example.synerzip.recircle_android.utilities.NetworkUtility;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;
import com.example.synerzip.recircle_android.utilities.SearchUtility;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prajakta Patil on 31/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class ListAnItemActivity extends AppCompatActivity {

    @BindView(R.id.txt_days_for_availability)
    protected TextView mTxtDaysOfAvailability;

    @BindView(R.id.edit_enter_price)
    protected EditText mEditTxtEnterPrice;

    @BindView(R.id.edit_min_rental)
    protected EditText mEditMinRental;

    @BindView(R.id.edit_item_desc)
    protected EditText mEditTxtItemDesc;

    @BindView(R.id.edit_enter_zip)
    protected EditText mEditTxtZipcode;

    @BindView(R.id.input_layout_search_item)
    protected TextInputLayout mInputLayoutSearchItem;

    @BindView(R.id.input_layout_item_desc)
    protected TextInputLayout mInputLayoutItemDesc;

    @BindView(R.id.input_layout_enter_zipcode)
    protected TextInputLayout mInputLayoutZipcode;

    @BindView(R.id.input_layout_min_rental)
    protected TextInputLayout mInputLayoutMinRental;

    @BindView(R.id.input_layout_rental_price)
    protected TextInputLayout mInputLayoutPrice;

    private String mItemDesc;

    private int mItemPrice, mMinRental;

    private long mZipcode;

    private ArrayList<Discounts> listDiscounts;

    private ArrayList<UserProdImages> listUploadItemImage;

    private ArrayList<UserProductUnAvailability> mItemAvailability;

    @BindView(R.id.img_upload_photos)
    protected LinearLayout mImgUploadPhotos;

    private RCAPInterface service;

    private SearchUtility utility;

    private ArrayList<String> productItemList;

    private AutocompleteAdapter mAutocompleteAdapter;

    @BindView(R.id.auto_txt_list_search_item_name)
    protected AutoCompleteTextView mProductAutoComplete;

    private ArrayList<ProductsData> productsDataList;

    private List<Product> productsCustomList;

    private SearchProduct searchProduct;

    private String manufacturerId = "";

    private String productId = "", mAccessToken;

    @BindView(R.id.checkbox_discount_five_days)
    protected CheckBox mDiscountForFiveDay;

    @BindView(R.id.checkbox_discount_ten_days)
    protected CheckBox mDiscountForTenDay;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    private int fromAustin;

    @BindView(R.id.recycler_view_upload_img)
    protected RecyclerView mRecyclerView;

    private Discounts mDiscounts;

    private boolean isLoggedIn;

    private int sizeZipcodeList;

    public UserProductUnAvailability userProductUnAvailability;

    @BindView(R.id.checkbox_agreement)
    protected CheckBox mCheckboxAgreement;

    private ArrayList<String> listUploadGalleryImage;

    @BindView(R.id.progress_bar)
    protected RelativeLayout mProgressBar;

    @BindView(R.id.layout_list_item)
    protected LinearLayout mLinearLayout;

    private ArrayList<Date> selectedDates;

    @BindView(R.id.price_slider)
    protected DiscreteSeekBar mDiscreteSeekBar;

    private double productPrice;

    private String productTitle;

    private SharedPreferences sharedPreferences;

    @BindView(R.id.txt_suggested_price)
    protected TextView mTxtSuggestedPrice;

    private long suggestedPrice;

    private int daysCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_an_item);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.list_an_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        utility = new SearchUtility();
        listUploadGalleryImage = new ArrayList<>();
        mProductAutoComplete.setSingleLine();

        mProductAutoComplete.addTextChangedListener(new RCTextWatcher(mProductAutoComplete));
        mEditTxtEnterPrice.addTextChangedListener(new RCTextWatcher(mEditTxtEnterPrice));
        mEditMinRental.addTextChangedListener(new RCTextWatcher(mEditMinRental));
        mEditTxtItemDesc.addTextChangedListener(new RCTextWatcher(mEditTxtItemDesc));
        mEditTxtZipcode.addTextChangedListener(new RCTextWatcher(mEditTxtZipcode));

        mItemAvailability = new ArrayList<>();
        listDiscounts = new ArrayList<>();
        listUploadItemImage = new ArrayList<>();
        selectedDates = new ArrayList<>();

        //get data from shared preferences
        sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);
        mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);

        //discounts checkbox listener
        mDiscountForFiveDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDiscounts = new Discounts(30, 5, 0);
                    listDiscounts.add(mDiscounts);
                }
            }
        });
        mDiscountForTenDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDiscounts = new Discounts(40, 10, 0);
                    listDiscounts.add(mDiscounts);
                }
            }
        });

    }//end onCreate()

    /**
     * check whether zipcode valid or not
     */
    private void checkZipcodes() {
        if (mZipcode != 0) {
            service = ApiClient.getClient().create(RCAPInterface.class);
            Call<ZipcodeRoot> call = service.zipcodeCheck(mZipcode);
            call.enqueue(new Callback<ZipcodeRoot>() {
                @Override
                public void onResponse(Call<ZipcodeRoot> call, Response<ZipcodeRoot> response) {
                    if (response.isSuccessful()) {
                        response.body();
                        sizeZipcodeList = response.body().getResults().size();
                        String[] stringArrayZipcodes = getResources().getStringArray(R.array.zipcodes_list_austin);
                        ArrayList<String> listAustinZipcodes = new ArrayList<>();
                        listAustinZipcodes.addAll(Arrays.asList(stringArrayZipcodes));
                        String zipcode = String.valueOf(mZipcode);
                        if (sizeZipcodeList != 0) {
                            if (listAustinZipcodes.contains(zipcode)) {
                                fromAustin = 1;
                            } else {
                                if (listAustinZipcodes.contains(zipcode)) {
                                    fromAustin = 0;
                                }
                            }
                        } else {
                            if (sizeZipcodeList == 0) {
                                RCLog.showToast(ListAnItemActivity.this, getString(R.string.invalid_zipcode));
                                return;
                            }
                        }
                        if (mCheckboxAgreement.isChecked()) {

                            Intent intent = new Intent(ListAnItemActivity.this, ListItemSummaryActivity.class);
                            intent.putExtra(getString(R.string.unavail_dates), selectedDates);
                            intent.putExtra(getString(R.string.unavail_dates_count), daysCount);
                            intent.putExtra(getString(R.string.item_price), mItemPrice);
                            intent.putExtra(getString(R.string.item_min_rental), mMinRental);
                            intent.putExtra(getString(R.string.upload_image), listUploadItemImage);
                            intent.putExtra(getString(R.string.uplaod_image_gallery), listUploadGalleryImage);
                            intent.putExtra(getString(R.string.list_item_desc), mItemDesc);
                            intent.putExtra(getString(R.string.product_title), productTitle);
                            intent.putExtra(getString(R.string.product_id), productId);
                            intent.putExtra(getString(R.string.discounts), listDiscounts);
                            intent.putExtra(getString(R.string.list_unavail_days), mItemAvailability);
                            intent.putExtra(getString(R.string.austin_check), fromAustin);
                            intent.putExtra(getString(R.string.zipcode), mZipcode);

                            if (mDiscounts.getDiscount_for_days() == 5) {
                                double discFiveDays = Math.round(productPrice * 0.03);
                                intent.putExtra(getString(R.string.disc_five_days), discFiveDays);
                            }
                            if (mDiscounts.getDiscount_for_days() == 10) {
                                double discTenDays = Math.round(productPrice * 0.04);
                                intent.putExtra(getString(R.string.disc_ten_days), discTenDays);
                            }

                            startActivity(intent);


                        } else {
                            RCLog.showToast(ListAnItemActivity.this, getString(R.string.terms_and_agreement));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ZipcodeRoot> call, Throwable t) {
                }
            });
        }
        return;
    }

    /**
     * list an item submit button
     *
     * @param v
     */
    @OnClick(R.id.btn_proceed)
    public void btnProceed(View v) {
        submitForm();
        if (isLoggedIn) {
            HideKeyboard.hideKeyBoard(ListAnItemActivity.this);
            if (NetworkUtility.isNetworkAvailable(this)) {
                if (getValues()) {
                    checkZipcodes();

                } else {
                    RCLog.showToast(ListAnItemActivity.this, getString(R.string.mandatory_dates));
                }
            } else {
                RCLog.showToast(this, getResources().getString(R.string.err_network_available));
            }
        } else {
            RCLog.showToast(ListAnItemActivity.this, getString(R.string.user_must_login));
            startActivity(new Intent(this, LogInActivity.class));
        }
    }

    /**
     * get values
     */
    public boolean getValues() {

        String strPrice = mEditTxtEnterPrice.getText().toString();
        String strRental = mEditMinRental.getText().toString();
        String strDesc = mEditTxtItemDesc.getText().toString();
        String strZipcode = mEditTxtZipcode.getText().toString();

        if (!strPrice.isEmpty() && !strRental.isEmpty() && !strDesc.isEmpty() && !strZipcode.isEmpty()) {
            mItemPrice = Integer.parseInt(mEditTxtEnterPrice.getText().toString().trim());
            mMinRental = Integer.parseInt(mEditMinRental.getText().toString().trim());
            mItemDesc = mEditTxtItemDesc.getText().toString().trim();
            mZipcode = Long.parseLong(mEditTxtZipcode.getText().toString().trim());
            //TODO product images should be taken from amazon s3 bucket ; yet to be done
            UserProdImages mUserProdImages;
            mUserProdImages = new UserProdImages("https://s3.ap-south-1.amazonaws.com/cmsios/CalendarView.png", "2017-02-04T13:13:09.000Z");
            listUploadItemImage.add(mUserProdImages);
            return true;
        }
        return false;
    }

    /**
     * enter calendar unavailability
     *
     * @param view
     */
    @OnClick(R.id.edit_calendar_availability)
    public void calendarAvailability(View view) {
        Intent intent = new Intent(ListAnItemActivity.this, ListItemCalendarActivity.class);
        intent.putExtra(getString(R.string.dates), selectedDates);
        startActivityForResult(intent, 1);
    }

    /**
     * search item autocomplete textview
     */
    @Override
    protected void onResume() {
        super.onResume();
        productsCustomList = new ArrayList<>();
        productItemList = new ArrayList<>();
        service = ApiClient.getClient().create(RCAPInterface.class);
        utility.populateAutoCompleteData();
        ReadyCallback readyCallback = new ReadyCallback() {
            @Override
            public void searchProductResult(SearchProduct sd) {
                searchProduct = sd;
            }

            @Override
            public void allItemsResult(ArrayList<ProductsData> mProductsDataList) {
                productsDataList = mProductsDataList;
                if (null != productsDataList && 0 != productsDataList.size()) {
                    for (int i = 0; i < productsDataList.size(); i++) {
                        productItemList.add(productsDataList.get(i).getProduct_manufacturer_name());
                        Product pd = new Product();
                        pd.setProduct_manufacturer_id(productsDataList.get(i).getProduct_manufacturer_id());
                        pd.setProduct_manufacturer_name(productsDataList.get(i).getProduct_manufacturer_name());
                        pd.setProduct_manufacturer_title(productsDataList.get(i).getProduct_manufacturer_name());
                        productsCustomList.add(pd);
                        ArrayList<Product> productsList = productsDataList.get(i).getProducts();
                        for (int j = 0; j < productsList.size(); j++) {
                            productItemList.add(productsDataList.get(i).getProduct_manufacturer_name()
                                    + " " + productsList.get(j).getProduct_title());

                            productsList.get(j).setProduct_manufacturer_title(productsDataList
                                    .get(i).getProduct_manufacturer_name()
                                    + " " + productsList.get(j).getProduct_title());
                            productsCustomList.add(productsList.get(j));
                        }
                    }

                    mAutocompleteAdapter = new AutocompleteAdapter
                            (ListAnItemActivity.this, R.layout.activity_search, R.id.txtProductName, productsCustomList);
                    mProductAutoComplete.setAdapter(mAutocompleteAdapter);

                } else {
                    RCLog.showToast(getApplicationContext(), getString(R.string.product_details_not_found));
                }
            }
        };

        utility.setCallback(readyCallback);
        mProductAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mDiscreteSeekBar.setProgress(1);
                Product product = (Product) parent.getAdapter().getItem(position);

                if (product.getProduct_manufacturer_id() != null
                        && !product.getProduct_manufacturer_id().isEmpty()) {
                    manufacturerId = product.getProduct_manufacturer_id();
                }
                if (product.getProduct_detail().getProduct_price() != 0) {
                    productPrice = product.getProduct_detail().getProduct_price();
                    productTitle = product.getProduct_manufacturer_title();
                }
                if (product.getProduct_id() != null && !product.getProduct_id().isEmpty()) {
                    productId = product.getProduct_id();
                    productTitle = product.getProduct_manufacturer_title();
                    if (productPrice != 0) {
                        productPrice = (int) Math.round(productPrice);
                        if (productPrice > 0 && productPrice <= 100) {
                            suggestedPrice = Math.round(0.1 * productPrice);
                        } else if (productPrice > 100 && productPrice <= 500) {
                            suggestedPrice = Math.round(0.04 * productPrice);
                        } else if (productPrice > 500 && productPrice <= 1000) {
                            suggestedPrice = Math.round(0.03 * productPrice);

                        } else if (productPrice > 1000 && productPrice <= 2000) {
                            suggestedPrice = Math.round(0.02 * productPrice);

                        } else if (productPrice > 2000 && productPrice <= 10000) {
                            suggestedPrice = Math.round(0.01 * productPrice);

                        } else if (productPrice > 10000 && productPrice <= 25000) {
                            suggestedPrice = Math.round(0.07 * productPrice);

                        } else {
                            suggestedPrice = Math.round(0.03 * productPrice);

                        }
                        mEditTxtEnterPrice.setText(String.valueOf(suggestedPrice));

                        //numeric slider for product suggested price
                        mDiscreteSeekBar.setVisibility(View.VISIBLE);
                        mTxtSuggestedPrice.setVisibility(View.VISIBLE);
                        mDiscreteSeekBar.setMin(1);
                        mDiscreteSeekBar.setMax((int) suggestedPrice * 2);
                        mDiscreteSeekBar.setProgress((int) suggestedPrice);

                        mDiscreteSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
                            @Override
                            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                                String strValue = String.valueOf(value);
                                mEditTxtEnterPrice.setText(strValue);
                            }

                            @Override
                            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                                seekBar.setProgress((int) suggestedPrice);
                            }

                            @Override
                            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

                            }
                        });
                    }
                }
                HideKeyboard.hideKeyBoard(ListAnItemActivity.this);
            }
        });

    }

    /**
     * upload images for products
     *
     * @param view
     */
    @OnClick(R.id.btn_choose_img)
    public void imgUploadPhotos(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }

    /**
     * get calendar availability dates
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> availableDates;
                availableDates = data.getStringArrayListExtra(getString(R.string.calendar_availability_days));
                selectedDates = (ArrayList<Date>) data.getSerializableExtra(getString(R.string.selectedDates));
                for (String date : availableDates) {
                    userProductUnAvailability = new UserProductUnAvailability(date, date);
                    mItemAvailability.add(userProductUnAvailability);
                }
                daysCount = data.getIntExtra(getString(R.string.calendar_availability_days_count), 0);
                if (daysCount != 0) {
                    mTxtDaysOfAvailability.setVisibility(View.VISIBLE);
                    mTxtDaysOfAvailability.setText(getString(R.string.calendar_days_selected) + " " +
                            daysCount + " " + getString(R.string.calendar_days));
                }
            }
        }
        //TODO images should be uploaded to amazon s3 bucket; yet to be done
        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            UploadImageAdapter mUploadImageAdapter;
            mUploadImageAdapter = new UploadImageAdapter(ListAnItemActivity.this, listUploadGalleryImage);
            if (data.getData() != null) {
                Uri uri = data.getData();
                String filepath = getPath(ListAnItemActivity.this, uri);
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
                        String filepath = getPath(ListAnItemActivity.this, uri);
                        listUploadGalleryImage.add(filepath);
                    }
                    mUploadImageAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mUploadImageAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                }
            }
        }
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

    private class RCTextWatcher implements TextWatcher {

        private View view;

        private RCTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.auto_txt_list_search_item_name:
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

    /**
     * validation for search item edittext
     *
     * @return
     */
    private boolean validateSearchItem() {
        if (mProductAutoComplete.getText().toString().trim().isEmpty()) {
            mInputLayoutSearchItem.setError(getString(R.string.validate_item_search));
            requestFocus(mProductAutoComplete);
            return false;
        } else {
            mInputLayoutSearchItem.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * validation for minimum rental edittext
     *
     * @return
     */
    private boolean validateMinRental() {

        mEditMinRental.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEditMinRental.getText().toString().startsWith("0")) {
                    mEditMinRental.setText(mEditMinRental.getText().toString().replace("0", ""));
                }
            }
        });

        if (mEditMinRental.getText().toString().trim().isEmpty() ||
                mEditMinRental.getText().toString().length() == 0) {
            mInputLayoutMinRental.setError(getString(R.string.validate_min_rental_days));
            requestFocus(mEditMinRental);
            return false;
        } else {
            mInputLayoutMinRental.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * validation for item description edittext
     *
     * @return
     */
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

    /**
     * validation for zipcode edittext
     *
     * @return
     */
    private boolean validateZipcode() {
        if (mEditTxtZipcode.getText().toString().trim().isEmpty() || mEditTxtZipcode.length() < 5) {
            mInputLayoutZipcode.setError(getString(R.string.validate_zipcode));
            requestFocus(mEditTxtZipcode);
            return false;
        } else {
            mInputLayoutZipcode.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * validation for price edittext
     *
     * @return
     */
    private boolean validatePrice() {
        mEditTxtEnterPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (mEditTxtEnterPrice.getText().toString().startsWith("0")) {
                    mEditTxtEnterPrice.setText(mEditTxtEnterPrice.getText().toString().replace("0", ""));
                }
                if (!s.toString().equals("")) {
                    int i = Integer.parseInt(s.toString());
                    if (i > 0 && i <= suggestedPrice) {
                        mDiscreteSeekBar.setProgress(i);
                    }
                }
            }
        });
        if (mEditTxtEnterPrice.getText().toString().trim().isEmpty()) {
            mInputLayoutPrice.setError(getString(R.string.validate_price));
            requestFocus(mEditTxtEnterPrice);
            return false;
        } else {
            mInputLayoutPrice.setErrorEnabled(false);
        }
        return true;
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
