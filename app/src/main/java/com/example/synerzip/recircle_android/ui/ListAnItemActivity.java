package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.example.synerzip.recircle_android.models.ZipcodeRoot;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.HideKeyboard;
import com.example.synerzip.recircle_android.utilities.NetworkUtility;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;
import com.example.synerzip.recircle_android.utilities.SearchUtility;

import java.util.ArrayList;
import java.sql.Date;
import java.util.Arrays;
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

    private int mZipcode, mItemPrice, mMinRental;

    ArrayList<Discounts> listDiscounts;

    ArrayList<UserProdImages> listUploadItemImage;

    ArrayList<String> mItemAvailability;

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

    private String productId = "";

    @BindView(R.id.checkbox_discount_five_days)
    public CheckBox mDiscontForFiveDay;

    @BindView(R.id.checkbox_discount_ten_days)
    public CheckBox mDiscontForTenDay;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    public boolean fromAustin;

    private Context mContext;

    private UploadImageAdapter mUploadImageAdapter;

    @BindView(R.id.recycler_view_upload_img)
    public RecyclerView mRecyclerView;

    private ArrayList<String> availableDates;

    private Discounts mDiscounts;

    private UserProdImages mUserProdImages;
    private SharedPreferences sharedPreferences;

    private boolean isLoggedIn;

    private String mStrZipcode;

    private int sizeZipcodeList;

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
        mProductAutoComplete.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mProductAutoComplete));
        mEditTxtEnterPrice.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mEditTxtEnterPrice));
        mEditMinRental.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mEditMinRental));
        mEditTxtItemDesc.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mEditTxtItemDesc));
        mEditTxtZipcode.addTextChangedListener(new ListAnItemActivity.MyTextWatcher(mEditTxtZipcode));

        listDiscounts = new ArrayList<>();

        //get data from shared preferences
        sharedPreferences = getSharedPreferences(RCWebConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean(RCWebConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);

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
        Call<ZipcodeRoot> call = service.zipcodeCheck(mStrZipcode);
        call.enqueue(new Callback<ZipcodeRoot>() {
            @Override
            public void onResponse(Call<ZipcodeRoot> call, Response<ZipcodeRoot> response) {
                if (response.isSuccessful()) {
                    sizeZipcodeList = response.body().getResults().size();
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

        if (mZipcode != 0) {
            checkZipcodes();
            String[] stringArrayZipcodes = getResources().getStringArray(R.array.zipcodes_list_austin);
            ArrayList<String> listAustinZipcodes = new ArrayList<String>();
            listAustinZipcodes.addAll(Arrays.asList(stringArrayZipcodes));

            if (sizeZipcodeList > 0) {
                if (listAustinZipcodes.contains(mZipcode)) {
                    fromAustin = true;
                } else {
                    fromAustin = false;
                }
            } else {
                RCLog.showToast(this, "Invalid zipcode");
            }
        }
        //TODO functionality yet to be completed
        ListAnItemRequest listAnItemRequest = new ListAnItemRequest(productId, mItemPrice, mMinRental,
                mItemDesc, listDiscounts, listUploadItemImage, availableDates, mZipcode, fromAustin);

        service = ApiClient.getClient().create(RCAPInterface.class);
        Call<AllProductInfo> call = service.listAnItem(listAnItemRequest);

        call.enqueue(new Callback<AllProductInfo>() {

            @Override
            public void onResponse(Call<AllProductInfo> call, Response<AllProductInfo> response) {
                if (response.isSuccessful()) {
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
        if (isLoggedIn) {
            submitForm();
            HideKeyboard.hideKeyBoard(ListAnItemActivity.this);
            if (NetworkUtility.isNetworkAvailable(this)) {
                listUploadItemImage = new ArrayList<>();
                mItemPrice = Integer.parseInt(mEditTxtEnterPrice.getText().toString().trim());
                mMinRental = Integer.parseInt(mEditMinRental.getText().toString().trim());
                mItemDesc = mEditTxtItemDesc.getText().toString().trim();

                mZipcode = Integer.parseInt(mEditTxtZipcode.getText().toString().trim());

                listUploadItemImage.add(mUserProdImages);
                getListAnItem();

            } else {
                RCLog.showToast(this, getResources().getString(R.string.err_network_available));
            }
        } else {
            RCLog.showToast(mContext, "User must log in");
            startActivity(new Intent(this, LogInActivity.class));
        }
    }

    @OnClick(R.id.edit_calendar_availability)
    public void calendarAvailability(View view) {
        startActivityForResult(new Intent(ListAnItemActivity.this, ListItemCalendarActivity.class), 1);
    }

    @OnClick(R.id.btn_choose_img)
    public void imgUploadPhotos(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
        //TODO for multiple image selection
//        intent.setType("image*//*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
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

        ReadyCallbak readyCallbak = new ReadyCallbak() {
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
                            (ListAnItemActivity.this, R.layout.activity_search, R.id.txtProductName, productsCustomList);
                    mProductAutoComplete.setAdapter(mAutocompleteAdapter);

                } else {
                    RCLog.showToast(getApplicationContext(), getString(R.string.product_details_not_found));
                }
            }
        };

        utility.setCallback(readyCallbak);

        mProductAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Product product = (Product) parent.getAdapter().getItem(position);
                if (product.getProduct_manufacturer_id() != null && !product.getProduct_manufacturer_id().isEmpty()) {
                    manufacturerId = product.getProduct_manufacturer_id();
                }
                if (product.getProduct_id() != null && !product.getProduct_id().isEmpty()) {
                    productId = product.getProduct_id();
                }
                HideKeyboard.hideKeyBoard(ListAnItemActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                availableDates = data.getStringArrayListExtra(getString(R.string.calendar_availability_days));
            }
            int daysCount = data.getIntExtra(getString(R.string.calendar_availability_days_count), 0);
            mTxtDaysOfAvailability.setText(getString(R.string.calendar_days_selected) +
                    daysCount + getString(R.string.calendar_days));
        }
        //TODO functionality yet to be completed
        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            mUploadImageAdapter = new UploadImageAdapter(mContext, listUploadItemImage);
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            mRecyclerView.addView(insertPhoto(picturePath));
            mUploadImageAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mUploadImageAdapter);

        }
    }

    private ImageView insertPhoto(String path) {

        Bitmap bm = decodeBitmapFromUri(path, 100, 100);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_upload_img);
        recyclerView.setLayoutManager(layoutManager);
        ImageView imageView = (ImageView) recyclerView.findViewById(R.id.img_upload_item);
        Drawable drawable = new BitmapDrawable(getResources(), bm);
        recyclerView.setBackground(drawable);
        return imageView;
    }

    private Bitmap decodeBitmapFromUri(String path, int reqWidth, int reqHeight) {
        Bitmap bm = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
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
        if (mEditTxtZipcode.getText().toString().trim().isEmpty() || mEditTxtZipcode.length() < 5) {
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
