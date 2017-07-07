package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.annotation.Nullable;
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
    private Date selectedDate, unSelectedDate;
    private Context mContext;

    public MonthDecorator(Context mContext,
                          @Nullable Date selectedDate,
                          @Nullable Date unSelectedDate) {

        this.selectedDate = selectedDate;
        this.mContext = mContext;
        this.unSelectedDate = unSelectedDate;
    }

    @Override
    public void decorate(CalendarCellView calendarCellView, Date date) {
        if (date == selectedDate) {
            calendarCellView.setBackgroundResource(R.drawable.ic_cross);
        } else {
            if (date == unSelectedDate) {
                calendarCellView.setBackgroundResource(0);
            }
        }
    }
}
