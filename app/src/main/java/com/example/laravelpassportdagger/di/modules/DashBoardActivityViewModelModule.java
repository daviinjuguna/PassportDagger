package com.example.laravelpassportdagger.di.modules;

import androidx.lifecycle.ViewModel;

import com.example.laravelpassportdagger.di.scopes.ViewModelKey;
import com.example.laravelpassportdagger.ui.main.DashBoardActivityViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class DashBoardActivityViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DashBoardActivityViewModel.class)
    public abstract ViewModel bindViewModel(DashBoardActivityViewModel dashBoardActivityViewModel);
}
