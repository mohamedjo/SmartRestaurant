package com.jo.android.smartrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActvity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String sentCode;
    private EditText editTextPhoneNumber,editTextCode;
    private Button buttonSendCode,buttonVerifyCode;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_actvity);

        mAuth=FirebaseAuth.getInstance();
        editTextPhoneNumber=findViewById(R.id.edit_text_phone);
        editTextPhoneNumber.setText("+20");
        editTextCode=findViewById(R.id.edit_text_code);
        buttonSendCode=findViewById(R.id.button_send_code);
        buttonVerifyCode=findViewById(R.id.button_verify);

        buttonSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });
        buttonVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCode();
            }
        });


    }

    private void verifyCode() {
        String code=editTextCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void sendVerificationCode() {
       String phoneNumber =editTextPhoneNumber.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            sendUserToMainActivity();
                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                Toast.makeText(LoginActvity.this, "invalid code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void sendUserToMainActivity() {
        Intent intent=new Intent(LoginActvity.this,MainActivity.class);
        startActivity(intent);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

           // signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(LoginActvity.this, "invalid phone Number", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;
            Toast.makeText(LoginActvity.this, "code has been Sent", Toast.LENGTH_SHORT).show();

            // ...
        }

    };



}
