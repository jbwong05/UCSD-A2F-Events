package com.example.a2fevents;

import android.widget.TextView;
import java.util.Calendar;

public class EventViewsCollection extends InfoViewsCollection {

    public EventViewsCollection(TextView theNameView, TextView theLocationView, TextView theDateAndTimeView) {
        super(theNameView, theLocationView, theDateAndTimeView);
    }

    public String getLocation() {
        // Parses the specific location
        String text = super.getLocationView().getText().toString();
        text = text.substring(text.indexOf('@') + 2);

        if(text.contains("|")) {
            text = text.substring(0, text.indexOf('|') - 1);
        }

        return text;
    }

    public int getMonth() {
        // Parses the specific month
        String text = super.getDateAndTimeView().getText().toString();
        text = text.substring(0, text.indexOf(' '));

        return convertMonth(text);
    }

    public int getDayNumber() {
        // Parses the specific day number
        String text = super.getDateAndTimeView().getText().toString();
        text = text.substring(text.indexOf(' ') + 1);
        text = text.substring(0, text.indexOf(','));
        return Integer.parseInt(text);
    }

    public int getYear() {
        // Parses the specific year
        String text = super.getDateAndTimeView().getText().toString();
        text = text.substring(text.indexOf(',') + 2);
        text = text.substring(0, text.indexOf(' '));
        return Integer.parseInt(text);
    }

    public int getAMPM() {
        // Parses AM or PM
        String text = super.getDateAndTimeView().getText().toString();
        text = text.substring(text.indexOf('M') - 1, text.indexOf('M') + 1);
        return text.equals("AM") ? Calendar.AM : Calendar.PM;
    }

    public int getStartHour() {
        // Parses the starting hour
        String text = super.getDateAndTimeView().getText().toString();
        text = text.substring(text.indexOf(':') - 1, text.indexOf(':'));
        int time =  Integer.parseInt(text);

        if(getAMPM() == Calendar.PM) {
            time += 12;
        }

        return time;
    }

    public int getEndHour() {
        // Parses the ending hour
        String text = super.getDateAndTimeView().getText().toString();
        text = text.substring(text.indexOf('–') + 2);
        text = text.substring(0, text.indexOf(':'));

        int time =  Integer.parseInt(text);

        if(getAMPM() == Calendar.PM) {
            time += 12;
        }

        return time;
    }

    public int getStartMinute() {
        // Parses the starting minute
        String text = super.getDateAndTimeView().getText().toString();
        text = text.substring(text.indexOf(':') + 1);
        text = text.substring(0, 2);
        return Integer.parseInt(text);
    }

    public int getEndMinute() {
        // Parses the ending minute
        String text = super.getDateAndTimeView().getText().toString();
        text = text.substring(text.indexOf(':') + 1);
        text = text.substring(text.indexOf(':') + 1);
        text = text.substring(0, 2);
        return Integer.parseInt(text);
    }
}
