package com.example.a2fevents;

import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

public class EventViewCollection {

    private ImageView imageView;
    private TextView nameView;
    private TextView locationView;
    private TextView dateAndTimeView;

    public EventViewCollection(ImageView theImageView, TextView theNameView, TextView theLocationView, TextView theDateAndTimeView) {

        // Stores the views for this event in instance variables
        imageView = theImageView;
        nameView = theNameView;
        locationView = theLocationView;
        dateAndTimeView = theDateAndTimeView;
    }

    public void displayEvents(float screenWidth, String imagePath, String name, String location, String dateAndTime) {

        // Displays the image
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        imageView.setMaxHeight((int)(1.791666667 * screenWidth));

        // Updates the text of the TextViews
        nameView.setText(name);
        locationView.setText(location);
        dateAndTimeView.setText(dateAndTime);
    }
}
