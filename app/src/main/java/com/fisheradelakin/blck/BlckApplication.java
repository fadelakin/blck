package com.fisheradelakin.blck;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by temidayo on 2/7/16.
 */
public class BlckApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }
}
