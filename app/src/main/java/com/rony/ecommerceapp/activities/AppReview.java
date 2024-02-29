package com.rony.ecommerceapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rony.ecommerceapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppReview extends AppCompatActivity {
    ListView listView;
    public static int ind;
    Toolbar toolbar;

    public static ArrayList<String> personName = new ArrayList<>();
    public static ArrayList<Double> rating = new ArrayList<>();
    public static ArrayList<String> review = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_review);
        listView = findViewById(R.id.listView);
        toolbar = findViewById(R.id.reviewlist_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        personName.clear();
        rating.clear();
        review.clear();

        extract();
    }

    private void extract() {
        String url = "https://api.myjson.online/v1/records/2d1aeaa1-fc28-43ab-8c4e-5ef6209a8a20";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JsonParse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void JsonParse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                personName.add(jsonObject1.getString("name"));
                rating.add(jsonObject1.getDouble("rating"));
                review.add(jsonObject1.getString("comment"));
            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,personName);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ind =i;

                    Intent myintent = new Intent(AppReview.this, AppRivew1.class);
                    //  myintent.putExtra("ind",i);
                    startActivity(myintent);

                }
            });

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}