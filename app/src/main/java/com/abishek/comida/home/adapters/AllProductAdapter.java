package com.abishek.comida.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abishek.comida.R;
import com.abishek.comida.home.product.FoodModel;
import com.abishek.comida.home.product.ShopProductDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.AllProductHolder> {

    private ArrayList<FoodModel> productList;
    private Context context;

    public AllProductAdapter(ArrayList<FoodModel> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_deisgn1,parent,false);
        return new AllProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductHolder holder, int position) {

        holder.foodName.setText(productList.get(position).getFoodName());
        holder.shopName.setText(productList.get(position).getShopName());
        holder.price.setText("₹"+productList.get(position).getPrice()+"/"+productList.get(position).getPrice_type());
        holder.shopAddress.setText(productList.get(position).getAddress());
        Picasso.get().load(productList.get(position).getFoodImage()).into(holder.foodImage);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     context.startActivity(new Intent(context, ShopProductDetail.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class AllProductHolder extends RecyclerView.ViewHolder{

        private TextView shopName,foodName,shopAddress,price;
        private ImageView foodImage;
        private LinearLayout card;

        public AllProductHolder(@NonNull View v) {
            super(v);

            card = v.findViewById(R.id.card);
            shopName = v.findViewById(R.id.shop_name);
            foodName = v.findViewById(R.id.food_name);
            shopAddress = v.findViewById(R.id.shop_address);
            price = v.findViewById(R.id.price);
            foodImage =v.findViewById(R.id.food_image);
        }
    }
}
