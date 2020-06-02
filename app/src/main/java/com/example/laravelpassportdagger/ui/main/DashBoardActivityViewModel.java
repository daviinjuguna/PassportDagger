package com.example.laravelpassportdagger.ui.main;

import androidx.lifecycle.ViewModel;

import com.example.laravelpassportdagger.di.api.ApiErrorConverter;
import com.example.laravelpassportdagger.network.API;

import javax.inject.Inject;

public class DashBoardActivityViewModel extends ViewModel {
    private API api;
    private ApiErrorConverter apiErrorConverter;

    @Inject
    DashBoardActivityViewModel(API api, ApiErrorConverter apiErrorConverter){
        this.api = api;
        this.apiErrorConverter = apiErrorConverter;
    }
}
