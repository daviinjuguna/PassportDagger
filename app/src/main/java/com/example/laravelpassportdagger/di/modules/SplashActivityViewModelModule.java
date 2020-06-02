package com.example.laravelpassportdagger.di.modules;

import androidx.lifecycle.ViewModel;

import com.example.laravelpassportdagger.di.scopes.ViewModelKey;
import com.example.laravelpassportdagger.ui.login.LoginUserViewModel;
import com.example.laravelpassportdagger.ui.splash.SplashActivityViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SplashActivityViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SplashActivityViewModel.class)
    public abstract ViewModel bindViewModel(SplashActivityViewModel splashActivityViewModel);
}
