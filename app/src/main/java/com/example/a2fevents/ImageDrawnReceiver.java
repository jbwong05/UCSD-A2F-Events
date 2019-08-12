package com.example.a2fevents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.widget.TextView;

public class ImageDrawnReceiver extends BroadcastReceiver {

    private static final double IMAGE_WIDTH_TO_HEIGHT_RATIO = 1.791666667;
    private static final double IMAGE_TO_MONTH_HEIGHT_RATIO = 9;
    private static final double IMAGE_TO_DAY_NUMBER_HEIGHT_RATIO = 5.25;

    private SparseArray<ImageViewsCollection> imageCollectionMap;

    public ImageDrawnReceiver(SparseArray<ImageViewsCollection> theImageCollectionMap) {
        imageCollectionMap = theImageCollectionMap;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // Retrieves the id of the sending ImageView
        int id = intent.getIntExtra(context.getResources().getString(R.string.image_view_id), -1);

        // Ensures id was received
        if(id != -1 && imageCollectionMap.get(id) != null) {

            // Retrieves TextViews
            TextView monthView = imageCollectionMap.get(id).getMonthView();
            TextView dayNumberView = imageCollectionMap.get(id).getDayNumberView();

            // Gets the dimensions of the ImageView
            int imageWidth = imageCollectionMap.get(id).getImageView().getWidth();
            int imageHeight = (int) (imageWidth / IMAGE_WIDTH_TO_HEIGHT_RATIO);

            // Set height
            monthView.setHeight((int) (imageHeight / IMAGE_TO_MONTH_HEIGHT_RATIO));
            dayNumberView.setHeight((int) (imageHeight / IMAGE_TO_DAY_NUMBER_HEIGHT_RATIO));

            // Sets width
            int totalHeight = monthView.getHeight() + dayNumberView.getHeight();
            monthView.setWidth(totalHeight);
            dayNumberView.setWidth(totalHeight);
        }
    }
}
