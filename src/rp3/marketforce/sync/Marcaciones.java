package rp3.marketforce.sync;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.*;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.DiaLaboral;
import rp3.marketforce.models.marcacion.Justificacion;
import rp3.marketforce.models.marcacion.Marcacion;
import rp3.marketforce.models.marcacion.Permiso;
import rp3.util.Convert;

/**
 * Created by magno_000 on 08/06/2015.
 */
public class Marcaciones {
    public static int executeSync(DataBase db) {
        List<Marcacion> marcacionUploads = rp3.marketforce.models.marcacion.Marcacion.getMarcacionesPendientes(db);

        for(Marcacion marc : marcacionUploads) {

            WebService webService = new WebService("MartketForce", "InsertMarcacion");
            webService.setTimeOut(20000);
            Calendar cal = Calendar.getInstance();
            cal.setTime(marc.getFecha());
            DiaLaboral dia = DiaLaboral.getDia(db, cal.get(Calendar.DAY_OF_WEEK) - 1);
            DateFormat format = new SimpleDateFormat("HH:mm");
            JSONArray jArray = new JSONArray();
            JSONObject jObject = new JSONObject();
            try {
                jObject.put("Tipo", marc.getTipo());
                jObject.put("TipoTabla", Contants.GENERAL_TABLE_TIPO_MARCACION);
                Calendar fecha = Calendar.getInstance();
                fecha.setTime(marc.getFecha());
                fecha.set(Calendar.HOUR_OF_DAY, 0);
                fecha.set(Calendar.MINUTE, 0);
                fecha.set(Calendar.SECOND, 0);
                fecha.set(Calendar.MILLISECOND, 0);
                jObject.put("FechaTicks", Convert.getDotNetTicksFromDate(fecha.getTime()));
                jObject.put("HoraInicioTicks", Convert.getDotNetTicksFromDate(marc.getFecha()));
                jObject.put("HoraFinTicks", Convert.getDotNetTicksFromDate(marc.getFecha()));
                Calendar hoy = Calendar.getInstance();
                if(marc.getTipo().equalsIgnoreCase("J1") || marc.getTipo().equalsIgnoreCase("J2") || dia.getHoraFin2() == null) {
                    hoy.setTime(format.parse(dia.getHoraInicio1().replace("h",":")));
                    hoy.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    jObject.put("HoraInicioPlanTicks", Convert.getDotNetTicksFromDate(hoy.getTime()));
                    hoy.setTime(format.parse(dia.getHoraFin1().replace("h",":")));
                    hoy.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    jObject.put("HoraFinPlanTicks", Convert.getDotNetTicksFromDate(hoy.getTime()));
                }
                else
                {
                    hoy.setTime(format.parse(dia.getHoraInicio2().replace("h",":")));
                    hoy.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    jObject.put("HoraInicioPlanTicks", Convert.getDotNetTicksFromDate(hoy.getTime()));
                    hoy.setTime(format.parse(dia.getHoraFin2().replace("h",":")));
                    hoy.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    jObject.put("HoraFinPlanTicks", Convert.getDotNetTicksFromDate(hoy.getTime()));
                }
                jObject.put("MinutosAtraso", marc.getMintutosAtraso());
                jObject.put("EnUbicacion", marc.isEnUbicacion());
                jObject.put("Latitud", marc.getLatitud());
                jObject.put("Longitud", marc.getLongitud());
                jObject.put("LatitudPlan", Double.parseDouble(PreferenceManager.getString(Contants.KEY_LATITUD_PARTIDA)));
                jObject.put("LongitudPlan", Double.parseDouble(PreferenceManager.getString(Contants.KEY_LONGITUD_PARTIDA)));
                if(marc.getPermiso() != null)
                    jObject.put("IdPermiso", marc.getPermiso().getIdPermiso());
                if(marc.getPermiso() != null && marc.getPermiso().getIdPermiso() == 0) {

                    JSONObject jObjectPermiso = new JSONObject();
                    jObjectPermiso.put("Motivo", marc.getPermiso().getTipo());
                    jObjectPermiso.put("MotivoTabla", Contants.GENERAL_TABLE_MOTIVO_PERMISO);
                    jObjectPermiso.put("Tipo", "A");
                    jObjectPermiso.put("TipoTabla", Contants.GENERAL_TABLE_TIPOS_PERMISO);
                    jObjectPermiso.put("FechaFinTicks", Convert.getDotNetTicksFromDate(marc.getPermiso().getFecha()));
                    jObjectPermiso.put("HoraInicioTicks", Convert.getDotNetTicksFromDate(marc.getPermiso().getFecha()));
                    jObjectPermiso.put("HoraFinTicks", Convert.getDotNetTicksFromDate(marc.getPermiso().getFecha()));
                    jObjectPermiso.put("EsPrevio", false);
                    jObjectPermiso.put("Estado", "P");
                    jObjectPermiso.put("EstadoTabla", Contants.GENERAL_TABLE_ESTADO_PERMISO);
                    jObjectPermiso.put("Observacion", marc.getPermiso().getObservacion());
                    jObjectPermiso.put("FechaInicioTicks", Convert.getDotNetTicksFromDate(marc.getPermiso().getFecha()));
                    jObject.put("Permiso", jObjectPermiso);
                }



            } catch (Exception ex) {

            }

            webService.addParameter("marcacion", jObject);

            try {
                webService.addCurrentAuthToken();

                try {
                    webService.invokeWebService();
                    marc.setPendiente(false);
                    rp3.marketforce.models.marcacion.Marcacion.update(db, marc);
                    if(marc.getPermiso() != null)
                    {
                        Permiso.delete(db, marc.getPermiso());
                    }
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
        }
        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

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

    public static int executeSyncMarcacionesHoy(DataBase db){

        Marcacion ultimaMarcacion = Marcacion.getUltimaMarcacion(db);
        Calendar dia_hoy = Calendar.getInstance();
        Calendar dia_marcacion = Calendar.getInstance();
        if(ultimaMarcacion != null)
            dia_marcacion.setTime(ultimaMarcacion.getFecha());
        if(ultimaMarcacion == null || dia_hoy.get(Calendar.DAY_OF_YEAR) != dia_marcacion.get(Calendar.DAY_OF_YEAR)) {
            WebService webService = new WebService("MartketForce", "GetMarcacionesHoy");

            try {
                webService.addCurrentAuthToken();

                try {
                    webService.invokeWebService();
                    JSONArray types = webService.getJSONArrayResponse();
                    for (int i = 0; i < types.length(); i++) {
                        JSONObject type = types.getJSONObject(i);
                        Marcacion marcacion = new Marcacion();
                        marcacion.setPendiente(false);
                        marcacion.setTipo(type.getString("Tipo"));
                        marcacion.setEnUbicacion(type.getBoolean("EnUbicacion"));
                        if(marcacion.getTipo().equalsIgnoreCase("J1") || marcacion.getTipo().equalsIgnoreCase("J3")) {
                            marcacion.setFecha(Convert.getDateFromDotNetTicks(type.getLong("HoraInicioTicks")));
                            marcacion.setHoraFin(Convert.getDateFromDotNetTicks(type.getLong("HoraInicioTicks")));
                            marcacion.setHoraInicio(Convert.getDateFromDotNetTicks(type.getLong("HoraInicioTicks")));
                        }
                        else
                        {
                            marcacion.setFecha(Convert.getDateFromDotNetTicks(type.getLong("HoraFinTicks")));
                            marcacion.setHoraFin(Convert.getDateFromDotNetTicks(type.getLong("HoraFinTicks")));
                            marcacion.setHoraInicio(Convert.getDateFromDotNetTicks(type.getLong("HoraFinTicks")));
                        }
                        marcacion.setLatitud(type.getDouble("Latitud"));
                        marcacion.setLongitud(type.getDouble("Longitud"));
                        marcacion.setMintutosAtraso(type.getInt("MinutosAtraso"));
                        Marcacion.insert(db, marcacion);

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
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncPermiso(DataBase db, long id) {
        Permiso permiso = Permiso.getPermisoById(db, id);


        WebService webService = new WebService("MartketForce", "InsertPermiso");
        webService.setTimeOut(20000);

        JSONObject jObject = new JSONObject();
        try {

            jObject.put("Motivo", permiso.getTipo());
            jObject.put("MotivoTabla", Contants.GENERAL_TABLE_MOTIVO_PERMISO);
            jObject.put("Tipo", "A");
            jObject.put("TipoTabla", Contants.GENERAL_TABLE_TIPOS_PERMISO);
            jObject.put("FechaFinTicks", Convert.getDotNetTicksFromDate(permiso.getFecha()));
            jObject.put("HoraInicioTicks", Convert.getDotNetTicksFromDate(permiso.getFecha()));
            jObject.put("HoraFinTicks", Convert.getDotNetTicksFromDate(permiso.getFecha()));
            jObject.put("EsPrevio", false);
            jObject.put("Estado", "P");
            jObject.put("EstadoTabla", Contants.GENERAL_TABLE_ESTADO_PERMISO);
            jObject.put("Observacion", permiso.getObservacion());
            jObject.put("FechaInicioTicks", Convert.getDotNetTicksFromDate(permiso.getFecha()));

        } catch (Exception ex) {

        }

        webService.addParameter("permiso", jObject);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                permiso.setIdPermiso(webService.getIntegerResponse());
                Permiso.update(db, permiso);
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

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncPermisoPrevio(DataBase db) {
        List<Justificacion> justificacions = Justificacion.getPermisosPendientesPropias(db);


        for(Justificacion permiso : justificacions) {
            WebService webService = new WebService("MartketForce", "InsertPermisoPrevio");
            webService.setTimeOut(20000);

            JSONObject jObject = new JSONObject();
            try {

                jObject.put("Motivo", permiso.getTipo());
                jObject.put("MotivoTabla", Contants.GENERAL_TABLE_MOTIVO_PERMISO);
                if(permiso.isAusencia())
                    jObject.put("Tipo", "F");
                else
                    jObject.put("Tipo", "A");
                jObject.put("TipoTabla", Contants.GENERAL_TABLE_TIPOS_PERMISO);
                jObject.put("FechaFinTicks", Convert.getDotNetTicksFromDate(permiso.getFecha()));
                jObject.put("HoraInicioTicks", Convert.getDotNetTicksFromDate(permiso.getFecha()));
                jObject.put("HoraFinTicks", Convert.getDotNetTicksFromDate(permiso.getFecha()));
                jObject.put("EsPrevio", true);
                jObject.put("Estado", "P");
                jObject.put("EstadoTabla", Contants.GENERAL_TABLE_ESTADO_PERMISO);
                jObject.put("Observacion", permiso.getObservacion());
                jObject.put("FechaInicioTicks", Convert.getDotNetTicksFromDate(permiso.getFecha()));

            } catch (Exception ex) {

            }

            webService.addParameter("permiso", jObject);

            try {
                webService.addCurrentAuthToken();

                try {
                    webService.invokeWebService();
                    permiso.setIdPermiso(webService.getIntegerResponse());
                    permiso.setPendiente(false);
                    Justificacion.update(db, permiso);
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
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncPermisoHoy(DataBase db) {
        WebService webService = new WebService("MartketForce", "GetPermisoHoy");
        webService.setTimeOut(20000);
        webService.addCurrentAuthToken();

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONObject jsonObject = webService.getJSONObjectResponse();
                if(jsonObject != null && !jsonObject.isNull("Tipo"))
                {
                    Permiso permiso = new Permiso();
                    permiso.setFecha(Convert.getDateFromDotNetTicks(jsonObject.getLong("FechaInicioTicks")));
                    permiso.setIdPermiso(1);
                    permiso.setTipo("FALTA");
                    Permiso.insert(db, permiso);
                }
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

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncPermisosPorAprobar(DataBase db) {
        WebService webService = new WebService("MartketForce", "GerPermisosAprobar");
        webService.setTimeOut(20000);
        webService.addCurrentAuthToken();

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                //Justificacion.deleteAll(db, Contract.Justificaciones.TABLE_NAME);
                JSONArray jsonArray = webService.getJSONArrayResponse();
                for(int i = 0; i < jsonArray.length(); i ++)
                {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    Justificacion justificacion = new Justificacion();
                    justificacion = Justificacion.getPermisoByIdServer(db, jObject.getInt("IdPermiso"));
                    justificacion.setIdPermiso(jObject.getInt("IdPermiso"));
                    justificacion.setIdAgente(jObject.getInt("IdAgente"));
                    justificacion.setFecha(Convert.getDateFromDotNetTicks(jObject.getLong("FechaInicioTicks")));
                    justificacion.setTipo(jObject.getString("Motivo"));
                    justificacion.setJornada(jObject.getString("TipoJornada"));
                    justificacion.setAusencia(jObject.getString("Tipo").equalsIgnoreCase("F"));
                    justificacion.setObservacion(jObject.getString("Observacion"));
                    justificacion.setEstado("P");
                    if(justificacion.getID() == 0)
                        Justificacion.insert(db, justificacion);
                    else
                        Justificacion.update(db, justificacion);
                }

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

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncPermisosRevisados(DataBase db) {
        List<Justificacion> justificacions = Justificacion.getPermisosPendientesAprobarUpload(db);


        for(Justificacion permiso : justificacions) {
            WebService webService = new WebService("MartketForce", "InsertPermisoPorAprobar");
            webService.setTimeOut(20000);

            JSONObject jObject = new JSONObject();
            try {
                jObject.put("IdPermiso", permiso.getIdPermiso());
                jObject.put("Estado", permiso.getEstado());
                jObject.put("ObservacionSupervisor", permiso.getObservacionSupervisor());
            } catch (Exception ex) {

            }

            webService.addParameter("permiso", jObject);

            try {
                webService.addCurrentAuthToken();

                try {
                    webService.invokeWebService();
                    permiso.setPendiente(false);
                    Justificacion.update(db, permiso);
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
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
