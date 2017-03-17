package com.example.synerzip.recircle_android.ui;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.All_Product_Info;
import com.example.synerzip.recircle_android.models.Product;
import com.example.synerzip.recircle_android.models.ProductDetails;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.ProductsData;
import com.example.synerzip.recircle_android.models.RootObject;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.NetworkUtility;
import com.example.synerzip.recircle_android.utilities.RCLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prajakta Patil on 7/3/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;

    HashMap<String, List<String>> listDataChild;

    public List<Integer> groupImages;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;

    @BindView(R.id.nav_view)
    public NavigationView navigationView;

    private ArrayList itemsList;
    private static final String TAG = "MainActivity";

    public String query;
    public ArrayList<String> productItemList;

    public ArrayAdapter<String> adapter;

    private RCAPInterface service;

    private static String ID;

    @BindView(R.id.txtAutocomplete)
    public AutoCompleteTextView productAutoComplete;

    public List <ProductsData> productsDataList;

    private List<ProductDetails> productDetails;

    private ArrayList<String> allItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (NetworkUtility.isNetworkAvailable(this)) {
            setSupportActionBar(toolbar);

            //   getSupportActionBar().setIcon(R.mipmap.recircle_app_icon);
            getSupportActionBar().setHomeButtonEnabled(true);
            ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            drawer.setDrawerListener(toggle);
            toggle.syncState();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                        drawer.closeDrawer(Gravity.RIGHT);
                    } else {
                        drawer.openDrawer(Gravity.RIGHT);
                    }
                }
            });
            navigationView.setNavigationItemSelectedListener(this);

            expListView = (ExpandableListView) findViewById(R.id.expListView);
            prepareListData();

            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    return false;
                }
            });
            expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                }
            });
            expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {

                }
            });
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    // TODO Auto-generated method stub
                    Toast.makeText(
                            getApplicationContext(),
                            listDataHeader.get(groupPosition)
                                    + " : "
                                    + listDataChild.get(
                                    listDataHeader.get(groupPosition)).get(
                                    childPosition), Toast.LENGTH_SHORT)
                            .show();
                    return false;
                }
            });
            initViews();
            initViewsForPopItems();
            getAllProductDetails();
        }else{
            RCLog.showToast(this,"Network not available. Please try again");
        }
    }//end onCreate()

    public void getAllProductDetails(){
        allItemsList = new ArrayList<>();

        service = ApiClient.getClient().create(RCAPInterface.class);

        Call<All_Product_Info> call = service.getProductDetails();

        call.enqueue(new Callback<All_Product_Info>() {
            @Override
            public void onResponse(Call<All_Product_Info> call, Response<All_Product_Info> response) {
                if (null != response) {
                    productDetails = response.body().getProductDetails();
                    Log.v("All Product Info", "" + productDetails.size());
                    for (ProductDetails productDetails1 : productDetails) {
                        allItemsList.add(productDetails1.getProduct_info().getProduct_title());
                        Log.v("Popular products", "" + productDetails1.getProduct_info().getProduct_title());
                    }
                } else {
                    RCLog.showToast(getApplicationContext(), "Bad gateway");
                }
            }

            @Override
            public void onFailure(Call<All_Product_Info> call, Throwable t) {
                Log.v(TAG, t.toString());
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        productItemList=new ArrayList<>();

        query = productAutoComplete.getText().toString();

        service = ApiClient.getClient().create(RCAPInterface.class);

        Call<RootObject> call = service.productNames();

        call.enqueue(new Callback<RootObject>() {
            @Override
            public void onResponse(Call<RootObject> call, Response<RootObject> response) {
                if (null != response) {
                     productsDataList= response.body().getProductsData();
                    Log.v("Output", "" + productsDataList.size());
                    for (int i=0;i<productsDataList.size();i++) {

                        productItemList.add( productsDataList.get(i).getProduct_manufacturer_name());
                       ArrayList<Product> productsList= productsDataList.get(i).getProducts();
                        for(int j=0;j<productsList.size();j++){
                            productItemList
                                    .add(productsDataList
                                            .get(i).getProduct_manufacturer_name()
                                            +" "+productsList.get(j).getProduct_title());
                        }

                        Log.v("Products Names", "" + productsDataList.get(i).getProduct_manufacturer_name());
                    }
                    adapter = new ArrayAdapter<>
                            (MainActivity.this, android.R.layout.simple_list_item_1, productItemList);
                    productAutoComplete.setAdapter(adapter);

                } else {
                    RCLog.showToast(getApplicationContext(), "Product Not Found");
                }
            }

            @Override
            public void onFailure(Call<RootObject> call, Throwable t) {}
        });

        productAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductsData pd = productsDataList.get(position);
                ID = pd.getProduct_manufacturer_id();
                Log.v("manfctr_ID",ID);
            }
        });

    }//end onResume()

    @OnClick(R.id.btn_click)
    public void callSearchApi() {
        Call<SearchProduct> call = service.searchProduct(ID);
        call.enqueue(new Callback<SearchProduct>() {
            @Override
            public void onResponse(Call<SearchProduct> call, Response<SearchProduct> response) {
                if (null != response && null != response.body()) {
                    ArrayList<Products> productsArrayList = response.body().getProducts();
                    for (Products products : productsArrayList) {
                        Log.v("Product from list", products.getProduct_info().getProduct_manufacturer_name());
                    }
                } else {
                    RCLog.showToast(getApplicationContext(), "Bad gateway");
                }
            }

            @Override
            public void onFailure(Call<SearchProduct> call, Throwable t) {

            }
        });

    }//end callSearchApi()

    /*
* Preparing the list data
*/
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        groupImages = new ArrayList<>();

        groupImages.add(R.drawable.ic_shield);
        groupImages.add(R.drawable.ic_creditcard);
        groupImages.add(R.drawable.ic_store);

        // Adding child data
        listDataHeader.add("We offer protection");
        listDataHeader.add("Pick-up / drop-off at the Recircle Store");
        listDataHeader.add("We handle payments.");

        List<String> expHeader1 = new ArrayList<>();
        expHeader1.add(getResources().getString(R.string.exp_list_content));

        List<String> expHeader2 = new ArrayList<>();
        expHeader1.add(getResources().getString(R.string.exp_list_content));

        List<String> expHeader3 = new ArrayList<>();
        expHeader3.add(getResources().getString(R.string.exp_list_content));

        listDataChild.put(listDataHeader.get(0), expHeader1);
        listDataChild.put(listDataHeader.get(1), expHeader2);
        listDataChild.put(listDataHeader.get(2), expHeader3);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.launching, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        itemsList = new ArrayList<>();
        itemsList.add("Alpha a77II");
        itemsList.add("Alpha a77II");
        itemsList.add("Alpha a77II");
        itemsList.add("Alpha a77II");
        itemsList.add("Alpha a77II");
        itemsList.add("EOS 5D Mark IV");
        RecyclerView.Adapter adapter = new CardRecentItemAdapter(itemsList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),
                    new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }
                    });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    private void initViewsForPopItems() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view_pop_items);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        itemsList = new ArrayList<>();
        itemsList.add("Alpha a77II");
        itemsList.add("Alpha a77II");
        itemsList.add("Alpha a77II");
        itemsList.add("Alpha a77II");
        itemsList.add("Alpha a77II");
        itemsList.add("EOS 5D Mark IV");
        RecyclerView.Adapter adapter = new CardPopItemsAdapter(itemsList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),
                    new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }
                    });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }
}





