package com.rony.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rony.ecommerceapp.R;
import com.rony.ecommerceapp.adapters.DetailsActivity;
import com.rony.ecommerceapp.models.MyCartModel;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Otp_Verification extends AppCompatActivity {

    EditText input1,input2,input3,input4,input5,input6;
    TextView textView;
    TextView resend;
    String getotpbackend;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);


        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        input3 = findViewById(R.id.input3);
        input4 = findViewById(R.id.input4);
        input5 = findViewById(R.id.input5);
        input6 = findViewById(R.id.input6);
        final Button otpveributton= findViewById(R.id.otpgetbutton);
        final ProgressBar progressBar = findViewById(R.id.otpcheek);

        textView = findViewById(R.id.textsendopt);
        textView.setText(String.format("+880-%s",getIntent().getStringExtra("mobile")));
        getotpbackend = getIntent().getStringExtra("backendotp");

        String userId = auth.getCurrentUser().getUid();
        CollectionReference cartRef = firestore.collection("AddToCart").document(userId).collection("User");

        otpveributton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!input1.getText().toString().trim().isEmpty() && !input2.getText().toString().trim().isEmpty() && !input3.getText().toString().trim().isEmpty() && !input4.getText().toString().trim().isEmpty() && !input5.getText().toString().trim().isEmpty() && !input6.getText().toString().trim().isEmpty()){

                    String enterotp = input1.getText().toString()+
                            input2.getText().toString()+
                            input3.getText().toString()+
                            input4.getText().toString()+
                            input5.getText().toString()+
                            input6.getText().toString();

                    if(getotpbackend!=null){
                          progressBar.setVisibility(View.VISIBLE);
                          otpveributton.setVisibility(View.INVISIBLE);
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                getotpbackend,enterotp
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        otpveributton.setVisibility(View.VISIBLE);

                                        if(task.isSuccessful()){
                                            if(CartActivity.buywhere==0){
                                                firestore.collection("UserOrder").document(auth.getCurrentUser().getUid())
                                                        .collection("User").add(DetailsActivity.orderMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Toast.makeText(Otp_Verification.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }


                                            if(CartActivity.buywhere==1){
                                                cartRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                        if(task.isSuccessful()){
                                                            for(QueryDocumentSnapshot document : task.getResult()){

                                                                final HashMap<String,Object> orderMap = new HashMap<>();
                                                                String documentId = document.getId();
                                                                MyCartModel myCartModel = document.toObject(MyCartModel.class);
                                                                myCartModel.setDocumentId(documentId);
                                                                orderMap.put("productName",myCartModel.getProductName());
                                                                orderMap.put("productPrice",myCartModel.getProductPrice());
                                                                orderMap.put("totalQuantity",myCartModel.getTotalQuantity());
                                                                orderMap.put("totalPrice",myCartModel.getTotalPrice());
                                                                firestore.collection("UserOrder").document(auth.getCurrentUser().getUid())
                                                                        .collection("User").add(orderMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                            }
                                                                        });

                                                                cartRef.document(document.getId()).delete();
                                                            }
                                                        }
                                                    }
                                                });
                                                Toast.makeText(Otp_Verification.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                                            }

//                                            Intent myintent = new Intent(Otp_Verification.this, OTP_Number.class);
//                                            startActivity(myintent);
                                            Intent myintent = new Intent(Otp_Verification.this, PaymentSuccessful.class);
                                            myintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(myintent);
                                        }
                                        else{
                                            Toast.makeText(Otp_Verification.this, "Enter the correct otp", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else{
                        Toast.makeText(Otp_Verification.this, "Please cheek internet connection", Toast.LENGTH_SHORT).show();
                    }

                  //  Toast.makeText(Otp_Verification.this, "Otp verify", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Otp_Verification.this, "Please enter all number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    
        numberotpmove();

        resend = findViewById(R.id.textsendopt);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhoneAuthProvider.getInstance().verifyPhoneNumber("+880" + getIntent().getStringExtra("mobile")
                        , 60, TimeUnit.SECONDS, Otp_Verification.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(Otp_Verification.this, "Error!Please cheek internet connection", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                               getotpbackend = backendotp;
                                Toast.makeText(Otp_Verification.this, "otp sended successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                );


            }
        });




    }

    private void numberotpmove() {

        input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().trim().isEmpty()){
                    input2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().trim().isEmpty()){
                    input3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().trim().isEmpty()){
                    input4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().trim().isEmpty()){
                    input5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().trim().isEmpty()){
                    input6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}