package com.example.a2fevents;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
        retriever.execute(getApplicationContext(), findViewById(R.id.internalLinearLayout));
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

    public void addToCalendar(View view) {

        // Determine which View collection the view is apart of
        /*int index = 0;
        boolean found = false;
        while(index < MAX_NUM_EVENTS && !found) {

            //found = eventViews[index].contains(view);
            index = found ? index : index + 1;
        }

        if(index < MAX_NUM_EVENTS && !eventViews[index].isEmpty()) {
            // Prompt for calendar addition
            new AddToCalendarDialogFragment(eventViews[index]).show(getSupportFragmentManager(), StringConstants.CALENDAR_PROMPT_TAG);
        }*/
    }
}
