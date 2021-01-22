package com.abishek.comida.myOrder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.abishek.comida.R;
import com.abishek.comida.myOrder.track.TrackOrder;

public class OrderDetails extends AppCompatActivity {

    private Button btnTrackOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        btnTrackOrder = findViewById(R.id.track_order);
        btnTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderDetails.this, TrackOrder.class));
            }
        });
    }
}