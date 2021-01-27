package com.abishek.comida.commonFiles;

import android.content.Context;
import android.net.ConnectivityManager;

public class CommonVariablesAndFunctions {

    private static final String TAG = "CommanVarAndFun";

    public static final int RETRY_SECONDS = 10000;
    public static final int NO_OF_RETRY = 2;

    public static final String BASE = "http://androasu.in/comida/api/customer/";
    public static final String BASE_IMAGE = "http://androasu.in/comida/";
    public static final String BASE_ALL_PRODUCTS = BASE+"product/all";
    public static final String BASE_PRODUCT_CATEGORY = BASE+"product/category/all/";
    public static final String BASE_RESTAURANT_ALL = BASE+"restaurent/all";
    public static final String BASE_RESTAURANT_INFO = BASE+"partner/info/";
    public static final String BASE_LOGIN_OTP = BASE+"login/send/otp";
    public static final String BASE_LOGIN = BASE+"login/with/otp";
    public static final String BASE_SIGN_UP = BASE+"create/new";
    public static final String BASE_PROFILE_SHOW = BASE+"show/profile/";
    public static final String BASE_PROFILE_EDIT = BASE+"edit/profile";
    public static final String BASE_ADDRESS_SHOW = BASE+"show/address/";
    public static final String BASE_ADDRESS_ADD = BASE+"create/address";



    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
