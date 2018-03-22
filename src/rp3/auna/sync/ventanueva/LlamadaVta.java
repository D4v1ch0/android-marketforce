package rp3.auna.sync.ventanueva;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.auna.Contants;
import rp3.auna.bean.*;
import rp3.auna.db.Contract;
import rp3.auna.models.ApplicationParameter;
import rp3.auna.models.ventanueva.*;
import rp3.auna.models.ventanueva.VisitaVta;
import rp3.auna.util.constants.Constants;
import rp3.auna.util.helper.Alarm;
import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.util.Convert;

/**
 * Created by Jesus Villa on 19/10/2017.
 */

public class LlamadaVta {
    private static final String TAG = LlamadaVta.class.getSimpleName();

    public static int executeSyncInsert(DataBase db) throws JSONException {
        WebService webService = new WebService("MartketForce","InsertarLlamadaVta");
        boolean failed = false;
        Log.d(TAG,"Iniciar WS InsertarLlamadaVta executeSyncInsert...");
        List<rp3.auna.models.ventanueva.LlamadaVta> llamadaVtas = rp3.auna.models.ventanueva.LlamadaVta.getLlamadasInsert(db);
        if(llamadaVtas.size() == 0){
            Log.d(TAG,"llamadaVtas.size()  insertar desde sqlite == 0...");
            return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
        }
        Log.d(TAG,"cantidad de llamadas para insertar desde sqlite:"+llamadaVtas.size());
        JSONArray array = new JSONArray();
        for(int i = 0;i<llamadaVtas.size();i++){
            rp3.auna.models.ventanueva.LlamadaVta obj = llamadaVtas.get(i);
            JSONObject object = new JSONObject();
            object.put("IdLlamada",obj.getIdLlamada());
            object.put("Descripcion",obj.getDescripcion());
            object.put("Observacion",obj.getObservacion());
            object.put("FechaLlamada", Convert.getDotNetTicksFromDate(obj.getFechaLlamada()));
            long inicio = obj.getFechaInicioLlamada().getTime();
            Log.d(TAG,"inicio:"+inicio);
            if(inicio!=0){
                Log.d(TAG,"inicio!=0...");
                object.put("FechaInicioLlamada",Convert.getDotNetTicksFromDate(obj.getFechaInicioLlamada()));
            }else{
                Log.d(TAG,"inicio!=0 ...");
                object.put("FechaInicioLlamada",null);
            }
            long fin = obj.getFechaFinLlamada().getTime();
            Log.d(TAG,"fin:"+fin);
            if(fin!=0){
                Log.d(TAG,"fin!=0...");
                object.put("FechaFinLlamada",Convert.getDotNetTicksFromDate(obj.getFechaFinLlamada()));
            }else{
                Log.d(TAG,"fin==0...");
                object.put("FechaFinLlamada",null);
            }
            object.put("Duracion",obj.getDuracion());
            object.put("IdCliente",obj.getIdCliente());
            object.put("IdAgente",PreferenceManager.getInt(Contants.KEY_IDAGENTE));
            object.put("Latitud",obj.getLatitud());
            object.put("Longitud",obj.getLongitud());
            object.put("LlamadaTabla",Contants.GENERAL_TABLE_ESTADOS_LLAMADA);
            object.put("LlamadoValue",obj.getLlamadoValue());
            object.put("MotivoReprogramacionTabla",obj.getMotivoReprogramacionTabla());
            object.put("MotivoReprogramacionValue",obj.getMotivoReprogramacionValue());
            object.put("MotivoVisitaTabla",obj.getMotivoVisitaTabla());
            object.put("MotivoVisitaValue",obj.getMotivoVisitaValue());
            object.put("ReferidoTabla",obj.getReferidoTabla());
            object.put("ReferidoValue",obj.getReferidoValue());
            object.put("LlamadaValor",obj.getLlamadaValor());
            object.put("LlamadaId",obj.getLlamadaId());
            array.put(object);
        }

        webService.addParameter("llamada", array);
        try {
            webService.addCurrentAuthToken();

            try {
                webService.setTimeOut(35000);
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED){
                    Log.d(TAG,"e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED...");
                    Log.d(TAG,"STATUScODE = "+e.getStatusCode());
                    failed = true;
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                }
                Log.d(TAG,"dentor del catch:"+e.getMessage());
                failed = true;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                Log.d(TAG,"Exception e:"+e.getMessage());
                failed = true;
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }
        } finally {
            webService.close();
            if(false){
                Log.d(TAG,"Hubo error al registrar llamadas ws...");
            }else{
                Log.d(TAG,"No Hubo error al registrar llamadas ws...");
            }
            //rp3.auna.models.ventanueva.LlamadaVta.actualizarInsertados(db);
        }
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncUpdate(DataBase db) throws JSONException {
        WebService webService = new WebService("MartketForce","ActualizarLlamadasVta");
        boolean failed = false;
        Log.d(TAG,"Iniciar WS executeSyncUpdateSync...");
        List<rp3.auna.models.ventanueva.LlamadaVta> llamadaVtas = rp3.auna.models.ventanueva.LlamadaVta.getLlamadasSincronizadasAll(db);
        if(llamadaVtas.size() == 0){
            Log.d(TAG,"llamadaVtas.size() a actualizar desde sqlite == 0...");
            return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
        }
        Log.d(TAG,"cantidad de llamadas para sincronizar:"+llamadaVtas.size());
        JSONArray array = new JSONArray();
        for(int i = 0;i<llamadaVtas.size();i++){
            rp3.auna.models.ventanueva.LlamadaVta obj = llamadaVtas.get(i);
            JSONObject object = new JSONObject();
            object.put("IdLlamada",obj.getIdLlamada());
            object.put("Descripcion",obj.getDescripcion());
            object.put("Observacion",obj.getObservacion());
            object.put("FechaLlamada", Convert.getDotNetTicksFromDate(obj.getFechaLlamada()));
            long inicio = obj.getFechaInicioLlamada().getTime();
            Log.d(TAG,"inicio:"+inicio);
            if(inicio!=0){
                Log.d(TAG,"inicio!=0...");
                object.put("FechaInicioLlamada",Convert.getDotNetTicksFromDate(obj.getFechaInicioLlamada()));
            }else{
                Log.d(TAG,"inicio!=0 ...");
                object.put("FechaInicioLlamada",null);
            }
            long fin = obj.getFechaFinLlamada().getTime();
            Log.d(TAG,"fin:"+fin);
            if(fin!=0){
                Log.d(TAG,"fin!=0...");
                object.put("FechaFinLlamada",Convert.getDotNetTicksFromDate(obj.getFechaFinLlamada()));
            }else{
                Log.d(TAG,"fin==0...");
                object.put("FechaFinLlamada",null);
            }
            //object.put("FechaInicioLlamada",Convert.getDotNetTicksFromDate(obj.getFechaInicioLlamada()));
            //object.put("FechaFinLlamada",Convert.getDotNetTicksFromDate(obj.getFechaFinLlamada()));
            object.put("Duracion",obj.getDuracion());
            object.put("IdCliente",obj.getIdCliente());
            object.put("IdAgente",PreferenceManager.getInt(Contants.KEY_IDAGENTE));
            object.put("Latitud",obj.getLatitud());
            object.put("Longitud",obj.getLongitud());
            object.put("LlamadaTabla",Contants.GENERAL_TABLE_ESTADOS_LLAMADA);
            object.put("LlamadoValue",obj.getLlamadoValue());
            object.put("MotivoReprogramacionTabla",obj.getMotivoReprogramacionTabla());
            object.put("MotivoReprogramacionValue",obj.getMotivoReprogramacionValue());
            object.put("MotivoVisitaTabla",obj.getMotivoVisitaTabla());
            object.put("MotivoVisitaValue",obj.getMotivoVisitaValue());
            object.put("ReferidoTabla",obj.getReferidoTabla());
            object.put("ReferidoValue",obj.getReferidoValue());
            object.put("LlamadaValor",obj.getLlamadaValor());
            object.put("LlamadaId",obj.getLlamadaId());
            array.put(object);
        }

        webService.addParameter("llamada", array);
        try {
            webService.addCurrentAuthToken();

            try {
                webService.setTimeOut(35000);
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED){
                    Log.d(TAG,"e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED...");
                    Log.d(TAG,"STATUScODE = "+e.getStatusCode());
                    failed = true;
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                }
                Log.d(TAG,"dentor del catch:"+e.getMessage());
                failed = true;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                Log.d(TAG,"Exception e:"+e.getMessage());
                failed = true;
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }
        } finally {
            webService.close();
            if(false){
                Log.d(TAG,"Hubo error al actualizar llamadas ws...");
            }else{
                Log.d(TAG,"No Hubo error al actualizar llamadas ws...");
            }
            //Log.d(TAG,"finally...actualizar todo!!!");
            //rp3.auna.models.ventanueva.LlamadaVta.actualizarInsertados(db);
            rp3.auna.models.ventanueva.LlamadaVta.deleteAll(db,Contract.LlamadaVta.TABLE_NAME);
        }
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSync(DataBase db, long fecha, Context context) {
        WebService webService = new WebService("MartketForce", "ObtenerLlamadaVta");
        Log.d(TAG,"Iniciar WS executeSync...");
        try {
            Log.d(TAG,"idAgente="+ PreferenceManager.getInt(Contants.KEY_IDAGENTE,0));
            webService.addParameter("@idagente", PreferenceManager.getInt(Contants.KEY_IDAGENTE));
            webService.addParameter("@fecha", fecha);
            webService.addCurrentAuthToken();
            try {
                webService.setTimeOut(35000);
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

            try {
                JSONArray agendas = webService.getJSONArrayResponse();
                Log.d(TAG,"cantidad de llamadas:"+agendas.length());
                if(agendas.length()>0) {
                    Log.d(TAG,"llamadas.length>0");
                    rp3.auna.models.ventanueva.LlamadaVta.deleteAll(db,Contract.LlamadaVta.TABLE_NAME,true);
                    Log.d(TAG,"despues de eliminar llamadas...");
                    /**
                     *
                     * Obtener las alertas de llamadas para elimnarlas e insertar las nuevas
                     *
                     */
                    //region Eliminar Alerta de llamadas por filtro de fecha
                    //Date today = Convert.getDateFromDotNetTicks(fecha);
                    //Calendar calendarToday = Calendar.getInstance();
                    /*Calendar calendarFilter = Calendar.getInstance();
                    calendarFilter.setTime(today);
                    boolean sameDay = calendarToday.get(Calendar.YEAR) == calendarFilter.get(Calendar.YEAR) &&
                            calendarToday.get(Calendar.DAY_OF_YEAR) == calendarFilter.get(Calendar.DAY_OF_YEAR);
                    if(sameDay){
                        Log.d(TAG," Es el dia de hoy sameday...");
                        try {
                            List<AlarmJvs> list = AlarmJvs.getLlamadasAll(db);
                            if(list!=null){
                                if(list.size()>0){
                                    Log.d(TAG,"Cantidad de alarmas de llamada:"+list.size());
                                    for (AlarmJvs jvs:list){
                                        jvs.cancelAlarm(context);
                                        AlarmJvs.delete(db,jvs);
                                    }
                                }
                            }
                            List<AlarmJvs> list1 = AlarmJvs.getLlamadasSupervisorAll(db);
                            for (AlarmJvs jvs:list1){
                                jvs.cancelAlarm(context);
                                AlarmJvs.delete(db,jvs);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }*/

                    //endregion

                    //region Eliminar Todas las Alertas de llamadas
                    /*try {
                        List<AlarmJvs> list = AlarmJvs.getLlamadasAll(db);
                        if(list!=null){
                            if(list.size()>0){
                                Log.d(TAG,"Cantidad de alarmas de llamada a eliminar:"+list.size());
                                for (AlarmJvs jvs:list){
                                    jvs.cancelAlarm(context);
                                    AlarmJvs.delete(db,jvs);
                                }
                            }
                        }
                        List<AlarmJvs> list1 = AlarmJvs.getLlamadasSupervisorAll(db);
                        Log.d(TAG,"Cantidad de alarmas de llamada a notificar supervisor a eliminar:"+list1.size());
                        for (AlarmJvs jvs:list1){
                            jvs.cancelAlarm(context);
                            AlarmJvs.delete(db,jvs);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/
                    //endregion

                    /*ApplicationParameter tiempoNotificar = ApplicationParameter.getParameter(db,
                            Constants.PARAMETERID_ALERTALLAMADA,Constants.LABEL_LLAMADAS);
                    int value = Integer.parseInt(tiempoNotificar.getValue());
                    Log.d(TAG,"Tiempo para notificar llamada:"+value+" minutos");
                    ApplicationParameter tiempoNotificarSupervisor = ApplicationParameter.getParameter(db,
                            Constants.PARAMETERID_ALERTASUPERVISOR,Constants.LABEL_LLAMADAS);
                    int valueSupervisor = Integer.parseInt(tiempoNotificarSupervisor.getValue());
                    Log.d(TAG,"Tiempo para notificar llamada supervisor:"+valueSupervisor);*/
                    /***************************************************************************/
                    for (int i = 0; i < agendas.length(); i++) {
                        JSONObject llamada = agendas.getJSONObject(i);
                        /**
                         * Obtener datos de Llamada
                         */
                        rp3.auna.models.ventanueva.LlamadaVta llamadaVtaDb = new rp3.auna.models.ventanueva.LlamadaVta();
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
                        if (!llamada.isNull("Observacion")) {
                            llamadaVtaDb.setObservacion(llamada.getString("Observacion"));
                        } else {
                            llamadaVtaDb.setObservacion("");
                        }
                        if (!llamada.isNull("FechaLlamada")) {
                            llamadaVtaDb.setFechaLlamada(Convert.getDateFromDotNetTicks(llamada.getLong("FechaLlamada")));
                        } else {
                            llamadaVtaDb.setFechaLlamada(null);
                        }

                        if (!llamada.isNull("FechaInicioLlamada")) {
                            llamadaVtaDb.setFechaInicioLlamada(Convert.getDateFromDotNetTicks(llamada.getLong("FechaInicioLlamada")));
                        } else {
                            llamadaVtaDb.setFechaInicioLlamada(null);
                        }
                        if (!llamada.isNull("FechaFinLlamada")) {
                            llamadaVtaDb.setFechaFinLlamada(Convert.getDateFromDotNetTicks(llamada.getLong("FechaFinLlamada")));
                        } else {
                            llamadaVtaDb.setFechaFinLlamada(null);
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
                            llamadaVtaDb.setLlamadaTabla(Contants.GENERAL_TABLE_ESTADOS_LLAMADA);
                        }
                        if (!llamada.isNull("LlamadoValue")) {
                            llamadaVtaDb.setLlamadoValue(llamada.getString("LlamadoValue"));
                        } else {
                            llamadaVtaDb.setLlamadoValue("");
                        }

                        if (!llamada.isNull("MotivoVisitaTabla")) {
                            llamadaVtaDb.setMotivoVisitaTabla(llamada.getInt("MotivoVisitaTabla"));
                        } else {
                            llamadaVtaDb.setMotivoVisitaTabla(0);
                        }
                        if (!llamada.isNull("MotivoVisitaValue")) {
                            llamadaVtaDb.setMotivoVisitaValue(llamada.getString("MotivoVisitaValue"));
                        } else {
                            llamadaVtaDb.setMotivoVisitaValue("");
                        }
                        if (!llamada.isNull("Latitud")) {
                            llamadaVtaDb.setLatitud(llamada.getDouble("Latitud"));
                        } else {
                            llamadaVtaDb.setLatitud(0);
                        }
                        if (!llamada.isNull("Longitud")) {
                            llamadaVtaDb.setLongitud(llamada.getDouble("Longitud"));
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
                        if (!llamada.isNull("LlamadaId")) {
                            llamadaVtaDb.setLlamadaId(llamada.getInt("LlamadaId"));
                        } else {
                            llamadaVtaDb.setLlamadaId(0);
                        }
                        llamadaVtaDb.setEstado(1);
                        llamadaVtaDb.setInsertado(2);
                        boolean insertLlamada = rp3.auna.models.ventanueva.LlamadaVta.insert(db, llamadaVtaDb);
                        Log.d(TAG,insertLlamada?"llamada insertada...":"llamada no fue insertada...");
                        /*Calendar calendarFilter = Calendar.getInstance();
                        calendarFilter.setTime(llamadaVtaDb.getFechaLlamada());
                        boolean sameDay = calendarToday.get(Calendar.YEAR) == calendarFilter.get(Calendar.YEAR) &&
                                calendarToday.get(Calendar.DAY_OF_YEAR) == calendarFilter.get(Calendar.DAY_OF_YEAR);

                        if(sameDay){
                            if(llamadaVtaDb.getLlamadoValue()!=null){
                                if(llamadaVtaDb.getLlamadoValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_LLAMADA_PENDIENTE)){
                                    Log.d(TAG,"Estas en el dia dentro de bucle es sameday...");
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(llamadaVtaDb.getFechaLlamada());
                                    final long ONE_MINUTE_IN_MILLIS=60000;
                                    final long t= calendar.getTimeInMillis();
                                    Log.d(TAG,"Fecha llamada:"+calendar.getTime().toString());
                                    //Alertar Llamada
                                    Date fechaLlamada = new Date(t-(value*ONE_MINUTE_IN_MILLIS));
                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.setTime(fechaLlamada);
                                    Log.d(TAG,"Fecha Alertar la llamada:"+calendar1.getTime().toString());
                                    int hora = calendar1.get(Calendar.HOUR_OF_DAY);
                                    int minutos = calendar1.get(Calendar.MINUTE);
                                    Alarm.setAlarmLlamada(db,context,hora,minutos,llamadaVtaDb.getIdLlamada()+200);
                                    //Notificar al Supervisor
                                    Date fechaNotificar = new Date(t+(valueSupervisor*ONE_MINUTE_IN_MILLIS));
                                    Calendar calendar2 = Calendar.getInstance();
                                    calendar2.setTime(fechaNotificar);
                                    Log.d(TAG,"Fecha Notificar Supervisor:"+calendar2.getTime().toString());
                                    int horaNotificar = calendar2.get(Calendar.HOUR_OF_DAY);
                                    int minutoNotificar = calendar2.get(Calendar.MINUTE);
                                    Alarm.setAlarmLlamadaSupervisor(db,context,horaNotificar,minutoNotificar,llamadaVtaDb.getIdLlamada()+2000);
                                }
                            }
                        }*/
                    }
                    Log.d(TAG,"Cantidad de llamadas:"+ rp3.auna.models.ventanueva.LlamadaVta.getLlamadasAll(db).size());
                }
            } catch (JSONException e) {
                Log.d(TAG, "JSONException:"+e.toString());
                e.printStackTrace();
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }
        } finally {
            webService.close();
        }

        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
