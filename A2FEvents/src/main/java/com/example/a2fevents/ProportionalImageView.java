package com.example.a2fevents;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import java.util.HashSet;

public class ProportionalImageView extends AppCompatImageView {

    private static final double WIDTH_TO_HEIGHT_RATIO = 1.791666667;
    private Context theContext;
    private Intent intent;
    private static HashSet<Integer> drawnImageIds;

    public ProportionalImageView(Context context) {
        super(context);
        theContext = context;
        drawnImageIds = new HashSet<>();
        setupIntent();
    }

    public ProportionalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        theContext = context;
        drawnImageIds = new HashSet<>();
        setupIntent();
    }

    public ProportionalImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        theContext = context;
        drawnImageIds = new HashSet<>();
        setupIntent();
    }

    private void setupIntent() {
        // Sets up the intent for broadcast when ImageView dimensions are set
        intent = new Intent();
        intent.putExtra(getResources().getString(R.string.image_view_id), this.getId());
        intent.setPackage(getResources().getString(R.string.package_name));
        intent.setAction(getResources().getString(R.string.image_drawn_receiver_intent));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();

        // Sets width and height based on TFN aspect ration and screen aspect ratio
        if (drawable != null) {

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int)(width / WIDTH_TO_HEIGHT_RATIO);
            setMeasuredDimension(width, height);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        // Checks if broadcast has already been sent for this ImageView
        if(!drawnImageIds.contains(this.getId())) {

            // Sends the broadcast and updates the Set
            theContext.sendBroadcast(intent);
            drawnImageIds.add(this.getId());
        }
    }
}