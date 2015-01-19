package rp3.marketforce;

import rp3.app.BaseActivity;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleCallback;
import rp3.data.MessageCollection;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ServerActivity extends BaseActivity {
	
	public static String SERVER_CODE = "serverCode";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.layout_choose_server);
	}
	
	public void IrDemo(View v)
	{
		PreferenceManager.setValue(Contants.KEY_FIRST_TIME, false);
		startActivity(new Intent(this, StartActivity.class));
		finish();
	}
	
	public void EscojerServer(View v)
	{
		findViewById(R.id.server_code_button).setVisibility(View.GONE);
		findViewById(R.id.server_code_layout).setVisibility(View.VISIBLE);
	}
	
	public void CancelCode(View v)
	{
		findViewById(R.id.server_code_button).setVisibility(View.VISIBLE);
		findViewById(R.id.server_code_layout).setVisibility(View.GONE);
	}
	
	public void SendCode(View v)
	{
		showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);
		Bundle bundle = new Bundle();
		bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SERVER_CODE);
		bundle.putString(SERVER_CODE, getText(R.id.server_code).toString());
		requestSync(bundle);
	}
	
	public void onSyncComplete(Bundle data, final MessageCollection messages) {
		closeDialogProgress();
		if(data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_SERVER_CODE)){
			if(messages.hasErrorMessage())
				showDialogMessage(messages, new SimpleCallback() {				
					@Override
					public void onExecute(Object... params) {
						if(!messages.hasErrorMessage())
							IrDemo(getRootView());
					}
				});
		}
	}

}
