package com.brunogtavares.mycalendar.MobileFrontEnd;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.brunogtavares.mycalendar.MobileFrontEnd.AddEvent.AddEventActivity;
import com.brunogtavares.mycalendar.MobileFrontEnd.CustomCalendar.CalendarView;
import com.brunogtavares.mycalendar.R;
import com.brunogtavares.mycalendar.backend.EventsApiService;
import com.brunogtavares.mycalendar.backend.models.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brunogtavares on 6/22/18.
 */

public class    CalendarFragment extends Fragment {

    private static final String FULL_DATE_FORMAT = "E MMM dd HH:mm:ss Z yyyy";

    private FloatingActionButton mFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        mFab = rootView.findViewById(R.id.fab_add_calendar);

        HashSet<Date> events = new HashSet<>();
        events.add(new Date());

        CalendarView cv = (CalendarView)rootView.findViewById(R.id.calendar_view);
        cv.updateCalendar(events);

        // assign event handler
        // When user press and hold it takes to add an activity
        cv.setEventHandler(new CalendarView.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date)
            {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(getActivity(), df.format(date), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), AddEventActivity.class);
                String dateString = df.format(date);
                intent.putExtra("date", dateString);
                startActivity(intent);
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getContext(), AddEventActivity.class);
                startActivity(intent);
            }
        });

        return rootView;

    }
}
