package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.shopeaze.databinding.FragmentWelcomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class WelcomeScreen extends Fragment {

    private FragmentWelcomeBinding binding;
    FirebaseAuth mAuth;
    private FirebaseDatabase db;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button shopperButton = view.findViewById(R.id.button_shopper);
        Button ownerButton = view.findViewById(R.id.button_owner);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://grocery-d4fbb-default-rtdb.firebaseio.com//");
        if(mAuth.getCurrentUser() != null){
            //Check if this user is a shopper or owner
            Query query = db.getReference("Users").child("Shoppers").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){ //Shopper
                        Toast.makeText(getActivity(), "Welcome back, shopper!", Toast.LENGTH_SHORT);
                        shopperButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                NavHostFragment.findNavController(WelcomeScreen.this)
                                        .navigate(R.id.action_WelcomeScreen_to_Login);
                            }
                        });
                        ownerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getActivity(), "You are not an owner!", Toast.LENGTH_SHORT);
                            }
                        });


                    }
                    else{ //Owner
                        Toast.makeText(getActivity(), "Welcome back, owner!", Toast.LENGTH_SHORT);
                        shopperButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            Toast.makeText(getActivity(), "You are not a shopper!", Toast.LENGTH_SHORT);
                            }
                        });
                        ownerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                NavHostFragment.findNavController(WelcomeScreen.this)
                                        .navigate(R.id.action_WelcomeScreen_to_ownerLogin);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            shopperButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(WelcomeScreen.this)
                            .navigate(R.id.action_WelcomeScreen_to_Login);
                }
            });
            ownerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(WelcomeScreen.this)
                            .navigate(R.id.action_WelcomeScreen_to_ownerLogin);
                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}