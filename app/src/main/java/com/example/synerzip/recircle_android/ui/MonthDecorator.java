package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.example.synerzip.recircle_android.R;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;

import java.util.Date;

/**
 * Created by Prajakta Patil on 28/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */


public class MonthDecorator implements CalendarCellDecorator {

    private Date selectedDate;
    private Context mContext;

    public MonthDecorator(Context mContext, Date selectedDate) {

        this.selectedDate = selectedDate;
        this.mContext = mContext;
    }

    @Override
    public void decorate(CalendarCellView calendarCellView, Date date) {
        if (date == selectedDate) {
            calendarCellView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.calendar_bg));
            calendarCellView.setBackgroundResource(R.drawable.ic_cross);
        }
    }
}
