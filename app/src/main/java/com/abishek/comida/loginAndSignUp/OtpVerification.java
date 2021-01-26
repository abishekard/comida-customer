package com.abishek.comida.loginAndSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abishek.comida.R;
import com.abishek.comida.commonFiles.LoginSessionManager;
import com.abishek.comida.commonFiles.MySingleton;
import com.abishek.comida.home.HomePage;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_LOGIN;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_LOGIN_OTP;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.NO_OF_RETRY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.RETRY_SECONDS;

public class OtpVerification extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "OtpVerification";

    private EditText edtOtp1;
    private EditText edtOtp2;
    private EditText edtOtp3;
    private EditText edtOtp4;

    private Button btnVerify;
    private String email;

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

        email = getIntent().getStringExtra("email");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.verify: getDataFromUi();
                //startActivity(new Intent(OtpVerification.this, HomePage.class));
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

    public void getDataFromUi()
    {
        String otp = edtOtp1.getText().toString()+edtOtp2.getText().toString()+
                edtOtp3.getText().toString()+edtOtp4.getText().toString();
        Log.e(TAG,"....otp: "+otp);
        if(otp.length()<4)
        {
            Toast.makeText(OtpVerification.this,"please enter valid otp",Toast.LENGTH_SHORT).show();
            return;
        }

        verifyOtpAndLogin(email,otp);
    }



    public void verifyOtpAndLogin(String email,String otp)
    {


        Log.e(TAG, "getOtpForLogin : called");

        final String URL = BASE_LOGIN;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "........."+response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String accessToken = jsonObject.getString("access_token");
                    String tokeType = jsonObject.getString("token_type");
                    JSONObject data = jsonObject.getJSONObject("data");
                    String name = data.getString("name");
                    String userId =data.getString("id");
                    String email = data.getString("email");
                    String mobile = data.getString("mobile");

                    LoginSessionManager loginSessionManager = new LoginSessionManager(OtpVerification.this);
                    loginSessionManager.createLoginSession(tokeType,accessToken,userId,name,mobile,email);
                    Toast.makeText(OtpVerification.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    finish();






                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("password",otp);
                Log.e(TAG,"....email: "+email);

                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(OtpVerification.this).addToRequestQueue(stringRequest);


    }
}