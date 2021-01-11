package com.abishek.comida.loginAndSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abishek.comida.R;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView btnRegister;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRegister = findViewById(R.id.register);
        btnLogin = findViewById(R.id.login);
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.register:startActivity(new Intent(Login.this,SignUp.class));
                break;
            case R.id.login:startActivity(new Intent(Login.this,OtpVerification.class));
                break;
        }
    }
}