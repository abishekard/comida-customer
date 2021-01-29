package com.abishek.comida.myOrder.track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abishek.comida.R;
import com.abishek.comida.cart.Checkout;
import com.abishek.comida.home.HomePage;
import com.transferwise.sequencelayout.SequenceStep;

public class TrackOrder extends AppCompatActivity {

    private static final String TAG = "trackOrder";
    SequenceStep step1, step2, step3, step4;
    private String orderId;
    private int stage;
    private TextView orderIdView;
    private Button btnDone;
    private int from = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        from = getIntent().getIntExtra("from", 0);

        btnDone = findViewById(R.id.btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from == 1)
                    clearingStackAndMoveToHomePage();
                else
                    finish();
            }
        });

        orderId = getIntent().getStringExtra("order_id");
        stage = Integer.parseInt(getIntent().getStringExtra("stage"));

        Log.e(TAG, "orderId: " + orderId + " stage: " + stage);


        orderIdView = findViewById(R.id.order_id);
        orderIdView.setText("#" + orderId);
        step1 = (SequenceStep) findViewById(R.id.step1);
        step2 = (SequenceStep) findViewById(R.id.step2);
        step3 = (SequenceStep) findViewById(R.id.step3);
        step4 = (SequenceStep) findViewById(R.id.step4);

        step1.setTitle("Order Confirmed");
        step1.setSubtitle("Your oder has been confirmed");
        step1.setTitleTextAppearance(R.style.order_status_text);

        step2.setTitle("Order Processed");
        step2.setSubtitle("we are processing your order");
        step2.setTitleTextAppearance(R.style.order_status_text);


        step3.setTitle("Out for Delivery");
        step3.setSubtitle("Our delivery partner is out for delivery");
        step3.setTitleTextAppearance(R.style.order_status_text);

        step4.setTitle("Delivered");
        step4.setSubtitle("Order delivered successfully.");
        step4.setTitleTextAppearance(R.style.order_status_text);

        if (stage == 1) {
            step1.setActive(true);
            step1.setTitle("Order Confirmed");
            step1.setSubtitle("Your oder has been confirmed");
            step1.setTitleTextAppearance(R.style.current_status_text);
        } else if (stage == 2) {
            step2.setActive(true);
            step2.setTitle("Order Processed");
            step2.setSubtitle("we are processing your order");
            step2.setTitleTextAppearance(R.style.current_status_text);
        } else if (stage == 3) {
            step3.setActive(true);
            step3.setTitle("Out for Delivery");
            step3.setSubtitle("Our delivery partner is out for delivery");
            //programatically seting style to Title
            step3.setTitleTextAppearance(R.style.current_status_text);
        } else {

            step4.setActive(true);
            step4.setTitle("Delivered");
            step4.setSubtitle("Order delivered successfully.");
            step4.setTitleTextAppearance(R.style.current_status_text);
        }
    }

    public void clearingStackAndMoveToHomePage() {

        Log.e("1", "called : clearingStackAndMoveToHomePage");

        Intent i = new Intent(TrackOrder.this, HomePage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finishAffinity();
    }
}