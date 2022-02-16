package com.viafoura.myapplication.managers.authorization;

import android.util.Base64;

import com.viafoura.myapplication.managers.network.NetworkEncodingMethod;
import com.viafoura.myapplication.managers.network.NetworkManager;
import com.viafoura.myapplication.managers.network.NetworkManagerInterface;
import com.viafoura.myapplication.managers.network.NetworkRequestMethod;
import com.viafoura.myapplication.managers.persistence.PersistenceKeys;
import com.viafoura.myapplication.managers.persistence.PersistenceManager;
import com.viafoura.myapplication.resources.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;

public class AuthorizationManager implements AuthorizationManagerInterface {
    private PersistenceManager persistenceManager;
    private NetworkManager networkManager;

    public AuthorizationManager(PersistenceManager persistenceManager, NetworkManager networkManager){
        this.persistenceManager = persistenceManager;
        this.networkManager = networkManager;
    }

    @Override
    public void authorize(AuthorizationManagerResult callback) {
        JSONObject body = new JSONObject();
        try {
            body.put("grant_type", "client_credentials");
            body.put("scope", Constant.LIVE_COMMENTS_SECTION_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String auth = Constant.AUTHORIZATION_USERNAME + ":" + Constant.AUTHORIZATION_PASSWORD;
        String authHeader = "Basic " + Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
        HashMap<String, String> customHeaders = new HashMap<>();
        customHeaders.put("Authorization", authHeader);
        customHeaders.put("Content-Type", "application/x-www-form-urlencoded");

        networkManager.newRequest(Constant.AUTHORIZATION_BASE_URL + "authorize_client", NetworkRequestMethod.POST, body, customHeaders, NetworkEncodingMethod.URL_ENCODING,
        new NetworkManagerInterface.NetworkResult() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.has("access_token")){
                        persistenceManager.saveObject(PersistenceKeys.AUTH_TOKEN, response.getString("access_token"));
                        callback.onSuccess();
                    }
                    else{
                        callback.onError();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError();
                }
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }
}
