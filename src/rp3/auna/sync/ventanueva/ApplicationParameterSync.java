package rp3.auna.sync.ventanueva;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.auna.Contants;
import rp3.auna.db.Contract;
import rp3.auna.util.constants.Constants;
import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.util.Convert;

/**
 * Created by Jesus Villa on 20/12/2017.
 */

public class ApplicationParameterSync {
    private static final String TAG = ApplicationParameterSync.class.getSimpleName();

    public static int executeSync(DataBase db) {
        WebService webService = new WebService("MartketForce", "ObtenerApplicationParameter");
        Log.d(TAG,"Iniciar WS executeSync...");
        try {
            Log.d(TAG,"applicationId="+ Constants.APPLICATIONID);
            webService.addParameter("@applicationId", Constants.APPLICATIONID);
            webService.addCurrentAuthToken();
            try {
                webService.setTimeOut(30000);
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED){
                    Log.d(TAG,"e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED");
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                }
                Log.d(TAG,"rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR");
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                Log.d(TAG,"Exception e:"+e.getMessage());
                Log.d(TAG,"rp3.content.SyncAdapter.SYNC_EVENT_ERROR");
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }
            Log.d(TAG,"antes eliminar todas las applicationParameter...");
            Log.d(TAG,"despues ingresar todo...");
            try {
                JSONArray parameters = webService.getJSONArrayResponse();
                Log.d(TAG,"cantidad de applicationParameter:"+parameters.length());
                if(parameters.length()>0) {
                    rp3.auna.models.ApplicationParameter.deleteAll(db,Contract.ApplicationParameter.TABLE_NAME);
                    for (int i = 0; i < parameters.length(); i++) {
                        JSONObject llamada = parameters.getJSONObject(i);
                        /**
                         * Obtener datos de Llamada
                         */
                        rp3.auna.models.ApplicationParameter llamadaVtaDb = new rp3.auna.models.ApplicationParameter();
                        if (!llamada.isNull("ApplicationId")) {
                            llamadaVtaDb.setApplicationId(llamada.getString("ApplicationId"));
                        } else {
                            llamadaVtaDb.setApplicationId(null);
                        }
                        if (!llamada.isNull("ParameterId")) {
                            llamadaVtaDb.setParameterId(llamada.getString("ParameterId"));
                        } else {
                            llamadaVtaDb.setParameterId(null);
                        }
                        if (!llamada.isNull("Value")) {
                            llamadaVtaDb.setValue(llamada.getString("Value"));
                        } else {
                            llamadaVtaDb.setValue(null);
                        }
                        if (!llamada.isNull("Label")) {
                            llamadaVtaDb.setLabel((llamada.getString("Label")));
                        } else {
                            llamadaVtaDb.setLabel(null);
                        }
                        if (!llamada.isNull("Description")) {
                            llamadaVtaDb.setDescription((llamada.getString("Description")));
                        } else {
                            llamadaVtaDb.setDescription(null);
                        }

                        boolean insertLlamada = rp3.auna.models.ApplicationParameter.insert(db, llamadaVtaDb);
                        //Log.d(TAG,insertLlamada?"parameter insertada...":"parameter no fue insertada...");
                    }
                    Log.d(TAG,"Cantidad de parameter:"+ rp3.auna.models.ApplicationParameter.getAll(db).size());
                }else{
                    return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                }
            } catch (JSONException e) {
                Log.e("Error", e.toString());
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }
        } finally {
            webService.close();
        }

        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
