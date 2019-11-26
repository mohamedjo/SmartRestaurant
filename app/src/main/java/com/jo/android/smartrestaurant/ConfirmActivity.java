package com.jo.android.smartrestaurant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmActivity extends AppCompatActivity {

    private TextView textViewSubtotal,textViewService,textViewTotalAmount;
    private RadioGroup radioGroup;
    private RadioButton radioButtonCridet,radioButtonCash;
    private Button buttonConfirm;


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

        radioButtonCridet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payWay=CRIDET;
            }
        });
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });


    }


}
