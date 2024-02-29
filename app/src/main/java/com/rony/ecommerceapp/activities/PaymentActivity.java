package com.rony.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rony.ecommerceapp.R;
import com.rony.ecommerceapp.adapters.DetailsActivity;
import com.rony.ecommerceapp.models.MyCartModel;

import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView subTotal,discount,shipping,total;
    Button payButton;
    int amount = 0;
//    FirebaseAuth auth;
//    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
//
//        auth = FirebaseAuth.getInstance();
//        firestore = FirebaseFirestore.getInstance();


        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        payButton = findViewById(R.id.pay_btn);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(PaymentActivity.this,AddressActivity.class);
                startActivity(myintent);
            }
        });

      //  amount = getIntent().getIntExtra("amount",0);
        amount = AddressActivity.amount;

        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        shipping = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);

        subTotal.setText(amount+"$");
        shipping.setText(50+"$");
        total.setText(amount+50+"$");

//        String userId = auth.getCurrentUser().getUid();
//        CollectionReference cartRef = firestore.collection("AddToCart").document(userId).collection("User");

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(CartActivity.buywhere==0){
//                   firestore.collection("UserOrder").document(auth.getCurrentUser().getUid())
//                           .collection("User").add(DetailsActivity.orderMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                               @Override
//                               public void onSuccess(DocumentReference documentReference) {
//                                   Toast.makeText(PaymentActivity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
//                               }
//                           });
//                }
//
//
//                if(CartActivity.buywhere==1){
//                    cartRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                            if(task.isSuccessful()){
//                                for(QueryDocumentSnapshot document : task.getResult()){
//
//                                    final HashMap<String,Object> orderMap = new HashMap<>();
//                                    String documentId = document.getId();
//                                    MyCartModel myCartModel = document.toObject(MyCartModel.class);
//                                    myCartModel.setDocumentId(documentId);
//                                    orderMap.put("productName",myCartModel.getProductName());
//                                    orderMap.put("productPrice",myCartModel.getProductPrice());
//                                    orderMap.put("totalQuantity",myCartModel.getTotalQuantity());
//                                    orderMap.put("totalPrice",myCartModel.getTotalPrice());
//                                    firestore.collection("UserOrder").document(auth.getCurrentUser().getUid())
//                                            .collection("User").add(orderMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                                @Override
//                                                public void onSuccess(DocumentReference documentReference) {
//                                                }
//                                            });
//
//                                    cartRef.document(document.getId()).delete();
//                                }
//                            }
//                        }
//                    });
//                         Toast.makeText(PaymentActivity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
//                }
//
                Intent myintent = new Intent(PaymentActivity.this, OTP_Number.class);
                startActivity(myintent);
            }
        });

    }
}