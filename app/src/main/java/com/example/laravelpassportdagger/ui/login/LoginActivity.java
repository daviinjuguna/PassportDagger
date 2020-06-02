package com.example.laravelpassportdagger.ui.login;

import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.RequestManager;
import com.example.laravelpassportdagger.R;
import com.example.laravelpassportdagger.baseviews.BaseActivity;
import com.example.laravelpassportdagger.di.ViewModelProviderFactory;
import com.example.laravelpassportdagger.ui.main.DashBoardActivity;
import com.example.laravelpassportdagger.ui.register.RegisterActivity;
import com.example.laravelpassportdagger.utils.TokenManager;
import com.example.laravelpassportdagger.utils.ViewSnackBar;
import com.google.android.material.textfield.TextInputLayout;
import com.pkmmte.view.CircularImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.login_logo)
    CircularImageView login_logo;
    @BindView(R.id.txtLayoutEmailSignIn)
    TextInputLayout txtEmail;
    @BindView(R.id.txtLayoutPasswordSignIn)
    TextInputLayout txtPassword;

    @Inject
    Drawable user;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    TokenManager tokenManager;
    @Inject
    RequestManager requestManager;

    private LoginUserViewModel loginUserViewModel;
    private ProgressDialog progressDialog;
    private ViewSnackBar viewSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        //setLogo(); hii imenishinda;

        loginUserViewModel = ViewModelProviders.of(this,providerFactory).get(LoginUserViewModel.class);

        View myView = findViewById(android.R.id.content);
        viewSnackBar = new ViewSnackBar(myView, this);

        subscribeObservers();
    }

    private void subscribeObservers() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        loginUserViewModel.observeAuthUser().observe(this,accessTokenModelAuthResource -> {
            if (accessTokenModelAuthResource != null){
                switch (accessTokenModelAuthResource.status){
                    case LOADING:
                        progressDialog.show();
                        break;

                    case AUTHENTICATED:
                        progressDialog.dismiss();
                        if (accessTokenModelAuthResource.data !=null){
                            tokenManager.saveAcessToken(accessTokenModelAuthResource.data);

                            Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        break;

                    case ERROR:
                        progressDialog.dismiss();
                        Log.d(TAG, "subscribeObservers: "+accessTokenModelAuthResource.message);
                        break;

                    case NOT_AUTHENTICATED:
                        progressDialog.dismiss();
                        break;
                }
            }
        });

    }

    private void setLogo(){
        requestManager
                .load(user)
                .into(login_logo);
    }

    private boolean validate() {
        boolean valid = true;

        String email = txtEmail.getEditText().getText().toString();
        String password = txtPassword.getEditText().getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("enter a valid email address");
            valid = false;
        } else {
            txtEmail.setError(null);
        }

        if ( password.length() < 3 || password.length() > 20) {
            txtPassword.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            txtPassword.setError(null);
        }

        return valid;

    }

    @OnClick(R.id.txtSignUp)
    public void register(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @OnClick(R.id.btnSignIn)
    public void login(){
        if (!validate()) {

            viewSnackBar.viewMySnack("Enter all fields", R.color.colorPrimary);
            return;
        }
        String email = txtEmail.getEditText().getText().toString();
        String password = txtPassword.getEditText().getText().toString();

        loginUserViewModel.authenticateUser(email,password);

    }
}