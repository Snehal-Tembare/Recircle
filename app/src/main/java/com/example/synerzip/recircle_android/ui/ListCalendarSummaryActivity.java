package com.example.synerzip.recircle_android.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.MULTIPLE;

public class ListCalendarSummaryActivity extends AppCompatActivity {
    @BindView(R.id.calendar_view)
    protected CalendarPickerView mPickerView;

    @BindView(R.id.txt_from_date)
    protected TextView mTxtFromDate;

    @BindView(R.id.txt_to_date)
    protected TextView mTxtToDate;

    @BindView(R.id.txt_reset)
    protected TextView mTxtReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        mTxtFromDate.setVisibility(View.GONE);
        mTxtToDate.setVisibility(View.GONE);
        mTxtReset.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 2);
        Date today = new Date();

        mPickerView.init(today, calendar.getTime());
    }
}
