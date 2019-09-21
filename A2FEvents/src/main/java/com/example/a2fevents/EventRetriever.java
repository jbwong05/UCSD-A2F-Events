package com.example.a2fevents;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventRetriever extends AsyncTask<Object, Object, Object[]> {

    private static final int HAS_SAVE_THE_DATE = 1;
    private static final int DOES_NOT_HAVE_SAVE_THE_DATE = 0;
    private static final int EVENT = 2;
    private static final int SAVE_THE_DATE = 3;
    private static final int ADD_VIEW = 4;
    private static final int REMOVE_STATUS_VIEW = 5;
    private static final long IMAGE_SIZE = 90000;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private int hasSaveTheDate;
    private File folder;
    private List<PyObject> events;

    public EventRetriever(File theFolder) {
        folder = theFolder;
        hasSaveTheDate = DOES_NOT_HAVE_SAVE_THE_DATE;
    }

    /*
     * Expected parameter array:
     * params[0]: Reference to the main activity
     * params[1]: The context
     * params[2]: The outer linear layout container for each dynamically added event layout
     */
    @Override
    protected Object[] doInBackground(Object... params) {

        // Get parameters
        MainActivity mainActivity = (MainActivity) params[0];
        Context context = (Context) params[1];
        LinearLayout linearLayout = (LinearLayout) params[2];

        // Gets list upcoming events
        events = getEvents();

        // Checks if save the date events found
        if(events.size() > 0) {

            PyObject eventName =  events.get(0).get(StringConstants.EVENT_NAME);
            if(eventName != null) {
                hasSaveTheDate = eventName.toString().equals(StringConstants.SAVE_THE_DATE_NAME) ?  HAS_SAVE_THE_DATE : DOES_NOT_HAVE_SAVE_THE_DATE;
            }
        }

        // Array of data to be sent to onPostExecute
        Object[] toReturn = new Object[3];

        // Checks if events found
        if(events.size() > 0) {

            // Download required images
            FilesToDownload files = determineImagesForDownload(events);

            // Downloads images
            if(files.hasImagesToDownload() && folder.getFreeSpace() > (files.getNumImages() * IMAGE_SIZE)) {
                downloadImages(files);
            }

            // Removes the status view
            publishProgress(REMOVE_STATUS_VIEW, mainActivity, context, linearLayout, null, null);

            int numExcerpts = getNumExcerpts(events.get(0));

            int numEvents = events.size();

            // Adds SaveTheDate View if necessary
            if(hasSaveTheDate == HAS_SAVE_THE_DATE) {
                publishProgress(ADD_VIEW, mainActivity, context, linearLayout, SAVE_THE_DATE, numExcerpts);
            }

            // Adds the remaining events
            for(int i = 1; i < numEvents; i++) {
                numExcerpts = getNumExcerpts(events.get(i));
                publishProgress(ADD_VIEW, mainActivity, context, linearLayout, EVENT, numExcerpts);
            }

            toReturn[0] = true;
            toReturn[1] = linearLayout;
            return toReturn;

        } else {
            toReturn[0] = false;
            return toReturn;
        }
    }

    /*
     * Expected parameter array
     * params[0]: Action to be performed; either ADD_VIEW or REMOVE_STATUS_VIEW
     * params[1]: Reference to the main activity
     * params[2]: The context
     * params[3]: The internal linear layout container which holds the dynamically added event layouts
     * params[4]: The type of layout to add; either EVENT or SAVE_THE_DATE
     */
    @Override
    protected void onProgressUpdate(Object... params) {

        // Extracts parameters
        MainActivity mainActivity = (MainActivity) params[1];
        Context context = (Context) params[2];
        LinearLayout linearLayout = (LinearLayout) params[3];

        // Determines action
        if((Integer) params[0] == ADD_VIEW) {
            int numEvents = (Integer) params[5];

            // Checks if event layout is to be added
            if((Integer) params[4] == EVENT) {
                linearLayout.addView(mainActivity.new EventLayout(context, mainActivity, numEvents));

                // Otherwise add save the date layout
            } else if((Integer) params[4] == SAVE_THE_DATE) {
                linearLayout.addView(mainActivity.new SaveTheDateLayout(context, mainActivity, numEvents));
            }

            // Removes the status TextView
        } else if((Integer) params[0] == REMOVE_STATUS_VIEW) {
            linearLayout.removeAllViews();
        }
    }

    /*
     * Expected parameter array:
     * results[0]: Boolean representing if events were found
     * results[1]: internal linear layout container
     */
    @Override
    protected void onPostExecute(Object[] results) {

        if((Boolean) results[0]) {

            // Display events after images downloaded
            displayEvents(events, folder.getAbsolutePath(), (LinearLayout) results[1]);

        } else {
            // Otherwise display no events found message
            setStatus((LinearLayout) results[1]);
        }
    }

    private List<PyObject> getEvents() {

        // Calls the getUpcomingEvents python script to retrieve any upcoming events
        Python py = Python.getInstance();
        PyObject eventGetter = py.getModule(StringConstants.SCRIPT_NAME);
        PyObject eventList = eventGetter.callAttr(StringConstants.MAIN_FUNCTION);

        return eventList.asList();
    }

    private int getNumExcerpts(PyObject event) {
        // Retrieves the number of excerpts containing info for the event
        int numExcerpts = -1;
        if(event != null) {
            PyObject excerptCount = event.get(StringConstants.EVENT_NUM_EXCERPTS);

            if(excerptCount != null) {
                numExcerpts = excerptCount.toInt();
            }
        }

        return numExcerpts;
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
            if(pythonLink != null && pythonImageName != null && !pythonLink.toString().equals("") && !pythonImageName.toString().equals("")) {

                // Determine which images have already been downloaded
                currentImage = new Image(pythonImageName.toString(), pythonLink.toString());
                currentFile = new File(Image.getFullImagePath(folder.getAbsolutePath(), currentImage.getName()));

                // Checks if file already exists or is already scheduled for download
                if(!currentFile.exists() && !files.hasImage(currentImage)) {
                    files.addImage(currentImage);
                }
            }
        }

        return files;
    }

    private void downloadImages(FilesToDownload filesToDownload) {
        // Uses multiple threads to download each image
        ExecutorService taskExecutor = Executors.newFixedThreadPool(NUMBER_OF_CORES);

        for(int i = 0; i < filesToDownload.getNumImages(); i++) {
            taskExecutor.execute(new ImageDownloader(filesToDownload.getImages().get(i), filesToDownload.getDestination()));
        }

        taskExecutor.shutdown();

        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void displayEvents(List<PyObject> events, String destination, LinearLayout linearLayout) {
        PyObject event;
        PyObject eventImageName;
        PyObject eventMonth;
        PyObject eventDayNumber;
        PyObject eventName;
        PyObject eventExcerpts;

        // Loops through the added events and updates each event layout
        for(int i = 0; i < linearLayout.getChildCount(); i++) {

            event = events.get(i);
            eventImageName = event.get(StringConstants.EVENT_IMAGE_NAME);
            eventMonth = event.get(StringConstants.EVENT_MONTH);
            eventDayNumber = event.get(StringConstants.EVENT_DAY_NUMBER);
            eventName = event.get(StringConstants.EVENT_NAME);
            eventExcerpts = event.get(StringConstants.EVENT_EXCERPTS);

            // null check
            if(eventImageName != null && eventMonth != null && eventDayNumber != null && eventName != null && eventExcerpts != null) {
                String path = eventImageName.toString().equals("") ? "" : Image.getFullImagePath(destination, eventImageName.toString());
                ((AbstractLayout) linearLayout.getChildAt(i)).displayEvent(path, eventMonth.toString(), eventDayNumber.toString(), eventName.toString(), eventExcerpts.asList());
            }
        }
    }

    private void setStatus(LinearLayout linearLayout) {
        // Changes the status of the initial status TextView
        ((TextView)linearLayout.getChildAt(0)).setText(StringConstants.NO_EVENTS);
    }
}