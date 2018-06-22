package com.brunogtavares.spfycalendar;

import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarView extends LinearLayout {

    private static final String TAG = CalendarView.class.getSimpleName();

    private static int NUMBER_OF_DAYS = 42;

    private LinearLayout mWeekHeader;
    private ImageButton mPrevButton, mNextButton;
    private TextView mDateTitle;
    private GridView mGridView;

    private Adapter mGridAdapter;

    private Calendar mCurrentDate;

    public CalendarView(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mWeekHeader = (LinearLayout) findViewById(R.id.ll_week_header);
        mPrevButton = (ImageButton) findViewById(R.id.ib_cal_prev_button);
        mNextButton = (ImageButton) findViewById(R.id.ib_cal_next_button);
        mDateTitle = (TextView) findViewById(R.id.tv_cal_title);
        mGridView = (GridView) findViewById(R.id.gv_days_of_month);

        mGridAdapter = mGridView.getAdapter();

        mCurrentDate = Calendar.getInstance();
    }


    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void generateCalendar() {

        List<Date> days = new ArrayList<>();

        // creates a copy of the calendar
        Calendar calendar = (Calendar) mCurrentDate.clone();

        // Setting the first day of the month to determine which day of the week it starts
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // Repositioning the calendar to the beginning of the week.
        calendar.add(Calendar.DAY_OF_MONTH, - firstDayOfTheMonth);

        // Populating the calendar based on a 6 rows (42 days) considering the worst
        // case scenario where the first of the month falls on the last day of the week
        // and it has 31 days.
        while (days.size() < NUMBER_OF_DAYS) {
            days.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }




    }

}