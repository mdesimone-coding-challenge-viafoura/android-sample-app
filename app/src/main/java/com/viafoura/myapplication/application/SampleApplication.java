package com.viafoura.myapplication.application;

import android.app.Application;

import com.viafoura.myapplication.managers.dependency.DependencyManager;

public class SampleApplication extends Application {
    public DependencyManager dependencyManager;

    @Override
    public void onCreate() {
        super.onCreate();

        dependencyManager = new DependencyManager(getApplicationContext());
    }
}
