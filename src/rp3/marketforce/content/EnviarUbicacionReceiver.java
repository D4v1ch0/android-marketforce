package rp3.marketforce.content;

import rp3.marketforce.sync.EnviarUbicacion;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.LocationUtils;
import rp3.util.LocationUtils.OnLocationResultListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

public class EnviarUbicacionReceiver extends BroadcastReceiver   {

	@Override
	public void onReceive(Context context, Intent intent) {						
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
	
}
