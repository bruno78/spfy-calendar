package com.brunogtavares.mycalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by brunogtavares on 6/27/18.
 */

public class AddEventActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddEventActivity.class.getSimpleName();
    private final int USERID = 1;

    // default date format
    private static final String DATE_FORMAT = "MMM dd, yyyy";
    private static final String TIME_FORMAT = "HH:mm";

    static final int START_DATE_DIALOG_ID = 0;
    static final int START_TIME_DIALOG_ID = 1;
    static final int END_DATE_DIALOG_ID = 2;
    static final int END_TIME_DIALOG_ID = 3;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

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

        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(mEndDate, mCalendarEndDate, END_DATE_DIALOG_ID);
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
        updateDateDisplay(mEndDate, mCalendarEndDate);
        updateTimeDisplay(mEndTime, mCalendarEndTime);

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
        DatePickerDialog endDateDialog;

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

            case END_DATE_DIALOG_ID:
                endDateDialog = new DatePickerDialog(
                        AddEventActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        mActiveDate.get(Calendar.YEAR),
                        mActiveDate.get(Calendar.MONTH), mActiveDate.get(Calendar.DAY_OF_MONTH));

                endDateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                return endDateDialog;

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
            case END_DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mActiveDate.get(Calendar.YEAR),
                        mActiveDate.get(Calendar.MONTH), mActiveDate.get(Calendar.DAY_OF_MONTH));
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
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(dateTime);
            return date;
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error parsing date: " + e);
            e.printStackTrace();
        }
        return null;
    }
}
