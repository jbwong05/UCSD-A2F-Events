package com.example.a2fevents;

import android.widget.ImageView;
import android.widget.TextView;

public class EventViewCollection {

    private ImageViewsCollection imageViewsCollection;
    private InfoViewsCollection infoViewsCollection;

    public EventViewCollection(ImageView theImageView, TextView theMonthView, TextView theDayNumberView, TextView theNameView, TextView theLocationView, TextView theDateAndTimeView) {

        // Creates collection objects for Views
        imageViewsCollection = new ImageViewsCollection(theImageView, theMonthView, theDayNumberView);
        infoViewsCollection = new InfoViewsCollection(theNameView, theLocationView, theDateAndTimeView);
    }

    public ImageViewsCollection getImageViewsCollection() {
        return imageViewsCollection;
    }

    public void displayEvents(String imagePath, String month, String dayNumber, String name, String location, String dateAndTime) {

        // Delegates to each collection
        imageViewsCollection.displayEvent(imagePath, month, dayNumber);
        infoViewsCollection.displayEvent(name, location, dateAndTime);
    }
}
