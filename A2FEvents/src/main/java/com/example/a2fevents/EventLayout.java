package com.example.a2fevents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

public class EventLayout extends AbstractLayout {

    private MonthDayNumberView monthDayNumberView;
    private TextView dateAndTimeView;

    public EventLayout(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.event_layout, this);
        setupViews();
    }

    public EventLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.event_layout, this);
        setupViews();
    }

    public EventLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.event_layout, this);
        setupViews();
    }

    private void setupViews() {
        super.setupViews((ProportionalImageView) findViewById(R.id.eventImage),
                (TextView) findViewById(R.id.eventName),
                (TextView) findViewById(R.id.eventDescription),
                (TextView) findViewById(R.id.eventTime),
                (TextView) findViewById(R.id.eventLocation));
        monthDayNumberView = findViewById(R.id.monthDayView);
        dateAndTimeView = findViewById(R.id.eventDateAndTime);
    }

    @Override
    public void displayEvent(String imagePath, String month, String dayNumber, String name, String description, String time, String location, String dateAndTime) {
        super.displayEvent(imagePath, month, dayNumber, name, description, time, location, dateAndTime);
        monthDayNumberView.displayEvent(month, dayNumber);
        dateAndTimeView.setText(dateAndTime);
    }
}
