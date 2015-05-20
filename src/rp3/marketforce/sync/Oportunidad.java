package rp3.marketforce.sync;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.util.ArrayList;
import java.util.List;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.oportunidad.EtapaTarea;
import rp3.marketforce.models.oportunidad.OportunidadContacto;
import rp3.marketforce.models.oportunidad.OportunidadFoto;
import rp3.marketforce.models.oportunidad.OportunidadResponsable;
import rp3.marketforce.models.oportunidad.OportunidadTarea;
import rp3.marketforce.models.oportunidad.OportunidadTareaActividad;
import rp3.util.Convert;

/**
 * Created by magno_000 on 18/05/2015.
 */
public class Oportunidad {
    public static int executeSyncInserts(DataBase db) {
        WebService webService = null;
        List<rp3.marketforce.models.oportunidad.Oportunidad> oportunidades = rp3.marketforce.models.oportunidad.Oportunidad.getOportunidadesInserts(db);
        if (oportunidades.size() == 0)
            return SyncAdapter.SYNC_EVENT_SUCCESS;

        JSONArray jArray = new JSONArray();
        for (int i = 0; i < oportunidades.size(); i++) {
            webService = new WebService("MartketForce", "CreateOportunidad");
            webService.setTimeOut(20000);
            rp3.marketforce.models.oportunidad.Oportunidad oportunidadUpload = oportunidades.get(i);
            JSONObject jObject = new JSONObject();
            try {
                jObject.put("IdOportunidad", oportunidadUpload.getIdOportunidad());
                jObject.put("IdInterno", oportunidadUpload.getID());
                jObject.put("IdEtapa", oportunidadUpload.getIdEtapa());
                jObject.put("IdAgente", oportunidadUpload.getIdAgente());
                jObject.put("Descripcion", oportunidadUpload.getDescripcion());
                jObject.put("Calificacion", oportunidadUpload.getCalificacion());
                jObject.put("FechaUltimaGestionTicks", Convert.getDotNetTicksFromDate(oportunidadUpload.getFechaUltimaGestion()));
                jObject.put("Estado", oportunidadUpload.getEstado());
                jObject.put("Direccion", oportunidadUpload.getDireccion());
                jObject.put("Importe", oportunidadUpload.getImporte());
                jObject.put("Latitud", oportunidadUpload.getLatitud());
                jObject.put("Longitud", oportunidadUpload.getLongitud());
                jObject.put("Observacion", oportunidadUpload.getObservacion());
                jObject.put("Probabilidad", oportunidadUpload.getProbabilidad());
                jObject.put("Referencia", oportunidadUpload.getReferencia());

                JSONArray jArrayTareas = new JSONArray();
                for (OportunidadTarea agt : oportunidadUpload.getOportunidadTareas()) {
                    JSONObject jObjectTarea = new JSONObject();
                    jObjectTarea.put("IdTarea", agt.getIdTarea());
                    jObjectTarea.put("Observacion", agt.getObservacion());
                    jObjectTarea.put("IdEtapa", agt.getIdEtapa());
                    jObjectTarea.put("Orden", agt.getOrden());
                    jObjectTarea.put("Estado", agt.getEstado());

                    JSONArray jArrayActividades = new JSONArray();
                    for (OportunidadTareaActividad ata : agt.getOportunidadTareaActividades()) {
                        JSONObject jObjectActividad = new JSONObject();
                        //jObjectActividad.put("IdAgenda", ata.getIdAgenda());
                        if (ata.getResultado() == null || ata.getResultado().equals("null"))
                            jObjectActividad.put("Resultado", " ");
                        else if (ata.getResultado().equals("true"))
                            jObjectActividad.put("Resultado", "Si");
                        else if (ata.getResultado().equals("false"))
                            jObjectActividad.put("Resultado", "No");
                        else
                            jObjectActividad.put("Resultado", ata.getResultado());

                        jObjectActividad.put("IdTareaActividad", ata.getIdTareaActividad());
                        if (ata.getIdsResultado() != null)
                            jObjectActividad.put("ResultadoCodigo", ata.getIdsResultado());
                        jObjectActividad.put("IdTarea", ata.getIdTarea());

                        jArrayActividades.put(jObjectActividad);
                    }

                    jObjectTarea.put("OportunidadTareaActividads", jArrayActividades);

                    jArrayTareas.put(jObjectTarea);
                }

                jObject.put("OportunidadTareas", jArrayTareas);

                JSONArray jArrayContactos = new JSONArray();
                for (OportunidadContacto agt : oportunidadUpload.getOportunidadContactos()) {
                    JSONObject jObjectContacto = new JSONObject();
                    jObjectContacto.put("IdInterno", agt.getID());
                    jObjectContacto.put("Cargo", agt.getCargo());
                    jObjectContacto.put("CorreoElectronico", agt.getEmail());
                    jObjectContacto.put("Telefono2", agt.getFijo());
                    jObjectContacto.put("Telefono1", agt.getMovil());
                    jObjectContacto.put("Nombre", agt.getNombre());
                    jObjectContacto.put("EsPrincipal", agt.isEsPrincipal());

                    jArrayContactos.put(jObjectContacto);
                }
                jObject.put("OportunidadContactos", jArrayContactos);

                JSONArray jArrayResponsables = new JSONArray();
                for (OportunidadResponsable agt : oportunidadUpload.getOportunidadResponsables()) {
                    JSONObject jObjectResponsable = new JSONObject();
                    jObjectResponsable.put("IdAgente", agt.getIdAgente());

                    jArrayResponsables.put(jObjectResponsable);
                }
                jObject.put("OportunidadResponsables", jArrayResponsables);

            } catch (Exception ex) {

            }

            webService.addParameter("oportunidad", jObject);

            try {
                webService.addCurrentAuthToken();

                    webService.invokeWebService();
                    int id = webService.getIntegerResponse();
                oportunidadUpload.setIdOportunidad(id);
                oportunidadUpload.setPendiente(false);
                rp3.marketforce.models.oportunidad.Oportunidad.update(db, oportunidadUpload);
                for (OportunidadTarea agt : oportunidadUpload.getOportunidadTareas()) {
                    agt.setIdOportunidad(id);
                    for (OportunidadTareaActividad ata : agt.getOportunidadTareaActividades()) {
                        ata.setIdOportunidad(1);
                        OportunidadTareaActividad.update(db, ata);
                    }
                    OportunidadTarea.update(db, agt);
                }

                for (OportunidadContacto agt : oportunidadUpload.getOportunidadContactos()) {
                    agt.setIdOportunidad(id);
                    OportunidadContacto.update(db, agt);
                }
                for (OportunidadResponsable agt : oportunidadUpload.getOportunidadResponsables()) {
                    agt.setIdOportunidad(id);
                    OportunidadResponsable.update(db, agt);
                }

                } catch (HttpResponseException e) {
                    if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                        return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                    return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
                } catch (Exception e) {
                    return SyncAdapter.SYNC_EVENT_ERROR;
                }
            }


        webService.close();

        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }


    public static int executeSyncPendientes(DataBase db) {
        WebService webService = new WebService("MartketForce", "UpdateOportunidad");
        webService.setTimeOut(20000);

        List<rp3.marketforce.models.oportunidad.Oportunidad> oportunidades = rp3.marketforce.models.oportunidad.Oportunidad.getOportunidadesPendientes(db);
        if (oportunidades.size() == 0)
            return SyncAdapter.SYNC_EVENT_SUCCESS;

        JSONArray jArray = new JSONArray();
        for (int i = 0; i < oportunidades.size(); i++) {
            rp3.marketforce.models.oportunidad.Oportunidad oportunidadUpload = oportunidades.get(i);
            JSONObject jObject = new JSONObject();
            try {
                jObject.put("IdOportunidad", oportunidadUpload.getIdOportunidad());
                jObject.put("IdInterno", oportunidadUpload.getID());
                jObject.put("IdEtapa", oportunidadUpload.getIdEtapa());
                jObject.put("IdAgente", oportunidadUpload.getIdAgente());
                jObject.put("Descripcion", oportunidadUpload.getDescripcion());
                jObject.put("Calificacion", oportunidadUpload.getCalificacion());
                jObject.put("FechaUltimaGestionTicks", Convert.getDotNetTicksFromDate(oportunidadUpload.getFechaUltimaGestion()));
                jObject.put("Estado", oportunidadUpload.getEstado());
                jObject.put("Direccion", oportunidadUpload.getDireccion());
                jObject.put("Importe", oportunidadUpload.getImporte());
                jObject.put("Latitud", oportunidadUpload.getLatitud());
                jObject.put("Longitud", oportunidadUpload.getLongitud());
                jObject.put("Observacion", oportunidadUpload.getObservacion());
                jObject.put("Probabilidad", oportunidadUpload.getProbabilidad());
                jObject.put("Referencia", oportunidadUpload.getReferencia());

                JSONArray jArrayTareas = new JSONArray();
                for (OportunidadTarea agt : oportunidadUpload.getOportunidadTareas()) {
                    JSONObject jObjectTarea = new JSONObject();
                    jObjectTarea.put("IdTarea", agt.getIdTarea());
                    jObjectTarea.put("Observacion", agt.getObservacion());
                    jObjectTarea.put("IdEtapa", agt.getIdEtapa());
                    jObjectTarea.put("Orden", agt.getOrden());
                    jObjectTarea.put("Estado", agt.getEstado());

                    JSONArray jArrayActividades = new JSONArray();
                    for (OportunidadTareaActividad ata : agt.getOportunidadTareaActividades()) {
                        JSONObject jObjectActividad = new JSONObject();
                        //jObjectActividad.put("IdAgenda", ata.getIdAgenda());
                        if (ata.getResultado() == null || ata.getResultado().equals("null"))
                            jObjectActividad.put("Resultado", " ");
                        else if (ata.getResultado().equals("true"))
                            jObjectActividad.put("Resultado", "Si");
                        else if (ata.getResultado().equals("false"))
                            jObjectActividad.put("Resultado", "No");
                        else
                            jObjectActividad.put("Resultado", ata.getResultado());

                        jObjectActividad.put("IdTareaActividad", ata.getIdTareaActividad());
                        if (ata.getIdsResultado() != null)
                            jObjectActividad.put("ResultadoCodigo", ata.getIdsResultado());
                        jObjectActividad.put("IdTarea", ata.getIdTarea());

                        jArrayActividades.put(jObjectActividad);
                    }

                    jObjectTarea.put("OportunidadTareaActividads", jArrayActividades);

                    jArrayTareas.put(jObjectTarea);
                }

                jObject.put("OportunidadTareas", jArrayTareas);

                JSONArray jArrayContactos = new JSONArray();
                for (OportunidadContacto agt : oportunidadUpload.getOportunidadContactos()) {
                    JSONObject jObjectContacto = new JSONObject();
                    jObjectContacto.put("IdInterno", agt.getID());
                    jObjectContacto.put("Cargo", agt.getCargo());
                    jObjectContacto.put("CorreoElectronico", agt.getEmail());
                    jObjectContacto.put("Telefono2", agt.getFijo());
                    jObjectContacto.put("Telefono1", agt.getMovil());
                    jObjectContacto.put("Nombre", agt.getNombre());
                    jObjectContacto.put("EsPrincipal", agt.isEsPrincipal());

                    jArrayContactos.put(jObjectContacto);
                }
                jObject.put("OportunidadContactos", jArrayContactos);

                JSONArray jArrayResponsables = new JSONArray();
                for (OportunidadResponsable agt : oportunidadUpload.getOportunidadResponsables()) {
                    JSONObject jObjectResponsable = new JSONObject();
                    jObjectResponsable.put("IdInterno", agt.getIdAgente());

                    jArrayResponsables.put(jObjectResponsable);
                }
                jObject.put("OportunidadResponsables", jArrayResponsables);
                jArray.put(jObject);
            } catch (Exception ex) {

            }
        }

        webService.addParameter("oportunidades", jArray);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                for (int i = 0; i < oportunidades.size(); i++) {
                    rp3.marketforce.models.oportunidad.Oportunidad oportunidadUpload = oportunidades.get(i);
                    oportunidadUpload.setPendiente(false);
                    rp3.marketforce.models.oportunidad.Oportunidad.update(db, oportunidadUpload);
                }
            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return SyncAdapter.SYNC_EVENT_ERROR;
            }

        } finally {
            webService.close();
        }
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSync(DataBase db){
        WebService webService = new WebService("MartketForce","GetOportunidades");
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

            rp3.marketforce.models.oportunidad.Oportunidad.deleteAll(db, Contract.Oportunidad.TABLE_NAME);
            OportunidadTarea.deleteAll(db, Contract.OportunidadTarea.TABLE_NAME);
            OportunidadTareaActividad.deleteAll(db, Contract.OportunidadTareaActividad.TABLE_NAME);
            OportunidadResponsable.deleteAll(db, Contract.OportunidadResponsable.TABLE_NAME);
            OportunidadContacto.deleteAll(db, Contract.OportunidadContacto.TABLE_NAME);
            OportunidadFoto.deleteAll(db, Contract.OportunidadFoto.TABLE_NAME);

            for(int i=0; i < types.length(); i++){

                try {
                    JSONObject type = types.getJSONObject(i);
                    rp3.marketforce.models.oportunidad.Oportunidad opt = new rp3.marketforce.models.oportunidad.Oportunidad();

                    opt.setIdEtapa(type.getInt("IdEtapa"));
                    opt.setIdOportunidad(type.getInt("IdOportunidad"));
                    opt.setDescripcion(type.getString("Descripcion"));
                    opt.setDireccion(type.getString("Direccion"));
                    opt.setReferencia(type.getString("Referencia"));
                    opt.setImporte(type.getDouble("Importe"));
                    opt.setProbabilidad(type.getInt("Probabilidad"));
                    opt.setCalificacion(type.getInt("Calificacion"));
                    opt.setObservacion(type.getString("Observacion"));
                    opt.setFechaCreacion(Convert.getDateFromDotNetTicks(type.getLong("FechaCreacionTicks")));
                    opt.setFechaUltimaGestion(Convert.getDateFromDotNetTicks(type.getLong("FechaUltimaGestionTicks")));
                    opt.setLatitud(type.getDouble("Latitud"));
                    opt.setLongitud(type.getDouble("Longitud"));
                    opt.setEstado(type.getString("Estado"));

                    rp3.marketforce.models.oportunidad.Oportunidad.insert(db, opt);


                    JSONArray strs = type.getJSONArray("OportunidadContactos");

                    for (int j = 0; j < strs.length(); j++) {
                        JSONObject str = strs.getJSONObject(j);
                        OportunidadContacto opCont = new OportunidadContacto();

                        opCont.setIdOportunidad(opt.getIdOportunidad());
                        opCont.setIdOportunidadContacto(str.getInt("IdOportunidadContacto"));
                        opCont.setNombre(str.getString("Nombre"));
                        opCont.setCargo(str.getString("Cargo"));

                        OportunidadContacto.insert(db, opCont);
                    }

                    strs = type.getJSONArray("OportunidadResponsables");

                    for (int j = 0; j < strs.length(); j++) {
                        JSONObject str = strs.getJSONObject(j);
                        OportunidadResponsable opResp = new OportunidadResponsable();

                        opResp.setIdOportunidad(opt.getIdOportunidad());
                        opResp.setIdAgente(str.getInt("IdAgente"));

                        OportunidadResponsable.insert(db, opResp);
                    }

                    strs = type.getJSONArray("OportunidadTareas");

                    for (int j = 0; j < strs.length(); j++) {
                        JSONObject str = strs.getJSONObject(j);
                        OportunidadTarea tarea = new OportunidadTarea();

                        tarea.setIdOportunidad(opt.getIdOportunidad());
                        tarea.setIdTarea(str.getInt("IdTarea"));
                        tarea.setOrden(str.getInt("Orden"));
                        tarea.setIdEtapa(str.getInt("IdEtapa"));
                        tarea.setObservacion(str.getString("Observacion"));
                        tarea.setEstado(str.getString("Estado"));

                        OportunidadTarea.insert(db, tarea);

                        if(!str.isNull("OportunidadTareaActividads")) {
                            JSONArray stAct = str.getJSONArray("OportunidadTareaActividads");

                            for (int r = 0; r < stAct.length(); r++) {
                                JSONObject strAct = stAct.getJSONObject(r);
                                OportunidadTareaActividad tareaAct = new OportunidadTareaActividad();

                                tareaAct.setIdOportunidad(opt.getIdOportunidad());
                                tareaAct.setIdTarea(tarea.getIdTarea());
                                tareaAct.setIdTareaActividad(strAct.getInt("IdTareaActividad"));
                                tareaAct.setIdEtapa(tarea.getIdEtapa());
                                tareaAct.setResultado(strAct.getString("Resultado"));
                                tareaAct.setIdsResultado(strAct.getString("ResultadoCodigo"));

                                OportunidadTareaActividad.insert(db, tareaAct);
                            }
                        }
                    }


                } catch (JSONException e) {
                    Log.e("Error", e.toString());
                    return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
                }
            }
        }finally{
            webService.close();
        }

        return rp3.marketforce.sync.SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
