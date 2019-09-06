package com.example.a2fevents;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class EventViewCollection {

    private ConstraintLayout constraintLayout;
    private ImageViewsCollection imageViewsCollection;
    private InfoViewsCollection infoViewsCollection;

    public EventViewCollection(ConstraintLayout theConstraintLayout, ImageView theImageView, TextView theMonthView, TextView theDayNumberView, TextView theNameView, TextView theLocationView, TextView theDateAndTimeView) {

        // Creates collection objects for Views
        constraintLayout = theConstraintLayout;
        imageViewsCollection = new ImageViewsCollection(theImageView, theMonthView, theDayNumberView);
        infoViewsCollection = new InfoViewsCollection(theNameView, theLocationView, theDateAndTimeView);
    }

    public ConstraintLayout getConstraintLayout() {
        return constraintLayout;
    }

    public ImageViewsCollection getImageViewsCollection() {
        return imageViewsCollection;
    }

    public void setStatus(String status) {
        infoViewsCollection.setStatus(status);
    }

    public void displayEvents(String imagePath, String month, String dayNumber, String name, String location, String dateAndTime) {

        // Delegates to each collection
        imageViewsCollection.displayEvent(imagePath, month, dayNumber);
        infoViewsCollection.displayEvent(name, location, dateAndTime);
    }

    public boolean contains(View view) {
        // Delegates to other contains methods
        return imageViewsCollection.contains(view) || infoViewsCollection.contains(view);
    }

    public boolean isEmpty() {
        return infoViewsCollection.isEmpty();
    }

    public String getName() {
        return infoViewsCollection.getName();
    }

    public String getSecondLineInfo() {
        return infoViewsCollection.getSecondLineInfo();
    }

    public String getLocation() {
        return infoViewsCollection.getLocation();
    }

    public int getMonth() {
        return infoViewsCollection.getMonth();
    }

    public int getDayNumber() {
        return infoViewsCollection.getDayNumber();
    }

    public int getYear() {
        return infoViewsCollection.getYear();
    }

    public int getStartHour() {
        return infoViewsCollection.getStartHour();
    }

    public int getEndHour() {
        return infoViewsCollection.getEndHour();
    }

    public int getStartMinute() {
        return infoViewsCollection.getStartMinute();
    }

    public int getEndMinute() {
        return infoViewsCollection.getEndMinute();
    }
}
