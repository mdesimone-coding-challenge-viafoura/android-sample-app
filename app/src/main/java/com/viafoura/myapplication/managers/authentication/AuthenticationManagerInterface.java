package com.viafoura.myapplication.managers.authentication;

import com.viafoura.myapplication.managers.authorization.AuthorizationManagerInterface;

public interface AuthenticationManagerInterface {
    interface AuthenticationManagerResult{
        void onSuccess();
        void onError();
    }

    void login(String email, String password, AuthenticationManagerResult callback);
    void signup(String name, String email, String password, AuthenticationManagerResult callback);
    void logout(AuthenticationManagerResult callback);
    boolean isAuthenticated();
}
