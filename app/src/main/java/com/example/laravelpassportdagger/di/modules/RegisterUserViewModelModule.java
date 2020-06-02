package com.example.laravelpassportdagger.di.modules;

import androidx.lifecycle.ViewModel;

import com.example.laravelpassportdagger.di.scopes.ViewModelKey;

import com.example.laravelpassportdagger.ui.register.RegisterUserViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class RegisterUserViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RegisterUserViewModel.class)
    public abstract ViewModel bindViewModel(RegisterUserViewModel registerUserViewModel);
}
