package com.example.a2fevents;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class AddToCalendarDialogFragment extends DialogFragment {

    private ViewCollection eventViewCollection;

    public AddToCalendarDialogFragment(ViewCollection theEventViewCollection) {
        eventViewCollection = theEventViewCollection;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        builder.setMessage(R.string.add_to_calendar_prompt)
                .setPositiveButton(R.string.add_to_calendar_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Launch calendar activity
                        addToCalendar();
                    }
                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void addToCalendar() {

        // Setup intent
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(StringConstants.CALENDAR_INTENT_TYPE);

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, getStartTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getEndTime());
        intent.putExtra(CalendarContract.Events.TITLE, eventViewCollection.getName());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, eventViewCollection.getDescription());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, eventViewCollection.getLocation());

        // Start calendar activity
        startActivity(intent);
    }

    private long getStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(eventViewCollection.getYear(), eventViewCollection.getMonth(), eventViewCollection.getDayNumber(), eventViewCollection.getStartHour(), eventViewCollection.getStartMinute());
        return calendar.getTimeInMillis();
    }

    private long getEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(eventViewCollection.getYear(), eventViewCollection.getMonth(), eventViewCollection.getDayNumber(), eventViewCollection.getEndHour(), eventViewCollection.getEndMinute());
        return calendar.getTimeInMillis();
    }
}