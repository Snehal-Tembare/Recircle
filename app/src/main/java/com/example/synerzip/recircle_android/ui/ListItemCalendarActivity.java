package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.utilities.RCLog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.RANGE;

import com.squareup.timessquare.CalendarPickerView;
/**
 * Created by Prajakta Patil on 31/3/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
public class ListItemCalendarActivity extends AppCompatActivity {
    @BindView(R.id.calendar_availability_view)
    public CalendarPickerView mPickerView;

    ArrayList<Date> selectedDates;
    Date selectFromDate, selectToDate, fromDate, toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_calendar_availability);
        ButterKnife.bind(this);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();

        mPickerView.init(today, nextYear.getTime()).inMode(RANGE);

        //on date selected listener
        mPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {

            @Override
            public void onDateSelected(Date date) {
                selectedDates = (ArrayList<Date>) mPickerView.getSelectedDates();
                selectFromDate = selectedDates.get(0);
                selectToDate = selectedDates.get(selectedDates.size() - 1);

                DateFormat formatter = new SimpleDateFormat(getString(R.string.date_format));
                try {
                    fromDate = formatter.parse(selectFromDate.toString());
                    toDate = formatter.parse(selectToDate.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });
    }

    /**
     * save list an item request
     *
     * @param view
     */
    @OnClick(R.id.btn_calendar_save)
    public void btnCalendarSave(View view) {
        if (fromDate != null && toDate != null) {
            Intent intent = new Intent(ListItemCalendarActivity.this, ListAnItemActivity.class);
            intent.putExtra(getString(R.string.unavail_from_date), fromDate.toString());
            intent.putExtra(getString(R.string.unavail_to_date), toDate.toString());
            intent.putExtra(getString(R.string.calendar_availability_days_count), selectedDates.size());
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
    @OnClick(R.id.txt_calendar_cancel)
    public void txtCalendarCancel(View view) {
/*        Intent intent=new Intent();
        intent.setClassName(this,"com.example.synerzip.recircle_android.ui.ListAnItemActivity");
        startActivity(intent);
        */
        finish();
    }
}