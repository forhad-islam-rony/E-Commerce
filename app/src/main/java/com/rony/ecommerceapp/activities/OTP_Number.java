package com.rony.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rony.ecommerceapp.R;

import java.util.concurrent.TimeUnit;

public class OTP_Number extends AppCompatActivity {

    EditText enternumber;
    Button getotpbutton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_number);
        enternumber = findViewById(R.id.edmobile);
        getotpbutton = findViewById(R.id.otpbutton);
        progressBar = findViewById(R.id.otpsendprogress);

        getotpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!enternumber.getText().toString().trim().isEmpty()){
                    if(enternumber.getText().toString().trim().length()==10){

                        progressBar.setVisibility(View.VISIBLE);
                        getotpbutton.setVisibility(View.INVISIBLE);
                        PhoneAuthProvider.getInstance().verifyPhoneNumber("+880" + enternumber.getText().toString()
                                , 60, TimeUnit.SECONDS, OTP_Number.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                        progressBar.setVisibility(View.GONE);
                                        getotpbutton.setVisibility(View.VISIBLE);
                                        
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        getotpbutton.setVisibility(View.VISIBLE);
                                        Toast.makeText(OTP_Number.this, "Error!Please cheek internet connection", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        progressBar.setVisibility(View.GONE);
                                        getotpbutton.setVisibility(View.VISIBLE);
                                        Intent myintent = new Intent(OTP_Number.this,Otp_Verification.class);
                                        myintent.putExtra("mobile",enternumber.getText().toString());
                                        myintent.putExtra("backendotp",backendotp);
                                        startActivity(myintent);
                                        finish();
                                    }
                                }
                        );

//                        Intent myintent = new Intent(OTP_Number.this,Otp_Verification.class);
//                        myintent.putExtra("mobile",enternumber.getText().toString());
//                        startActivity(myintent);
                    }
                    else{

                        Toast.makeText(OTP_Number.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(OTP_Number.this, "Enter Mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}