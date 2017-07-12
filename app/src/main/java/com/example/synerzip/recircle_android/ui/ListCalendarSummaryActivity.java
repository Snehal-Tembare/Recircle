package com.example.synerzip.recircle_android.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.squareup.timessquare.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.synerzip.recircle_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Prajakta Patil on 9/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class ListCalendarSummaryActivity extends AppCompatActivity {
    @BindView(R.id.calendar_view)
    protected CalendarPickerView mPickerView;

    @BindView(R.id.txt_from_date)
    protected TextView mTxtFromDate;

    @BindView(R.id.txt_to_date)
    protected TextView mTxtToDate;

    @BindView(R.id.txt_reset)
    protected TextView mTxtReset;

    public static ArrayList<Date> unavailableDates;

    @BindView(R.id.btn_save)
    protected Button mBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        unavailableDates = new ArrayList<>();
        unavailableDates = AdditionalDetailsActivity.selectedDates;

        mTxtFromDate.setVisibility(View.GONE);
        mTxtToDate.setVisibility(View.GONE);
        mTxtReset.setVisibility(View.GONE);
        mBtnSave.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 2);
        Date today = new Date();

        mPickerView.init(today, calendar.getTime());
        mPickerView.setPressed(false);
        mPickerView.setSaveEnabled(false);
        mPickerView.setEnabled(false);
        mPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                RCLog.showToast(ListCalendarSummaryActivity.this, "Date cannot be selected");
                mPickerView.setPressed(false);
                mPickerView.setClickable(false);
                mPickerView.setSelected(false);
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
        List<CalendarCellDecorator> decoratorList = new ArrayList<>();

        if (unavailableDates != null && !unavailableDates.isEmpty()) {
            for (Date date : unavailableDates) {
                decoratorList.add(new MonthDecorator(ListCalendarSummaryActivity.this, null,
                        null, date));
            }
            mPickerView.setDecorators(decoratorList);
        }
    }

    /**
     * close the calendar activity
     *
     * @param view
     */
    @OnClick(R.id.txt_cancel)
    public void txtCalendarCancel(View view) {
        finish();
    }
}
