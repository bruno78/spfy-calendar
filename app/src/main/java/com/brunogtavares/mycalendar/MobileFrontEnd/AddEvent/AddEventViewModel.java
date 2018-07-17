package com.brunogtavares.mycalendar.MobileFrontEnd.AddEvent;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

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
    private EventsApiService mApi;
    private long mEventId;

    public AddEventViewModel(EventsApiService apiService, long eventId) {
        mApi = apiService;
        mEventId = eventId;
    }

    public LiveData<Event> getEvent() {

        Call<LiveData<Event>> call = mApi.getEventById(mEventId);
       call.enqueue(new Callback<LiveData<Event>>() {
           @Override
           public void onResponse(Call<LiveData<Event>> call, Response<LiveData<Event>> response) {
               mEvent = response.body();
           }

           @Override
           public void onFailure(Call<LiveData<Event>> call, Throwable t) {
                mEvent = null;
           }
       });

       return mEvent;
    }
}
