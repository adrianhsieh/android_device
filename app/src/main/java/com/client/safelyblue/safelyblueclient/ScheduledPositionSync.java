package com.client.safelyblue.safelyblueclient;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.client.safelyblue.safelyblueclient.db.PositionReportDB;
import com.client.safelyblue.safelyblueclient.json.PostJsonArrayRequest;

import org.json.JSONArray;

/**
 * Created by albin on 3/12/15.
 */
public class ScheduledPositionSync extends BroadcastReceiver {

    private static final String TAG = "ScheduledPositionSync";
    private static final String INTENT_ACTION = "com.waldo.app.PERIODIC_TASK_HEART_BEAT";
    private static final String SERVER_URL = "http://sb-sandbox.cloudhub.io/api/devices/";
    private static final int GPS_PERIOD = 60000;

    private PositionReportDB pDB;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction() != "") {
            SafelyBlueApplication myApplication = (SafelyBlueApplication) context.getApplicationContext();
            Log.i(TAG,"Intent action == "+intent.getAction());
            if (intent.getAction().equals(INTENT_ACTION)) {
                triggerDataSync(context, myApplication);
            }
        }

    }
    private void triggerDataSync(Context context, SafelyBlueApplication myApplication) {
        Log.i(TAG, "Put the code to trigger REST Call");
        pDB = new PositionReportDB(context);
        JSONArray jsonArray = pDB.getAllPositionReports();
        Log.i(TAG, "Position Reports: "+jsonArray);
        if(jsonArray.length() > 0) {

            PostJsonArrayRequest req = new PostJsonArrayRequest(SERVER_URL+myApplication.getIMEI()+"/positionreports", jsonArray,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.i(TAG, response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    }
            );
            SafelyBlueApplication.getInstance().getRequestQueue().add(req);
        }

    }

    public void restartPeriodicTaskHeartBeat(Context context, SafelyBlueApplication myApplication) {
        Log.i(TAG,"Restart periodic check");

        Intent alarmIntent = new Intent(context, ScheduledPositionSync.class);
        boolean isAlarmUp = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null;
        if (!isAlarmUp) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmIntent.setAction(INTENT_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), GPS_PERIOD, pendingIntent);
        }
    }
    public void stopPeriodicTaskHeartBeat(Context context) {
        Log.i(TAG,"Stop Periodic service.");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, ScheduledPositionSync.class);
        alarmIntent.setAction(INTENT_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.cancel(pendingIntent);
    }

}
