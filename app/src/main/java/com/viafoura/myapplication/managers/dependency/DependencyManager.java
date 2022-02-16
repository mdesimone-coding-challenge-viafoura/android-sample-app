package com.viafoura.myapplication.managers.dependency;

import android.content.Context;

import com.viafoura.myapplication.managers.authentication.AuthenticationManager;
import com.viafoura.myapplication.managers.authorization.AuthorizationManager;
import com.viafoura.myapplication.managers.comments.CommentsManager;
import com.viafoura.myapplication.managers.network.NetworkManager;
import com.viafoura.myapplication.managers.persistence.PersistenceManager;

public class DependencyManager{
    private PersistenceManager persistenceManager;
    private AuthorizationManager authorizationManager;
    private AuthenticationManager authenticationManager;
    private CommentsManager commentsManager;
    private NetworkManager networkManager;

    private Context context;

    public DependencyManager(Context context){
        this.context = context;
    }

    public NetworkManager provideNetworkManager(){
        if(networkManager == null){
            networkManager = new NetworkManager(context);
        }
        return networkManager;
    }

    public AuthenticationManager provideAuthenticationManager(){
        if(authenticationManager == null){
            authenticationManager = new AuthenticationManager(providePersistenceManager(), provideNetworkManager());
        }
        return authenticationManager;
    }

    public AuthorizationManager provideAuthorizationManager(){
        if(authorizationManager == null){
            authorizationManager = new AuthorizationManager(providePersistenceManager(), provideNetworkManager());
        }
        return authorizationManager;
    }

    public PersistenceManager providePersistenceManager(){
        if(persistenceManager == null){
            persistenceManager = new PersistenceManager(context);
        }
        return persistenceManager;
    }

    public CommentsManager provideCommentsManager(){
        if(commentsManager == null){
            commentsManager = new CommentsManager(providePersistenceManager(), provideAuthorizationManager(), provideNetworkManager());
        }
        return commentsManager;
    }
}
