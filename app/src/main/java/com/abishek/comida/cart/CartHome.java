package com.abishek.comida.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abishek.comida.R;
import com.abishek.comida.address.AddressHomePage;
import com.abishek.comida.cart.cartRoom.CartDaoAccess;
import com.abishek.comida.cart.cartRoom.ComidaDatabase;
import com.abishek.comida.commonFiles.LoginSessionManager;
import com.abishek.comida.commonFiles.MySingleton;
import com.abishek.comida.home.product.CategoryModel;
import com.abishek.comida.home.product.FoodModel;
import com.abishek.comida.home.product.ShopProductDetail;
import com.abishek.comida.loginAndSignUp.Login;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_IMAGE;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_PRODUCT_CATEGORY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_RESTAURANT_INFO;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.NO_OF_RETRY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.RETRY_SECONDS;
import static com.abishek.comida.commonFiles.LoginSessionManager.ACCESS_TOKEN;
import static com.abishek.comida.commonFiles.LoginSessionManager.TOKEN_TYPE;

public class CartHome extends AppCompatActivity implements View.OnClickListener,CartItemChangeListener {

    private String TAG = getClass().getSimpleName();
    private Button btnCheckout;
    private RecyclerView cartRecyclerView;
    int subTotal=0,discount=0,total=0;
    private TextView subTotalView,discountView,totalView,deliveryView,shopNameView,specialityView,addressView;
    private ImageView shopImageView;
    private LinearLayout emptyCart;
    private NestedScrollView mainLayout;
    private int partnerId;

    private LoginSessionManager loginSessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_home);

        loginSessionManager = new LoginSessionManager(CartHome.this);

        SharedPreferences pref = getSharedPreferences("partner_info",0);
        partnerId = pref.getInt("pid",0);

        btnCheckout = findViewById(R.id.btn_checkout);
        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        emptyCart = findViewById(R.id.empty_Cart);
        mainLayout = findViewById(R.id.main_layout);

        shopNameView = findViewById(R.id.shop_name);
        specialityView = findViewById(R.id.speciality);
        addressView = findViewById(R.id.address);
        shopImageView = findViewById(R.id.shop_image);

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
            case R.id.btn_checkout:
                if(!loginSessionManager.isLoggedIn()) {
                    startActivity(new Intent(CartHome.this, Login.class));
                    return;
                }
                startActivity(new Intent(CartHome.this, AddressHomePage.class).putExtra("from",2));
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


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            for (FoodModel food:foodList)
            {

                subTotal = subTotal+Integer.parseInt(food.getPrice())*food.getQuantity();
                discount = discount + Integer.parseInt(food.getDiscount())*food.getQuantity();
            }

            if(foodList.size()>0)
            {
                mainLayout.setVisibility(View.VISIBLE);
                emptyCart.setVisibility(View.GONE);
                fetchPartnerData();
            }


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

    @Override
    public void cartEmpty() {
        SharedPreferences pref = getSharedPreferences("partner_info",0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("pid",0);
        editor.apply();
        emptyCart.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    public void fetchPartnerData()
    {


        Log.e(TAG, "fetchPartner : called");

        final String URL = BASE_RESTAURANT_INFO + partnerId;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject subJson = jsonObject.getJSONObject("data");

                    String shopName = subJson.getString("shop_name");
                    String shopImage = subJson.getString("shop_image");
                    String address = subJson.getString("address");
                    String speciality = subJson.getString("speciality");

                    shopNameView.setText(shopName);
                    addressView.setText(address);
                    specialityView.setText(speciality);
                    Picasso.get().load(BASE_IMAGE+shopImage).into(shopImageView);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();

                String tokenType = new LoginSessionManager(CartHome.this).getUserDetailsFromSP().get(TOKEN_TYPE);
                String accessToken = new LoginSessionManager(CartHome.this).getUserDetailsFromSP().get(ACCESS_TOKEN);

                header.put("Accept", "application/json");
                header.put("Authorization", tokenType + " " + accessToken);

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                return super.getParams();
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(CartHome.this).addToRequestQueue(stringRequest);


    }

}