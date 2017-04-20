package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prajakta Patil on 20/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class AutocompleteAdapter extends ArrayAdapter<Product> {

    private Context context;
    private int resource, textViewResourceId;
    private List<Product> items, tempItems, suggestions;

    public AutocompleteAdapter(Context context, int resource, int textViewResourceId, List<Product> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.autocomplete_row_layout, parent, false);
        }
        Product product = items.get(position);


        if (product != null) {
            TextView txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            if (txtProductName != null)
                txtProductName.setText(product.getProduct_manufacturer_title());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String manuName = ((Product) resultValue).getProduct_manufacturer_title();
            return manuName;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Product product : tempItems) {

                    if (product.getProduct_manufacturer_title().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(product);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Product> filterList = (ArrayList<Product>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Product product : filterList) {
                    add(product);
                    notifyDataSetChanged();
                }
            }
        }
    };
}