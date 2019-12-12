package com.jo.android.smartrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jo.android.smartrestaurant.data.UserData;
import com.jo.android.smartrestaurant.model.Item;
import com.jo.android.smartrestaurant.model.ItemInCart;
import com.jo.android.smartrestaurant.viewholders.CartItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {


    public static final String HENT_TO_DESPLAY_CATIGORY = "desplay";
    public static final String SUBTATEL_KEY ="subtotal" ;
    private RecyclerView recyclerViewCart;
    private DatabaseReference cartListReference;
    private DatabaseReference menuReference;
    private List<ItemInCart> itemInCartList;
    private TextView textViewSubTotal,textViewRestaurantName;
    private Button buttonCheckOut, buttonAddItems;


    private long subtotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerViewCart = findViewById(R.id.rv_cart);
        buttonCheckOut = findViewById(R.id.button_checkout);
        buttonAddItems = findViewById(R.id.button_add_new_item);
        textViewRestaurantName = findViewById(R.id.tv_restaurant_name_in_cart);
        if(!UserData.RESTAURANT_ID.equals("")&&UserData.RESTAURANT_ID!=null) {

            textViewRestaurantName.setText(UserData.RESTAURANT_NAME);
            cartListReference = FirebaseDatabase.getInstance().getReference().child("user_cart")
                    .child(UserData.USER_ID);
            menuReference = FirebaseDatabase.getInstance().getReference().child("menus").child(UserData.RESTAURANT_ID);

            itemInCartList = new ArrayList<>();
            final CartAdapter adapter = new CartAdapter(itemInCartList);
            recyclerViewCart.setHasFixedSize(true);
            recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

            cartListReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int i = 0;
                    itemInCartList.clear();
                    for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                        ItemInCart itemInCart = itemSnapShot.getValue(ItemInCart.class);
                        itemInCartList.add(itemInCart);


                    }
                    getSubTotalPrice();
                    adapter.setItemInCartList(itemInCartList);
                    recyclerViewCart.setAdapter(adapter);



                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        buttonAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToHomeActvity();
            }
        });


        buttonCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToConfirmActivity();

            }
        });


    }

    private void getSubTotalPrice() {
        for(final ItemInCart itemInCart:itemInCartList){
            String whichPart = itemInCart.getCategory();
            final String itemId = itemInCart.getItemId();
            menuReference.child(whichPart).child(itemId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Item menuItem = dataSnapshot.getValue(Item.class);
                    int quantity=itemInCart.getQuantity();
                    long itemPrice=menuItem.getPrice();

                    subtotal=subtotal+quantity*itemPrice;


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }

    private void sendToConfirmActivity() {
        Intent intent = new Intent(CartActivity.this, ConfirmActivity.class);
        intent.putExtra(SUBTATEL_KEY,subtotal);
        startActivity(intent);
    }

    private void sendToHomeActvity() {
        Intent intent = new Intent(CartActivity.this, UserHomeActivity.class);
        intent.putExtra(HENT_TO_DESPLAY_CATIGORY, "desplay");
        startActivity(intent);
    }

    private class CartAdapter extends RecyclerView.Adapter<CartItemViewHolder> {

        List<ItemInCart> itemInCartList;

        public void setItemInCartList(List<ItemInCart> itemInCartList) {
            this.itemInCartList = itemInCartList;
        }

        public CartAdapter(List<ItemInCart> itemInCartList) {
            this.itemInCartList = itemInCartList;
        }

        @NonNull
        @Override
        public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.cart_item, parent, false);
            CartItemViewHolder viewHolder = new CartItemViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final CartItemViewHolder holder, int position) {
            final ItemInCart itemInCart = itemInCartList.get(position);
            String whichPart = itemInCart.getCategory();
            String itemId = itemInCart.getItemId();
            menuReference.child(whichPart).child(itemId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Item menuItem = dataSnapshot.getValue(Item.class);

                    if (menuItem != null) {
                        holder.textViewItemName.setText(menuItem.getName());
                        holder.textViewItemDescription.setText(menuItem.getDescription());
                        long price = menuItem.getPrice();
                        holder.textViewItemPrice.setText("EGP " + price);
                        int quantity = itemInCart.getQuantity();
                        holder.textViewQuantity.setText(quantity + "");
                        long itemTotalPrice = quantity * price;
                        holder.textViewTotalPrice.setText("EGP " + itemTotalPrice);



                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        @Override
        public int getItemCount() {
            return itemInCartList.size();
        }
    }

}

