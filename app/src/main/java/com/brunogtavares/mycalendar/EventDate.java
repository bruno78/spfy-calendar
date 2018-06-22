package com.brunogtavares.mycalendar;

import java.util.Date;

/**
 * Created by brunogtavares on 6/21/18.
 */

public class EventDate {
    private int id;
    private String message;
    private Date date;

    public EventDate(String message, Date date) {
        this.message = message;
        this.date = date;
    }

    public EventDate(int id, String message, Date date) {
        this.message = message;
        this.date = date;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}
