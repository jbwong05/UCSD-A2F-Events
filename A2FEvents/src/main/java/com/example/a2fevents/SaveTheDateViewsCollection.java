package com.example.a2fevents;

import android.view.View;
import android.widget.TextView;
import java.util.Calendar;
import java.util.List;

public class SaveTheDateViewsCollection extends InfoViewsCollection {

    private TextView descriptionView;

    public SaveTheDateViewsCollection(TextView theNameView, TextView theLocationView, TextView theDateAndTimeView, TextView theDescriptionView) {
        super(theNameView, theLocationView, theDateAndTimeView);
        descriptionView = theDescriptionView;
    }

    @Override
    public List<View> getViewList() {
        List<View> viewList = super.getViewList();
        viewList.add(descriptionView);
        return viewList;
    }

    @Override
    public boolean contains(View view) {
        int currentId = view.getId();
        return super.contains(view) || descriptionView.getId() == currentId;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && descriptionView.getText().equals("");
    }

    @Override
    public String getDescription() {
        return descriptionView.getText().toString();
    }

    @Override
    String getLocation() {
        // Parses the location
        String locationText = super.getLocationView().getText().toString();
        locationText = locationText.substring(locationText.indexOf(':') + 2);
        return locationText;
    }

    @Override
    int getMonth() {
        // Parses the specific month
        String text = super.getDateAndTimeView().getText().toString();
        text = text.substring(text.indexOf(':') + 2);
        text = text.substring(0, text.indexOf(' '));

        return convertMonth(text);
    }

    @Override
    int getDayNumber() {
        // Parses the day number
        String text = super.getDateAndTimeView().getText().toString();
        text = text.substring(text.indexOf(' ') + 1);
        text = text.substring(text.indexOf(' ') + 1);
        text = text.substring(0, text.indexOf(' '));
        return Integer.parseInt(text);
    }

    @Override
    int getYear() {
        // Assumes the current year
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    @Override
    int getAMPM() {
        // Parses AM or PM
        String text = super.getDateAndTimeView().getText().toString();
        text = text.substring(text.indexOf('@') + 7);
        return text.equals("AM") ? Calendar.AM : Calendar.PM;
    }

    @Override
    int getStartHour() {
        // Parses the starting hour
        String text = super.getDateAndTimeView().getText().toString();
        text = text.substring(text.indexOf('@') + 2);
        text = text.substring(0, 1);
        int time =  Integer.parseInt(text);

        if(getAMPM() == Calendar.PM) {
            time += 12;
        }

        return time;
    }

    @Override
    int getEndHour() {
        // No specified ending hour
        // Default to 1 hour duration
        int endHour = getStartHour() + 1;
        return endHour > 24 ? 1 : endHour;
    }

    @Override
    int getStartMinute() {
        // Parses the starting minute
        String text = super.getDateAndTimeView().getText().toString();
        text = text.substring(text.indexOf(':') + 1);
        text = text.substring(text.indexOf(':') + 1);
        text = text.substring(0, 2);
        return Integer.parseInt(text);
    }

    @Override
    int getEndMinute() {
        // No specified ending hour
        // Default to 0
        return 0;
    }

    @Override
    public void displayEvent(String name, String month, String dayNumber, String location, String dateAndTime, String description) {

        // Updates the text of the TextViews
        super.displayEvent(name, month, dayNumber, location, dateAndTime, description);
        descriptionView.setText(description);
    }
}
