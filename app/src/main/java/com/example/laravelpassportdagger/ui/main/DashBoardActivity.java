package com.example.laravelpassportdagger.ui.main;

import android.os.Bundle;

import com.example.laravelpassportdagger.R;
import com.example.laravelpassportdagger.baseviews.BaseActivity;

import butterknife.ButterKnife;

public class DashBoardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
    }
}