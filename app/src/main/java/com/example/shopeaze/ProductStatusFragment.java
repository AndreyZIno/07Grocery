package com.example.shopeaze;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductStatusFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    //We have to pull the current orders associated with the StoreName from the database
    //So get current user reference from database
    public void onStart() {
        super.onStart();
        //Should be a owner
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference ref= db.getReference();
    }


}
