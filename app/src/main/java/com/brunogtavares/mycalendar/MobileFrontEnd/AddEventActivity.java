package com.brunogtavares.mycalendar.MobileFrontEnd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.brunogtavares.mycalendar.R;
import com.brunogtavares.mycalendar.backend.Event;
import com.brunogtavares.mycalendar.backend.EventDataService;
import com.brunogtavares.mycalendar.backend.RetrofitClientInstance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brunogtavares on 6/27/18.
 */

public class AddEventActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddEventActivity.class.getSimpleName();
    public static final String EXTRA_TASK_ID = "EVENT";
    private final int USERID = 1;

    private static final int DEFAULT_TASK_ID = -1;
    private int mEventId = DEFAULT_TASK_ID;

    // default date format
    private static final String DATE_FORMAT = "MMM dd, yyyy";
    private static final String TIME_FORMAT = "HH:mm";

    static final int START_DATE_DIALOG_ID = 0;
    static final int START_TIME_DIALOG_ID = 1;
    static final int END_TIME_DIALOG_ID = 2;

    private EditText mEvent;
    private TextView mStartDate;
    private TextView mStartTime;
    private TextView mEndDate;
    private TextView mEndTime;

    private Calendar mCalendarStartDate;
    private Calendar mCalendarStartTime;
    private Calendar mCalendarEndDate;
    private Calendar mCalendarEndTime;

    private Button mSaveButton;

    private TextView mActiveDateDisplay;
    private Calendar mActiveDate;

    private EventDataService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mAPIService = RetrofitClientInstance.getRetrofitInstance().create(EventDataService.class);

        mEvent = (EditText) findViewById(R.id.et_event_description);
        mStartDate = (TextView) findViewById(R.id.tv_start_date);
        mStartTime = (TextView) findViewById(R.id.tv_start_time);
        mEndDate = (TextView) findViewById(R.id.tv_end_date);
        mEndTime = (TextView) findViewById(R.id.tv_end_time);

        mSaveButton = (Button) findViewById(R.id.bt_save_button);

        mCalendarStartDate = Calendar.getInstance();
        mCalendarStartTime = Calendar.getInstance();
        mCalendarEndDate = Calendar.getInstance();
        mCalendarEndTime = Calendar.getInstance();

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(mStartDate, mCalendarStartDate, START_DATE_DIALOG_ID);
            }
        });

        mStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDateDialog(mStartTime, mCalendarStartTime, START_TIME_DIALOG_ID);
            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(mEndTime, mCalendarEndTime, END_TIME_DIALOG_ID);
            }
        });

        updateDateDisplay(mStartDate, mCalendarStartDate);
        updateTimeDisplay(mStartTime, mCalendarStartTime);
        updateDateDisplay(mEndDate, mCalendarStartDate);
        updateTimeDisplay(mEndTime, mCalendarEndTime);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mSaveButton.setText(R.string.update_button);
            if (mEventId == DEFAULT_TASK_ID) {
                // populate the UI
                mEventId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);
            }
        }

        // TODO: Implement SAVE BUTTON,
        // Check for
        // 1. end date < start date
        // 2. null or empty description
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event;

                String eventDescription = mEvent.getText().toString();
                String startDate = mStartDate.getText().toString();
                String startTime = mStartTime.getText().toString();
                String endDate = mStartDate.getText().toString();
                String endtime = mEndTime.getText().toString();
                Date startEvent = parseStringToDate(startDate, startTime);
                Date endEvent = parseStringToDate(endDate, endtime);

                event = new Event(eventDescription, startEvent, endEvent, USERID);

                Log.d(LOG_TAG, " " + eventDescription + "\n"
                        + startEvent + "\n"
                        + endDate + "\n"
                        + startTime + "\n"
                        + endtime + "\n");

                if(mEvent.getText().toString().isEmpty()) {
                    Toast.makeText(AddEventActivity.this, "Event field cannot be blank", Toast.LENGTH_SHORT);

                }
                else {
                    sendPost(event);
                }


            }
        });

    }

    private void sendPost(Event event) {
        Call<Event> call = mAPIService.addEvent(event);
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if(response.isSuccessful()) {
                    Log.i(LOG_TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.e(LOG_TAG, "Unable to submit post to API.");
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mActiveDate.set(Calendar.YEAR, year);
            mActiveDate.set(Calendar.MONTH, monthOfYear);
            mActiveDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateDisplay(mActiveDateDisplay, mActiveDate);
            unregisterDateDisplay();
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            mActiveDate.set(Calendar.HOUR_OF_DAY, hour);
            mActiveDate.set(Calendar.MINUTE, minute);
            updateTimeDisplay(mActiveDateDisplay, mActiveDate);
            unregisterDateDisplay();
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        DatePickerDialog startDatedialog;

        switch (id) {
            case START_DATE_DIALOG_ID:
                startDatedialog = new DatePickerDialog(
                        AddEventActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        mActiveDate.get(Calendar.YEAR),
                        mActiveDate.get(Calendar.MONTH), mActiveDate.get(Calendar.DAY_OF_MONTH));

                startDatedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                return startDatedialog;

            case START_TIME_DIALOG_ID:
                return new TimePickerDialog(this, timeSetListener,
                        mActiveDate.get(Calendar.HOUR_OF_DAY), mActiveDate.get(Calendar.MINUTE), true);

            case END_TIME_DIALOG_ID:
                return new TimePickerDialog(this, timeSetListener,
                        mActiveDate.get(Calendar.HOUR_OF_DAY), mActiveDate.get(Calendar.MINUTE), true);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case START_DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mActiveDate.get(Calendar.YEAR),
                        mActiveDate.get(Calendar.MONTH), mActiveDate.get(Calendar.DAY_OF_MONTH));
                break;
            case START_TIME_DIALOG_ID:
                ((TimePickerDialog) dialog).updateTime( mActiveDate.get(Calendar.HOUR_OF_DAY),
                        mActiveDate.get(Calendar.MINUTE));
                break;
            case END_TIME_DIALOG_ID:
                ((TimePickerDialog) dialog).updateTime( mActiveDate.get(Calendar.HOUR_OF_DAY),
                        mActiveDate.get(Calendar.MINUTE));
                break;
        }
    }

    private void unregisterDateDisplay() {
        mActiveDateDisplay = null;
        mActiveDate = null;
    }

    public void showDateDialog(TextView dateDisplay, Calendar date, int dialogId) {
        mActiveDateDisplay = dateDisplay;
        mActiveDate = date;
        showDialog(dialogId);
    }

    private void updateDateDisplay(TextView textViewDate, Calendar calendarStartDate) {
        textViewDate.setText(getStringDate(calendarStartDate));
    }

    private void updateTimeDisplay(TextView textViewDate, Calendar calendarStartDate) {
        textViewDate.setText(getStringTime(calendarStartDate));
    }

    private static String getStringDate(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        return format.format(calendar.getTime());
    }

    private static String getStringTime(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT, Locale.US);
        return format.format(calendar.getTime());
    }

    private static Date parseStringToDate(String eventDate, String evenTime) {

        String dateTime = eventDate + " " + evenTime;

        String format = DATE_FORMAT + " " + TIME_FORMAT;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
            Date date = sdf.parse(dateTime);
            return date;
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error parsing date: " + e);
            e.printStackTrace();
        }
        return null;
    }
}
