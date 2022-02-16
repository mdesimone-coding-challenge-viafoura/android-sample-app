package com.viafoura.myapplication.managers.authorization;

public interface AuthorizationManagerInterface {
    interface AuthorizationManagerResult{
        void onSuccess();
        void onError();
    }

    void authorize(AuthorizationManagerResult callback);
}
