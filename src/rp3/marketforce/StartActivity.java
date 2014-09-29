package rp3.marketforce;

import rp3.configuration.Configuration;
import rp3.content.SimpleCallback;
import rp3.data.MessageCollection;
import rp3.marketforce.db.DbOpenHelper;
import rp3.marketforce.sync.SyncAdapter;
import rp3.sync.SyncAudit;
import android.os.Bundle;

public class StartActivity extends rp3.app.StartActivity{
	
	public StartActivity() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Configuration.TryInitializeConfiguration(this, DbOpenHelper.class);	
	}
	
	private void setServiceRecurring(){
//		Intent i = new Intent(this, EnviarUbicacionReceiver.class);
//		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
//		
//		// Set the alarm to start at 8:30 a.m.
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(System.currentTimeMillis());
//		calendar.set(Calendar.HOUR_OF_DAY, 8);
//		calendar.set(Calendar.MINUTE, 30);
//				
//		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//		//am.cancel(pi); // cancel any existing alarms
//		am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//			calendar.getTimeInMillis(),
//		    AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);		
		
		//AlarmManager.INTERVAL_FIFTEEN_MINUTES
	}

	@Override
	public void onContinue() {	
		
		super.onContinue();				
		
		Long days = SyncAudit.getDaysOfLastSync(SyncAdapter.SYNC_TYPE_GENERAL, SyncAdapter.SYNC_EVENT_SUCCESS);
		
		if(days == null || days > 0){
			Bundle bundle = new Bundle();
			bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GENERAL);
			requestSync(bundle);
			
			
			
		}else{
			callNextActivity();
		}
	}
	
	public void onSyncComplete(Bundle data, final MessageCollection messages) {
		if(messages.getCuount()>0)
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
	
	private void callNextActivity(){
		setServiceRecurring();
		
		startActivity(MainActivity.newIntent(this));
		finish();
	}

	
}
