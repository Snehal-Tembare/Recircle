package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.utilities.RCLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.MULTIPLE;

import com.example.synerzip.recircle_android.R;
import com.squareup.timessquare.CalendarPickerView;

public class ListItemCalendarActivity extends AppCompatActivity {
    @BindView(R.id.calendar_availability_view)
    public CalendarPickerView mPickerView;

    ArrayList<Date> selectedDates;

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
        mPickerView.init(today, nextYear.getTime()).withSelectedDate(today).inMode(MULTIPLE);
        mPickerView.init(today, nextYear.getTime()).inMode(MULTIPLE);

        mPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {

            @Override
            public void onDateSelected(Date date) {
                selectedDates = (ArrayList<Date>) mPickerView.getSelectedDates();
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
        if (selectedDates != null) {

            Intent intent = new Intent(ListItemCalendarActivity.this, ListAnItemActivity.class);
            intent.putExtra(getString(R.string.calendar_availability_days), selectedDates);
            intent.putExtra(getString(R.string.calendar_availability_days_count), selectedDates.size());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            RCLog.showToast(ListItemCalendarActivity.this, getString(R.string.error_list_item_calendar));
        }
        RCLog.showToast(this, selectedDates + "");
    }

    /**
     * close the calendar activity
     *
     * @param view
     */
    @OnClick(R.id.txt_calendar_cancel)
    public void txtCalendarCancel(View view) {
        startActivity(new Intent(ListItemCalendarActivity.this, ListAnItemActivity.class));
    }
}