package com.example.synerzip.recircle_android.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;

import java.util.ArrayList;

/**
 * Created by Prajakta Patil on 10/3/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
public class CardRecentItemAdapter extends RecyclerView.Adapter<CardRecentItemAdapter.ViewHolder> {
    private ArrayList<String> itemsList;

    public CardRecentItemAdapter(ArrayList<String> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public CardRecentItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardRecentItemAdapter.ViewHolder viewHolder, int i) {
        viewHolder.mTxtItems.setText(itemsList.get(i));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTxtItems;

        public ViewHolder(View view) {
            super(view);
            mTxtItems = (TextView) view.findViewById(R.id.txt_items);
        }
    }
}
