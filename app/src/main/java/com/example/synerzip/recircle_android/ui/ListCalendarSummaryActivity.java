package com.example.synerzip.recircle_android.ui;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.squareup.timessquare.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private ArrayList<Date> unavailableDates;

    @BindView(R.id.btn_save)
    protected Button mBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        unavailableDates = new ArrayList<>();
        unavailableDates = (ArrayList<Date>) getIntent().getSerializableExtra(getString(R.string.unavail_dates));

        mTxtFromDate.setVisibility(View.GONE);
        mTxtToDate.setVisibility(View.GONE);
        mTxtReset.setVisibility(View.GONE);
        mBtnSave.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 2);
        Date today = new Date();

        mPickerView.init(today, calendar.getTime());
        mPickerView.highlightDates(unavailableDates);

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
