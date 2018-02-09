package rp3.auna.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import rp3.auna.Contants;
import rp3.auna.bean.LlamadaData;
import rp3.auna.util.session.SessionManager;

/**
 * Created by Jesus Villa on 17/10/2017.
 */

public class DuracionLlamadaReceiver extends BroadcastReceiver {

    private static final String TAG = DuracionLlamadaReceiver.class.getSimpleName();
    public static boolean flag = false;
    public static long start_time = 0;
    public static long end_time= 0;
    //Also declare the interface in your BroadcastReceiver as static
    private static IDuracionMessageReceiver iDuracionMessageReceiver;
    public interface IDuracionMessageReceiver {
        void sendDuracionMessage(int duracionMessage);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive...");
        String action = intent.getAction();
        if(GpsLocationReceiver.isAppRunning(context,context.getPackageName().toString())){
            Log.d(TAG,"App is running...");
            if (action.equalsIgnoreCase("android.intent.action.PHONE_STATE")) {
                LlamadaData data = new LlamadaData();
                if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                        TelephonyManager.EXTRA_STATE_RINGING)) {
                    start_time = System.currentTimeMillis();
                    Log.d(TAG,"startTime...."+start_time);
                }
                if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                        TelephonyManager.EXTRA_STATE_IDLE)) {
                    end_time = System.currentTimeMillis();
                    Date date = new Date();
                    date.setTime(start_time);
                    Date date1 = new Date();
                    date1.setTime(end_time);
                    //Total time talked =
                    long total_time = end_time - start_time;
                    date.setTime(total_time);
                    Log.d(TAG,"total_time long..."+total_time);
                    long diffInMs = date1.getTime() - date.getTime();
                    Log.d(TAG,"Diferencia Minutes:"+diffInMs);
                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
                    Log.d(TAG,"Diferencia Seconds:"+diffInSec);
                    long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(total_time);
                    Log.d(TAG,"timeSeconds:"+timeSeconds);
                    //Store total_time somewhere or pass it to an Activity using intent
                    //int i = Integer.parseInt(String.valueOf(total_time));
                    Log.d(TAG,"Total time int:"+timeSeconds);
                    String finalTime = String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(total_time),
                            TimeUnit.MILLISECONDS.toSeconds(total_time) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total_time))
                    );
                    Log.d(TAG,"FinalTime:"+finalTime);
                    data.setDuration((int) timeSeconds);
                    //this.iDuracionMessageReceiver.sendDuracionMessage((int) timeSeconds);
                    SessionManager.getInstance(context).createDuracionSession(data);
                }
            }
        }else{
            Log.d(TAG,"App is not running...");
        }

    }

    public static void registerCallback(IDuracionMessageReceiver iDuracionMessageReceiver) {
        DuracionLlamadaReceiver.iDuracionMessageReceiver = iDuracionMessageReceiver;
    }


    @Override
    public IBinder peekService(Context myContext, Intent service) {
        Log.d(TAG,"peekService...");
        return super.peekService(myContext, service);
    }
}
