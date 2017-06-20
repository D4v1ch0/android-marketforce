package rp3.marketforce;

import rp3.app.BaseActivity;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleCallback;
import rp3.data.MessageCollection;
import rp3.marketforce.sync.Server;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ServerActivity extends BaseActivity {
	private static final String TAG = ServerActivity.class.getSimpleName();
	
	public static String SERVER_CODE = "serverCode";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.layout_choose_server);
	}
	
	public void IrStart()
	{
		PreferenceManager.setValue(Contants.KEY_FIRST_TIME, false);
		PreferenceManager.setValue(Contants.KEY_SECOND_TIME, false);
		startActivity(new Intent(this, StartActivity.class));
		finish();
	}
	
	public void IrDemo(View v)
	{
		showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);
		new ServerOperation().execute(Contants.DEFAULT_APP);
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
		new ServerOperation().execute(getTextViewString(R.id.server_code).toString());
	}
	
	 private class ServerOperation extends AsyncTask<String, Void, String> {

	        @Override
	        protected String doInBackground(String... params) {
	            int code = Server.executeSync(params[0]);
	            if(code == SyncAdapter.SYNC_EVENT_SUCCESS)
	            	return "";
	            else
	            	return "Código Incorrecto.";
	        }

	        @Override
	        protected void onPostExecute(String result) {
	        	closeDialogProgress();
	        	if(result.equalsIgnoreCase(""))
	        		IrStart();
	        	else
	        		Toast.makeText(context, result, Toast.LENGTH_LONG).show();
	        }

	        @Override
	        protected void onPreExecute() {}

	        @Override
	        protected void onProgressUpdate(Void... values) {}
	    }

	/**
	 *
	 *Ciclo de vida
	 *
	 */

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG,"onStart...");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume...");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d(TAG,"onBackPressed...");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPause...");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG,"onStop...");
	}

}
