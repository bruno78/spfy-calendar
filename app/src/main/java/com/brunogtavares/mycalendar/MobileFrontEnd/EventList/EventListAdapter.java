package com.brunogtavares.mycalendar.MobileFrontEnd.EventList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brunogtavares.mycalendar.R;
import com.brunogtavares.mycalendar.backend.models.Event;

import java.util.List;

/**
 * Created by brunogtavares on 6/28/18.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    private static final String LOG_TAG = EventListAdapter.class.getSimpleName();

    final private EventAdapterOnClickHandler mClickHandler;
    private List<Event> mEvents;
    private Context mContext;

    public interface EventAdapterOnClickHandler {
        void onClick(Event event);
    }


    public EventListAdapter(Context context, EventAdapterOnClickHandler listener) {
        this.mContext = context;
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
    public EventListAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View eventView = inflater.inflate(R.layout.fragment_event_list_item, parent, false);

        return new EventViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = mEvents.get(position);

        EventViewHolder view = holder;

        String[] date = event.getStartTime().split(" ");
        String[] endDate = event.getEndtime().split(" ");

        String week = date[0];
        view.mEventWeek.setText(week);
        String day = date[2];
        view.mEventDay.setText(day);
        view.mEventDescription.setText(event.getEvent());
        String startTime = date[3];
        view.mEventStartTime.setText(startTime);
        String endTime = endDate[3];
        view.mEventEndTime.setText(endTime);

    }

    @Override
    public int getItemCount() {
        return mEvents == null ? 0 : mEvents.size();
    }

    public List<Event> getEvents() {
        return mEvents;
    }
    public void setEvents(List<Event> events) {
        this.mEvents = events;
    }

}
