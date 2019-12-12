package com.jo.android.smartrestaurant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jo.android.smartrestaurant.data.ManagerData;
import com.jo.android.smartrestaurant.model.OfferItem;

public class AddOfferActivity extends AppCompatActivity {
    private EditText editTextName,editTextDescription,editTextPrice;

    ImageView imageViewClose;
    private Button buttonAddOffer;
    private DatabaseReference offersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_offer);
        editTextName =findViewById(R.id.edit_text_offer_name);
        editTextDescription =findViewById(R.id.edit_text_offer_description);
        editTextPrice =findViewById(R.id.edit_text_offer_price);
        imageViewClose =findViewById(R.id.iv_close_offer);
        buttonAddOffer =findViewById(R.id.button_add_offer);
        offersRef= FirebaseDatabase.getInstance().getReference().child("menus").child(ManagerData.RESTAURANT_PHONE).child("offers");



        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewOffer();
            }
        });

    }

    private void addNewOffer() {

       String offerName= editTextName.getText().toString();
       String offerDescription= editTextDescription.getText().toString();
       String offerPriceString= editTextPrice.getText().toString();
       long price=Long.valueOf(offerPriceString);
       String state="new";
        OfferItem offer=new OfferItem(offerName,offerDescription,price,state);

    String id=offersRef.push().getKey();
    offersRef.child(id).setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {

            if(task.isSuccessful()){
                finish();
            }
        }
    });
    }

}
