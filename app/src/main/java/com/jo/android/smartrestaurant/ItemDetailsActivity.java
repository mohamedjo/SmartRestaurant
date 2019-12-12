package com.jo.android.smartrestaurant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jo.android.smartrestaurant.data.UserData;
import com.jo.android.smartrestaurant.model.Item;
import com.jo.android.smartrestaurant.model.ItemInCart;

import static com.jo.android.smartrestaurant.MenuActivity.ITEM_ID_KEY;
import static com.jo.android.smartrestaurant.UserHomeActivity.CATEGORY_TITTLE_KEY;
import static com.jo.android.smartrestaurant.UserHomeActivity.RESTAURANT_ID_KEY;

public class ItemDetailsActivity extends AppCompatActivity {

    private TextView textViewName,textViewDescription,textViewPrice,textViewQuntity;
    private DatabaseReference itemReference,cartListReference,tableReference;
    private ImageView imageViewCose;
    private Button buttonDec,buttoninc, buttonAddToCart;
    int quntity=1;
    private String restaurantId;
    private String whichPart;
    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        getSupportActionBar().hide();
         itemId=getIntent().getStringExtra(ITEM_ID_KEY);
         restaurantId=getIntent().getStringExtra(RESTAURANT_ID_KEY);
         whichPart=getIntent().getStringExtra(CATEGORY_TITTLE_KEY);

        textViewName=findViewById(R.id.tv_item_name_details);
        textViewDescription=findViewById(R.id.tv_item_description_details);
        textViewPrice=findViewById(R.id.tv_price_details);
        textViewQuntity=findViewById(R.id.tv_quantity);
        textViewQuntity.setText(""+quntity);
        imageViewCose=findViewById(R.id.iv_close);
        buttoninc=findViewById(R.id.button_increase);
        buttonDec=findViewById(R.id.button_decrease);
        buttonAddToCart =findViewById(R.id.button_add_to_cart);

       cartListReference=FirebaseDatabase.getInstance().getReference().child("user_cart")
                .child(UserData.USER_ID);
        tableReference=FirebaseDatabase.getInstance().getReference().child("tables")
                .child(UserData.RESTAURANT_ID).child(UserData.TABLE_NUMBER+"");

        imageViewCose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buttoninc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incraeseQuntity();
            }
        });
        buttonDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decraeseQuntity();
            }
        });

        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToCart();
            }
        });



        itemReference= FirebaseDatabase.getInstance().getReference().child("menus").child(restaurantId)
                .child(whichPart);
        itemReference.child(itemId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){

                   Item item=dataSnapshot.getValue(Item.class);
                   textViewName.setText(item.getName());
                   textViewDescription.setText(item.getDescription());
                   textViewPrice.setText("EGP "+item.getPrice());


               }
               else {

                   Toast.makeText(ItemDetailsActivity.this, "no data", Toast.LENGTH_SHORT).show();
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void addItemToCart() {
        String id=cartListReference.push().getKey();
        final ItemInCart itemInOrder=new ItemInCart(id,whichPart,itemId,quntity);

        cartListReference.child(id).setValue(itemInOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    tableReference.child("user_id").setValue(UserData.USER_ID);
                    String idInTable=tableReference.push().getKey();

                 tableReference.child("orders").child(idInTable).setValue(itemInOrder);
                 tableReference.child("table_num").setValue(UserData.TABLE_NUMBER);
                }
            }
        });


        finish();

    }

    private void incraeseQuntity() {
        quntity=quntity+1;
        textViewQuntity.setText(quntity+"");
    }
    private void decraeseQuntity() {
        if (quntity>1) {
            quntity = quntity -1;
            textViewQuntity.setText(quntity + "");
        }
    }
}
