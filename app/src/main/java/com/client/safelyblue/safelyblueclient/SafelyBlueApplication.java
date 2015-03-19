package com.client.safelyblue.safelyblueclient;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SafelyBlueApplication extends Application {
    private static SafelyBlueApplication sInstance;

    private RequestQueue mRequestQueue;
    private static String imei;

    @Override
    public void onCreate() {
        super.onCreate();

        mRequestQueue = Volley.newRequestQueue(this);
        sInstance = this;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        imei = telephonyManager.getDeviceId();
    }

    public String getIMEI(){
        return imei;
    }

    public synchronized static SafelyBlueApplication getInstance() {
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}
