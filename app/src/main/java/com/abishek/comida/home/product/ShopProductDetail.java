package com.abishek.comida.home.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.abishek.comida.R;
import com.abishek.comida.commonFiles.MySingleton;
import com.abishek.comida.home.adapters.ProductParentAdapter;
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
import java.util.Map;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_IMAGE;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_PRODUCT_CATEGORY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.NO_OF_RETRY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.RETRY_SECONDS;

public class ShopProductDetail extends AppCompatActivity {

    private static final String TAG ="ShopProductDetails" ;

    private ArrayList<CategoryModel> categoryList;
    private RecyclerView categoryRecycler;
    private String shopName,address,rating,openTime,closeTime,shopImage,speciality,partnerId;
    private TextView shopNameV,specialityV,shopAddressV,itemCountV;
    private ImageView shopImageV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_and_add);

        categoryList = new ArrayList<>();
        partnerId = getIntent().getStringExtra("partner_id");
        categoryRecycler = findViewById(R.id.category_food_recycler);
        shopNameV = findViewById(R.id.shop_name);
        shopAddressV = findViewById(R.id.shop_address);
        specialityV = findViewById(R.id.speciality);
        itemCountV =findViewById(R.id.item_count);
        shopImageV = findViewById(R.id.shop_image);
        fetchProductList();
    }

    public void fetchProductList()
    {


        Log.e(TAG, "fetchCategory : called");

        final String URL = BASE_PRODUCT_CATEGORY+partnerId;


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
                    for(int i=0;i<subJson.length();i++)
                    {
                        String categoryName = subJson.getJSONObject(i).getString("category_name");
                        JSONArray childArray = subJson.getJSONObject(i).getJSONArray("category_data");
                        ArrayList<FoodModel> foodList= new ArrayList<>();
                        for(int j=0;j<childArray.length();j++)
                        {
                            JSONObject childJson = childArray.getJSONObject(j);
                            String productId =childJson.getString("id");
                            String itemName=childJson.getString("item_name");
                            String itemImage = childJson.getString("item_image");
                            String price = childJson.getString("price");
                            String priceType=childJson.getString("price_type");
                            String discount=childJson.getString("discount");
                            String vegNonVeg=childJson.getString("veg_non_veg");
                            String category=childJson.getString("category");

                            foodList.add(new FoodModel(productId,itemName,itemImage,price,priceType,discount,vegNonVeg,category));

                        }


                        categoryList.add(new CategoryModel(categoryName,foodList));

                    }


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
        MySingleton.getInstance(ShopProductDetail.this).addToRequestQueue(stringRequest);


    }

    private void setDataToView()
    {
        categoryRecycler = findViewById(R.id.category_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShopProductDetail.this);
        ProductParentAdapter productParentAdapter = new ProductParentAdapter(categoryList,ShopProductDetail.this,partnerId);
        categoryRecycler.setAdapter(productParentAdapter);
        categoryRecycler.setLayoutManager(linearLayoutManager);
        productParentAdapter.notifyDataSetChanged();

        shopNameV.setText(shopName);
        shopAddressV.setText(address);
        specialityV.setText(speciality);
        Picasso.get().load(BASE_IMAGE+shopImage).into(shopImageV);
        Log.e(TAG,".........."+BASE+shopImage);

    }




}