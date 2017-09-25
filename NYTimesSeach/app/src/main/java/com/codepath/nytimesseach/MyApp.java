package com.codepath.nytimesseach;

import android.app.Application;

import com.codepath.nytimesseach.dagger.DaggerNYTimesComponent;
import com.codepath.nytimesseach.dagger.NYTimesComponent;

/**
 * Created by jan_spidlen on 9/24/17.
 */

public class MyApp extends Application {
    private NYTimesComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerNYTimesComponent.create();
    }

    public NYTimesComponent getNetComponent() {
        return component;
    }
}
