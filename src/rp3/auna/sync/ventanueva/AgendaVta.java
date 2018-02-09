package rp3.auna.sync.ventanueva;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.util.ArrayList;
import java.util.List;

import rp3.auna.Contants;
import rp3.auna.db.Contract;
import rp3.auna.models.ventanueva.FotoVisitaVta;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.models.ventanueva.VisitaVta;
import rp3.auna.sync.Llamada;
import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.util.Convert;

/**
 * Created by Jesus Villa on 25/09/2017.
 */

public class AgendaVta {

    private static final String TAG = AgendaVta.class.getSimpleName();

    public static int executeSync(DataBase db) {
        WebService webService = new WebService("MartketForce", "ObtenerAgendaVta");
        Log.d(TAG,"Iniciar WS executeSync...");
        try {
            Log.d(TAG,"idAgente="+ PreferenceManager.getInt(Contants.KEY_IDAGENTE));
            webService.addParameter("@idagente", PreferenceManager.getInt(Contants.KEY_IDAGENTE));
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
            Log.d(TAG,"antes de deleteLlamadas fotos visitas agendas...");
            rp3.auna.models.ventanueva.AgendaVta.deleteAgendas(db);
            VisitaVta.deleteVisitas(db);
            LlamadaVta.deleteLlamadas(db);
            FotoVisitaVta.deleteFotoVisita(db);
            Log.d(TAG,"despues de deletes...");
            try {
                JSONArray agendas = webService.getJSONArrayResponse();
                Log.d(TAG,"cantidad de agendas:"+agendas.length());
                if(agendas.length()>0) {
                    for (int i = 0; i < agendas.length(); i++) {

                        JSONObject agenda = agendas.getJSONObject(i);
                        rp3.auna.models.ventanueva.AgendaVta agendaVtaDb = new rp3.auna.models.ventanueva.AgendaVta();
                        /**
                         * Obtener datos de Agenda
                         */
                        if(!agenda.isNull("IdAgenda")){
                            agendaVtaDb.setIdAgenda(agenda.getInt("IdAgenda"));
                        }else{
                            agendaVtaDb.setIdAgenda(0);
                        }
                        if(!agenda.isNull("IdCliente")){
                            agendaVtaDb.setIdCliente(agenda.getInt("IdCliente"));
                        }else{
                            agendaVtaDb.setIdCliente(0);
                        }
                        if(!agenda.isNull("IdAgente")){
                            agendaVtaDb.setIdAgente(agenda.getInt("IdAgente"));
                        }else{
                            agendaVtaDb.setIdAgente(0);
                        }
                        if(!agenda.isNull("OrigenTabla")){
                            agendaVtaDb.setOrigenTabla(agenda.getInt("OrigenTabla"));
                        }else{
                            agendaVtaDb.setOrigenTabla(0);
                        }
                        if(!agenda.isNull("OrigenValue")){
                            agendaVtaDb.setOrigenValue(agenda.getString("OrigenValue"));
                        }else{
                            agendaVtaDb.setOrigenValue(null);
                        }
                        if(!agenda.isNull("Latitud")){
                            agendaVtaDb.setLatitud(agenda.getDouble("Latitud"));
                        }else{
                            agendaVtaDb.setLatitud(0);
                        }
                        if(!agenda.isNull("Longitud")){
                            agendaVtaDb.setLongitud(agenda.getDouble("Longitud"));
                        }else{
                            agendaVtaDb.setLongitud(0);
                        }
                        agendaVtaDb.setInsertado(0);
                        boolean insertAgenda = rp3.auna.models.ventanueva.AgendaVta.insert(db,agendaVtaDb);
                        if (insertAgenda) {
                            Log.d(TAG, "Agenda insertada..." + agendaVtaDb.toString());
                        }else{
                            Log.d(TAG,"Agenda no fue insertada...");
                        }
                        /***
                         * Verificar si tiene Visita
                         */
                        if(!agenda.isNull("Visita")){
                            JSONObject visita = agenda.getJSONObject("Visita");
                            VisitaVta visitaDb = new VisitaVta();
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
                            }
                            if(!visita.isNull("FechaVisita")){
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
                            }else{
                                visitaDb.setFechaFin(null);
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
                            }
                            if(!visita.isNull("MotivoReprogramacionValue")){
                                visitaDb.setMotivoReprogramacionValue(visita.getString("MotivoReprogramacionValue"));
                            }else{
                                visitaDb.setMotivoReprogramacionValue(null);
                            }if(!visita.isNull("MotivoVisitaTabla")){
                                visitaDb.setMotivoVisitaTabla(visita.getInt("MotivoVisitaTabla"));
                            }else{
                                visitaDb.setMotivoVisitaTabla(0);
                            }
                            if(!visita.isNull("MotivoVisitaValue")){
                                visitaDb.setMotivoVisitaValue(visita.getString("MotivoVisitaValue"));
                            }else{
                                visitaDb.setMotivoVisitaValue(null);
                            }if(!visita.isNull("VisitaTabla")){
                                visitaDb.setVisitaTabla(visita.getInt("VisitaTabla"));
                            }else{
                                visitaDb.setVisitaTabla(0);
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
                            }if(!visita.isNull("LlamadaId")){
                                visitaDb.setLlamadaId(visita.getInt("LlamadaId"));
                            }else{
                                visitaDb.setLlamadaId(0);
                            }
                            if(!visita.isNull("Estado")){
                                visitaDb.setEstado(visita.getInt("Estado"));
                            }else{
                                visitaDb.setEstado(1);
                            }
                            if(!visita.isNull("Fotos")){
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
                                    fo.setInsertado(2);
                                    fotos.add(fo);
                                }
                                visitaDb.setFotos(fotos);

                            }else{
                                visitaDb.setFotos(null);
                            }
                            visitaDb.setInsertado(0);
                            boolean insertVisita = VisitaVta.insert(db,visitaDb );
                            if (insertVisita) {
                                Log.d(TAG, "VisitaVta insertada..." + visitaDb.toString());
                            }else{
                                Log.d(TAG,"VisitaVta no fue insertada...");
                            }
                            //Else Then Don`t Have any Visit
                        }else {
                            agendaVtaDb.setVisita(null);
                        }
                        /**
                         * Verificar si tiene Llamada
                         */
                        if(!agenda.isNull("Llamada")){
                            JSONObject llamada = agenda.getJSONObject("Llamada");
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
                                llamadaVtaDb.setLlamadaTabla(0);
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

                            llamadaVtaDb.setEstado(0);
                            llamadaVtaDb.setInsertado(0);
                            boolean insertLlamada = LlamadaVta.insert(db, llamadaVtaDb);
                            if (insertLlamada) {
                                Log.d(TAG, "llamada insertada..." + llamadaVtaDb.toString());
                            }else{
                                Log.d(TAG,"llamada no fue insertada...");
                            }
                            //Else then don`t have any calls
                        }else{
                            agendaVtaDb.setLlamada(null);
                        }

                    }
                    Log.d(TAG,"Cantidad de agendas:"+ rp3.auna.models.ventanueva.AgendaVta.getAll(db).size());
                    Log.d(TAG,"Cantidad de llamadas:"+ rp3.auna.models.ventanueva.LlamadaVta.getLlamadasAll(db).size());
                    Log.d(TAG,"Cantidad de visitas:"+ rp3.auna.models.ventanueva.VisitaVta.getAll(db).size());
                    Log.d(TAG,"Cantidad de fotovisitas:"+ rp3.auna.models.ventanueva.FotoVisitaVta.getAll(db).size());
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
        WebService webService = new WebService("MartketForce","InsertarAgendaVta");
        boolean failed = false;
        int id = 0;

        List<rp3.auna.models.ventanueva.AgendaVta> agendaVtas = rp3.auna.models.ventanueva.AgendaVta.getAgendasInsert(db);
        if(agendaVtas.size() == 0){
            Log.d(TAG,"llamadaVtas.size() == 0...");
            return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
        }

        JSONObject jObject = new JSONObject();
        JSONArray jArray = new JSONArray();
        Log.d(TAG,"Cantidad de agendasVta a insertar:"+ agendaVtas.size());

        for(int s = 0; s < agendaVtas.size(); s ++)
        {
            rp3.auna.models.ventanueva.AgendaVta cl = agendaVtas.get(s);
            Log.d(TAG,cl.toString());
            try
            {
                Log.d(TAG,(s+1) +" agenda a insertar");
                jObject = new JSONObject();
                jObject.put("IdAgenda", cl.getIdAgenda());
                jObject.put("IdAgente", cl.getIdAgente());
                jObject.put("IdCliente", cl.getIdCliente());
                jObject.put("OrigenTabla", cl.getOrigenTabla());
                jObject.put("OrigenValue", cl.getOrigenValue());
                jObject.put("Latitud", cl.getLatitud());
                jObject.put("Longitud", cl.getLongitud());

                if(cl.getLlamada()==null){
                    jObject.put("Llamada",null);
                }else{
                    Log.d(TAG,(s+1) +" llamada a insertar");
                    JSONObject llamada = new JSONObject();
                    llamada.put("IdLlamada",cl.getLlamada().getIdLlamada());
                    llamada.put("Descripcion",cl.getLlamada().getDescripcion());
                    llamada.put("Observacion",cl.getLlamada().getObservacion());
                    llamada.put("FechaLlamada",Convert.getDotNetTicksFromDate(cl.getLlamada().getFechaLlamada()));
                    llamada.put("FechaInicioLlamada",Convert.getDotNetTicksFromDate(cl.getLlamada().getFechaInicioLlamada()));
                    llamada.put("FechaFinLlamada",Convert.getDotNetTicksFromDate(cl.getLlamada().getFechaFinLlamada()));
                    llamada.put("Duracion",cl.getLlamada().getDuracion());
                    llamada.put("IdCliente",cl.getLlamada().getIdCliente());
                    llamada.put("IdAgente",cl.getLlamada().getIdAgente());
                    llamada.put("Latitud",cl.getLlamada().getLatitud());
                    llamada.put("Longitud",cl.getLlamada().getLongitud());
                    llamada.put("LlamadaTabla",cl.getLlamada().getLlamadaTabla());
                    llamada.put("LlamadoValue",cl.getLlamada().getLlamadoValue());
                    llamada.put("MotivoReprogramacionTabla",cl.getLlamada().getMotivoReprogramacionTabla());
                    llamada.put("MotivoReprogramacionValue",cl.getLlamada().getMotivoReprogramacionValue());
                    llamada.put("MotivoVisitaTabla",cl.getLlamada().getMotivoVisitaTabla());
                    llamada.put("MotivoVisitaValue",cl.getLlamada().getMotivoVisitaValue());
                    llamada.put("ReferidoTabla",cl.getLlamada().getReferidoTabla());
                    llamada.put("ReferidoValue",cl.getLlamada().getReferidoValue());
                    llamada.put("LlamadaValor",cl.getLlamada().getLlamadaValor());
                    llamada.put("LlamadaId",cl.getLlamada().getLlamadaId());
                    jObject.put("Llamada",llamada);
                }
                if(cl.getVisita()==null){
                    jObject.put("Visita",null);
                }else{
                    Log.d(TAG,(s+1) +" visita a insertar");
                    JSONObject visita = new JSONObject();
                    visita.put("IdVisita",cl.getVisita().getIdVisita());
                    visita.put("Descripcion",cl.getVisita().getDescripcion());
                    visita.put("Observacion",cl.getVisita().getObservacion());
                    visita.put("FechaVisita",Convert.getDotNetTicksFromDate(cl.getVisita().getFechaVisita()));
                    visita.put("FechaInicio",Convert.getDotNetTicksFromDate(cl.getVisita().getFechaInicio()));
                    visita.put("FechaFin",Convert.getDotNetTicksFromDate(cl.getVisita().getFechaFin()));
                    visita.put("IdClienteDireccion",cl.getVisita().getIdClienteDireccion());
                    visita.put("IdCliente",cl.getVisita().getIdCliente());
                    visita.put("IdAgente",cl.getVisita().getIdAgente());
                    visita.put("Latitud",cl.getVisita().getLatitud());
                    visita.put("Longitud",cl.getVisita().getLongitud());
                    visita.put("MotivoReprogramacionTabla",cl.getVisita().getMotivoReprogramacionTabla());
                    visita.put("MotivoReprogramacionValue",cl.getVisita().getMotivoReprogramacionValue());
                    visita.put("MotivoVisitaTabla",cl.getVisita().getMotivoVisitaTabla());
                    visita.put("MotivoVisitaValue",cl.getVisita().getMotivoVisitaValue());
                    visita.put("VisitaTabla",cl.getVisita().getVisitaTabla());
                    visita.put("VisitaValue",cl.getVisita().getVisitaValue());
                    visita.put("ReferidoTabla",cl.getVisita().getReferidoTabla());
                    visita.put("ReferidoValue",cl.getVisita().getReferidoValue());
                    visita.put("TiempoCode",cl.getVisita().getTiempoCode());
                    visita.put("DuracionCode",cl.getVisita().getDuracionCode());
                    visita.put("ReferidoCount",cl.getVisita().getReferidoCount());
                    visita.put("TipoVenta",cl.getVisita().getTipoVenta());
                    visita.put("VisitaId",cl.getVisita().getVisitaId());
                    visita.put("LlamadaId",cl.getVisita().getLlamadaId());
                    visita.put("Estado",cl.getVisita().getEstado());
                    JSONArray fotos = new JSONArray();
                    if(cl.getVisita().getFotos()!=null && cl.getVisita().getFotos().size()>0){
                        for(FotoVisitaVta foto:cl.getVisita().getFotos()){
                            JSONObject fotito = new JSONObject();
                            fotito.put("IdFoto",foto.getIdFoto());
                            fotito.put("IdVisita",foto.getIdVisita());
                            fotito.put("Foto",foto.getFoto());
                            fotito.put("Observacion",foto.getObservacion());
                            fotos.put(fotito);
                        }
                        visita.put("Fotos",fotos);
                    }else{
                        visita.put("Fotos",null);
                    }
                    jObject.put("Visita",visita);
                }


                jObject.put("Estado", cl.getEstado());

                jArray.put(jObject);
            }
            catch(Exception ex)
            {
                Log.d(TAG,"Exception:"+ex.getMessage());
            }
        }
        webService.addParameter("Agenda", jArray);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
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
                rp3.auna.models.ventanueva.AgendaVta.ActualizarInsertados(db);
                VisitaVta.actualizarInsertados(db);
                FotoVisitaVta.actualizarInsertados(db);
                if(rp3.auna.models.ventanueva.AgendaVta.getAgendasInsert(db).size()==0){
                    Log.d(TAG,"Ya no hay agendas insertados con estado 1 ..se subieron al servidor...");
                }else{
                    Log.d(TAG,"Si hay agendas con estado 1insertados...");
                }
                if(rp3.auna.models.ventanueva.LlamadaVta.getLlamadasInsert(db).size()==0){
                    Log.d(TAG,"Ya no hay Llamadas insertados con estado 1 ..se subieron al servidor...");
                }else{
                    Log.d(TAG,"Si hay Llamadas con estado 1insertados...");
                }
                if(rp3.auna.models.ventanueva.VisitaVta.getVisitasInsert(db).size()==0){
                    Log.d(TAG,"Ya no hay Visitas insertados con estado 1 ..se subieron al servidor...");
                }else{
                    Log.d(TAG,"Si hay Visitas con estado 1insertados...");
                }
                if(rp3.auna.models.ventanueva.FotoVisitaVta.getFotoVisitasInsert(db).size()==0){
                    Log.d(TAG,"Ya no hay FotosVisitas insertados con estado 1 ..se subieron al servidor...");
                }else{
                    Log.d(TAG,"Si hay FotoVisitas con estado 1insertados...");
                }
            }
            webService.close();
        }
        Log.d(TAG,"failed es:"+failed);
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
