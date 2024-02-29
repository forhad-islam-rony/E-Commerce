package com.rony.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rony.ecommerceapp.R;
import com.rony.ecommerceapp.adapters.DetailsActivity;

import java.util.HashMap;
import java.util.Map;

public class AddAdressActivity extends AppCompatActivity {

    EditText name,address,city,postalCode,phoneNumber;
    Toolbar toolbar;
    Button addadressbutton;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);

        toolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddAdressActivity.this, AddressActivity.class));
            }
        });

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        phoneNumber = findViewById(R.id.phoneNumber);
        postalCode = findViewById(R.id.postal_code);
        addadressbutton = findViewById(R.id.ad_add_address);

        addadressbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                String userCity = city.getText().toString();
                String userAdress = address.getText().toString();
                String userCode = postalCode.getText().toString();
                String userNumber = phoneNumber.getText().toString();

                String final_adress = "";

                if(!userName.isEmpty()){
                    final_adress+=userName+", ";
                }
                if(!userCity.isEmpty()){
                    final_adress+=userCity+", ";
                }
                if(!userAdress.isEmpty()){
                    final_adress+=userAdress+", ";
                }
                if(!userCode.isEmpty()){
                    final_adress+=userCode+", ";
                }
                if(!userNumber.isEmpty()){
                    final_adress+=userNumber+". ";
                }

                if(!userName.isEmpty() && !userCity.isEmpty() && !userAdress.isEmpty() && !userCode.isEmpty() && !userNumber.isEmpty()){

                    Map<String,String> map = new HashMap<>();
                    map.put("userAddress",final_adress);

                    firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                            .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(AddAdressActivity.this,"Address Added Successfully",Toast.LENGTH_SHORT).show();
                                        Intent myintent = new Intent(AddAdressActivity.this, AddressActivity.class);
                                        startActivity(myintent);
                                        finish();
                                    }
                                }
                            });
                }
                else {

                    Toast.makeText(AddAdressActivity.this,"Error!! Try Again",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}