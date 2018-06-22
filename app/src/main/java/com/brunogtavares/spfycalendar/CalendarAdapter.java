package com.brunogtavares.spfycalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by brunogtavares on 6/21/18.
 */

public class CalendarAdapter extends ArrayAdapter {

    private static final String TAG = CalendarAdapter.class.getSimpleName();

    private LayoutInflater mInflater;
    private List<Date> mMonthlyDates;
    private Calendar mCurrentDate;
    private List<EventDate> mEventsList;

    public CalendarAdapter(@NonNull Context context, List<Date> monthlyDates,
                           List<EventDate> eventsList) {
        super(context, R.layout.view_calendar_day);

        this.mMonthlyDates = monthlyDates;
        this.mEventsList = eventsList;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Date mDate = mMonthlyDates.get(position);
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(mDate);
        int displayDay = calendarDate.get(Calendar.DAY_OF_MONTH);
        int displayMonth = calendarDate.get(Calendar.MONTH);
        int displayYear = calendarDate.get(Calendar.YEAR);

        // Today's date
        Date today = new Date();
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(today);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);

        View view = convertView;

        // Setting up text view for each day
        TextView day = view.findViewById(R.id.tv_single_day);
        day.setText(String.valueOf(displayDay));
        day.setTypeface(null, Typeface.NORMAL);
        day.setTextColor(Color.BLACK);


        if(view == null) {
            view = mInflater.inflate(R.layout.view_calendar_day, parent, false);
        }

        if (displayMonth != currentMonth && displayYear != currentYear) {
            day.setTextColor(getContext().getResources().getColor(R.color.greyed_out));
        }
        else if (currentDay == displayDay) {
            day.setTextColor(getContext().getResources().getColor(R.color.today));
            day.setTypeface(null, Typeface.BOLD);
        }

        Calendar eventCalendar = Calendar.getInstance();

        // Loop through the days with events.
        if(mEventsList.size() > 0) {

            for (EventDate event : mEventsList) {
                eventCalendar.setTime(event.getDate());

                // Mark the event if there's one
                if(displayDay == eventCalendar.get(Calendar.DAY_OF_MONTH) &&
                        displayMonth == eventCalendar.get(Calendar.DAY_OF_MONTH) + 1 &&
                        displayYear == eventCalendar.get(Calendar.YEAR)) {
                    day.setBackgroundColor(getContext().getResources().getColor(R.color.event));
                }
            }
        }
        return view;
    }

    @Override
    public int getCount() {
        if (mMonthlyDates == null) return 0;
        return mMonthlyDates.size();
    }
}
