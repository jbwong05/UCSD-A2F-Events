package com.example.a2fevents;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class EventLayout extends AbstractLayout implements View.OnClickListener {

    private MonthDayNumberLayout monthDayNumberLayout;
    private TextView dateAndTimeView;
    private String dateAndTimeText;

    public EventLayout(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.event_layout, this);
        setupViews();
        setupOnClickListener();
    }

    public EventLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.event_layout, this);
        setupViews();
        setupOnClickListener();
    }

    public EventLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.event_layout, this);
        setupViews();
        setupOnClickListener();
    }

    private void setupViews() {
        super.setupViews((ProportionalImageView) findViewById(R.id.eventImage),
                (TextView) findViewById(R.id.eventName),
                (TextView) findViewById(R.id.eventDescription),
                (TextView) findViewById(R.id.eventTime),
                (TextView) findViewById(R.id.eventLocation));
        monthDayNumberLayout = findViewById(R.id.monthDayView);
        dateAndTimeView = findViewById(R.id.eventDateAndTime);
    }

    @Override
    public void displayEvent(String imagePath, String month, String dayNumber, String name, String description, String time, String location, String dateAndTime) {
        super.displayEvent(imagePath, month, dayNumber, name, description, time, location, dateAndTime);
        monthDayNumberLayout.displayEvent(month, dayNumber);
        dateAndTimeView.setText(dateAndTime);
        dateAndTimeText = dateAndTime;
    }

    protected void setupOnClickListener() {
        super.setupOnClickListener(this);
        monthDayNumberLayout.setOnClickListener(this);
        dateAndTimeView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("click", view.getId() + "");
    }
}
