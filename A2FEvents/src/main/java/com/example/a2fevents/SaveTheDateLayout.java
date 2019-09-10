package com.example.a2fevents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

public class SaveTheDateLayout extends AbstractLayout {

    public SaveTheDateLayout(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.save_the_date_layout, this);
        setupViews();
    }

    public SaveTheDateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.save_the_date_layout, this);
        setupViews();
    }

    public SaveTheDateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.save_the_date_layout, this);
        setupViews();
    }

    private void setupViews() {
        super.setupViews((ProportionalImageView) findViewById(R.id.saveTheDateImage),
                (TextView) findViewById(R.id.saveTheDateName),
                (TextView) findViewById(R.id.saveTheDateDescription),
                (TextView) findViewById(R.id.saveTheDateTime),
                (TextView) findViewById(R.id.saveTheDateLocation));
    }

    @Override
    public void displayEvent(String imagePath, String month, String dayNumber, String name, String description, String time, String locatiaon, String dateAndTime) {
        super.displayEvent(imagePath, month, dayNumber, name, description, time, locatiaon, dateAndTime);
    }
}
