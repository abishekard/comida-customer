package com.abishek.comida.loginAndSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.abishek.comida.R;

import org.w3c.dom.Text;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnLogin = findViewById(R.id.login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login: onBackPressed();
                break;
        }
    }
}