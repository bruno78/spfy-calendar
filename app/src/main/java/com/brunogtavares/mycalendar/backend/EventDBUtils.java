package com.brunogtavares.mycalendar.backend;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.brunogtavares.mycalendar.backend.models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunogtavares on 6/28/18.
 */

public class EventDBUtils {

    private static final String LOG_TAG = EventDBUtils.class.getSimpleName();
    private static final String JSON_FILE = "db.json";

    final static String DATE_FORMAT = "EEE MMM dd H:mm a";

    private EventDBUtils(){}

    public static String readingFromJson(Context context) {

        String json = null;
        try {
            InputStream is = context.getAssets().open(JSON_FILE);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static List<Event> getEventsFromJSON(Context context) {

        String jsonResponse = readingFromJson(context);

        if(TextUtils.isEmpty(jsonResponse)) return new ArrayList<>();

        List<Event> events = new ArrayList<>();
        try {

            JSONObject response = new JSONObject(jsonResponse);
            JSONArray eventsList = response.getJSONArray("events");

            for( int i = 0; i < eventsList.length(); i++ ) {

                JSONObject eventJson = eventsList.getJSONObject(i);

                int id = eventJson.getInt("id");
                String event = eventJson.getString("event");
                String startDateString = eventJson.getString("startTime");
                String endDateString = eventJson.getString("endTime");;
                int userId = eventJson.getInt("userId");

                events.add(new Event(id, event, startDateString, endDateString, userId));

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Unable to parse event JSON response", e);
        }

        return events;
    }
}
