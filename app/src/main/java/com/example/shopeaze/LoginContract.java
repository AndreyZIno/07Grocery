package com.example.shopeaze;

public interface LoginContract {

    public interface Model{
        public void loginUser(String email, String password, final com.example.shopeaze.LoginModel.OnLoginFinishedListener listener);
    }
    public interface View {
        public void showProgressBar();
        public void hideProgressBar();
        public void showErrorMessage(String message);
        public void showLoginSuccessMessage();
        public void navigateToStoreList();
    }

    public interface Presenter {
        public void loginUser(String email, String password);
    }
}
