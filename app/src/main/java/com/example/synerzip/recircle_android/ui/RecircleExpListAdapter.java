package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Prajakta Patil on 10/3/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
public class RecircleExpListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> headerTitleList;
    private HashMap<String, List<String>> headerDataList;

    public RecircleExpListAdapter(Context mContext, List<String> headerTitleList,
                                  HashMap<String, List<String>> listChildData) {
        this.mContext = mContext;
        this.headerTitleList = headerTitleList;
        this.headerDataList = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.headerDataList.get(this.headerTitleList.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String headerData = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.exp_list_item, null);
        }

        TextView mTxtHeaderData = (TextView) convertView
                .findViewById(R.id.txtListItem);

        mTxtHeaderData.setText(headerData);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.headerDataList.get(this.headerTitleList.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.headerTitleList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.headerTitleList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.exp_list_header, null);
        }

        TextView mTxtHeader = (TextView) convertView
                .findViewById(R.id.txtListHeader);
        mTxtHeader.setTypeface(null, Typeface.BOLD);
        mTxtHeader.setText(headerTitle);

       // ImageView imageView=(ImageView)convertView.findViewById(R.id.imgHeader);
       // int imageId = this.groupImages.get(groupPosition);
      //  imageView.setImageBitmap();

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
