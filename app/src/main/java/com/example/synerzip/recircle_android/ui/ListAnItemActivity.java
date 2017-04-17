package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.AllProductInfo;
import com.example.synerzip.recircle_android.models.Discounts;
import com.example.synerzip.recircle_android.models.ListAnItemRequest;
import com.example.synerzip.recircle_android.models.Product;
import com.example.synerzip.recircle_android.models.ProductsData;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.example.synerzip.recircle_android.models.UserProductUnAvailability;
import com.example.synerzip.recircle_android.models.ZipcodeRoot;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.HideKeyboard;
import com.example.synerzip.recircle_android.utilities.NetworkUtility;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;
import com.example.synerzip.recircle_android.utilities.SearchUtility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * Copyright © 2016 Synerzip. All rights reserved
 */
public class ListAnItemActivity extends AppCompatActivity {

    @BindView(R.id.txt_days_for_availability)
    public TextView mTxtDaysOfAvailability;

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

    private String mItemDesc;

    private int mItemPrice, mMinRental;

    private long mZipcode;

    ArrayList<Discounts> listDiscounts;

    ArrayList<UserProdImages> listUploadItemImage;

    ArrayList<UserProductUnAvailability> mItemAvailability;

    @BindView(R.id.img_upload_photos)
    public LinearLayout mImgUploadPhotos;

    RCAPInterface service;

    private SearchUtility utility;

    public ArrayList<String> productItemList;

    AutocompleteAdapter mAutocompleteAdapter;

    @BindView(R.id.auto_txt_list_search_item_name)
    public AutoCompleteTextView mProductAutoComplete;

    public ArrayList<ProductsData> productsDataList;

    public List<Product> productsCustomList;

    private SearchProduct searchProduct;

    private String manufacturerId = "";

    private String productId = "", mAccessToken;

    @BindView(R.id.checkbox_discount_five_days)
    public CheckBox mDiscontForFiveDay;

    @BindView(R.id.checkbox_discount_ten_days)
    public CheckBox mDiscontForTenDay;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    public int fromAustin;

    private UploadImageAdapter mUploadImageAdapter;

    @BindView(R.id.recycler_view_upload_img)
    public RecyclerView mRecyclerView;

    private ArrayList<String> availableDates;

    private Discounts mDiscounts;

    private UserProdImages mUserProdImages;
    private SharedPreferences sharedPreferences;

    private boolean isLoggedIn;

    public int sizeZipcodeList;

    public UserProductUnAvailability userProductUnAvailability;

    public Date mUnavailFromDate, mUnavailToDate = null;

    @BindView(R.id.checkbox_agreement)
    public CheckBox mCheckboxAreement;

    public ArrayList<String> listUploadGalleryImage;

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

        mProductAutoComplete.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mProductAutoComplete));
        mEditTxtEnterPrice.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mEditTxtEnterPrice));
        mEditMinRental.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mEditMinRental));
        mEditTxtItemDesc.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mEditTxtItemDesc));
        mEditTxtZipcode.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mEditTxtZipcode));
        mItemAvailability = new ArrayList<>();
        listDiscounts = new ArrayList<>();
        listUploadItemImage = new ArrayList<>();
        //get data from shared preferences
        sharedPreferences = getSharedPreferences(RCWebConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean(RCWebConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);
        mAccessToken = sharedPreferences.getString(RCWebConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);


        mDiscontForFiveDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDiscounts = new Discounts(30, 5, 0);
                    listDiscounts.add(mDiscounts);
                }
            }
        });
        mDiscontForTenDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        service = ApiClient.getClient().create(RCAPInterface.class);
        Call<ZipcodeRoot> call = service.zipcodeCheck(mZipcode);
        call.enqueue(new Callback<ZipcodeRoot>() {
            @Override
            public void onResponse(Call<ZipcodeRoot> call, Response<ZipcodeRoot> response) {
                if (response.isSuccessful()) {
                    response.body();
                    sizeZipcodeList = response.body().getResults().size();
                    if (mZipcode != 0) {
                        String[] stringArrayZipcodes = getResources().getStringArray(R.array.zipcodes_list_austin);
                        ArrayList<String> listAustinZipcodes = new ArrayList<>();
                        listAustinZipcodes.addAll(Arrays.asList(stringArrayZipcodes));
                        if (sizeZipcodeList != 0) {
                            if (listAustinZipcodes.contains(mZipcode)) {
                                fromAustin = 1;
                            } else if (listAustinZipcodes.contains(mZipcode)) {
                                fromAustin = 0;
                            }
                            getListAnItem();
                        } else {
                            RCLog.showToast(ListAnItemActivity.this, getString(R.string.invalid_zipcode));
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<ZipcodeRoot> call, Throwable t) {
            }
        });
    }

    /**
     * api call for list an item
     */
    private void getListAnItem() {
        checkZipcodes();
        ListAnItemRequest listAnItemRequest = new ListAnItemRequest(productId, mItemPrice, mMinRental,
                mItemDesc, listDiscounts, listUploadItemImage, mItemAvailability, mZipcode, fromAustin);

        service = ApiClient.getClient().create(RCAPInterface.class);
        Call<AllProductInfo> call = service.listAnItem("Bearer " + mAccessToken, listAnItemRequest);

        call.enqueue(new Callback<AllProductInfo>() {

            @Override
            public void onResponse(Call<AllProductInfo> call, Response<AllProductInfo> response) {
                if (response.isSuccessful()) {
                    RCLog.showToast(ListAnItemActivity.this, getString(R.string.item_added));
                } else {
                    if (response.code() == RCWebConstants.RC_ERROR_CODE_BAD_REQUEST) {
                        RCLog.showToast(ListAnItemActivity.this, getString(R.string.product_creation_failed));
                    } else {
                        RCLog.showToast(ListAnItemActivity.this, getString(R.string.user_not_authenticated));
                    }
                }
            }

            @Override
            public void onFailure(Call<AllProductInfo> call, Throwable t) {
            }
        });
    }


    /**
     * list an item submit button
     *
     * @param v
     */
    @OnClick(R.id.btn_save_and_continue)
    public void btnSaveAndContinue(View v) {
        submitForm();
        if (isLoggedIn) {
            HideKeyboard.hideKeyBoard(ListAnItemActivity.this);
            if (NetworkUtility.isNetworkAvailable(this)) {
                initializeViews();
                checkZipcodes();
            } else {
                RCLog.showToast(this, getResources().getString(R.string.err_network_available));
            }
        } else {
            RCLog.showToast(ListAnItemActivity.this, getString(R.string.user_must_login));
            startActivity(new Intent(this, LogInActivity.class));
        }
    }

    /**
     * initialize views
     */
    public void initializeViews() {
        mItemPrice = Integer.parseInt(mEditTxtEnterPrice.getText().toString().trim());
        mMinRental = Integer.parseInt(mEditMinRental.getText().toString().trim());
        mItemDesc = mEditTxtItemDesc.getText().toString().trim();
        mZipcode = Long.parseLong(mEditTxtZipcode.getText().toString().trim());

        //TODO product images should be taken from amazon s3 bucket ; yet to be done
        mUserProdImages =
                new UserProdImages("https://s3.ap-south-1.amazonaws.com/cmsios/CalendarView.png",
                        "2017-02-04T13:13:09.000Z");
        listUploadItemImage.add(mUserProdImages);
        userProductUnAvailability = new UserProductUnAvailability(mUnavailFromDate.toString(),
                mUnavailToDate.toString());
        mItemAvailability.add(userProductUnAvailability);
        return;
    }

    /**
     * enter calendar unavailability
     *
     * @param view
     */
    @OnClick(R.id.edit_calendar_availability)
    public void calendarAvailability(View view) {
        startActivityForResult(new Intent(ListAnItemActivity.this, ListItemCalendarActivity.class), 1);
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
                            productItemList.add(productsDataList
                                    .get(i).getProduct_manufacturer_name()
                                    + " " + productsList.get(j).getProduct_title());

                            productsList.get(j).setProduct_manufacturer_title(productsDataList
                                    .get(i).getProduct_manufacturer_name()
                                    + " " + productsList.get(j).getProduct_title());
                            productsCustomList.add(productsList.get(j));
                        }
                    }

                    mAutocompleteAdapter = new AutocompleteAdapter
                            (ListAnItemActivity.this, R.layout.activity_search,
                                    R.id.txtProductName, productsCustomList);
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

                Product product = (Product) parent.getAdapter().getItem(position);
                if (product.getProduct_manufacturer_id() != null
                        && !product.getProduct_manufacturer_id().isEmpty()) {
                    manufacturerId = product.getProduct_manufacturer_id();
                }
                if (product.getProduct_id() != null && !product.getProduct_id().isEmpty()) {
                    productId = product.getProduct_id();
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
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
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
                availableDates = data.getStringArrayListExtra(getString(R.string.calendar_availability_days));

                String from = data.getStringExtra(getString(R.string.unavail_from_date));
                String to = data.getStringExtra(getString(R.string.unavail_to_date));
                DateFormat formatter = new SimpleDateFormat(getString(R.string.date_format));
                try {
                    mUnavailFromDate = formatter.parse(from.toString());
                    mUnavailToDate = formatter.parse(to.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int daysCount = data.getIntExtra(getString(R.string.calendar_availability_days_count), 0);
                if (daysCount != 0) {
                    mTxtDaysOfAvailability.setVisibility(View.VISIBLE);
                    mTxtDaysOfAvailability.setText(getString(R.string.calendar_days_selected) + " " +
                            daysCount + " " + getString(R.string.calendar_days));
                }

            }
        }
        //TODO functionality yet to be completed for upload images
        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            mUploadImageAdapter = new UploadImageAdapter(ListAnItemActivity.this, listUploadGalleryImage);
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            listUploadGalleryImage.add(picturePath);
            mUploadImageAdapter.notifyDataSetChanged();
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            mRecyclerView.setAdapter(mUploadImageAdapter);
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
