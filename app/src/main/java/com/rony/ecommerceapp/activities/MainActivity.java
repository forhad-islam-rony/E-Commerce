package com.rony.ecommerceapp.activities;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.rony.ecommerceapp.R;
import com.rony.ecommerceapp.fragments.HomeFragment;
import com.rony.ecommerceapp.fragments.OfferFragment;
import com.rony.ecommerceapp.fragments.OrderFragment;

public class MainActivity extends AppCompatActivity {

    Fragment HomeFragment;

    FirebaseAuth auth;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bnView;

    AlertDialog.Builder builder;
    String ROOT_FRAGMENT_TAG = "root_fragment";
    // public static int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        builder = new AlertDialog.Builder(this);

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationView);

        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu2);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bnView = findViewById(R.id.bnView);
        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.menu_account){
                    Intent myintent = new Intent(MainActivity.this,ProfileActivity.class);
                    startActivity(myintent);
                }
                else if(id==R.id.menu_review){

                    Intent myintent = new Intent(MainActivity.this,AppReview.class);
                    startActivity(myintent);

                }
                else if(id==R.id.menu_home){
                    getSupportActionBar().setTitle("CнқOυт");
                    loedFragment(new HomeFragment(),1);

                }


                return true;
            }
        });

        HomeFragment = new HomeFragment();
        loedFragment(HomeFragment,0);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if(id==R.id.menu_account){
                    Intent myintent = new Intent(MainActivity.this,ProfileActivity.class);
                    startActivity(myintent);

                }
                else if(id==R.id.menu_home){
                    getSupportActionBar().setTitle("CнқOυт");
                    loedFragment(new HomeFragment(),1);

                }
                else if(id==R.id.menu_offer){

                    getSupportActionBar().setTitle("My Offers");

                    loedFragment(new OfferFragment(),1);

                }
                else if(id==R.id.menu_order){
                    getSupportActionBar().setTitle("My Orders");
                    loedFragment(new OrderFragment(),1);

                }
                else if(id==R.id.menu_settings){

                    Toast.makeText(MainActivity.this, "Working with this!! Will be update very soon..", Toast.LENGTH_LONG).show();

                }else if(id==R.id.menu_logout){

                    builder.setTitle("Alert!!")
                    .setMessage("Do you want to LogOut??")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            auth.signOut();
                            Intent myintent = new Intent(MainActivity.this, RegistrationActivity.class);
                            startActivity(myintent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .show();

                } else if(id == R.id.menu_cart){
            Intent myintent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(myintent);
        }

                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });

    }


//    private void offerFragement(Fragment fragment) {
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//
//        ft.add(R.id.home_container,fragment);
//        ft.commit();
//    }

    private void loedFragment(Fragment fragment,int flag) {

       FragmentManager fm = getSupportFragmentManager();
       FragmentTransaction ft = fm.beginTransaction();

       if(flag==0){
           ft.add(R.id.home_container,fragment);
           fm.popBackStack(ROOT_FRAGMENT_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
           ft.addToBackStack(ROOT_FRAGMENT_TAG);
       }
       else if(flag==1){
           ft.replace(R.id.home_container,fragment);
           ft.addToBackStack(null);
       }

        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
        }

    }
