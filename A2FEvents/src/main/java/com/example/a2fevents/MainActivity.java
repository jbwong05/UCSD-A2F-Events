package com.example.a2fevents;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.chaquo.python.PyObject;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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

    private void addToCalendar(AbstractLayout layout) {
        // Add to calendar prompt
        new AddToCalendarDialogFragment(layout).show(getSupportFragmentManager(), StringConstants.CALENDAR_PROMPT_TAG);
    }

    private AbstractLayout findLayout(View view){

        LinearLayout linearLayout = findViewById(R.id.internalLinearLayout);

        boolean found = false;
        int index;
        for(index = 0; index < linearLayout.getChildCount() && !found; index++) {

            found = ((AbstractLayout)linearLayout.getChildAt(index)).hasView(view);
        }

        index--;
        return found ? (AbstractLayout)linearLayout.getChildAt(index) : null;
    }

    public class EventLayout extends AbstractLayout implements View.OnClickListener {

        private MonthDayNumberLayout monthDayNumberLayout;

        public EventLayout(Context context, MainActivity mainActivity, int numExcerpts) {
            super(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            inflater.inflate(R.layout.event_layout, this);
            addInfoViews(context, mainActivity, numExcerpts);
            setupViews();
            setupOnClickListener();
        }

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
            // Gets a reference to eac view
            super.setupViews((ProportionalImageView) findViewById(R.id.eventImage),
                    (TextView) findViewById(R.id.eventName));
            monthDayNumberLayout = findViewById(R.id.monthDayView);
        }

        @Override
        public boolean hasView(View view) {
            // Determines if the current layout has the given view
            return super.hasView(view) || monthDayNumberLayout.hasView(view);
        }

        @Override
        public void displayEvent(String imagePath, String month, String dayNumber, String name, List<PyObject> excerpts) {
            // Updates the layout with the given event information
            super.displayEvent(imagePath, month, dayNumber, name, excerpts);
            monthDayNumberLayout.displayEvent(month, dayNumber);
        }

        @Override
        public Calendar getStartTime() {
            // Retrieves a Calendar object representing the starting time
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(getStartYear(), getStartMonth(), getStartDayNumber(), getStartHour(), getStartMinute(), 0);
            return calendar;
        }

        @Override
        public Calendar getEndTime() {
            // Retrieves a Calendar object representing the ending time
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(getEndYear(), getEndMonth(), getEndDayNumber(), getEndHour(), getEndMinute(), 0);
            return calendar;
        }

        private int getStartYear() {
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            if(getEndMonth() < now.get(Calendar.MONTH)) {
                year++;
            }
            return year;
        }

        private int getEndYear() {
            // Calculates ending year based on the starting year
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
            // Calculates the ending month based on the day numbers
            if(getEndDayNumber() < getStartDayNumber()) {
                int newMonth = getStartMonth() + 1;
                return newMonth > Calendar.DECEMBER ? Calendar.JANUARY : newMonth;
            } else {
                return getStartMonth();
            }
        }

        private int getStartDayNumber() {
            String text = monthDayNumberLayout.getDayNumberText();
            return text.equals("") ? -1 : Integer.parseInt(text);
        }

        private int getEndDayNumber() {
            Calendar current = Calendar.getInstance();
            Calendar now = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH));

            // Checks if end time is next day
            if(getStartAMPM() == Calendar.PM && getEndAMPM() == Calendar.AM) {
                int dayNumber = (getStartDayNumber() + 1) % (now.getActualMaximum(Calendar.DAY_OF_MONTH) + 1);
                return dayNumber == 0 ? 1 : dayNumber;

            } else {
                return getStartDayNumber();
            }
        }

        private int getStartHour() {

            if(timeText == null) {
                return -1;
            } else {
                String text = "";
                if(timeText.contains("WHEN") || timeText.contains("When")) {

                    text = timeText.substring(timeText.indexOf(':') + 2);

                    if(text.contains(":")) {
                        text = text.substring(0, text.indexOf(':'));
                    } else {
                        text = text.substring(0, text.indexOf(' '));
                    }


                } else if(timeText.matches(".*[0-9][aApP][mM]\\s[-]\\s.*")) {

                    if(timeText.matches("^[0-9][:][0-9][0-9][aApP][mM]\\s[-]\\s.*")) {
                        text = timeText.substring(0, timeText.indexOf(':'));
                    } else {
                        text = timeText.substring(0, timeText.indexOf(' ') - 2);
                    }
                }

                return CalendarUtilities.adjustTime(Integer.parseInt(text), getStartAMPM());
            }
        }

        private int getEndHour() {
            return getEndHour(getStartHour());
        }

        private int getStartMinute() {

            if(timeText == null) {
                return -1;
            } else {
                String text = "";

                if(timeText.contains("WHEN") || timeText.contains("When")) {

                    text = timeText.substring(timeText.indexOf(':') + 1);

                    if(text.contains(":")) {
                        text = text.substring(text.indexOf(':') + 1);
                        text = text.substring(0, text.indexOf(' '));
                    } else {
                        return 0;
                    }


                } else if(timeText.matches(".*[0-9][aApP][mM]\\s[-]\\s.*")) {

                    if(timeText.matches("^[0-9][:][0-9][0-9][aApP][mM]\\s[-]\\s.*")) {
                        text = timeText.substring(timeText.indexOf(':') + 1);
                        text = text.substring(0, text.indexOf(' ') - 2);
                    } else {
                        return 0;
                    }
                }

                return Integer.parseInt(text);
            }
        }

        private int getEndMinute() {
            // Assume 1 hour
            return getStartMinute();
        }

        private int getStartAMPM() {
            // Assumes timeText format of WHEN: 6:30 PM
            if(timeText == null) {
                return -1;
            } else {
                return timeText.contains("AM") ? Calendar.AM : Calendar.PM;
            }
        }

        private int getEndAMPM() {
            // Determines the time of day for the ending time
            return getEndHour() < getStartHour() ? Calendar.AM : getStartAMPM();
        }

        protected void setupOnClickListener() {
            // Sets up the onClickListener for all Views
            super.setupOnClickListener(this);
            monthDayNumberLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Add to calendar prompt
            super.updateTexts(view);
            addToCalendar(this);
        }
    }

    public class SaveTheDateLayout extends AbstractLayout implements View.OnClickListener {

        public SaveTheDateLayout(Context context, MainActivity mainActivity, int numExcerpts) {
            super(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            inflater.inflate(R.layout.save_the_date_layout, this);
            addInfoViews(context, mainActivity, numExcerpts);
            setupViews();
            setupOnClickListener();
        }

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
            // Retrieves references for each View
            super.setupViews((ProportionalImageView) findViewById(R.id.saveTheDateImage),
                    (TextView) findViewById(R.id.saveTheDateName));
        }

        @Override
        public void displayEvent(String imagePath, String month, String dayNumber, String name, List<PyObject> excerpts) {
            // Delegates to parent method to update layout with event information
            super.displayEvent(imagePath, month, dayNumber, name, excerpts);
        }

        @Override
        public Calendar getStartTime() {
            // Retrieves a Calendar object representing the starting time
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(getStartYear(), getStartMonth(), getStartDayNumber(), getStartHour(), getStartMinute(), 0);
            return calendar;
        }

        @Override
        public Calendar getEndTime() {
            // Retrieves a Calendar object representing the ending time
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
            // Calculates the ending year
            if(getStartMonth() == Calendar.DECEMBER && getStartDayNumber() == 31 && getEndAMPM() == Calendar.AM) {
                return getStartYear() + 1;
            } else {
                return getStartYear();
            }
        }

        private int getStartMonth() {
            if(timeText == null) {
                return -1;
            } else {
                String text = timeText.substring(timeText.indexOf(':') + 2);
                text = text.substring(0, text.indexOf(' '));
                return CalendarUtilities.convertMonth(text);
            }
        }

        private int getEndMonth() {
            // Calculates the ending month based on the day numbers
            if(getEndDayNumber() < getStartDayNumber()) {
                int newMonth = getStartMonth() + 1;
                return newMonth > Calendar.DECEMBER ? Calendar.JANUARY : newMonth;

            } else {
                return getStartMonth();
            }
        }

        private int getStartDayNumber() {
            if(timeText == null) {
                return -1;
            } else {
                String text = timeText.substring(timeText.indexOf(' ') + 1);
                text = text.substring(text.indexOf(' ') + 1);
                text = text.substring(0, text.indexOf(' '));
                return Integer.parseInt(text);
            }
        }

        private int getEndDayNumber() {
            Calendar current = Calendar.getInstance();
            Calendar now = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH));

            // Calculates the ending day number based on if the ending time is during the next day
            if(getStartAMPM() == Calendar.PM && getEndAMPM() == Calendar.AM) {
                int dayNumber = (getStartDayNumber() + 1) % (now.getActualMaximum(Calendar.DAY_OF_MONTH) + 1);
                return dayNumber == 0 ? 1 : dayNumber;

            } else {
                return getStartDayNumber();
            }
        }

        private int getStartHour() {
            if(timeText == null) {
                return -1;
            } else {
                // Assumes timeText format of When: September 26 @ 6:00 PM
                String text = timeText.substring(timeText.indexOf('@') + 2);
                text = text.substring(0, text.indexOf(':'));
                return CalendarUtilities.adjustTime(Integer.parseInt(text), getStartAMPM());
            }
        }

        private int getEndHour() {
            // Calculates the ending hour
            return super.getEndHour(getStartHour());
        }

        private int getStartMinute() {
            if(timeText == null) {
                return -1;
            } else {
                // Assumes timeText format of When: September 26 @ 6:00 PM
                String text = timeText.substring(timeText.indexOf(':') + 1);
                text = text.substring(text.indexOf(':') + 1);
                text = text.substring(0, text.indexOf(' '));
                return Integer.parseInt(text);
            }
        }

        private int getEndMinute() {
            // Assumes 1 hour
            return getStartMinute();
        }

        private int getStartAMPM() {
            if(timeText == null) {
                return -1;
            } else {
                // Assumes timeText format of When: September 26 @ 6:00 PM
                return timeText.contains("AM") ? Calendar.AM : Calendar.PM;
            }
        }

        private int getEndAMPM() {
            // Determines the time of day for the ending time
            return getEndHour() < getStartHour() ? Calendar.AM : getStartAMPM();
        }

        protected void setupOnClickListener() {
            // Delegates to parent method to set up onClickListener for each View
            super.setupOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Add to calendar prompt
            super.updateTexts(view);
            addToCalendar(this);
        }
    }

    public class InfoLayout extends ConstraintLayout implements View.OnClickListener {

        private TextView textView;

        public InfoLayout(Context context) {
            super(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            inflater.inflate(R.layout.info_layout, this);
            textView = findViewById(R.id.infoText);
            textView.setId((int) Calendar.getInstance().getTimeInMillis());
            setupOnClickListener();
        }

        public InfoLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            LayoutInflater inflater = LayoutInflater.from(context);
            inflater.inflate(R.layout.info_layout, this);
            textView = findViewById(R.id.infoText);
            textView.setId((int) Calendar.getInstance().getTimeInMillis());
            setupOnClickListener();
        }

        public InfoLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            LayoutInflater inflater = LayoutInflater.from(context);
            inflater.inflate(R.layout.info_layout, this);
            textView = findViewById(R.id.infoText);
            textView.setId((int) Calendar.getInstance().getTimeInMillis());
            setupOnClickListener();
        }

        public void setText(String text) {
            textView.setText(text);
        }

        public int getTextId() {
            return textView.getId();
        }

        public String getText() {
            return textView.getText().toString();
        }

        protected void setupOnClickListener() {
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AbstractLayout abstractLayout = findLayout(view);

            if(abstractLayout != null) {
                abstractLayout.updateTexts(view);
                addToCalendar(abstractLayout);
            }
        }
    }
}
