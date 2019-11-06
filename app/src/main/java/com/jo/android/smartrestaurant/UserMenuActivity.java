package com.jo.android.smartrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UserMenuActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        textView = findViewById(R.id.text);

        Intent intent = getIntent();
        String val = intent.getStringExtra("QR Result");
        textView.setText(val);
    }
}
