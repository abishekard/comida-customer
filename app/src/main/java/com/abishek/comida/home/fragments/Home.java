package com.abishek.comida.home.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abishek.comida.R;
import com.abishek.comida.cart.CartHome;
import com.abishek.comida.commonFiles.MySingleton;
import com.abishek.comida.home.adapters.AllRestaurantAdapter;
import com.abishek.comida.home.product.FoodModel;
import com.abishek.comida.home.adapters.AllProductAdapter;
import com.abishek.comida.home.product.RestaurantModel;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_ALL_PRODUCTS;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_RESTAURANT_ALL;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.NO_OF_RETRY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.RETRY_SECONDS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG ="HomeFragment" ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView cartIcon;
    private static int LOCATION_REQUEST_COUNT = 0;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float MARKER_ZOOM = 16f;
    private Boolean mLocationPermissionsGranted = false;

    private TextView locationView,detailedLocationView;

    private List<RestaurantModel> restaurantList;

    private RecyclerView restaurantRecycler;



    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        restaurantList = new ArrayList<>();
        cartIcon = view.findViewById(R.id.cart);

        cartIcon.setOnClickListener(this);
        locationView = view.findViewById(R.id.location);
        detailedLocationView = view.findViewById(R.id.detailed_location);

        if(hasLocationPermission())
            getDeviceLocation();
        else
            requestLocationPermission();
        if(!isLocationEnabled())
            Toast.makeText(getContext(),"Plese Enable GPS ",Toast.LENGTH_SHORT).show();

        fetchProductList(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.cart: startActivity(new Intent(getContext(), CartHome.class));
                break;
        }
    }

    //get device location
    private void getDeviceLocation() {
        Log.d("MainActivity", "get location your device");
        try {
            FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            try {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                //  moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 17f);


                                Log.e(TAG,currentLocation.toString());
                                setAddress(currentLocation.getLatitude(),currentLocation.getLongitude());
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean hasLocationPermission() {
        return (ActivityCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(getContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }


    private void requestLocationPermission() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
    }

    private boolean isLocationEnabled() {
        return LocationManagerCompat.isLocationEnabled(getContext().getSystemService(LocationManager.class));
    }



    private void setAddress(double lat,double lng) {
        Log.e(TAG, "called : setAddress");



        Geocoder geocoder;
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(lat,lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e(TAG,"address found="+addresses);


        if(addresses==null ) {
            detailedLocationView.setText("Location not found");
            return;
        }

        else if(addresses.size()==0){
            detailedLocationView.setText("location not found");
            return;
        }

        String address = addresses.get(0).getAddressLine(0);

        int countComma = 0;
        int indexOf2ndComma = -1;

        int i=0;

        for (i=0;i<address.length();i++)
        {
            if(address.charAt(i) == ',')
                countComma++;
            if (countComma == 3) {
                break;
            }
        }
        indexOf2ndComma = i;

        address = address.substring(0,indexOf2ndComma);
        Log.e(TAG,"........"+address);
        detailedLocationView.setText(address);
        String[] temp = address.split(",");
        locationView.setText(temp[0]);

        /* et_city.setText(addresses.get(0).getLocality());*/


    }


    public void fetchProductList(View view)
    {


        Log.e(TAG, "fetchAllProductList : called");

        final String URL = BASE_RESTAURANT_ALL;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray subJson = jsonObject.getJSONArray("data");
                    for(int i=0;i<subJson.length();i++)
                    {
                        JSONObject childJson = subJson.getJSONObject(i);
                        String shopId =childJson.getString("id");
                        String shopName=childJson.getString("shop_name");
                        String speciality = childJson.getString("speciality");
                        String shopImage = childJson.getString("shop_image");
                        String address=childJson.getString("address");
                        String lat=childJson.getString("lat");
                        String lng=childJson.getString("long");
                        String openTime=childJson.getString("open_time");
                        String closeTime=childJson.getString("close_time");
                        String available=childJson.getString("available");
                        String rating=childJson.getString("rating");


                        restaurantList.add(new RestaurantModel(shopId,shopName,shopImage,speciality,
                                address,lat,lng,closeTime,openTime,available,rating));
                    }


                    setDataToView(view);

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
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }

    private void setDataToView(View v)
    {
        restaurantRecycler = v.findViewById(R.id.all_restaurant_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        AllRestaurantAdapter allRestaurantAdapter = new AllRestaurantAdapter(restaurantList,getContext());
        restaurantRecycler.setAdapter(allRestaurantAdapter);
        restaurantRecycler.setLayoutManager(linearLayoutManager);
        allRestaurantAdapter.notifyDataSetChanged();

    }

}