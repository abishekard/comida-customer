package com.abishek.comida.splashScreen;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.abishek.comida.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SCPage1 extends Fragment {


    public SCPage1() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scpage1, container, false);
    }

}
