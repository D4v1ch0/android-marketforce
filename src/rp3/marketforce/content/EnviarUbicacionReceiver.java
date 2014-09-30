package rp3.marketforce.content;

import rp3.marketforce.sync.EnviarUbicacion;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.LocationUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

public class EnviarUbicacionReceiver extends BroadcastReceiver   {

	@Override
	public void onReceive(Context context, Intent intent) {						
		Location location = LocationUtils.getLocation(context);
		
		if(location!=null){			
			Bundle settingsBundle = new Bundle() ;
			settingsBundle.putDouble(EnviarUbicacion.ARG_LATITUD, location.getLatitude());
			settingsBundle.putDouble(EnviarUbicacion.ARG_LONGITUD, location.getLongitude());			
			settingsBundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ENVIAR_UBICACION);
			
			Toast.makeText(context, "msg msg", Toast.LENGTH_LONG).show();
			
			rp3.sync.SyncUtils.requestSync(settingsBundle);
		}			
	}
	
}
