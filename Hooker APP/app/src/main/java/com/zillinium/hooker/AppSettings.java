package com.zillinium.hooker;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.res.Resources;

public class AppSettings {
    protected static SharedPreferences pref;

    protected AppSettings(SharedPreferences preferences) {
        pref = preferences;
    }

    public static AppSettings get(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return new AppSettings(preferences);
    }

    public static int getFontSize() {
        try {
            return Integer.parseInt(pref.getString("txtSize", "12"));
        }
        catch (Exception e) {
            return 2;
        }
    }
    
    public static String getHookURL() {
            return pref.getString("hook", "http://example.com");
    }

    public static String getName() {
        return pref.getString("client", "Tester");
    }
   

    public static boolean isFullscreen() {
        return pref.getBoolean("fullscreen", false);
    }
    public static boolean isThemed() {
        return pref.getBoolean("themed", false);
    }

}
