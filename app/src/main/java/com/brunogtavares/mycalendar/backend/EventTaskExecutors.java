package com.brunogtavares.mycalendar.backend;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by brunogtavares on 7/17/18.
 */

public class EventTaskExecutors {

    private static final Object LOCK = new Object();
    private static EventTaskExecutors sInstance;
    private final Executor mNetworkIO;

    private EventTaskExecutors(Executor networkIO) {
        this.mNetworkIO = networkIO;
    }

    public static EventTaskExecutors getsInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new EventTaskExecutors(
                        Executors.newFixedThreadPool(3));
            }
        }

        return sInstance;
    }

    public Executor networkIO() { return mNetworkIO; }

}
