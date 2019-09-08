package com.example.a2fevents;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.ArrayList;
import java.util.List;

public class ViewCollection {

    private ConstraintLayout constraintLayout;
    private ImageViewsCollection imageViewsCollection;
    private InfoViewsCollection infoViewsCollection;

    public ViewCollection(ConstraintLayout theConstraintLayout, ImageView theImageView, TextView theMonthView, TextView theDayNumberView, TextView theNameView, TextView theLocationView, TextView theDateAndTimeView) {

        // Creates collection objects for Views
        constraintLayout = theConstraintLayout;
        imageViewsCollection = new ImageViewsCollection(theImageView, theMonthView, theDayNumberView);
        infoViewsCollection = new EventViewsCollection(theNameView, theLocationView, theDateAndTimeView);
    }

    public ViewCollection(ConstraintLayout theConstraintLayout, ImageView theImageView, TextView theNameView, TextView theLocationView, TextView theDateAndTimeView, TextView theDescriptionView) {

        // Creates collection objects for Views
        constraintLayout = theConstraintLayout;
        imageViewsCollection = new ImageViewsCollection(theImageView);
        infoViewsCollection = new SaveTheDateViewsCollection(theNameView, theLocationView, theDateAndTimeView, theDescriptionView);
    }

    public ConstraintLayout getConstraintLayout() {
        return constraintLayout;
    }

    public ImageViewsCollection getImageViewsCollection() {
        return imageViewsCollection;
    }

    public List<View> getViewsList() {
        List<View> viewList = new ArrayList<>();
        viewList.addAll(imageViewsCollection.getViewsList());
        viewList.addAll(infoViewsCollection.getViewList());
        return viewList;
    }

    public void setStatus(String status) {
        infoViewsCollection.setStatus(status);
    }

    public void displayEvent(String imagePath, String month, String dayNumber, String name, String location, String dateAndTime, String description) {

        // Delegates to each collection
        imageViewsCollection.displayEvent(imagePath, month, dayNumber);
        infoViewsCollection.displayEvent(month, dayNumber, name, location, dateAndTime, description);
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

    public String getDescription() {
        return infoViewsCollection.getDescription();
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