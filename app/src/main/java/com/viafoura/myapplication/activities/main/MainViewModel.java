package com.viafoura.myapplication.activities.main;

import com.viafoura.myapplication.activities.base.BaseViewModel;
import com.viafoura.myapplication.application.SampleApplication;
import com.viafoura.myapplication.managers.authentication.AuthenticationManager;
import com.viafoura.myapplication.managers.authentication.AuthenticationManagerInterface;
import com.viafoura.myapplication.managers.authorization.AuthorizationManagerInterface;
import com.viafoura.myapplication.managers.comments.CommentsManager;

public class MainViewModel extends BaseViewModel {
    private AuthenticationManager authenticationManager;

    static final String ARTICLE_URL = "https://www.clarin.com/autos/dymak-spiritus-triciclo-electrico-quieren-fabricar-argentina-vas-poder-manejar_0_KOlHQRzm7T.html";
    static final String ARTICLE_NAME = "Triciclo Electrico";
    static final String ARTICLE_ID = "@313j12m2w1";

    boolean isUserAuthenticated = false;

    public MainViewModel(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;

        loadAuthenticationState();
    }

    public void loadAuthenticationState(){
        isUserAuthenticated = authenticationManager.isAuthenticated();
    }

    public void login(String email, String password, LoginInterface loginInterface){
        if(email.isEmpty() || password.isEmpty()){
            loginInterface.onError();
            return;
        }
        authenticationManager.login(email, password, new AuthenticationManagerInterface.AuthenticationManagerResult() {
            @Override
            public void onSuccess() {
                loadAuthenticationState();
                loginInterface.onSuccess();
            }

            @Override
            public void onError() {
                loginInterface.onError();
            }
        });
    }

    public void signup(String name, String email, String password, SignUpInterface signUpInterface){
        if(name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()){
            signUpInterface.onError();
            return;
        }
        authenticationManager.signup(name, email, password, new AuthenticationManagerInterface.AuthenticationManagerResult() {
            @Override
            public void onSuccess() {
                loadAuthenticationState();
                signUpInterface.onSuccess();
            }

            @Override
            public void onError() {
                signUpInterface.onError();
            }
        });
    }

    public void logout(LogoutInterface logoutInterface){
        authenticationManager.logout(new AuthenticationManagerInterface.AuthenticationManagerResult() {
            @Override
            public void onSuccess() {
                loadAuthenticationState();
                logoutInterface.onSuccess();
            }

            @Override
            public void onError() {
                logoutInterface.onError();
            }
        });
    }

    public interface SignUpInterface {
        void onSuccess();
        void onError();
    }

    public interface LoginInterface {
        void onSuccess();
        void onError();
    }

    public interface LogoutInterface {
        void onSuccess();
        void onError();
    }
}
