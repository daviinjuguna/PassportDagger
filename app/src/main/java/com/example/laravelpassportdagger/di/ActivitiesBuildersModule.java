package com.example.laravelpassportdagger.di;

import com.example.laravelpassportdagger.di.api.ApiModule;
import com.example.laravelpassportdagger.di.api.AuthApiModule;
import com.example.laravelpassportdagger.di.modules.DashBoardActivityViewModelModule;
import com.example.laravelpassportdagger.di.modules.LoginUserViewModelModule;

import com.example.laravelpassportdagger.di.modules.RegisterUserViewModelModule;
import com.example.laravelpassportdagger.di.modules.SplashActivityViewModelModule;

import com.example.laravelpassportdagger.ui.main.DashBoardActivity;
import com.example.laravelpassportdagger.ui.splash.SplashActivity;
import com.example.laravelpassportdagger.ui.login.LoginActivity;
import com.example.laravelpassportdagger.ui.register.RegisterActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitiesBuildersModule {
    @ContributesAndroidInjector(
            modules = {
                    LoginUserViewModelModule.class,
                    AuthApiModule.class
            }
    )
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector(
            modules ={
                    RegisterUserViewModelModule.class,
                    AuthApiModule.class
            }
    )
    abstract RegisterActivity contributeRegisterActivity();

    @ContributesAndroidInjector(
            modules = {
                    SplashActivityViewModelModule.class,
                    AuthApiModule.class
            }
    )
    abstract SplashActivity contributeMainActivity();

    @ContributesAndroidInjector(
            modules = {
                    DashBoardActivityViewModelModule.class,
                    ApiModule.class
            }
    )
    abstract DashBoardActivity contributeDashBoardActivity();
}
