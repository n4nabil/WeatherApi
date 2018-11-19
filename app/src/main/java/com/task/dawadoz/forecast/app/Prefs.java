package com.task.dawadoz.forecast.app;

import android.content.Context;
import android.content.SharedPreferences;


public class Prefs {

    private static final String PRE_LOAD = "preLoad";
    private static final String PREFS_NAME = "prefs";



    private static String LOG_STATUS = "logStatus";
    private static String USER_NAME = "username";
    private static String PASSWORD = "password";

    public static String LOG_STATUS_IN = "login";
    public static String LOG_STATUS_OUT = "logout";


    private static String AUTH_UID = "uid";
    private static String AUTH_TOKEN_TYPE = "token-type";
    private static String AUTH_EXPIRY = "expiry";
    private static String AUTH_CLIENT = "client";
    private static String AUTH_ACCESS_TOKEN = "access-token";
    private static String AUTH_CONTENT_TYPE = "Content-Type";






    private static Prefs instance;
    private final SharedPreferences sharedPreferences;


    public Prefs(Context context) {

        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static Prefs with(Context context) {

        if (instance == null) {
            instance = new Prefs(context);
        }
        return instance;
    }

    public void setPreLoad(boolean totalTime) {

        sharedPreferences
                .edit()
                .putBoolean(PRE_LOAD, totalTime)
                .apply();
    }

    public boolean getPreLoad(){
        return sharedPreferences.getBoolean(PRE_LOAD, false);
    }



    public void setLogStatus(String logStatus) {

        sharedPreferences
                .edit()
                .putString(LOG_STATUS, logStatus)
                .apply();
    }

    public String getLogStatus(){
        return sharedPreferences.getString(LOG_STATUS, LOG_STATUS_OUT);
    }

    public void setUserName(String userName) {

        sharedPreferences
                .edit()
                .putString(USER_NAME, userName)
                .apply();
    }

    public String getUserName(){
        return sharedPreferences.getString(USER_NAME, "");
    }

    public void setPassword(String password) {

        sharedPreferences
                .edit()
                .putString(PASSWORD, password)
                .apply();
    }

    public String getPassword(){
        return sharedPreferences.getString(PASSWORD, "");
    }












    public void setAuthUid(String authUid) {

        sharedPreferences
                .edit()
                .putString(AUTH_UID, authUid)
                .apply();
    }

    public String getAuthUid() {
        return sharedPreferences.getString(AUTH_UID, "");
    }

    public void setAuthClient(String authClient) {

        sharedPreferences
                .edit()
                .putString(AUTH_CLIENT, authClient)
                .apply();
    }

    public String getAuthClient() {
        return sharedPreferences.getString(AUTH_CLIENT, "");
    }
    public void setAuthContentType(String authContentType) {

        sharedPreferences
                .edit()
                .putString(AUTH_CONTENT_TYPE, authContentType)
                .apply();
    }

    public String getAuthContentType() {
        return sharedPreferences.getString(AUTH_CONTENT_TYPE, "");
    }

    public void setAuthTokenType(String authTokenType) {

        sharedPreferences
                .edit()
                .putString(AUTH_TOKEN_TYPE, authTokenType)
                .apply();
    }

    public String getAuthTokenType() {
        return sharedPreferences.getString(AUTH_TOKEN_TYPE, "");
    }


    public void setAuthAccessToken(String authAccessToken) {

        sharedPreferences
                .edit()
                .putString(AUTH_ACCESS_TOKEN, authAccessToken)
                .apply();
    }

    public String getAuthAccessToken() {
        return sharedPreferences.getString(AUTH_ACCESS_TOKEN, "");
    }

    public void setAuthExpiry(String authExpiry) {

        sharedPreferences
                .edit()
                .putString(AUTH_EXPIRY, authExpiry)
                .apply();
    }

    public String getAuthExpiry() {
        return sharedPreferences.getString(AUTH_EXPIRY, "");
    }



}
