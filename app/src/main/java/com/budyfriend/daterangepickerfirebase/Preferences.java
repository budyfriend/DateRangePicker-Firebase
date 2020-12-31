package com.budyfriend.daterangepickerfirebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

public class Preferences {
    private static String data_active = "active";
    private static String data_username= "username";
    private static String data_role = "role";

    private static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public  static void setUsername(Context context, String username){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(data_username,username);
        editor.apply();
    }

    public static String getUsername(Context context){
        return getSharedPreferences(context).getString(data_username,"");
    }

    public  static void setRole(Context context, String role){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(data_role,role);
        editor.apply();
    }

    public static String getRole(Context context){
        return getSharedPreferences(context).getString(data_role,"");
    }

    public static void setActive(Context context, boolean active){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(data_active,active);
        editor.apply();
    }

    public static boolean isActive(Context context){
        return  getSharedPreferences(context).getBoolean(data_active,false);
    }

    public static void clearData(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }



}
