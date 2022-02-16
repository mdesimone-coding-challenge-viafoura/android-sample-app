package com.viafoura.myapplication.managers.network;

import org.json.JSONObject;

import java.util.Map;

public interface NetworkManagerInterface {
    interface NetworkResult{
        void onSuccess(JSONObject response);
        void onError();
    }

    void newRequest(String url, NetworkRequestMethod method, JSONObject body, Map<String, String> headers, NetworkEncodingMethod encodingMethod, NetworkResult networkResult);
}
