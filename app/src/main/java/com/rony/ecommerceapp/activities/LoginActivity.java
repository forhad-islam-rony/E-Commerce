package com.rony.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.rony.ecommerceapp.R;

public class LoginActivity extends AppCompatActivity {

    Button buttonsignin;
    TextView textsignup;

    EditText edemail,edpassword;
    FirebaseAuth mAuth;
    boolean passwordVisible=false;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        buttonsignin = findViewById(R.id.buttonsignin);
        textsignup = findViewById(R.id.textsignup);
        edemail = findViewById(R.id.edemail);
        edpassword = findViewById(R.id.edpassword);

       edpassword.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View view, MotionEvent motionEvent) {

               final int Right = 2;
               if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                   if(motionEvent.getRawX()>=edpassword.getRight()-edpassword.getCompoundDrawables()[Right].getBounds().width()){
                       int selecton = edpassword.getSelectionEnd();

                       if(passwordVisible){
                           edpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                           edpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                           passwordVisible =false;
                       }
                       else{
                           edpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.baseline_visibility_24,0);
                           edpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                           passwordVisible =true;
                       }

                       edpassword.setSelection(selecton);
                       return true;
                   }
               }


               return false;
           }
       });


        buttonsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edemail.getText().toString();
                String password = edpassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    edemail.setError("Enter your Email");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    edpassword.setError("Enter your Password");
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(LoginActivity.this,"Enter a valid email!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<6){
                    Toast.makeText(LoginActivity.this,"Password too short, Enter minimum 6 characters!",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                            Intent myintent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(myintent);
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Error:Login Unsuccessful!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
        textsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(myintent);
            }
        });
    }
}