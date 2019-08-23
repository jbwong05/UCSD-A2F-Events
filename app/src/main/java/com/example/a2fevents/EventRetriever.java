package com.example.a2fevents;

import android.os.AsyncTask;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class EventRetriever extends AsyncTask<Void, Void, Boolean> {

    private static final long IMAGE_SIZE = 90000;
    private File folder;
    private EventViewCollection[] eventViews;
    private List<PyObject> events;

    public EventRetriever(File theFolder, EventViewCollection[] theEventViews) {
        folder = theFolder;
        eventViews = theEventViews;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        // Gets list upcoming events
        events = getEvents();

        // Checks if events found
        if(events.size() > 0) {

            // Download required images
            FilesToDownload files = determineImagesForDownload(events);

            // Starts async task to download images
            if(files.hasImagesToDownload() && folder.getFreeSpace() > (files.getNumImages() * IMAGE_SIZE)) {
                downloadImages(files);
            }

            return true;

        } else {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean eventsFound) {

        if(eventsFound) {
            // Display events after images downloaded
            displayEvents(events, folder.getAbsolutePath());

        } else {
            // Otherwise display no events found message
            eventViews[0].setStatus(StringConstants.NO_EVENTS);
        }
    }

    private List<PyObject> getEvents() {

        // Calls the getUpcomingEvents python script to retrieve any upcoming events
        Python py = Python.getInstance();
        PyObject eventGetter = py.getModule(StringConstants.SCRIPT_NAME);
        PyObject eventList = eventGetter.callAttr(StringConstants.MAIN_FUNCTION);

        return eventList.asList();
    }

    private FilesToDownload determineImagesForDownload(List<PyObject> events) {

        // Downloads images to application internal folder
        FilesToDownload files = new FilesToDownload(folder.getAbsolutePath());
        File currentFile;
        PyObject pythonLink;
        PyObject pythonImageName;
        Image currentImage;

        // Loop through each event link
        for(int i = 0; i < events.size(); i++) {

            pythonLink = events.get(i).get(StringConstants.EVENT_IMAGE_LINK);
            pythonImageName = events.get(i).get(StringConstants.EVENT_IMAGE_NAME);

            // Null check
            if(pythonLink != null && pythonImageName != null) {

                // Determine which images have already been downloaded
                currentImage = new Image(pythonImageName.toString(), pythonLink.toString());
                currentFile = new File(getFullImagePath(folder.getAbsolutePath(), currentImage.getName()));

                // Checks if file already exists or is already scheduled for download
                if(!currentFile.exists() && !files.hasImage(currentImage)) {
                    files.addImage(currentImage);
                }
            }
        }

        return files;
    }

    private void downloadImages(FilesToDownload files) {
        // Downloads images in a background thread
        int count;
        Image[] images = files.getImages();

        try {

            // Loops through all links
            for(int i = 0; i < files.getNumImages(); i++) {

                // Connect to the url
                URL url = new URL(images[i].getLink());
                URLConnection connection = url.openConnection();
                connection.connect();

                // Download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                OutputStream output = new FileOutputStream(getFullImagePath(files.getDestination(), images[i].getName()));

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
    }

    private void displayEvents(List<PyObject> events, String destination) {
        PyObject event;
        PyObject eventImageName;
        PyObject eventMonth;
        PyObject eventDayNumber;
        PyObject eventName;
        PyObject eventLocation;
        PyObject eventDateAndTime;

        // Loops through the retrieved events and updates the corresponding TextViews
        for(int i = 0; i < events.size(); i++) {

            event = events.get(i);
            eventImageName = event.get(StringConstants.EVENT_IMAGE_NAME);
            eventMonth = event.get(StringConstants.EVENT_MONTH);
            eventDayNumber = event.get(StringConstants.EVENT_DAY_NUMBER);
            eventName = event.get(StringConstants.EVENT_NAME);
            eventLocation = event.get(StringConstants.EVENT_LOCATION);
            eventDateAndTime = event.get(StringConstants.EVENT_DATE_AND_TIME);

            // null check
            if(eventImageName != null && eventMonth != null && eventDayNumber != null && eventName != null && eventLocation != null && eventDateAndTime != null) {
                eventViews[i].displayEvents(getFullImagePath(destination, eventImageName.toString()), eventMonth.toString(), eventDayNumber.toString(), eventName.toString(), eventLocation.toString(), eventDateAndTime.toString());
            }
        }
    }

    private static String getFullImagePath(String path, String imageName) {
        return path + "/" + StringConstants.IMAGE_PREFIX + imageName.hashCode() + StringConstants.IMAGE_EXTENSION;
    }
}