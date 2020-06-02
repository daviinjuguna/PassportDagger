package com.example.laravelpassportdagger.di;

import android.app.Application;

import com.example.laravelpassportdagger.baseviews.BaseApplication;
import com.example.laravelpassportdagger.di.api.ApiErrorConverter;
import com.example.laravelpassportdagger.di.modules.ViewModelFactoryModule;
import com.example.laravelpassportdagger.di.session.SessionManager;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivitiesBuildersModule.class,
                AppModule.class,
                ViewModelFactoryModule.class,
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    SessionManager sessionManager();
    ApiErrorConverter apiErrorConverter();

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

}