package com.jo.android.smartrestaurant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.hedgehog.ratingbar.RatingBar;
import com.jo.android.smartrestaurant.data.UserData;

public class WaitingActivity extends AppCompatActivity {

    private TextView textViewState;
    private Button buttonRate;
    private DatabaseReference userStateRef, restaurantRatingRef;
    private RatingBar mRatingBar;

    private float rateValue;
    private String starsString = "five_stars";
    private Long numOfCustomers=0l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        userStateRef = FirebaseDatabase.getInstance().getReference().child("user_state")
                .child(UserData.USER_ID);
        restaurantRatingRef = FirebaseDatabase.getInstance().getReference().child("resturants_rates").child(UserData.RESTAURANT_ID);

        textViewState = findViewById(R.id.tv_state);
        buttonRate = findViewById(R.id.btn_rate);

        mRatingBar = findViewById(R.id.ratingbar);
        mRatingBar.setStarEmptyDrawable(getResources().getDrawable(R.mipmap.star_empty));
        // mRatingBar.setStarHalfDrawable(getResources().getDrawable(R.mipmap.star_half));
        mRatingBar.setStarFillDrawable(getResources().getDrawable(R.mipmap.star_full));
        mRatingBar.setStarCount(5);
        mRatingBar.setStar(5f);
        mRatingBar.halfStar(false);
        mRatingBar.setmClickable(true);
        mRatingBar.setStarImageWidth(120f);
        mRatingBar.setStarImageHeight(60f);
        mRatingBar.setImagePadding(35);
        mRatingBar.setOnRatingChangeListener(
                new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(float ratingCount) {
                        rateValue = ratingCount;
                    }
                }
        );
        userStateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String state = dataSnapshot.child("state").getValue(String.class);
                    if (state.equals("finished")) {
                        Toast.makeText(WaitingActivity.this, " what is your opinion", Toast.LENGTH_SHORT).show();

                        inFinishing();
                    }
                    if (state.equals("waiting")) {

                        Toast.makeText(WaitingActivity.this, " waiting", Toast.LENGTH_SHORT).show();
                        inWaiting();
                    }
                } else {
                    Toast.makeText(WaitingActivity.this, " waiting", Toast.LENGTH_SHORT).show();

                    inWaiting();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateRestaurant();
            }
        });

    }

    private void rateRestaurant() {

        if (rateValue == 1f) {

            starsString = "one_stars";

        }
        if (rateValue == 2f) {
            starsString = "two_stars";

        }
        if (rateValue == 3f) {
            starsString = "three_stars";

        }
        if (rateValue == 4f) {
            starsString = "four_stars";

        }
        if (rateValue == 5f) {
            starsString = "five_stars";

        }

        restaurantRatingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()&&dataSnapshot.hasChild(starsString)) {
                    numOfCustomers = dataSnapshot.child(starsString).getValue(Long.class);

                }
                else{
                    numOfCustomers=0l;


                }
                increaseCoumt();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });




    }

    private void increaseCoumt() {
        restaurantRatingRef.child(starsString).setValue(numOfCustomers + 1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    userStateRef.child("state").setValue("waiting").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });

                }
            }
        });
    }


    private void inWaiting() {

        textViewState.setText("your order in progress");
        buttonRate.setVisibility(View.GONE);
        mRatingBar.setVisibility(View.GONE);
    }

    private void inFinishing() {

        textViewState.setText("whats your opinion");
        buttonRate.setVisibility(View.VISIBLE);
        mRatingBar.setVisibility(View.VISIBLE);

    }
}
