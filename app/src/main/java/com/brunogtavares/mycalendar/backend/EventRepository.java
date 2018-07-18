package com.brunogtavares.mycalendar.backend;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.brunogtavares.mycalendar.backend.models.Event;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brunogtavares on 7/17/18.
 */

public class EventRepository {

    private EventsApiService mApiService;

    private static final Object LOCK = new Object();
    private static EventRepository sInstance;

    private EventRepository(EventsApiService apiService){
        this.mApiService = apiService;
    };

    public static EventRepository getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                EventsApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(EventsApiService.class);
                sInstance = new EventRepository(apiService);
            }
        }

        return sInstance;
    }

    public LiveData<List<Event>> getEvents() {
        final MutableLiveData<List<Event>> eventList = new MutableLiveData<>();

        mApiService.getAllEvents().enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                eventList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                eventList.setValue(null);
            }
        });

        return eventList;
    }

    public LiveData<Event> getEvent(long eventId) {
        final MutableLiveData<Event> event = new MutableLiveData<>();

        mApiService.getEventById(eventId).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                event.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                // TODO
            }
        });
        return event;
    }

    public void deleteEvent(long eventId) {
        mApiService.deleteEvent(eventId).enqueue(new Callback<ResponseBody>() {
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

    public void addEvent(Event event) {
        mApiService.addEvent(event).enqueue(new Callback<ResponseBody>() {
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

    public void updateEvent(long id, Event event) {
        mApiService.updateEvent(id, event).enqueue(new Callback<ResponseBody>() {
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

}
