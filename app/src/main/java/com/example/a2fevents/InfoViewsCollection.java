package com.example.a2fevents;

import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class InfoViewsCollection {
    private TextView nameView;
    private TextView locationView;
    private TextView dateAndTimeView;

    public InfoViewsCollection(TextView theNameView, TextView theLocationView, TextView theDateAndTimeView) {
        nameView = theNameView;
        locationView = theLocationView;
        dateAndTimeView = theDateAndTimeView;
    }

    public void displayEvent(String month, String dayNumber, String name, String location, String dateAndTime, String description) {

        // Updates the text of the TextViews
        nameView.setText(name);
        locationView.setText(location);
        dateAndTimeView.setText(dateAndTime);
    }

    public TextView getLocationView() {
        return locationView;
    }

    public TextView getDateAndTimeView() {
        return dateAndTimeView;
    }

    public List<View> getViewList() {
        List<View> viewList = new ArrayList<>();
        viewList.add(nameView);
        viewList.add(locationView);
        viewList.add(dateAndTimeView);
        return viewList;
    }

    public void setStatus(String status) {
        nameView.setText(status);
    }

    public boolean contains(View view) {
        int currentId = view.getId();
        return nameView.getId() == currentId || locationView.getId() == currentId || dateAndTimeView.getId() == currentId;
    }

    public boolean isEmpty() {
        return nameView.getText().toString().equals("") && locationView.getText().toString().equals("") && dateAndTimeView.getText().toString().equals("");
    }

    public String getName() {
        return nameView.getText().toString();
    }

    public String getDescription() {
        return locationView.getText().toString();
    }

    public int convertMonth(String text) {

        // Converts month from text to Calendar constant equivalent
        switch(text) {
            case "January":
            case "Jan":
                return Calendar.JANUARY;
            case "February":
            case "Feb":
                return Calendar.FEBRUARY;
            case "March":
            case "Mar":
                return Calendar.MARCH;
            case "April":
            case "Apr":
                return Calendar.APRIL;
            case "May":
                return Calendar.MAY;
            case "June":
            case "Jun":
                return Calendar.JUNE;
            case "July":
            case "Jul":
                return Calendar.JULY;
            case "August":
            case "Aug":
                return Calendar.AUGUST;
            case "September":
            case "Sep":
                return Calendar.SEPTEMBER;
            case "October":
            case "Oct":
                return Calendar.OCTOBER;
            case "November":
            case "Nov":
                return Calendar.NOVEMBER;
            case "December":
            case "Dec":
                return Calendar.DECEMBER;
            default:
                return -1;
        }
    }

    abstract String getLocation();
    abstract int getMonth();
    abstract int getDayNumber();
    abstract int getYear();
    abstract int getAMPM();
    abstract int getStartHour();
    abstract int getEndHour();
    abstract int getStartMinute();
    abstract int getEndMinute();
}
