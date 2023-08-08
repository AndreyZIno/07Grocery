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
    public void loginUser(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            view.showErrorMessage("Please enter email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            view.showErrorMessage("Please enter password");
            return;
        }

        view.showProgressBar();
        model.loginUser(email, password, new LoginModel.OnLoginFinishedListener() {
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
