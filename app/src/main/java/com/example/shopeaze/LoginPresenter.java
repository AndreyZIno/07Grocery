package com.example.shopeaze;

import android.text.TextUtils;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.Model model;
    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        model = new LoginModel();
    }
    @Override
    public void loginUser() {
        String email1 = view.getEmail();
        String pass = view.getPassword();
        if (TextUtils.isEmpty(email1)) {
            view.showErrorMessage("Please enter email");
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            view.showErrorMessage("Please enter password");
            return;
        }

        view.showProgressBar();
        model.loginUser(email1, pass, new LoginModel.OnLoginFinishedListener() {
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
