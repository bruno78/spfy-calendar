package com.brunogtavares.mycalendar.backend;

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

public interface EventDataService {

    @GET("/events")
    Call<List<Event>> getAllEvents();

    @POST("/events")
    Call<Event> addEvent(@Body Event event);

    @PUT("/events/{id}")
    Call<ResponseBody> updateEvent(@Path("id") String id, @Body Event event);

    @DELETE("/events/{id}")
    Call<ResponseBody>  deleteEvent(@Path("id") String id);
}
