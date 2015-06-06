package rp3.marketforce.sync;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.AgenteResumen;
import rp3.marketforce.models.AgenteUbicacion;
import rp3.util.Convert;

public class Agente {

	public static int executeSync(DataBase db){
		WebService webService = new WebService("MartketForce","GetAgente");			
			
		try
		{			
			webService.addCurrentAuthToken();
			
			try {
				webService.invokeWebService();
				JSONObject jObject = webService.getJSONObjectResponse();
				PreferenceManager.setValue(Contants.KEY_IDAGENTE, jObject.getInt(Contants.KEY_IDAGENTE));
				if(!jObject.isNull(Contants.KEY_IDRUTA))
                    PreferenceManager.setValue(Contants.KEY_IDRUTA, jObject.getInt(Contants.KEY_IDRUTA));
                if(!jObject.isNull(Contants.KEY_FOTO))
                    PreferenceManager.setValue(Contants.KEY_FOTO, jObject.getString(Contants.KEY_FOTO));
				PreferenceManager.setValue(Contants.KEY_ES_SUPERVISOR, jObject.getBoolean(Contants.KEY_ES_SUPERVISOR));
				PreferenceManager.setValue(Contants.KEY_ES_AGENTE, jObject.getBoolean(Contants.KEY_ES_AGENTE));
				PreferenceManager.setValue(Contants.KEY_ES_ADMINISTRADOR, jObject.getBoolean(Contants.KEY_ES_ADMINISTRADOR));
				PreferenceManager.setValue(Contants.KEY_CARGO, jObject.getString(Contants.KEY_CARGO));
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
	
	public static int executeSyncGetAgente(DataBase db){
		WebService webService = new WebService("MartketForce","GetResumenGestionAgentes");			
			
		try
		{			
			webService.addCurrentAuthToken();
			webService.addLongParameter("@idagentesupervisor", PreferenceManager.getInt(Contants.KEY_IDAGENTE));
			
			
			try {
				webService.invokeWebService();
				JSONArray jArray = webService.getJSONArrayResponse();
				AgenteResumen.deleteAll(db, Contract.AgentesResumen.TABLE_NAME);
				for(int i = 0 ; i < jArray.length(); i ++)
				{
					JSONObject jObject = jArray.getJSONObject(i);
					AgenteResumen resumen = new AgenteResumen();
					resumen.setIdAgente(jObject.getInt("IdAgente"));
					resumen.setNombres(jObject.getString("Nombres"));
					resumen.setApellidos(jObject.getString("Apellidos"));
					resumen.setFecha(Convert.getDateFromDotNetTicks(jObject.getLong("FechaTicks")));
					resumen.setGestionados(jObject.getInt("Gestionados"));
					resumen.setNoGestionados(jObject.getInt("NoGestionados"));
					resumen.setPendientes(jObject.getInt("Proximos"));
					AgenteResumen.insert(db, resumen);
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

    public static int executeSyncGetUbicaciones(DataBase db){
        WebService webService = new WebService("MartketForce","GetResumenUbicacionAgentes");

        try
        {
            webService.addCurrentAuthToken();
            webService.addLongParameter("@idagentesupervisor", PreferenceManager.getInt(Contants.KEY_IDAGENTE));


            try {
                webService.invokeWebService();
                JSONArray jArray = webService.getJSONArrayResponse();
                AgenteUbicacion.deleteAll(db, Contract.AgentesUbicacion.TABLE_NAME);
                for(int i = 0 ; i < jArray.length(); i ++)
                {
                    JSONObject jObject = jArray.getJSONObject(i);
                    AgenteUbicacion ubicacion = new AgenteUbicacion();
                    ubicacion.setIdAgente(jObject.getInt("IdAgente"));
                    ubicacion.setNombres(jObject.getString("Nombres"));
                    ubicacion.setApellidos(jObject.getString("Apellidos"));
                    ubicacion.setFecha(Convert.getDateFromDotNetTicks(jObject.getLong("UltimaActualizacionTicks")));
                    ubicacion.setLatitud(jObject.getDouble("Latitud"));
                    ubicacion.setLongitud(jObject.getDouble("Longitud"));
                    AgenteUbicacion.insert(db, ubicacion);
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
	
	public static int executeSyncParametros(DataBase db){
		WebService webService = new WebService("MartketForce","GetParametros");

		try
		{
			webService.addCurrentAuthToken();

			try {
				webService.invokeWebService();
				JSONObject jObject = webService.getJSONObjectResponse();
				PreferenceManager.setValue(Contants.KEY_ALARMA_INICIO, Convert.getDateFromDotNetTicks(jObject.getLong(Contants.KEY_ALARMA_INICIO)));
				PreferenceManager.setValue(Contants.KEY_ALARMA_FIN, Convert.getDateFromDotNetTicks(jObject.getLong(Contants.KEY_ALARMA_FIN)));
				PreferenceManager.setValue(Contants.KEY_ALARMA_INTERVALO, jObject.getInt(Contants.KEY_ALARMA_INTERVALO));
				PreferenceManager.setValue(Contants.KEY_PREFIJO_TELEFONICO, jObject.getString(Contants.KEY_PREFIJO_TELEFONICO));
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

    public static int executeSyncAgentes(DataBase db) {
        WebService webService = new WebService("MartketForce", "GetAgentesOportunidad");
        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

            JSONArray types = webService.getJSONArrayResponse();

            rp3.marketforce.models.oportunidad.Agente.deleteAll(db, Contract.Agente.TABLE_NAME);

            for (int i = 0; i < types.length(); i++) {

                try {
                    JSONObject type = types.getJSONObject(i);
                    rp3.marketforce.models.oportunidad.Agente agente = new rp3.marketforce.models.oportunidad.Agente();

                    agente.setIdAgente(type.getInt("IdAgente"));
                    agente.setNombre(type.getString("Nombre"));

                    rp3.marketforce.models.oportunidad.Agente.insert(db, agente);
                } catch (JSONException e) {
                    Log.e("Error", e.toString());
                    return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
                }
            }
        } finally {
            webService.close();
        }

        return rp3.marketforce.sync.SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
