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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.razorpay.PaymentResultListener;

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
import static com.abishek.comida.commonFiles.LoginSessionManager.ACCESS_TOKEN;
import static com.abishek.comida.commonFiles.LoginSessionManager.TOKEN_TYPE;

public class Checkout extends AppCompatActivity implements View.OnClickListener,
        OrderConfirmedDialog.ConfirmationDialogListener, PaymentResultListener {

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
    private RadioGroup paymentMethodGroup;
    private String paymentMethod;
    private String generatedOrderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        paymentMethod = "online";
        generatedOrderId = System.currentTimeMillis() / 10L + "";
        Log.e(TAG,"generatedOrderId  "+generatedOrderId);

        btnConfirm = findViewById(R.id.btn_confirm_order);
        btnChange = findViewById(R.id.change_address);
        addressView = findViewById(R.id.address);
        addressNameView = findViewById(R.id.address_name);

        subTotalView = findViewById(R.id.sub_total);
        discountView = findViewById(R.id.discount);
        totalView = findViewById(R.id.total);
        paymentMethodGroup = findViewById(R.id.payment_method);


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


        paymentMethodGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton btn = findViewById(checkedId);
                if(btn.getText().toString().toLowerCase().equals("pay now"))
                    paymentMethod="online";
                if(btn.getText().toString().toLowerCase().equals("pay on delivery"))
                    paymentMethod="cod";
                Log.e(TAG,"...."+paymentMethod);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_order:
                if(paymentMethod.equals("cod"))
                   getAllDetailsToPlaceOrder();
                else
                {
                    total  = subTotal-discount+25;
                    startPayment((total*100)+"");
                }

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

    public void startPayment(String amountInPaise) {

        final Activity activity = this;

        final com.razorpay.Checkout co = new com.razorpay.Checkout();


        try {
            JSONObject options = new JSONObject();
            options.put("name", "Comida");
            options.put("description", "Order-ID #"+generatedOrderId);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", amountInPaise);

            JSONObject preFill = new JSONObject();
            preFill.put("email", new LoginSessionManager(Checkout.this).getUserDetailsFromSP().get("email"));
            preFill.put("contact",new LoginSessionManager(Checkout.this).getUserDetailsFromSP().get("mobile"));
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }
    @Override
    public void onPaymentSuccess(String s) {
           getAllDetailsToPlaceOrder();
       // Toast.makeText(Checkout.this,"success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
         Toast.makeText(Checkout.this,"Payment Failed",Toast.LENGTH_SHORT).show();
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


    public void placeOrder(String userId, String addressId, String productId, String quantity,
                           String total, String partnerId) {


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
                    btnConfirm.setEnabled(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                btnConfirm.setEnabled(true);
                Toast.makeText(Checkout.this, "server problem", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();

                String tokenType = new LoginSessionManager(Checkout.this).getUserDetailsFromSP().get(TOKEN_TYPE);
                String accessToken = new LoginSessionManager(Checkout.this).getUserDetailsFromSP().get(ACCESS_TOKEN);

                header.put("Accept", "application/json");
                header.put("Authorization", tokenType + " " + accessToken);

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
                params.put("payment_method",paymentMethod);
                params.put("order_id",generatedOrderId);

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