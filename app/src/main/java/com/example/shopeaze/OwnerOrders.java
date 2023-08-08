package com.example.shopeaze;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
    private DatabaseReference ref;
    private List<Product> productList;
    private List<Order> orderList;
    private String storeName;
    private RecyclerView ownerordersRecyclerView;
    private OwnerOrdersAdapter ownerordersAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_orders, container, false);

        ImageButton inventoryButton = view.findViewById(R.id.button_inventory);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(OwnerOrders.this);
                navController.navigate(R.id.action_OwnerOrders_to_ProductList);
            }
        });

        ImageButton ordersButton = view.findViewById(R.id.button_orders);
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(OwnerOrders.this);
                NavDestination currentDestination = navController.getCurrentDestination();
                if (currentDestination != null && currentDestination.getId() == R.id.OwnerOrders) {
                    // User is already on OwnerOrders fragment, do nothing
                    return;
                }
            }
        });

        ref = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        orderList = new ArrayList<>();
        ownerordersRecyclerView = view.findViewById(R.id.ownerordersRecyclerView);
        ownerordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadOrders();
        return view;
    }




    private void loadOrders() {
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
                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            String orderID = orderSnapshot.getKey();
                            String status = orderSnapshot.child("Status").getValue(String.class);
                            String shopperEmail = orderSnapshot.child("Shopper Email").getValue(String.class);
                            productList = new ArrayList<>();

                            for (DataSnapshot productSnapshot : orderSnapshot.getChildren()) {
                                String name = productSnapshot.child("cartProductName").getValue(String.class);
                                String brand = productSnapshot.child("cartProductBrand").getValue(String.class);
                                double price = productSnapshot.child("cartProductPrice").getValue(Double.class);
                                String description = productSnapshot.child("cartProductDescription").getValue(String.class);
                                int quantity = productSnapshot.child("cartQuantity").getValue(Integer.class);
                                Product product = new Product(name, brand, price, description, quantity, null, status, userId);
                                productList.add(product);

                            }
                            if (productList.size() > 0) {
                                orderList.add(new Order(shopperEmail, status, productList, orderID));
                            }
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
        ownerordersAdapter = new OwnerOrdersAdapter(orderList);
        ownerordersRecyclerView.setAdapter(ownerordersAdapter);
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
        storeNameRef.setValue("Hello Fresh");
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = NavHostFragment.findNavController(this);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                ImageButton inventoryButton = view.findViewById(R.id.button_inventory);
                ImageButton ordersButton = view.findViewById(R.id.button_orders);
                TextView inventoryText = view.findViewById(R.id.textViewInventoryText);
                TextView ordersText = view.findViewById(R.id.textViewOrderText);
                ImageView inventoryIcon = view.findViewById(R.id.inventoryIcon);
                ImageView ordersIcon = view.findViewById(R.id.orderIcon);
                if (destination.getId() == R.id.ProductList) {
                    inventoryButton.setImageResource(R.drawable.focused_nav_button);
                    ordersButton.setImageResource(R.drawable.nav_gradient);
                    inventoryText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    ordersText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    inventoryIcon.setImageResource(R.drawable.black_store);
                    ordersIcon.setImageResource(R.drawable.white_orders);
                } else if (destination.getId() == R.id.OwnerOrders) {
                    inventoryButton.setImageResource(R.drawable.nav_gradient);
                    ordersButton.setImageResource(R.drawable.focused_nav_button);
                    inventoryText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    ordersText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    inventoryIcon.setImageResource(R.drawable.white_store);
                    ordersIcon.setImageResource(R.drawable.black_orders);
                }
            }
        });
    }

}

