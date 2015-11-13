package rp3.marketforce.content;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import rp3.db.sqlite.DataBase;
import java.util.Locale;

import rp3.marketforce.R;
import rp3.marketforce.marcaciones.MarcacionActivity;
import rp3.marketforce.marcaciones.PermisoActivity;
import rp3.marketforce.models.marcacion.Marcacion;
import rp3.util.NotificationPusher;

/**
 * Created by magno_000 on 15/06/2015.
 */
public class GCMReceiver extends GcmListenerService {
    TextToSpeech t1;

    public static final String NOTIFICATION_TYPE_MARCACION = "MARCACION";
    public static final String NOTIFICATION_TYPE_APROBAR_JUSTIFICACION = "APROBAR_JUSTIFICACION";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("Message");
        String title = data.getString("Title");
        String footer = data.getString("Footer","");
        String type = data.getString("Type");
        if(TextUtils.isEmpty(title))
        {
            title = getApplicationContext().getString(R.string.app_name);
        }
        Log.d("Marketforce", "From: " + from);
        Log.d("Marketforce", "Message: " + message);

        String toSpeech = "";
        
        if(!TextUtils.isEmpty(message)) {
            if(TextUtils.isEmpty(type)) {
                int posPuntos = footer.indexOf(":");
                int posGuion = footer.indexOf("-");
                toSpeech = "Mensaje de " + footer.substring(posPuntos, posGuion);
                NotificationPusher.pushNotification(1, getApplicationContext(), message, title, footer);
            }
            else
            {
                if(type.equalsIgnoreCase(NOTIFICATION_TYPE_MARCACION)) {
                    try {
                        DataBase db = DataBase.newDataBase(rp3.marketforce.db.DbOpenHelper.class);
                        Marcacion ultimaMarcacion = Marcacion.getUltimaMarcacion(db);
                        if (ultimaMarcacion == null || rp3.marketforce.models.marcacion.Marcacion.getMarcacionesPendientes(db).size() == 0) {
                            NotificationPusher.pushNotification(1, getApplicationContext(), message, title, MarcacionActivity.class);
                        }

                        rp3.marketforce.sync.Marcaciones.executeSync(db);
                    }
                    catch(Exception ex)
                    {}

                }
                else if(type.equalsIgnoreCase(NOTIFICATION_TYPE_APROBAR_JUSTIFICACION))
                    NotificationPusher.pushNotification(1, getApplicationContext(), message, title, PermisoActivity.class);
                else
                    NotificationPusher.pushNotification(1, getApplicationContext(), message, title, footer);
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
