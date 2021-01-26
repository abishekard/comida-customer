package com.abishek.comida.cart;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abishek.comida.R;
import com.abishek.comida.cart.cartRoom.CartDaoAccess;
import com.abishek.comida.cart.cartRoom.ComidaDatabase;
import com.abishek.comida.cart.dialog.ClearCartForNewRestaurant;
import com.abishek.comida.home.product.FoodModel;

import java.util.List;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>  {

    private static final String TAG = "cartRecyclerAdapter";
    private Context context;
    private List<FoodModel> foodList;
    private CartItemChangeListener cartItemChangeListener;

    public CartRecyclerAdapter(Context context, List<FoodModel> foodList) {
        this.context = context;
        this.foodList = foodList;
        cartItemChangeListener = (CartItemChangeListener) context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_food_recycler_item,parent,false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        int price,discount,qty;


        price = Integer.parseInt(foodList.get(position).getPrice());
        discount = Integer.parseInt(foodList.get(position).getDiscount());
        qty = foodList.get(position).getQuantity();
        holder.foodNameView.setText(foodList.get(position).getFoodName());
        holder.priceView.setText(foodList.get(position).getPrice());
        holder.discountView.setText(foodList.get(position).getDiscount());
        holder.quantityView.setText(foodList.get(position).getQuantity()+"");
        holder.priceView.setText((price*qty)+"");
        holder.discountView.setText((discount*qty)+"");
        Log.e(TAG,"original_qty: "+qty);

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = foodList.get(position).getQuantity();
                if(qty!=1)
                {
                    Log.e(TAG,"original_qty: "+qty);
                    holder.quantityView.setText(--qty+"");
                    new InsertQuantity(ComidaDatabase.getDatabase(context),qty,
                            Integer.parseInt(foodList.get(position).getProductId())).execute();
                    holder.priceView.setText((price*qty)+"");
                    holder.discountView.setText((discount*qty)+"");
                    Log.e(TAG,"afterI_qty: "+qty);
                    cartItemChangeListener.cartQuantityChanged(qty,price,discount,"d");
                    foodList.get(position).setQuantity(qty);
                }
            }
        });
        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty =foodList.get(position).getQuantity();
                if(qty!=10)
                {
                    Log.e(TAG,"original_qty: "+qty);
                    holder.quantityView.setText(++qty+"");
                    new InsertQuantity(ComidaDatabase.getDatabase(context),qty,
                            Integer.parseInt(foodList.get(position).getProductId())).execute();
                    holder.priceView.setText((price*qty)+"");
                    holder.discountView.setText((discount*qty)+"");

                    Log.e(TAG,"afterD_qty: "+qty);
                    cartItemChangeListener.cartQuantityChanged(qty,price,discount,"i");
                    foodList.get(position).setQuantity(qty);
                }
            }
        });
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cartItemChangeListener.cartItemRemoved(foodList.get(position).getProductId(),qty,price,discount);
                new DeleteCartItem(ComidaDatabase.getDatabase(context),foodList.get(position)
                        .getProductId(),position).execute();




            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }



    public class CartViewHolder extends RecyclerView.ViewHolder{

        private TextView foodNameView,priceView,discountView,quantityView;
        private ImageView increment,decrement,btnRemove;
        public CartViewHolder(@NonNull View v) {
            super(v);

            foodNameView = v.findViewById(R.id.food_name);
            priceView = v.findViewById(R.id.price);
            discountView = v.findViewById(R.id.discount);
            increment = v.findViewById(R.id.increment);
            decrement= v.findViewById(R.id.decrement);
            btnRemove = v.findViewById(R.id.btn_remove);
            quantityView  =v.findViewById(R.id.quantity);
        }
    }

    class DeleteCartItem extends AsyncTask<Void, Void, Void> {

        private final CartDaoAccess cartDao;
        private String productId;
        private int position;


        public DeleteCartItem(ComidaDatabase instance,String productId,int position) {
            cartDao = instance.getDaoAccess();
            this.productId = productId;
            this.position = position;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cartDao.deleteByProductId(Integer.parseInt(productId));





            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            notifyItemRemoved(position);
            notifyItemRangeChanged(position,foodList.size());
            foodList.remove(position);
            if(foodList.size()==0)
                cartItemChangeListener.cartEmpty();
            Log.e(TAG,"......size :"+foodList.size()+".....position: "+position);
        }
    }

    class InsertQuantity extends AsyncTask<Void, Void, Void> {

        private final CartDaoAccess cartDao;

        private int qty,pId;


        public InsertQuantity(ComidaDatabase instance,int qty,int pId) {
            cartDao = instance.getDaoAccess();
            this.qty = qty;
            this.pId = pId;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            cartDao.setQuantity(qty,pId);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }



}
