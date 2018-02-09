package rp3.auna.util.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jesus Villa on 26/12/2017.
 */

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = BootReceiver.class.getSimpleName();
    AlarmReceiver alarm = new AlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive...");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {Log.d(TAG,"onReceive...intent action is equals...");

            // TODO: 04.12.2016
        }
    }
}
