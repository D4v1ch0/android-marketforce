package rp3.marketforce.sync;

import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import android.util.Log;

public class EnviarUbicacion {

	public final static String ARG_LONGITUD = "poslongitud";
	public final static String ARG_LATITUD = "poslatitud";
	
	public static int executeSync(double longitud, double latitud){
		try
		{
			
			WebService webService = new WebService("MartketForce","SetUbicacion");
			
			webService.addParameter("@longitud", longitud);
			webService.addParameter("@latitud", latitud);
									
			webService.addCurrentAuthToken();
						
			webService.invokeWebService();
			
							
		}catch(Exception ex){
			if(ex!=null)
				Log.e("SetUbicacion", ex.getMessage());
		}
		return SyncAdapter.SYNC_EVENT_SUCCESS;		
	}	
}
