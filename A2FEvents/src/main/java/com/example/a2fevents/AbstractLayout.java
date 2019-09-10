package com.example.a2fevents;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.File;

public abstract class AbstractLayout extends ConstraintLayout {

    private ProportionalImageView imageView;
    private TextView nameView;
    private TextView descriptionView;
    private TextView timeView;
    private TextView locationView;

    public AbstractLayout(Context context) {
        super(context);
    }

    public AbstractLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbstractLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void setupViews(ProportionalImageView theImageView, TextView theNameView, TextView theDescriptionView, TextView theTimeView, TextView theLocationView) {
        imageView = theImageView;
        nameView = theNameView;
        descriptionView = theDescriptionView;
        timeView = theTimeView;
        locationView = theLocationView;
    }

    public void displayEvent(String imagePath, String month, String dayNumber, String name, String description, String time, String location, String dateAndTime) {

        // Displays the image if it exists
        File image = new File(imagePath);
        if(image.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }

        nameView.setText(name);
        descriptionView.setText(description);
        timeView.setText(time);
        locationView.setText(location);
    }

    public void addToCalendar(View view) {

        // Determine which View collection the view is apart of
        /*int index = 0;
        boolean found = false;
        while(index < MAX_NUM_EVENTS && !found) {

            //found = eventViews[index].contains(view);
            index = found ? index : index + 1;
        }

        if(index < MAX_NUM_EVENTS && !eventViews[index].isEmpty()) {
            // Prompt for calendar addition
            new AddToCalendarDialogFragment(eventViews[index]).show(getSupportFragmentManager(), StringConstants.CALENDAR_PROMPT_TAG);
        }*/
    }
}
