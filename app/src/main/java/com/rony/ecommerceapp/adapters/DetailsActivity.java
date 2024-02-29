package com.rony.ecommerceapp.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rony.ecommerceapp.R;
import com.rony.ecommerceapp.activities.AddAdressActivity;
import com.rony.ecommerceapp.activities.AddressActivity;
import com.rony.ecommerceapp.activities.CartActivity;
import com.rony.ecommerceapp.activities.PaymentActivity;
import com.rony.ecommerceapp.models.NewProductModel;
import com.rony.ecommerceapp.models.PopularProductModel;
import com.rony.ecommerceapp.models.ShowAllModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating,name,description,price,quantity;

    private  Toolbar toolbar;
    public static int totalQuantity= 1;
    public static int totalPrice = 0;
    Button addTocart,buyNow;
    ImageView addItem,subItem;

    //New Product
    NewProductModel newProductModel = null;

    //popular product
    PopularProductModel popularProductModel = null;

    //show All product
    ShowAllModel showAllModel = null;
    public static final  HashMap<String,Object> orderMap = new HashMap<>();

    FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        toolbar = findViewById(R.id.detailed_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.detailsactivity);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        orderMap.clear();

        final Object object = getIntent().getSerializableExtra("details");

        if(object instanceof NewProductModel){
            newProductModel = (NewProductModel) object;
        }
        else if(object instanceof PopularProductModel){
            popularProductModel = (PopularProductModel) object;
        }
        else if(object instanceof ShowAllModel){
            showAllModel = (ShowAllModel) object;
        }

        detailedImg = findViewById(R.id.detailed_img);
        quantity = findViewById(R.id.quantity);
        rating = findViewById(R.id.rating);
        name = findViewById(R.id.detaided_name);
        description =findViewById(R.id.detailed_des);
        price = findViewById(R.id.detailed_price);

        addTocart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);

        addItem = findViewById(R.id.add_item);
        subItem = findViewById(R.id.sub_item);

    if(newProductModel != null){
        String string = quantity.getText().toString();
        totalQuantity = Integer.parseInt(string);
        Glide.with(getApplicationContext()).load(newProductModel.getImg_url()).into(detailedImg);
        name.setText(newProductModel.getName());
        rating.setText(newProductModel.getRating());
        description.setText(newProductModel.getDescription());
        price.setText(String.valueOf(newProductModel.getPrice()));
        name.setText(newProductModel.getName());

        totalPrice = newProductModel.getPrice() * totalQuantity;

    }

        if(popularProductModel != null){
            String string = quantity.getText().toString();
            totalQuantity = Integer.parseInt(string);
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url()).into(detailedImg);
            name.setText(popularProductModel.getName());
            rating.setText(popularProductModel.getRating());
            description.setText(popularProductModel.getDescription());
            price.setText(String.valueOf(popularProductModel.getPrice()));
            name.setText(popularProductModel.getName());

            totalPrice = popularProductModel.getPrice() * totalQuantity;

        }

        if(showAllModel != null){
            String string = quantity.getText().toString();
            totalQuantity = Integer.parseInt(string);
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            rating.setText(showAllModel.getRating());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());

            totalPrice = showAllModel.getPrice() * totalQuantity;

        }

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartActivity.cheek = 0;
                CartActivity.buywhere = 0;
                AddressAdapter.radio=0;
                String string = quantity.getText().toString();
                totalQuantity = Integer.parseInt(string);
                Intent myintent = new Intent(DetailsActivity.this, AddressActivity.class);
                if(newProductModel !=null){
                    myintent.putExtra("item",newProductModel);
                }
                if(popularProductModel !=null){
                    myintent.putExtra("item",popularProductModel);
                }
                if(showAllModel !=null){
                    myintent.putExtra("item",showAllModel);
                }
                orderMap.put("productName",name.getText().toString());
                orderMap.put("productPrice",price.getText().toString());
                orderMap.put("totalQuantity", quantity.getText().toString());
                orderMap.put("totalPrice",totalPrice);
                startActivity(myintent);
            }
        });

        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String string = quantity.getText().toString();
                totalQuantity = Integer.parseInt(string);
                addtocart();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(totalQuantity<10){
                   totalQuantity++;
                   quantity.setText(String.valueOf(totalQuantity));

                   if(newProductModel != null){

                       totalPrice = newProductModel.getPrice() * totalQuantity;
                   }
                   if(popularProductModel != null){
                       totalPrice = popularProductModel.getPrice() * totalQuantity;
                   }
                   if(showAllModel != null){
                       totalPrice = showAllModel.getPrice() * totalQuantity;
                   }
               }
            }
        });

        subItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));

                    if(newProductModel != null){

                        totalPrice = newProductModel.getPrice() * totalQuantity;
                    }
                    if(popularProductModel != null){
                        totalPrice = popularProductModel.getPrice() * totalQuantity;
                    }
                    if(showAllModel != null){
                        totalPrice = showAllModel.getPrice() * totalQuantity;
                    }
                }
            }
        });

     //   totalQuantity = 1;

    }

    private void addtocart() {
        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("productName",name.getText().toString());
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailsActivity.this,"Item Added Successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}