package com.example.a2fevents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class MonthDayNumberLayout extends LinearLayout {

    private final static double MONTH_HEIGHT_TO_WIDTH_RATIO = 0.35;

    private TextView monthView;
    private TextView dayNumberView;
    private String monthText;
    private String dayNumberText;

    public MonthDayNumberLayout(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.month_day_number_layout, this);
        setupViews();
        setupViewLayoutListener();
    }

    public MonthDayNumberLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.month_day_number_layout, this);
        setupViews();
        setupViewLayoutListener();
    }

    public MonthDayNumberLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.month_day_number_layout, this);
        setupViews();
        setupViewLayoutListener();
    }

    private void setupViews() {
        monthView = findViewById(R.id.eventMonth);
        dayNumberView = findViewById(R.id.eventDayNumber);
    }

    private void setupViewLayoutListener() {

        // Attach an observer to be notified when the month view is laid out
        final ViewTreeObserver observer = monthView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Adjusts the width based on the height
                    // Assumes text has been set before month view has been laid out
                    monthView.setWidth((int)(monthView.getHeight() / MONTH_HEIGHT_TO_WIDTH_RATIO));

                    // Removes the observer after view has already been resized
                    ViewTreeObserver obs = monthView.getViewTreeObserver();
                    obs.removeOnGlobalLayoutListener(this);
                }
            });
    }

    public void displayEvent(String month, String dayNumber) {
        monthView.setText(month);
        dayNumberView.setText(dayNumber);
        monthText = month;
        dayNumberText = dayNumber;
    }

    public boolean hasView(View view) {
        return monthView.getId() == view.getId() || dayNumberView.getId() == view.getId();
    }

    public String getMonthText() {
        return monthText;
    }

    public String getDayNumberText() {
        return dayNumberText;
    }
}
