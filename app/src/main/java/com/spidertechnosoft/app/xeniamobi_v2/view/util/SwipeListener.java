package com.spidertechnosoft.app.xeniamobi_v2.view.util;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by DELL on 03-02-2017.
 */
public class SwipeListener implements View.OnTouchListener {



    public static enum Action {
        LR, // Left to right
        RL, // Right to left
        TB, // Top to bottom
        BT, // Bottom to top
        None // Action not found
    }

    private  int HORIZONTAL_MIN_DISTANCE = 100; // The minimum distance for horizontal swipe
    private  int VERTICAL_MIN_DISTANCE = 80; // The minimum distance for vertical swipe
    private float downX, downY, upX, upY; // Coordinates
    private Action mSwipeDetected = Action.None; // Last action

    public SwipeListener(int hOffset){

        this.HORIZONTAL_MIN_DISTANCE=hOffset;

    }

    public SwipeListener(int hOffset, int vOffset){

        this.HORIZONTAL_MIN_DISTANCE=hOffset;

        this.VERTICAL_MIN_DISTANCE=vOffset;
    }

    public boolean swipeDetected() {
        return mSwipeDetected != Action.None;
    }

    public Action getAction() {
        return mSwipeDetected;
    }

    /**
     * Swipe detection
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                mSwipeDetected = Action.None;
                return false;
            }
            case MotionEvent.ACTION_MOVE: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;


                // Detection of horizontal swipe
                if (Math.abs(deltaX) > HORIZONTAL_MIN_DISTANCE) {
                    // Left to right
                    if (deltaX < 0) {
                        mSwipeDetected = Action.LR;
                        return true;
                    }
                    // Right to left
                    if (deltaX > 0) {
                        mSwipeDetected = Action.RL;
                        return true;
                    }
                } else

                    // Detection of vertical swipe
                    if (Math.abs(deltaY) > VERTICAL_MIN_DISTANCE) {
                        // Top to bottom
                        if (deltaY < 0) {
                            mSwipeDetected = Action.TB;
                            return false;
                        }
                        // Bottom to top
                        if (deltaY > 0) {
                            mSwipeDetected = Action.BT;
                            return false;
                        }
                    }
                return true;
            }
        }
        return false;
    }
}