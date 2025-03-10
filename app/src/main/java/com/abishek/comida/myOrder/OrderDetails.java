package com.abishek.comida.myOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.FtsOptions;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abishek.comida.MainActivity;
import com.abishek.comida.R;
import com.abishek.comida.commonFiles.LoginSessionManager;
import com.abishek.comida.commonFiles.MySingleton;
import com.abishek.comida.home.product.CategoryModel;
import com.abishek.comida.home.product.FoodModel;
import com.abishek.comida.home.product.ShopProductDetail;
import com.abishek.comida.myOrder.track.TrackOrder;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_GET_REVIEW;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_GIVE_REVIEW;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_IMAGE;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_ORDER_DETAIL;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_PRODUCT_CATEGORY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.NO_OF_RETRY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.RETRY_SECONDS;

public class OrderDetails extends AppCompatActivity implements RatingDialogListener {

    private static final String TAG = "OrderDetail";
    private Button btnTrackOrder;
    private String from;

    private TextView shopNameView,specialityView,shopAddressView,subTotalView,
            discountView,totalView,addressTypeView,addressView,orderIdView;
    private ImageView shopImageView;
    private RecyclerView foodRecyclerView;
    private String orderId;
    private String shopName,shopAddress,shopImage,speciality,partnerId,status,addressType,deliveredAddress,otp,comment;
    private List<OrderFoodModel> foodList;
    private int total=0,Isubtotal=0,Idiscount=0;
    private TextView otpView,commentView;
    private LinearLayout reviewLayout1,reviewLayout2;
    private RatingBar ratingBar;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        userId = new LoginSessionManager(OrderDetails.this).getUserDetailsFromSP().get("user_id");

        orderId = getIntent().getStringExtra("order_id");
        from = getIntent().getStringExtra("from");
        inItUi();
        foodList = new ArrayList<>();
    }

    public void inItUi()
    {

        foodRecyclerView = findViewById(R.id.order_recycler_view);
        btnTrackOrder = findViewById(R.id.track_order);
        specialityView = findViewById(R.id.speciality);
        shopNameView = findViewById(R.id.shop_name);
        shopImageView = findViewById(R.id.shop_image);
        shopAddressView = findViewById(R.id.shop_address);
        subTotalView = findViewById(R.id.sub_total);
        discountView = findViewById(R.id.discount);
        totalView = findViewById(R.id.total);
        addressTypeView = findViewById(R.id.address_type);
        addressView = findViewById(R.id.delivered_address);
        orderIdView = findViewById(R.id.order_id);
        reviewLayout1 = findViewById(R.id.review_layout1);
        reviewLayout2 = findViewById(R.id.review_layout2);
        ratingBar = findViewById(R.id.rating_bar);
        commentView = findViewById(R.id.review);

        otpView = findViewById(R.id.otp_view);

        btnTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderDetails.this, TrackOrder.class)
                        .putExtra("order_id",orderId).putExtra("stage",status).putExtra("from",2));
            }
        });

        reviewLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   showDialog();
            }
        });



        if(from.equals("history"))
        {
            btnTrackOrder.setVisibility(View.GONE);
            reviewLayout1.setVisibility(View.VISIBLE);
            otpView.setVisibility(View.GONE);
        }

        fetchOrderDetail();
    }



    public void fetchOrderDetail()
    {


        Log.e(TAG, "fetchCategory : called");

        final String URL = BASE_ORDER_DETAIL+orderId;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray subJson = jsonObject.getJSONArray("orders");
                    partnerId = jsonObject.getString("partner_id");
                    shopName = jsonObject.getString("shop_name");
                    shopImage = jsonObject.getString("shop_image");
                    speciality = jsonObject.getString("speciality");
                    addressType = jsonObject.getString("address_type");
                    deliveredAddress = jsonObject.getString("delivered_address");
                    shopImage = jsonObject.getString("shop_image");
                    shopAddress = jsonObject.getString("shop_address");
                    status = jsonObject.getString("status");
                    otp = jsonObject.getString("otp");
                    comment = jsonObject.getString("comment");



                        for(int i=0;i<subJson.length();i++)
                        {
                            JSONObject childJson = subJson.getJSONObject(i);
                            String discount =childJson.getString("discount");
                            String quantity=childJson.getString("quantity");
                            String price = childJson.getString("price");
                            String dateTime = childJson.getString("created_at");
                            String itemName=childJson.getString("item_name");
                            String itemImage=childJson.getString("item_image");
                            String priceType=childJson.getString("price_type");

                            Isubtotal = Isubtotal+(Integer.parseInt(price)*Integer.parseInt(quantity));
                            Idiscount = Idiscount +(Integer.parseInt(discount)*Integer.parseInt(quantity));
                            foodList.add(new OrderFoodModel(discount,quantity,price,dateTime,itemName,itemImage,priceType));

                        }

                        total = Isubtotal-Idiscount+25;






                    setDataToView();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

                Toast.makeText(OrderDetails.this,"server problem",Toast.LENGTH_SHORT).show();

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


                return super.getParams();
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(OrderDetails.this).addToRequestQueue(stringRequest);


    }

    private void setDataToView() {


        shopNameView.setText(shopName);
        specialityView.setText(speciality);
        otpView.setText("OTP : "+otp);
        shopAddressView.setText(shopAddress);
        Picasso.get().load(BASE_IMAGE+shopImage).into(shopImageView);
        addressTypeView.setText(addressType);
        addressView.setText(deliveredAddress);
        orderIdView.setText("#"+orderId);

        subTotalView.setText(Isubtotal+"");
        discountView.setText(Idiscount+"");
        totalView.setText(total+"");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderDetails.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        OrderDetailFoodAdapter orderDetailFoodAdapter = new OrderDetailFoodAdapter(foodList,OrderDetails.this);
        foodRecyclerView.setLayoutManager(linearLayoutManager);
        foodRecyclerView.setAdapter(orderDetailFoodAdapter);
        orderDetailFoodAdapter.notifyDataSetChanged();

        if(comment.equals("1") && from.equals("history"))
        {
            fetchReview();
        }
    }

    public void fetchReview()
    {


        Log.e(TAG, "fetchReview : called");

        final String URL = BASE_GET_REVIEW;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");
                    JSONObject subJson = jsonObject.getJSONArray("data").getJSONObject(0);
                    int rating = subJson.getInt("rating");
                    String comment = subJson.getString("comment");

                    reviewLayout1.setVisibility(View.GONE);
                    reviewLayout2.setVisibility(View.VISIBLE);
                    ratingBar.setRating(rating);
                    commentView.setText(comment);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

                Toast.makeText(OrderDetails.this,"server problem",Toast.LENGTH_SHORT).show();

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

                HashMap<String,String> params = new HashMap<>();
                params.put("order_id",orderId);

                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(OrderDetails.this).addToRequestQueue(stringRequest);


    }


    private void showDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(2)
                .setTitle("Rate this Order")
                .setDescription("Please select start for rating")
                .setCommentInputEnabled(true)
                .setStarColor(R.color.quantum_yellow)
                .setNoteDescriptionTextColor(R.color.grey)
                .setTitleTextColor(R.color.black)
                .setDescriptionTextColor(R.color.black)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.grey)
                .setCommentTextColor(R.color.black)
                .setCommentBackgroundColor(R.color.white)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(OrderDetails.this)
                 // only if listener is implemented by fragment
                .show();
    }


    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {

        setReview(i+"",s);

    }

    public void setReview(String rating,String comment)
    {


        Log.e(TAG, "setReview : called");

        final String URL = BASE_GIVE_REVIEW;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");
                    if(status == 200)
                    {
                        Toast.makeText(OrderDetails.this,"Review Submitted",Toast.LENGTH_SHORT).show();
                        finish();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

                Toast.makeText(OrderDetails.this,"server problem",Toast.LENGTH_SHORT).show();

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

                HashMap<String,String> params = new HashMap<>();
                params.put("order_id",orderId);
                params.put("user_id",userId);
                params.put("partner_id",partnerId);
                params.put("rating",rating);
                params.put("comment",comment);

                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(OrderDetails.this).addToRequestQueue(stringRequest);


    }
}