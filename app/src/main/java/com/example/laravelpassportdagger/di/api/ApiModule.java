package com.example.laravelpassportdagger.di.api;

import com.example.laravelpassportdagger.network.API;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApiModule {

    @Provides
    static API provideAPI(Retrofit retrofit){


        return retrofit.create(API.class);

    }
}
