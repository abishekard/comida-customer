package com.abishek.comida.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.abishek.comida.commonFiles.LoginSessionManager;
import com.abishek.comida.commonFiles.MySingleton;
import com.abishek.comida.home.HomePage;
import com.abishek.comida.home.product.FoodModel;
import com.abishek.comida.home.product.RestaurantModel;
import com.abishek.comida.myOrder.track.TrackOrder;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_PLACE_ORDER;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_RESTAURANT_ALL;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.NO_OF_RETRY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.RETRY_SECONDS;

public class Checkout extends AppCompatActivity implements View.OnClickListener, OrderConfirmedDialog.ConfirmationDialogListener {

    private Button btnConfirm;
    private String addressId;
    private String TAG = "Chekout";
    private String address, addressName;
    private TextView addressView, addressNameView, btnChange;
    private int subTotal = 0, discount = 0;
    private TextView subTotalView, discountView, totalView;
    private String newOrderId;
    private String userId,productId,quantity;
    private int partnerId,total;
    private   SharedPreferences pref;
    private SharedPreferences.Editor editor;


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

        pref = getSharedPreferences("partner_info",0);
        editor = pref.edit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_order: getAllDetailsToPlaceOrder();
               // showConfirmationDialog();

                break;
            case R.id.change_address:
                finish();
                break;

        }
    }

    @Override
    public void backToHomeClicked() {
        clearingStackAndMoveToHomePage();
    }

    @Override
    public void trackOrderClicked() {
          clearingStackAndMoveToTrackOrder();
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
            Log.e(TAG, "......size :" + foodList.size());


            for (FoodModel food : foodList) {

                if(productId==null)
                    productId = food.getProductId();
                else
                productId=productId+","+food.getProductId();

                if(quantity==null)
                    quantity = food.getQuantity()+"";
                else
                    quantity=quantity+","+food.getQuantity();

                subTotal = subTotal + Integer.parseInt(food.getPrice()) * food.getQuantity();
                discount = discount + Integer.parseInt(food.getDiscount()) * food.getQuantity();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (foodList.size() == 0) {
                Toast.makeText(Checkout.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            subTotalView.setText(subTotal + "");
            discountView.setText(discount + "");
            totalView.setText((subTotal - discount + 25) + "");

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
            showConfirmationDialog();
            editor.putInt("pid",0);
            editor.apply();
            Log.e(TAG, "......cart cleared");
        }
    }


    public void placeOrder(String userId, String addressId, String productId, String quantity, String total, String partnerId) {


        btnConfirm.setEnabled(false);

        Log.e(TAG, "placeOrder : called");

        final String URL = BASE_PLACE_ORDER;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");
                    if (status == 200) {
                        newOrderId = jsonObject.getString("orderId");
                        new ClearCart(ComidaDatabase.getDatabase(Checkout.this)).execute();

                        return;

                    }

                    Toast.makeText(Checkout.this, "server problem", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

                Toast.makeText(Checkout.this, "server problem", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                /*Map<String, String> header = new HashMap<>();

                String tokenType = new LoginSessionManager(getContext()).getUserDetailsFromSP().get(TOKEN_TYPE);
                String accessToken = new LoginSessionManager(getContext()).getUserDetailsFromSP().get(ACCESS_TOKEN);

                //String fullKey = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjcxNDdmNGFjNWFlN2IzZDM4MmYyNTAwNWVhNTIwOGUyNDAzNjYwNzMyOWMyYjZiYWQ1YTlhMmNlZTEzZDI3ZTgzN2RkOTY5NzcxNWNhMzUxIn0.eyJhdWQiOiIxIiwianRpIjoiNzE0N2Y0YWM1YWU3YjNkMzgyZjI1MDA1ZWE1MjA4ZTI0MDM2NjA3MzI5YzJiNmJhZDVhOWEyY2VlMTNkMjdlODM3ZGQ5Njk3NzE1Y2EzNTEiLCJpYXQiOjE1NTExOTc0MjcsIm5iZiI6MTU1MTE5NzQyNywiZXhwIjoxNTgyNzMzNDI3LCJzdWIiOiI4NSIsInNjb3BlcyI6W119.kLmk7mEukKdoS9e_v31VQX29ypn7hJb7qAJvKA_GqeiYEYe2EQ9zLTd1IwO-S31CofoypnJ-LvAT7D4I0EZ9iYM1AS5A6-7bWH3-h01-glLQubbfedhvlg0xfT60s2r1onxlEMUnt-0kB2tbYgX_df4zJPExUhHRpzlnLNChzC3r1QD1dzgn-814GjxlQkwfgv_5dsKzyMlvVCHiTDg2z35h2uiWeRuVhmznbUGaGCWcxPwHpNV4k9pHOH9yrCwkjJuHlcSIiXD7W_QsRnzEa_dY6wASdymtGqHb99c3kfWmiKKwngAC9GY56OeMP0vLnYpXOAspu5rDlQkLCzCeh58KnqbqMUrQ0bZ3ChTaeATXM_fncQiByfMgAAfiVfu8GpKsnQKSYobzcqrqjmAgPTNEcq5ba4BCUuw1ysv0LodTqHGUHsSNsiZfx3GyqLoyOCMWY5oWO4M4saOTo3pUSGPSq15BsqRQXqbvzshxk9ysaAU1K9dZj-AZpy4mUxf3y4UX8-EADqJmYV7ywEph_FveDbdWNNUF72bqbTg8DTxwJ6V53cEOsxbmNb82jFJnz1vSxLFDDXv9Vvf23W5hm4Io2Ogxv8wyE5vNUgL2XepFrGwWWANEsp4fLebzfgFD3045vkrcfRPc164LVKHdLyaHhxB8TrYeK9TOqeEfk7M";

                header.put("Accept", "application/json");
                header.put("Authorization", tokenType + " " + accessToken);*/

                return super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("address_id", addressId);
                params.put("product_id", productId);
                params.put("quantity", quantity);
                params.put("total_price", total);
                params.put("partner_id", partnerId);

                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(Checkout.this).addToRequestQueue(stringRequest);


    }

    public void showConfirmationDialog() {
        OrderConfirmedDialog orderConfirmedDialog = new OrderConfirmedDialog();
        orderConfirmedDialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.dialog);
        orderConfirmedDialog.show(getSupportFragmentManager(), "order_confirm");
    }


    public void clearingStackAndMoveToHomePage() {

        Log.e("1", "called : clearingStackAndMoveToHomePage");

        Intent i = new Intent(Checkout.this, HomePage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finishAffinity();
    }
    public void clearingStackAndMoveToTrackOrder() {

        if(newOrderId != null) {
            Log.e("1", "called : clearingStackAndMoveToTrackOrder");

            Intent i = new Intent(Checkout.this, TrackOrder.class);
            i.putExtra("order_id", newOrderId);
            i.putExtra("stage",1+"");
            i.putExtra("from",1);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finishAffinity();
        }
        else{
            Toast.makeText(Checkout.this,"something went wrong",Toast.LENGTH_SHORT).show();
        }
    }

    public  void getAllDetailsToPlaceOrder()
    {
       partnerId = pref.getInt("pid",0);
       userId = new LoginSessionManager(Checkout.this).getUserDetailsFromSP().get("user_id");
       total  = subTotal-discount+25;

       Log.e(TAG,"...userId: "+userId+" addressId: "+addressId+"...produceId: "+productId+
            "...quantity: "+quantity+"...total: "+total+"...partnerId: "+partnerId  ) ;
       placeOrder(userId,addressId,productId,quantity,total+"",partnerId+"");
    }
}