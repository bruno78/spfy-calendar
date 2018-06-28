package com.brunogtavares.mycalendar.MobileFrontEnd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brunogtavares.mycalendar.R;
import com.brunogtavares.mycalendar.backend.Event;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by brunogtavares on 6/28/18.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private static final String LOG_TAG = EventAdapter.class.getSimpleName();

    final private EventAdapterOnClickHandler mClickHandler;
    private final List<Event> mEvents;
    private Context mContext;

    public interface EventAdapterOnClickHandler {
        void onClick(Event event);
    }


    public EventAdapter(Context context, List<Event> events, EventAdapterOnClickHandler listener) {
        this.mContext = context;
        this.mEvents = events;
        mClickHandler = listener;
    }


    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mEventWeek, mEventDay, mEventDescription,
                mEventStartTime, mEventEndTime;

        public EventViewHolder(View itemView) {
            super(itemView);

            mEventWeek = (TextView) itemView.findViewById(R.id.tv_event_week);
            mEventDay = (TextView) itemView.findViewById(R.id.tv_event_day);
            mEventDescription = (TextView) itemView.findViewById(R.id.tv_event_description);
            mEventStartTime = (TextView) itemView.findViewById(R.id.tv_event_start_date);
            mEventEndTime = (TextView) itemView.findViewById(R.id.tv_event_end_date);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickHandler.onClick(mEvents.get(position));

        }

    }

    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View eventView = inflater.inflate(R.layout.fragment_event_list_item, parent, false);

        return new EventViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = mEvents.get(position);

        EventViewHolder view = holder;

        String week = new SimpleDateFormat("EEE").format(event.getStartTime());
        view.mEventWeek.setText(week);
        String day = new SimpleDateFormat("yyyy").format(event.getStartTime());
        view.mEventDay.setText(day);
        view.mEventDescription.setText(event.getEvent());
        String startTime = new SimpleDateFormat("H:mm a").format(event.getStartTime());
        view.mEventStartTime.setText(startTime);
        String endTime = new SimpleDateFormat("H:mm a").format(event.getEndtime());
        view.mEventEndTime.setText(endTime);

    }

    @Override
    public int getItemCount() {
        return mEvents == null ? 0 : mEvents.size();
    }

    public List<Event> getEvents() {
        return mEvents;
    }

}
