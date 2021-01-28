package com.abishek.comida.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abishek.comida.R;
import com.abishek.comida.cart.cartRoom.CartDaoAccess;
import com.abishek.comida.cart.cartRoom.ComidaDatabase;
import com.abishek.comida.home.product.FoodModel;

import java.util.List;

public class Checkout extends AppCompatActivity implements View.OnClickListener {

    private Button btnConfirm;
    private String addressId;
    private String TAG = "Chekout";
    private String address, addressName;
    private TextView addressView, addressNameView, btnChange;
    private int subTotal=0,discount=0;
    private TextView subTotalView,discountView,totalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        btnConfirm = findViewById(R.id.btn_confirm_order);
        btnChange = findViewById(R.id.change_address);
        addressView = findViewById(R.id.address);
        addressNameView = findViewById(R.id.address_name);

        subTotalView = findViewById(R.id.sub_total);
        discountView = findViewById(R.id.discount);
        totalView = findViewById(R.id.total);


        addressId = getIntent().getStringExtra("address_id");
        address = getIntent().getStringExtra("address");
        addressName = getIntent().getStringExtra("address_name");
        Log.e(TAG, "........addressId:" + addressId);

        addressView.setText(address);
        addressNameView.setText(addressName);
        btnChange.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        new FetchCartItems(ComidaDatabase.getDatabase(Checkout.this)).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_order:
                OrderConfirmedDialog orderConfirmedDialog = new OrderConfirmedDialog();
                orderConfirmedDialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.dialog);
                orderConfirmedDialog.show(getSupportFragmentManager(), "order_confirm");
                break;
            case R.id.change_address:
                finish();
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

            if(foodList.size()==0)
            {
                Toast.makeText(Checkout.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            subTotalView.setText(subTotal+"");
            discountView.setText(discount+"");
            totalView.setText((subTotal-discount+25)+"");

        }
    }
}