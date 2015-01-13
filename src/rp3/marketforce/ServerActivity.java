package rp3.marketforce;

import rp3.app.BaseActivity;
import rp3.configuration.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ServerActivity extends BaseActivity {

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
		
	}

}
