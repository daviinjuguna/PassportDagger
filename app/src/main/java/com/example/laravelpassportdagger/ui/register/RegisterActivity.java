package com.example.laravelpassportdagger.ui.register;

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
import com.example.laravelpassportdagger.ui.login.LoginActivity;
import com.example.laravelpassportdagger.ui.main.DashBoardActivity;
import com.example.laravelpassportdagger.utils.TokenManager;
import com.example.laravelpassportdagger.utils.ViewSnackBar;
import com.google.android.material.textfield.TextInputLayout;
import com.pkmmte.view.CircularImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";

    @BindView(R.id.register_logo)
    CircularImageView register_logo;
    @BindView(R.id.txtLayoutNameSignUp)
    TextInputLayout txtName;
    @BindView(R.id.txtLayoutEmailSignUp)
    TextInputLayout txtEmail;
    @BindView(R.id.txtLayoutPasswordSignUp)
    TextInputLayout txtPassword;
    @BindView(R.id.txtConfirmLayoutPasswordSignUp)
    TextInputLayout txtConfirmPassword;

    @Inject
    Drawable user;
    @Inject
    RequestManager requestManager;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    TokenManager tokenManager;

    private RegisterUserViewModel registerUserViewModel;
    private ProgressDialog progressDialog;
    private ViewSnackBar viewSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        //setLogo();

        registerUserViewModel = ViewModelProviders.of(this,providerFactory).get(RegisterUserViewModel.class);

        View myView = findViewById(android.R.id.content);
        viewSnackBar = new ViewSnackBar(myView, this);

        subscribeObservers();
    }

    private void subscribeObservers() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing Up");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        registerUserViewModel.observeAuthUser().observe(this,accessTokenModelAuthResource -> {
            if (accessTokenModelAuthResource !=null){
                switch (accessTokenModelAuthResource.status){
                    case LOADING:
                        progressDialog.show();
                        break;

                    case AUTHENTICATED:
                        progressDialog.hide();
                        assert accessTokenModelAuthResource.data != null;
                        if (accessTokenModelAuthResource.data.getTokenType().equals("Bearer")){
                            tokenManager.saveAcessToken(accessTokenModelAuthResource.data);

                            Intent intent = new Intent(RegisterActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        break;

                    case ERROR:
                        progressDialog.dismiss();
                        Log.d(TAG, "subscribeObservers: "+accessTokenModelAuthResource.message);
                        viewSnackBar.viewMySnack(accessTokenModelAuthResource.message, R.color.colorPrimary);
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
                .into(register_logo);
    }

    private boolean validate() {
        boolean valid = true;

        String name = txtName.getEditText().getText().toString();
        String email = txtEmail.getEditText().getText().toString();
        String password = txtPassword.getEditText().getText().toString();
        String confirmPassword = txtConfirmPassword.getEditText().getText().toString();

        if (name.isEmpty()){
            txtName.setError("enter a valid name");
        }else {
            txtName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("enter a valid email address");
            valid = false;
        } else {
            txtEmail.setError(null);
        }

        if (password.length() < 6 || password.length() > 20) {
            txtPassword.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            txtPassword.setError(null);
        }

        if (!password.equals(confirmPassword)) {

            txtPassword.setError("Passwords do not match");
            txtConfirmPassword.setError("Passwords do not match");
            valid = false;

        } else {

            txtPassword.setError(null);
            txtConfirmPassword.setError(null);

        }

        return valid;

    }

    @OnClick(R.id.txtSignIn)
   public void login(){
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    @OnClick(R.id.btnSignUp)
    public void register(){
        if (!validate()) {

            viewSnackBar.viewMySnack("Enter all fields", R.color.colorPrimary);
            return;
        }

        String name = txtName.getEditText().getText().toString();
        String email = txtEmail.getEditText().getText().toString();
        String password = txtPassword.getEditText().getText().toString();
        String confirmPassword = txtConfirmPassword.getEditText().getText().toString();

        registerUserViewModel.addUser(name,email,password, confirmPassword);
    }

}