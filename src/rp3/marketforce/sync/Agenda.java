package rp3.marketforce.sync;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.utils.Utils;
import rp3.util.Convert;

public class Agenda {

    public static int executeSync(DataBase db, int idAgenda) {
        WebService webService = new WebService("MartketForce", "UpdateAgendaFull");
        webService.setTimeOut(20000);

        rp3.marketforce.models.Agenda agendaUpload = rp3.marketforce.models.Agenda.getAgendaUpload(db, idAgenda);

        JSONArray jArray = new JSONArray();
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("IdAgenda", agendaUpload.getIdAgenda());
            jObject.put("IdRuta", PreferenceManager.getInt(Contants.KEY_IDRUTA));
            jObject.put("IdClienteContacto", agendaUpload.getIdContacto());
            jObject.put("EstadoAgenda", agendaUpload.getEstadoAgenda());
            jObject.put("Observacion", agendaUpload.getObservaciones());
            jObject.put("FechaInicioTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaInicio()));
            jObject.put("FechaFinTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaFin()));
            jObject.put("FechaInicioGestionTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaInicioReal()));
            jObject.put("FechaFinGestionTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaFinReal()));
            jObject.put("Latitud", agendaUpload.getLatitud());
            jObject.put("Longitud", agendaUpload.getLongitud());
            jObject.put("Duracion", agendaUpload.getDuracion());
            jObject.put("TiempoViaje", agendaUpload.getTiempoViaje());
            jObject.put("DistanciaUbicacion", agendaUpload.getDistancia());
            jObject.put("MotivoReprogramacion", agendaUpload.getIdMotivoReprogramacion());
            jObject.put("MotivoReprogramacionTabla", Contants.GENERAL_TABLE_MOTIVOS_REPROGRAMACION);

            JSONArray jArrayTareas = new JSONArray();
            for (AgendaTarea agt : agendaUpload.getAgendaTareas()) {
                JSONObject jObjectTarea = new JSONObject();
                //jObjectTarea.put("IdAgenda", agt.getIdAgenda());
                //jObjectTarea.put("IdRuta", agt.getIdRuta());
                jObjectTarea.put("IdTarea", agt.getIdTarea());
                jObjectTarea.put("EstadoTarea", agt.getEstadoTarea());
                //jObjectTarea.put("TipoTarea", agt.getTipoTarea());

                JSONArray jArrayActividades = new JSONArray();
                for (AgendaTareaActividades ata : agt.getActividades()) {
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

                    //jObjectActividad.put("IdRuta", ata.getIdRuta());
                    jObjectActividad.put("IdTareaActividad", ata.getIdTareaActividad());
                    if (ata.getIdsResultado() != null)
                        jObjectActividad.put("ResultadoCodigo", ata.getIdsResultado());
                    //jObjectActividad.put("IdTareaActividadPadre", ata.getIdTareaActividadPadre());
                    jObjectActividad.put("IdTarea", ata.getIdTarea());
                    jObjectActividad.put("IdTareaOpcion", ata.getIdTareaOpcion());
                    //jObjectActividad.put("Tipo", ata.getTipo());
                    //jObjectActividad.put("IdTipoActividad", ata.getIdTipoActividad());

                    jArrayActividades.put(jObjectActividad);
                }

                jObjectTarea.put("AgendaTareaActividades", jArrayActividades);

                jArrayTareas.put(jObjectTarea);
            }

            jObject.put("AgendaTareas", jArrayTareas);
            jArray.put(jObject);
        } catch (Exception ex) {

        }

        webService.addParameter("agendas", jArray);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                String error = webService.getStringResponse();
                agendaUpload.setEnviado(true);
                rp3.marketforce.models.Agenda.update(db, agendaUpload);
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

        if ((agendaUpload.getFoto1Int() != null && agendaUpload.getFoto1Int().length() > 0) && agendaUpload.getFoto1Ext() == null) {
            webService = new WebService("MartketForce", "SetMediaAgenda");

            jObject = new JSONObject();
            try {
                jObject.put("IdAgenda", agendaUpload.getIdAgenda());
                jObject.put("IdRuta", PreferenceManager.getInt(Contants.KEY_IDRUTA));
                jObject.put("IdMedia", 1);
                jObject.put("Nombre", agendaUpload.getIdAgenda() + "_Foto1.jpg");
                jObject.put("Contenido", Utils.BitmapToBase64(agendaUpload.getFoto1Int()));
            } catch (Exception ex) {

            }

            webService.addParameter("media", jObject);

            try {
                webService.addCurrentAuthToken();

                try {
                    webService.invokeWebService();
                    agendaUpload.setFoto1Ext("1");
                    rp3.marketforce.models.Agenda.update(db, agendaUpload);
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
        }

        if ((agendaUpload.getFoto2Int() != null && agendaUpload.getFoto2Int().length() > 0) && agendaUpload.getFoto2Ext() == null) {
            webService = new WebService("MartketForce", "SetMediaAgenda");

            jObject = new JSONObject();
            try {
                jObject.put("IdAgenda", agendaUpload.getIdAgenda());
                jObject.put("IdRuta", PreferenceManager.getInt(Contants.KEY_IDRUTA));
                jObject.put("IdMedia", 2);
                jObject.put("Nombre", agendaUpload.getIdAgenda() + "_Foto2.jpg");
                jObject.put("Contenido", Utils.BitmapToBase64(agendaUpload.getFoto2Int()));
            } catch (Exception ex) {

            }

            webService.addParameter("media", jObject);

            try {
                webService.addCurrentAuthToken();
                try {
                    webService.invokeWebService();
                    agendaUpload.setFoto2Ext("1");
                    rp3.marketforce.models.Agenda.update(db, agendaUpload);
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
        }

        if ((agendaUpload.getFoto3Int() != null && agendaUpload.getFoto3Int().length() > 0) && agendaUpload.getFoto3Ext() == null) {
            webService = new WebService("MartketForce", "SetMediaAgenda");

            jObject = new JSONObject();
            try {
                jObject.put("IdAgenda", agendaUpload.getIdAgenda());
                jObject.put("IdRuta", PreferenceManager.getInt(Contants.KEY_IDRUTA));
                jObject.put("IdMedia", 3);
                jObject.put("Nombre", agendaUpload.getIdAgenda() + "_Foto3.jpg");
                jObject.put("Contenido", Utils.BitmapToBase64(agendaUpload.getFoto3Int()));
            } catch (Exception ex) {

            }

            webService.addParameter("media", jObject);

            try {
                webService.addCurrentAuthToken();

                try {
                    webService.invokeWebService();
                    agendaUpload.setFoto3Ext("1");
                    rp3.marketforce.models.Agenda.update(db, agendaUpload);
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
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncGeolocation(DataBase db, int idAgenda, double latitud, double longitud) {
        WebService webService = new WebService("MartketForce", "UpdateAgendaGeolocation");
        webService.setTimeOut(20000);

        rp3.marketforce.models.Agenda agendaUpload = rp3.marketforce.models.Agenda.getAgenda(db, idAgenda);

        JSONArray jArray = new JSONArray();
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("IdAgenda", agendaUpload.getIdAgenda());
            jObject.put("IdRuta", PreferenceManager.getInt(Contants.KEY_IDRUTA));
            jObject.put("Latitud", latitud);
            jObject.put("Longitud", longitud);
            jObject.put("DistanciaUbicacion", agendaUpload.getDistancia());
            jArray.put(jObject);
        } catch (Exception ex) {

        }

        webService.addParameter("agendas", jArray);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                String error = webService.getStringResponse();
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

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncReschedule(DataBase db, int idAgenda) {
        WebService webService = new WebService("MartketForce", "ReprogramarAgenda");

        rp3.marketforce.models.Agenda agendaUpload = rp3.marketforce.models.Agenda.getAgendaUpload(db, idAgenda);

        JSONArray jArray = new JSONArray();
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("IdAgenda", agendaUpload.getIdAgenda());
            jObject.put("IdRuta", PreferenceManager.getInt(Contants.KEY_IDRUTA));
            jObject.put("EstadoAgenda", agendaUpload.getEstadoAgenda());
            jObject.put("FechaInicioTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaInicio()));
            jObject.put("FechaFinTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaFin()));
            jObject.put("Duracion", agendaUpload.getDuracion());
            jObject.put("TiempoViaje", agendaUpload.getTiempoViaje());
            jObject.put("MotivoReprogramacion", agendaUpload.getIdMotivoReprogramacion());
            jObject.put("MotivoReprogramacionTabla", Contants.GENERAL_TABLE_MOTIVOS_REPROGRAMACION);

            jArray.put(jObject);
        } catch (Exception ex) {

        }

        webService.addParameter("agendas", jArray);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                String error = webService.getStringResponse();
                agendaUpload.setEnviado(true);
                rp3.marketforce.models.Agenda.update(db, agendaUpload);
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

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncNoVisita(DataBase db, int idAgenda) {
        WebService webService = new WebService("MartketForce", "MotivoNoVisitaAgenda");

        rp3.marketforce.models.Agenda agendaUpload = rp3.marketforce.models.Agenda.getAgendaUpload(db, idAgenda);

        JSONArray jArray = new JSONArray();
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("IdAgenda", agendaUpload.getIdAgenda());
            jObject.put("IdRuta", PreferenceManager.getInt(Contants.KEY_IDRUTA));
            jObject.put("Observacion", agendaUpload.getObservaciones());
            jObject.put("MotivoNoGestion", agendaUpload.getIdMotivoNoVisita());
            jObject.put("DistanciaUbicacion", agendaUpload.getDistancia());

            jArray.put(jObject);
        } catch (Exception ex) {

        }

        webService.addParameter("agendasNoGestion", jArray);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                String error = webService.getStringResponse();
                agendaUpload.setEnviado(true);
                rp3.marketforce.models.Agenda.update(db, agendaUpload);
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

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncInsert(DataBase db, long id_local) {
        WebService webService = new WebService("MartketForce", "InsertarAgenda");

        rp3.marketforce.models.Agenda agenda = rp3.marketforce.models.Agenda.getAgenda(db, id_local);

        JSONObject jObject = new JSONObject();
        try {
            jObject.put("FechaInicioTicks", Convert.getDotNetTicksFromDate(agenda.getFechaInicio()));
            jObject.put("FechaFinTicks", Convert.getDotNetTicksFromDate(agenda.getFechaFin()));
            jObject.put("FechaInicioOriginalTicks", Convert.getDotNetTicksFromDate(agenda.getFechaInicio()));
            jObject.put("FechaFinOriginalTicks", Convert.getDotNetTicksFromDate(agenda.getFechaFin()));
            jObject.put("IdCliente", agenda.getIdCliente());
            jObject.put("IdClienteDireccion", agenda.getIdClienteDireccion());
            jObject.put("Ciudad", agenda.getCiudad());
            jObject.put("NombresCompletos", agenda.getNombreCompleto());
            jObject.put("Direccion", agenda.getDireccion());
            if (agenda.getIdContacto() != 0)
                jObject.put("IdClienteContacto", agenda.getIdContacto());
            jObject.put("Duracion", agenda.getDuracion());
            jObject.put("TiempoViaje", agenda.getTiempoViaje());
            jObject.put("DistanciaUbicacion", agenda.getDistancia());
            jObject.put("MotivoReprogramacion", agenda.getIdMotivoReprogramacion());
            jObject.put("MotivoReprogramacionTabla", Contants.GENERAL_TABLE_MOTIVOS_REPROGRAMACION);

            JSONArray jArrayTareas = new JSONArray();
            for (AgendaTarea agt : agenda.getAgendaTareas()) {
                JSONObject jObjectTarea = new JSONObject();
                jObjectTarea.put("IdTarea", agt.getIdTarea());
                jArrayTareas.put(jObjectTarea);
            }

            jObject.put("AgendaTareas", jArrayTareas);
        } catch (Exception ex) {
            if(agenda != null) {
                agenda.setEnviado(false);
                rp3.marketforce.models.Agenda.update(db, agenda);
            }
        }

        webService.addParameter("agenda", jObject);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                int id = webService.getIntegerResponse();

                agenda.setIdAgenda(id);
                agenda.setIdRuta(PreferenceManager.getInt(Contants.KEY_IDRUTA));
                agenda.setEnviado(true);

                rp3.marketforce.models.Agenda.update(db, agenda);

                for (int j = 0; j < agenda.getAgendaTareas().size(); j++) {

                    rp3.marketforce.models.AgendaTarea agendaTarea = agenda.getAgendaTareas().get(j);

                    agendaTarea.setIdAgenda(id);
                    agendaTarea.setEstadoTarea("P");
                    agendaTarea.setIdRuta(PreferenceManager.getInt(Contants.KEY_IDRUTA));

                    rp3.marketforce.models.AgendaTarea.update(db, agendaTarea);
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

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncInserts(DataBase db) {
        WebService webService = new WebService("MartketForce", "UpdateAgendaFull");

        List<rp3.marketforce.models.Agenda> agendas = rp3.marketforce.models.Agenda.getAgendaInserts(db);
        List<rp3.marketforce.models.Agenda> agendasConId = new ArrayList<rp3.marketforce.models.Agenda>();
        if (agendas.size() == 0)
            return SyncAdapter.SYNC_EVENT_SUCCESS;

        JSONArray jArray = new JSONArray();
        for (int i = 0; i < agendas.size(); i++) {
            rp3.marketforce.models.Agenda agendaUpload = agendas.get(i);
            JSONObject jObject = new JSONObject();
            try {
                jObject.put("IdAgenda", agendaUpload.getIdAgenda());
                jObject.put("IdInterno", agendaUpload.getID());
                jObject.put("IdRuta", agendaUpload.getIdRuta());
                jObject.put("IdCliente", agendaUpload.getIdCliente());
                jObject.put("IdClienteContacto", agendaUpload.getIdContacto());
                jObject.put("IdClienteDireccion", agendaUpload.getIdClienteDireccion());
                jObject.put("EstadoAgenda", agendaUpload.getEstadoAgenda());
                jObject.put("Observacion", agendaUpload.getObservaciones());
                jObject.put("FechaInicioTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaInicio()));
                jObject.put("FechaFinTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaFin()));
                jObject.put("FechaInicioGestionTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaInicioReal()));
                jObject.put("FechaFinGestionTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaFinReal()));
                jObject.put("Latitud", agendaUpload.getLatitud());
                jObject.put("Longitud", agendaUpload.getLongitud());
                jObject.put("MotivoNoGestion", agendaUpload.getIdMotivoNoVisita());
                jObject.put("Duracion", agendaUpload.getDuracion());
                jObject.put("TiempoViaje", agendaUpload.getTiempoViaje());
                jObject.put("DistanciaUbicacion", agendaUpload.getDistancia());
                jObject.put("MotivoReprogramacion", agendaUpload.getIdMotivoReprogramacion());
                jObject.put("MotivoReprogramacionTabla", Contants.GENERAL_TABLE_MOTIVOS_REPROGRAMACION);
                jObject.put("FechaInicioOriginalTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaInicio()));
                jObject.put("FechaFinOriginalTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaFin()));

                JSONArray jArrayTareas = new JSONArray();
                for (AgendaTarea agt : agendaUpload.getAgendaTareas()) {
                    JSONObject jObjectTarea = new JSONObject();
                    //jObjectTarea.put("IdAgenda", agt.getIdAgenda());
                    //jObjectTarea.put("IdRuta", agt.getIdRuta());
                    jObjectTarea.put("IdTarea", agt.getIdTarea());
                    jObjectTarea.put("EstadoTarea", agt.getEstadoTarea());
                    //jObjectTarea.put("TipoTarea", agt.getTipoTarea());

                    JSONArray jArrayActividades = new JSONArray();
                    for (AgendaTareaActividades ata : agt.getActividades()) {
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

                        //jObjectActividad.put("IdRuta", ata.getIdRuta());
                        jObjectActividad.put("IdTareaActividad", ata.getIdTareaActividad());
                        if (ata.getIdsResultado() != null)
                            jObjectActividad.put("ResultadoCodigo", ata.getIdsResultado());
                        //jObjectActividad.put("IdTareaActividadPadre", ata.getIdTareaActividadPadre());
                        jObjectActividad.put("IdTarea", ata.getIdTarea());
                        jObjectActividad.put("IdTareaOpcion", ata.getIdTareaOpcion());
                        //jObjectActividad.put("Tipo", ata.getTipo());
                        //jObjectActividad.put("IdTipoActividad", ata.getIdTipoActividad());

                        jArrayActividades.put(jObjectActividad);
                    }

                    jObjectTarea.put("AgendaTareaActividades", jArrayActividades);

                    jArrayTareas.put(jObjectTarea);
                }

                jObject.put("AgendaTareas", jArrayTareas);
                jArray.put(jObject);
            } catch (Exception ex) {

            }
        }

        webService.addParameter("agendas", jArray);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONObject codigos = webService.getJSONObjectResponse();
                JSONArray resp = codigos.getJSONArray("Result");
                for (int i = 0; i < resp.length(); i++) {
                    JSONObject jObjResp = resp.getJSONObject(i);
                    for (rp3.marketforce.models.Agenda agdResp : agendas) {
                        if (agdResp.getID() == jObjResp.getInt("IdInterno")) {
                            agdResp.setIdAgenda(jObjResp.getInt("IdAgendaServer"));
                            agdResp.setIdRuta(jObjResp.getInt("IdRutaServer"));
                            agdResp.setEnviado(true);
                            rp3.marketforce.models.Agenda.update(db, agdResp);
                            rp3.marketforce.models.Agenda.delete(db, agdResp);
                            agendasConId.add(agdResp);
                            for (AgendaTarea agdTarea : agdResp.getAgendaTareas()) {
                                agdTarea.setIdAgenda(jObjResp.getInt("IdAgendaServer"));
                                agdTarea.setIdRuta(jObjResp.getInt("IdRutaServer"));
                                AgendaTarea.update(db, agdTarea);
                                for(AgendaTareaActividades agdActividad : agdTarea.getActividades())
                                {
                                    agdActividad.setIdAgenda(jObjResp.getInt("IdAgendaServer"));
                                    agdActividad.setIdRuta(jObjResp.getInt("IdRutaServer"));
                                    AgendaTareaActividades.update(db, agdActividad);
                                }
                            }
                        }
                    }
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

        JSONObject jObject = new JSONObject();
        for (int i = 0; i < agendasConId.size(); i++) {
            rp3.marketforce.models.Agenda agendaUpload = agendasConId.get(i);
            if (agendaUpload.getFoto1Int() != null && agendaUpload.getFoto1Int().length() > 0) {
                webService = new WebService("MartketForce", "SetMediaAgenda");

                jObject = new JSONObject();
                try {
                    jObject.put("IdAgenda", agendaUpload.getIdAgenda());
                    jObject.put("IdRuta", agendaUpload.getIdRuta());
                    jObject.put("IdMedia", 1);
                    jObject.put("Nombre", agendaUpload.getIdAgenda() + "_Foto1.jpg");
                    jObject.put("Contenido", Utils.BitmapToBase64(agendaUpload.getFoto1Int()));
                } catch (Exception ex) {

                }

                webService.addParameter("media", jObject);

                try {
                    webService.addCurrentAuthToken();
                    agendaUpload.setFoto1Ext("1");
                    rp3.marketforce.models.Agenda.update(db, agendaUpload);
                    try {
                        webService.invokeWebService();
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
            }

            if (agendaUpload.getFoto2Int() != null && agendaUpload.getFoto2Int().length() > 0) {
                webService = new WebService("MartketForce", "SetMediaAgenda");

                jObject = new JSONObject();
                try {
                    jObject.put("IdAgenda", agendaUpload.getIdAgenda());
                    jObject.put("IdRuta", agendaUpload.getIdRuta());
                    jObject.put("IdMedia", 2);
                    jObject.put("Nombre", agendaUpload.getIdAgenda() + "_Foto2.jpg");
                    jObject.put("Contenido", Utils.BitmapToBase64(agendaUpload.getFoto2Int()));
                } catch (Exception ex) {

                }

                webService.addParameter("media", jObject);

                try {
                    webService.addCurrentAuthToken();
                    agendaUpload.setFoto2Ext("1");
                    rp3.marketforce.models.Agenda.update(db, agendaUpload);
                    try {
                        webService.invokeWebService();
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
            }

            if (agendaUpload.getFoto3Int() != null && agendaUpload.getFoto3Int().length() > 0) {
                webService = new WebService("MartketForce", "SetMediaAgenda");

                jObject = new JSONObject();
                try {
                    jObject.put("IdAgenda", agendaUpload.getIdAgenda());
                    jObject.put("IdRuta", agendaUpload.getIdRuta());
                    jObject.put("IdMedia", 3);
                    jObject.put("Nombre", agendaUpload.getIdAgenda() + "_Foto3.jpg");
                    jObject.put("Contenido", Utils.BitmapToBase64(agendaUpload.getFoto3Int()));
                } catch (Exception ex) {

                }

                webService.addParameter("media", jObject);

                try {
                    webService.addCurrentAuthToken();
                    agendaUpload.setFoto3Ext("1");
                    rp3.marketforce.models.Agenda.update(db, agendaUpload);
                    try {
                        webService.invokeWebService();
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
            }
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncPendientes(DataBase db) {
        WebService webService = new WebService("MartketForce", "UpdateAgendaFull");

        List<rp3.marketforce.models.Agenda> agendas = rp3.marketforce.models.Agenda.getAgendaPendientes(db);
        List<rp3.marketforce.models.Agenda> agendasConId = new ArrayList<rp3.marketforce.models.Agenda>();
        if (agendas.size() == 0)
            return SyncAdapter.SYNC_EVENT_SUCCESS;

        JSONArray jArray = new JSONArray();
        for (int i = 0; i < agendas.size(); i++) {
            rp3.marketforce.models.Agenda agendaUpload = agendas.get(i);
            JSONObject jObject = new JSONObject();
            try {
                jObject.put("IdAgenda", agendaUpload.getIdAgenda());
                jObject.put("IdInterno", agendaUpload.getID());
                jObject.put("IdRuta", agendaUpload.getIdRuta());
                jObject.put("IdCliente", agendaUpload.getIdCliente());
                jObject.put("IdClienteContacto", agendaUpload.getIdContacto());
                jObject.put("IdClienteDireccion", agendaUpload.getIdClienteDireccion());
                jObject.put("EstadoAgenda", agendaUpload.getEstadoAgenda());
                jObject.put("Observacion", agendaUpload.getObservaciones());
                jObject.put("FechaInicioTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaInicio()));
                jObject.put("FechaFinTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaFin()));
                jObject.put("FechaInicioGestionTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaInicioReal()));
                jObject.put("FechaFinGestionTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaFinReal()));
                jObject.put("Latitud", agendaUpload.getLatitud());
                jObject.put("Longitud", agendaUpload.getLongitud());
                jObject.put("MotivoNoGestion", agendaUpload.getIdMotivoNoVisita());
                jObject.put("Duracion", agendaUpload.getDuracion());
                jObject.put("TiempoViaje", agendaUpload.getTiempoViaje());
                jObject.put("DistanciaUbicacion", agendaUpload.getDistancia());
                jObject.put("MotivoReprogramacion", agendaUpload.getIdMotivoReprogramacion());
                jObject.put("MotivoReprogramacionTabla", Contants.GENERAL_TABLE_MOTIVOS_REPROGRAMACION);
                jObject.put("FechaInicioOriginalTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaInicio()));
                jObject.put("FechaFinOriginalTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaFin()));
                agendasConId.add(agendaUpload);

                JSONArray jArrayTareas = new JSONArray();
                for (AgendaTarea agt : agendaUpload.getAgendaTareas()) {
                    JSONObject jObjectTarea = new JSONObject();
                    //jObjectTarea.put("IdAgenda", agt.getIdAgenda());
                    //jObjectTarea.put("IdRuta", agt.getIdRuta());
                    jObjectTarea.put("IdTarea", agt.getIdTarea());
                    jObjectTarea.put("EstadoTarea", agt.getEstadoTarea());
                    //jObjectTarea.put("TipoTarea", agt.getTipoTarea());

                    JSONArray jArrayActividades = new JSONArray();
                    for (AgendaTareaActividades ata : agt.getActividades()) {
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

                        //jObjectActividad.put("IdRuta", ata.getIdRuta());
                        jObjectActividad.put("IdTareaActividad", ata.getIdTareaActividad());
                        if (ata.getIdsResultado() != null)
                            jObjectActividad.put("ResultadoCodigo", ata.getIdsResultado());
                        //jObjectActividad.put("IdTareaActividadPadre", ata.getIdTareaActividadPadre());
                        jObjectActividad.put("IdTarea", ata.getIdTarea());
                        jObjectActividad.put("IdTareaOpcion", ata.getIdTareaOpcion());
                        //jObjectActividad.put("Tipo", ata.getTipo());
                        //jObjectActividad.put("IdTipoActividad", ata.getIdTipoActividad());

                        jArrayActividades.put(jObjectActividad);
                    }

                    jObjectTarea.put("AgendaTareaActividades", jArrayActividades);

                    jArrayTareas.put(jObjectTarea);
                }

                jObject.put("AgendaTareas", jArrayTareas);
                jArray.put(jObject);
            } catch (Exception ex) {

            }
        }

        webService.addParameter("agendas", jArray);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                for (rp3.marketforce.models.Agenda agd : agendas) {
                    agd.setEnviado(true);
                    rp3.marketforce.models.Agenda.update(db, agd);
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

        JSONObject jObject = new JSONObject();
        for (int i = 0; i < agendasConId.size(); i++) {
            rp3.marketforce.models.Agenda agendaUpload = agendasConId.get(i);
            if ((agendaUpload.getFoto1Int() != null && agendaUpload.getFoto1Int().length() > 0) && agendaUpload.getFoto1Ext() == null) {
                webService = new WebService("MartketForce", "SetMediaAgenda");

                jObject = new JSONObject();
                try {
                    jObject.put("IdAgenda", agendaUpload.getIdAgenda());
                    jObject.put("IdRuta", agendaUpload.getIdRuta());
                    jObject.put("IdMedia", 1);
                    jObject.put("Nombre", agendaUpload.getIdAgenda() + "_Foto1.jpg");
                    jObject.put("Contenido", Utils.BitmapToBase64(agendaUpload.getFoto1Int()));
                } catch (Exception ex) {

                }

                webService.addParameter("media", jObject);

                try {
                    webService.addCurrentAuthToken();


                    try {
                        webService.invokeWebService();
                        agendaUpload.setEnviado(true);
                        agendaUpload.setFoto1Ext("1");
                        rp3.marketforce.models.Agenda.update(db, agendaUpload);
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
            }

            if ((agendaUpload.getFoto2Int() != null && agendaUpload.getFoto2Int().length() > 0) && agendaUpload.getFoto2Ext() == null) {
                webService = new WebService("MartketForce", "SetMediaAgenda");

                jObject = new JSONObject();
                try {
                    jObject.put("IdAgenda", agendaUpload.getIdAgenda());
                    jObject.put("IdRuta", agendaUpload.getIdRuta());
                    jObject.put("IdMedia", 2);
                    jObject.put("Nombre", agendaUpload.getIdAgenda() + "_Foto2.jpg");
                    jObject.put("Contenido", Utils.BitmapToBase64(agendaUpload.getFoto2Int()));
                } catch (Exception ex) {

                }

                webService.addParameter("media", jObject);

                try {
                    webService.addCurrentAuthToken();


                    try {
                        webService.invokeWebService();
                        agendaUpload.setEnviado(true);
                        agendaUpload.setFoto2Ext("1");
                        rp3.marketforce.models.Agenda.update(db, agendaUpload);
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
            }

            if ((agendaUpload.getFoto3Int() != null && agendaUpload.getFoto3Int().length() > 0) && agendaUpload.getFoto3Ext() == null) {
                webService = new WebService("MartketForce", "SetMediaAgenda");

                jObject = new JSONObject();
                try {
                    jObject.put("IdAgenda", agendaUpload.getIdAgenda());
                    jObject.put("IdRuta", agendaUpload.getIdRuta());
                    jObject.put("IdMedia", 3);
                    jObject.put("Nombre", agendaUpload.getIdAgenda() + "_Foto3.jpg");
                    jObject.put("Contenido", Utils.BitmapToBase64(agendaUpload.getFoto3Int()));
                } catch (Exception ex) {

                }

                webService.addParameter("media", jObject);

                try {
                    webService.addCurrentAuthToken();


                    try {
                        webService.invokeWebService();
                        agendaUpload.setEnviado(true);
                        agendaUpload.setFoto3Ext("1");
                        rp3.marketforce.models.Agenda.update(db, agendaUpload);
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
            }
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}

