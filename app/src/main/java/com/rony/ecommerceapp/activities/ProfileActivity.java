package com.rony.ecommerceapp.activities;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rony.ecommerceapp.R;
import com.rony.ecommerceapp.databinding.ActivityMainBinding;
import com.rony.ecommerceapp.models.UserModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ProgressBar progressBar;
    Button update;
    EditText editText;
    Toolbar toolbar;
    CircleImageView imageView;
    FloatingActionButton fab;
    FirebaseStorage storage;
    FirebaseDatabase database;
    TextView textView;

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageView = findViewById(R.id.profile_img);
        fab = findViewById(R.id.floatbutton);
        textView = findViewById(R.id.nametext);

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserModel userModel = snapshot.getValue(UserModel.class);
                                   if(userModel.getProfile_picture()!=null) {
                                       Glide.with(getApplicationContext()).load(userModel.getProfile_picture()).into(imageView);
                                   }
                                textView.setText("Welcome back, "+userModel.getName());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ProfileActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/+");
//                startActivityForResult(intent,33);

            }
        });


        update =  findViewById(R.id.update);
        editText = findViewById(R.id.edemail);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatepass();
                auth.signOut();
                Intent myintent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(myintent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

          Uri uri = data.getData();
          if(uri!=null){
              imageView.setImageURI(uri);

              final StorageReference reference = storage.getReference().child("profile_picture")
                      .child(FirebaseAuth.getInstance().getUid());

              reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      //Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                      reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {

                              database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                      .child("profile_picture").setValue(uri.toString());
                              Toast.makeText(ProfileActivity.this, "Profile Picture Uploaded", Toast.LENGTH_SHORT).show();

                          }
                      });

                  }
              });

          }

    }

    private void updatepass() {
        String email = editText.getText().toString().trim();

        if(email.isEmpty()){
            editText.setError("Email is required");
            editText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editText.setError("Please provide valid email");
            return;
        }
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(ProfileActivity.this, "Cheek your email to reset your password!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ProfileActivity.this, "Try again Something wrong happend!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}