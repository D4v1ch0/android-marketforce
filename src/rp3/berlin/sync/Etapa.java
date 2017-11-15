package rp3.berlin.sync;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.berlin.db.Contract;
import rp3.berlin.models.oportunidad.EtapaTarea;

/**
 * Created by magno_000 on 18/05/2015.
 */
public class Etapa {
    public static int executeSync(DataBase db){
        WebService webService = new WebService("MartketForce","GetEtapas");
        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

            JSONArray types = webService.getJSONArrayResponse();

            rp3.berlin.models.oportunidad.Etapa.deleteAll(db, Contract.Etapa.TABLE_NAME);
            EtapaTarea.deleteAll(db, Contract.EtapaTarea.TABLE_NAME);

            for(int i=0; i < types.length(); i++){

                try {
                    JSONObject type = types.getJSONObject(i);
                    rp3.berlin.models.oportunidad.Etapa etapa = new rp3.berlin.models.oportunidad.Etapa();

                    etapa.setIdEtapa(type.getInt("IdEtapa"));
                    etapa.setOrden(type.getInt("Orden"));
                    etapa.setEstado(type.getString("Estado"));
                    etapa.setIdOportunidadTipo(type.getInt("IdOportunidadTipo"));
                    if(!type.isNull("IdEtapaPadre"))
                        etapa.setIdEtapaPadre(type.getInt("IdEtapaPadre"));
                    else
                        etapa.setIdEtapaPadre(0);

                    if(!type.isNull("Dias"))
                        etapa.setDias(type.getInt("Dias"));
                    else
                        etapa.setDias(0);

                    if(!type.isNull("EsVariable"))
                        etapa.setEsVariable(type.getBoolean("EsVariable"));
                    else
                        etapa.setEsVariable(false);

                    etapa.setDescripcion(type.getString("Descripcion"));

                    rp3.berlin.models.oportunidad.Etapa.insert(db, etapa);


                    JSONArray strs = type.getJSONArray("EtapaTareas");

                    for (int j = 0; j < strs.length(); j++) {
                        JSONObject str = strs.getJSONObject(j);
                        EtapaTarea etapaTarea = new EtapaTarea();

                        etapaTarea.setIdTarea(str.getInt("IdTarea"));
                        etapaTarea.setOrden(str.getInt("Orden"));
                        etapaTarea.setIdEtapa(type.getInt("IdEtapa"));

                        EtapaTarea.insert(db, etapaTarea);
                    }


                } catch (JSONException e) {
                    Log.e("Error", e.toString());
                    return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
                }
            }
        }finally{
            webService.close();
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncTipos(DataBase db){
        WebService webService = new WebService("MartketForce","GetOportunidadTipos");
        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

            JSONArray types = webService.getJSONArrayResponse();

            rp3.berlin.models.oportunidad.OportunidadTipo.deleteAll(db, Contract.OportunidadTipo.TABLE_NAME);

            for(int i=0; i < types.length(); i++){

                try {
                    JSONObject type = types.getJSONObject(i);
                    rp3.berlin.models.oportunidad.OportunidadTipo optTipo = new rp3.berlin.models.oportunidad.OportunidadTipo();

                    optTipo.setIdOportunidadTipo(type.getInt("IdOportunidadTipo"));
                    optTipo.setDescripcion(type.getString("Descripcion"));

                    rp3.berlin.models.oportunidad.OportunidadTipo.insert(db, optTipo);

                } catch (JSONException e) {
                    Log.e("Error", e.toString());
                    return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
                }
            }
        }finally{
            webService.close();
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}