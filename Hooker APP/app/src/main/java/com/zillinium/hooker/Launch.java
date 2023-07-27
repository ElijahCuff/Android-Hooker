package com.zillinium.hooker;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Launch extends Activity {
    public static Activity me;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        startActivity(new Intent(this, Preferences.class).setFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS));      
    }  
}
