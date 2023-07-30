package com.example.shopeaze;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoreList {

    private DatabaseReference databaseReference;
    private List<Store> stores;

    public StoreList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("stores");
        stores = new ArrayList<>();
        loadStoresFromFirebase();
    }

    // adding store handled by storeowner account signup and removing store handled by storeowner account deletion

    public List<Store> getAllStores() {
        return stores;
    }

    private void loadStoresFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stores.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Store store = snapshot.getValue(Store.class);
                    stores.add(store);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error loading stores from Firebase: " + error.getMessage());
            }
        });
    }

    public Store getStoreByID(String storeID) throws AppExceptions.StoreNotFoundException {
        for (Store store : stores) {
            if (store.getStoreID().equals(storeID)) {
                return store;
            }
        }

        // if the store is not found
        throw new AppExceptions.StoreNotFoundException("Store with ID " + storeID + " not found.");
    }

}
