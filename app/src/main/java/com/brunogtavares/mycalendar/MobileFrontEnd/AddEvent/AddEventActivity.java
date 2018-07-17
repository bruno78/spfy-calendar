package com.brunogtavares.mycalendar.MobileFrontEnd.AddEvent;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.brunogtavares.mycalendar.R;
import com.brunogtavares.mycalendar.backend.EventTaskExecutors;
import com.brunogtavares.mycalendar.backend.models.Event;
import com.brunogtavares.mycalendar.backend.EventsApiService;
import com.brunogtavares.mycalendar.backend.RetrofitClientInstance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brunogtavares on 6/27/18.
 */

public class AddEventActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddEventActivity.class.getSimpleName();
    // Extra for the event ID to be received in the intent
    public static final String EXTRA_EVENT_ID = "EVENT";
    // Extra for the event ID to be received after rotation
    public static final String INSTANCE_EVENT_ID = "instanceEventId";
    // Mock user
    private final int USERID = 1;


    private static final int DEFAULT_TASK_ID = -1;
    private int mEventId = DEFAULT_TASK_ID;

    // default date format
    private static final String DATE_FORMAT = "MMM dd, yyyy";
    private static final String TIME_FORMAT = "HH:mm";
    private static final String FULL_DATE_FORMAT = "E MMM dd HH:mm:ss Z yyyy";

    static final int START_DATE_DIALOG_ID = 0;
    static final int START_TIME_DIALOG_ID = 1;
    static final int END_TIME_DIALOG_ID = 2;

    private EditText mEventTextView;
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

    private EventsApiService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mAPIService = RetrofitClientInstance.getRetrofitInstance().create(EventsApiService.class);

        mEventTextView = (EditText) findViewById(R.id.et_event_description);
        mStartDate = (TextView) findViewById(R.id.tv_start_date);
        mStartTime = (TextView) findViewById(R.id.tv_start_time);
        mEndDate = (TextView) findViewById(R.id.tv_end_date);
        mEndTime = (TextView) findViewById(R.id.tv_end_time);

        mSaveButton = (Button) findViewById(R.id.bt_save_button);

        mCalendarStartDate = Calendar.getInstance();
        mCalendarStartTime = Calendar.getInstance();
        mCalendarEndDate = Calendar.getInstance();
        mCalendarEndTime = Calendar.getInstance();


        // Updating an event
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_EVENT_ID)) {
            mSaveButton.setText(R.string.update_button);
            if (mEventId == DEFAULT_TASK_ID) {
                // populate the UI
                mEventId = intent.getIntExtra(EXTRA_EVENT_ID, DEFAULT_TASK_ID);

                mAPIService = RetrofitClientInstance.getRetrofitInstance().create(EventsApiService.class);
                AddEventViewModelFactory factory = new AddEventViewModelFactory(mEventId);
                final AddEventViewModel viewModel =
                        ViewModelProviders.of(this, factory).get(AddEventViewModel.class);
                viewModel.getEvent().observe(this, new Observer<Event>() {
                    @Override
                    public void onChanged(@Nullable Event event) {
                        viewModel.getEvent().removeObserver(this);

                        populateUI(event);
                    }
                });
            }
        }

        // Getting intent from the calendar
        String dateExtra = intent.getStringExtra("date");
        if(dateExtra != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
            try {
                mCalendarStartDate.setTime(sdf.parse(dateExtra));
                mCalendarEndDate.setTime(sdf.parse(dateExtra));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }



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


        // TODO: Implement SAVE BUTTON,
        // Check for
        // 1. end date < start date
        // 2. null or empty description
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String eventDescription = mEventTextView.getText().toString();
                String startDate = mStartDate.getText().toString();
                String startTime = mStartTime.getText().toString();
                String endDate = mStartDate.getText().toString();
                String endTime = mEndTime.getText().toString();
                String startEvent = completeDate(startDate, startTime);
                String endEvent = completeDate(endDate, endTime);

                final Event event = new Event(eventDescription, startEvent, endEvent, USERID);

                Log.d(LOG_TAG, " " + eventDescription + "\n"
                        + startEvent + "\n"
                        + endDate);

                if(mEventTextView.getText().toString().isEmpty()) {
                    Toast.makeText(AddEventActivity.this, "Event field cannot be blank", Toast.LENGTH_SHORT);

                }
                else {
                    EventTaskExecutors.getsInstance().networkIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(mEventId == DEFAULT_TASK_ID) {
                                saveEvent(event);
                            }
                            else {
                                updateEvent((long) mEventId, event);
                            }

                        }
                    });
                }
                finish();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_EVENT_ID, mEventId);
        super.onSaveInstanceState(outState);
    }

    private void updateEvent(Long id, Event event) {

        Call<ResponseBody> call = mAPIService.updateEvent(id, event);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // TODO
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // TODO
            }
        });
    }

    private void saveEvent(Event event) {
        Call<ResponseBody> call = mAPIService.addEvent(event);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // TODO
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // TODO
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

    private static String completeDate(String eventDate, String evenTime) {

        String dateTime = eventDate + " " + evenTime;

        String format = DATE_FORMAT + " " + TIME_FORMAT;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
            SimpleDateFormat fullFormat = new SimpleDateFormat(FULL_DATE_FORMAT, Locale.US);
            Date date = sdf.parse(dateTime);
            return fullFormat.format(date);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error parsing date: " + e);
            e.printStackTrace();
        }
        return null;
    }

    private void populateUI(Event event) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        SimpleDateFormat sdfTime = new SimpleDateFormat(TIME_FORMAT, Locale.US);
        SimpleDateFormat sdfFullTime = new SimpleDateFormat(FULL_DATE_FORMAT, Locale.US);


        try {
            Date dateStart = sdfFullTime.parse(event.getStartTime());
            String startDateString = sdf.format(dateStart);
            String startTimeString = sdfTime.format(dateStart);

            Date dateEnd = sdfFullTime.parse(event.getEndtime());
            String endDateString = sdf.format(dateEnd);
            String endTimeString = sdfTime.format(dateEnd);

            mStartDate.setText(startDateString);
            mStartTime.setText(startTimeString);

            mEndDate.setText(endDateString);
            mEndTime.setText(endTimeString);

            mCalendarStartDate.setTime(sdf.parse(startDateString));
            mCalendarStartTime.setTime(sdfTime.parse(startTimeString));

            mCalendarEndDate.setTime(sdf.parse(endDateString));
            mCalendarEndTime.setTime(sdfTime.parse(endTimeString));

            mEventTextView.setText(event.getEvent());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
