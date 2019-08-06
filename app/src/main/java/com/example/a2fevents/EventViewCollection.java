package com.example.a2fevents;

import android.widget.TextView;

public class EventViewCollection {

    private TextView nameView;
    private TextView locationView;
    private TextView dateAndTimeView;

    public EventViewCollection(TextView theNameView, TextView theLocationView, TextView theDateAndTimeView) {

        // Stores the views for this event in instance variables
        nameView = theNameView;
        locationView = theLocationView;
        dateAndTimeView = theDateAndTimeView;
    }

    public void displayEvent(String name, String location, String dateAndTime) {

        // Updates the text of the TextViews
        nameView.setText(name);
        locationView.setText(location);
        dateAndTimeView.setText(dateAndTime);
    }
}
