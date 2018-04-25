package rp3.auna.sync;

import android.util.Log;

import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import rp3.accounts.ServerAuthenticate;
import rp3.accounts.User;
import rp3.auna.models.ventanueva.ComisionesVta;
import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.db.Contract;
import rp3.auna.models.AgenteResumen;
import rp3.auna.models.AgenteUbicacion;
import rp3.runtime.Session;
import rp3.sync.TestConnection;
import rp3.util.Convert;

public class Agente {

    public static String KEY_MESSAGE = "message";
    public static String KEY_DESCUENTO = "descuento";
    private static final String TAG = "AgenteWS";
    public static Bundle executeSyncSignIn(String user, String pass)
    {
        String authType = User.getAccountType();
        WebService method = new WebService();
        Bundle bundle = new Bundle();
        if(TestConnection.executeSync()) {
            method.setConfigurationName("Core", "SignIn");
            method.setAuthTokenType(authType);

            method.addParameter("LogonName", user);
            method.addParameter("Password", pass);

            bundle = new Bundle();


            try {
                method.invokeWebService();

                JSONObject response = method.getJSONObjectResponse();
                String authToken = null;
                String fullName = null;

                if (!response.getJSONObject("Data").isNull("AuthToken")) {
                    authToken = response.getJSONObject("Data").getString("AuthToken");
                    fullName = response.getJSONObject("Data").getString("Name");
                    Session.getUser().setFullName(fullName);
                }

                if (!response.isNull("Message"))
                    bundle.putString(ServerAuthenticate.KEY_ERROR_MESSAGE, response.getJSONObject("Message").getString("Text"));

                bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, authType);
                //bundle.putString(KEY_FULL_NAME, fullName);
                bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, response.getJSONObject("Data").getBoolean("IsValid"));
            } catch (Exception e) {
                bundle.putString(ServerAuthenticate.KEY_ERROR_MESSAGE, e.getMessage());
                bundle.putString(AccountManager.KEY_AUTHTOKEN, null);
                bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, false);
            }
        }
        else {
            bundle.putString(ServerAuthenticate.KEY_ERROR_MESSAGE, "No hay conexión al servidor");
            bundle.putString(AccountManager.KEY_AUTHTOKEN, null);
            bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, false);

        }
        return bundle;
    }

    public static Bundle executeSyncAuthDescuento(String user, String pass) {
        Bundle signIn = executeSyncSignIn(user, pass);

        WebService webService = new WebService("MartketForce", "GetDescuento");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ServerAuthenticate.KEY_SUCCESS, signIn.getBoolean(ServerAuthenticate.KEY_SUCCESS, false));
        if (signIn.getBoolean(ServerAuthenticate.KEY_SUCCESS, false)) {
            try {
                webService.addCurrentAuthToken();
                webService.addStringParameter("@user", user);

                try {
                    webService.invokeWebService();
                    int descuento = webService.getIntegerResponse();
                    bundle.putInt(KEY_DESCUENTO, descuento);
                } catch (HttpResponseException e) {
                    if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                        bundle.putInt(KEY_MESSAGE, SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                    bundle.putInt(KEY_MESSAGE, SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                } catch (Exception e) {
                    bundle.putInt(KEY_MESSAGE, SyncAdapter.SYNC_EVENT_ERROR);
                }

            } finally {
                webService.close();
            }

            bundle.putInt(KEY_MESSAGE, SyncAdapter.SYNC_EVENT_SUCCESS);
        }
        else
        {
            bundle.putInt(KEY_MESSAGE, SyncAdapter.SYNC_EVENT_ERROR);
        }
        return bundle;
    }

	public static int executeSync(DataBase db){
		WebService webService = new WebService("MartketForce","GetAgente");			
			
		try
		{
		    webService.setTimeOut(35000);
			webService.addCurrentAuthToken();
			
			try {
				webService.invokeWebService();
				JSONObject jObject = webService.getJSONObjectResponse();
				if(jObject!=null){
                    Log.d(TAG,"getAgente:"+jObject.toString());

                    if(!jObject.isNull(Contants.KEY_IDAGENTE)){
                        PreferenceManager.setValue(Contants.KEY_IDAGENTE, jObject.getInt(Contants.KEY_IDAGENTE));
                    }else{
                        return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                    }

                    if(!jObject.isNull(Contants.KEY_IDRUTA)){
                        PreferenceManager.setValue(Contants.KEY_IDRUTA, 0);
                        PreferenceManager.setValue(Contants.KEY_IDRUTA, jObject.getInt(Contants.KEY_IDRUTA));
                    }
                    if(!jObject.isNull(Contants.KEY_FOTO)){
                        PreferenceManager.setValue(Contants.KEY_FOTO, jObject.getString(Contants.KEY_FOTO));
                    }
                    if(!jObject.isNull(Contants.KEY_ES_SUPERVISOR)){
                        PreferenceManager.setValue(Contants.KEY_ES_SUPERVISOR, false);
                        PreferenceManager.setValue(Contants.KEY_ES_SUPERVISOR, jObject.getBoolean(Contants.KEY_ES_SUPERVISOR));
                    }

                    if(!jObject.isNull(Contants.KEY_ID_SUPERVISOR)){
                        PreferenceManager.setValue(Contants.KEY_ID_SUPERVISOR, jObject.getInt(Contants.KEY_ID_SUPERVISOR));
                    }
                    if(!jObject.isNull(Contants.KEY_ES_AGENTE)){
                        PreferenceManager.setValue(Contants.KEY_ES_AGENTE, false);
                        PreferenceManager.setValue(Contants.KEY_ES_AGENTE, jObject.getBoolean(Contants.KEY_ES_AGENTE));
                    }
                    if(!jObject.isNull(Contants.KEY_ES_ADMINISTRADOR)){
                        PreferenceManager.setValue(Contants.KEY_ES_ADMINISTRADOR, false);
                        PreferenceManager.setValue(Contants.KEY_ES_ADMINISTRADOR, jObject.getBoolean(Contants.KEY_ES_ADMINISTRADOR));
                    }
                    if(!jObject.isNull(Contants.KEY_CARGO)){
                        Log.d(TAG,"Si hay cargo...");
                        PreferenceManager.setValue(Contants.KEY_CARGO, "");
                        PreferenceManager.setValue(Contants.KEY_CARGO, jObject.getString(Contants.KEY_CARGO));
                    }

                    if(!jObject.isNull(Contants.KEY_DESCUENTO_MAXIMO)){
                        PreferenceManager.setValue(Contants.KEY_DESCUENTO_MAXIMO, jObject.getInt(Contants.KEY_DESCUENTO_MAXIMO));
                    }
                }else{
				    return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                }
			} catch (HttpResponseException e) {
				if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
					return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
				return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
			} catch (Exception e) {
				return SyncAdapter.SYNC_EVENT_ERROR;
			}
			
		}finally{
            String cargo = PreferenceManager.getString(Contants.KEY_CARGO,null);
            if(cargo==null){
                Log.d(TAG,"el cargo es null...");
            }else{
                Log.d(TAG,"el cargo es:"+cargo);
            }
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

    public static int executeSyncGetDeviceId(Context context)
    {
        InstanceID instanceID = InstanceID.getInstance(context);
        try {
            String token = instanceID.getToken(context.getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if(!TextUtils.isEmpty(token))
            {
                PreferenceManager.setValue(Contants.KEY_APP_INSTANCE_ID, token);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            //return SyncAdapter.SYNC_EVENT_ERROR;
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncDeviceId()
    {
        WebService webService = new WebService("MartketForce","SetGCMId");

        try
        {
            webService.setTimeOut(15000);
            webService.addCurrentAuthToken();
            JSONObject jObject = new JSONObject();
            try {
                jObject.put("AuthId", PreferenceManager.getString(Contants.KEY_APP_INSTANCE_ID));
            }catch (Exception ex)
            {}
            webService.addParameter("gcmid", jObject);


            try {
                webService.invokeWebService();
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

    public static int executeSyncSendNotification(int idAgente, String title, String message)
    {
        WebService webService = new WebService("MartketForce","SendNotification");

        try
        {
            webService.addCurrentAuthToken();
            JSONObject jObject = new JSONObject();
            try {
                jObject.put("IdAgente", idAgente);
                jObject.put("Titulo", title);
                jObject.put("Mensaje", message);
            }catch (Exception ex)
            {Log.d(TAG,"exception:"+ex.getMessage());}
            webService.addParameter("notification", jObject);


            try {
                webService.invokeWebService();
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
				if(jObject.isNull(Contants.KEY_ALARMA_INTERVALO)){
                    return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                }
				PreferenceManager.setValue(Contants.KEY_ALARMA_INICIO, Convert.getDateFromDotNetTicks(jObject.getLong(Contants.KEY_ALARMA_INICIO)));
				PreferenceManager.setValue(Contants.KEY_ALARMA_FIN, Convert.getDateFromDotNetTicks(jObject.getLong(Contants.KEY_ALARMA_FIN)));
				PreferenceManager.setValue(Contants.KEY_ALARMA_INTERVALO, jObject.getInt(Contants.KEY_ALARMA_INTERVALO));
				PreferenceManager.setValue(Contants.KEY_PREFIJO_TELEFONICO, jObject.getString(Contants.KEY_PREFIJO_TELEFONICO));
                PreferenceManager.setValue(Contants.KEY_AGENTE_UBICACION_1, jObject.getInt(Contants.KEY_AGENTE_UBICACION_1));
                PreferenceManager.setValue(Contants.KEY_AGENTE_UBICACION_2, jObject.getInt(Contants.KEY_AGENTE_UBICACION_2));
                PreferenceManager.setValue(Contants.KEY_AGENTE_UBICACION_3, jObject.getInt(Contants.KEY_AGENTE_UBICACION_3));
                if(!jObject.isNull(Contants.KEY_MODULO_OPORTUNIDADES))
                    PreferenceManager.setValue(Contants.KEY_MODULO_OPORTUNIDADES, jObject.getBoolean(Contants.KEY_MODULO_OPORTUNIDADES));
                if(!jObject.isNull(Contants.KEY_MODULO_MARCACIONES))
                    PreferenceManager.setValue(Contants.KEY_MODULO_MARCACIONES, jObject.getBoolean(Contants.KEY_MODULO_MARCACIONES));
                if(!jObject.isNull(Contants.KEY_MODULO_POS))
                    PreferenceManager.setValue(Contants.KEY_MODULO_POS, jObject.getBoolean(Contants.KEY_MODULO_POS));
                if(!jObject.isNull(Contants.KEY_MARACIONES_DISTANCIA))
                    PreferenceManager.setValue(Contants.KEY_MARACIONES_DISTANCIA, jObject.getDouble(Contants.KEY_MARACIONES_DISTANCIA) + "");
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
            webService.setTimeOut(55000);
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
            if(types==null){
                return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
            }
            Log.d(TAG,"getAgenteS:"+types.toString());
            rp3.auna.models.Agente.deleteAll(db, Contract.Agente.TABLE_NAME);

            for (int i = 0; i < types.length(); i++) {

                try {
                    JSONObject type = types.getJSONObject(i);
                    rp3.auna.models.Agente agente = new rp3.auna.models.Agente();

                    agente.setIdAgente(type.getInt("IdAgente"));
                    agente.setNombre(type.getString("Nombre"));
                    if(!type.isNull("Telefono"))
                        agente.setTelefono(type.getString("Telefono"));
                    if(!type.isNull("Email"))
                        agente.setEmail(type.getString("Email"));

                    rp3.auna.models.Agente.insert(db, agente);
                } catch (JSONException e) {
                    Log.e("Error", e.toString());
                    return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
                }
            }
        } finally {
            webService.close();
        }

        return rp3.auna.sync.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncLog() {
        WebService webService = new WebService("MartketForce", "SendLog");
        try {
            webService.addCurrentAuthToken();

            try {
                File file2 = new File(Environment.getExternalStorageDirectory() + "/test.log");
                file2.setExecutable(true);
                file2.setReadable(true);
                file2.setWritable(true);

                String logs = "";
                InputStream in = new FileInputStream(file2);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    logs = logs + new String(buf);
                }
                in.close();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Logs",logs);
                logs = null;

                webService.addParameter("logs", jsonObject);
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }


        } finally {
            webService.close();
        }

        return rp3.auna.sync.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncComisiones(DataBase db) {
        WebService webService = new WebService("MartketForce", "Comisiones");
        try {
            webService.addCurrentAuthToken();
            webService.addIntParameter("@idagente", PreferenceManager.getInt(Contants.KEY_IDAGENTE));
            try {
                webService.invokeWebService();
                try{
                    JSONObject jObject = webService.getJSONObjectResponse();
                    if(jObject!=null){
                        Log.d(TAG,"jObject!=null...");
                        ComisionesVta.deleteAll(db, Contract.ComisionAgente.TABLE_NAME);
                        Log.d(TAG,"despues deleteAll...");
                        ComisionesVta comisionesVta = new ComisionesVta();
                        comisionesVta.setIdAgente(jObject.getInt("IdAgente"));
                        comisionesVta.setPkAsesor(jObject.getInt("PkAsesor"));
                        comisionesVta.setNombreAsesor(jObject.getString("NombreAsesor"));
                        comisionesVta.setVentas(String.valueOf(jObject.getInt("Ventas")));
                        comisionesVta.setComision(String.valueOf(jObject.getDouble("Comision")));
                        comisionesVta.setIncentivo(String.valueOf(jObject.getDouble("Incentivo")));
                        ComisionesVta.insert(db, comisionesVta);
                        Log.d(TAG,"despues del insert...");
                    }else{
                        Log.d(TAG,"jObject==null...");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            } catch (HttpResponseException e) {
                Log.d(TAG,"HttpResponseException...");
                e.printStackTrace();
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED){
                    return rp3.auna.sync.SyncAdapter.SYNC_EVENT_SUCCESS;
                    //return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                }
                //return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
                return rp3.auna.sync.SyncAdapter.SYNC_EVENT_SUCCESS;
            } catch (Exception e) {
                //return SyncAdapter.SYNC_EVENT_ERROR;
                return rp3.auna.sync.SyncAdapter.SYNC_EVENT_SUCCESS;
            }
        } finally {
            Log.d(TAG,"despues deleteAll...");
            webService.close();
        }

        return rp3.auna.sync.SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
