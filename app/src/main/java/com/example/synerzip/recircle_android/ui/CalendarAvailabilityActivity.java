package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.synerzip.recircle_android.R;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.RANGE;
/**
 * Created by Prajakta Patil on 3/4/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
public class CalendarAvailabilityActivity extends AppCompatActivity {
    @BindView(R.id.calendar_availability_view)
    public CalendarPickerView mPickerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_availability);
        ButterKnife.bind(this);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        mPickerView.init(today, nextYear.getTime()).withSelectedDate(today).inMode(RANGE);
        mPickerView.init(today, nextYear.getTime()).inMode(RANGE);
    }

    @OnClick(R.id.txt_calendar_cancel)
    public void txtCalendarCancel(View view) {
        startActivity(new Intent(CalendarAvailabilityActivity.this, ListAnItemActivity.class));
    }
}
