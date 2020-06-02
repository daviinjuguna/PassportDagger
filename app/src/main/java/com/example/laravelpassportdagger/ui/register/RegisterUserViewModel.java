package com.example.laravelpassportdagger.ui.register;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.laravelpassportdagger.di.api.ApiErrorConverter;
import com.example.laravelpassportdagger.di.session.SessionManager;
import com.example.laravelpassportdagger.models.AccessTokenModel;
import com.example.laravelpassportdagger.models.ApiErrorsModel;
import com.example.laravelpassportdagger.network.API;
import com.example.laravelpassportdagger.network.AuthResource;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.schedulers.Schedulers;

import retrofit2.HttpException;

public class RegisterUserViewModel extends ViewModel {
    private static final String TAG = "RegisterUserViewModel";



    private API api;
    private ApiErrorConverter apiErrorConverter;
    private ApiErrorsModel apiErrorsModel;
    private SessionManager sessionManager;

    @Inject
   public RegisterUserViewModel(API api, ApiErrorConverter apiErrorConverter, SessionManager sessionManager) {
        this.api = api;
        this.apiErrorConverter = apiErrorConverter;
        this.sessionManager = sessionManager;
    }
    public void addUser(String name, String email, String password, String password_confirmation){
        Log.d(TAG, "addUser: Register attempt");
        sessionManager.authenticateUser(registerUser(name,email,password,password_confirmation));
    }

    private LiveData<AuthResource<AccessTokenModel>> registerUser(String name, String email, String password, String password_confirmation) {
        return LiveDataReactiveStreams.fromPublisher(
                api.register(name, email, password, password_confirmation)
                        .onErrorReturn(throwable -> {
                            AccessTokenModel errorAccessTokenModel = new AccessTokenModel();
                            errorAccessTokenModel.setAccessToken("error");

                            try {
                                HttpException error = (HttpException) throwable;
                                apiErrorsModel = apiErrorConverter.apiErrorsModel(error.response().errorBody());

                                for (Map.Entry<String, List<String>> errors : apiErrorsModel.getErrors().entrySet()) {


                                    if (errors.getKey().equals("name")) {

                                        String dname = errors.getValue().get(0);
                                        errorAccessTokenModel.setError_message(dname);
                                    }
                                    if (errors.getKey().equals("email")) {

                                        errorAccessTokenModel.setError_message("Email already exists...");

                                    }

                                    if (errors.getKey().equals("password")) {

                                        String dname = errors.getValue().get(0);
                                        errorAccessTokenModel.setError_message(dname);

                                    }

                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                errorAccessTokenModel.setError_message("Check your internet connection...");
                            }
                            return errorAccessTokenModel;
                        })
                        .map(accessTokenModel -> {
                            if (accessTokenModel.getAccessToken().equals("error")){
                                return AuthResource.error(accessTokenModel.getError_message(),(AccessTokenModel)null);
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
