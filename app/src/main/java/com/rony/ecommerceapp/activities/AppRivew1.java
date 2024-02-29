package com.rony.ecommerceapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rony.ecommerceapp.R;

public class AppRivew1 extends AppCompatActivity {

    TextView textView,textView2;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_rivew1);
        toolbar = findViewById(R.id.review_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.text2);

        textView.setText("Rating: "+String.valueOf(AppReview.rating.get(AppReview.ind))+"(Stars)");
        textView2.setText("Review: \n"+AppReview.review.get(AppReview.ind));

    }
}