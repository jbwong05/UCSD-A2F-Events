package com.example.a2fevents;

import android.widget.TextView;

public class InfoViewsCollection {

    private TextView nameView;
    private TextView locationView;
    private TextView dateAndTimeView;

    public InfoViewsCollection(TextView theNameView, TextView theLocationView, TextView theDateAndTimeView) {
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
