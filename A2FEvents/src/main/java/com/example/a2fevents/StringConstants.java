package com.example.a2fevents;

import android.content.Context;
import android.content.res.Resources;

public class StringConstants {
    public static String EVENT_IMAGE_LINK;
    public static String EVENT_IMAGE_CLICK_LINK;
    public static String EVENT_IMAGE_NAME;
    public static String EVENT_MONTH;
    public static String EVENT_DAY_NUMBER;
    public static String EVENT_NAME;
    public static String EVENT_EXCERPTS;
    public static String EVENT_EXCERPTS_LINKS;
    public static String EVENT_NUM_EXCERPTS;
    public static String SAVE_THE_DATE_NAME;
    public static String IMAGE_PREFIX;
    public static String FOLDER_NAME;
    public static String NO_EVENTS;
    public static String SCRIPT_NAME;
    public static String MAIN_FUNCTION;
    public static String CALENDAR_PROMPT_TAG;
    public static String CALENDAR_INTENT_TYPE;
    public static String A2F_WEBSITE;
    public static String GRACEPOINT_WEBSITE;

    public static void setupStringConstants(Context context) {
        // Retrieves String constants from xml
        Resources resources = context.getResources();

        EVENT_IMAGE_LINK = resources.getString(R.string.event_image_link);
        EVENT_IMAGE_CLICK_LINK = context.getResources().getString(R.string.event_image_click_link);
        EVENT_IMAGE_NAME = resources.getString(R.string.event_image_name);
        EVENT_MONTH = resources.getString(R.string.event_month);
        EVENT_DAY_NUMBER = resources.getString(R.string.event_day_number);
        EVENT_NAME = resources.getString(R.string.event_name);
        EVENT_EXCERPTS = resources.getString(R.string.event_excerpts);
        EVENT_EXCERPTS_LINKS = context.getResources().getString(R.string.event_excerpts_links);
        EVENT_NUM_EXCERPTS = resources.getString(R.string.event_num_excerpts);
        SAVE_THE_DATE_NAME = resources.getString(R.string.save_the_date_name);
        IMAGE_PREFIX = resources.getString(R.string.image_prefix);
        FOLDER_NAME = resources.getString(R.string.folder_name);
        NO_EVENTS = resources.getString(R.string.no_events);
        SCRIPT_NAME = resources.getString(R.string.script_name);
        MAIN_FUNCTION = resources.getString(R.string.main_function);
        CALENDAR_PROMPT_TAG = resources.getString(R.string.calendar_prompt_tag);
        CALENDAR_INTENT_TYPE = resources.getString(R.string.calendar_intent_type);
        A2F_WEBSITE = resources.getString(R.string.a2f_website);
        GRACEPOINT_WEBSITE = resources.getString(R.string.gracepoint_website);
    }
}
