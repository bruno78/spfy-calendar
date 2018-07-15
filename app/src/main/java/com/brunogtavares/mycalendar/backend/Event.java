package com.brunogtavares.mycalendar.backend;

import com.google.gson.annotations.SerializedName;


/**
 * Created by brunogtavares on 6/27/18.
 */

public class Event {
    @SerializedName("id")
    private int id;
    @SerializedName("event")
    private String event;
    @SerializedName("startTime")
    private String startTime;
    @SerializedName("endTime")
    private String endtime;
    @SerializedName("userId")
    private int userId;

    public Event(String event, String startTime, String endTime, int userId ) {
        this.event = event;
        this.startTime = startTime;
        this.endtime = endTime;
        this.userId = userId;
    }
    public Event(int id, String event, String startTime, String endTime, int userId ) {
        this.id = id;
        this.event = event;
        this.startTime = startTime;
        this.endtime = endTime;
        this.userId = userId;
    }

    public int getId() { return id; }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
