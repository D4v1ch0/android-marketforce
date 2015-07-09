package rp3.marketforce.content;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Random;

import rp3.app.BaseActivity;
import rp3.configuration.PreferenceManager;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.models.Ubicacion;
import rp3.marketforce.sync.EnviarUbicacion;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.Utils;
import rp3.runtime.Session;
import rp3.util.ConnectionUtils;
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

public class EnviarUbicacionReceiver extends BroadcastReceiver    {

	@Override
	public void onReceive(final Context context, Intent intent) {			
		/*
		 * Antes de subir posicion actual, se chequea si se paso la hora establecida como limite
		 * para enviar al servicio web la posicion.
		 */
		try
		{
			Session.Start(context);
			rp3.configuration.Configuration.TryInitializeConfiguration(context);
			if(context == null)
				Utils.ErrorToFile("Context is null - " + Calendar.getInstance().getTime().toString());
			else
				Utils.ErrorToFile("Context is ok - " + Calendar.getInstance().getTime().toString());
			Calendar calendarCurrent = Calendar.getInstance();
			calendarCurrent.setTimeInMillis(System.currentTimeMillis());
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(PreferenceManager.getLong(Contants.KEY_ALARMA_FIN));
			calendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
			
			String prueba = cal.getTime().toString();
			String prueba2 = calendar.getTime().toString();
			
			if(calendarCurrent.getTimeInMillis() < calendar.getTimeInMillis())
			{
				LocationUtils.getLocation(context, new OnLocationResultListener() {
					
					@Override
					public void getLocationResult(Location location) {	
						try
						{
							if(location!=null){
                                if(Session.IsLogged()) {
                                    if (ConnectionUtils.isNetAvailable(context)) {
                                        Ubicacion ub = new Ubicacion();
                                        ub.setLatitud(location.getLatitude());
                                        ub.setLongitud(location.getLongitude());
                                        ub.setFecha(Calendar.getInstance().getTimeInMillis());
                                        ub.setPendiente(true);
                                        Ubicacion.insert(DataBase.newDataBase(rp3.marketforce.db.DbOpenHelper.class), ub);

                                        Bundle bundle = new Bundle();
                                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_BATCH);
                                        rp3.sync.SyncUtils.requestSync(bundle);
                                    } else {
                                        Ubicacion ub = new Ubicacion();
                                        ub.setLatitud(location.getLatitude());
                                        ub.setLongitud(location.getLongitude());
                                        ub.setFecha(Calendar.getInstance().getTimeInMillis());
                                        ub.setPendiente(true);
                                        Ubicacion.insert(DataBase.newDataBase(rp3.marketforce.db.DbOpenHelper.class), ub);
                                    }
                                }
							}	
						}
						catch(Exception e)
						{
							Utils.ErrorToFile(e);
						}
					}
				});
			}
			else
			{
				cancelAlarm(context);
			}
		}
		catch(Exception ex)
		{
			Utils.ErrorToFile(ex);
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
		Calendar cal = Calendar.getInstance();
		long time = PreferenceManager.getLong(Contants.KEY_ALARMA_INICIO);
		cal.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
				
		String prueba = cal.getTime().toString();
		String prueba2 = calendar.getTime().toString();
		
		Random r = new Random();
		int i1 = r.nextInt(5);
		
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		//am.cancel(pi); // cancel any existing alarms
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis() + (i1 * 1000 * 5),
				1000 * 60 * PreferenceManager.getInt(Contants.KEY_ALARMA_INTERVALO), pi);
	}
	
}
