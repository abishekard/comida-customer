package com.abishek.comida.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.abishek.comida.R;

public class Checkout extends AppCompatActivity implements View.OnClickListener {

    private Button btnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        btnConfirm = findViewById(R.id.btn_confirm_order);

        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_confirm_order: OrderConfirmedDialog orderConfirmedDialog = new OrderConfirmedDialog();
            orderConfirmedDialog.setStyle( DialogFragment.STYLE_NO_FRAME,R.style.dialog);
            orderConfirmedDialog.show(getSupportFragmentManager(),"order_confirm");
                break;

        }
    }
}