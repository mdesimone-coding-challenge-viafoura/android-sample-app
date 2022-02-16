package com.viafoura.myapplication.managers.persistence;

import android.content.Context;
import android.content.SharedPreferences;

public class PersistenceManager implements PersistenceManagerInterface {
    private Context context;
    private SharedPreferences sharedPreferences;

    private static String PREFERENCES_NAME = "SampleAppViafoura";

    public PersistenceManager(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void saveObject(String key, Object value) {
        SharedPreferences.Editor preferences = sharedPreferences.edit();
        if(value instanceof String){
            preferences.putString(key, (String) value);
        }
        else if(value instanceof Boolean){
            preferences.putBoolean(key, (Boolean) value);
        }
        preferences.apply();
    }

    @Override
    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    @Override
    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }
}
