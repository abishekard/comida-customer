package com.abishek.comida.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abishek.comida.R;
import com.abishek.comida.home.product.CategoryModel;

import java.util.ArrayList;

public class ProductParentAdapter extends RecyclerView.Adapter<ProductParentAdapter.ProductParentHolder> {

    private ArrayList<CategoryModel> categoryList;
    private Context context;
    private String pId;

    public ProductParentAdapter(ArrayList<CategoryModel> categoryList, Context context,String pId) {
        this.categoryList = categoryList;
        this.context = context;
        this.pId = pId;
    }

    @NonNull
    @Override
    public ProductParentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_food_parent_item,parent,false);
        return new ProductParentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductParentHolder holder, int position) {

        ProductChildAdapter productChildAdapter = new ProductChildAdapter(categoryList.get(position).getFoodList(),context,pId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        holder.childRecycler.setLayoutManager(linearLayoutManager);
        holder.childRecycler.setAdapter(productChildAdapter);
        holder.categoryName.setText(categoryList.get(position).getCategoryName());

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ProductParentHolder extends RecyclerView.ViewHolder{

        private RecyclerView childRecycler;
        private TextView categoryName;

        public ProductParentHolder(@NonNull View v) {
            super(v);

            childRecycler = v.findViewById(R.id.category_food_recycler);
            categoryName = v.findViewById(R.id.category_name);
        }
    }
}
