package com.example.synerzip.recircle_android.ui.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prajakta Patil on 16/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

//TODO the implementation is in progress

public class MsgAutocompleteAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource, textViewResourceId;
    private List<String> items, tempItems, suggestions;

    public MsgAutocompleteAdapter(Context context, int resource, int textViewResourceId,
                                  List<String> items) {
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
            view = inflater.inflate(R.layout.layout_owner_msg, parent, false);
        }
        String product = items.get(position);


        if (product != null) {
            TextView txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            if (txtProductName != null)
                txtProductName.setText(product);
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
            String value = ((String) resultValue);
            return value;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (String product : tempItems) {

                    if (product.toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            List<String> filterList = (ArrayList<String>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (String product : filterList) {
                    add(product);
                    notifyDataSetChanged();
                }
            }
        }
    };
}