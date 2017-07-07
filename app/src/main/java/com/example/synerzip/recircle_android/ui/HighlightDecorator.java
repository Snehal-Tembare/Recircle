package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.synerzip.recircle_android.R;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;

import java.util.Date;

/**
 * Created by Prajakta Patil on 7/7/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class HighlightDecorator implements CalendarCellDecorator {

    private Date highlightedDate;
    private Context mContext;

    public HighlightDecorator(Context mContext, @Nullable Date highlightedDate) {

        this.mContext = mContext;
        this.highlightedDate=highlightedDate;
    }

    @Override
    public void decorate(CalendarCellView calendarCellView, Date date) {

        if(date.equals(highlightedDate)){
            calendarCellView.setBackgroundResource(R.drawable.ic_cross);
        }
    }
}
