package com.example.laravelpassportdagger.ui.login;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import androidx.lifecycle.ViewModel;


import com.example.laravelpassportdagger.di.api.ApiErrorConverter;
import com.example.laravelpassportdagger.di.session.SessionManager;

import com.example.laravelpassportdagger.models.AccessTokenModel;
import com.example.laravelpassportdagger.models.ApiErrorsModel;
import com.example.laravelpassportdagger.network.API;
import com.example.laravelpassportdagger.network.AuthResource;


import javax.inject.Inject;


import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;



public class LoginUserViewModel extends ViewModel {
    private static final String TAG = "LoginViewModel";

    //Dagger injection via constructor
    private SessionManager sessionManager;
    private API api;
    private ApiErrorConverter apiErrorConverter;
    private ApiErrorsModel apiErrorsModel;



    @Inject
   public LoginUserViewModel(API api, SessionManager sessionManager, ApiErrorConverter apiErrorConverter) {

        this.api =  api;
        this.sessionManager = sessionManager;
        this.apiErrorConverter = apiErrorConverter;

    }

    public void authenticateUser(String email, String password){
        Log.d(TAG, "authenticateUser: Login attempt");
        sessionManager.authenticateUser(queryUser(email,password));
    }

    private LiveData<AuthResource<AccessTokenModel>> queryUser(String email, String password) {
        return LiveDataReactiveStreams.fromPublisher(
                api.login(email, password)
                .onErrorReturn(throwable -> {
                    AccessTokenModel errorLoginModel = new AccessTokenModel();
                    errorLoginModel.setAccessToken("error");

                    try {

                        HttpException error = (HttpException) throwable;
                        Log.d(TAG, "queryUser: " + throwable.getMessage());

                        apiErrorsModel = apiErrorConverter.apiErrorsModel(error.response().errorBody());
                        String errors = apiErrorsModel.getMessage();
                        errorLoginModel.setError_message(errors);
                    }catch (Exception e){
                        e.printStackTrace();
                        errorLoginModel.setError_message("Check credentials....");
                    }

                            return errorLoginModel;
                        })
                    .map(accessTokenModel -> {
                       if (accessTokenModel.getAccessToken().equals("error")){
                           return AuthResource.error(accessTokenModel.getError_message(), (AccessTokenModel)null);
                       }
                       return AuthResource.authenticated(accessTokenModel);
                    })
                .subscribeOn(Schedulers.io())
        );
    }


    LiveData<AuthResource<AccessTokenModel>> observeAuthUser(){
        return sessionManager.getAuthUser();
    }

}
