package com.example.laravelpassportdagger.network;

import com.example.laravelpassportdagger.models.AccessTokenModel;


import io.reactivex.rxjava3.core.Flowable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

import retrofit2.http.POST;

public interface API {

    @POST("login")
    @FormUrlEncoded
    Flowable<AccessTokenModel> login(
            @Field("username") String email,
            @Field("password") String password
    );

    @POST("register")
    @FormUrlEncoded
    Flowable<AccessTokenModel> register(
            @Field("name")String name,
            @Field("email")String email,
            @Field("password")String password,
            @Field("password_confirmation")String password_confirmation
    );

    @POST("refresh")
    @FormUrlEncoded
    Call<AccessTokenModel> refresh(
            @Field("refresh_token") String refreshToken
    );

    @POST("logout")
    @FormUrlEncoded
    Call<AccessTokenModel> logout();
}
