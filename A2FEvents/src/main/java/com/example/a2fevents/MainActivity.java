package com.example.a2fevents;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int THIRD = 2;
    private static final int FOURTH = 3;
    private static final int MAX_NUM_EVENTS = 4;

    private ImageDrawnReceiver imageDrawnReceiver;
    private EventRetriever retriever;
    private ViewCollection[] eventViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configures the action bar
        configureActionBar();

        // Get string constants from xml
        StringConstants.setupStringConstants(getApplicationContext());

        // Setup collection of Views
        eventViews = setupEventViewCollection();

        // Register ImageDrawnReceiver
        registerImageDrawnReceiver(eventViews);

        // Start async task to retrieve events
        retriever = new EventRetriever(getDir(StringConstants.FOLDER_NAME, Context.MODE_PRIVATE), eventViews);
        retriever.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregisters receiver
        unregisterReceiver(imageDrawnReceiver);

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

    private void registerImageDrawnReceiver(ViewCollection[] eventViews) {

        // Creates sparse array linking ImageView id to the corresponding event collection
        SparseArray<ImageViewsCollection> sparseArray = new SparseArray<>();
        sparseArray.append(R.id.firstEventImage, eventViews[SECOND].getImageViewsCollection());
        sparseArray.append(R.id.secondEventImage, eventViews[THIRD].getImageViewsCollection());
        sparseArray.append(R.id.thirdEventImage, eventViews[FOURTH].getImageViewsCollection());

        // Registers broadcast receiver
        imageDrawnReceiver = new ImageDrawnReceiver(sparseArray);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StringConstants.IMAGE_DRAWN_RECEIVER_INTENT);
        this.registerReceiver(imageDrawnReceiver, intentFilter);
    }

    private ViewCollection[] setupEventViewCollection() {

        // Sets up the EventViewCollection array
        ViewCollection[] toReturn = new ViewCollection[MAX_NUM_EVENTS];

        ConstraintLayout constraintLayout = findViewById(R.id.internalConstraintLayout);

        toReturn[FIRST] = new ViewCollection(constraintLayout, (ProportionalImageView) findViewById(R.id.saveTheDateImage),
                (TextView) findViewById(R.id.saveTheDateName),
                (TextView) findViewById(R.id.saveTheDateLocation),
                (TextView) findViewById(R.id.saveTheDateDate),
                (TextView) findViewById(R.id.saveTheDateDescription));
        toReturn[SECOND] = new ViewCollection(constraintLayout, (ProportionalImageView) findViewById(R.id.firstEventImage),
                (TextView) findViewById(R.id.firstEventMonth),
                (TextView) findViewById(R.id.firstEventDayNumber),
                (TextView) findViewById(R.id.firstEventName),
                (TextView) findViewById(R.id.firstEventLocation),
                (TextView) findViewById(R.id.firstEventDateAndTime));
        toReturn[THIRD] = new ViewCollection(constraintLayout, (ProportionalImageView) findViewById(R.id.secondEventImage),
                (TextView) findViewById(R.id.secondEventMonth),
                (TextView) findViewById(R.id.secondEventDayNumber),
                (TextView) findViewById(R.id.secondEventName),
                (TextView) findViewById(R.id.secondEventLocation),
                (TextView) findViewById(R.id.secondEventDateAndTime));
        toReturn[FOURTH] = new ViewCollection(constraintLayout, (ProportionalImageView) findViewById(R.id.thirdEventImage),
                (TextView) findViewById(R.id.thirdEventMonth),
                (TextView) findViewById(R.id.thirdEventDayNumber),
                (TextView) findViewById(R.id.thirdEventName),
                (TextView) findViewById(R.id.thirdEventLocation),
                (TextView) findViewById(R.id.thirdEventDateAndTime));

        return toReturn;
    }

    public void addToCalendar(View view) {

        // Determine which View collection the view is apart of
        int index = 0;
        boolean found = false;
        while(index < MAX_NUM_EVENTS && !found) {

            found = eventViews[index].contains(view);
            index = found ? index : index + 1;
        }

        if(index < MAX_NUM_EVENTS && !eventViews[index].isEmpty()) {
            // Prompt for calendar addition
            new AddToCalendarDialogFragment(eventViews[index]).show(getSupportFragmentManager(), StringConstants.CALENDAR_PROMPT_TAG);
        }
    }
}
