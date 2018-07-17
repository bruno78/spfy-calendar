package com.brunogtavares.mycalendar.MobileFrontEnd.EventList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.brunogtavares.mycalendar.backend.EventRepository;
import com.brunogtavares.mycalendar.backend.EventsApiService;
import com.brunogtavares.mycalendar.backend.RetrofitClientInstance;
import com.brunogtavares.mycalendar.backend.models.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brunogtavares on 7/16/18.
 */

public class EventListViewModel extends AndroidViewModel {

    private LiveData<List<Event>> mEvents;


    public EventListViewModel(@NonNull Application application) {
        super(application);
        mEvents = EventRepository.getInstance().getEvents();
    }

    public LiveData<List<Event>> getEvents() {
        return mEvents;
    }
}
