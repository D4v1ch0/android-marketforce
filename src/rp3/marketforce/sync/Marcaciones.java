package rp3.marketforce.sync;

import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;

/**
 * Created by magno_000 on 14/12/2015.
 */
public class Marcaciones {
    public static int executeSyncGrupo(DataBase db){
        WebService webService = new WebService("MartketForce","GetGrupo");

        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONObject jObject = webService.getJSONObjectResponse();
                if(jObject != null) {
                    PreferenceManager.setValue(Contants.KEY_APLICA_MARCACION, jObject.getBoolean(Contants.KEY_APLICA_MARCACION));
                    PreferenceManager.setValue(Contants.KEY_APLICA_BREAK, jObject.getBoolean(Contants.KEY_APLICA_BREAK));
                    if(!jObject.isNull(Contants.KEY_LONGITUD_PARTIDA))
                        PreferenceManager.setValue(Contants.KEY_LONGITUD_PARTIDA, jObject.getDouble(Contants.KEY_LONGITUD_PARTIDA) + "");
                    if(!jObject.isNull(Contants.KEY_LATITUD_PARTIDA))
                        PreferenceManager.setValue(Contants.KEY_LATITUD_PARTIDA, jObject.getDouble(Contants.KEY_LATITUD_PARTIDA) + "");
                }
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
