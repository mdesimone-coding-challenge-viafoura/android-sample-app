package com.viafoura.myapplication.managers.persistence;

public interface PersistenceManagerInterface {
    void saveObject(String key, Object value);
    boolean getBoolean(String key);
    String getString(String key);
}
