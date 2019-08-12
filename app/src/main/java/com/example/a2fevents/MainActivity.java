package com.example.a2fevents;

import android.content.Context;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.chaquo.python.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int THIRD = 2;
    private static final int MAX_NUM_EVENTS = 3;
    private static final long IMAGE_SIZE = 90000;
    private static String EVENT_IMAGE_LINK;
    private static String EVENT_MONTH;
    private static String EVENT_DAY_NUMBER;
    private static String EVENT_NAME;
    private static String EVENT_LOCATION;
    private static String EVENT_DATE_AND_TIME;
    private static String IMAGE_PREFIX;
    private static String IMAGE_EXTENSION;

    private static EventViewCollection[] eventViews;
    private static List<PyObject> events;
    private ImageDrawnReceiver imageDrawnReceiver;
    private static boolean isDownloading;
    private DownloadFileFromURL downloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configures the action bar
        configureActionBar();

        // Get string constants from xml
        getStrings();

        // Displays Retrieving status
        setStatus(getResources().getString(R.string.retrieving));

        // Setup collection of Views
        eventViews = setupEventViewCollection();

        // Register ImageDrawnReceiver
        registerImageDrawnReceiver();

        // Gets list upcoming events
        events = getEvents();

        // Checks if events found
        if(events.size() > 0) {

            // Download required images
            isDownloading = false;
            downloadImages(events);

        } else {
            // Set no events found status
            setStatus(getResources().getString(R.string.no_events));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregisters receiver
        unregisterReceiver(imageDrawnReceiver);

        // Cancels downloader if currently downloading
        if(isDownloading) {
            downloader.cancel(true);
        }
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

    private void getStrings() {
        // Retrieves String constants from xml
        EVENT_IMAGE_LINK = getResources().getString(R.string.event_image_link);
        EVENT_MONTH = getResources().getString(R.string.event_month);
        EVENT_DAY_NUMBER = getResources().getString(R.string.event_day_number);
        EVENT_NAME = getResources().getString(R.string.event_name);
        EVENT_LOCATION = getResources().getString(R.string.event_location);
        EVENT_DATE_AND_TIME = getResources().getString(R.string.event_date_and_time);
        IMAGE_PREFIX = getResources().getString(R.string.image_prefix);
        IMAGE_EXTENSION = getResources().getString(R.string.image_extension);
    }

    private void registerImageDrawnReceiver() {

        // Creates sparse array linking ImageView id to the corresponding event collection
        SparseArray<ImageViewsCollection> sparseArray = new SparseArray<>();
        sparseArray.append(R.id.firstEventImage, eventViews[FIRST].getImageViewsCollection());
        sparseArray.append(R.id.secondEventImage, eventViews[SECOND].getImageViewsCollection());
        sparseArray.append(R.id.thirdEventImage, eventViews[THIRD].getImageViewsCollection());

        // Registers broadcast receiver
        imageDrawnReceiver = new ImageDrawnReceiver(sparseArray);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getResources().getString(R.string.image_drawn_receiver_intent));
        this.registerReceiver(imageDrawnReceiver, intentFilter);
    }

    private void setStatus(String status) {
        // Displays Retrieving status
        ((TextView) findViewById(R.id.firstEventName)).setText(status);
    }

    private EventViewCollection[] setupEventViewCollection() {

        // Sets up the EventViewCollection array
        EventViewCollection[] toReturn = new EventViewCollection[MAX_NUM_EVENTS];

        toReturn[FIRST] = new EventViewCollection((ProportionalImageView) findViewById(R.id.firstEventImage),
                (TextView) findViewById(R.id.firstEventMonth),
                (TextView) findViewById(R.id.firstEventDayNumber),
                (TextView) findViewById(R.id.firstEventName),
                (TextView) findViewById(R.id.firstEventLocation),
                (TextView) findViewById(R.id.firstEventDateAndTime));
        toReturn[SECOND] = new EventViewCollection((ProportionalImageView) findViewById(R.id.secondEventImage),
                (TextView) findViewById(R.id.secondEventMonth),
                (TextView) findViewById(R.id.secondEventDayNumber),
                (TextView) findViewById(R.id.secondEventName),
                (TextView) findViewById(R.id.secondEventLocation),
                (TextView) findViewById(R.id.secondEventDateAndTime));
        toReturn[THIRD] = new EventViewCollection((ProportionalImageView) findViewById(R.id.thirdEventImage),
                (TextView) findViewById(R.id.thirdEventMonth),
                (TextView) findViewById(R.id.thirdEventDayNumber),
                (TextView) findViewById(R.id.thirdEventName),
                (TextView) findViewById(R.id.thirdEventLocation),
                (TextView) findViewById(R.id.thirdEventDateAndTime));

        return toReturn;
    }

    private List<PyObject> getEvents() {

        // Calls the getUpcomingEvents python script to retrieve any upcoming events
        Python py = Python.getInstance();
        PyObject eventGetter = py.getModule(getResources().getString(R.string.script_name));
        PyObject eventList = eventGetter.callAttr(getResources().getString(R.string.main_function));

        return eventList.asList();
    }

    private void downloadImages(List<PyObject> events) {

        // Downloads images to application internal folder
        File folder = getApplicationContext().getDir(getResources().getString(R.string.folder_name), Context.MODE_PRIVATE);
        FilesToDownload files = new FilesToDownload(folder.getAbsolutePath());
        File currentFile;
        String currentLink;
        PyObject pythonLink;

        // Loop through each event link
        for(int i = 0; i < events.size(); i++) {

            pythonLink = events.get(i).get(EVENT_IMAGE_LINK);

            // Null check
            if(pythonLink != null) {

                // Determine which images have already been downloaded
                currentLink = pythonLink.toString();
                currentFile = new File(getFullImagePath(folder.getAbsolutePath(), currentLink));

                // Checks if file already exists or is already scheduled for download
                if(!currentFile.exists() && !files.hasLink(currentLink)) {
                    files.addFile(currentLink);
                }

            }
        }

        // Starts async task to download images
        if(files.hasFilesToDownload() && folder.getFreeSpace() > (files.getNumLinks() * IMAGE_SIZE)) {
            downloader = new DownloadFileFromURL();
            downloader.execute(files);

            // Otherwise display events using already downloaded images
        } else {
            displayEvents(folder.getAbsolutePath());
        }
    }

    private static void displayEvents(String destination) {
        PyObject event;
        PyObject eventImageLink;
        PyObject eventMonth;
        PyObject eventDayNumber;
        PyObject eventName;
        PyObject eventLocation;
        PyObject eventDateAndTime;

        // Loops through the retrieved events and updates the corresponding TextViews
        for(int i = 0; i < events.size(); i++) {

            event = events.get(i);
            eventImageLink = event.get(EVENT_IMAGE_LINK);
            eventMonth = event.get(EVENT_MONTH);
            eventDayNumber = event.get(EVENT_DAY_NUMBER);
            eventName = event.get(EVENT_NAME);
            eventLocation = event.get(EVENT_LOCATION);
            eventDateAndTime = event.get(EVENT_DATE_AND_TIME);

            // null check
            if(eventImageLink != null && eventMonth != null && eventDayNumber != null && eventName != null && eventLocation != null && eventDateAndTime != null) {
                eventViews[i].displayEvents(getFullImagePath(destination, eventImageLink.toString()), eventMonth.toString(), eventDayNumber.toString(), eventName.toString(), eventLocation.toString(), eventDateAndTime.toString());
            }
        }
    }

    private static String getFullImagePath(String path, String link) {
        return path + "/" + IMAGE_PREFIX + link.hashCode() + IMAGE_EXTENSION;
    }

    // Inner AsyncTask that downloads images in the background
    private static class DownloadFileFromURL extends AsyncTask<FilesToDownload, Void, String> {

        @Override
        protected void onPreExecute() {
            isDownloading = true;
        }

        @Override
        protected String doInBackground(FilesToDownload... filesArr) {

            // Downloads images in a background thread
            int count;
            FilesToDownload files = filesArr[0];
            String[] links = files.getLinks();

            try {

                // Loops through all links
                for(int i = 0; i < files.getNumLinks(); i++) {

                    // Connect to the url
                    URL url = new URL(links[i]);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    // Download the file
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    // Output stream
                    OutputStream output = new FileOutputStream(getFullImagePath(files.getDestination(), links[i]));

                    byte[] data = new byte[1024];

                    long total = 0;

                    // While there are still bytes to write
                    while ((count = input.read(data)) != -1) {
                        total += count;

                        // Writing data to file
                        output.write(data, 0, count);
                    }

                    // Flushing output
                    output.flush();

                    // Closing streams
                    output.close();
                    input.close();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return files.getDestination();
        }

        @Override
        protected void onPostExecute(String destination) {
            // Display events after images downloaded
            displayEvents(destination);
            isDownloading = false;
        }
    }
}
