package rp3.marketforce.sync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import android.util.Log;

public class Canal {
	
		public static int executeSync(DataBase db){
			WebService webService = new WebService("MartketForce","GetCanales");
			try
			{			
				webService.addCurrentAuthToken();
				
				try {
					webService.invokeWebService();
				} catch (HttpResponseException e) {
					if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
						return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
					return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
				} catch (Exception e) {
					return SyncAdapter.SYNC_EVENT_ERROR;
				}
				
				JSONArray types = webService.getJSONArrayResponse();			
				
				rp3.marketforce.models.Canal.deleteAll(db, Contract.Canal.TABLE_NAME);
				
				for(int i=0; i < types.length(); i++){
					try {
						
						JSONObject type = types.getJSONObject(i);
						
						rp3.marketforce.models.Canal cl = new rp3.marketforce.models.Canal();
						cl.setID(type.getLong("IdCanal"));
						cl.setDescripcion(type.getString("Descripcion"));
					
						rp3.marketforce.models.Canal.insert(db, cl);
						
					} catch (JSONException e) {
						Log.e("Entro","Error: "+e.toString());
						return SyncAdapter.SYNC_EVENT_ERROR;
					}
				}
			}finally{
				webService.close();
			}
			return SyncAdapter.SYNC_EVENT_SUCCESS;		
		}
	
}
