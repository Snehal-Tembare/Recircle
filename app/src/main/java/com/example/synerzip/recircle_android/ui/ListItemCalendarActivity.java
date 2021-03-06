package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.synerzip.recircle_android.utilities.RCLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.synerzip.recircle_android.R;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.MULTIPLE;

import com.squareup.timessquare.*;

/**
 * Created by Prajakta Patil on 31/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class ListItemCalendarActivity extends AppCompatActivity {

    @BindView(R.id.calendar_view)
    protected CalendarPickerView mPickerView;

    @BindView(R.id.txt_from_date)
    protected TextView mTxtFromDate;

    @BindView(R.id.txt_to_date)
    protected TextView mTxtToDate;

    @BindView(R.id.txt_reset)
    protected TextView mTxtReset;

    public static ArrayList<Date> selectedDates;

    private ArrayList<String> datesList;

    private ArrayList<Date> restoreDates;
    private String dateString;
    private DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        mTxtFromDate.setVisibility(View.GONE);
        mTxtToDate.setVisibility(View.GONE);
        mTxtReset.setVisibility(View.GONE);
        selectedDates = new ArrayList<>();
        datesList = new ArrayList<>();
        restoreDates = new ArrayList<>();

        restoreDates = (ArrayList<Date>) getIntent().getSerializableExtra(getString(R.string.dates));
        dateFormat = new SimpleDateFormat(getString(R.string.calendar_date_format));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 2);
        Date today = new Date();

        mPickerView.setDecorators(Collections.<CalendarCellDecorator>emptyList());

        Calendar nextMonth = Calendar.getInstance();
        nextMonth.add(Calendar.MONTH, 2);

        mPickerView.init(today, nextMonth.getTime()).inMode(MULTIPLE);
        if (!restoreDates.isEmpty()) {

            mPickerView.highlightDates(restoreDates);
        }

        if (MyProfileActivity.isItemEdit) {
            selectedDates.addAll(restoreDates);}
        //on date selected listener
        mPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {

            @Override
            public void onDateSelected(Date date) {


                List<CalendarCellDecorator> decoratorList = new ArrayList<>();
                decoratorList.add(new MonthDecorator(ListItemCalendarActivity.this, date, null,
                        null));
                mPickerView.setDecorators(decoratorList);

                selectedDates.add(date);

                dateString = dateFormat.format(date);
                datesList.add(dateString);
            }

            @Override
            public void onDateUnselected(Date date) {
                selectedDates.remove(date);
                DateFormat newDateFormat = new SimpleDateFormat(getString(R.string.calendar_date_format));
                Log.v("Date", date.toString());
                String dateString = newDateFormat.format(date);
                Log.v("Date", dateString);
                datesList.remove(dateString);

                List<CalendarCellDecorator> decoratorList = new ArrayList<>();
                decoratorList.add(new MonthDecorator(ListItemCalendarActivity.this, null, date,
                        null));
                mPickerView.setDecorators(decoratorList);
            }
        });

        for (Date date1:selectedDates){
            dateString = dateFormat.format(date1);
            datesList.add(dateString);
        }

    }//end onCreate()

    /**
     * save list an item request
     *
     * @param view
     */
    @OnClick(R.id.btn_save)
    public void btnCalendarSave(View view) {
        if (!datesList.isEmpty() && datesList.size() != 0) {
            Intent intent = new Intent(ListItemCalendarActivity.this, HomeActivity.class);
            intent.putExtra(getString(R.string.calendar_availability_days_count), datesList.size());
            intent.putExtra(getString(R.string.calendar_availability_days), datesList);
            intent.putExtra(getString(R.string.selectedDates), selectedDates);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            RCLog.showToast(ListItemCalendarActivity.this, getString(R.string.error_list_item_calendar));
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