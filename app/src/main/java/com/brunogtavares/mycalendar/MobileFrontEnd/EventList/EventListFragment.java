package com.brunogtavares.mycalendar.MobileFrontEnd.EventList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.brunogtavares.mycalendar.MobileFrontEnd.AddEvent.AddEventActivity;
import com.brunogtavares.mycalendar.MobileFrontEnd.ViewLifecycleFragment;
import com.brunogtavares.mycalendar.R;
import com.brunogtavares.mycalendar.backend.EventRepository;
import com.brunogtavares.mycalendar.backend.EventTaskExecutors;
import com.brunogtavares.mycalendar.backend.models.Event;
import com.brunogtavares.mycalendar.backend.EventsApiService;
import com.brunogtavares.mycalendar.backend.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brunogtavares on 6/22/18.
 */

public class EventListFragment extends ViewLifecycleFragment implements EventListAdapter.EventAdapterOnClickHandler{

    private static final String LOG_TAG = EventListFragment.class.getSimpleName();
    private EventListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private EventRepository mRepo;

    private TextView mEmptyText;

    private FloatingActionButton mFab;
    public EventListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event_list, container,false);

        mRecyclerView = rootView.findViewById(R.id.rv_event_list);
        mAdapter = new EventListAdapter(getContext(), EventListFragment.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab_add_event_btn);

        mEmptyText = rootView.findViewById(R.id.tv_empty_view);
        mEmptyText.setText("No events yet!");

        mRepo = EventRepository.getInstance();

//        final EventsApiService service = RetrofitClientInstance.getRetrofitInstance().create(EventsApiService.class);
//        Call<List<Event>> call = service.getAllEvents();
//        call.enqueue(new Callback<List<Event>>() {
//            @Override
//            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
//                emptyText.setVisibility(View.GONE);
//
//                List<Event> eventList = response.body();
//
//                mAdapter.setEvents(eventList);
//                mRecyclerView.setAdapter(mAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<List<Event>> call, Throwable t) {
//                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
//            }
//        });


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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewModel();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left on a ViewHolder to delete an entry
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // swipe to delete

                EventTaskExecutors.getsInstance().networkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Event> events = mAdapter.getEvents();
                        //deleteEvent(events, position);
                        mRepo.deleteEvent(events.get(position).getId());

                    }
                });

            }
        }).attachToRecyclerView(mRecyclerView);
    }


    private void setupViewModel() {
        EventListViewModel viewModel = ViewModelProviders.of(this).get(EventListViewModel.class);
        final Observer<List<Event>> eventsObserver = new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                Log.d(LOG_TAG, "Receiving database update from LiveData in ViewModel");
                if( events != null) {
                    mEmptyText.setVisibility(View.GONE);
                    mAdapter.setEvents(events);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        };
        viewModel.getEvents().observe(this, eventsObserver);
    }


    private void deleteEvent(List<Event> events, int position) {

        final EventsApiService service = RetrofitClientInstance.getRetrofitInstance().create(EventsApiService.class);
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

    @Override
    public void onClick(Event event) {
        Intent intent = new Intent(getContext(), AddEventActivity.class);
        intent.putExtra(AddEventActivity.EXTRA_EVENT_ID, event.getId());
        startActivity(intent);

    }


}

