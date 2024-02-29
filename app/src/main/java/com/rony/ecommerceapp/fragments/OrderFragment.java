package com.rony.ecommerceapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.rony.ecommerceapp.activities.AddressActivity;
import com.rony.ecommerceapp.activities.CartActivity;
import com.rony.ecommerceapp.adapters.AddressAdapter;
import com.rony.ecommerceapp.adapters.MyCartAdapter;
import com.rony.ecommerceapp.adapters.MyorderAdapter;
import com.rony.ecommerceapp.models.MyCartModel;
import com.rony.ecommerceapp.models.MyorderModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OrderFragment extends Fragment {

    int overAllTotalAmount;
    TextView overAllAmount;
    TextView textView;

    RecyclerView recyclerView;
    List<MyorderModel> orderModelList;
    MyorderAdapter orderAdapter;

    Button clearAll;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    public OrderFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_order, container, false);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        clearAll = root.findViewById(R.id.clear_all);
        textView = root.findViewById(R.id.textorder);


        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));

        overAllAmount = root.findViewById(R.id.textView2);
        recyclerView = root.findViewById(R.id.order_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderModelList = new ArrayList<>();
        orderAdapter = new MyorderAdapter(getContext(),orderModelList);
        recyclerView.setAdapter(orderAdapter);

        firestore.collection("UserOrder").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            for(DocumentSnapshot doc:task.getResult().getDocuments()){
                                String documentId = doc.getId();
                                MyorderModel myorderModel = doc.toObject(MyorderModel.class);

                                myorderModel.setSetDocumentID(documentId);
                                orderModelList.add(myorderModel);
                                orderAdapter.notifyDataSetChanged();
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                });


        return root;
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int totalBill = intent.getIntExtra("totalAmount",0);
            overAllAmount.setText("Total Amount :"+totalBill+"$");

            String userId = auth.getCurrentUser().getUid();
            CollectionReference orderRef = firestore.collection("UserOrder").document(userId).collection("User");

            clearAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    orderRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    orderRef.document(document.getId()).delete();
                                }
                            }
                        }
                    });
                    Toast.makeText(context, "Item Clear Successfully", Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);
                    overAllAmount.setText("Total Amount :"+00+"$");
                }
            });
        }
    };
}