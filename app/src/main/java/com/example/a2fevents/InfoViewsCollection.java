package com.example.a2fevents;

import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class InfoViewsCollection {

    private TextView nameView;
    private TextView locationView;
    private TextView dateAndTimeView;

    public InfoViewsCollection(TextView theNameView, TextView theLocationView, TextView theDateAndTimeView) {
        nameView = theNameView;
        locationView = theLocationView;
        dateAndTimeView = theDateAndTimeView;
    }

    public void setStatus(String status) {
        if(nameView.getId() == R.id.firstEventName) {
            nameView.setText(status);
        }
    }

    public void displayEvent(String name, String location, String dateAndTime) {

        // Updates the text of the TextViews
        nameView.setText(name);
        locationView.setText(location);
        dateAndTimeView.setText(dateAndTime);
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

    public String getSecondLineInfo() {
        return locationView.getText().toString();
    }

    public String getLocation() {
        String text = locationView.getText().toString();
        text = text.substring(text.indexOf('@') + 2);

        if(text.contains("|")) {
            text = text.substring(0, text.indexOf('|') - 1);
        }

        return text;
    }

    public int getMonth() {
        String text = dateAndTimeView.getText().toString();
        text = text.substring(0, text.indexOf(' '));

        switch(text) {
            case "Jan":
                return Calendar.JANUARY;
            case "Feb":
                return Calendar.FEBRUARY;
            case "Mar":
                return Calendar.MARCH;
            case "Apr":
                return Calendar.APRIL;
            case "May":
                return Calendar.MAY;
            case "Jun":
                return Calendar.JUNE;
            case "Jul":
                return Calendar.JULY;
            case "Aug":
                return Calendar.AUGUST;
            case "Sep":
                return Calendar.SEPTEMBER;
            case "Oct":
                return Calendar.OCTOBER;
            case "Nov":
                return Calendar.NOVEMBER;
            case "Dec":
                return Calendar.DECEMBER;
            default:
                return -1;
        }
    }

    public int getDayNumber() {
        String text = dateAndTimeView.getText().toString();
        text = text.substring(text.indexOf(' ') + 1);
        text = text.substring(0, text.indexOf(','));
        return Integer.parseInt(text);
    }

    public int getYear() {
        String text = dateAndTimeView.getText().toString();
        text = text.substring(text.indexOf(',') + 2);
        text = text.substring(0, text.indexOf(' '));
        return Integer.parseInt(text);
    }

    public int getAMPM() {
        String text = dateAndTimeView.getText().toString();
        text = text.substring(text.indexOf('M') - 1, text.indexOf('M') + 1);
        return text.equals("AM") ? Calendar.AM : Calendar.PM;
    }

    public int getStartHour() {
        String text = dateAndTimeView.getText().toString();
        text = text.substring(text.indexOf(':') - 1, text.indexOf(':'));
        int time =  Integer.parseInt(text);

        if(getAMPM() == Calendar.PM) {
            time += 12;
        }

        return time;
    }

    public int getEndHour() {
        String text = dateAndTimeView.getText().toString();
        text = text.substring(text.indexOf('–') + 2);
        text = text.substring(0, text.indexOf(':'));

        int time =  Integer.parseInt(text);

        if(getAMPM() == Calendar.PM) {
            time += 12;
        }

        return time;
    }

    public int getStartMinute() {
        String text = dateAndTimeView.getText().toString();
        text = text.substring(text.indexOf(':') + 1);
        text = text.substring(0, 2);
        return Integer.parseInt(text);
    }

    public int getEndMinute() {
        String text = dateAndTimeView.getText().toString();
        text = text.substring(text.indexOf(':') + 1);
        text = text.substring(text.indexOf(':') + 1);
        text = text.substring(0, 2);
        return Integer.parseInt(text);
    }
}
