package rp3.auna.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jesus Villa on 20/12/2017.
 */

public class LlamadaReceiver extends BroadcastReceiver {
    private static final String TAG = LlamadaReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive...");
    }
}
