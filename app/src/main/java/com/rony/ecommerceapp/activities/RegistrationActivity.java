package com.rony.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rony.ecommerceapp.R;
import com.rony.ecommerceapp.models.UserModel;

public class RegistrationActivity extends AppCompatActivity {
    Button buttonsignup;
    TextView textsignin;
    EditText edname,edemail,edpassword;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    boolean passwordVisible;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        if(mAuth.getCurrentUser()!=null){
            Intent myintent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(myintent);
            finish();
        }

        buttonsignup = findViewById(R.id.buttonsignup);
        textsignin = findViewById(R.id.textsignin);
        edname = findViewById(R.id.edname);
        edemail = findViewById(R.id.edemail);
        edpassword = findViewById(R.id.edpassword);

        edpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final  int Right = 2;
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if(motionEvent.getRawX()>= edpassword.getRight()-edpassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = edpassword.getSelectionEnd();
                        if(passwordVisible){

                            edpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);

                            edpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;

                        }else{
                            edpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.baseline_visibility_24,0);

                            edpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;

                        }
                        edpassword.setSelection(selection);
                        return true;
                    }

                }

                return false;
            }
        });


        sharedPreferences = getSharedPreferences("OnBoardingScreen",MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("FirstTime",true);
        if(isFirstTime){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("FirstTime",false);
            editor.commit();

            Intent myintent = new Intent(RegistrationActivity.this, OnBoardingActivity.class);
            startActivity(myintent);
            finish();
        }


        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edname.getText().toString();
                String email = edemail.getText().toString();
                String password = edpassword.getText().toString();

                if(TextUtils.isEmpty(name)){
                    edname.setError("Enter your name");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    edemail.setError("Enter your name");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    edpassword.setError("Enter your name");
                    return;
                }
               if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                   Toast.makeText(RegistrationActivity.this,"Enter a valid email!",Toast.LENGTH_SHORT).show();
                   return;
               }
                if(password.length()<6){
                    Toast.makeText(RegistrationActivity.this,"Password too short, Enter minimum 6 characters!",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                           UserModel userModel = new UserModel(name,email,password);
                           String id = task.getResult().getUser().getUid();
                           database.getReference().child("Users").child(id).setValue(userModel);

                            Toast.makeText(RegistrationActivity.this,"Successfully Register",Toast.LENGTH_SHORT).show();
                            Intent myintent = new Intent(RegistrationActivity.this,MainActivity.class);
                            startActivity(myintent);
                            finish();
                        }
                        else{
                            Toast.makeText(RegistrationActivity.this,"Registration Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
               // finish();


            }
        });
        textsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(myintent);
                finish();
            }
        });
    }
}