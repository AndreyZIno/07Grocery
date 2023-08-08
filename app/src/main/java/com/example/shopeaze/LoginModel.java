package com.example.shopeaze;

import com.google.firebase.auth.FirebaseAuth;

public class LoginModel implements LoginContract.Model{
    private FirebaseAuth mAuth;
    public LoginModel() {
        mAuth = FirebaseAuth.getInstance();
    }
    public void loginUser(String email, String password, final OnLoginFinishedListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onLoginSuccess();
                    } else {
                        listener.onLoginFailure();
                    }
                });
    }

    public interface OnLoginFinishedListener {
        void onLoginSuccess();
        void onLoginFailure();
    }
}
