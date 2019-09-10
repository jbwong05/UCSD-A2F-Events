package com.example.a2fevents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MonthDayNumberView extends LinearLayout {

    private TextView monthView;
    private TextView dayNumberView;

    public MonthDayNumberView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.month_day_number_layout, this);
        setupViews();
    }

    public MonthDayNumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.month_day_number_layout, this);
        setupViews();
    }

    public MonthDayNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.month_day_number_layout, this);
        setupViews();
    }

    private void setupViews() {
        monthView = findViewById(R.id.eventMonth);
        dayNumberView = findViewById(R.id.eventDayNumber);
    }

    public void displayEvent(String month, String dayNumber) {
        monthView.setText(month);
        dayNumberView.setText(dayNumber);
        resize();
    }

    private void resize() {
        int totalHeight = monthView.getHeight() + dayNumberView.getHeight();
        monthView.setWidth(totalHeight);
        dayNumberView.setWidth(totalHeight);
    }
}
