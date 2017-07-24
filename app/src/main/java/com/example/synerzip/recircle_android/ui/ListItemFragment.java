package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Discounts;
import com.example.synerzip.recircle_android.models.EditProduct;
import com.example.synerzip.recircle_android.models.Product;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.ProductsData;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.models.UserProductDiscount;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.HideKeyboard;
import com.example.synerzip.recircle_android.utilities.NetworkUtility;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.SearchUtility;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prajakta Patil on 15/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class ListItemFragment extends Fragment {

    public static EditProduct editProduct;

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

    public static int mItemPrice, mMinRental;

    public static ArrayList<Discounts> listDiscounts;

    private SearchUtility utility;

    private ArrayList<String> productItemList;

    private AutocompleteAdapter mAutocompleteAdapter;

    @BindView(R.id.auto_txt_list_search_item_name)
    protected AutoCompleteTextView mProductAutoComplete;

    private ArrayList<ProductsData> productsDataList;

    private List<Product> productsCustomList;

    private SearchProduct searchProduct;

    private RCAPInterface service;

    private String manufacturerId = "";

    public static String productId = "";

    private Products product;

    @BindView(R.id.checkbox_discount_five_days)
    protected CheckBox mDiscountForFiveDay;

    @BindView(R.id.checkbox_discount_ten_days)
    protected CheckBox mDiscountForTenDay;

    private Discounts mDiscounts;

    @BindView(R.id.price_slider)
    protected DiscreteSeekBar mDiscreteSeekBar;

    private double productPrice;

    public static String productTitle, mProductName;

    @BindView(R.id.txt_suggested_price)
    protected TextView mTxtSuggestedPrice;

    private long suggestedPrice;

    public static double discFiveDays, discTenDays;

    /**
     * ListItemFragment empty constructor
     */
    public ListItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_list_item, container, false);
        ButterKnife.bind(this, view);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        utility = new SearchUtility(getActivity());
        mProductAutoComplete.setSingleLine();

        if (MyProfileActivity.isItemEdit) {
            mProductAutoComplete.dismissDropDown();
            mProductAutoComplete.setEnabled(false);
        }

        mProductAutoComplete.addTextChangedListener(new ListItemFragment.RCTextWatcher(mProductAutoComplete));
        mEditTxtEnterPrice.addTextChangedListener(new ListItemFragment.RCTextWatcher(mEditTxtEnterPrice));
        mEditMinRental.addTextChangedListener(new ListItemFragment.RCTextWatcher(mEditMinRental));

        listDiscounts = new ArrayList<>();

        final ArrayList<Discounts> strings = new ArrayList<>();
        //discounts checkbox listener
        mDiscountForFiveDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDiscounts = new Discounts(30, 5, 1);
                    strings.add(mDiscounts);
                } else {
                    strings.remove(mDiscounts);
                }
                listDiscounts.addAll(strings);

            }
        });

        mDiscountForTenDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDiscounts = new Discounts(40, 10, 1);

                    strings.add(mDiscounts);
                } else {
                    strings.remove(mDiscounts);
                }
                listDiscounts.addAll(strings);
            }
        });
        listDiscounts.addAll(strings);
        return view;

    }//end onCreateView()

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (MyProfileActivity.isItemEdit){
            mProductAutoComplete.dismissDropDown();
            mProductAutoComplete.setEnabled(false);
        }
    }

    @OnClick(R.id.btn_upload_img)
    public void btnUploadImg(View view) {
        mProductName = mProductAutoComplete.getText().toString();
        submitForm();
        HideKeyboard.hideKeyBoard(getActivity());
        if (NetworkUtility.isNetworkAvailable()) {
            if (getValues()) {
                Intent intent = new Intent(getActivity(), UploadImgActivity.class);
                if (MyProfileActivity.isItemEdit) {
                    intent.putExtra(getString(R.string.product), product);

                    //Set data for edit
                    editProduct.setUser_product_id(product.getUser_product_info().getUser_product_id());
                    editProduct.setPrice_per_day(Integer.parseInt(mEditTxtEnterPrice.getText().toString().trim()));
                    editProduct.setMin_rental_days(Integer.parseInt(mEditMinRental.getText().toString().trim()));
                    editProduct.setUser_prod_discounts(listDiscounts);
                    editProduct.setUser_prod_images(product.getUser_product_info().getUser_prod_images());
                    editProduct.setFromAustin(1);
                }
                startActivity(intent);
            } else {
                RCLog.showToast(getActivity(), getString(R.string.mandatory_dates));
            }
        } else {
            RCLog.showToast(getActivity(), getResources().getString(R.string.err_network_available));
        }
    }

    public boolean getValues() {

        String strPrice = mEditTxtEnterPrice.getText().toString();
        String strRental = mEditMinRental.getText().toString();

        if (!strPrice.isEmpty() && !strRental.isEmpty()) {
            mItemPrice = Integer.parseInt(mEditTxtEnterPrice.getText().toString().trim());
            mMinRental = Integer.parseInt(mEditMinRental.getText().toString().trim());
            if (mDiscounts != null) {
                if (mDiscounts.getDiscount_for_days() != 0) {
                    if (mDiscounts.getDiscount_for_days() == 5) {
                        discFiveDays = Math.round(productPrice * 0.03);
                    }
                    if (mDiscounts.getDiscount_for_days() == 10) {
                        discTenDays = Math.round(productPrice * 0.04);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * search item autocomplete textview
     */
    @Override
    public void onResume() {
        super.onResume();
        productsCustomList = new ArrayList<>();
        productItemList = new ArrayList<>();
        utility.populateAutoCompleteData();

        final ReadyCallback readyCallback = new ReadyCallback() {
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
                            (getActivity(), R.layout.fragment_search_item, R.id.txtProductName, productsCustomList);
                    mProductAutoComplete.setAdapter(mAutocompleteAdapter);

                } else {
                    RCLog.showToast(getActivity(), getString(R.string.product_details_not_found));
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
                if (product.getProduct_detail() != null) {
                    if (product.getProduct_detail().getProduct_price() != 0) {
                        productPrice = product.getProduct_detail().getProduct_price();
                        productTitle = product.getProduct_manufacturer_title();
                    }
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

                        mDiscreteSeekBar.setOnProgressChangeListener
                                (new DiscreteSeekBar.OnProgressChangeListener() {
                                    @Override
                                    public void onProgressChanged(DiscreteSeekBar seekBar,
                                                                  int value, boolean fromUser) {
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
                HideKeyboard.hideKeyBoard(getActivity());
            }
        });

        if (MyProfileActivity.isItemEdit) {
            editProduct = new EditProduct();
            if (ApiClient.getClient(getActivity()) != null) {
                service = ApiClient.getClient(getActivity()).create(RCAPInterface.class);

                if (getArguments() != null) {
                    Call<Products> call = service.getProductDetailsByID(getArguments().getString(getString(R.string.product_id)));

                    call.enqueue(new Callback<Products>() {
                        @Override
                        public void onResponse(Call<Products> call, Response<Products> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    product = response.body();
                                    if (product != null) {
                                        mProductAutoComplete.setText(product.getProduct_info().getProduct_title());
                                        mEditTxtEnterPrice.setText(product.getUser_product_info().getPrice_per_day());
                                        mEditMinRental.setText(product.getUser_product_info().getMin_rental_days());

                                        ArrayList<UserProductDiscount> productDiscountArrayList = product.getUser_product_info().getUser_product_discounts();
                                        if (productDiscountArrayList.size() != 0) {
                                            for (int i = 0; i < productDiscountArrayList.size(); i++) {
                                                if (productDiscountArrayList.get(i).getDiscount_for_days() == 5) {
                                                    mDiscountForFiveDay.setChecked(true);
                                                } else if (productDiscountArrayList.get(i).getDiscount_for_days() == 10) {
                                                    mDiscountForTenDay.setChecked(true);
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Products> call, Throwable t) {

                        }
                    });
                }

            } else {

            }
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
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
}
