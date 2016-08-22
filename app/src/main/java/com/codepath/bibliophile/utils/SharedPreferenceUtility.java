package com.codepath.bibliophile.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtility {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String KEY_ADDRESS_LINE_1 = "key_addressLine1";
    public static final String KEY_ADDRESS_LINE_2 = "key_addressLine2";
    public static final String KEY_CITY = "key_city";
    public static final String KEY_STATE = "key_state";
    public static final String KEY_ZIPCODE = "key_zipcode";

    public static String getAddressLine1(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        return sharedpreferences.getString(KEY_ADDRESS_LINE_1,null);
    }

    public static String getAddressLine2(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        return sharedpreferences.getString(KEY_ADDRESS_LINE_2,null);    }

    public static String getCity(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        return sharedpreferences.getString(KEY_CITY,null);
    }

    public static String getState(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        return sharedpreferences.getString(KEY_STATE,null);
    }

    public static String getZipcode(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        return sharedpreferences.getString(KEY_ZIPCODE,null);
    }



    public static void setAddress(Context context, String addressLine1,String addressLine2,String city, String state, String zipcode ){
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(KEY_ADDRESS_LINE_1,addressLine1);
        editor.putString(KEY_ADDRESS_LINE_2,addressLine2);
        editor.putString(KEY_CITY,city);
        editor.putString(KEY_STATE,state);
        editor.putString(KEY_ZIPCODE,zipcode);
        editor.apply();
    }
}

