package com.example.shopeaze;

import android.text.TextUtils;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.Model model;
    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view, LoginContract.Model model) {
        this.view = view;
        this.model = model;
    }
    @Override
    public void loginUser() {
        String email = view.getEmail();
        if (email.equals("")) {
            view.showErrorMessage("Please enter email");
            return;
        }

        String pass = view.getPassword();
        if (pass.equals("") || TextUtils.isEmpty(pass)) {
            view.showErrorMessage("Please enter password");
            return;
        }

        view.showProgressBar();
        model.loginUser(email, pass, new LoginModel.OnLoginFinishedListener() {
            @Override
            public void onLoginSuccess() {
                view.hideProgressBar();
                view.showLoginSuccessMessage();
                view.navigateToStoreList();
            }
            @Override
            public void onLoginFailure() {
                view.hideProgressBar();
                view.showErrorMessage("Authentication failed");
            }
        });
    }
}
