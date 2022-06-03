package com.ocean.whatsappclone.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ocean.whatsappclone.LoginActivity;

public class SessionManager {

    SharedPreferences sharedprefernce;
    SharedPreferences.Editor editor;

    Context context;

    private static final String PREF_NAME = "sharedcheckLogin";
    private static final String User_Id = "userid";
    private static final String IS_LOGIN = "islogin";

    //todo: constructor of session manager class
    public SessionManager(Context context) {

        this.context = context;
        sharedprefernce = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        editor = sharedprefernce.edit();

    }

    //todo: logged in boolean function for false
    public Boolean isNotLogin() {
        return sharedprefernce.getBoolean(IS_LOGIN, false);

    }

    //todo: logged in boolean function for true
    public void setLogin() {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    //todo: logout user function
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
