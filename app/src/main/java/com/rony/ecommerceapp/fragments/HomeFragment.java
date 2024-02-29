package com.rony.ecommerceapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rony.ecommerceapp.R;
import com.rony.ecommerceapp.activities.ShowAllActivity;
import com.rony.ecommerceapp.adapters.CategpryAdapter;
import com.rony.ecommerceapp.adapters.NewProductAdapter;
import com.rony.ecommerceapp.adapters.PopularProductAdapter;
import com.rony.ecommerceapp.adapters.ShowAllAdapter;
import com.rony.ecommerceapp.models.CategoryModel;
import com.rony.ecommerceapp.models.NewProductModel;
import com.rony.ecommerceapp.models.PopularProductModel;
import com.rony.ecommerceapp.models.ShowAllModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView catShowAll,popularShowAll,newProductShowAll;

    EditText search_box;
    private List<ShowAllModel> showAllModelList;
    private RecyclerView recyclerViewSearch;
    private ShowAllAdapter showAllAdapter;
    public static int diaglogshow=0;

    String RootFragment = "root_fragment";

    LinearLayout linearLayout;
    ProgressDialog progressDialog;

    RecyclerView catRecyclerview;
    CategpryAdapter categpryAdapter;
    List<CategoryModel> categoryModelList;

    RecyclerView newProductRecylerView,popularRecylerView;
    NewProductAdapter newProductAdapter;
    List<NewProductModel> newProductModelList;

    //popular product
    PopularProductAdapter popularProductAdapter;
    List<PopularProductModel> popularProductModelList;


    FirebaseFirestore db;


    public HomeFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getActivity());;

         catRecyclerview = root.findViewById(R.id.rec_category);
         newProductRecylerView = root.findViewById(R.id.new_product_rec);
         popularRecylerView = root.findViewById(R.id.popular_rec);
         catShowAll = root.findViewById(R.id.category_see_all);
         popularShowAll = root.findViewById(R.id.popular_see_all);
         newProductShowAll = root.findViewById(R.id.newProducts_see_all);

         catShowAll.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent myintent = new Intent(getContext(), ShowAllActivity.class);
                 startActivity(myintent);
             }
         });
        newProductShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(getContext(), ShowAllActivity.class);
                startActivity(myintent);
            }
        });
        popularShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(getContext(), ShowAllActivity.class);
                startActivity(myintent);
            }
        });


         linearLayout = root.findViewById(R.id.home_layout);
         linearLayout.setVisibility(View.GONE);

        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.banner1,"New Arrivals", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2,"Discount On All Beauty Products", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner4,"Celebrates Anniversary Offer", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner6,"75% OFF", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);

        if(diaglogshow==0){

            progressDialog.setTitle("Welcome To My Ecommerce App");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        catRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
         categoryModelList = new ArrayList<>();
         categpryAdapter = new CategpryAdapter(getActivity(),categoryModelList);
        catRecyclerview.setAdapter(categpryAdapter);

        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document :task.getResult()){
                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categpryAdapter.notifyDataSetChanged();
                                linearLayout.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                                diaglogshow=1;

                            }
                        }else{
                            Toast.makeText(getActivity(),""+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        newProductRecylerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        newProductModelList = new ArrayList<>();
        newProductAdapter = new NewProductAdapter(getActivity(),newProductModelList);
        newProductRecylerView.setAdapter(newProductAdapter);


        db.collection("NewProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document :task.getResult()){
                                NewProductModel newProductModel = document.toObject(NewProductModel.class);
                                newProductModelList.add(newProductModel);
                                newProductAdapter.notifyDataSetChanged();

                            }
                        }else{
                            Toast.makeText(getActivity(),""+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //popular product

        popularRecylerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        popularProductModelList = new ArrayList<>();
        popularProductAdapter = new PopularProductAdapter(getActivity(),popularProductModelList);
        popularRecylerView.setAdapter(popularProductAdapter);


        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document :task.getResult()){
                                PopularProductModel popularProductModel = document.toObject(PopularProductModel.class);
                                popularProductModelList.add(popularProductModel);
                                popularProductAdapter.notifyDataSetChanged();

                            }
                        }else{
                            Toast.makeText(getActivity(),""+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        search_box = root.findViewById(R.id.search_box);
        showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(getContext(),showAllModelList);
        recyclerViewSearch = root.findViewById(R.id.search_rec);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(showAllAdapter);
        recyclerViewSearch.setHasFixedSize(true);
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().isEmpty()){
                    showAllModelList.clear();
                    showAllAdapter.notifyDataSetChanged();
                }else{
                    searchProduct(editable.toString());
                }

            }
        });


        return root;
    }

    private void searchProduct(String toString) {

        if(!toString.isEmpty()){
            db.collection("ShowAll").whereEqualTo("type",toString)
            .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful() && task.getResult() != null){
                                showAllModelList.clear();
                                showAllAdapter.notifyDataSetChanged();
                                for(DocumentSnapshot doc : task.getResult().getDocuments()){
                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
    }
}