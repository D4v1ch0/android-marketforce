package rp3.marketforce;

import java.util.Calendar;
import java.util.Random;

import rp3.configuration.Configuration;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleCallback;
import rp3.data.MessageCollection;
import rp3.marketforce.content.EnviarUbicacionReceiver;
import rp3.marketforce.db.DbOpenHelper;
import rp3.marketforce.sync.SyncAdapter;
import rp3.sync.SyncAudit;
import rp3.util.ConnectionUtils;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends rp3.app.StartActivity{
	
	public StartActivity() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		if(PreferenceManager.getBoolean(Contants.KEY_FIRST_TIME, true))
		{
			startActivity(new Intent(this, ServerActivity.class));
			finish();
		}
		else
			Configuration.reinitializeConfiguration(context, DbOpenHelper.class);
		Configuration.TryInitializeConfiguration(this, DbOpenHelper.class);	
		
	}
	
	private void setServiceRecurring(){
		Intent i = new Intent(this, EnviarUbicacionReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		
		// Set the alarm to start at 8:30 a.m.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		long time = PreferenceManager.getLong(Contants.KEY_ALARMA_INICIO);
		cal.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
		
		String prueba = cal.getTime().toString();
		String prueba2 = calendar.getTime().toString();
				
		Random r = new Random();
		int i1 = r.nextInt(5);
		
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		//am.cancel(pi); // cancel any existing alarms
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
			calendar.getTimeInMillis() + (i1 * 1000 * 5),
			1000 * 60 * PreferenceManager.getInt(Contants.KEY_ALARMA_INTERVALO), pi);

	}

	@Override
	public void onContinue() {	
		
		super.onContinue();		
		Long days = SyncAudit.getDaysOfLastSync(SyncAdapter.SYNC_TYPE_GENERAL, SyncAdapter.SYNC_EVENT_SUCCESS);
		
		if(days == null || days > 0){
			Bundle bundle = new Bundle();
			bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GENERAL);
			requestSync(bundle);
		}else
			callNextActivity();
	}
	
	public void onSyncComplete(Bundle data, final MessageCollection messages) {
		if(!data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && !ConnectionUtils.isNetAvailable(this))
		{
			callNextActivity();
		}
		else if(data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_GENERAL) ||
				data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_SOLO_RESUMEN)){
			if(messages.hasErrorMessage())
				showDialogMessage(messages, new SimpleCallback() {				
					@Override
					public void onExecute(Object... params) {
						if(!messages.hasErrorMessage())
							callNextActivity();
						else
							finish();
					}
				});
			else
				callNextActivity();
		}
	}
	
//	@Override
//	public void onVerifyRequestSignIn() {
//		callNextActivity();
//	}
	
	private void callNextActivity(){
		setServiceRecurring();		
		startActivity(MainActivity.newIntent(this));
		finish();
		setServiceRecurring();
	}

	
}
