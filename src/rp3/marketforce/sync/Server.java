package rp3.marketforce.sync;

import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;

public class Server {
	public static int executeSync(String code){
		WebService webService = new WebService("MartketForce","GetServer");			
			
		try
		{			
			webService.addCurrentAuthToken();
			webService.addStringParameter("@applicationid", Contants.APPLICATION_ID);
			webService.addStringParameter("@validationCode", code);
			
			try {
				webService.invokeWebService();
				JSONObject jObject = webService.getJSONObjectResponse();
				PreferenceManager.setValue(Contants.KEY_IDAGENTE, jObject.getInt(Contants.KEY_IDAGENTE));
				if(!jObject.isNull(Contants.KEY_IDRUTA))
					PreferenceManager.setValue(Contants.KEY_IDRUTA, jObject.getInt(Contants.KEY_IDRUTA));
				PreferenceManager.setValue(Contants.KEY_ES_SUPERVISOR, jObject.getBoolean(Contants.KEY_ES_SUPERVISOR));
				PreferenceManager.setValue(Contants.KEY_ES_AGENTE, jObject.getBoolean(Contants.KEY_ES_AGENTE));
				PreferenceManager.setValue(Contants.KEY_ES_ADMINISTRADOR, jObject.getBoolean(Contants.KEY_ES_ADMINISTRADOR));
			} catch (HttpResponseException e) {
				if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
					return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
				return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
			} catch (Exception e) {
				return SyncAdapter.SYNC_EVENT_ERROR;
			}
			
		}finally{
			webService.close();
		}
		
		return SyncAdapter.SYNC_EVENT_SUCCESS;		
	}
}
