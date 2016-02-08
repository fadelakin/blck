package com.fisheradelakin.blck;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

/**
 * Created by temidayo on 2/7/16.
 */
public class BlckApplication extends Application {

    public static final String PREFS = "PREFS";
    public static final String SCREEN_HEIGHT = "SCREEN_HEIGHT";
    public static final String SCREEN_WIDTH = "SCREEN_WIDTH";

    @Override
    public void onCreate() {
        super.onCreate();

        // Get phone screen details
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        SharedPreferences sharedPreferences
                = getSharedPreferences(PREFS, MODE_PRIVATE);

        sharedPreferences.edit()
                .putInt(SCREEN_HEIGHT, metrics.heightPixels)
                .putInt(SCREEN_WIDTH, metrics.widthPixels)
                .apply();
    }
}
