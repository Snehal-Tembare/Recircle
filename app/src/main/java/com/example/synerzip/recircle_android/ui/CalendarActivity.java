package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.UserProductUnAvailability;
import com.example.synerzip.recircle_android.ui.rentitem.RentInfoActivity;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.squareup.timessquare.CalendarPickerView;

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

/**
 * Created by Prajakta Patil on 24/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class CalendarActivity extends AppCompatActivity {

    public static ArrayList<Date> selectedDates;

    public ArrayList<Date> localselectedDates;

    private ArrayList<UserProductUnAvailability> userProductUnAvailabilities;

    private Bundle bundle;

    private Products product;

    public static boolean isDateSelected = false;

    @BindView(R.id.calendar_view)
    protected CalendarPickerView mPickerView;

    private Date fromDate;
    private Date toDate;

    private Date selectFromDate;
    private Date selectToDate;

    @BindView(R.id.txt_from_date)
    protected TextView mTxtFromDate;

    @BindView(R.id.txt_to_date)
    protected TextView mTxtToDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        selectedDates = new ArrayList<>();

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        bundle = getIntent().getExtras();

        if (bundle != null) {
            userProductUnAvailabilities = bundle.getParcelableArrayList(getString(R.string.unavail_dates));
            product = bundle.getParcelable(getString(R.string.product));
            if (userProductUnAvailabilities != null && userProductUnAvailabilities.size() != 0) {

                final ArrayList<Date> dateArray = new ArrayList<>();

                for (UserProductUnAvailability unAvailability : userProductUnAvailabilities) {

                    DateFormat dateFormat = new SimpleDateFormat(getString(R.string.calendar_date_format));

                    Date parsedDate = null;
                    try {
                        parsedDate = dateFormat.parse(unAvailability.getUnavai_from_date());
                        dateArray.add(parsedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                mPickerView.setDateSelectableFilter(new CalendarPickerView.DateSelectableFilter() {
                    @Override
                    public boolean isDateSelectable(Date date) {
                        for (int i = 0; i < dateArray.size(); i++) {
                            if (dateArray.contains(date)) {
                                return false;
                            }
                            return true;
                        }
                        return true;
                    }
                });
            }
        }

        mPickerView.init(today, nextYear.getTime()).withSelectedDate(today).inMode(RANGE);
        mPickerView.init(today, nextYear.getTime()).inMode(RANGE);

        if (RentInfoActivity.isDateEdited) {
            if (bundle != null && null != bundle.getSerializable(getString(R.string.selected_dates_list))) {
                selectedDates = (ArrayList<Date>) getIntent().getExtras().getSerializable(getString(R.string.selected_dates_list));

                mPickerView.highlightDates(selectedDates);

                selectFromDate = selectedDates.get(0);
                selectToDate = selectedDates.get(selectedDates.size() - 1);

                DateFormat formatter = new SimpleDateFormat(getString(R.string.date_format));
                try {
                    fromDate = formatter.parse(selectFromDate.toString());
                    toDate = formatter.parse(selectToDate.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar calFromDate = Calendar.getInstance();
                Calendar calToDate = Calendar.getInstance();
                calFromDate.setTime(fromDate);
                calToDate.setTime(toDate);

                CharSequence weekdayFromDate =
                        android.text.format.DateFormat.format(getString(R.string.day_format), fromDate);
                CharSequence weekdayToDate =
                        android.text.format.DateFormat.format(getString(R.string.day_format), toDate);
                CharSequence monthFromDate =
                        android.text.format.DateFormat.format(getString(R.string.month_format), fromDate);
                CharSequence monthToDate =
                        android.text.format.DateFormat.format(getString(R.string.month_format), toDate);

                String formatedFromDate =
                        weekdayFromDate + " , " + calFromDate.get(Calendar.DATE) + " " + monthFromDate
                                + " " + calFromDate.get(Calendar.YEAR);
                String formatedToDate =
                        weekdayToDate + " , " + calToDate.get(Calendar.DATE) + " " + monthToDate
                                + " " + calToDate.get(Calendar.YEAR);

                mTxtFromDate.setText(formatedFromDate);
                mTxtToDate.setText(formatedToDate);
            }
        }


        //on date selected listener
        mPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {


                mPickerView.clearHighlightedDates();

                localselectedDates = (ArrayList<Date>) mPickerView.getSelectedDates();
                selectFromDate = localselectedDates.get(0);
                selectToDate = localselectedDates.get(localselectedDates.size() - 1);

                DateFormat formatter = new SimpleDateFormat(getString(R.string.date_format));
                try {
                    fromDate = formatter.parse(selectFromDate.toString());
                    toDate = formatter.parse(selectToDate.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar calFromDate = Calendar.getInstance();
                Calendar calToDate = Calendar.getInstance();
                calFromDate.setTime(fromDate);
                calToDate.setTime(toDate);

                CharSequence weekdayFromDate =
                        android.text.format.DateFormat.format(getString(R.string.day_format), fromDate);
                CharSequence weekdayToDate =
                        android.text.format.DateFormat.format(getString(R.string.day_format), toDate);
                CharSequence monthFromDate =
                        android.text.format.DateFormat.format(getString(R.string.month_format), fromDate);
                CharSequence monthToDate =
                        android.text.format.DateFormat.format(getString(R.string.month_format), toDate);

                String formatedFromDate =
                        weekdayFromDate + " , " + calFromDate.get(Calendar.DATE) + " " + monthFromDate
                                + " " + calFromDate.get(Calendar.YEAR);
                String formatedToDate =
                        weekdayToDate + " , " + calToDate.get(Calendar.DATE) + " " + monthToDate
                                + " " + calToDate.get(Calendar.YEAR);

                mTxtFromDate.setText(formatedFromDate);
                mTxtToDate.setText(formatedToDate);
            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });
    }

    /**
     * textview cancel to go to previous activity
     *
     * @param view
     */
    @OnClick(R.id.txt_cancel)
    public void txtCancel(View view) {
        if (RentInfoActivity.isDateEdited && selectedDates.size() == 0) {
            RCLog.showToast(CalendarActivity.this, getString(R.string.error_dates));
        } else {
            finish();
        }
    }

    /**
     * button for save dates
     *
     * @param view
     */
    @OnClick(R.id.btn_save)
    public void btnSave(View view) {

        if (fromDate != null && toDate != null) {
            Intent intent = new Intent(CalendarActivity.this, SearchItemFragment.class);
            intent.putExtra(getString(R.string.from_date), fromDate.toString());
            intent.putExtra(getString(R.string.to_date), toDate.toString());
            intent.putExtra(getString(R.string.selected_dates_list), selectedDates);
            if (fromDate.equals(toDate)) {
                RCLog.showToast(getApplicationContext(), getString(R.string.date_validation_code));
            } else {

                selectedDates.clear();
                if (localselectedDates != null && localselectedDates.size() != 0) {
                    selectedDates.addAll(localselectedDates);
                }
                if (RentInfoActivity.isDateEdited) {
                    RentInfoActivity.isDateChanged = true;
                }
                if (DetailsActivity.isShowInfo) {
                    Intent infoIntent = new Intent(this, RentInfoActivity.class);
                    infoIntent.putExtra(getString(R.string.from_date), fromDate.toString());
                    infoIntent.putExtra(getString(R.string.to_date), toDate.toString());
                    infoIntent.putExtra(getString(R.string.selected_dates_list), selectedDates);
                    infoIntent.putExtra(getString(R.string.product), product);
                    startActivity(infoIntent);
                    DetailsActivity.isShowInfo = false;
                } else {
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        } else {
            RCLog.showToast(CalendarActivity.this, getString(R.string.error_dates));
        }
    }

    /**
     * reset start and end date
     *
     * @param view
     */
    @OnClick(R.id.txt_reset)
    public void txtReset(View view) {
        mTxtFromDate.setText(getString(R.string.enter_start_date));
        mTxtToDate.setText(R.string.enter_end_date);
        if (RentInfoActivity.isDateEdited) {
            selectedDates.clear();
            mPickerView.clearHighlightedDates();
        }
    }
}
