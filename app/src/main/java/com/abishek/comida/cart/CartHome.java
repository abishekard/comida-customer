package com.abishek.comida.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abishek.comida.R;
import com.abishek.comida.cart.cartRoom.CartDaoAccess;
import com.abishek.comida.cart.cartRoom.ComidaDatabase;
import com.abishek.comida.home.product.FoodModel;

import java.util.List;

public class CartHome extends AppCompatActivity implements View.OnClickListener,CartItemChangeListener {

    private String TAG = getClass().getSimpleName();
    private Button btnCheckout;
    private RecyclerView cartRecyclerView;
    int subTotal=0,discount=0,total=0;
    private TextView subTotalView,discountView,totalView,deliveryView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_home);

        btnCheckout = findViewById(R.id.btn_checkout);
        cartRecyclerView = findViewById(R.id.cart_recycler_view);

        subTotalView = findViewById(R.id.sub_total);
        discountView = findViewById(R.id.discount);
        totalView = findViewById(R.id.total);
        deliveryView = findViewById(R.id.delivery_view);
        btnCheckout.setOnClickListener(this);

        new FetchCartItems(ComidaDatabase.getDatabase(CartHome.this)).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_checkout: startActivity(new Intent(CartHome.this,Checkout.class));
                break;
        }
    }



    class FetchCartItems extends AsyncTask<Void, Void, Void> {

        private final CartDaoAccess cartDao;
        private List<FoodModel> foodList;


        public FetchCartItems(ComidaDatabase instance) {
            cartDao = instance.getDaoAccess();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            foodList = cartDao.getFoodList();
            Log.e(TAG,"......size :"+foodList.size());

            for (FoodModel food:foodList)
            {

                subTotal = subTotal+Integer.parseInt(food.getPrice())*food.getQuantity();
                discount = discount + Integer.parseInt(food.getDiscount())*food.getQuantity();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
             setDataTOView(foodList);

             subTotalView.setText(subTotal+"");
             discountView.setText(discount+"");
             totalView.setText((subTotal-discount+25)+"");

        }
    }

    public void setDataTOView(List<FoodModel> foodList)
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartHome.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        CartRecyclerAdapter cartRecyclerAdapter = new CartRecyclerAdapter(CartHome.this,foodList);
        cartRecyclerView.setAdapter(cartRecyclerAdapter);
        cartRecyclerView.setLayoutManager(linearLayoutManager);
        cartRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void cartQuantityChanged(int quantity, int price, int discount,String IorD) {

        if(IorD.equals("i"))
        {
            String currentSubtotal = subTotalView.getText().toString();
            String currentDiscount = discountView.getText().toString();
            int newSubtotal = Integer.parseInt(currentSubtotal)+price;
            int newDiscount = Integer.parseInt(currentDiscount)+discount;
            subTotalView.setText(newSubtotal+"");
            discountView.setText(newDiscount+"");
            totalView.setText((newSubtotal-newDiscount)+"");
        }
        if(IorD.equals("d"))
        {
            String currentSubtotal = subTotalView.getText().toString();
            String currentDiscount = discountView.getText().toString();
            int newSubtotal = Integer.parseInt(currentSubtotal)-price;
            int newDiscount = Integer.parseInt(currentDiscount)-discount;
            subTotalView.setText(newSubtotal+"");
            discountView.setText(newDiscount+"");
            totalView.setText((newSubtotal-newDiscount+25)+"");
        }
        Log.e(TAG,quantity+"  "+price+"  "+discount);

    }

    @Override
    public void cartItemRemoved(String productId, int quantity, int price, int discount) {

        Log.e(TAG,quantity+"  "+price+"  "+discount);
        String currentSubtotal = subTotalView.getText().toString();
        String currentDiscount = discountView.getText().toString();
        int newSubtotal = Integer.parseInt(currentSubtotal)-(price*quantity);
        int newDiscount = Integer.parseInt(currentDiscount)-(discount*quantity);
        subTotalView.setText(newSubtotal+"");
        discountView.setText(newDiscount+"");
        totalView.setText((newSubtotal-newDiscount+25)+"");
    }
}