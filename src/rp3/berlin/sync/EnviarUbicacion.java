package rp3.berlin.sync;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.berlin.models.Ubicacion;
import rp3.util.Convert;
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
	
	public static int executeSyncPendientes(DataBase db){
		try
		{
			JSONArray jArray = new JSONArray();
			
			List<Ubicacion> ubicaciones = Ubicacion.getUbicaciones(db);
			for(Ubicacion ub : ubicaciones)
			{
				JSONObject jObject = new JSONObject();
				jObject.put("Latitud", ub.getLatitud());
				jObject.put("Longitud", ub.getLongitud());
				jObject.put("FechaTicks", Convert.getDotNetTicksFromDate(Convert.getDateFromTicks(ub.getFecha())));
				jArray.put(jObject);
			}
			
			WebService webService = new WebService("MartketForce","SetUbicaciones");
			
			webService.addParameter("ubicaciones", jArray);
									
			webService.addCurrentAuthToken();
						
			webService.invokeWebService();
			
			for(Ubicacion ub : ubicaciones)
			{
				ub.setPendiente(false);
				Ubicacion.update(db, ub);
			}
			
							
		}catch(Exception ex){
			if(ex!=null)
				Log.e("SetUbicacion", ex.getMessage());
		}
		return SyncAdapter.SYNC_EVENT_SUCCESS;		
	}
}
