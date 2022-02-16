package com.viafoura.myapplication.managers.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NetworkManager implements NetworkManagerInterface {
    private Context context;
    private RequestQueue requestQueue;

    public NetworkManager(Context context){
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void newRequest(String url, NetworkRequestMethod method, JSONObject body, Map<String, String> headers, NetworkEncodingMethod encodingMethod, NetworkResult networkResult) {
        StringRequest request = new StringRequest(getFromNetworkRequestMethod(method), url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.length() == 0){
                                networkResult.onSuccess(null);
                            }
                            else{
                                JSONObject object = new JSONObject(response);
                                networkResult.onSuccess(object);
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            networkResult.onError();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                networkResult.onError();
            }
        }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                if(encodingMethod == NetworkEncodingMethod.JSON){
                    try {
                        return body.toString().getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return super.getBody();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(encodingMethod == NetworkEncodingMethod.URL_ENCODING){
                    try {
                        return jsonToMap(body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(headers != null){
                    return headers;
                }
                return super.getHeaders();
            }
        };

        requestQueue.add(request);
    }

    public int getFromNetworkRequestMethod(NetworkRequestMethod requestMethod){
        if(requestMethod == NetworkRequestMethod.POST){
            return Request.Method.POST;
        }
        else if(requestMethod == NetworkRequestMethod.DELETE){
            return Request.Method.DELETE;
        }
        return Request.Method.GET;
    }

    public static Map<String, String> jsonToMap(JSONObject json) throws JSONException {
        Map<String, String> retMap = new HashMap<String, String>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, String> toMap(JSONObject object) throws JSONException {
        Map<String, String> map = new HashMap<String, String>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            String value = object.getString(key);

            map.put(key, value);
        }
        return map;
    }
}
