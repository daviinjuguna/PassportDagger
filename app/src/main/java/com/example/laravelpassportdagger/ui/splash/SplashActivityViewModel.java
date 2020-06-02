package com.example.laravelpassportdagger.ui.splash;

import androidx.lifecycle.ViewModel;

import com.example.laravelpassportdagger.di.api.ApiErrorConverter;
import com.example.laravelpassportdagger.network.API;

import javax.inject.Inject;

public class SplashActivityViewModel extends ViewModel {
    private API api;
    private ApiErrorConverter apiErrorConverter;

    @Inject
    public SplashActivityViewModel(API api, ApiErrorConverter apiErrorConverter) {
        this.api = api;
        this.apiErrorConverter = apiErrorConverter;
    }
}
