package rp3.auna.sync;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.db.Contract;
import rp3.auna.models.ventanueva.ComisionesVta;
import rp3.auna.models.ventanueva.FotoVisitaVta;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.sync.GeneralValue;
import rp3.util.Convert;

/**
 * Created by Jesus Villa on 22/03/2018.
 */

public class SyncServicio {
    private static final String TAG = GeneralValue.class.getSimpleName();

    public static int executeSync(DataBase db, Context context) throws JSONException {
        WebService webService = new WebService("MarketForce","SyncService");
        Log.d(TAG,"Iniciar WS Sincronizar...");
        //Asignar modelo a enviar
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ApplicationId","AGENDACOMERCIAL");
        JSONObject gcm = new JSONObject();
        gcm.put("AuthId", PreferenceManager.getString("TokenId"));
        jsonObject.put("GcmId",gcm);
        Calendar calendar = Calendar.getInstance();
        long fecha = Convert.getDotNetTicksFromDate(calendar.getTime());
        jsonObject.put("Fecha",fecha);
        webService.addParameter("model",jsonObject);
        try
        {
            webService.addCurrentAuthToken();
            try {
                webService.setTimeOut(60000);
                webService.invokeWebService();
                JSONObject response = webService.getJSONObjectResponse();
                if(response!=null){
                    Log.d(TAG,"response!=null...");
                    //region Verificar GeneralValues
                    JSONArray gvsGeneralValues = response.getJSONArray("GeneralValues");
                    Log.d(TAG,"Cantidad de values:"+gvsGeneralValues.length());
                    if(gvsGeneralValues.length()>0){
                        rp3.data.models.GeneralValue.deleteAll(db, rp3.data.models.Contract.GeneralValue.TABLE_NAME);
                        TypeToken<List<rp3.util.GeneralValue>> typeToken = new TypeToken<List<rp3.util.GeneralValue>>(){};
                        List<rp3.util.GeneralValue> list = new Gson().fromJson(gvsGeneralValues.toString(),typeToken.getType());
                        for(rp3.util.GeneralValue gv:list){
                            if(gv.getReference10()!=null){
                                if(gv.getReference10().equalsIgnoreCase("Rp3AunaApp")){
                                    rp3.data.models.GeneralValue model = new rp3.data.models.GeneralValue();
                                    model.setGeneralTableId(gv.getId());
                                    model.setCode(gv.getCode());
                                    model.setValue(gv.getContent());
                                    if(gv.getReference01()!=null){
                                        model.setReference1(gv.getReference01());
                                    }else{
                                        model.setReference1("");
                                    }
                                    if(gv.getReference02()!=null){
                                        model.setReference2(gv.getReference02());
                                    }else{
                                        model.setReference2("");
                                    }
                                    if(gv.getReference03()!=null){
                                        model.setReference3(gv.getReference03());
                                    }else{
                                        model.setReference3("");
                                    }
                                    if(gv.getReference04()!=null){
                                        model.setReference4(gv.getReference04());
                                    }else{
                                        model.setReference4("");
                                    }
                                    if(gv.getReference05()!=null){
                                        model.setReference5(gv.getReference05());
                                    }else{
                                        model.setReference5("");
                                    }

                                    rp3.data.models.GeneralValue.insert(db, model);
                                }
                            }

                        }
                    }else{
                        return  SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                    }
                    //endregion

                    //region Verificar Identificaciones
                    JSONArray gvsIdentification = response.getJSONArray("IdentificationTypes");
                    if(gvsIdentification==null){
                        return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                    }
                    if(gvsIdentification.length()==0){
                        return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                    }
                    rp3.data.models.IdentificationType.deleteAll(db, rp3.data.models.Contract.IdentificationType.TABLE_NAME);

                    for(int i=0; i < gvsIdentification.length(); i++){

                        try {
                            JSONObject gv = gvsIdentification.getJSONObject(i);

                            rp3.data.models.IdentificationType model = new rp3.data.models.IdentificationType();
                            model.setID(gv.getLong("IdentificationTypeId"));
                            model.setName(gv.getString("Name"));
                            model.setApplyNaturalPersonOnly(gv.getBoolean("ApplyNaturalPersonOnly"));
                            if (!gv.isNull("UseCustomValidator")) {
                                model.setUseCustomValidator(gv.getBoolean("UseCustomValidator"));
                            } else {
                                model.setUseCustomValidator(false);
                            }
                            if (!gv.isNull("Length")) {
                                model.setLenght(gv.getInt("Length"));
                            } else {
                                model.setLenght(0);
                            }
                            if (!gv.isNull("MaskType")) {
                                model.setMaskType(gv.getInt("MaskType"));
                            } else {
                                model.setMaskType(0);
                            }
                            if (!gv.isNull("Mask")) {
                                model.setMask(gv.getString("Mask"));
                            } else {
                                model.setMask(null);
                            }
                            if (!gv.isNull("RegExValidator")) {
                                model.setRegExValidator(gv.getString("RegExValidator"));
                            } else {
                                model.setRegExValidator(null);
                            }

                            Log.d(TAG,model.toString());
                            rp3.data.models.IdentificationType.insert(db, model);

                        } catch (JSONException e) {

                            return SyncAdapter.SYNC_EVENT_ERROR;
                        }

                    }
                    //endregion

                    //region Verificar ApplicationParameters
                    JSONArray parametersApplication = response.getJSONArray("ApplicationParameters");
                    Log.d(TAG,"cantidad de applicationParameter:"+parametersApplication.length());
                    if(parametersApplication.length()>0) {
                        rp3.auna.models.ApplicationParameter.deleteAll(db,rp3.auna.db.Contract.ApplicationParameter.TABLE_NAME);
                        for (int i = 0; i < parametersApplication.length(); i++) {
                            JSONObject llamada = parametersApplication.getJSONObject(i);
                            /**
                             * Obtener datos de Llamada
                             */
                            rp3.auna.models.ApplicationParameter llamadaVtaDb = new rp3.auna.models.ApplicationParameter();
                            if (!llamada.isNull("ApplicationId")) {
                                llamadaVtaDb.setApplicationId(llamada.getString("ApplicationId"));
                            } else {
                                llamadaVtaDb.setApplicationId(null);
                            }
                            if (!llamada.isNull("ParameterId")) {
                                llamadaVtaDb.setParameterId(llamada.getString("ParameterId"));
                            } else {
                                llamadaVtaDb.setParameterId(null);
                            }
                            if (!llamada.isNull("Value")) {
                                llamadaVtaDb.setValue(llamada.getString("Value"));
                            } else {
                                llamadaVtaDb.setValue(null);
                            }
                            if (!llamada.isNull("Label")) {
                                llamadaVtaDb.setLabel((llamada.getString("Label")));
                            } else {
                                llamadaVtaDb.setLabel(null);
                            }
                            if (!llamada.isNull("Description")) {
                                llamadaVtaDb.setDescription((llamada.getString("Description")));
                            } else {
                                llamadaVtaDb.setDescription(null);
                            }

                            boolean insertLlamada = rp3.auna.models.ApplicationParameter.insert(db, llamadaVtaDb);
                            Log.d(TAG,insertLlamada?"parameter insertada...":"parameter no fue insertada...");
                        }
                        Log.d(TAG,"Cantidad de parameter:"+ rp3.auna.models.ApplicationParameter.getAll(db).size());
                    }else{
                        return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                    }
                    //endregion

                    //region Verificar Agente propio
                    JSONObject agente = response.getJSONObject("Agente");
                    if(agente!=null){
                        Log.d(TAG,"getAgente:"+agente.toString());
                        if(!agente.isNull(Contants.KEY_IDAGENTE)){
                            PreferenceManager.setValue(Contants.KEY_IDAGENTE, agente.getInt(Contants.KEY_IDAGENTE));
                        }else{
                            return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                        }

                        if(!agente.isNull(Contants.KEY_IDRUTA))
                            PreferenceManager.setValue(Contants.KEY_IDRUTA, agente.getInt(Contants.KEY_IDRUTA));
                        if(!agente.isNull(Contants.KEY_FOTO))
                            PreferenceManager.setValue(Contants.KEY_FOTO, agente.getString(Contants.KEY_FOTO));
                        PreferenceManager.setValue(Contants.KEY_ES_SUPERVISOR, agente.getBoolean(Contants.KEY_ES_SUPERVISOR));
                        if(!agente.isNull(Contants.KEY_ID_SUPERVISOR))
                            PreferenceManager.setValue(Contants.KEY_ID_SUPERVISOR, agente.getInt(Contants.KEY_ID_SUPERVISOR));
                        PreferenceManager.setValue(Contants.KEY_ES_AGENTE, agente.getBoolean(Contants.KEY_ES_AGENTE));
                        PreferenceManager.setValue(Contants.KEY_ES_ADMINISTRADOR, agente.getBoolean(Contants.KEY_ES_ADMINISTRADOR));
                        PreferenceManager.setValue(Contants.KEY_CARGO, agente.getString(Contants.KEY_CARGO));
                        if(!agente.isNull(Contants.KEY_DESCUENTO_MAXIMO))
                            PreferenceManager.setValue(Contants.KEY_DESCUENTO_MAXIMO, agente.getInt(Contants.KEY_DESCUENTO_MAXIMO));
                    }else{
                        return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                    }
                    //endregion

                    //region Verificar Parametro
                    JSONObject parametro = response.getJSONObject("Parametros");
                    if(parametro.isNull(Contants.KEY_ALARMA_INTERVALO)){
                        return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                    }
                    PreferenceManager.setValue(Contants.KEY_ALARMA_INICIO, Convert.getDateFromDotNetTicks(parametro.getLong(Contants.KEY_ALARMA_INICIO)));
                    PreferenceManager.setValue(Contants.KEY_ALARMA_FIN, Convert.getDateFromDotNetTicks(parametro.getLong(Contants.KEY_ALARMA_FIN)));
                    PreferenceManager.setValue(Contants.KEY_ALARMA_INTERVALO, parametro.getInt(Contants.KEY_ALARMA_INTERVALO));
                    PreferenceManager.setValue(Contants.KEY_PREFIJO_TELEFONICO, parametro.getString(Contants.KEY_PREFIJO_TELEFONICO));
                    PreferenceManager.setValue(Contants.KEY_AGENTE_UBICACION_1, parametro.getInt(Contants.KEY_AGENTE_UBICACION_1));
                    PreferenceManager.setValue(Contants.KEY_AGENTE_UBICACION_2, parametro.getInt(Contants.KEY_AGENTE_UBICACION_2));
                    PreferenceManager.setValue(Contants.KEY_AGENTE_UBICACION_3, parametro.getInt(Contants.KEY_AGENTE_UBICACION_3));
                    if(!parametro.isNull(Contants.KEY_MODULO_OPORTUNIDADES))
                        PreferenceManager.setValue(Contants.KEY_MODULO_OPORTUNIDADES, parametro.getBoolean(Contants.KEY_MODULO_OPORTUNIDADES));
                    if(!parametro.isNull(Contants.KEY_MODULO_MARCACIONES))
                        PreferenceManager.setValue(Contants.KEY_MODULO_MARCACIONES, parametro.getBoolean(Contants.KEY_MODULO_MARCACIONES));
                    if(!parametro.isNull(Contants.KEY_MODULO_POS))
                        PreferenceManager.setValue(Contants.KEY_MODULO_POS, parametro.getBoolean(Contants.KEY_MODULO_POS));
                    if(!parametro.isNull(Contants.KEY_MARACIONES_DISTANCIA))
                        PreferenceManager.setValue(Contants.KEY_MARACIONES_DISTANCIA, parametro.getDouble(Contants.KEY_MARACIONES_DISTANCIA) + "");
                    //endregion

                    //region Verificar Device
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
                    //endregion

                    //region Verificar SyncAgentes
                    JSONArray agentes = response.getJSONArray("Agentes");
                    if(agentes==null){
                        return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                    }
                    if(agentes.length()==0)
                        return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                    Log.d(TAG,"getAgenteS:"+agentes.toString());
                    rp3.auna.models.Agente.deleteAll(db, Contract.Agente.TABLE_NAME);

                    for (int i = 0; i < agentes.length(); i++) {
                        try {
                            JSONObject type = agentes.getJSONObject(i);
                            rp3.auna.models.Agente agente1 = new rp3.auna.models.Agente();

                            agente1.setIdAgente(type.getInt("IdAgente"));
                            agente1.setNombre(type.getString("Nombre"));
                            if(!type.isNull("Telefono"))
                                agente1.setTelefono(type.getString("Telefono"));
                            if(!type.isNull("Email"))
                                agente1.setEmail(type.getString("Email"));

                            rp3.auna.models.Agente.insert(db, agente1);
                        } catch (JSONException e) {
                            Log.e("Error", e.toString());
                            return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
                        }
                    }
                    //endregion

                    //region Verificar Comisiones
                    JSONObject comision = response.getJSONObject("Asesor");
                    if(comision!=null){
                        Log.d(TAG,"jObject!=null...");
                        ComisionesVta.deleteAll(db, Contract.ComisionAgente.TABLE_NAME);
                        Log.d(TAG,"despues deleteAll...");
                        ComisionesVta comisionesVta = new ComisionesVta();
                        comisionesVta.setIdAgente(comision.getInt("IdAgente"));
                        comisionesVta.setPkAsesor(comision.getInt("PkAsesor"));
                        comisionesVta.setNombreAsesor(comision.getString("NombreAsesor"));
                        comisionesVta.setVentas(String.valueOf(comision.getInt("Ventas")));
                        comisionesVta.setComision(String.valueOf(comision.getDouble("Comision")));
                        comisionesVta.setIncentivo(String.valueOf(comision.getDouble("Incentivo")));
                        ComisionesVta.insert(db, comisionesVta);
                        Log.d(TAG,"despues del insert comision...");
                    }else{
                        Log.d(TAG,"comision==null...");
                    }
                    //endregion

                    //region Verificar Prospecto
                    JSONArray prospectos = response.getJSONArray("Prospectos");
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
                    //endregion

                    //region Verificar Llamada
                    JSONArray llamadas = response.getJSONArray("Llamadas");
                    Log.d(TAG,"cantidad de llamadas:"+llamadas.length());
                    if(llamadas.length()>0) {
                        Log.d(TAG,"llamadas.length>0");
                        rp3.auna.models.ventanueva.LlamadaVta.deleteAll(db,Contract.LlamadaVta.TABLE_NAME,true);
                        Log.d(TAG,"despues de eliminar llamadas...");

                        /***************************************************************************/
                        for (int i = 0; i < llamadas.length(); i++) {
                            JSONObject llamada = llamadas.getJSONObject(i);
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
                        }
                        Log.d(TAG,"Cantidad de llamadas:"+ rp3.auna.models.ventanueva.LlamadaVta.getLlamadasAll(db).size());
                    }
                    //endregion

                    //region Verificar Visita
                    JSONArray visitas = response.getJSONArray("Visitas");
                    Log.d(TAG,"cantidad de visitas para insertar en agenda:"+visitas.length());
                    if(visitas.length()>0) {
                        rp3.auna.models.ventanueva.VisitaVta.deleteAll(db,Contract.VisitaVta.TABLE_NAME,true);
                        rp3.auna.models.ventanueva.FotoVisitaVta.deleteAll(db,Contract.FotoVisitaVta.TABLE_NAME,true);
                        for (int i = 0; i < visitas.length(); i++) {
                            JSONObject visita = visitas.getJSONObject(i);
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
                        }
                        Log.d(TAG,"Cantidad totales de visitas:"+ rp3.auna.models.ventanueva.VisitaVta.getAll(db).size());
                    }
                    //endregion
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

    public static int executeSyncAgenda(DataBase db,Context context,long fecha)throws JSONException{
        WebService webService = new WebService("MarketForce","SyncAgenda");
        Log.d(TAG,"Iniciar WS Agendas totales...");
        try{
            Log.d(TAG,"idAgente="+ PreferenceManager.getInt(Contants.KEY_IDAGENTE,0));
            if(PreferenceManager.getInt(Contants.KEY_IDAGENTE,0)==0){
                Log.d(TAG,"Error en el ID AGENTE = 0...");
                return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
            }
            webService.addParameter("@idagente", PreferenceManager.getInt(Contants.KEY_IDAGENTE));
            webService.addParameter("@fecha", fecha);
            webService.addCurrentAuthToken();
            try{
                webService.setTimeOut(60000);
                webService.invokeWebService();
                JSONObject response = webService.getJSONObjectResponse();
                if(response!=null){
                    Log.d(TAG,"response!=null...");

                    //region Verificar Prospecto
                    JSONArray prospectos = response.getJSONArray("Prospectos");
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
                    //endregion

                    //region Verificar Llamada
                    JSONArray llamadas = response.getJSONArray("Llamadas");
                    Log.d(TAG,"cantidad de llamadas:"+llamadas.length());
                    if(llamadas.length()>0) {
                        Log.d(TAG,"llamadas.length>0");
                        rp3.auna.models.ventanueva.LlamadaVta.deleteAll(db,Contract.LlamadaVta.TABLE_NAME,true);
                        Log.d(TAG,"despues de eliminar llamadas...");

                        /***************************************************************************/
                        for (int i = 0; i < llamadas.length(); i++) {
                            JSONObject llamada = llamadas.getJSONObject(i);
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
                        }
                        Log.d(TAG,"Cantidad de llamadas:"+ rp3.auna.models.ventanueva.LlamadaVta.getLlamadasAll(db).size());
                    }
                    //endregion

                    //region Verificar Visita
                    JSONArray visitas = response.getJSONArray("Visitas");
                    Log.d(TAG,"cantidad de visitas para insertar en agenda:"+visitas.length());
                    if(visitas.length()>0) {
                        rp3.auna.models.ventanueva.VisitaVta.deleteAll(db,Contract.VisitaVta.TABLE_NAME,true);
                        rp3.auna.models.ventanueva.FotoVisitaVta.deleteAll(db,Contract.FotoVisitaVta.TABLE_NAME,true);
                        for (int i = 0; i < visitas.length(); i++) {
                            JSONObject visita = visitas.getJSONObject(i);
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
                        }
                        Log.d(TAG,"Cantidad totales de visitas:"+ rp3.auna.models.ventanueva.VisitaVta.getAll(db).size());
                    }
                    //endregion
                }else{
                    Log.d(TAG,"response == null...");
                }

            }catch (HttpResponseException e) {
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
}
