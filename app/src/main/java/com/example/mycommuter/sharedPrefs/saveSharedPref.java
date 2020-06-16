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


    public static void setLoggedIn(Context context, Pair<Boolean, String> loggedIn,String Email) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString("token", loggedIn.second);
        editor.putString("email",Email);

        editor.putBoolean(LOGGED_IN_PREF, loggedIn.first);
        editor.apply();
    }

    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }
    public static String getToken(Context context) {
        return getPreferences(context).getString("token", null);
    }
    public static String getEmail(Context context) {
        return getPreferences(context).getString("email", null);
    }
}