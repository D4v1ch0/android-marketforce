package rp3.marketforce.content;

import java.util.Calendar;
import java.util.Random;

import rp3.marketforce.Contants;
import rp3.marketforce.sync.EnviarUbicacion;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.LocationUtils;
import rp3.util.LocationUtils.OnLocationResultListener;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

public class EnviarUbicacionReceiver extends WakefulBroadcastReceiver    {

	@Override
	public void onReceive(Context context, Intent intent) {			
		/*
		 * Antes de subir posicion actual, se chequea si se paso la hora establecida como limite
		 * para enviar al servicio web la posicion.
		 */
		String horaLimite = rp3.configuration.Configuration.getAppConfiguration().get(Contants.ALARM_CANCEL_TIME);
		String[] tiempoLimite = horaLimite.split("H");
		
		Calendar calendarCurrent = Calendar.getInstance();
		calendarCurrent.setTimeInMillis(System.currentTimeMillis());
		
		Calendar calendarLimit = Calendar.getInstance();
		calendarLimit.setTimeInMillis(System.currentTimeMillis());
		calendarLimit.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tiempoLimite[0]));
		calendarLimit.set(Calendar.MINUTE, Integer.parseInt(tiempoLimite[1]));
		
		if(calendarCurrent.getTimeInMillis() < calendarLimit.getTimeInMillis())
		{
			LocationUtils.getLocation(context, new OnLocationResultListener() {
				
				@Override
				public void getLocationResult(Location location) {				
					if(location!=null){			
						Bundle settingsBundle = new Bundle() ;
						settingsBundle.putDouble(EnviarUbicacion.ARG_LATITUD, location.getLatitude());
						settingsBundle.putDouble(EnviarUbicacion.ARG_LONGITUD, location.getLongitude());			
						settingsBundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ENVIAR_UBICACION);
						
						rp3.sync.SyncUtils.requestSync(settingsBundle);
					}	
				}
			});
		}
		else
		{
			cancelAlarm(context);
		}
	}
	
	public void cancelAlarm(Context context)
	{
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

	    Intent updateServiceIntent = new Intent(context, EnviarUbicacionReceiver.class);
	    PendingIntent pendingUpdateIntent = PendingIntent.getService(context, 0, updateServiceIntent, 0);

	    try {
	        alarmManager.cancel(pendingUpdateIntent);
	        restartAlarm(context);
	    } catch (Exception e) {
	        Log.e("AlarmEnviarUbicacion", "AlarmManager update was not canceled. " + e.toString());
	    }
	}
	
	public void restartAlarm(Context context)
	{
		Intent i = new Intent(context, EnviarUbicacionReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
		
		// Set the alarm to start at 8:30 a.m.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		calendar.set(Calendar.MINUTE, 30);
				
		Random r = new Random();
		int i1 = r.nextInt(5);
		
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		//am.cancel(pi); // cancel any existing alarms
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis() + (i1 * 1000 * 5),
				1000 * 60 * 10, pi);
	}
	
}
