package com.abishek.comida.home.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.abishek.comida.MainActivity;
import com.abishek.comida.R;
import com.abishek.comida.aboutUs.AboutUs;
import com.abishek.comida.address.AddNewAddress;
import com.abishek.comida.address.AddressHomePage;
import com.abishek.comida.commonFiles.LoginSessionManager;
import com.abishek.comida.loginAndSignUp.Login;
import com.abishek.comida.myOrder.MyOrders;
import com.abishek.comida.myOrder.track.TrackOrder;
import com.abishek.comida.notification.NotificationHomePage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link More#newInstance} factory method to
 * create an instance of this fragment.
 */
public class More extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout myOrder, notification, aboutUs, feedback, logOut;
    private LoginSessionManager loginSessionManager;

    public More() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment More.
     */
    // TODO: Rename and change types and number of parameters
    public static More newInstance(String param1, String param2) {
        More fragment = new More();
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
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        loginSessionManager = new LoginSessionManager(getContext());

        myOrder = view.findViewById(R.id.my_orders);
        notification = view.findViewById(R.id.notification);
        aboutUs = view.findViewById(R.id.about_us);
        feedback = view.findViewById(R.id.send_feedback);
        logOut = view.findViewById(R.id.log_out);

        myOrder.setOnClickListener(this);
        notification.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        feedback.setOnClickListener(this);
        logOut.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_orders:
                if (!loginSessionManager.isLoggedIn()) {
                    startActivity(new Intent(getContext(), Login.class));
                    return;
                }
                startActivity(new Intent(getContext(), MyOrders.class));
                break;
            case R.id.notification:
                if (!loginSessionManager.isLoggedIn()) {
                    startActivity(new Intent(getContext(), Login.class));
                    return;
                }
                startActivity(new Intent(getContext(), NotificationHomePage.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(getContext(), AboutUs.class));
                break;
            case R.id.send_feedback:


                break;
            case R.id.log_out:
               openLogOutConfirmationDialog();
                break;
        }
    }

    void openLogOutConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to logout ?");
        builder.setTitle("Confirm");

        builder.setPositiveButton("Yes", new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new LoginSessionManager(getContext()).logoutUser();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}