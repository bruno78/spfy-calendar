package com.brunogtavares.mycalendar.MobileFrontEnd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.brunogtavares.mycalendar.R;
import com.brunogtavares.mycalendar.backend.Event;
import com.brunogtavares.mycalendar.backend.EventDataService;
import com.brunogtavares.mycalendar.backend.RetrofitClientInstance;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brunogtavares on 6/22/18.
 */

public class EventListFragment extends Fragment implements EventAdapter.EventAdapterOnClickHandler{

    private EventAdapter mAdapter;
    private RecyclerView mRecyclerView;
    public EventListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event_list, container,false);

        final TextView emptyText = rootView.findViewById(R.id.tv_empty_view);
        emptyText.setText("No events yet!");

        EventDataService service = RetrofitClientInstance.getRetrofitInstance().create(EventDataService.class);
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
            }
        });

        return rootView;
    }

    @Override
    public void onClick(Event event) {
        Intent intent = new Intent(getContext(), AddEventActivity.class);
        intent.putExtra(AddEventActivity.EXTRA_TASK_ID, (Serializable) event);
        startActivity(intent);

    }
}

