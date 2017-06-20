package rp3.marketforce.content;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.Locale;

import rp3.marketforce.R;
import rp3.util.NotificationPusher;

/**
 * Created by magno_000 on 15/06/2015.
 */
public class GCMReceiver extends GcmListenerService {
    private static final String TAG = GCMReceiver.class.getSimpleName();
    TextToSpeech t1;
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG,"onNMessageReceived...");
        String message = data.getString("Message");
        String title = data.getString("Title");
        String footer = data.getString("Footer","");
        String type = data.getString("Type", "");

        if(TextUtils.isEmpty(title))
        {
            title = getApplicationContext().getString(R.string.app_name);
        }
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        String toSpeech = "";

        if(!TextUtils.isEmpty(message)) {
            int posPuntos = footer.indexOf(":");
            int posGuion = footer.indexOf("-");
            toSpeech = "Mensaje de " + footer.substring(posPuntos, posGuion);
            NotificationPusher.pushNotification(1, getApplicationContext(), message, title, footer);
        }
        else
        {
            if(type.equalsIgnoreCase("LOG"))
            {
                rp3.marketforce.sync.Agente.executeSyncLog();
            }
        }

        final String toSpeechFinal = toSpeech;
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    try {
                        t1.setLanguage(new Locale("es", "ES"));
                        t1.speak( toSpeechFinal, TextToSpeech.QUEUE_FLUSH, null);
                    } catch (Exception ex)
                    {}
                }
            }
        });
    }


}
