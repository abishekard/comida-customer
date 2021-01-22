package com.abishek.comida.home.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abishek.comida.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductChildAdapter extends RecyclerView.Adapter<ProductChildAdapter.ProductChildHolder> {

    private ArrayList<FoodModel> foodList;
    private Context context;


    public ProductChildAdapter(ArrayList<FoodModel> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_food_child_item,parent,false);


        return new ProductChildHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductChildHolder holder, int position) {

        holder.foodNameView.setText(foodList.get(position).getFoodName());
        holder.priceView.setText("₹"+foodList.get(position).getPrice()+"/"+foodList.get(position).getPrice_type());
        Picasso.get().load(foodList.get(position).getFoodImage()).into(holder.foodImageView);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class ProductChildHolder extends RecyclerView.ViewHolder{

        private TextView foodNameView;
        private TextView priceView;
        private ImageView btnIncrement,btnDecrement;
        private TextView quantityView;
        private Button btnAdd;
        private ImageView foodImageView;
        private LinearLayout quantityLayout;
        public ProductChildHolder(@NonNull View v) {
            super(v);

            foodNameView = v.findViewById(R.id.food_name);
            priceView = v.findViewById(R.id.price);
            btnIncrement = v.findViewById(R.id.increment);
            btnDecrement = v.findViewById(R.id.decrement);
            quantityView = v.findViewById(R.id.quantity);
            btnAdd = v.findViewById(R.id.btn_add);
            foodImageView = v.findViewById(R.id.food_image);
            quantityLayout = v.findViewById(R.id.quantity_layout);
        }
    }
}
