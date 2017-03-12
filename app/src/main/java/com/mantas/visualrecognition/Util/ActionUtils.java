package com.mantas.visualrecognition.Util;


import android.view.MotionEvent;

public class ActionUtils {


    public static boolean containsTouch(int action) {
        return action == MotionEvent.ACTION_UP;
    }
}
