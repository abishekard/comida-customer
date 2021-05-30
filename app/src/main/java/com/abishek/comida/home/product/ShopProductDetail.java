package com.abishek.comida.home.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abishek.comida.R;
import com.abishek.comida.address.AddNewAddress;
import com.abishek.comida.cart.CartHome;
import com.abishek.comida.cart.cartRoom.CartDaoAccess;
import com.abishek.comida.cart.cartRoom.ComidaDatabase;
import com.abishek.comida.commonFiles.LoginSessionManager;
import com.abishek.comida.commonFiles.MySingleton;
import com.abishek.comida.home.adapters.ProductParentAdapter;
import com.abishek.comida.review.ReviewAdapter;
import com.abishek.comida.review.ReviewModel;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_GET_PARTNER_REVIEW;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_IMAGE;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_PRODUCT_CATEGORY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.NO_OF_RETRY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.RETRY_SECONDS;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.isNetworkAvailable;
import static com.abishek.comida.commonFiles.LoginSessionManager.ACCESS_TOKEN;
import static com.abishek.comida.commonFiles.LoginSessionManager.TOKEN_TYPE;

public class ShopProductDetail extends AppCompatActivity {

    private static final String TAG = "ShopProductDetails";

    private ArrayList<CategoryModel> categoryList;
    private RecyclerView categoryRecycler,reviewRecyclerView;
    private String shopName, address, rating, openTime, closeTime, available, shopImage, speciality, partnerId;
    private TextView shopNameV, specialityV, shopAddressV, itemCountV, ratingV;
    private ImageView shopImageV, availability;
    private int itemCount;
    private LinearLayout goTOCartLayout;
    private TextView cartItemCount,cartTotalPrice,btnToCart;
    private ArrayList<ReviewModel> reviewList;
    private TextView reviewHeading;
    private float totalRating=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_and_add);

        itemCount = 0;

        if (!isNetworkAvailable(ShopProductDetail.this)) {
            Toast.makeText(ShopProductDetail.this, "check your Internet connection", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        categoryList = new ArrayList<>();
        partnerId = getIntent().getStringExtra("partner_id");
        categoryRecycler = findViewById(R.id.category_food_recycler);
        shopNameV = findViewById(R.id.shop_name);
        shopAddressV = findViewById(R.id.shop_address);
        specialityV = findViewById(R.id.speciality);
        itemCountV = findViewById(R.id.item_count);
        ratingV = findViewById(R.id.rating_view);
        shopImageV = findViewById(R.id.shop_image);
        availability = findViewById(R.id.availability);
        goTOCartLayout = findViewById(R.id.go_to_cart_layout);
        reviewRecyclerView = findViewById(R.id.review_recycler_view);
        reviewHeading = findViewById(R.id.review_heading);

        cartItemCount = findViewById(R.id.cart_item_count);
        cartTotalPrice = findViewById(R.id.cart_total_price);
        btnToCart = findViewById(R.id.to_cart);

        btnToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopProductDetail.this, CartHome.class));
                finish();
            }
        });

        new FetchCartItems(ComidaDatabase.getDatabase(ShopProductDetail.this)).execute();
        fetchProductList();
        fetchReview();
    }

    public void fetchProductList() {


        Log.e(TAG, "fetchCategory : called");

        ProgressDialog progressDialog = new ProgressDialog(ShopProductDetail.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String URL = BASE_PRODUCT_CATEGORY + partnerId;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray subJson = jsonObject.getJSONArray("data");
                    partnerId = jsonObject.getString("partner_id");
                    shopName = jsonObject.getString("shop_name");
                    shopImage = jsonObject.getString("shop_image");
                    openTime = jsonObject.getString("open_time");
                    address = jsonObject.getString("address");
                    closeTime = jsonObject.getString("close_time");
                    speciality = jsonObject.getString("speciality");
                    available = jsonObject.getString("available");
                    rating = jsonObject.getString("rating");

                    for (int i = 0; i < subJson.length(); i++) {
                        String categoryName = subJson.getJSONObject(i).getString("category_name");
                        JSONArray childArray = subJson.getJSONObject(i).getJSONArray("category_data");
                        ArrayList<FoodModel> foodList = new ArrayList<>();
                        for (int j = 0; j < childArray.length(); j++) {
                            ++itemCount;
                            JSONObject childJson = childArray.getJSONObject(j);
                            String productId = childJson.getString("id");
                            String itemName = childJson.getString("item_name");
                            String itemImage = childJson.getString("item_image");
                            String price = childJson.getString("price");
                            String priceType = childJson.getString("price_type");
                            String discount = childJson.getString("discount");
                            String vegNonVeg = childJson.getString("veg_non_veg");
                            String category = childJson.getString("category");

                            foodList.add(new FoodModel(productId, itemName, itemImage, price, priceType, discount, vegNonVeg, category));

                        }

                        progressDialog.dismiss();

                        categoryList.add(new CategoryModel(categoryName, foodList));

                    }


                    setDataToView();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

                progressDialog.dismiss();
                Toast.makeText(ShopProductDetail.this, "server problem", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();

                String tokenType = new LoginSessionManager(ShopProductDetail.this).getUserDetailsFromSP().get(TOKEN_TYPE);
                String accessToken = new LoginSessionManager(ShopProductDetail.this).getUserDetailsFromSP().get(ACCESS_TOKEN);

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
        MySingleton.getInstance(ShopProductDetail.this).addToRequestQueue(stringRequest);


    }

    private void setDataToView() {
        categoryRecycler = findViewById(R.id.category_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShopProductDetail.this);
        ProductParentAdapter productParentAdapter = new ProductParentAdapter(categoryList,
                ShopProductDetail.this, partnerId, new GoToCartListener() {
            @Override
            public void itemAdded(int price) {

                setBottomCartAdd(price);
            }

            @Override
            public void increased(int price) {

                setCartIncrement(price);
            }

            @Override
            public void decreased(int price) {
                 setCartDecrement(price);
            }

            @Override
            public void cartClear() {
                goTOCartLayout.setVisibility(View.GONE);
            }
        });
        categoryRecycler.setAdapter(productParentAdapter);
        categoryRecycler.setLayoutManager(linearLayoutManager);
        productParentAdapter.notifyDataSetChanged();

        shopNameV.setText(shopName);
        shopAddressV.setText(address);
        specialityV.setText(speciality);
        Picasso.get().load(BASE_IMAGE + shopImage).into(shopImageV);

        if (available.equals("1"))
            availability.setImageDrawable(ContextCompat.getDrawable(ShopProductDetail.this, R.drawable.ic_open_tag));
        else {
            availability.setImageDrawable(ContextCompat.getDrawable(ShopProductDetail.this, R.drawable.ic_close_tag));

        }
     //   ratingV.setText(rating);
        itemCountV.setText(itemCount + "");

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        String currentTime = sdf.format(cal.getTime());
        Log.e(TAG, "........." + currentTime);
        String currHrs[] = currentTime.split(":");
        Log.e(TAG, "........." + Integer.parseInt(currHrs[0]));

        String cloTime[] = closeTime.split(":");
        Log.e(TAG, "........." + Integer.parseInt(cloTime[0]));

        String opnTime[] = openTime.split(":");
        Log.e(TAG, "........." + Integer.parseInt(opnTime[0]));

        if (Integer.parseInt(currHrs[0]) > Integer.parseInt(cloTime[0]) || Integer.parseInt(currHrs[0]) < Integer.parseInt(opnTime[0])) {
            availability.setImageDrawable(ContextCompat.getDrawable(ShopProductDetail.this, R.drawable.ic_close_tag));
        }

    }

    public void setBottomCart(int count,int price)
    {
        goTOCartLayout.setVisibility(View.VISIBLE);
        cartItemCount.setText(count+"");
        cartTotalPrice.setText(price+"");

    }
    public void setBottomCartAdd(int price)
    {
         if(goTOCartLayout.getVisibility()==View.VISIBLE)
         {
             int itemCount= Integer.parseInt(cartItemCount.getText().toString());
             int tPrice = Integer.parseInt(cartTotalPrice.getText().toString());
             tPrice = tPrice+price;
             ++itemCount;
             cartItemCount.setText(itemCount+"");
             cartTotalPrice.setText(tPrice+"");
         }
         else {
             setBottomCart(1,price);
         }
    }
    public void setCartIncrement(int price)
    {

        int tPrice = Integer.parseInt(cartTotalPrice.getText().toString());
        tPrice = tPrice+price;
        cartTotalPrice.setText(tPrice+"");
    }

    public void setCartDecrement(int price)
    {
        int tPrice = Integer.parseInt(cartTotalPrice.getText().toString());
        tPrice = tPrice-price;
        cartTotalPrice.setText(tPrice+"");
    }


    class FetchCartItems extends AsyncTask<Void, Void, Void> {

        private final CartDaoAccess cartDao;
        private List<FoodModel> cartFoodList;

        public FetchCartItems(ComidaDatabase instance) {
            cartDao = instance.getDaoAccess();
            cartFoodList = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cartFoodList = cartDao.getFoodList();



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int total=0;
            for (FoodModel food:cartFoodList) {

                total=total+Integer.parseInt(food.getPrice());
            }
            if(cartFoodList.size()>0)
            setBottomCart(cartFoodList.size(),total);


        }
    }


    public void fetchReview() {


        Log.e(TAG, "fetchReview : called");

        final String URL = BASE_GET_PARTNER_REVIEW;

        totalRating=0;

         reviewList  = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(jsonArray.length()==0)
                    {
                        return;
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                      String userId=jsonObject1.getString("user_id");
                      String partnerId=jsonObject1.getString("partner_id");
                      String orderId = jsonObject1.getString("order_id");
                      String comment = jsonObject1.getString("comment");
                      String rating = jsonObject1.getString("rating");
                      String createdAt = jsonObject1.getString("created_at");
                      String customerName = jsonObject1.getString("customer_name");
                      String customerImage= jsonObject1.getString("customer_image");

                       totalRating = totalRating + Integer.parseInt(rating);


                       reviewList.add(new ReviewModel(userId,partnerId,orderId,comment,rating,createdAt,customerName,customerImage));

                    }

                    reviewRecyclerView.setVisibility(View.VISIBLE);
                    reviewHeading.setVisibility(View.VISIBLE);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShopProductDetail.this);
                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    ReviewAdapter reviewAdapter = new ReviewAdapter(reviewList,ShopProductDetail.this);
                    reviewRecyclerView.setLayoutManager(linearLayoutManager);
                    reviewRecyclerView.setAdapter(reviewAdapter);
                    reviewAdapter.notifyDataSetChanged();
                    ratingV.setText((totalRating/jsonArray.length())+"");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

                Toast.makeText(ShopProductDetail.this, "server problem", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();

                String tokenType = new LoginSessionManager(ShopProductDetail.this).getUserDetailsFromSP().get(TOKEN_TYPE);
                String accessToken = new LoginSessionManager(ShopProductDetail.this).getUserDetailsFromSP().get(ACCESS_TOKEN);

                header.put("Accept", "application/json");
                header.put("Authorization", tokenType + " " + accessToken);

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> params = new HashMap<>();
                params.put("partner_id",partnerId);

                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ShopProductDetail.this).addToRequestQueue(stringRequest);


    }
}