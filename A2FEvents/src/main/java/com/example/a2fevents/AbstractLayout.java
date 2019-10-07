package com.example.a2fevents;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.chaquo.python.PyObject;
import java.io.File;
import java.util.Calendar;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;

public abstract class AbstractLayout extends ConstraintLayout {

    private static int id = (int) Calendar.getInstance().getTimeInMillis();

    private ProportionalImageView imageView;
    private TextView nameView;
    private String nameText;
    protected String locationText = null;
    protected String descriptionText = null;
    protected String timeText = null;
    private int excerptStartIndex;
    private SparseArray<MainActivity.InfoLayout> infoLayouts;
    private SparseArray<String> locations;
    private SparseArray<String> descriptions;
    private SparseArray<String> times;
    private int numDescriptions = 0;
    private int numTimes = 0;
    private int numLocations = 0;

    public AbstractLayout(Context context) {
        super(context);
        locations = new SparseArray<>();
        descriptions = new SparseArray<>();
        times = new SparseArray<>();
    }

    public AbstractLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        locations = new SparseArray<>();
        descriptions = new SparseArray<>();
        times = new SparseArray<>();
    }

    public AbstractLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        locations = new SparseArray<>();
        descriptions = new SparseArray<>();
        times = new SparseArray<>();
    }

    protected void addInfoViews(Context context, MainActivity mainActivity, int numInfo) {
        // Adds numInfo infoLayouts to the current layout
        ConstraintLayout constraintLayout = (ConstraintLayout) this.getChildAt(0);
        excerptStartIndex = constraintLayout.getChildCount();
        infoLayouts = new SparseArray<>();

        for(int i = 0; i < numInfo; i++) {
            MainActivity.InfoLayout infoLayout =  mainActivity.new InfoLayout(context);
            infoLayouts.append(infoLayout.getTextId(), infoLayout);
            infoLayout.setId(id++);
            View aboveChild = constraintLayout.getChildAt(constraintLayout.getChildCount() - 1);
            constraintLayout.addView(infoLayout);

            // Applies new constraints
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(infoLayout.getId(), ConstraintSet.TOP, aboveChild.getId(), ConstraintSet.BOTTOM, 0);
            constraintSet.connect(infoLayout.getId(), ConstraintSet.LEFT, aboveChild.getId(), ConstraintSet.LEFT, 0);
            constraintSet.connect(infoLayout.getId(), ConstraintSet.RIGHT, aboveChild.getId(), ConstraintSet.RIGHT, 0);
            constraintSet.applyTo(constraintLayout);
        }
    }

    protected void setupViews(ProportionalImageView theImageView, TextView theNameView) {
        imageView = theImageView;
        nameView = theNameView;
    }

    public void displayEvent(String imagePath, String month, String dayNumber, String name, List<PyObject> excerpts) {
        // Displays the image
        displayImage(imagePath);

        // Sets and stores text of TextViews
        nameView.setText(name);
        nameText = name;

        // Displays excerpt infos and saves off Strings
        for(int i = 0; i < excerpts.size(); i++) {
            PyObject text = excerpts.get(i);

            if(text != null) {
                ConstraintLayout constraintLayout = (ConstraintLayout) this.getChildAt(0);
                MainActivity.InfoLayout infoLayout = (MainActivity.InfoLayout) constraintLayout.getChildAt(i + excerptStartIndex);
                infoLayout.setText(text.toString());
                addToList(infoLayout.getTextId(), text.toString());
            }
        }
    }

    private void displayImage(String imagePath) {
        // Displays the image if it exists
        if(!imagePath.equals("")) {
            File image = new File(imagePath);
            if(image.exists()) {
                try {
                    if(Image.isGIF(imagePath)) {
                        GifDrawable gifDrawable = new GifDrawable(image);
                        imageView.setImageDrawable(gifDrawable);

                    } else {
                        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addToList(int id, String text) {
        // Saves off info Strings
        boolean added = false;
        if(text.contains("WHEN:") || text.contains("When:") || text.matches("^[0-9][aApP][mM]\\s[-]\\s.*")) {
            numTimes++;
            times.append(id, text);
            added = true;
        }

        if(text.contains("WHERE:") || text.contains("Where:") || text.matches(".*\\s[@]\\s\\D.*")) {
            numLocations++;
            locations.append(id, text);
            added = true;
        }

        if(!added) {
            numDescriptions++;
            descriptions.append(id, text);
        }
    }

    protected void setupOnClickListener(View.OnClickListener listener) {
        // Sets up onClickListener
        imageView.setOnClickListener(listener);
        nameView.setOnClickListener(listener);
    }

    public String getName() {

        // Retrieves the name
        if(numTimes > 1) {

            // Handles multiple names case
            if(timeText.matches(".*[0-9][aApP][mM]\\s[-]\\s.*\\s[@]\\s.*")) {
                String text = timeText.substring(timeText.indexOf('-') + 1);
                text = text.substring(0, text.indexOf('@'));
                nameText = text.trim();
            }
        }
        return nameText;
    }

    public String getDescription() {
        if(descriptionText == null) {
            return "";
        }

        return descriptionText;
    }

    public String getLocation() {

        if(locationText == null) {
            return "";
        }

        // Where: and Multiple locations
        if((locationText.contains("Where:") || locationText.contains("WHERE:")) && locationText.matches(".*([\\s][@][\\s].*){2,}")) {
            // Removes "Where: "
            locationText = locationText.substring(locationText.indexOf(":") + 2);

        } else if (locationText.contains("Where:") || locationText.contains("WHERE:")) {
            locationText = locationText.substring(locationText.indexOf(":") + 2);

        } else if(locationText.equals(timeText)) {
            // Time and location text are the same
            locationText = locationText.substring(locationText.indexOf("@") + 1).trim();
        }

        return locationText;
    }

    protected String getClickedText(int numInfos, SparseArray<String> array, View clickedView) {
        String text = null;

        // Retrieves the text that was clicked
        if(numInfos == 1) {
            // Only one possibility
            text = array.get(array.keyAt(0));

        } else if(numInfos > 1) {

            // Multiple possibilities
            if(infoLayouts.indexOfKey(clickedView.getId()) >= 0) {
                // Multiple possibilities but infoLayout was clicked
                text = infoLayouts.get(infoLayouts.keyAt(infoLayouts.indexOfKey(clickedView.getId()))).getText();
            }
        }

        return text;
    }

    public void updateTexts(View clickedView) {
        // Updates the current texts
        timeText = getClickedText(numTimes, times, clickedView);
        locationText = getClickedText(numLocations, locations, clickedView);
        descriptionText = getClickedText(numDescriptions, descriptions, clickedView);
    }

    public boolean hasView(View view) {
        int currentId = view.getId();
        return imageView.getId() == currentId || nameView.getId() == currentId || infoLayouts.indexOfKey(currentId) >= 0;
    }

    protected int getEndHour(int startHour) {
        // Calculates the ending hour
        int endHour = (startHour + 1) % 25;
        return endHour == 0 ? 1 : endHour;
    }

    public abstract Calendar getStartTime();
    public abstract Calendar getEndTime();
}
