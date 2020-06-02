package com.example.laravelpassportdagger.baseviews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import com.example.laravelpassportdagger.R;
import com.example.laravelpassportdagger.utils.ConnectivityReceiver;
import com.google.android.material.snackbar.Snackbar;

import dagger.android.support.DaggerAppCompatActivity;

public class BaseActivity extends DaggerAppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistableState) {
        super.onCreate(savedInstanceState, persistableState);

        BaseApplication.getBaseApplication(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Looks like you're Not connected to internet";
            color = Color.YELLOW;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

}