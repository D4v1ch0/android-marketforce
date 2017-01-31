package rp3.auna.sync;

import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.auna.Contants;

public class Server {
	public static int executeSync(String code){
		WebService webService = new WebService("ServerVerification","GetServer");			
			
		try
		{			
			webService.addStringParameter("@applicationid", Contants.APPLICATION_ID);
			webService.addStringParameter("@validationCode", code);
			
			try {
				webService.invokeWebService();
				JSONObject jObject = webService.getJSONObjectResponse();
				PreferenceManager.setValue(Contants.KEY_CLIENT, jObject.getString("ClientName"));
				PreferenceManager.setValue(Contants.KEY_SERVER, jObject.getString("Server"));
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
