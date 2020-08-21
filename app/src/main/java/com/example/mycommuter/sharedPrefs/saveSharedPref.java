package com.example.mycommuter.sharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

import static com.example.mycommuter.sharedPrefs.Preferencesutil.LOGGED_IN_PREF;

public class saveSharedPref {

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static void setLoggedIn(Context context, Pair<Boolean, String> loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString("token", loggedIn.second);

        editor.putBoolean(LOGGED_IN_PREF, loggedIn.first);
        editor.apply();
    }

    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }

    public static String getToken(Context context) {
        return getPreferences(context).getString("token", null);
    }

    public void storeDestination(Context context, String latitude, String longitude) {
        SharedPreferences.Editor mEditor = getPreferences(context).edit();
        mEditor.putString("Lat", latitude);
        mEditor.putString("Long", longitude);
        mEditor.apply();
    }

    public static String retrieveDestination(Context context, String tag) {
        return getPreferences(context).getString(tag, null);
    }

    public void storeMapStyle(Context context, String style) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString("MapStyle", style);
        editor.apply();
    }

    public static String setMapStyle(Context context) {
        return getPreferences(context).getString("MapStyle", null);
    }
}