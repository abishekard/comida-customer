package com.abishek.comida.myOrder;

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
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_IMAGE;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.getDate;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.getTime;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<MyOrderModel> orderList;
    private Context context;
    private String from;

    public OrderAdapter(List<MyOrderModel> orderList, Context context,String from) {
        this.orderList = orderList;
        this.context = context;
        this.from=from;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_list_design,parent,false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {


        String dateTime[] = orderList.get(position).getCreateAt().split(" ");
        String formattedDateTime = getDate(dateTime[0])+"  "+getTime(dateTime[1]);
        holder.orderIdView.setText("#"+orderList.get(position).getOrderId());
        holder.dateView.setText(formattedDateTime);
        holder.priceView.setText(orderList.get(position).getTotalPrice());
        holder.addressView.setText(orderList.get(position).getDeliveredAddress());
        Picasso.get().load(BASE_IMAGE+orderList.get(position).getImage()).into(holder.foodImageView);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 context.startActivity(new Intent(context,OrderDetails.class).putExtra("from",from)
                         .putExtra("order_id",orderList.get(position).getOrderId()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{

        private TextView orderIdView,dateView,addressView,priceView;
        private ImageView foodImageView;
        private LinearLayout card;

        public OrderViewHolder(@NonNull View v) {
            super(v);
            orderIdView = v.findViewById(R.id.order_id);
            dateView = v.findViewById(R.id.date);
            addressView = v.findViewById(R.id.delivered_address);
            priceView = v.findViewById(R.id.total);
            foodImageView = v.findViewById(R.id.food_image);
            card = v.findViewById(R.id.card);
        }
    }
}
