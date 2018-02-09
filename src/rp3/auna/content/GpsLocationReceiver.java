package rp3.auna.content;

import android.app.ActivityManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import rp3.auna.events.EventBus;
import rp3.auna.events.Events;

/**
 * Created by Jesus Villa on 20/10/2017.
 */

public class GpsLocationReceiver extends BroadcastReceiver{

    private static final String TAG = GpsLocationReceiver.class.getSimpleName();
    public OnGpsReceivedListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive...");
        //this.listener = (OnGpsReceivedListener) context;
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            Log.d(TAG,"providers changed...");

            final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
            Bundle msj = new Bundle();

            /*try{
                if(isAppRunning(context,context.getPackageName().toString())){
                    Log.d(TAG,"App is running...");
                }else{
                    Log.d(TAG,"App not is running...");
                }
            }catch (Exception e){
                e.printStackTrace();
            }*/

            /*if(shouldShowNotification(context)){
                Log.d(TAG,"App is show running...");
                Intent i = new Intent();
                i.setClassName("rp3.auna", "rp3.auna.StartActivity");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }else{
                Log.d(TAG,"App not show is running...");
            }*/
            if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                //do something
                Log.d(TAG,"Prendido");
                msj.putString("GpsReceiver","Prendido");
                EventBus.getBus().post(new Events.Message(msj));
            }
            else
            {
                Log.d(TAG,"Apagado");
                msj.putString("GpsReceiver","Apagado");
                EventBus.getBus().post(new Events.Message(msj));
                //do something else
            }
            //Intent pushIntent = new Intent(context, LocalService.class);
            //context.startService(pushIntent);
        }
    }

    public interface OnGpsReceivedListener {
        public void onGpsReceived(String status);
    }

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if(procInfos!=null){
            Log.d(TAG,"procIinfos!=null...");
            Log.d(TAG,"procInfosSize:"+procInfos.size());
        }else{
            Log.d(TAG,"procInfos==null...");
        }

        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean shouldShowNotification(Context context) {
        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        if (myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
            return true;

        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        // app is in foreground, but if screen is locked show notification anyway
        return km.inKeyguardRestrictedInputMode();
    }
}
