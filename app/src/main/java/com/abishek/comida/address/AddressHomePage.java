package com.abishek.comida.address;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abishek.comida.R;
import com.abishek.comida.commonFiles.GPSTracker;
import com.abishek.comida.commonFiles.LoginSessionManager;
import com.abishek.comida.commonFiles.MySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_ADDRESS_SHOW;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.NO_OF_RETRY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.RETRY_SECONDS;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.isNetworkAvailable;
import static com.abishek.comida.commonFiles.LoginSessionManager.ACCESS_TOKEN;
import static com.abishek.comida.commonFiles.LoginSessionManager.TOKEN_TYPE;

public class AddressHomePage extends AppCompatActivity {

    private static final String TAG = "AddressHomePage";
    private Button btnAddAddress;
    private String userId;
    private List<AddressModel> addressList;
    private LinearLayout noAddressLayout;
    private RecyclerView addressRecyclerView;
    private ArrayList<String> addressTypeList;
    private TextView titleView;
    private int from=0;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_home_page);

        from = getIntent().getIntExtra("from",0);
        Log.e(TAG,".....from: "+from);

        if(!isNetworkAvailable(AddressHomePage.this))
        {
            Toast.makeText(AddressHomePage.this,"check your Internet connection",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnAddAddress = findViewById(R.id.btn_add_address);
        noAddressLayout = findViewById(R.id.no_address_layout);
        addressRecyclerView = findViewById(R.id.address_recycler);
        progressBar = findViewById(R.id.progress_bar);
        titleView = findViewById(R.id.title);
        btnAddAddress.setEnabled(false);
        userId = new LoginSessionManager(AddressHomePage.this).getUserDetailsFromSP().get("user_id");
        addressList = new ArrayList<>();


        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AddressHomePage.this, AddNewAddress.class).putExtra("addressTypes",addressTypeList));
            }
        });

        if(from==2)
        {
            titleView.setText("Select an Address");
        }
    }


    public void fetchAddress() {


        Log.e(TAG, "fetchAllProductList : called");

        final String URL = BASE_ADDRESS_SHOW + userId;

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);


                try {
                    addressList = new ArrayList<>();
                    addressTypeList = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");

                    if(status != 200)
                    {
                        Toast.makeText(AddressHomePage.this,"Some thing went wrong",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    JSONArray data = jsonObject.getJSONArray("data");
                    if(data.length()==0)
                    {
                       // Toast.makeText(AddressHomePage.this,"no address exists \n please add an address",Toast.LENGTH_SHORT).show();
                        noAddressLayout.setVisibility(View.VISIBLE);
                        addressRecyclerView.setVisibility(View.GONE);
                        btnAddAddress.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        return;
                    }



                    for (int i = 0 ;i<data.length();i++)
                    {
                        JSONObject childJson = data.getJSONObject(i);
                        String id=childJson.getString("id");
                        String address  =childJson.getString("address");
                        String state  =childJson.getString("state");
                        String city  =childJson.getString("city");
                        String pinCode  =childJson.getString("pincode");
                        String landmark  =childJson.getString("landmark");
                        String locality  =childJson.getString("locality");
                        String lat  =childJson.getString("latitude");
                        String lng  =childJson.getString("longitude");
                        String addressType = childJson.getString("address_type");

                        addressList.add(new AddressModel(id,address,state,city,pinCode,landmark,locality,lat,lng,addressType));
                        addressTypeList.add(addressType);

                    }

                    setDataToView();
                    btnAddAddress.setEnabled(true);


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

                String tokenType = new LoginSessionManager(AddressHomePage.this).getUserDetailsFromSP().get(TOKEN_TYPE);
                String accessToken = new LoginSessionManager(AddressHomePage.this).getUserDetailsFromSP().get(ACCESS_TOKEN);

                header.put("Accept", "application/json");
                header.put("Authorization", tokenType + " " + accessToken);


                return super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                return super.getParams();
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(AddressHomePage.this).addToRequestQueue(stringRequest);


    }

    @Override
    protected void onResume() {
        super.onResume();

        userId = new LoginSessionManager(AddressHomePage.this).getUserDetailsFromSP().get("user_id");
        noAddressLayout.setVisibility(View.GONE);
        addressRecyclerView.setVisibility(View.VISIBLE);
        fetchAddress();

    }

    public void setDataToView()
    {
        AddressAdapter addressAdapter = new AddressAdapter(addressList,AddressHomePage.this,from);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddressHomePage.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        addressRecyclerView.setLayoutManager(linearLayoutManager);
        addressRecyclerView.setAdapter(addressAdapter);
        addressAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }
}