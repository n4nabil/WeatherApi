package com.task.dawadoz.forecast.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


public class MainApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
