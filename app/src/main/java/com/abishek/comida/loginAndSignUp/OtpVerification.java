package com.abishek.comida.loginAndSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.abishek.comida.R;
import com.abishek.comida.home.HomePage;

public class OtpVerification extends AppCompatActivity implements View.OnClickListener {

    private EditText edtOtp1;
    private EditText edtOtp2;
    private EditText edtOtp3;
    private EditText edtOtp4;

    private Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        edtOtp1 = findViewById(R.id.otp_1);
        edtOtp2 = findViewById(R.id.otp_2);
        edtOtp3 = findViewById(R.id.otp_3);
        edtOtp4 = findViewById(R.id.otp_4);
        edtOtp1.addTextChangedListener(new GenericTextWatcher(edtOtp1));
        edtOtp2.addTextChangedListener(new GenericTextWatcher(edtOtp2));
        edtOtp3.addTextChangedListener(new GenericTextWatcher(edtOtp3));
        edtOtp4.addTextChangedListener(new GenericTextWatcher(edtOtp4));

        btnVerify = findViewById(R.id.verify);
        btnVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.verify:startActivity(new Intent(OtpVerification.this, HomePage.class));
                break;
        }
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.otp_1:
                    if (text.length() == 1)
                        edtOtp2.requestFocus();
                    break;
                case R.id.otp_2:
                    if (text.length() == 1)
                        edtOtp3.requestFocus();
                    else if (text.length() == 0)
                        edtOtp1.requestFocus();
                    break;
                case R.id.otp_3:
                    if (text.length() == 1)
                        edtOtp4.requestFocus();
                    else if (text.length() == 0)
                        edtOtp2.requestFocus();
                    break;
                case R.id.otp_4:
                    if (text.length() == 0)
                        edtOtp3.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
}