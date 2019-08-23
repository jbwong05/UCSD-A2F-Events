package com.example.a2fevents;

import android.content.Context;
import android.content.res.Resources;

public class StringConstants {
    public static String EVENT_IMAGE_LINK;
    public static String EVENT_IMAGE_NAME;
    public static String EVENT_MONTH;
    public static String EVENT_DAY_NUMBER;
    public static String EVENT_NAME;
    public static String EVENT_LOCATION;
    public static String EVENT_DATE_AND_TIME;
    public static String IMAGE_PREFIX;
    public static String IMAGE_EXTENSION;
    public static String IMAGE_DRAWN_RECEIVER_INTENT;
    public static String FOLDER_NAME;
    public static String NO_EVENTS;
    public static String SCRIPT_NAME;
    public static String MAIN_FUNCTION;

    public static void setupStringConstants(Context context) {
        // Retrieves String constants from xml
        Resources resources = context.getResources();

        EVENT_IMAGE_LINK = resources.getString(R.string.event_image_link);
        EVENT_IMAGE_NAME = resources.getString(R.string.event_image_name);
        EVENT_MONTH = resources.getString(R.string.event_month);
        EVENT_DAY_NUMBER = resources.getString(R.string.event_day_number);
        EVENT_NAME = resources.getString(R.string.event_name);
        EVENT_LOCATION = resources.getString(R.string.event_location);
        EVENT_DATE_AND_TIME = resources.getString(R.string.event_date_and_time);
        IMAGE_PREFIX = resources.getString(R.string.image_prefix);
        IMAGE_EXTENSION = resources.getString(R.string.image_extension);
        IMAGE_DRAWN_RECEIVER_INTENT = resources.getString(R.string.image_drawn_receiver_intent);
        FOLDER_NAME = resources.getString(R.string.folder_name);
        NO_EVENTS = resources.getString(R.string.no_events);
        SCRIPT_NAME = resources.getString(R.string.script_name);
        MAIN_FUNCTION = resources.getString(R.string.main_function);
    }
}