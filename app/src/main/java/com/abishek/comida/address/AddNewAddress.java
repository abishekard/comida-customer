package com.abishek.comida.address;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abishek.comida.MainActivity;
import com.abishek.comida.R;
import com.abishek.comida.commonFiles.LoginSessionManager;
import com.abishek.comida.commonFiles.MySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_ADDRESS_ADD;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_ADDRESS_SHOW;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.NO_OF_RETRY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.RETRY_SECONDS;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.isNetworkAvailable;
import static com.abishek.comida.commonFiles.LoginSessionManager.ACCESS_TOKEN;
import static com.abishek.comida.commonFiles.LoginSessionManager.TOKEN_TYPE;

public class AddNewAddress extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, LocationListener, Animation.AnimationListener, View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    public static int map_to_addsignup = 0;
    SupportMapFragment mapFragment;
    CameraPosition cameraPosition;
    String longitude, latitude;
    double lat;
    double lan;
    String locality = null;
    String country = null;
    String state;
    String sub_admin;
    String city;
    String pincode;
    String locality_city;
    String sub_localoty;
    String country_code;
    FusedLocationProviderClient mFusedLocationProviderClient;

    // Animation
    Animation animFadein;
    private GoogleMap mMap;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private TextView resutText;
    private LatLng latLng = null;
    private boolean mRequestingLocationUpdates;
    private Button ic_save_proceed;
    private Context context;
    private TextView id_tv_change;
    private ImageView img_back, img_pin;
    private TextView locationView;
    //private RadarView radar_view;
  //  private ProgressBar pro_bar;
    private TextView homeAddressView,officeAddressView,otherAddressView,btnCancel;
    private LinearLayout addressType,otherType;

    private Button btnSave;
    private TextView landmarkView,localityView;
    private String addressName="home";
    private EditText otherTypeName;
    private ProgressBar progressBar;
    private ArrayList<String> addressTypeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_new_address);

        addressTypeList = (ArrayList<String>)getIntent().getSerializableExtra("addressTypes");
        Log.e(TAG,"....addressTypes: "+addressTypeList.toString());


        if(!isNetworkAvailable(AddNewAddress.this))
        {
            Toast.makeText(AddNewAddress.this,"check your Internet connection",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        try {
            findViewByID();
            startLocationButtonClick();
         //   pro_bar.setVisibility(View.VISIBLE);
            configureCameraIdle();
            ic_save_proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        //code

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void configureCameraIdle() {
        Log.e("apple","helo");
       // pro_bar.setVisibility(View.VISIBLE);
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCameraIdle() {
                try {
               //     pro_bar.setVisibility(View.VISIBLE);
                    LatLng latLng = mMap.getCameraPosition().target;
                    Geocoder geocoder = new Geocoder(AddNewAddress.this);
                    resutText.setText("Loading...");
                    progressBar.setVisibility(View.VISIBLE);
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                        if (addressList != null && addressList.size() > 0) {
                            Log.e(TAG,addressList.toString());
                            locality = addressList.get(0).getAddressLine(0);
                            country = addressList.get(0).getCountryName();
                            state = addressList.get(0).getAdminArea();
                            city = addressList.get(0).getSubAdminArea();
                           // city = addressList.get(0).getFeatureName();
                            pincode = addressList.get(0).getPostalCode();
                            locality_city = addressList.get(0).getLocality();
                            sub_localoty = addressList.get(0).getSubLocality();
                            country_code = addressList.get(0).getCountryCode();
                            latitude = addressList.get(0).getLatitude()+"";
                            longitude = addressList.get(0).getLongitude()+"";
                            if (locality != null && country != null) {
                                resutText.setText(locality + "");
                                String[] temp = locality.toString().split(",");
                                locationView.setText(temp[0]);
                          //      pro_bar.setVisibility(View.GONE);
                            } else {
                                resutText.setText("Location could not be fetched...");
                             //   pro_bar.setVisibility(View.GONE);
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        try {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            mRequestingLocationUpdates = true;
                            configureCameraIdle();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            if (response.isPermanentlyDenied()) {
                                // open device settings when the permission is
                                // denied permanently
                                openSettings();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        }


                    }).check();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findViewByID() {
        try {
            context = AddNewAddress.this;
            locationView = findViewById(R.id.location);
            img_back = findViewById(R.id.btn_back);
            resutText = findViewById(R.id.detailed_location);
          //  ic_save_proceed = findViewById(R.id.btn_save_address);
            img_pin = findViewById(R.id.pin);
            progressBar = findViewById(R.id.progress_bar);

            addressType=findViewById(R.id.address_type);
            otherType = findViewById(R.id.other_type);
            btnCancel = findViewById(R.id.cancel);
            homeAddressView = findViewById(R.id.home_address);
            officeAddressView = findViewById(R.id.office_address);
            otherAddressView = findViewById(R.id.other_address);
            otherTypeName = findViewById(R.id.other_type_name);
            landmarkView = findViewById(R.id.landmark);
            localityView = findViewById(R.id.locality);

            btnSave = findViewById(R.id.btn_save_address);

            addressType.setOnClickListener(this);
            otherType.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
            homeAddressView.setOnClickListener(this);
            officeAddressView.setOnClickListener(this);
            otherAddressView.setOnClickListener(this);
            btnSave.setOnClickListener(this);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openSettings() {
        try {
            Intent intent = new Intent();
            intent.setAction(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",
                    BuildConfig.APPLICATION_ID, null);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get device location
    private void getDeviceLocation() {
        Log.d("MainActivity", "get location your device");
        try {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 17f);
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

    private void moveCamera(LatLng latLng, float zoom) {
        try {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            getDeviceLocation();
            mMap.setOnCameraIdleListener(onCameraIdleListener);
            mMap.setTrafficEnabled(true);
            mMap.setIndoorEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            mMap.setBuildingsEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(false);
            /*mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.gmap));*/
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.resetMinMaxZoomPreference();
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            mMap.isIndoorEnabled();
            mMap.isBuildingsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.other_address:
                addressType.setVisibility(View.GONE);
                otherType.setVisibility(View.VISIBLE);
                setAddressTypeBackground((TextView) v);
                break;
            case R.id.home_address:setAddressTypeBackground((TextView) v);
                                  addressName="home";
                break;
            case R.id.office_address:setAddressTypeBackground((TextView) v);
                                   addressName = "office";
                break;
            case R.id.address_type:

                break;
            case R.id.other_type:
                break;
            case R.id.cancel:
                addressType.setVisibility(View.VISIBLE);
                otherType.setVisibility(View.GONE);
                homeAddressView.setBackgroundColor(ContextCompat.getColor(AddNewAddress.this,R.color.transparent));
                officeAddressView.setBackgroundColor(ContextCompat.getColor(AddNewAddress.this,R.color.transparent));
                otherAddressView.setBackgroundColor(ContextCompat.getColor(AddNewAddress.this,R.color.transparent));
                break;
            case R.id.btn_save_address: getDataFromUi();
                break;

        }
    }

    public void setAddressTypeBackground(TextView textView)
    {
        homeAddressView.setBackgroundColor(ContextCompat.getColor(AddNewAddress.this,R.color.transparent));
        officeAddressView.setBackgroundColor(ContextCompat.getColor(AddNewAddress.this,R.color.transparent));
        otherAddressView.setBackgroundColor(ContextCompat.getColor(AddNewAddress.this,R.color.transparent));

        textView.setBackground(ContextCompat.getDrawable(AddNewAddress.this,R.drawable.circle_grey));
    }



    public void getDataFromUi()
    {
        if(resutText.getText().toString().toLowerCase().equals("loading..."))
        {
            Toast.makeText(AddNewAddress.this,"Network problem. Try again later.",Toast.LENGTH_SHORT).show();
            return;
        }
       String landmark = landmarkView.getText().toString();
       String locality = localityView.getText().toString();
       if(otherType.getVisibility()==View.VISIBLE)
       {
           addressName=otherTypeName.getText().toString();
       }

       if(landmark.equals(""))
       {
           Toast.makeText(AddNewAddress.this,"please enter a landmark",Toast.LENGTH_SHORT).show();
           return;
       }
        if(locality.equals(""))
        {
            Toast.makeText(AddNewAddress.this,"please enter a locality",Toast.LENGTH_SHORT).show();
            return;
        }
        if(longitude.equals("")||latitude.equals(""))
        {
            Toast.makeText(AddNewAddress.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            return;
        }
        if(addressName.equals(""))
        {
            Toast.makeText(AddNewAddress.this,"please select address type",Toast.LENGTH_SHORT).show();
            return;
        }

        if(addressTypeList.contains(addressName))
        {
            Toast.makeText(AddNewAddress.this,"address type already exists",Toast.LENGTH_SHORT).show();
            return;
        }
        if(locality_city == null)
        {
            Toast.makeText(AddNewAddress.this,"unable to detect city.\nPlease try another address.",Toast.LENGTH_SHORT).show();
            return;
        }

        saveAddressToDatabase();
    }

    public void saveAddressToDatabase() {


        Log.e(TAG, "saveAddress : called");

        btnSave.setEnabled(false);
        final String URL = BASE_ADDRESS_ADD;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");

                    if(status != 200)
                    {
                        Toast.makeText(AddNewAddress.this,"Something went wrong (server)",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(AddNewAddress.this,"Address Added",Toast.LENGTH_SHORT).show();
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

                Toast.makeText(AddNewAddress.this,"server problem",Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();

                String tokenType = new LoginSessionManager(AddNewAddress.this).getUserDetailsFromSP().get(TOKEN_TYPE);
                String accessToken = new LoginSessionManager(AddNewAddress.this).getUserDetailsFromSP().get(ACCESS_TOKEN);

                header.put("Accept", "application/json");
                header.put("Authorization", tokenType + " " + accessToken);
                Log.e(TAG,"Authorization: "+ tokenType + " " + accessToken);


                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> params = new HashMap<>();
                params.put("address",locality);
                params.put("state",state);
                params.put("city",city);
                params.put("pincode",pincode);
                params.put("latitude",latitude);
                params.put("longitude",longitude);
                params.put("landmark",landmarkView.getText().toString());
                params.put("locality",localityView.getText().toString());
                params.put("address_type",addressName);
                params.put("local_city",locality_city);


                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(AddNewAddress.this).addToRequestQueue(stringRequest);


    }

}