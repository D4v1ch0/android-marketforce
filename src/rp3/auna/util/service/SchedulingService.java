package rp3.auna.util.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import rp3.auna.AlarmAlertActivity;
import rp3.auna.R;
import rp3.auna.util.broadcast.AlarmReceiver;

/**
 * Created by Jesus Villa on 26/12/2017.
 */

public class SchedulingService extends IntentService {
    private static final String TAG = SchedulingService.class.getSimpleName();
    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    public SchedulingService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG,"onHandleIntent...");
        sendNotification("Alerta", intent);
        AlarmReceiver.completeWakefulIntent(intent);
    }

    // Post a notification indicating whether a doodle was found.
    private void sendNotification(String msg, Intent intent) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        final Intent sendIntent = new Intent(this, AlarmAlertActivity.class);

        final Bundle bundle = new Bundle();
        bundle.putAll(intent.getExtras());
        bundle.putBoolean("fromNotification", true);

        sendIntent.putExtras(bundle);

//        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                sendIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("RP3 Market Force")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mBuilder.setAutoCancel(true);
//        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(sendIntent);
    }
}
