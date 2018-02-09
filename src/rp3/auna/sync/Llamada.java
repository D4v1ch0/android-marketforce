package rp3.auna.sync;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.util.List;

import rp3.auna.Contants;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.util.Convert;

/**
 * Created by Jesus Villa on 12/09/2017.
 */

public class Llamada {
    private static final String TAG = Llamada.class.getSimpleName();

    public static int executeSync(DataBase db) {
        WebService webService = new WebService("MartketForce", "Llamadas");


        try {
            Log.d(TAG,"idAgente="+PreferenceManager.getInt(Contants.KEY_IDAGENTE));
            webService.addParameter("@idagente", PreferenceManager.getInt(Contants.KEY_IDAGENTE));
            webService.addCurrentAuthToken();
            try {
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
            Log.d(TAG,"antes de deleteLlamadas...");
            LlamadaVta.deleteLlamadas(db);
            Log.d(TAG,"despues de deleteLlamadas...");
            try {
                JSONArray llamadas = webService.getJSONArrayResponse();
                Log.d(TAG,"cantidad de llamadas:"+llamadas.length());
                if(llamadas.length()>0) {
                    for (int i = 0; i < llamadas.length(); i++) {

                        JSONObject llamada = llamadas.getJSONObject(i);
                        LlamadaVta llamadaVtaDb = new LlamadaVta();

                        if (!llamada.isNull("IdLlamada")) {
                            llamadaVtaDb.setIdLlamada(llamada.getInt("IdLlamada"));
                        } else {
                            llamadaVtaDb.setIdLlamada(0);
                        }
                        if (!llamada.isNull("Descripcion")) {
                            llamadaVtaDb.setDescripcion(llamada.getString("Descripcion"));
                        } else {
                            llamadaVtaDb.setDescripcion("");
                        }
                        if (!llamada.isNull("FechaLlamada")) {
                            llamadaVtaDb.setFechaLlamada(Convert.getDateFromDotNetTicks(llamada.getLong("FechaLlamada")));
                        } else {
                            llamadaVtaDb.setFechaLlamada(null);
                        }
                        if (!llamada.isNull("IdCliente")) {
                            llamadaVtaDb.setIdCliente(llamada.getInt("IdCliente"));
                        } else {
                            llamadaVtaDb.setIdCliente(0);
                        }
                        if (!llamada.isNull("IdAgente")) {
                            llamadaVtaDb.setIdAgente(llamada.getInt("IdAgente"));
                        } else {
                            llamadaVtaDb.setIdAgente(0);
                        }
                        if (!llamada.isNull("Duracion")) {
                            llamadaVtaDb.setDuracion(llamada.getInt("Duracion"));
                        } else {
                            llamadaVtaDb.setDuracion(0);
                        }
                        if (!llamada.isNull("LlamadaTabla")) {
                            llamadaVtaDb.setLlamadaTabla(llamada.getInt("LlamadaTabla"));
                        } else {
                            llamadaVtaDb.setLlamadaTabla(0);
                        }
                        if (!llamada.isNull("LlamadoValue")) {
                            llamadaVtaDb.setLlamadoValue(llamada.getString("LlamadoValue"));
                        } else {
                            llamadaVtaDb.setLlamadoValue("");
                        }

                        if (!llamada.isNull("MotivoLlamadaTabla")) {
                            llamadaVtaDb.setMotivoVisitaTabla(llamada.getInt("MotivoLlamadaTabla"));
                        } else {
                            llamadaVtaDb.setMotivoVisitaTabla(0);
                        }
                        if (!llamada.isNull("MotivoLlamadaValue")) {
                            llamadaVtaDb.setMotivoVisitaValue(llamada.getString("MotivoLlamadaValue"));
                        } else {
                            llamadaVtaDb.setMotivoVisitaValue("");
                        }
                        if (!llamada.isNull("Observacion")) {
                            llamadaVtaDb.setObservacion(llamada.getString("Observacion"));
                        } else {
                            llamadaVtaDb.setObservacion("");
                        }
                        if (!llamada.isNull("Latitud")) {
                            llamadaVtaDb.setLatitud(llamada.getInt("Latitud"));
                        } else {
                            llamadaVtaDb.setLatitud(0);
                        }
                        if (!llamada.isNull("Longitud")) {
                            llamadaVtaDb.setLongitud(llamada.getInt("Longitud"));
                        } else {
                            llamadaVtaDb.setLongitud(0);
                        }
                        if (!llamada.isNull("ReferidoTabla")) {
                            llamadaVtaDb.setReferidoTabla(llamada.getInt("ReferidoTabla"));
                        } else {
                            llamadaVtaDb.setReferidoTabla(0);
                        }
                        if (!llamada.isNull("ReferidoValue")) {
                            llamadaVtaDb.setReferidoValue(llamada.getString("ReferidoValue"));
                        } else {
                            llamadaVtaDb.setReferidoValue("");
                        }
                        if (!llamada.isNull("MotivoReprogramacionTabla")) {
                            llamadaVtaDb.setMotivoReprogramacionTabla(llamada.getInt("MotivoReprogramacionTabla"));
                        } else {
                            llamadaVtaDb.setMotivoReprogramacionTabla(0);
                        } if (!llamada.isNull("MotivoReprogramacionValue")) {
                            llamadaVtaDb.setMotivoReprogramacionValue(llamada.getString("MotivoReprogramacionValue"));
                        } else {
                            llamadaVtaDb.setMotivoReprogramacionValue("");
                        } if (!llamada.isNull("LlamadaValor")) {
                            llamadaVtaDb.setLlamadaValor(llamada.getInt("LlamadaValor"));
                        } else {
                            llamadaVtaDb.setLlamadaValor(0);
                        }


                        llamadaVtaDb.setEstado(0);
                        llamadaVtaDb.setInsertado(0);
                        boolean insert = LlamadaVta.insert(db, llamadaVtaDb);
                        if (insert) {
                            Log.d(TAG, "llamada insertada..." + llamadaVtaDb.toString());
                        }
                    }
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

    public static int executeSyncInserts(DataBase db){
        WebService webService = new WebService("MartketForce","InsertarLlamada");
        boolean failed = false;
        int id = 0;
        List<LlamadaVta> llamadaVtas = LlamadaVta.getLlamadasInsert(db);
        if(llamadaVtas.size() == 0){
            Log.d(TAG,"llamadaVtas.size() == 0...");
            return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
        }
        JSONObject jObject = new JSONObject();
        JSONArray jArray = new JSONArray();
        Log.d(TAG,"Cantidad de llamadaVtas a insertar:"+ llamadaVtas.size());
        for(int s = 0; s < llamadaVtas.size(); s ++)
        {
            LlamadaVta cl = llamadaVtas.get(s);
            Log.d(TAG,cl.toString());
            try
            {
                jObject = new JSONObject();

                jObject.put("IdLlamada", cl.getIdLlamada());
                jObject.put("Descripcion", cl.getDescripcion());
                jObject.put("FechaLlamada", Convert.getDotNetTicksFromDate(cl.getFechaLlamada()));
                Log.d(TAG,"Ticks:"+Convert.getDotNetTicksFromDate(cl.getFechaLlamada()));
                jObject.put("IdCliente", cl.getIdCliente());
                jObject.put("IdAgente", cl.getIdAgente());
                jObject.put("Duracion", cl.getDuracion());
                //jObject.put("EstadoLlamadaTabla", cl.getEstadoLlamadaTabla());
                //jObject.put("EstadoLlamadoValue", cl.getEstadoLlamadoValue());
                jObject.put("Estado", cl.getEstado());

                jArray.put(jObject);
            }
            catch(Exception ex)
            {
                Log.d(TAG,"Exception:"+ex.getMessage());
            }
        }
        webService.addParameter("llamada", jArray);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED){
                    failed = true;
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                 }
                failed = true;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                failed = true;
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

        } finally {
            if(failed==false){
                Log.d(TAG,"insertado");
                LlamadaVta.actualizarInsertados(db);
                if(LlamadaVta.getLlamadasInsert(db).size()==0){
                    Log.d(TAG,"Ya no hay insertados con estado 1 ..se subieron al servidor...");
                }else{
                    Log.d(TAG,"Si hay con estado 1insertados...");
                }
            }
            webService.close();
        }
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
