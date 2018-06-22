package com.brunogtavares.spfycalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by brunogtavares on 6/22/18.
 */

public class EventListFragment extends Fragment {

    public EventListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event_list, container,false);

        TextView emptyText = rootView.findViewById(R.id.tv_empty_view);
        emptyText.setText("No events yet!");

        return rootView;
    }

}
