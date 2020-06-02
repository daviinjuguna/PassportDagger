package com.example.laravelpassportdagger.di.modules;

import androidx.lifecycle.ViewModel;

import com.example.laravelpassportdagger.di.scopes.ViewModelKey;
import com.example.laravelpassportdagger.ui.login.LoginUserViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class LoginUserViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginUserViewModel.class)
    public abstract ViewModel bindViewModel(LoginUserViewModel loginUserViewModel);
}
