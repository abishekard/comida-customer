package com.abishek.comida.home.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abishek.comida.R;
import com.abishek.comida.cart.cartRoom.CartDaoAccess;
import com.abishek.comida.cart.cartRoom.ComidaDatabase;
import com.abishek.comida.cart.dialog.ClearCartForNewRestaurant;
import com.abishek.comida.home.product.FoodModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_IMAGE;

public class ProductChildAdapter extends RecyclerView.Adapter<ProductChildAdapter.ProductChildHolder> {

    private List<FoodModel> foodList, cartFoodList;
    private Context context;
    private int qty;
    private String TAG = "productChildAdapter";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int currentPid;

    public ProductChildAdapter(ArrayList<FoodModel> foodList, Context context,String currentPid) {
        this.foodList = foodList;
        this.context = context;
        this.currentPid = Integer.parseInt(currentPid);
        new FetchCartItems(ComidaDatabase.getDatabase(context)).execute();
        pref = context.getSharedPreferences("partner_info", 0);
        editor = pref.edit();

    }

    @NonNull
    @Override
    public ProductChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_food_child_item, parent, false);


        return new ProductChildHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductChildHolder holder, int position) {

        if (cartFoodList.size() > 0)
            for (FoodModel food : cartFoodList) {
                if (food.getProductId().equals(foodList.get(position).getProductId())) {
                    holder.quantityLayout.setVisibility(View.VISIBLE);
                    holder.btnAdd.setVisibility(View.GONE);
                    holder.quantityView.setText(food.getQuantity() + "");
                }
            }

        holder.foodNameView.setText(foodList.get(position).getFoodName());
        holder.priceView.setText("₹" + foodList.get(position).getPrice() + "/" + foodList.get(position).getPrice_type());
        Picasso.get().load(BASE_IMAGE + foodList.get(position).getFoodImage()).into(holder.foodImageView);
        qty = foodList.get(position).getQuantity();
        Log.e(TAG, "original_qty: " + qty);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                int pId = pref.getInt("pid", 0);
                Log.e(TAG,".......currentP: "+currentPid+" pid: "+pId);
                if (pId == 0) {
                    editor.putInt("pid", currentPid);

                    editor.apply();
                    pId=currentPid;

                }

                if (pId !=currentPid ) {
                    /*ClearCartForNewRestaurant clearCartForNewRestaurant = new ClearCartForNewRestaurant();
                    clearCartForNewRestaurant.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.setting_dialog);
                    clearCartForNewRestaurant.show(((FragmentActivity) context).getSupportFragmentManager(), "dialog");*/
                    clearCartDialog();
                }
                else {

                    holder.btnAdd.setVisibility(View.GONE);
                    holder.quantityLayout.setVisibility(View.VISIBLE);

                    new InsertCartItems(ComidaDatabase.getDatabase(context), foodList.get(position)).execute();
                    ///  new FetchCartItems(ComidaDatabase.getDatabase(context)).execute();
                }
            }
        });

        holder.btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qty != 10) {
                    holder.quantityView.setText(++qty + "");
                    foodList.get(position).setQuantity(qty);
                    Log.e(TAG, "afterI_qty: " + qty);
                    new InsertQuantity(ComidaDatabase.getDatabase(context), qty,
                            Integer.parseInt(foodList.get(position).getProductId())).execute();
                }
            }
        });
        holder.btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qty != 1) {
                    holder.quantityView.setText(--qty + "");
                    foodList.get(position).setQuantity(qty);
                    Log.e(TAG, "afterD_qty: " + qty);
                    new InsertQuantity(ComidaDatabase.getDatabase(context), qty,
                            Integer.parseInt(foodList.get(position).getProductId())).execute();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }


    public static class ProductChildHolder extends RecyclerView.ViewHolder {

        private TextView foodNameView;
        private TextView priceView;
        private ImageView btnIncrement, btnDecrement;
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


    class InsertCartItems extends AsyncTask<Void, Void, Void> {

        private final CartDaoAccess cartDao;

        private FoodModel food;


        public InsertCartItems(ComidaDatabase instance, FoodModel food) {
            cartDao = instance.getDaoAccess();
            this.food = food;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e(TAG, "done");
            cartDao.insert(food);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e(TAG, "done");
        }
    }


    class FetchCartItems extends AsyncTask<Void, Void, Void> {

        private final CartDaoAccess cartDao;


        public FetchCartItems(ComidaDatabase instance) {
            cartDao = instance.getDaoAccess();
            cartFoodList = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cartFoodList = cartDao.getFoodList();
            Log.e(TAG, "......size :" + foodList.size());


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }

    class InsertQuantity extends AsyncTask<Void, Void, Void> {

        private final CartDaoAccess cartDao;

        private int qty, pId;


        public InsertQuantity(ComidaDatabase instance, int qty, int pId) {
            cartDao = instance.getDaoAccess();
            this.qty = qty;
            this.pId = pId;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e(TAG, "cart_qty" + qty);
            cartDao.setQuantity(qty, pId);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    class ClearCart extends AsyncTask<Void, Void, Void> {

        private final CartDaoAccess cartDao;

        public ClearCart(ComidaDatabase instance) {
            cartDao = instance.getDaoAccess();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            cartDao.deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            editor.putInt("pid",currentPid);
            editor.apply();

        }
    }



    private void clearCartDialog() {

        final AlertDialog alertDialog;

        alertDialog = new AlertDialog.Builder(context,R.style.setting_dialog).create();

        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.fragment_clear_cart_for_new_restaurant,null);

        alertDialog.setView(view);

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);



        TextView btnOk = view.findViewById(R.id.btn_ok);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearCart(ComidaDatabase.getDatabase(context)).execute();
                alertDialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
