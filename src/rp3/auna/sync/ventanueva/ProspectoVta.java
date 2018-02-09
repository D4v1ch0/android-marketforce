package rp3.auna.sync.ventanueva;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rp3.auna.Contants;
import rp3.auna.db.Contract;
import rp3.auna.models.ventanueva.*;
import rp3.auna.models.ventanueva.AgendaVta;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.models.ventanueva.VisitaVta;
import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.util.Convert;

/**
 * Created by Jesus Villa on 09/10/2017.
 */

public class ProspectoVta {

    private static final String TAG = ProspectoVta.class.getSimpleName();

    public static int executeSync(DataBase db) {
        WebService webService = new WebService("MartketForce", "ObtenerProspectoVta");
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

            JSONArray prospectos = webService.getJSONArrayResponse();
            TypeToken<List<rp3.auna.bean.ProspectoVta>> token = new TypeToken<List<rp3.auna.bean.ProspectoVta>>() {};
            List<rp3.auna.bean.ProspectoVta> prospectoVtaList = new Gson().fromJson(prospectos.toString(),token.getType());
            if(prospectos!=null){
                if(prospectos.length()>0){
                    ProspectoVtaDb.deleteAll(db, Contract.ProspectoVta.TABLE_NAME);
                    Log.d(TAG,"cantidad de prospectos WS por idAgente:"+prospectos.length());
                    for(rp3.auna.bean.ProspectoVta prospectoVta:prospectoVtaList){
                        Log.d(TAG,prospectoVta.toString());
                        ProspectoVtaDb obj = new ProspectoVtaDb();
                        obj.setLlamadaReferido(prospectoVta.getLlamadaReferido());
                        obj.setVisitaReferido(prospectoVta.getVisitaReferido());
                        obj.setIdAgente(prospectoVta.getIdAgente());
                        obj.setTipoPersonaCode(prospectoVta.getTipoPersonaCode());
                        obj.setContactoTelefono(prospectoVta.getContactoTelefono());
                        obj.setContactoApellidoMaterno(prospectoVta.getContactoApellidoMaterno());
                        obj.setContactoApellidoPaterno(prospectoVta.getContactoApellidoPaterno());
                        obj.setContactoNombre(prospectoVta.getContactoNombre());
                        obj.setEmpresaTelefono(prospectoVta.getEmpresaTelefono());
                        obj.setRuc(prospectoVta.getRuc());
                        obj.setRazonSocial(prospectoVta.getRazonSocial());
                        obj.setNombres(prospectoVta.getNombres());
                        obj.setApellidoPaterno(prospectoVta.getApellidoPaterno());
                        obj.setApellidoMaterno(prospectoVta.getApellidoMaterno());
                        obj.setTipoDocumento(prospectoVta.getTipoDocumento());
                        obj.setDocumento(prospectoVta.getDocumento());
                        obj.setDireccion2(prospectoVta.getDireccion2());
                        obj.setDireccion1(prospectoVta.getDireccion1());
                        obj.setTelefono(prospectoVta.getTelefono());
                        obj.setCelular(prospectoVta.getCelular());
                        obj.setNombre(prospectoVta.getNombre());
                        obj.setOrigenCode(prospectoVta.getOrigenCode());
                        obj.setEmail(prospectoVta.getEmail());
                        obj.setEstadoCode(prospectoVta.getEstadoCode());
                        obj.setReferente(prospectoVta.getReferente());
                        obj.setEstado(3);
                        obj.setIdProspecto(prospectoVta.getIdProspecto());
                        ProspectoVtaDb.insert(db,obj);
                    }
                    //Log.d(TAG,"Cantidad de prospectos DB Insert:"+ProspectoVtaDb.getProspectoInsert(db).size());

                    Log.d(TAG,"Cantidad de prospectos DB Totales idAgente:"+ProspectoVtaDb.getAll(db).size());
                }
                else{
                    Log.d(TAG,"no hay prospectos = 0...");
                }
            }else{
                Log.d(TAG,"prospectos == null...");
            }
        } finally {
            webService.close();
        }
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncInserts(DataBase db) throws JSONException {
        WebService webService = new WebService("MartketForce","InsertarProspectoVta");
        boolean failed = false;
        Log.d(TAG,"Iniciar WS executeInsertProspectSync...");
        List<rp3.auna.models.ventanueva.ProspectoVtaDb> prospectoVtaDbs = rp3.auna.models.ventanueva.ProspectoVtaDb.getProspectoInsert(db);
        if(prospectoVtaDbs.size() == 0){
            Log.d(TAG,"prospectoVtaDbs.size() == 0...");
            return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
        }
        Log.d(TAG,"Prospecto Insertados a enviar:"+prospectoVtaDbs.size());
        /*Collections.sort(prospectoVtaDbs, new Comparator<ProspectoVtaDb>() {
            @Override
            public int compare(ProspectoVtaDb o1, ProspectoVtaDb o2) {
                return (o1.getIdAgente() - o2.getIdAgente());
            }
        });*/
        JSONArray array = new JSONArray();
        for(int i = 0;i<prospectoVtaDbs.size();i++){
            rp3.auna.bean.ProspectoVta obj = new rp3.auna.bean.ProspectoVta();
            ProspectoVtaDb inf = prospectoVtaDbs.get(i);
            Log.d(TAG,"inf:"+inf.toString());
            obj.setEmail(inf.getEmail());
            obj.setLlamadaReferido(inf.getLlamadaReferido());
            obj.setVisitaReferido(inf.getVisitaReferido());
            obj.setIdAgente(inf.getIdAgente());
            obj.setTipoPersonaCode(inf.getTipoPersonaCode());
            obj.setDireccion1(inf.getDireccion1());
            obj.setDireccion2(inf.getDireccion2());
            obj.setNombre(inf.getNombre());
            obj.setRuc(inf.getRuc());
            obj.setCelular(inf.getCelular());
            obj.setContactoApellidoMaterno(inf.getContactoApellidoMaterno());
            obj.setContactoApellidoPaterno(inf.getContactoApellidoPaterno());
            obj.setContactoNombre(inf.getContactoNombre());
            obj.setDocumento(inf.getDocumento());
            obj.setTipoDocumento(inf.getTipoDocumento());
            obj.setContactoTelefono(inf.getContactoTelefono());
            obj.setNombres(inf.getNombres());
            obj.setApellidoPaterno(inf.getApellidoPaterno());
            obj.setApellidoMaterno(inf.getApellidoMaterno());
            obj.setEmpresaTelefono(inf.getEmpresaTelefono());
            obj.setRazonSocial(inf.getRazonSocial());
            obj.setTelefono(inf.getTelefono());
            obj.setOrigenCode(inf.getOrigenCode());
            obj.setEstadoCode(inf.getEstadoCode());
            JSONObject object = new JSONObject();
            object.put("IdProspectom",0);
            object.put("Nombre",obj.getNombre());
            object.put("Celular",obj.getCelular());
            object.put("Telefono",obj.getTelefono());
            object.put("TipoDocumento",obj.getTipoDocumento());
            object.put("Documento",obj.getDocumento());
            object.put("Direccion1",obj.getDireccion1());
            object.put("Direccion2",obj.getDireccion2());
            object.put("TipoPersonaCode",obj.getTipoPersonaCode());
            object.put("Ruc",obj.getRuc());
            object.put("RazonSocial",obj.getRazonSocial());
            object.put("Nombres",obj.getNombres());
            object.put("ApellidoPaterno",obj.getApellidoPaterno());
            object.put("ApellidoMaterno",obj.getApellidoMaterno());
            object.put("ContactoNombre",obj.getContactoNombre());
            object.put("ContactoApellidoPaterno",obj.getContactoApellidoPaterno());
            object.put("ContactoApellidoMaterno",obj.getContactoApellidoMaterno());
            object.put("ContactoTelefono",obj.getContactoTelefono());
            object.put("EmpresaTelefono",obj.getEmpresaTelefono());
            object.put("Email",obj.getEmail());
            object.put("LlamadaReferido",obj.getLlamadaReferido());
            object.put("VisitaReferido",obj.getVisitaReferido());
            object.put("IdAgente",obj.getIdAgente());
            object.put("OrigenCode",obj.getOrigenCode());
            object.put("EstadoCode",obj.getEstadoCode());
            object.put("Referente",obj.getReferente());
            array.put(object);
        }

            webService.addParameter("model", array);
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
                    Log.d(TAG,"..."+e.getMessage());
                    failed = true;
                    return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
                } catch (Exception e) {
                    Log.d(TAG,"Exception e:"+e.getMessage());
                    failed = true;
                    return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
                }
                JSONArray prospectos = webService.getJSONArrayResponse();
                TypeToken<List<rp3.auna.bean.ProspectoVta>> token = new TypeToken<List<rp3.auna.bean.ProspectoVta>>() {};
                List<rp3.auna.bean.ProspectoVta> prospectoVtaList = new Gson().fromJson(prospectos.toString(),token.getType());
                if(prospectos!=null){
                    if(prospectos.length()>0){
                        Log.d(TAG,"cantidad de prospectos WS:"+prospectos.length());
                        Log.d(TAG,"cantidad de prospecto BD temp:"+prospectoVtaDbs.size());
                        /*Collections.sort(prospectoVtaDbs, new Comparator<ProspectoVtaDb>() {
                            @Override
                            public int compare(ProspectoVtaDb o1, ProspectoVtaDb o2) {
                                return (int) (o1.getID() - o2.getID());
                            }
                        });*/
                        //prospectoVtaDbs
                        //ProspectoVtaDb.deleteProspectos(db);
                        for(int i=0;i<prospectoVtaDbs.size();i++){
                            ProspectoVtaDb obj = prospectoVtaDbs.get(i);
                            Log.d(TAG,"Obj Antes:"+obj.toString());
                            Log.d(TAG,"Id en orden:"+prospectoVtaList.get(i).getIdProspecto());
                            Log.d(TAG,prospectoVtaList.get(i).toString());
                            obj.setEmail(prospectoVtaList.get(i).getEmail());
                            obj.setLlamadaReferido(prospectoVtaList.get(i).getLlamadaReferido());
                            obj.setVisitaReferido(prospectoVtaList.get(i).getVisitaReferido());
                            obj.setIdAgente(prospectoVtaList.get(i).getIdAgente());
                            obj.setTipoPersonaCode(prospectoVtaList.get(i).getTipoPersonaCode());
                            obj.setContactoTelefono(prospectoVtaList.get(i).getContactoTelefono());
                            obj.setContactoApellidoMaterno(prospectoVtaList.get(i).getContactoApellidoMaterno());
                            obj.setContactoApellidoPaterno(prospectoVtaList.get(i).getContactoApellidoPaterno());
                            obj.setContactoNombre(prospectoVtaList.get(i).getContactoNombre());
                            obj.setEmpresaTelefono(prospectoVtaList.get(i).getEmpresaTelefono());
                            obj.setRuc(prospectoVtaList.get(i).getRuc());
                            obj.setRazonSocial(prospectoVtaList.get(i).getRazonSocial());
                            obj.setNombres(prospectoVtaList.get(i).getNombres());
                            obj.setApellidoPaterno(prospectoVtaList.get(i).getApellidoPaterno());
                            obj.setApellidoMaterno(prospectoVtaList.get(i).getApellidoMaterno());
                            obj.setTipoDocumento(prospectoVtaList.get(i).getTipoDocumento());
                            obj.setDocumento(prospectoVtaList.get(i).getDocumento());
                            obj.setDireccion2(prospectoVtaList.get(i).getDireccion2());
                            obj.setDireccion1(prospectoVtaList.get(i).getDireccion1());
                            obj.setTelefono(prospectoVtaList.get(i).getTelefono());
                            obj.setCelular(prospectoVtaList.get(i).getCelular());
                            obj.setNombre(prospectoVtaList.get(i).getNombre());
                            obj.setOrigenCode(prospectoVtaList.get(i).getOrigenCode());
                            obj.setReferente(prospectoVtaList.get(i).getReferente());
                            obj.setEstado(3);
                            obj.setEstadoCode(prospectoVtaList.get(i).getEstadoCode());
                            obj.setIdProspecto(prospectoVtaList.get(i).getIdProspecto());
                            Log.d(TAG,"Obj Despues:"+obj.toString());
                            ProspectoVtaDb.update(db,obj);
                            //AgendaVta agendaVta = AgendaVta.getAgendaBDByProspectoBD(db,(int)obj.getID());
                            List<rp3.auna.models.ventanueva.VisitaVta> visitaVta = rp3.auna.models.ventanueva.VisitaVta.getVisitasfromProspectoBD(db,(int)obj.getID());
                            List<LlamadaVta> llamadaVta = LlamadaVta.getLlamadasFromProspectoBD(db,(int)obj.getID());
                            if(visitaVta.size()>0){
                                Log.d(TAG,"Si hay visitas temporales con este Id Prospecto temporal...");
                                for(rp3.auna.models.ventanueva.VisitaVta v:visitaVta){
                                    v.setIdCliente(obj.getIdProspecto());
                                    boolean res = VisitaVta.update(db,v);
                                    Log.d(TAG,res?"Si actualizo visita con IdProspecto de servidor":"No se actualizo visita con IdProspecto de servidor");
                                }
                            }
                            if(llamadaVta.size()>0){
                                Log.d(TAG,"Si hay llamadas temporales con este Id Prospecto temporal...");
                                for(LlamadaVta l:llamadaVta){
                                    l.setIdCliente(obj.getIdProspecto());
                                    boolean res = LlamadaVta.update(db,l);
                                    Log.d(TAG,res?"Si actualizo llamada con IdProspecto de servidor":"No se actualizo llamada con IdProspecto de servidor");
                                }
                            }
                        }
                        //Log.d(TAG,"Cantidad de prospectos DB Insert:"+ProspectoVtaDb.getProspectoInsert(db).size());
                        Log.d(TAG,"Cantidad de prospectos DB Totales:"+ProspectoVtaDb.getAll(db).size());
                        Log.d(TAG,"Cantidad de prospectos DB Insertados:"+ ProspectoVtaDb.getProspectoInsert(db).size());
                    }
                    else{
                        Log.d(TAG,"no hay prospectos desde WS = 0...");
                    }
                }else{
                    Log.d(TAG,"prospectos desde WS == null...");
                }
            } finally {
                Log.d(TAG,"finally ws prospecto");
                webService.close();
            }
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncSincronizada(DataBase db) throws JSONException {
        WebService webService = new WebService("MartketForce","ActualizarProspectoVta");
        boolean failed = false;
        Log.d(TAG,"Iniciar WS executeInsertProspectSincronizadaSync...");
        List<rp3.auna.models.ventanueva.ProspectoVtaDb> prospectoVtaDbs = rp3.auna.models.ventanueva.ProspectoVtaDb.getProspectoSincronizadas(db);
        if(prospectoVtaDbs.size() == 0){
            Log.d(TAG,"prospectoVtaDbs.size() == 0...");
            return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
        }
        Log.d(TAG,"Prospecto Sincronizados a enviar:"+prospectoVtaDbs.size());
        /*Collections.sort(prospectoVtaDbs, new Comparator<ProspectoVtaDb>() {
            @Override
            public int compare(ProspectoVtaDb o1, ProspectoVtaDb o2) {
                return (o1.getIdAgente() - o2.getIdAgente());
            }
        });*/
        JSONArray array = new JSONArray();
        for(int i = 0;i<prospectoVtaDbs.size();i++){
            rp3.auna.bean.ProspectoVta obj = new rp3.auna.bean.ProspectoVta();
            ProspectoVtaDb inf = prospectoVtaDbs.get(i);
            obj.setIdProspecto(inf.getIdProspecto());
            obj.setEmail(inf.getEmail());
            obj.setLlamadaReferido(inf.getLlamadaReferido());
            obj.setVisitaReferido(inf.getVisitaReferido());
            obj.setIdAgente(inf.getIdAgente());
            obj.setTipoPersonaCode(inf.getTipoPersonaCode());
            obj.setDireccion1(inf.getDireccion1());
            obj.setDireccion2(inf.getDireccion2());
            obj.setNombre(inf.getNombre());
            obj.setRuc(inf.getRuc());
            obj.setCelular(inf.getCelular());
            obj.setContactoApellidoMaterno(inf.getContactoApellidoMaterno());
            obj.setContactoApellidoPaterno(inf.getContactoApellidoPaterno());
            obj.setContactoNombre(inf.getContactoNombre());
            obj.setDocumento(inf.getDocumento());
            obj.setTipoDocumento(inf.getTipoDocumento());
            obj.setContactoTelefono(inf.getContactoTelefono());
            obj.setNombres(inf.getNombres());
            obj.setApellidoPaterno(inf.getApellidoPaterno());
            obj.setApellidoMaterno(inf.getApellidoMaterno());
            obj.setEmpresaTelefono(inf.getEmpresaTelefono());
            obj.setRazonSocial(inf.getRazonSocial());
            obj.setTelefono(inf.getTelefono());
            obj.setOrigenCode(inf.getOrigenCode());
            obj.setEstadoCode(inf.getEstadoCode());
            obj.setReferente(inf.getReferente());
            JSONObject object = new JSONObject();
            object.put("IdProspecto",obj.getIdProspecto());
            object.put("Nombre",obj.getNombre());
            object.put("Celular",obj.getCelular());
            object.put("Telefono",obj.getTelefono());
            object.put("TipoDocumento",obj.getTipoDocumento());
            object.put("Documento",obj.getDocumento());
            object.put("Direccion1",obj.getDireccion1());
            object.put("Direccion2",obj.getDireccion2());
            object.put("TipoPersonaCode",obj.getTipoPersonaCode());
            object.put("Ruc",obj.getRuc());
            object.put("RazonSocial",obj.getRazonSocial());
            object.put("Nombres",obj.getNombres());
            object.put("ApellidoPaterno",obj.getApellidoPaterno());
            object.put("ApellidoMaterno",obj.getApellidoMaterno());
            object.put("ContactoNombre",obj.getContactoNombre());
            object.put("ContactoApellidoPaterno",obj.getContactoApellidoPaterno());
            object.put("ContactoApellidoMaterno",obj.getContactoApellidoMaterno());
            object.put("ContactoTelefono",obj.getContactoTelefono());
            object.put("EmpresaTelefono",obj.getEmpresaTelefono());
            object.put("Email",obj.getEmail());
            object.put("LlamadaReferido",obj.getLlamadaReferido());
            object.put("VisitaReferido",obj.getVisitaReferido());
            object.put("IdAgente",obj.getIdAgente());
            object.put("OrigenCode",obj.getOrigenCode());
            object.put("EstadoCode",obj.getEstadoCode());
            object.put("Referente",obj.getReferente());
            array.put(object);
        }

        webService.addParameter("model", array);
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
                Log.d(TAG,"..."+e.getMessage());
                failed = true;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                Log.d(TAG,"Exception e:"+e.getMessage());
                failed = true;
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }
            //ProspectoVtaDb.deleteAll(db, Contract.ProspectoVta.TABLE_NAME);(db);
            //ProspectoVtaDb.deleteAll(db,Contract.ProspectoVta.TABLE_NAME);
        } finally {
            Log.d(TAG,"finally ws prospecto");
            ProspectoVtaDb.deleteAll(db, Contract.ProspectoVta.TABLE_NAME);
            webService.close();
        }
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncRobinson(DataBase db) throws JSONException{
        WebService webService = new WebService("MartketForce","ListaNegraProspectoVta");
        boolean failed = false;
        Log.d(TAG,"Iniciar WS executeSyncRobinson...");
        List<rp3.auna.models.ventanueva.ProspectoVtaDb> prospectoVtaDbs = rp3.auna.models.ventanueva.ProspectoVtaDb.getProspectoListaNegra(db);
        if(prospectoVtaDbs.size() == 0){
            Log.d(TAG,"prospectoVtaDbs.size() == 0...No hay prospectos en lista negra para enviar...");
            return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
        }
        Log.d(TAG,"Cantidad de Prospectos en Lista Negra a enviar:"+prospectoVtaDbs.size());

        JSONArray array = new JSONArray();
        for(int i = 0;i<prospectoVtaDbs.size();i++){
            rp3.auna.bean.ProspectoVta obj = new rp3.auna.bean.ProspectoVta();
            ProspectoVtaDb inf = prospectoVtaDbs.get(i);
            obj.setIdProspecto(inf.getIdProspecto());
            obj.setEmail(inf.getEmail());
            obj.setLlamadaReferido(inf.getLlamadaReferido());
            obj.setVisitaReferido(inf.getVisitaReferido());
            obj.setIdAgente(inf.getIdAgente());
            obj.setTipoPersonaCode(inf.getTipoPersonaCode());
            obj.setDireccion1(inf.getDireccion1());
            obj.setDireccion2(inf.getDireccion2());
            obj.setNombre(inf.getNombre());
            obj.setRuc(inf.getRuc());
            obj.setCelular(inf.getCelular());
            obj.setContactoApellidoMaterno(inf.getContactoApellidoMaterno());
            obj.setContactoApellidoPaterno(inf.getContactoApellidoPaterno());
            obj.setContactoNombre(inf.getContactoNombre());
            obj.setDocumento(inf.getDocumento());
            obj.setTipoDocumento(inf.getTipoDocumento());
            obj.setContactoTelefono(inf.getContactoTelefono());
            obj.setNombres(inf.getNombres());
            obj.setApellidoPaterno(inf.getApellidoPaterno());
            obj.setApellidoMaterno(inf.getApellidoMaterno());
            obj.setEmpresaTelefono(inf.getEmpresaTelefono());
            obj.setRazonSocial(inf.getRazonSocial());
            obj.setTelefono(inf.getTelefono());
            obj.setOrigenCode(inf.getOrigenCode());
            obj.setEstadoCode(inf.getEstadoCode());
            obj.setReferente(inf.getReferente());
            JSONObject object = new JSONObject();
            object.put("IdProspecto",obj.getIdProspecto());
            object.put("Nombre",obj.getNombre());
            object.put("Celular",obj.getCelular());
            object.put("Telefono",obj.getTelefono());
            object.put("TipoDocumento",obj.getTipoDocumento());
            object.put("Documento",obj.getDocumento());
            object.put("Direccion1",obj.getDireccion1());
            object.put("Direccion2",obj.getDireccion2());
            object.put("TipoPersonaCode",obj.getTipoPersonaCode());
            object.put("Ruc",obj.getRuc());
            object.put("RazonSocial",obj.getRazonSocial());
            object.put("Nombres",obj.getNombres());
            object.put("ApellidoPaterno",obj.getApellidoPaterno());
            object.put("ApellidoMaterno",obj.getApellidoMaterno());
            object.put("ContactoNombre",obj.getContactoNombre());
            object.put("ContactoApellidoPaterno",obj.getContactoApellidoPaterno());
            object.put("ContactoApellidoMaterno",obj.getContactoApellidoMaterno());
            object.put("ContactoTelefono",obj.getContactoTelefono());
            object.put("EmpresaTelefono",obj.getEmpresaTelefono());
            object.put("Email",obj.getEmail());
            object.put("LlamadaReferido",obj.getLlamadaReferido());
            object.put("VisitaReferido",obj.getVisitaReferido());
            object.put("IdAgente",obj.getIdAgente());
            object.put("OrigenCode",obj.getOrigenCode());
            object.put("EstadoCode",obj.getEstadoCode());
            object.put("Referente",obj.getReferente());
            array.put(object);
        }

        webService.addParameter("model", array);
        try {
            webService.addCurrentAuthToken();

            try {
                webService.setTimeOut(40000);
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED){
                    Log.d(TAG,"e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED...");
                    Log.d(TAG,"STATUScODE = "+e.getStatusCode());
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
            Log.d(TAG,"finally ws prospecto lista negra");
            ProspectoVtaDb.deleteAll(db, Contract.ProspectoVta.TABLE_NAME);
            webService.close();
        }
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
