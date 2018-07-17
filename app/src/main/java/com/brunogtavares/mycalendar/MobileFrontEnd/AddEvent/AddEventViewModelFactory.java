package com.brunogtavares.mycalendar.MobileFrontEnd.AddEvent;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.brunogtavares.mycalendar.backend.EventsApiService;
import com.brunogtavares.mycalendar.backend.RetrofitClientInstance;

/**
 * Created by brunogtavares on 7/17/18.
 */

public class AddEventViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final long mEventId;

    public AddEventViewModelFactory(long eventId) {
        mEventId = eventId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AddEventViewModel(mEventId);
    }


}
