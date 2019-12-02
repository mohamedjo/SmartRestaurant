package com.jo.android.smartrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jo.android.smartrestaurant.data.ManagerData;

import java.util.regex.Pattern;

public class ManagerLoginActivity extends AppCompatActivity {

  private   ProgressBar progressBar;
   private EditText editTextResturanPhone, editTextManagerName, editTextPassword;
   private Button btnManagerLogin;
   private TextView textViewGotoUserLogin;

    private DatabaseReference managerReference;
    private String restaurantPhone,managerName,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);

        managerReference=FirebaseDatabase.getInstance().getReference().child("managers");
        init();


    }


    private void init() {
    progressBar = findViewById(R.id.progres_bar_login);
    editTextResturanPhone = findViewById(R.id.edit_text_resturan_phone);
    editTextManagerName = findViewById(R.id.edit_text_manager_name);
    editTextPassword = findViewById(R.id.edit_text_manager_password);
    btnManagerLogin = findViewById(R.id.btn_manager_login);
    textViewGotoUserLogin = findViewById(R.id.tv_go_to_user_login);

    textViewGotoUserLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendToUserLoginActivity();
        }
    });

    btnManagerLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isValidate()) {
                managerLogin();
            }
        }
    });
    }

    private void sendToManagerHomeActivity() {
        Intent intent = new Intent(ManagerLoginActivity.this, ManagerHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    private boolean isValidate() {
        boolean isValid= true;
        restaurantPhone = editTextResturanPhone.getText().toString();
        managerName  = editTextManagerName.getText().toString();
        password = editTextPassword.getText().toString();

        if(restaurantPhone.trim().isEmpty()){
            editTextResturanPhone.setError("Required");
            isValid=false;
        }else{
            editTextResturanPhone.setError(null);
        }

        if(managerName.trim().isEmpty()){
            editTextManagerName.setError("Required");
            isValid=false;
        }
        else{
            editTextManagerName.setError(null);
        }

        if(password.trim().isEmpty()){
            editTextPassword.setError("Required");
            isValid=false;
        }else{
            editTextPassword.setError(null);
        }
        return isValid;

    }



    private void sendToUserLoginActivity() {
        Intent intent = new Intent(ManagerLoginActivity.this, LoginActvity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();

    }




    private boolean isValidEmail(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    private void managerLogin(){

        managerReference.child(restaurantPhone).child(managerName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String savedPassword=dataSnapshot.child("password").getValue(String.class);
                            if(password.equals(savedPassword)){
                                ManagerData.RESTAURANT_PHONE=restaurantPhone;
                                ManagerData.MANAGER_NAME=managerName;
                                sendToManagerHomeActivity();
                            }
                            else {

                                Toast.makeText(ManagerLoginActivity.this, " incorrect password", Toast.LENGTH_SHORT).show();

                            }

                        }
                        else{

                            Toast.makeText(ManagerLoginActivity.this, " invalid restaurant phone or manager  name ", Toast.LENGTH_SHORT).show();

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }
}
