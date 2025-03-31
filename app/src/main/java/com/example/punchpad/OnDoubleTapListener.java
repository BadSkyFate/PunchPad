package com.example.punchpad;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class OnDoubleTapListener implements View.OnTouchListener {

    private GestureDetector gestureDetector;

    public OnDoubleTapListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // Safely call the abstract method without causing recursion
                OnDoubleTapListener.this.onDoubleTap(e);
                return true;
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    // Abstract method for subclasses to handle double-tap events
    public abstract void onDoubleTap(MotionEvent e);
}
