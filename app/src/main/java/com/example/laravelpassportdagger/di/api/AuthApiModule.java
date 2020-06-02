package com.example.laravelpassportdagger.di.api;


import android.util.Log;

import com.example.laravelpassportdagger.network.API;
import com.example.laravelpassportdagger.network.CustomAuthenticator;
import com.example.laravelpassportdagger.utils.TokenManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;

@Module
public class AuthApiModule {

    private static final String TAG = "AuthApiModule";

    @Provides
    static API createAuthAPI(Retrofit retrofit, OkHttpClient client, TokenManager tokenManager){

        OkHttpClient newClient = client.newBuilder().addInterceptor(chain -> {

            Request request = chain.request();

            Request.Builder builder = request.newBuilder();


            if(tokenManager.getToken().getAccessToken() != null){
                builder.addHeader("Authorization", "Bearer " + tokenManager.getToken().getAccessToken());
            }else if(tokenManager.getToken().getAccessToken() == null){

                Log.d(TAG, "createAuthAPI: it is null");

            }
            request = builder.build();
            return chain.proceed(request);
        }).authenticator(CustomAuthenticator.getInstance(tokenManager, retrofit)).build();

        Retrofit newRetrofit = retrofit.newBuilder().client(newClient).build();
        return newRetrofit.create(API.class);

    }

}
