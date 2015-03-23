package com.client.safelyblue.safelyblueclient;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.client.safelyblue.safelyblueclient.db.PositionReportDB;

import java.lang.Override;
import java.lang.String;

public class MainActivity extends Activity implements LocationListener {

    private static final String TAG = "MainActivity";

    //private LocationListener locListener = new MyLocationListener();
    private static String imei;



    private PositionReportDB pDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Intent startPositionSyncServiceIntent = new Intent(MainActivity.this, PositionSyncTriggerService.class);
        startService(startPositionSyncServiceIntent);

        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 1, this);
        Log.i(TAG,"Is GPS Enabled? "+locManager.isProviderEnabled(LocationManager.GPS_PROVIDER));


    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG,"Location Changed"+location);


        if(location != null) {
            ContentValues locationMap = new ContentValues();
            locationMap.put("longitude", location.getLongitude());
            locationMap.put("latitude", location.getLatitude());
            locationMap.put("altitiude", location.getAltitude());
            locationMap.put("accuracy", location.getAltitude());
            locationMap.put("datetimestamp", location.getTime());


            pDB = new PositionReportDB(MainActivity.this);
            boolean dbInsertResult = pDB.insertPosition(locationMap);

        }else{
            Log.e(TAG, "No Location Created");
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG, "Status Changed");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "Provider Enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e(TAG, "Provider Disabled");
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "On Resume Called");

    }

    @Override
    protected void onPause() {
        Log.e(TAG, "On Pause Called");

    }
*/
/*
    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {



        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "Provider Disabled");

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "Provider Enabled");

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "Provider Enabled");

        }
    }
    */
}
