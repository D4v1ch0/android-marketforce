package rp3.auna.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jesus Villa on 21/10/2017.
 */

public class ShutDownReceiver extends BroadcastReceiver{
    private static final String TAG = ShutDownReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive...");
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_SHUTDOWN)) {
            Log.d(TAG,"shutdown is on...");
        } else {
            Log.d(TAG,"shutdown is off...");
        }
    }
}
