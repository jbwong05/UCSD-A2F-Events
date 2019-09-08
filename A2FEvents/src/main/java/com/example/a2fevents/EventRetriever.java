package com.example.a2fevents;

import android.os.AsyncTask;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
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

public class EventRetriever extends AsyncTask<Void, Integer, Boolean> {

    private static final int HAS_SAVE_THE_DATE = 1;
    private static final int DOES_NOT_HAVE_SAVE_THE_DATE = 0;
    private static final long IMAGE_SIZE = 90000;
    private File folder;
    private ViewCollection[] eventViews;
    private List<PyObject> events;

    public EventRetriever(File theFolder, ViewCollection[] theEventViews) {
        folder = theFolder;
        eventViews = theEventViews;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        // Gets list upcoming events
        events = getEvents();

        int hasSaveTheDate = DOES_NOT_HAVE_SAVE_THE_DATE;

        // Checks if save the date events found
        if(events.size() > 0) {

            PyObject eventDescription =  events.get(0).get(StringConstants.EVENT_DESCRIPTION);
            if(eventDescription != null) {
                hasSaveTheDate = eventDescription.toString().equals("") ? DOES_NOT_HAVE_SAVE_THE_DATE : HAS_SAVE_THE_DATE;
            }
        }

        // Publishes the amount of events retrieved
        publishProgress(hasSaveTheDate, events.size());

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
    protected void onProgressUpdate(Integer... numEvents) {

        // Checks if save the date views need to be removed
        if(numEvents[0] == DOES_NOT_HAVE_SAVE_THE_DATE) {

            // Removes all save the date views
            removeViews(eventViews[0].getConstraintLayout(), eventViews[0].getViewsList());

            numEvents[1]--;
        }

        // Removes extra event views
        for(int i = eventViews.length - 1; i > numEvents[1] - 1; i--) {
            removeViews(eventViews[i].getConstraintLayout(), eventViews[i].getViewsList());
        }
    }

    private void removeViews(ConstraintLayout constraintLayout, List<View> viewList) {

        // Removes vies from layout
        for(int i = 0; i < viewList.size(); i++) {

            if(viewList.get(i) != null) {
                constraintLayout.removeView(viewList.get(i));
            }
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
        PyObject eventDescription;

        // Loops through the retrieved events and updates the corresponding TextViews
        for(int i = 0; i < events.size(); i++) {

            event = events.get(i);
            eventImageName = event.get(StringConstants.EVENT_IMAGE_NAME);
            eventMonth = event.get(StringConstants.EVENT_MONTH);
            eventDayNumber = event.get(StringConstants.EVENT_DAY_NUMBER);
            eventName = event.get(StringConstants.EVENT_NAME);
            eventLocation = event.get(StringConstants.EVENT_LOCATION);
            eventDateAndTime = event.get(StringConstants.EVENT_DATE_AND_TIME);
            eventDescription = event.get(StringConstants.EVENT_DESCRIPTION);

            // null check
            if(eventImageName != null && eventMonth != null && eventDayNumber != null && eventName != null && eventLocation != null && eventDateAndTime != null && eventDescription != null) {
                eventViews[i].displayEvent(getFullImagePath(destination, eventImageName.toString()), eventMonth.toString(), eventDayNumber.toString(), eventName.toString(), eventLocation.toString(), eventDateAndTime.toString(), eventDescription.toString());
            }
        }
    }

    private static String getFullImagePath(String path, String imageName) {
        return path + "/" + StringConstants.IMAGE_PREFIX + imageName.hashCode() + StringConstants.IMAGE_EXTENSION;
    }
}