package com.abishek.comida.commonFiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;


import com.abishek.comida.loginAndSignUp.Login;

import java.util.HashMap;


public class LoginSessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LoginPreference";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String ON_BOARDING_SHOWN = "onBoardingShown";

    public static final String TOKEN_TYPE = "token_type";
    public static final String ACCESS_TOKEN = "access_token";

    public static final String USER_ID = "user_id";
    public static final String NAME = "name";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String OTP_VERIFIED = "0";
    public static final String PAN_NUM = "pan_number";
    public static final String IFSC_CODE = "ifsc_code";
    public static final String STORE_NAME = "store_name";
    public static final String DEALER_TYPE = "dealer_type";
    public static final String ACCOUNT_NUMBER = "account_number";
    public static final String GST_NUMBER = "gst_number";
    public static final String ACCOUNT_HOLDER_NAME = "account_holder_name";
    public static final String STORE_IMAGE = "store_name";
    public static final String PROFILE_IMAGE = "profile_image";
    public static final String PAN_IMAGE = "pan_image";
    public static final String CREATED_AT = "created_at";
    public static final String REFERRAL = "referral";
    public static final String CANCELLED_CHEQUE = "cancelled_cheque";
    public static final String CANCELLED_CHEQUE_NUMBER = "cancelled_cheque";
    public static final String OTHER = "others";


    public static final String FCM_TOKEN = "fcm_token";
    public static final String IS_FCM_REGISTERED = "is_fcm_registered";

    public static final String DEFAULT_VEHICLE_ID = "default_vehcile_id";

    public LoginSessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
/*
        if (onBoardingShown()) {
            int supportedVersion = new VersionChecker(context).getSupportedVersion();
            if (VERSION_CODE < supportedVersion) {
                new VersionChecker(context).openVersionNotSupportedDialog();
            }
        }

        if (!isFcmRegistered())
            context.startService(new Intent(context, SendFcmToMekvahanServer.class));*/

    }


    public void createLoginSession(String token_type, String accessToken, String userId, String name, String mobile,
                                   String email, String otpVerified, String panNumber, String ifscCode, String storeName,
                                   String dealerType, String accountNumber, String gstNumber, String accountHolderName,
                                   String profileImage,String cancellerChequeNumber) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(TOKEN_TYPE, token_type);
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.putString(USER_ID, userId);
        editor.putString(NAME, name);
        editor.putString(MOBILE, mobile);
        editor.putString(EMAIL, email);
        editor.putString(OTP_VERIFIED, otpVerified);
        editor.putString(PAN_NUM, panNumber);
        editor.putString(IFSC_CODE, ifscCode);
        editor.putString(STORE_NAME, storeName);
        editor.putString(DEALER_TYPE, dealerType);
        editor.putString(ACCOUNT_NUMBER, accountNumber);
        editor.putString(GST_NUMBER, gstNumber);
        editor.putString(ACCOUNT_HOLDER_NAME, accountHolderName);
        editor.putString(PROFILE_IMAGE, profileImage);
        editor.putString(CANCELLED_CHEQUE_NUMBER,cancellerChequeNumber);
        editor.commit();
    }

    public void addCustomerDetails(String name, String email, String referralCode,String storeName,String dealerType) {
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(STORE_NAME,storeName);
        editor.putString(REFERRAL, referralCode);
        editor.putString(DEALER_TYPE,dealerType);
        editor.commit();
    }
    public void addProfileImage(String imageUrl) {
        editor.putString(PROFILE_IMAGE, imageUrl);

        editor.commit();
    }

    public void addAccountDetails(String panNumber, String accountNumber, String accountHolderName,
                                  String ifsc,String cancelledCheque,String gstNumber) {
        editor.putString(PAN_NUM, panNumber);
        editor.putString(ACCOUNT_NUMBER, accountNumber);
        editor.putString(ACCOUNT_HOLDER_NAME,accountHolderName);
        editor.putString(IFSC_CODE, ifsc);
        editor.putString(CANCELLED_CHEQUE,cancelledCheque);
        editor.putString(GST_NUMBER,gstNumber);
        editor.commit();
    }

    public void checkLogin() {

        if (!this.isLoggedIn()) {

            Intent i = new Intent(context, Login.class);

            Log.e("LogginSessionManager", "login :" + isLoggedIn());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }


    public boolean onBoardingShown() {
        return pref.getBoolean(ON_BOARDING_SHOWN, false);
    }

    public HashMap<String, String> getUserDetailsFromSP() {

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(TOKEN_TYPE, pref.getString(TOKEN_TYPE, ""));
        user.put(ACCESS_TOKEN, pref.getString(ACCESS_TOKEN, ""));
        user.put(USER_ID, pref.getString(USER_ID, null));
        user.put(NAME, pref.getString(NAME, "You are Awesome"));
        user.put(MOBILE, pref.getString(MOBILE, null));
        user.put(EMAIL, pref.getString(EMAIL, "someoneawesom@gmail.com"));
        user.put(REFERRAL, pref.getString(REFERRAL, ""));
        user.put(PROFILE_IMAGE, pref.getString(PROFILE_IMAGE, ""));
        user.put(OTHER, pref.getString(OTHER, ""));
        user.put(PAN_NUM, pref.getString(PAN_NUM, ""));
        user.put(ACCOUNT_NUMBER, pref.getString(ACCOUNT_NUMBER, ""));
        user.put(ACCOUNT_HOLDER_NAME, pref.getString(ACCOUNT_HOLDER_NAME, ""));
        user.put(IFSC_CODE, pref.getString(IFSC_CODE, ""));
        user.put(GST_NUMBER, pref.getString(GST_NUMBER, ""));
        user.put(CANCELLED_CHEQUE_NUMBER, pref.getString(CANCELLED_CHEQUE_NUMBER, ""));



        return user;
    }

    public void logoutUser() {

       /* - new Thread(() -> {
            try {
                FirebaseInstanceId.getInstance().deleteToken(getFcmToken(), "");
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();*/
        //-  context.stopService(new Intent(context, PhoneService.class));
        editor.clear();
        editor.commit();


        Intent i = new Intent(context, Login.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //-   new ClearRoom(MekVahanDatabase.getInstance(context)).execute();

        // Staring Login Activity
        Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show();
        context.startActivity(i);

    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


  /*  public int getDefaultVehicleId() {
        return pref.getInt(DEFAULT_VEHICLE_ID, -1);
    }

    public void setDefaultVehicleId(int vehicleId) {
        editor.putInt(DEFAULT_VEHICLE_ID, vehicleId);
        editor.commit();
    }*/

    public void setFCMToken(String s) {
        editor.putString(FCM_TOKEN, s);
        editor.commit();
    }

    public String getFcmToken() {
        return pref.getString(FCM_TOKEN, null);
    }

   /*- public void prepareFCMToken(MyListener listener) {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Log.e("prepareFCMToken", "onSuccess: " + instanceIdResult.getToken());
                setFCMToken(instanceIdResult.getToken());
                listener.updatePage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("prepareFCMToken", "onFailure: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }*/

    public boolean isFcmRegistered() {
        return pref.getBoolean(IS_FCM_REGISTERED, false);
    }

    public void setIsFcmRegistered(boolean value) {
        editor.putBoolean(IS_FCM_REGISTERED, value);
        editor.commit();
    }

   /*- class ClearRoom extends AsyncTask<Void,Void,Void> {

        private final MyVehicleDao myVehiclesDao;

        public ClearRoom (MekVahanDatabase instance) {
            myVehiclesDao = instance.getMyVehicleDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
           myVehiclesDao.deleteAll();
            return null;
        }

    }*/
}
