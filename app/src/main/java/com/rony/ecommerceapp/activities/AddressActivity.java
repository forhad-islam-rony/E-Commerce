package com.rony.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rony.ecommerceapp.R;
import com.rony.ecommerceapp.adapters.AddressAdapter;
import com.rony.ecommerceapp.adapters.DetailsActivity;
import com.rony.ecommerceapp.models.AddressModel;
import com.rony.ecommerceapp.models.NewProductModel;
import com.rony.ecommerceapp.models.PopularProductModel;
import com.rony.ecommerceapp.models.ShowAllModel;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress {

    Button addAddressButton;
    RecyclerView recyclerView;
    private List<AddressModel> addressModelList;
    private AddressAdapter addressAdapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Button paymentButton;
    Toolbar toolbar;
    public static int amount = 0;
    String mAddress = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressActivity.this, MainActivity.class));
            }
        });

        Object object = getIntent().getSerializableExtra("item");

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.address_recycler);
        paymentButton = findViewById(R.id.payment_btn);

        addAddressButton = findViewById(R.id.add_address_btn);
        addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(AddressActivity.this,"Hello",Toast.LENGTH_SHORT).show();
               startActivity(new Intent(AddressActivity.this, AddAdressActivity.class));
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addressModelList = new ArrayList<>();
        addressAdapter = new AddressAdapter(getApplicationContext(),addressModelList,this);
        recyclerView.setAdapter(addressAdapter);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            for(DocumentSnapshot doc: task.getResult().getDocuments()){
                                AddressModel addressModel = doc.toObject(AddressModel.class);
                                addressModelList.add(addressModel);
                                addressAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //int amount = 0;
                int flag=CartActivity.cheek;

//                int quantity = DetailsActivity.totalQuantity;
//
//                if(object instanceof NewProductModel){
//                    NewProductModel newProductModel = (NewProductModel) object;
//                    amount = newProductModel.getPrice();
//                    amount = amount*quantity;
//                    flag=1;
//                }
//                if(object instanceof PopularProductModel){
//                    PopularProductModel popularProductModel = (PopularProductModel) object;
//                    amount = popularProductModel.getPrice();
//                    amount = amount*quantity;
//                    flag =1;
//                }
//                if(object instanceof ShowAllModel){
//                    ShowAllModel showAllModel = (ShowAllModel) object;
//                    amount = showAllModel.getPrice();
//                    amount = amount*quantity;
//                    flag =1;
//                }
//                if(flag==0){
//                    amount = getIntent().getIntExtra("amount",0);
//                }
                if(flag==1){
                    amount = CartActivity.bill;
                }
                else if(flag==0){
                    amount=DetailsActivity.totalPrice;
                }
                if(AddressAdapter.ad==0){
                    Toast.makeText(AddressActivity.this, "Please add your address", Toast.LENGTH_SHORT).show();
                }
                else if(AddressAdapter.radio==0){
                    Toast.makeText(AddressActivity.this, "Please select your address", Toast.LENGTH_SHORT).show();
                }
                else{

                    Intent myintent = new Intent(AddressActivity.this,PaymentActivity.class);
                    // myintent.putExtra("amount",amount);
                    startActivity(myintent);
                }
            }
        });
    }

    @Override
    public void setAddress(String address) {

        mAddress = address;

    }
}