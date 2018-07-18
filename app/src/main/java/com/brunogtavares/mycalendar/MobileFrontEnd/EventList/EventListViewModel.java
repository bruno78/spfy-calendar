package com.brunogtavares.mycalendar.MobileFrontEnd.EventList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.brunogtavares.mycalendar.backend.EventRepository;
import com.brunogtavares.mycalendar.backend.models.Event;

import java.util.List;


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
