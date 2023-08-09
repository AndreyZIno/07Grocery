package com.example.shopeaze;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class OwnerOrders extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    // Declare a ListView to display the orders and a Button for refreshing the orders.
    private ListView ownerordersListView;
    private Button refreshButton;


    // Declare an ArrayAdapter to handle the list of orders.
    private ArrayAdapter<Order> ownerordersAdapter;


    // Declare a List to hold the orders data.
    private List<String> ownerordersList;
    Query ordersCollection;
    DatabaseReference ref;
    List<Product> productList;


    List<Order> orderList;
    String storeName;
    String shopperEmail;
    Order order;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_orders, container, false);
        ref = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        orderList = new ArrayList<>();
        ownerordersListView = view.findViewById(R.id.ownerordersListView);
        loadOrders();

        ImageButton inventoryButton = view.findViewById(R.id.button_stores);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(OwnerOrders.this);
                navController.navigate(R.id.action_owner_orders_to_product_list);
            }
        });

        refreshButton = view.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
            refreshButton.startAnimation(animation);
            loadOrders();
        });

        return view;
    }

    private void loadOrders(){
        String userId = mAuth.getUid();
        DatabaseReference storeRef = ref.child("Users").child("StoreOwner").child(userId);
        com.google.firebase.database.Query storeNameRef = storeRef.child("StoreName");
        storeNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storeName = snapshot.getValue(String.class);
                Log.d("StoreName", storeName);
                com.google.firebase.database.Query ordersRef = storeRef.child("Orders");
                ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot orderSnapshot : snapshot.getChildren()){
                            Log.d("OrderID", orderSnapshot.getKey());
                            String orderID = orderSnapshot.getKey();
                            String shopperEmail = orderSnapshot.child("Shopper Email").getValue(String.class);
                            productList = new ArrayList<>();
                            for(DataSnapshot productSnapshot : orderSnapshot.getChildren()){
                                String storeId = productSnapshot.child("storeID").getValue(String.class);
                                String brand = productSnapshot.child("cartProductBrand").getValue(String.class);
                                String cartproductID = productSnapshot.child("cartProductID").getValue(String.class);
                                String name = productSnapshot.child("cartProductName").getValue(String.class);
                                int quantity = productSnapshot.child("cartQuantity").getValue(Integer.class);
                                String status = productSnapshot.child("status").getValue(String.class);
                                double price = productSnapshot.child("cartProductPrice").getValue(Double.class);
                                String image = null;
                                Product product = new Product(name, brand, price, null, quantity, status, null, storeId, cartproductID);
                                productList.add(product);

                            }
                            if(productList.size() > 0){
                                orderList.add(new Order(shopperEmail, null, productList));
                            }
                            printOrderList();
                        }
                        displayProducts();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }










    private void printOrderList(){
        Log.d("OrderList", orderList.toString());
    }


    private void displayProducts() {
        // Create a custom adapter to display the list of orders and their products
        ownerordersAdapter = new ArrayAdapter<Order>(getContext(), android.R.layout.simple_list_item_1, orderList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                }
                // Get the order at the current position
                Order order = getItem(position);
                // Display order information
                TextView orderTextView = convertView.findViewById(android.R.id.text1);
                orderTextView.setText("Status: " + order.getStatus());
                return convertView;
            }
        };


        // Set the custom adapter to the ListView
        ownerordersListView.setAdapter(ownerordersAdapter);


        // Set item click listener to show detailed product information when an order is clicked
        ownerordersListView.setOnItemClickListener((parent, view, position, id) -> {
            Order selectedOrder = ownerordersAdapter.getItem(position);
            showProductDetails(selectedOrder);
        });
    }


    private void showProductDetails(Order order) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_order_product_details);


        TextView productTitleTextView = dialog.findViewById(R.id.productTitleTextView);
        TextView productDetailsTextView = dialog.findViewById(R.id.productDetailsTextView);


        productTitleTextView.setText("Order Details for " + order.getOrderNumber());


        StringBuilder detailsBuilder = new StringBuilder();
        for (Product product : order.getProducts()) {
            detailsBuilder.append("Product: ").append(product.getName())
                    .append("\nBrand: ").append(product.getBrand())
                    .append("\nPrice: ").append(product.getPrice())
                    .append("\nDescription: ").append(product.getDescription())
                    .append("\nQuantity: ").append(product.getQuantity())
                    .append("\n\n");
        }


        productDetailsTextView.setText(detailsBuilder.toString());


        dialog.show();
    }








    //Create an example  order




    private void createExampleOrder(){
        Log.d("Create Order", "Creating Order");
        //Go to a specific Shopper and then push to
        String userID = mAuth.getUid();
        DatabaseReference shopperRef = ref.child("Users").child("StoreOwner").child(userID);
        DatabaseReference orderRef = shopperRef.child("Orders");
        DatabaseReference newOrderRef = orderRef.push();
        String orderID = newOrderRef.getKey();
        DatabaseReference statusRef = newOrderRef.child("Status");
        //User id
        DatabaseReference shopperEmailRef = newOrderRef.child("User ID");
        shopperEmailRef.setValue(userID);
        statusRef.setValue("Pending");
        DatabaseReference itemsRef = newOrderRef.child("Items");
        DatabaseReference newItemRef = itemsRef.push();
        DatabaseReference nameRef = newItemRef.child("Name");
        nameRef.setValue("Banana");
        DatabaseReference brandRef = newItemRef.child("Brand");
        brandRef.setValue("Banana Republic");
        DatabaseReference priceRef = newItemRef.child("Price");
        priceRef.setValue(1.99);
        DatabaseReference descriptionRef = newItemRef.child("Description");
        descriptionRef.setValue("A delicious banana");
        DatabaseReference quantityRef = newItemRef.child("Quantity");
        quantityRef.setValue(1);
        DatabaseReference storeNameRef = newItemRef.child("Store Name");
        storeNameRef.setValue("Banana Republic");
    }

}

