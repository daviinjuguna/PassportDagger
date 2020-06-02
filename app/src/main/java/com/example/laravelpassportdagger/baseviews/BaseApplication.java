package com.example.laravelpassportdagger.baseviews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.laravelpassportdagger.di.DaggerAppComponent;
import com.example.laravelpassportdagger.utils.TokenManager;
import com.facebook.stetho.Stetho;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class BaseApplication extends DaggerApplication {

    public static Context context;
    public static BaseApplication instance;

    public static BaseApplication getBaseApplication(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        instance = this;

        EmojiManager.install(new IosEmojiProvider());
        Stetho.initializeWithDefaults(this);
    }

    public static Context getContext() {
        return context;
    }

    public static synchronized BaseApplication getInstance() {
        return instance;
    }

    public static boolean hasNetwork(){
        return instance.isConnected();
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public TokenManager tokenManager(){

        return TokenManager.getINSTANCE(getSharedPreferences("prefs",MODE_PRIVATE));

    }
}
