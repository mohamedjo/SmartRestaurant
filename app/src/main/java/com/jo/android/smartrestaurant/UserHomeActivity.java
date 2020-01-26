package com.jo.android.smartrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jo.android.smartrestaurant.data.UserData;


public class UserHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String CATEGORY_TITTLE_KEY="category";
    public static final String RESTAURANT_ID_KEY="restaurant_id";

    public static final int REQUEST_QR_READER = 1;
    public static final String QR_CONTENT ="qr_content" ;
    private FirebaseAuth mAuth;
    private Button buttonScan;
    private TextView textViewAscScan;
    private ScrollView scrollViewCategory;
    private TextView textViewRestuarantName,textViewOffers,textViewMeals,textViewSandwitches,textViewPizza, textViewPasta,textViewSalat,textViewDrinks,textViewrecommendation;

    private TextView textViewUserName;

    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mAuth=FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        buttonScan=findViewById(R.id.button_scan);
        scrollViewCategory=findViewById(R.id.catagory_list);
        textViewAscScan=findViewById(R.id.tv_ask_scan);

        textViewRestuarantName=findViewById(R.id.tv_restaurant_name);
        textViewMeals=findViewById(R.id.tv_meals);
        textViewSandwitches=findViewById(R.id.tv_Sandwiches);
        textViewPizza=findViewById(R.id.tv_pizza);
        textViewPasta =findViewById(R.id.tv_pasta);
        textViewSalat=findViewById(R.id.tv_salat);
        textViewDrinks=findViewById(R.id.tv_drinks);
        textViewRestuarantName=findViewById(R.id.tv_restaurant_name);
        textViewOffers=findViewById(R.id.tv_offers);
        textViewrecommendation=findViewById(R.id.tv_Recommendation);
        if(UserData.RESTAURANT_ID!=null&&!UserData.RESTAURANT_ID.equals("")){
            getResturantName();
            desplayMenu();
        }
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToQRReaderActivity();

            }
        });
        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                      //  .setAction("Action", null).show();
                sendToQRReaderActivity();

            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        textViewSandwitches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendToMenuActivity("sandwiches");
            }
        });
        textViewMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendToMenuActivity("meals");
            }
        });
        textViewPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendToMenuActivity("pizza");
            }
        });
        textViewPasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendToMenuActivity("pastas");
            }
        });
        textViewSalat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendToMenuActivity("salads");
            }
        });
        textViewDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendToMenuActivity("drinks");
            }
        });
        textViewOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendToMenuActivity("offers");
            }
        });
        textViewrecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToRecommendationActivity();
            }
        });


    }

    private void sendToRecommendationActivity() {
        Intent intent = new Intent(UserHomeActivity.this, RecommendationActivity.class);

        startActivity(intent);
    }

    private void sendToMenuActivity(String menePart) {
        Intent intent = new Intent(UserHomeActivity.this, MenuActivity.class);
        intent.putExtra(CATEGORY_TITTLE_KEY,menePart);
        intent.putExtra(RESTAURANT_ID_KEY,restaurantId);
        startActivity(intent);

    }


    private void sendToQRReaderActivity() {
        Intent intent=new Intent(UserHomeActivity.this,QrReaderActivity.class);
        startActivityForResult(intent,REQUEST_QR_READER);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_QR_READER)
        {
          String qrContent=data.getStringExtra(QR_CONTENT);
          String[] contentArray=qrContent.split(" ");
          restaurantId=contentArray[0];
          UserData.RESTAURANT_ID=restaurantId;
          UserData.TABLE_NUMBER=Integer.valueOf(contentArray[1]);
            getResturantName();
            desplayMenu();


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void logout(){

        mAuth.signOut();
        removeUserInfo();
        sendToLoginActivity();

    }

    private void removeUserInfo() {


    }

    private void sendToLoginActivity() {
        Intent intent=new Intent(UserHomeActivity.this,LoginActvity.class);
        finish();

        startActivity(intent);
    }
    private void desplayMenu(){


        textViewAscScan.setVisibility(View.GONE);
        buttonScan.setVisibility(View.GONE);
        scrollViewCategory.setVisibility(View.VISIBLE);
        Toast.makeText(this, UserData.RESTAURANT_NAME, Toast.LENGTH_SHORT).show();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_orders) {

            sendToCartActvity();

        } else if (id == R.id.nav_offers) {


        }

        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void sendToCartActvity() {
        Intent intent=new Intent(UserHomeActivity.this,CartActivity.class);
        startActivity(intent);

    }

    private void getResturantName(){
        FirebaseDatabase.getInstance().getReference().child("restaurant")
                .child(UserData.RESTAURANT_ID).child("tittle").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    textViewRestuarantName.setText(dataSnapshot.getValue(String.class)+" restaurant");
                    UserData.RESTAURANT_NAME=dataSnapshot.getValue(String.class);

                }
                else{
                    Toast.makeText(UserHomeActivity.this, " no data founded", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

}
