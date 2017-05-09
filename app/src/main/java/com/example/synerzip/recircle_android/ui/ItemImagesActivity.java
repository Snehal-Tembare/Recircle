package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Discounts;
import com.example.synerzip.recircle_android.models.Product;
import com.example.synerzip.recircle_android.models.ProductsData;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.HideKeyboard;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.SearchUtility;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemImagesActivity extends AppCompatActivity {

    @BindView(R.id.edit_enter_price)
    protected EditText mEditTxtEnterPrice;

    @BindView(R.id.edit_min_rental)
    protected EditText mEditMinRental;

    @BindView(R.id.input_layout_search_item)
    protected TextInputLayout mInputLayoutSearchItem;

    @BindView(R.id.input_layout_min_rental)
    protected TextInputLayout mInputLayoutMinRental;

    @BindView(R.id.input_layout_rental_price)
    protected TextInputLayout mInputLayoutPrice;

    private int mItemPrice, mMinRental;

    private ArrayList<Discounts> listDiscounts;

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

    private String productId = "";

    @BindView(R.id.checkbox_discount_five_days)
    protected CheckBox mDiscountForFiveDay;

    @BindView(R.id.checkbox_discount_ten_days)
    protected CheckBox mDiscountForTenDay;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    private Discounts mDiscounts;

    @BindView(R.id.price_slider)
    protected DiscreteSeekBar mDiscreteSeekBar;

    private double productPrice;

    private String productTitle;

    private SharedPreferences sharedPreferences;

    @BindView(R.id.txt_suggested_price)
    protected TextView mTxtSuggestedPrice;

    private long suggestedPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_images);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.list_an_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        utility = new SearchUtility();
        mProductAutoComplete.setSingleLine();

        mProductAutoComplete.addTextChangedListener(new ItemImagesActivity.RCTextWatcher(mProductAutoComplete));
        mEditTxtEnterPrice.addTextChangedListener(new ItemImagesActivity.RCTextWatcher(mEditTxtEnterPrice));
        mEditMinRental.addTextChangedListener(new ItemImagesActivity.RCTextWatcher(mEditMinRental));

        listDiscounts = new ArrayList<>();

        //get data from shared preferences
        sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);

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

    @OnClick(R.id.btn_choose_img)
    public void btnUploadImg(View view){
        Intent intent=new Intent(ItemImagesActivity.this,UploadImgActivity.class);
        intent.putExtra(getString(R.string.item_price), mItemPrice);
        intent.putExtra(getString(R.string.item_min_rental), mMinRental);
        intent.putExtra(getString(R.string.product_title), productTitle);
        intent.putExtra(getString(R.string.product_id), productId);
        intent.putExtra(getString(R.string.discounts), listDiscounts);
        startActivity(intent);
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
                            (ItemImagesActivity.this, R.layout.activity_search, R.id.txtProductName, productsCustomList);
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
                HideKeyboard.hideKeyBoard(ItemImagesActivity.this);
            }
        });

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