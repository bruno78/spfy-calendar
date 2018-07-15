package com.brunogtavares.mycalendar.MobileFrontEnd;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.brunogtavares.mycalendar.R;
import com.brunogtavares.mycalendar.backend.Event;
import com.brunogtavares.mycalendar.backend.EventDBUtils;
import com.brunogtavares.mycalendar.backend.EventDataService;
import com.brunogtavares.mycalendar.backend.RetrofitClientInstance;

import java.io.Serializable;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brunogtavares on 6/22/18.
 */

public class EventListFragment extends Fragment implements EventAdapter.EventAdapterOnClickHandler{

    private static final String LOG_TAG = EventListFragment.class.getSimpleName();
    private EventAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private FloatingActionButton mFab;
    public EventListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event_list, container,false);

        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab_add_event_btn);

        final TextView emptyText = rootView.findViewById(R.id.tv_empty_view);
        emptyText.setText("No events yet!");

        final EventDataService service = RetrofitClientInstance.getRetrofitInstance().create(EventDataService.class);
        Call<List<Event>> call = service.getAllEvents();
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                emptyText.setVisibility(View.GONE);

                List<Event> eventList = response.body();

                mRecyclerView = getActivity().findViewById(R.id.rv_event_list);
                mAdapter = new EventAdapter(getContext(), eventList, EventListFragment.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                List<Event> eventList = EventDBUtils.getEventsFromJSON(getContext());
                Log.i(LOG_TAG, "List size: " + eventList);
                mRecyclerView = getActivity().findViewById(R.id.rv_event_list);
                mAdapter = new EventAdapter(getContext(), eventList, EventListFragment.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                int position = viewHolder.getAdapterPosition();
                List<Event> events = mAdapter.getEvents();
                Call<ResponseBody> call = service.deleteEvent(events.get(position).getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(getContext(), "Event was successfully deleted!", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "Something went wrong, unable to delete event", Toast.LENGTH_LONG);
                    }
                });

            }
        }).attachToRecyclerView(mRecyclerView);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddEventActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onClick(Event event) {
        Intent intent = new Intent(getContext(), AddEventActivity.class);
        intent.putExtra(AddEventActivity.EXTRA_TASK_ID, event.getId());
        startActivity(intent);

    }
}

