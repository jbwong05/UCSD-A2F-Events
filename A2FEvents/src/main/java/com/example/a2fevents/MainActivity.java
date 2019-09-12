package com.example.a2fevents;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private EventRetriever retriever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configures the action bar
        configureActionBar();

        // Get string constants from xml
        StringConstants.setupStringConstants(getApplicationContext());

        // Start async task to retrieve events
        retriever = new EventRetriever(getDir(StringConstants.FOLDER_NAME, Context.MODE_PRIVATE));
        retriever.execute(this, getApplicationContext(), findViewById(R.id.internalLinearLayout));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cancels downloader if currently downloading
        retriever.cancel(true);
    }

    private void configureActionBar() {
        // Apply action bar layout
        ActionBar actionBar = getSupportActionBar();

        // null Check
        if(actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_layout);
        }
    }

    public void addToCalendar(AbstractLayout layout) {
        // Add to calendar prompt
        new AddToCalendarDialogFragment(layout).show(getSupportFragmentManager(), StringConstants.CALENDAR_PROMPT_TAG);
    }

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
        public boolean hasView(View view) {
            return super.hasView(view) || monthDayNumberLayout.hasView(view) || dateAndTimeView.getId() == view.getId();
        }

        @Override
        public void displayEvent(String imagePath, String month, String dayNumber, String name, String description, String time, String location, String dateAndTime) {
            super.displayEvent(imagePath, month, dayNumber, name, description, time, location, dateAndTime);
            monthDayNumberLayout.displayEvent(month, dayNumber);
            dateAndTimeView.setText(dateAndTime);
            dateAndTimeText = dateAndTime;
        }

        @Override
        public Calendar getStartTime() {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(getStartYear(), getStartMonth(), getStartDayNumber(), getStartHour(), getStartMinute(), 0);
            return calendar;
        }

        @Override
        public Calendar getEndTime() {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(getEndYear(), getEndMonth(), getEndDayNumber(), getEndHour(), getEndMinute(), 0);
            return calendar;
        }

        private int getStartYear() {
            // Assumes dateAndTimeText format of SEP 13, 2019 6:30 PM – 11:00 PM
            // Parses the specific year
            String text = dateAndTimeText.substring(dateAndTimeText.indexOf(',') + 2);
            text = text.substring(0, text.indexOf(' '));
            return Integer.parseInt(text);
        }

        private int getEndYear() {
            if(getStartMonth() == Calendar.DECEMBER && getStartDayNumber() == 31 && getEndAMPM() == Calendar.AM) {
                return getStartYear() + 1;
            } else {
                return getStartYear();
            }
        }

        private int getStartMonth() {
            // Assumes format of Sep or September
            return CalendarUtilities.convertMonth(monthDayNumberLayout.getMonthText());
        }

        private int getEndMonth() {
            if(getEndDayNumber() < getStartDayNumber()) {
                int newMonth = getStartMonth() + 1;
                return newMonth > Calendar.DECEMBER ? Calendar.JANUARY : newMonth;
            } else {
                return getStartMonth();
            }
        }

        private int getStartDayNumber() {
            return Integer.parseInt(monthDayNumberLayout.getDayNumberText());
        }

        private int getEndDayNumber() {
            Calendar current = Calendar.getInstance();
            Calendar now = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH));

            if(getStartAMPM() == Calendar.PM && getEndAMPM() == Calendar.AM) {
                int dayNumber = (getStartDayNumber() + 1) % (now.getActualMaximum(Calendar.DAY_OF_MONTH) + 1);
                return dayNumber == 0 ? 1 : dayNumber;
            } else {
                return getStartDayNumber();
            }
        }

        private int getStartHour() {
            // Assumes timeText format of WHEN: 6:30 PM
            String text = timeText.substring(timeText.indexOf(':') + 2);
            text = text.substring(0, text.indexOf(':'));
            int toReturn = CalendarUtilities.adjustTime(Integer.parseInt(text), getStartAMPM());
            return toReturn;
        }

        private int getEndHour() {
            // Assumes dateAndTimeText format of SEP 13, 2019 6:30 PM – 11:00 PM
            String text = dateAndTimeText.substring(dateAndTimeText.indexOf('–') + 2);
            text = text.substring(0, text.indexOf(':'));
            int toReturn = CalendarUtilities.adjustTime(Integer.parseInt(text), getEndAMPM());
            return toReturn;
        }

        private int getStartMinute() {
            // Assumes timeText format of WHEN: 6:30 PM
            String text = timeText.substring(timeText.indexOf(':') + 1);
            text = text.substring(text.indexOf(':') + 1);
            text = text.substring(0, text.indexOf(' '));
            return Integer.parseInt(text);
        }

        private int getEndMinute() {
            // Assumes dateAndTimeText format of SEP 13, 2019 6:30 PM – 11:00 PM
            String text = dateAndTimeText.substring(dateAndTimeText.indexOf('–'));
            text = text.substring(text.indexOf(':') + 1);
            text = text.substring(0, text.indexOf(' '));
            return Integer.parseInt(text);
        }

        private int getStartAMPM() {
            // Assumes timeText format of WHEN: 6:30 PM
            return timeText.contains("AM") ? Calendar.AM : Calendar.PM;
        }

        private int getEndAMPM() {
            // Assumes dateAndTimeText format of SEP 13, 2019 6:30 PM – 11:00 PM
            String text = dateAndTimeText.substring(dateAndTimeText.indexOf('–'));
            return text.contains("AM") ? Calendar.AM : Calendar.PM;
        }

        protected void setupOnClickListener() {
            super.setupOnClickListener(this);
            monthDayNumberLayout.setOnClickListener(this);
            dateAndTimeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            addToCalendar(this);
        }
    }

    public class SaveTheDateLayout extends AbstractLayout implements View.OnClickListener {

        public SaveTheDateLayout(Context context) {
            super(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            inflater.inflate(R.layout.save_the_date_layout, this);
            setupViews();
            setupOnClickListener();
        }

        public SaveTheDateLayout(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
            LayoutInflater inflater = LayoutInflater.from(context);
            inflater.inflate(R.layout.save_the_date_layout, this);
            setupViews();
            setupOnClickListener();
        }

        public SaveTheDateLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            LayoutInflater inflater = LayoutInflater.from(context);
            inflater.inflate(R.layout.save_the_date_layout, this);
            setupViews();
            setupOnClickListener();
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

        @Override
        public Calendar getStartTime() {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(getStartYear(), getStartMonth(), getStartDayNumber(), getStartHour(), getStartMinute(), 0);
            return calendar;
        }

        @Override
        public Calendar getEndTime() {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(getEndYear(), getEndMonth(), getEndDayNumber(), getEndHour(), getEndMinute(), 0);
            return calendar;
        }

        private int getStartYear() {
            // Assumes this year
            Calendar now = Calendar.getInstance();
            return now.get(Calendar.YEAR);
        }

        private int getEndYear() {
            if(getStartMonth() == Calendar.DECEMBER && getStartDayNumber() == 31 && getEndAMPM() == Calendar.AM) {
                return getStartYear() + 1;
            } else {
                return getStartYear();
            }
        }

        private int getStartMonth() {
            // Assumes timeText format of When: September 26 @ 6:00 PM
            String text = timeText.substring(timeText.indexOf(':') + 2);
            text = text.substring(0, text.indexOf(' '));
            return CalendarUtilities.convertMonth(text);
        }

        private int getEndMonth() {
            if(getEndDayNumber() < getStartDayNumber()) {
                int newMonth = getStartMonth() + 1;
                return newMonth > Calendar.DECEMBER ? Calendar.JANUARY : newMonth;
            } else {
                return getStartMonth();
            }
        }

        private int getStartDayNumber() {
            // Assumes timeText format of When: September 26 @ 6:00 PM
            String text = timeText.substring(timeText.indexOf(' ') + 1);
            text = text.substring(text.indexOf(' ') + 1);
            text = text.substring(0, text.indexOf(' '));
            return Integer.parseInt(text);
        }

        private int getEndDayNumber() {
            Calendar current = Calendar.getInstance();
            Calendar now = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH));

            if(getStartAMPM() == Calendar.PM && getEndAMPM() == Calendar.AM) {
                int dayNumber = (getStartDayNumber() + 1) % (now.getActualMaximum(Calendar.DAY_OF_MONTH) + 1);
                return dayNumber == 0 ? 1 : dayNumber;
            } else {
                return getStartDayNumber();
            }
        }

        private int getStartHour() {
            // Assumes timeText format of When: September 26 @ 6:00 PM
            String text = timeText.substring(timeText.indexOf('@') + 2);
            text = text.substring(0, text.indexOf(':'));
            return CalendarUtilities.adjustTime(Integer.parseInt(text), getStartAMPM());
        }

        private int getEndHour() {
            int endHour = (getStartHour() + 1) % 25;
            return endHour == 0 ? 1 : endHour;
        }

        private int getStartMinute() {
            // Assumes timeText format of When: September 26 @ 6:00 PM
            String text = timeText.substring(timeText.indexOf(':') + 1);
            text = text.substring(text.indexOf(':') + 1);
            text = text.substring(0, text.indexOf(' '));
            return Integer.parseInt(text);
        }

        private int getEndMinute() {
            // Assumes 1 hour
            return getStartMinute();
        }

        private int getStartAMPM() {
            // Assumes timeText format of When: September 26 @ 6:00 PM
            return timeText.contains("AM") ? Calendar.AM : Calendar.PM;
        }

        private int getEndAMPM() {
            return getEndHour() < getStartHour() ? Calendar.AM : getStartAMPM();
        }

        protected void setupOnClickListener() {
            super.setupOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Add to calendar prompt
            addToCalendar(this);
        }
    }
}
