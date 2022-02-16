package com.viafoura.myapplication.managers.authentication;

import com.viafoura.myapplication.managers.network.NetworkEncodingMethod;
import com.viafoura.myapplication.managers.network.NetworkManager;
import com.viafoura.myapplication.managers.network.NetworkManagerInterface;
import com.viafoura.myapplication.managers.network.NetworkRequestMethod;
import com.viafoura.myapplication.managers.persistence.PersistenceKeys;
import com.viafoura.myapplication.managers.persistence.PersistenceManager;
import com.viafoura.myapplication.resources.Constant;

import org.json.JSONException;
import org.json.JSONObject;


public class AuthenticationManager implements AuthenticationManagerInterface {
    private PersistenceManager persistenceManager;
    private NetworkManager networkManager;

    public AuthenticationManager(PersistenceManager persistenceManager, NetworkManager networkManager){
        this.persistenceManager = persistenceManager;
        this.networkManager = networkManager;
    }

    @Override
    public void login(String email, String password, AuthenticationManagerResult callback) {
        JSONObject body = new JSONObject();
        try {
            body.put("email", email);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        networkManager.newRequest(Constant.USERS_BASE_URL + Constant.USERS_SITE_ID + "/users/login", NetworkRequestMethod.POST, body, null, NetworkEncodingMethod.JSON, new NetworkManagerInterface.NetworkResult() {
            @Override
            public void onSuccess(JSONObject response) {
                persistenceManager.saveObject(PersistenceKeys.LOGGED_IN, true);
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void signup(String name, String email, String password, AuthenticationManagerResult callback) {
        JSONObject body = new JSONObject();
        try {
            body.put("name", name);
            body.put("email", email);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkManager.newRequest(Constant.USERS_BASE_URL + Constant.USERS_SITE_ID + "/users", NetworkRequestMethod.POST, body, null, NetworkEncodingMethod.JSON,
        new NetworkManagerInterface.NetworkResult() {
            @Override
            public void onSuccess(JSONObject response) {
                persistenceManager.saveObject(PersistenceKeys.LOGGED_IN, true);
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void logout(AuthenticationManagerResult callback) {
        networkManager.newRequest(Constant.USERS_BASE_URL + Constant.USERS_SITE_ID + "/users/logout", NetworkRequestMethod.DELETE, null, null, NetworkEncodingMethod.JSON, new NetworkManagerInterface.NetworkResult() {
            @Override
            public void onSuccess(JSONObject response) {
                persistenceManager.saveObject(PersistenceKeys.LOGGED_IN, false);
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public boolean isAuthenticated() {
        return persistenceManager.getBoolean(PersistenceKeys.LOGGED_IN);
    }
}
