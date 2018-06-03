package com.xxxgreen.mvx.drivekeeper.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xxxgreen.mvx.drivekeeper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MVX on 7/31/2017.
 */

public class ButtonPanel extends LinearLayout {
    // Desired width-to-height ratio - 4.0 for four squares
    private final String TAG = "ButtonPanel";
    private final double mScale = 4.0;
    public ImageButton playButton, pauseButton, stopButton, addButton;

    public ButtonPanel(Context context) {
        super(context);
        init();
    }

    public ButtonPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width > (int)((mScale * height) + 0.5)) {
            width = (int)((mScale * height) + 0.5);
        } else {
            height = (int)((width / mScale) + 0.5);
        }

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        );

        setMeasuredDimension(width, height);
    }

    private void init() {
        inflate(getContext(), R.layout.buttonpanel_main, this);

        playButton = (ImageButton) findViewById(R.id.playButton);
        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        stopButton = (ImageButton) findViewById(R.id.stopButton);
        addButton = (ImageButton) findViewById(R.id.addButton);
    }

}
