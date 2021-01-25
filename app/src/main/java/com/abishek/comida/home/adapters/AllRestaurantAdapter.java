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
import com.abishek.comida.home.product.RestaurantModel;
import com.abishek.comida.home.product.ShopProductDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_IMAGE;

public class AllRestaurantAdapter extends RecyclerView.Adapter<AllRestaurantAdapter.RestaurantHolder> {

    private List<RestaurantModel> restaurantList;
    private Context context;

    public AllRestaurantAdapter(List<RestaurantModel> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_recycler_item,parent,false);
        return new RestaurantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantHolder holder, int position) {

        holder.shopNameView.setText(restaurantList.get(position).getShopName());
        holder.specialityView.setText(restaurantList.get(position).getSpeciality());
        holder.ratingView.setText(restaurantList.get(position).getRating());
        holder.addressView.setText(restaurantList.get(position).getAddress());
        Picasso.get().load(BASE_IMAGE+restaurantList.get(position).getShopImage()).into(holder.shopImageView);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ShopProductDetail.class).
                        putExtra("partner_id",restaurantList.get(position).getId()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class RestaurantHolder extends RecyclerView.ViewHolder{

        private TextView shopNameView,specialityView,addressView,ratingView;
        private ImageView shopImageView;
        private LinearLayout card;
        public RestaurantHolder(@NonNull View v) {
            super(v);
            shopNameView = v.findViewById(R.id.shop_name);
            addressView = v.findViewById(R.id.shop_address);
            specialityView = v.findViewById(R.id.speciality);
            ratingView = v.findViewById(R.id.rating);
            shopImageView = v.findViewById(R.id.shop_image);
            card = v.findViewById(R.id.card);


        }
    }
}
