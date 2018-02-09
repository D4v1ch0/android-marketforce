package rp3.auna.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import rp3.auna.util.service.AgendaService;

/**
 * Created by Jesus Villa on 21/10/2017.
 */

public class AgendaReceiver extends BroadcastReceiver {
    private static final String TAG = AgendaReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive...");
        Log.d(TAG,"AgendaReceiver...");
        Intent serviceIntent = new Intent(context,AgendaService.class);
        serviceIntent.putExtras(intent.getExtras());
        context.startService(serviceIntent);
    }
}
