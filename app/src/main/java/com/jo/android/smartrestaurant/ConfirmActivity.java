package com.jo.android.smartrestaurant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jo.android.smartrestaurant.data.UserData;
import com.jo.android.smartrestaurant.model.ItemInCart;

public class ConfirmActivity extends AppCompatActivity {

    private TextView textViewSubtotal,textViewService,textViewTotalAmount;
    private RadioGroup radioGroup;
    private RadioButton radioButtonCridet,radioButtonCash;
    private Button buttonConfirm;

    private DatabaseReference tableRef,userOrdersRef,userCartRef;

    public static final int CASH=1;
    public static final int CRIDET=2;



    private long subtotal;
    private long service;
    private  long totalAmount;
    private  int payWay=CASH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        subtotal=getIntent().getLongExtra(CartActivity.SUBTATEL_KEY,0);
        service=subtotal/10;

        tableRef = FirebaseDatabase.getInstance().getReference().child("tables").child(UserData.RESTAURANT_ID).child(""+UserData.TABLE_NUMBER);
        userOrdersRef=FirebaseDatabase.getInstance().getReference().child("user_orders").child(UserData.USER_ID).child(UserData.RESTAURANT_ID);
        userCartRef=FirebaseDatabase.getInstance().getReference().child("user_cart")
                .child(UserData.USER_ID);
        totalAmount=subtotal+service;

        textViewService=findViewById(R.id.tv_service);
        textViewSubtotal=findViewById(R.id.tv_subtotal_confirmation);
        textViewTotalAmount=findViewById(R.id.tv_total_amount);
        radioButtonCash=findViewById(R.id.radio_btn_cash);
        radioButtonCridet=findViewById(R.id.radio_btn_credit);
        buttonConfirm=findViewById(R.id.button_confirm);

        textViewService.setText(service+"");
        textViewSubtotal.setText(subtotal+"");
        textViewTotalAmount.setText(totalAmount+"");
        radioButtonCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payWay=CASH;
            }
        });
///25252
        radioButtonCridet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payWay=CRIDET;
            }
        });
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmOrders();
            }
        });


    }

    private void confirmOrders() {

        savePayWay();
        storeOrders();
        deleteFromCart();

        Toast.makeText(this, "the pay way :"+payWay, Toast.LENGTH_SHORT).show();
    }

    private void deleteFromCart() {
        userCartRef.removeValue();
    }

    private void storeOrders() {
        userCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    ItemInCart itemInCart=dataSnapshot1.getValue(ItemInCart.class);
                    String id=itemInCart.getId();
                    userOrdersRef.child(id).setValue(itemInCart);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void savePayWay() {
        tableRef.child("pay_way").setValue(payWay);
    }


}
