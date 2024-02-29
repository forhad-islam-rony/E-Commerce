package com.rony.ecommerceapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rony.ecommerceapp.R;
import com.rony.ecommerceapp.activities.ShowAllActivity;
import com.rony.ecommerceapp.models.CategoryModel;

import java.util.List;

public class CategpryAdapter extends RecyclerView.Adapter<CategpryAdapter.ViewHolder> {
    private Context context;
    private List<CategoryModel> list;

    public CategpryAdapter(Context context,List<CategoryModel> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.catagory_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(list.get(position).getImg_url()).into(holder.catimage);
        holder.catname.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(context, ShowAllActivity.class);
                myintent.putExtra("type",list.get(holder.getAdapterPosition()).getType());
                context.startActivity(myintent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView catimage;
        TextView catname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            catimage  = itemView.findViewById(R.id.cat_img);
            catname = itemView.findViewById(R.id.cat_name);
        }
    }
}
