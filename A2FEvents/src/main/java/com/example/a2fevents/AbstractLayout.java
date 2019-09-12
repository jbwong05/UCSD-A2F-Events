package com.example.a2fevents;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.File;
import java.util.Calendar;

public abstract class AbstractLayout extends ConstraintLayout {

    private ProportionalImageView imageView;
    private TextView nameView;
    private TextView descriptionView;
    private TextView timeView;
    private TextView locationView;
    private String nameText;
    private String descriptionText;
    protected String timeText;
    private String locationText;

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

        // Sets and stores text of TextViews
        nameView.setText(name);
        descriptionView.setText(description);
        timeView.setText(time);
        locationView.setText(location);

        nameText = name;
        descriptionText = description;
        timeText = time;
        locationText = location;
    }

    protected void setupOnClickListener(View.OnClickListener listener) {
        // Sets up onClickListener
        imageView.setOnClickListener(listener);
        nameView.setOnClickListener(listener);
        descriptionView.setOnClickListener(listener);
        timeView.setOnClickListener(listener);
        locationView.setOnClickListener(listener);
    }

    public String getName() {
        return nameText;
    }

    public String getDescription() {
        return descriptionText;
    }

    public String getLocation() {
        // Assumes locationText format of WHERE: QUALCOMM ROOM (in Warren)
        return locationText.substring(locationText.indexOf(':') + 2);
    }

    public boolean hasView(View view) {
        // Determines if this layout contains the given view
        int currentId = view.getId();
        return imageView.getId() == currentId || nameView.getId() == currentId || descriptionView.getId() == currentId || timeView.getId() == currentId || locationView.getId() == currentId;
    }

    public abstract Calendar getStartTime();
    public abstract Calendar getEndTime();
}
