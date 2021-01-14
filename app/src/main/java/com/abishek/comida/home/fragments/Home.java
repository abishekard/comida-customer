package com.abishek.comida.home.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abishek.comida.R;
import com.abishek.comida.cart.CartHome;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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


}