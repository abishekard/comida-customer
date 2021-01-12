package com.abishek.comida.cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.abishek.comida.R;

public class CartHome extends AppCompatActivity implements View.OnClickListener {

    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_home);

        btnCheckout = findViewById(R.id.btn_checkout);

        btnCheckout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_checkout: startActivity(new Intent(CartHome.this,Checkout.class));
                break;
        }
    }
}