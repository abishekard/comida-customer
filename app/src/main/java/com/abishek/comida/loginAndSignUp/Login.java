package com.abishek.comida.loginAndSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abishek.comida.R;
import com.abishek.comida.address.AddNewAddress;
import com.abishek.comida.commonFiles.MySingleton;
import com.abishek.comida.home.HomePage;
import com.abishek.comida.home.product.RestaurantModel;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_LOGIN_OTP;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_RESTAURANT_ALL;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.NO_OF_RETRY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.RETRY_SECONDS;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private final String  TAG = "Login.class";

    private TextView btnRegister;
    private Button btnLogin;
    private EditText edtEmail;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRegister = findViewById(R.id.register);
        btnLogin = findViewById(R.id.login);
        edtEmail = findViewById(R.id.edt_email);
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Wait...");
        progressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.register:startActivity(new Intent(Login.this,SignUp.class));
            finish();
                break;
            case R.id.login: getDataFromUi();
                //startActivity(new Intent(Login.this,OtpVerification.class));
                break;
        }
    }

    public void getDataFromUi()
    {
        String email = edtEmail.getText().toString();
        if(email.equals(""))
        {
            Toast.makeText(Login.this,"please enter an email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!email.contains("@gmail.com"))
        {
            Toast.makeText(Login.this,"please enter a valid email",Toast.LENGTH_SHORT).show();
            return;
        }

        getOtpForLogin(email);
    }

    public void getOtpForLogin(String email)
    {


        progressDialog.show();
        btnLogin.setEnabled(false);
        Log.e(TAG, "getOtpForLogin : called");

        final String URL = BASE_LOGIN_OTP;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "........."+response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");
                    if(status==200)
                    {
                        startActivity(new Intent(Login.this,OtpVerification.class).putExtra("email",email));
                        Toast.makeText(Login.this,"Otp sent to your email",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                        return;
                    }
                    if(status ==202)
                    {
                        startActivity(new Intent(Login.this,SignUp.class));
                        Toast.makeText(Login.this,"email does not exist.\nPlease create a new Account.",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        btnLogin.setEnabled(true);
                        finish();
                        return;
                    }

                    Toast.makeText(Login.this,"something went wrong.\nTry again later",Toast.LENGTH_SHORT).show();



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                Toast.makeText(Login.this,"server problem",Toast.LENGTH_SHORT).show();
                btnLogin.setEnabled(true);
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("email",email);

                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(Login.this).addToRequestQueue(stringRequest);


    }


}