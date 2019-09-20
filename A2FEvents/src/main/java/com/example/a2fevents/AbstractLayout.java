package com.example.a2fevents;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.icu.text.IDNA;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.chaquo.python.PyObject;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import static android.view.Gravity.CENTER;

public abstract class AbstractLayout extends ConstraintLayout {

    private static int id = (int) Calendar.getInstance().getTimeInMillis();

    private ProportionalImageView imageView;
    private TextView nameView;
    private String nameText;
    private int excerptStartIndex;

    public AbstractLayout(Context context) {
        super(context);
    }

    public AbstractLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbstractLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void addInfoViews(Context context, MainActivity mainActivity, int numInfo) {
        ConstraintLayout constraintLayout = (ConstraintLayout) this.getChildAt(0);
        excerptStartIndex = constraintLayout.getChildCount();

        for(int i = 0; i < numInfo; i++) {
            MainActivity.InfoLayout infoLayout =  mainActivity.new InfoLayout(context);
            infoLayout.setId(id++);
            View aboveChild = constraintLayout.getChildAt(constraintLayout.getChildCount() - 1);
            constraintLayout.addView(infoLayout);

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

        // Displays the image if it exists
        if(!imagePath.equals("")) {
            File image = new File(imagePath);
            if(image.exists()) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            }
        }

        // Sets and stores text of TextViews
        nameView.setText(name);
        nameText = name;

        for(int i = 0; i < excerpts.size(); i++) {
            PyObject text = excerpts.get(i);

            if(text != null) {
                ConstraintLayout constraintLayout = (ConstraintLayout) this.getChildAt(0);
                MainActivity.InfoLayout infoLayout = (MainActivity.InfoLayout) constraintLayout.getChildAt(i + excerptStartIndex);
                infoLayout.setText(text.toString());
            }
        }
    }

    protected void setupOnClickListener(View.OnClickListener listener) {
        // Sets up onClickListener
        imageView.setOnClickListener(listener);
        nameView.setOnClickListener(listener);
    }

    public String getName() {
        return nameText;
    }

    /*public String getDescription() {
        return descriptionText;
    }

    public String getLocation() {
        // Assumes locationText format of WHERE: QUALCOMM ROOM (in Warren)
        return locationText.substring(locationText.indexOf(':') + 2);
    }*/

    public boolean hasView(View view) {
        // Determines if this layout contains the given view
        int currentId = view.getId();

        int currentIndex = 0;
        boolean found = false;
        while(!found && currentIndex < this.getChildCount()) {

            if(this.getChildAt(currentIndex).getId() == currentId) {
                found = true;
            }

            currentIndex++;
        }

        return imageView.getId() == currentId || nameView.getId() == currentId || found;
    }

    public abstract Calendar getStartTime();
    public abstract Calendar getEndTime();
}
