package rp3.auna.sync.ventanueva;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.auna.Contants;
import rp3.auna.db.Contract;
import rp3.auna.models.ApplicationParameter;
import rp3.auna.models.ventanueva.*;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.util.constants.Constants;
import rp3.auna.util.helper.Alarm;
import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.util.Convert;

/**
 * Created by Jesus Villa on 03/12/2017.
 */

public class VisitaVta {
    private static final String TAG = VisitaVta.class.getSimpleName();

    public static int executeSyncInserts(DataBase db){
        WebService webService = new WebService("MartketForce","InsertarVisitaVta");
        boolean failed = false;
        List<rp3.auna.models.ventanueva.VisitaVta> agendaVtas = rp3.auna.models.ventanueva.VisitaVta.getVisitasInsert(db);
        if(agendaVtas.size() == 0){
            Log.d(TAG,"visitas insertadas.size() == 0...");
            return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
        }

        JSONArray jArray = new JSONArray();
        Log.d(TAG,"Cantidad de Visitas a insertar en el servidor:"+ agendaVtas.size());

        for(int s = 0; s < agendaVtas.size(); s ++)
        {
            rp3.auna.models.ventanueva.VisitaVta cl = agendaVtas.get(s);
            Log.d(TAG,cl.toString());
            try
            {
                Log.d(TAG,(s+1) +" visita a insertar");
                JSONObject visita = new JSONObject();
                visita.put("IdVisita",cl.getIdVisita());
                visita.put("Descripcion",cl.getDescripcion());
                visita.put("Observacion",cl.getObservacion());
                visita.put("FechaVisita",Convert.getDotNetTicksFromDate(cl.getFechaVisita()));
                long inicio = cl.getFechaInicio().getTime();
                Log.d(TAG,"inicio:"+inicio);
                if(inicio!=0){
                    Log.d(TAG,"inicio!=0...");
                    visita.put("FechaInicio",Convert.getDotNetTicksFromDate(cl.getFechaInicio()));
                }else{
                    Log.d(TAG,"inicio!=0 ...");
                    visita.put("FechaInicio",null);
                }
                long fin = cl.getFechaFin().getTime();
                Log.d(TAG,"fin:"+fin);
                if(fin!=0){
                    Log.d(TAG,"fin!=0...");
                    visita.put("FechaFin",Convert.getDotNetTicksFromDate(cl.getFechaFin()));
                }else{
                    Log.d(TAG,"fin==0...");
                    visita.put("FechaFin",null);
                }
                //visita.put("FechaInicio",Convert.getDotNetTicksFromDate(cl.getFechaInicio()));
                //visita.put("FechaFin",Convert.getDotNetTicksFromDate(cl.getFechaFin()));
                visita.put("IdClienteDireccion",cl.getIdClienteDireccion());
                visita.put("IdCliente",cl.getIdCliente());
                visita.put("IdAgente",cl.getIdAgente());
                visita.put("Latitud",cl.getLatitud());
                visita.put("Longitud",cl.getLongitud());
                visita.put("MotivoReprogramacionTabla",cl.getMotivoReprogramacionTabla());
                visita.put("MotivoReprogramacionValue",cl.getMotivoReprogramacionValue());
                visita.put("MotivoVisitaTabla",cl.getMotivoVisitaTabla());
                visita.put("MotivoVisitaValue",cl.getMotivoVisitaValue());
                visita.put("VisitaTabla",1834);
                visita.put("VisitaValue",cl.getVisitaValue());
                visita.put("ReferidoTabla",cl.getReferidoTabla());
                visita.put("ReferidoValue",cl.getReferidoValue());
                visita.put("TiempoCode",cl.getTiempoCode());
                visita.put("DuracionCode",cl.getDuracionCode());
                visita.put("ReferidoCount",cl.getReferidoCount());
                visita.put("TipoVenta",cl.getTipoVenta());
                visita.put("VisitaId",cl.getVisitaId());
                visita.put("LlamadaId",cl.getLlamadaId());
                visita.put("Estado",cl.getEstado());
                JSONArray fotos = new JSONArray();
                if(cl.getFotos()!=null){
                    if(cl.getFotos().size()>0){
                    for(FotoVisitaVta foto:cl.getFotos()){
                        JSONObject fotito = new JSONObject();
                        fotito.put("IdFoto",foto.getIdFoto());
                        fotito.put("IdVisita",foto.getIdVisita());
                        fotito.put("Foto",foto.getFoto());
                        fotito.put("Observacion",foto.getObservacion());
                        fotos.put(fotito);
                    }
                    visita.put("Fotos",fotos);
                    }
                }else{
                    visita.put("Fotos",null);
                }
                visita.put("Estado", cl.getEstado());
                jArray.put(visita);
            }
            catch(Exception ex)
            {
                Log.d(TAG,"Exception:"+ex.getMessage());
            }
        }
        webService.addParameter("visita", jArray);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();

                //webService.getStringResponse();
            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED){
                    Log.d(TAG,"e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED...");
                    failed = true;
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                }
                Log.d(TAG,"..."+e.getMessage());
                failed = true;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                Log.d(TAG,"Exception e:"+e.getMessage());
                failed = true;
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

        } finally {
            if(failed==false){
                Log.d(TAG,"insertado");
                //rp3.auna.models.ventanueva.VisitaVta.actualizarInsertados(db);
                //FotoVisitaVta.actualizarInsertados(db);
                /*if(rp3.auna.models.ventanueva.VisitaVta.getVisitasInsert(db).size()==0){
                    Log.d(TAG,"Ya no hay Visitas insertados con estado 1 ..se subieron al servidor...");
                }else{
                    Log.d(TAG,"Si hay Visitas con estado 1insertados...");
                }
                if(rp3.auna.models.ventanueva.FotoVisitaVta.getFotoVisitasInsert(db).size()==0){
                    Log.d(TAG,"Ya no hay FotosVisitas insertados con estado 1 ..se subieron al servidor...");
                }else{
                    Log.d(TAG,"Si hay FotoVisitas con estado 1insertados...");
                }*/
            }
            if(failed){
                Log.d(TAG,"Ws registrar llamada fallo...");
            }else{
                Log.d(TAG,"ws registrar llamada inserto correctamente...");
            }
            webService.close();
        }
        Log.d(TAG,"failed es:"+failed);
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSync(DataBase db, long fecha, Context context) {
        WebService webService = new WebService("MartketForce", "ObtenerVisitaVta");
        Log.d(TAG,"Iniciar WS executeSync...");
        try {
            Log.d(TAG,"idAgente="+ PreferenceManager.getInt(Contants.KEY_IDAGENTE));
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
            Log.d(TAG,"antes de deleteVisitas fotos visitas agendas...");

            Log.d(TAG,"despues de deletes...");
            try {
                JSONArray agendas = webService.getJSONArrayResponse();
                Log.d(TAG,"cantidad de visitas para insertar en agenda:"+agendas.length());
                if(agendas.length()>0) {
                    rp3.auna.models.ventanueva.VisitaVta.deleteAll(db,Contract.VisitaVta.TABLE_NAME,true);
                    rp3.auna.models.ventanueva.FotoVisitaVta.deleteAll(db,Contract.FotoVisitaVta.TABLE_NAME,true);
                    /**
                     *
                     * Obtener las alertas de visitas para elimnarlas e insertar las nuevas
                     *
                     */

                    //Calendar calendarToday = Calendar.getInstance();
                    //region Eliminar Alertas Visita filtro fecha
                    /*Date today = Convert.getDateFromDotNetTicks(fecha);
                    Calendar calendarFilter = Calendar.getInstance();
                    calendarFilter.setTime(today);
                    boolean sameDay = calendarToday.get(Calendar.YEAR) == calendarFilter.get(Calendar.YEAR) &&
                            calendarToday.get(Calendar.DAY_OF_YEAR) == calendarFilter.get(Calendar.DAY_OF_YEAR);
                    if(sameDay){
                        List<AlarmJvs> list = AlarmJvs.getVisitasAll(db);
                        Log.d(TAG,"Cantidad de alertas de visita:"+list.size());
                        if(list.size()>0){
                            for (AlarmJvs jvs:list){
                                jvs.cancelAlarm(context);
                                AlarmJvs.delete(db,jvs);
                            }
                            //AlarmJvs.deleteAll(db,Contract.AlarmManagerJVS.TABLE_NAME,true);
                        }
                        List<AlarmJvs> list1 = AlarmJvs.getVisitasSupervisorAll(db);
                        for (AlarmJvs jvs:list1){
                            jvs.cancelAlarm(context);
                            AlarmJvs.delete(db,jvs);
                        }
                    }*/
                    //endregion

                    //region Eliminar todas las Alertas de visita
                    /*try{
                        List<AlarmJvs> list = AlarmJvs.getVisitasAll(db);
                        Log.d(TAG,"Cantidad de alertas de visita a eliminar:"+list.size());
                        if(list.size()>0){
                            for (AlarmJvs jvs:list){
                                jvs.cancelAlarm(context);
                                AlarmJvs.delete(db,jvs);
                            }
                        }
                        List<AlarmJvs> list1 = AlarmJvs.getVisitasSupervisorAll(db);
                        Log.d(TAG,"Cantidad de alertas a supervisor de visita a eliminar:"+list1.size());
                        for (AlarmJvs jvs:list1){
                            jvs.cancelAlarm(context);
                            AlarmJvs.delete(db,jvs);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/
                    //endregion

                    /***************************************************************************/
                    /*ApplicationParameter tiempoNotificar = ApplicationParameter.getParameter(db,
                            Constants.PARAMETERID_ALERTAVISITA,Constants.LABEL_VISITAS);
                    int value = Integer.parseInt(tiempoNotificar.getValue());

                    ApplicationParameter tiempoNotificarSupervisor = ApplicationParameter.getParameter(db,
                            Constants.PARAMETERID_ALERTASUPERVISORVISITA,Constants.LABEL_VISITAS);
                    int valueSupervisor = Integer.parseInt(tiempoNotificarSupervisor.getValue());
                    Log.d(TAG,"Valor de notificar visita:"+value);
                    Log.d(TAG,"Valor de supervisor visita alertar:"+valueSupervisor);*/
                    for (int i = 0; i < agendas.length(); i++) {

                        JSONObject visita = agendas.getJSONObject(i);
                        /***
                         * Verificar si tiene Visita
                         */
                        rp3.auna.models.ventanueva.VisitaVta visitaDb = new rp3.auna.models.ventanueva.VisitaVta();
                        if(!visita.isNull("IdVisita")){
                            visitaDb.setIdVisita(visita.getInt("IdVisita"));
                        }else{
                            visitaDb.setIdVisita(0);
                        }
                        if(!visita.isNull("Descripcion")){
                            visitaDb.setDescripcion(visita.getString("Descripcion"));
                        }else{
                            visitaDb.setDescripcion(null);
                        }
                        if(!visita.isNull("Observacion")){
                            visitaDb.setObservacion(visita.getString("Observacion"));
                        }else{
                            visitaDb.setObservacion(null);
                        }if(!visita.isNull("FechaVisita")){
                            visitaDb.setFechaVisita(Convert.getDateFromDotNetTicks(visita.getLong("FechaVisita")));
                        }else{
                            visitaDb.setFechaVisita(null);
                        }
                        if(!visita.isNull("FechaInicio")){
                            visitaDb.setFechaInicio(Convert.getDateFromDotNetTicks(visita.getLong("FechaInicio")));
                        }else{
                            visitaDb.setFechaInicio(null);
                        }
                        if(!visita.isNull("FechaFin")){
                            visitaDb.setFechaFin(Convert.getDateFromDotNetTicks(visita.getLong("FechaFin")));
                        }else{visitaDb.setFechaFin(null);
                        }
                        if(!visita.isNull("IdClienteDireccion")){
                            visitaDb.setIdClienteDireccion(visita.getString("IdClienteDireccion"));
                        }else{
                            visitaDb.setIdClienteDireccion(null);
                        }if(!visita.isNull("IdCliente")){
                            visitaDb.setIdCliente(visita.getInt("IdCliente"));
                        }else{
                            visitaDb.setIdCliente(0);
                        }
                        if(!visita.isNull("IdAgente")){
                            visitaDb.setIdAgente(visita.getInt("IdAgente"));
                        }else{
                            visitaDb.setIdAgente(0);
                        }
                        if(!visita.isNull("Latitud")){
                            visitaDb.setLatitud(visita.getDouble("Latitud"));
                        }else{
                            visitaDb.setLatitud(0);
                        }
                        if(!visita.isNull("Longitud")){
                            visitaDb.setLongitud(visita.getDouble("Longitud"));
                        }else{
                            visitaDb.setLongitud(0);
                        }if(!visita.isNull("MotivoReprogramacionTabla")){
                            visitaDb.setMotivoReprogramacionTabla(visita.getInt("MotivoReprogramacionTabla"));
                        }else{
                            visitaDb.setMotivoReprogramacionTabla(0);
                        }if(!visita.isNull("MotivoReprogramacionValue")){
                            visitaDb.setMotivoReprogramacionValue(visita.getString("MotivoReprogramacionValue"));
                        }else{
                            visitaDb.setMotivoReprogramacionValue(null);
                        }if(!visita.isNull("MotivoVisitaTabla")){
                            visitaDb.setMotivoVisitaTabla(visita.getInt("MotivoVisitaTabla"));
                        }else{
                            visitaDb.setMotivoVisitaTabla(0);
                        }if(!visita.isNull("MotivoVisitaValue")){
                            visitaDb.setMotivoVisitaValue(visita.getString("MotivoVisitaValue"));
                        }else{
                            visitaDb.setMotivoVisitaValue(null);
                        }if(!visita.isNull("VisitaTabla")){
                            visitaDb.setVisitaTabla(visita.getInt("VisitaTabla"));
                        }else{
                            visitaDb.setVisitaTabla(1834);
                        }if(!visita.isNull("VisitaValue")){
                            visitaDb.setVisitaValue(visita.getString("VisitaValue"));
                        }else{
                            visitaDb.setVisitaValue(null);
                        }
                        if(!visita.isNull("ReferidoTabla")){
                            visitaDb.setReferidoTabla(visita.getInt("ReferidoTabla"));
                        }else{
                            visitaDb.setReferidoTabla(0);
                        }if(!visita.isNull("ReferidoValue")){
                            visitaDb.setReferidoValue(visita.getString("ReferidoValue"));
                        }else{
                            visitaDb.setReferidoValue(null);
                        }
                        if(!visita.isNull("TiempoCode")){
                            visitaDb.setTiempoCode(visita.getString("TiempoCode"));
                        }else{
                            visitaDb.setTiempoCode(null);
                        }if(!visita.isNull("DuracionCode")){
                            visitaDb.setDuracionCode(visita.getString("DuracionCode"));
                        }else{
                            visitaDb.setDuracionCode(null);
                        }
                        if(!visita.isNull("ReferidoCount")){
                            visitaDb.setReferidoCount(visita.getInt("ReferidoCount"));
                        }else{
                            visitaDb.setReferidoCount(0);
                        }if(!visita.isNull("TipoVenta")){
                            visitaDb.setTipoVenta(visita.getString("TipoVenta"));
                        }else{
                            visitaDb.setTipoVenta(null);
                        }
                        if(!visita.isNull("VisitaId")){
                            visitaDb.setVisitaId(visita.getInt("VisitaId"));
                        }else{
                            visitaDb.setVisitaId(0);
                        }if(!visita.isNull("LlamadaId")){visitaDb.setLlamadaId(visita.getInt("LlamadaId"));
                        }else{
                            visitaDb.setLlamadaId(0);
                        }
                        if(!visita.isNull("Estado")){
                            visitaDb.setEstado(visita.getInt("Estado"));
                        }else{
                            visitaDb.setEstado(1);
                        }if(!visita.isNull("Fotos")){
                            ArrayList<FotoVisitaVta> fotos = new ArrayList<>();
                            JSONArray fotoJS = visita.getJSONArray("Fotos");
                            for (int j = 0;j<fotoJS.length();j++) {
                                JSONObject f = fotoJS.getJSONObject(i);
                                FotoVisitaVta fo = new FotoVisitaVta();
                                if(!f.isNull("IdFoto")){
                                    fo.setIdFoto(f.getInt("IdFoto"));
                                }else{
                                    fo.setIdFoto(0);
                                }
                                if(!f.isNull("IdVisita")){
                                    fo.setIdVisita(f.getInt("IdVisita"));
                                }else{
                                    fo.setIdVisita(0);
                                }
                                if(!f.isNull("Foto")){
                                    fo.setFoto(f.getString("Foto"));
                                }else{
                                    fo.setFoto(null);
                                }
                                if(!f.isNull("Observacion")){
                                    fo.setObservacion(f.getString("Observacion"));
                                }else{
                                    fo.setObservacion(null);
                                }
                                fo.setInsertado(0);
                                fotos.add(fo);
                            }
                                visitaDb.setFotos(fotos);
                        }else{
                            visitaDb.setFotos(null);
                            }
                        visitaDb.setInsertado(2);
                        boolean insertVisita = rp3.auna.models.ventanueva.VisitaVta.insert(db,visitaDb );
                        Log.d(TAG,insertVisita?"VisitaVta insertada...":"VisitaVta no fue insertada");
                        /*Calendar calendarFilter = Calendar.getInstance();
                        calendarFilter.setTime(visitaDb.getFechaVisita());
                        boolean sameDay = calendarToday.get(Calendar.YEAR) == calendarFilter.get(Calendar.YEAR) &&
                                calendarToday.get(Calendar.DAY_OF_YEAR) == calendarFilter.get(Calendar.DAY_OF_YEAR);
                        if(sameDay){
                            if(visitaDb.getVisitaValue()!=null){
                                if(visitaDb.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_PENDIENTE)){
                                    Log.d(TAG,"Estas en el dia dentro sameday dentro de bucle");
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(visitaDb.getFechaVisita());
                                    Log.d(TAG,"Fecha de visita:"+calendar.getTime().toString());
                                    final long ONE_MINUTE_IN_MILLIS=60000;
                                    final long t= calendar.getTimeInMillis();
                                    //Alertar de Visita
                                    Date fechaVisita = new Date(t-(value*ONE_MINUTE_IN_MILLIS));
                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.setTime(fechaVisita);
                                    Log.d(TAG,"Fecha para alertar visita:"+calendar1.getTime().toString());
                                    int hora = calendar1.get(Calendar.HOUR_OF_DAY);
                                    int minutos = calendar1.get(Calendar.MINUTE);
                                    Alarm.setAlarmVisita(db,context,hora,minutos,visitaDb.getIdVisita()+100);
                                    //Notificar al Supervisor Visita
                                    Date fechaNotificar = new Date(t+(valueSupervisor*ONE_MINUTE_IN_MILLIS));
                                    Calendar calendar2 = Calendar.getInstance();
                                    calendar2.setTime(fechaNotificar);
                                    Log.d(TAG,"Fecha alertar supervisor visita:"+calendar2.getTime().toString());
                                    int horaNotificar = calendar2.get(Calendar.HOUR_OF_DAY);
                                    int minutoNotificar = calendar2.get(Calendar.MINUTE);
                                    Alarm.setAlarmVisitaSupervisor(db,context,horaNotificar,minutoNotificar,visitaDb.getIdVisita()+1000);
                                }
                            }

                        }*/

                    }
                    Log.d(TAG,"Cantidad totales de visitas:"+ rp3.auna.models.ventanueva.VisitaVta.getAll(db).size());
                    //Log.d(TAG,"Cantidad de fotovisitas:"+ rp3.auna.models.ventanueva.FotoVisitaVta.getAll(db).size());
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

    public static int executeSyncUpdate(DataBase db){
        WebService webService = new WebService("MartketForce","ActualizarVisitasVta");
        boolean failed = false;
        Log.d(TAG,"Iniciar WS executeSyncUpdateSync...");
        List<rp3.auna.models.ventanueva.VisitaVta> visitaVtas = rp3.auna.models.ventanueva.VisitaVta.getVisitaSincronizadas(db);
        if(visitaVtas.size() == 0){
            Log.d(TAG,"visitaVtas.size() == 0...");
            return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
        }
        Log.d(TAG,"cantidad de visitas para sincronizar:"+visitaVtas.size());
        JSONArray array = new JSONArray();
        for(int i = 0;i<visitaVtas.size();i++){
            rp3.auna.models.ventanueva.VisitaVta obj = visitaVtas.get(i);
            JSONObject object = new JSONObject();
            try {
                object.put("IdVisita",obj.getIdVisita());
                object.put("Descripcion",obj.getDescripcion());
                object.put("Observacion",obj.getObservacion());
                object.put("FechaVisita", Convert.getDotNetTicksFromDate(obj.getFechaVisita()));
                long inicio = obj.getFechaInicio().getTime();
                Log.d(TAG,"inicio:"+inicio);
                if(inicio!=0){
                    Log.d(TAG,"inicio!=0...");
                    object.put("FechaInicio",Convert.getDotNetTicksFromDate(obj.getFechaInicio()));
                }else{
                    Log.d(TAG,"inicio!=0 ...");
                    object.put("FechaInicio",null);
                }
                long fin = obj.getFechaFin().getTime();
                Log.d(TAG,"fin:"+fin);
                if(fin!=0){
                    Log.d(TAG,"fin!=0...");
                    object.put("FechaFin",Convert.getDotNetTicksFromDate(obj.getFechaFin()));
                }else{
                    Log.d(TAG,"fin==0...");
                    object.put("FechaFin",null);
                }
                //object.put("FechaInicio",Convert.getDotNetTicksFromDate(obj.getFechaInicio()));
                //object.put("FechaFin",Convert.getDotNetTicksFromDate(obj.getFechaFin()));
                object.put("IdClienteDireccion",obj.getIdClienteDireccion());
                object.put("IdCliente",obj.getIdCliente());
                object.put("IdAgente",obj.getIdAgente());
                object.put("Latitud",obj.getLatitud());
                object.put("Longitud",obj.getLongitud());
                object.put("MotivoReprogramacionTabla",obj.getMotivoReprogramacionTabla());
                object.put("MotivoReprogramacionValue",obj.getMotivoReprogramacionValue());
                object.put("MotivoVisitaTabla",obj.getMotivoVisitaTabla());
                object.put("MotivoVisitaValue",obj.getMotivoVisitaValue());
                object.put("VisitaTabla",1834);
                object.put("VisitaValue",obj.getVisitaValue());
                object.put("ReferidoTabla",obj.getReferidoTabla());
                object.put("ReferidoValue",obj.getReferidoValue());
                object.put("TiempoCode",obj.getTiempoCode());
                object.put("DuracionCode",obj.getDuracionCode());
                object.put("TipoVenta",obj.getTipoVenta());
                object.put("ReferidoCount",obj.getReferidoCount());
                object.put("Estado",obj.getEstado());
                object.put("VisitaId",obj.getVisitaId());
                object.put("LlamadaId",obj.getLlamadaId());
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        webService.addParameter("visita", array);
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
            Log.d(TAG,"finally...actualizar todo!!!");
            //rp3.auna.models.ventanueva.VisitaVta.actualizarInsertados(db);
            rp3.auna.models.ventanueva.VisitaVta.deleteAll(db, Contract.VisitaVta.TABLE_NAME);
            rp3.auna.models.ventanueva.FotoVisitaVta.deleteAll(db,Contract.FotoVisitaVta.TABLE_NAME);
        }
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
