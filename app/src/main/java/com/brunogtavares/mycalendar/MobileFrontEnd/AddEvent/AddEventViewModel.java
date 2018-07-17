package com.brunogtavares.mycalendar.MobileFrontEnd.AddEvent;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.brunogtavares.mycalendar.backend.EventRepository;
import com.brunogtavares.mycalendar.backend.EventsApiService;
import com.brunogtavares.mycalendar.backend.RetrofitClientInstance;
import com.brunogtavares.mycalendar.backend.models.Event;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brunogtavares on 7/17/18.
 */

public class AddEventViewModel extends ViewModel {

    private LiveData<Event> mEvent;

    public AddEventViewModel(long eventId) {
        mEvent = EventRepository.getInstance().getEvent(eventId);
    }


    public LiveData<Event> getEvent() {
        return mEvent;
    }
}
