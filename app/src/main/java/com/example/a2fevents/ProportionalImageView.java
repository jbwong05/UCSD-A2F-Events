package com.example.a2fevents;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class ProportionalImageView extends AppCompatImageView {

    private static final double widthToHeightRatio = 1.791666667;

    public ProportionalImageView(Context context) {
        super(context);
    }

    public ProportionalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProportionalImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();

        // Sets width and height based on TFN aspect ration and screen aspect ratio
        if (drawable != null) {

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int)(width / widthToHeightRatio);
            setMeasuredDimension(width, height);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}