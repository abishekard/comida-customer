package com.abishek.comida.myOrder.track;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.abishek.comida.R;
import com.transferwise.sequencelayout.SequenceStep;

public class TrackOrder extends AppCompatActivity {

    SequenceStep step1,step2,step3,step4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        step1=(SequenceStep)findViewById(R.id.step1);
        step2=(SequenceStep)findViewById(R.id.step2);
        step3=(SequenceStep)findViewById(R.id.step3);
        step4=(SequenceStep)findViewById(R.id.step4);

        step1.setTitle("Order Confirmed");
        step1.setSubtitle("Your oder has been confirmed");
        step1.setTitleTextAppearance(R.style.order_status_text);

        step2.setTitle("Order Processed");
        step2.setSubtitle("we are processing your order");
        step2.setTitleTextAppearance(R.style.order_status_text);

        //programatically activating
        step3.setActive(true);
        step3.setTitle("Out for Delivery");
        step3.setSubtitle("Our delivery partner is out for delivery");
        //programatically seting style to Title
        step3.setTitleTextAppearance(R.style.current_status_text);

        step4.setTitle("Delivered");
        step4.setSubtitle("Order delivered successfully.");
        step4.setTitleTextAppearance(R.style.order_status_text);
    }
}