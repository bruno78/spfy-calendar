package com.brunogtavares.mycalendar.backend;

import android.arch.lifecycle.LiveData;

import com.brunogtavares.mycalendar.backend.models.Event;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by brunogtavares on 6/28/18.
 */

public interface EventsApiService {

    @GET("/events")
    Call<List<Event>> getAllEvents();

    @GET("/events/{id}")
    Call<Event> getEventById(@Path("id") long id);

    @POST("/events")
    Call<ResponseBody> addEvent(@Body Event event);

    @PUT("/events/{id}")
    Call<ResponseBody> updateEvent(@Path("id") long id, @Body Event event);

    @DELETE("/events/{id}")
    Call<ResponseBody>  deleteEvent(@Path("id") long id);
}
