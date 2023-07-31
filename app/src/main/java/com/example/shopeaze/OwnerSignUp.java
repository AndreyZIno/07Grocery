package com.example.shopeaze;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OwnerSignUp extends Fragment {
    TextInputEditText editTextEmail, editTextPassword, editStoreName;
    Button buttonSignUp;
    TextView textView;
    ProgressBar progressBar;
    private FirebaseDatabase db;
    FirebaseAuth mAuth;


    public void onStart() {
        super.onStart();
        //new line:
        db = FirebaseDatabase.getInstance("https://grocery-d4fbb-default-rtdb.firebaseio.com//");
        //old:
        mAuth = FirebaseAuth.getInstance();
        // Check if user is already signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            NavHostFragment.findNavController(OwnerSignUp.this)
                    .navigate(R.id.action_logout_to_WelcomeScreen);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_sign_up_owner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextEmail = view.findViewById(R.id.email);
        editTextPassword = view.findViewById(R.id.password);
        editStoreName = view.findViewById(R.id.store_name);
        buttonSignUp = view.findViewById(R.id.btn_signup);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.loginNow);

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(OwnerSignUp.this)
                        .navigate(R.id.action_signUp_to_Login);
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref= db.getReference();   //new
                progressBar.setVisibility(View.VISIBLE);
                String email;
                String password;
                //read email and password:
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (password.length() < 6){
                    Toast.makeText(getActivity(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            //Read firebase realtime db to see if the store name already exists
                            //If it does, then don't create the account
                            //If it doesn't, then create the account
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    //ref.child("Users").child("Shopper").child("Email").setValue(email); //new line
                                    Toast.makeText(getActivity(), "Account Created.",
                                            Toast.LENGTH_SHORT).show();
                                    NavHostFragment.findNavController(OwnerSignUp.this)
                                            .navigate(R.id.action_signUp_to_Login);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        if (mAuth.getCurrentUser() != null) {
            //Create new store owner
            String username = mAuth.getCurrentUser().getEmail();
            String password = mAuth.getCurrentUser().getUid();
            String storeName = editStoreName.getText().toString();
            StoreOwner storeOwner = new StoreOwner(username, password, storeName);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("StoreOwner");
            databaseReference.child(mAuth.getCurrentUser().getUid()).setValue(storeOwner);
        }
    }

}
