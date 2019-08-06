package com.example.a2fevents;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.chaquo.python.*;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final static int NUM_EVENTS = 3;
    private final static String SCRIPT_NAME = "getUpcomingEvents";
    private final static String MAIN_FUNCTION = "main";
    private final static String EVENT_NAME = "eventName";
    private final static String EVENT_LOCATION = "eventLocation";
    private final static String EVENT_DATE_AND_TIME = "eventDateAndTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup collections of TextViews
        EventViewCollection[] eventViews = setupEventViewCollection();

        // Gets list upcoming events
        List<PyObject> events = getEvents();

        // Displays the upcoming events
        displayEvents(eventViews, events);
    }

    private EventViewCollection[] setupEventViewCollection() {

        // Sets up the EventViewCollection array
        EventViewCollection[] toReturn = new EventViewCollection[NUM_EVENTS];

        toReturn[0] = new EventViewCollection((TextView) findViewById(R.id.firstEventName), (TextView) findViewById(R.id.firstEventLocation), (TextView) findViewById(R.id.firstEventDateAndTime));
        toReturn[1] = new EventViewCollection((TextView) findViewById(R.id.secondEventName), (TextView) findViewById(R.id.secondEventLocation), (TextView) findViewById(R.id.secondEventDateAndTime));
        toReturn[2] = new EventViewCollection((TextView) findViewById(R.id.thirdEventName), (TextView) findViewById(R.id.thirdEventLocation), (TextView) findViewById(R.id.thirdEventDateAndTime));

        return toReturn;
    }

    private List<PyObject> getEvents() {

        // Calls the getUpcomingEvents python script to retrieve any upcoming events
        Python py = Python.getInstance();
        PyObject eventGetter = py.getModule(SCRIPT_NAME);
        PyObject eventList = eventGetter.callAttr(MAIN_FUNCTION);

        return eventList.asList();
    }

    private void displayEvents(EventViewCollection[] eventViews, List<PyObject> events) {

        // Loops through the retrieved events and updates the corresponding TextViews
        for(int i = 0; i < events.size(); i++) {
            PyObject event = events.get(i);
            String name = event.get(EVENT_NAME).toString();
            eventViews[i].displayEvent(event.get(EVENT_NAME).toString(), event.get(EVENT_LOCATION).toString(), event.get(EVENT_DATE_AND_TIME).toString());
        }
    }
}
