package com.example.a2fevents;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class ImageViewsCollection {

    private ImageView imageView;
    private TextView monthView;
    private TextView dayNumberView;

    public ImageViewsCollection(ImageView theImageView, TextView theMonthView, TextView theDayNumberView) {
        imageView = theImageView;
        monthView = theMonthView;
        dayNumberView = theDayNumberView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getMonthView() {
        return monthView;
    }

    public TextView getDayNumberView() {
        return dayNumberView;
    }

    public void displayEvent(String imagePath, String month, String dayNumber) {

        // Displays the image if it exists
        File image = new File(imagePath);
        if(image.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }

        // Updates the text of the TextViews
        monthView.setText(month);
        dayNumberView.setText(dayNumber);
    }

    public boolean contains(View view) {
        int currentId = view.getId();
        return imageView.getId() == currentId || monthView.getId() == currentId || dayNumberView.getId() == currentId;
    }
}
