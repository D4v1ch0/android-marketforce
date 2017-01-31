package rp3.auna.content;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import rp3.db.sqlite.DataBase;

import java.util.Calendar;
import java.util.Locale;

import rp3.auna.R;
import rp3.auna.marcaciones.MarcacionActivity;
import rp3.auna.marcaciones.PermisoActivity;
import rp3.auna.models.marcacion.Marcacion;
import rp3.auna.utils.Utils;
import rp3.runtime.Session;
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
        try {
            Session.Start(getBaseContext());
            rp3.configuration.Configuration.TryInitializeConfiguration(getBaseContext());
        }
        catch (Exception ex)
        {
            Utils.ErrorToFile(ex);
            ex.printStackTrace();
        }

        String message = data.getString("Message");
        String title = data.getString("Title");
        String footer = data.getString("Footer","");
        String type = data.getString("Type", "");

        Utils.ErrorToFile("Context is null - " + from + " - " + message + " - " + title + " - " + Calendar.getInstance().getTime().toString());

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
                if(getApplicationContext() == null)
                    Utils.ErrorToFile("Context is null - " + Calendar.getInstance().getTime().toString());
                NotificationPusher.pushNotification(1, getApplicationContext(), message, title, footer);
            }
            else
            {
                if(type.equalsIgnoreCase(NOTIFICATION_TYPE_MARCACION)) {
                    try {
                        DataBase db = DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class);
                        Marcacion ultimaMarcacion = Marcacion.getUltimaMarcacion(db);
                        if (ultimaMarcacion == null || rp3.auna.models.marcacion.Marcacion.getMarcacionesPendientes(db).size() == 0) {
                            toSpeech = title;
                            if(getApplicationContext() == null)
                                Utils.ErrorToFile("Context is null - " + Calendar.getInstance().getTime().toString());
                            NotificationPusher.pushNotification(1, getApplicationContext(), message, title, MarcacionActivity.class);
                        }

                        rp3.auna.sync.Marcaciones.executeSync(db);
                    }
                    catch(Exception ex)
                    {
                        Utils.ErrorToFile(ex);
                        ex.printStackTrace();
                    }

                }
                else if(type.equalsIgnoreCase(NOTIFICATION_TYPE_APROBAR_JUSTIFICACION)) {
                    toSpeech = title;
                    if(getApplicationContext() == null)
                        Utils.ErrorToFile("Context is null - " + Calendar.getInstance().getTime().toString());
                    NotificationPusher.pushNotification(1, getApplicationContext(), message, title, PermisoActivity.class);
                }
                else {
                    int posPuntos = footer.indexOf(":");
                    int posGuion = footer.indexOf("-");
                    toSpeech = "Mensaje de " + footer.substring(posPuntos, posGuion);
                    if(getApplicationContext() == null)
                        Utils.ErrorToFile("Context is null - " + Calendar.getInstance().getTime().toString());
                    NotificationPusher.pushNotification(1, getApplicationContext(), message, title, footer);
                }
            }
        }
        else
        {
            if(type.equalsIgnoreCase("LOG"))
            {
                rp3.auna.sync.Agente.executeSyncLog();
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
