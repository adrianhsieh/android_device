package com.client.safelyblue.safelyblueclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by albin on 3/12/15.
 */
public class PositionSyncTriggerService extends Service {
    private static final String TAG = "PosSyncTriggService";

    ScheduledPositionSync mPeriodicTaskReceiver = new ScheduledPositionSync();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Start Command Invoked");
        SafelyBlueApplication myApplication = (SafelyBlueApplication) getApplicationContext();

        mPeriodicTaskReceiver.restartPeriodicTaskHeartBeat(PositionSyncTriggerService.this, myApplication);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "Not sure when this should be triggered.");
        return null;
    }
}
