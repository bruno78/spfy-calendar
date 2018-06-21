package com.brunogtavares.spfycalendar;

import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

public class CalendarFragment extends Fragment {

    private static final String TAG = CalendarFragment.class.getSimpleName();

    private static int NUMBER_OF_DAYS = 42;

    private LinearLayout mWeekHeader;
    private ImageButton mPrevButton, mNextButton;
    private TextView mDateTitle;
    private GridView mGridView;

    private Adapter mGridAdapter;

    private Calendar mCurrentDate;

    public CalendarFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.view_calendar, container, false);

        mWeekHeader = (LinearLayout) rootView.findViewById(R.id.ll_week_header);
        mPrevButton = (ImageButton) rootView.findViewById(R.id.ib_cal_prev_button);
        mNextButton = (ImageButton) rootView.findViewById(R.id.ib_cal_next_button);
        mDateTitle = (TextView) rootView.findViewById(R.id.tv_cal_title);
        mGridView = (GridView) rootView.findViewById(R.id.gv_days_of_month);

        mGridAdapter = mGridView.getAdapter();

        mCurrentDate = Calendar.getInstance();

        return rootView;
    }

    private void generateCalendar() {

        List<Date> days = new ArrayList<>();

        // creates a copy of the calendar
        Calendar calendar = (Calendar) mCurrentDate.clone();

        // Setting the first day of the month to determine which day of the week it starts
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int beginningOfTheMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // Repositioning the calendar to the beginning of the week.
        calendar.add(Calendar.DAY_OF_MONTH, - beginningOfTheMonth);

        // Populating the calendar based on a 6 rows (42 days) considering the worst
        // case scenario where the first of the month falls on the last day of the week
        // and it has 31 days.
        while (days.size() < NUMBER_OF_DAYS) {
            days.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }



    }

    private class CalendarAdapter extends ArrayAdapter<Date> {

        public CalendarAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            // Check if the existing view is being reused, otherwise inflate the view.
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(
                        R.layout.view_calendar, parent, false);
            }

            // Current day
            Date day = getItem(position);

            // Today
            Date today = new Date();



        }
    }


}