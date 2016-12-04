package dsfx3d.dope.ytp3.utils;

import android.util.Log;

/**
 *
 *  @L
 *
 *  L is the custom logger class for the entire application
 *
 * */


public class L/*ogger*/ {
    private String TAG;

    public static final int VERBOSE = 1;
    public static final int ERROR = 0;

    public L(String tag) {
        TAG = "__"+tag;
        if (TAG.length()>20)
            TAG = TAG.substring(0,19)+'%';
    }

    public void p(String msg, int mode) {
        switch (mode) {
            case VERBOSE:
                Log.v(TAG,msg);
                break;

            case ERROR:
                Log.e(TAG,msg);
        }
    }
}
