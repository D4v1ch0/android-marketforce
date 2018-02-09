package rp3.auna.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jesus Villa on 21/10/2017.
 */

public class AppReceiver extends BroadcastReceiver {
    private static final String TAG = AppReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive...");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.d(TAG,"ScreenEventReceiver ON");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.d(TAG,"ScreenEventReceiverON");
        }
    }
}
