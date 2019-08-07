package com.example.a2fevents;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
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

    private final static int ONE_IMAGE = 1;
    private final static int TWO_IMAGES = 2;
    private final static int THREE_IMAGES = 3;
    private final static int NUM_EVENTS = 3;
    private final static String SCRIPT_NAME = "getUpcomingEvents";
    private final static String MAIN_FUNCTION = "main";
    private final static String EVENT_IMAGE_LINK = "eventImageLink";
    private final static String EVENT_NAME = "eventName";
    private final static String EVENT_LOCATION = "eventLocation";
    private final static String EVENT_DATE_AND_TIME = "eventDateAndTime";

    private static EventViewCollection[] eventViews;
    private static List<PyObject> events;
    private static float screenWidth;
    private static boolean isDownloading;
    private DownloadFileFromURL downloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.firstEventName)).setText(getResources().getString(R.string.loading));

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels / displayMetrics.density;

        // Setup collections of TextViews
        eventViews = setupEventViewCollection();

        // Gets list upcoming events
        events = getEvents();

        // Delete any previous images
        deleteImages(getFilesDir());

        // Download required images
        isDownloading = false;
        downloadImages(events);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(isDownloading) {
            downloader.cancel(true);
        }

        // Delete previously downloaded images
        deleteImages(getFilesDir());
    }

    private EventViewCollection[] setupEventViewCollection() {

        // Sets up the EventViewCollection array
        EventViewCollection[] toReturn = new EventViewCollection[NUM_EVENTS];

        toReturn[0] = new EventViewCollection((ImageView) findViewById(R.id.firstEventImage), (TextView) findViewById(R.id.firstEventName), (TextView) findViewById(R.id.firstEventLocation), (TextView) findViewById(R.id.firstEventDateAndTime));
        toReturn[1] = new EventViewCollection((ImageView) findViewById(R.id.secondEventImage), (TextView) findViewById(R.id.secondEventName), (TextView) findViewById(R.id.secondEventLocation), (TextView) findViewById(R.id.secondEventDateAndTime));
        toReturn[2] = new EventViewCollection((ImageView) findViewById(R.id.thirdEventImage), (TextView) findViewById(R.id.thirdEventName), (TextView) findViewById(R.id.thirdEventLocation), (TextView) findViewById(R.id.thirdEventDateAndTime));

        return toReturn;
    }

    private List<PyObject> getEvents() {

        // Calls the getUpcomingEvents python script to retrieve any upcoming events
        Python py = Python.getInstance();
        PyObject eventGetter = py.getModule(SCRIPT_NAME);
        PyObject eventList = eventGetter.callAttr(MAIN_FUNCTION);

        return eventList.asList();
    }

    private void deleteImages(File directory) {
        // Deletes event images in the given directory
        File currentFile;

        for(int i = 0; i < NUM_EVENTS; i++) {
            currentFile = new File(directory, "event" + i + ".jpg");

            if(currentFile.exists()) {
                currentFile.delete();
            }
        }
    }

    private void downloadImages(List<PyObject> events) {

        // Downloads images to application internal folder
        FilesToDownload files = null;
        File destination = getApplicationContext().getFilesDir();

        // Compresses links to one object
        switch(events.size()) {
            case ONE_IMAGE:
                files = new FilesToDownload(destination,
                        events.get(0).get(EVENT_IMAGE_LINK).toString());
                break;
            case TWO_IMAGES:
                files = new FilesToDownload(destination,
                        events.get(0).get(EVENT_IMAGE_LINK).toString(),
                        events.get(TWO_IMAGES - 1).get(EVENT_IMAGE_LINK).toString());
                break;
            case THREE_IMAGES:
                files = new FilesToDownload(destination,
                        events.get(0).get(EVENT_IMAGE_LINK).toString(),
                        events.get(TWO_IMAGES - 1).get(EVENT_IMAGE_LINK).toString(),
                        events.get(THREE_IMAGES - 1).get(EVENT_IMAGE_LINK).toString());
                break;
        }

        // Starts async task to download images
        if(files != null) {
            downloader = new DownloadFileFromURL();
            downloader.execute(files);
        }
    }

    private static void displayEvents(String destination, EventViewCollection[] eventViews, List<PyObject> events) {

        // Loops through the retrieved events and updates the corresponding TextViews
        for(int i = 0; i < events.size(); i++) {
            PyObject event = events.get(i);
            String imagePath = destination + "/event" + i + ".jpg";
            eventViews[i].displayEvents(screenWidth, imagePath, event.get(EVENT_NAME).toString(), event.get(EVENT_LOCATION).toString(), event.get(EVENT_DATE_AND_TIME).toString());
        }
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
                for(int i = 0; i < links.length; i++) {

                    URL url = new URL(links[i]);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    // Download the file
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    // Output stream
                    OutputStream output = new FileOutputStream(files.getDestination() + "/event" + i + ".jpg");

                    byte[] data = new byte[1024];

                    long total = 0;

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
            displayEvents(destination, eventViews, events);
            isDownloading = false;
        }
    }
}
