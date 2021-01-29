package com.abishek.comida.cart;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.abishek.comida.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderConfirmedDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderConfirmedDialog extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnTrackOrder;
    private TextView btnBackToHome;

    private ConfirmationDialogListener confirmationDialogListener;
    private int backDone;

    public OrderConfirmedDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderConfirmedDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderConfirmedDialog newInstance(String param1, String param2) {
        OrderConfirmedDialog fragment = new OrderConfirmedDialog();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_confirmed_dialog, container, false);

        backDone=0;

        btnTrackOrder = view.findViewById(R.id.track_order);
        btnBackToHome = view.findViewById(R.id.back_to_home);

        btnTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDone=1;
                confirmationDialogListener.trackOrderClicked();


            }
        });
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDone=1;
                confirmationDialogListener.backToHomeClicked();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        confirmationDialogListener = (ConfirmationDialogListener)context;
    }



    public interface ConfirmationDialogListener{
        void backToHomeClicked();
        void trackOrderClicked();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(backDone==0)
        confirmationDialogListener.backToHomeClicked();
    }
}