package rp3.auna.util.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Locale;
import java.util.Random;

import rp3.app.NotificationActivity;
import rp3.auna.Main2Activity;
import rp3.auna.R;
import rp3.auna.StartActivity;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.utils.Utils;

import static rp3.auna.Contants.GENERAL_VALUE_CODE_LLAMADA_PENDIENTE;

/**
 * Created by Jesus Villa on 21/10/2017.
 */

public class AgendaService extends Service {
    private static final String TAG = AgendaService.class.getSimpleName();
    private boolean isRunning;
    private Context context;
    private TextToSpeech speech;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind...");

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate...");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG,"onStart...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand...");
        final NotificationManager mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(this.getApplicationContext(), StartActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        Bundle todo = intent1.getExtras();
        String tipo = null;
        String tiempo = null;
        String mensaje = null;
        int id = 0;
        LlamadaVta llamadaVta=null;
        if(todo.getString("tipo").equalsIgnoreCase("Llamada")){
            tipo = "llamada";
            id = todo.getInt("id");
            if(todo.getString("temp").equalsIgnoreCase("bd")){
                llamadaVta = LlamadaVta.getLlamadaIdBD(Utils.getDataBase(getApplicationContext()),id);
                Log.d(TAG,"Es con ID de BD...");
            }else{
                Log.d(TAG,"Es con ID del servidor...");
                llamadaVta = LlamadaVta.getLlamadaId(Utils.getDataBase(getApplicationContext()),id);
            }
        }else{
            tipo = "visita";
        }

        if(llamadaVta.getLlamadoValue()== GENERAL_VALUE_CODE_LLAMADA_PENDIENTE){
            Log.d(TAG,"La llamada esta pendiente...");
        }else{
            Log.d(TAG,"llamadaVta.getLlamadoValue()...");
        }
        mensaje = "Tiene una "+tipo+" pendiente en "+tiempo+" minutos";
        /*Notification mNotify  = new Notification.Builder(this)
                .setContentTitle("Alerta de Agenda!")
                .setContentText(mensaje)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();*/

        final String finalMensaje = mensaje;
        speech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    try {
                        speech.setLanguage(new Locale("es", "ES"));
                        speech.speak(finalMensaje, TextToSpeech.QUEUE_FLUSH, null);
                    } catch (Exception ex)
                    {}
                }
            }
        });

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(rp3.core.R.drawable.ic_launcher)
                        .setContentTitle("Alerta de Agenda!")
                        .setContentText(mensaje);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, NotificationActivity.class);
        resultIntent.putExtra("title", "Alerta de Agenda!");
        resultIntent.putExtra("message", mensaje);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(id, mBuilder.build());
        Log.e(TAG, "In the service");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
        this.isRunning = false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG,"onConfigurationChanged...");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG,"onLowMemory...");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d(TAG,"onTrimMemory...");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind...");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG,"onRebind...");
    }
}
