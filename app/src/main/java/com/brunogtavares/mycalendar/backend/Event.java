package com.brunogtavares.mycalendar.backend;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by brunogtavares on 6/27/18.
 */

public class Event {
    @SerializedName("id")
    private int id;
    @SerializedName("event")
    private String event;
    @SerializedName("startTime")
    private Date startTime;
    @SerializedName("endTime")
    private Date endtime;
    @SerializedName("userId")
    private int userId;

    public Event(String event, Date startTime, Date endTime, int userId ) {
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
