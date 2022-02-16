package com.viafoura.myapplication.managers.comments;

import com.viafoura.myapplication.managers.authorization.AuthorizationManager;
import com.viafoura.myapplication.managers.authorization.AuthorizationManagerInterface;
import com.viafoura.myapplication.managers.network.NetworkEncodingMethod;
import com.viafoura.myapplication.managers.network.NetworkManager;
import com.viafoura.myapplication.managers.network.NetworkManagerInterface;
import com.viafoura.myapplication.managers.network.NetworkRequestMethod;
import com.viafoura.myapplication.managers.persistence.PersistenceKeys;
import com.viafoura.myapplication.managers.persistence.PersistenceManager;
import com.viafoura.myapplication.resources.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class CommentsManager implements CommentsManagerInterface {
    private PersistenceManager persistenceManager;
    private AuthorizationManager authorizationManager;
    private NetworkManager networkManager;

    public CommentsManager(PersistenceManager persistenceManager, AuthorizationManager authorizationManager, NetworkManager networkManager){
        this.persistenceManager = persistenceManager;
        this.authorizationManager = authorizationManager;
        this.networkManager = networkManager;
    }

    @Override
    public void createCommentsContainer(String containerId, String title, CommentsManagerResultUUID callback) {
        JSONObject body = new JSONObject();
        try {
            body.put("container_id", containerId);
            body.put("title", title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = Constant.LIVE_COMMENTS_BASE_URL + "livecomments/" + Constant.LIVE_COMMENTS_SECTION_ID;
        networkManager.newRequest(url, NetworkRequestMethod.POST, body, getHeaders(), NetworkEncodingMethod.JSON,
        new NetworkManagerInterface.NetworkResult() {
            @Override
            public void onSuccess(JSONObject response) {
                try{
                    if(response.has("content_container_uuid")){
                        UUID uuid = UUID.fromString(response.getString("content_container_uuid"));
                        callback.onSuccess(uuid);
                    }
                    else{
                        callback.onError();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void createComment(UUID contentContainerUUID, String originTitle, String content, CommentsManagerResultUUID callback) {
        authorizationManager.authorize(new AuthorizationManagerInterface.AuthorizationManagerResult() {
            @Override
            public void onSuccess() {
                JSONObject body = new JSONObject();
                try {
                    body.put("content", content);
                    JSONObject metadata = new JSONObject();
                    metadata.put("origin_title", originTitle);
                    body.put("metadata", metadata);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url = Constant.LIVE_COMMENTS_BASE_URL + "livecomments/" + Constant.LIVE_COMMENTS_SECTION_ID + "/" + contentContainerUUID.toString() + "/comments";
                networkManager.newRequest(url, NetworkRequestMethod.POST, body, getHeaders(), NetworkEncodingMethod.JSON, new NetworkManagerInterface.NetworkResult() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try{
                            if(response.has("content_uuid")){
                                UUID uuid = UUID.fromString(response.getString("content_uuid"));
                                callback.onSuccess(uuid);
                            }
                            else{
                                callback.onError();
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {
                        callback.onError();
                    }
                });
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void likeComment(UUID contentContainerUUID, UUID contentUUID, CommentsManagerResult callback) {
        authorizationManager.authorize(new AuthorizationManagerInterface.AuthorizationManagerResult() {
            @Override
            public void onSuccess() {
                JSONObject body = new JSONObject();
                String url = Constant.LIVE_COMMENTS_BASE_URL + "livecomments/" + Constant.LIVE_COMMENTS_SECTION_ID + "/" + contentContainerUUID.toString() + "/comments/" + contentUUID.toString() + "/like";
                networkManager.newRequest(url, NetworkRequestMethod.POST, body, getHeaders(), NetworkEncodingMethod.JSON, new NetworkManagerInterface.NetworkResult() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        callback.onSuccess();
                    }

                    @Override
                    public void onError() {
                        callback.onError();
                    }
                });
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void dislikeComment(UUID contentContainerUUID, UUID contentUUID, CommentsManagerResult callback) {
        authorizationManager.authorize(new AuthorizationManagerInterface.AuthorizationManagerResult() {
            @Override
            public void onSuccess() {
                JSONObject body = new JSONObject();
                String url = Constant.LIVE_COMMENTS_BASE_URL + "livecomments/" + Constant.LIVE_COMMENTS_SECTION_ID + "/" + contentContainerUUID.toString() + "/comments/" + contentUUID.toString() + "/dislike";
                networkManager.newRequest(url, NetworkRequestMethod.POST, body, getHeaders(), NetworkEncodingMethod.JSON, new NetworkManagerInterface.NetworkResult() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        callback.onSuccess();
                    }

                    @Override
                    public void onError() {
                        callback.onError();
                    }
                });
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void replyToComment(UUID contentContainerUUID, UUID contentUUID, String originTitle, String content, CommentsManagerResult callback) {
        authorizationManager.authorize(new AuthorizationManagerInterface.AuthorizationManagerResult() {
            @Override
            public void onSuccess() {
                JSONObject body = new JSONObject();
                try {
                    body.put("content", content);
                    JSONObject metadata = new JSONObject();
                    metadata.put("origin_title", originTitle);
                    body.put("metadata", metadata);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url = Constant.LIVE_COMMENTS_BASE_URL + "livecomments/" + Constant.LIVE_COMMENTS_SECTION_ID + "/" + contentContainerUUID.toString() + "/comments/" + contentUUID.toString();
                networkManager.newRequest(url, NetworkRequestMethod.POST, body, getHeaders(), NetworkEncodingMethod.JSON, new NetworkManagerInterface.NetworkResult() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        callback.onSuccess();
                    }

                    @Override
                    public void onError() {
                        callback.onError();
                    }
                });
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    private Map<String, String> getHeaders(){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + persistenceManager.getString(PersistenceKeys.AUTH_TOKEN));
        return headers;
    }
}
