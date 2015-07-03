package rp3.marketforce.sync;

import java.util.Date;

import rp3.configuration.PreferenceManager;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.ServerActivity;
import rp3.marketforce.cliente.CrearClienteFragment;
import rp3.marketforce.models.Tarea;
import rp3.marketforce.ruta.CrearVisitaFragment;
import rp3.marketforce.ruta.MotivoNoVisitaFragment;
import rp3.marketforce.ruta.RutasDetailFragment;
import rp3.marketforce.ruta.RutasListFragment;
import rp3.sync.SyncAudit;
import rp3.sync.TestConnection;
import rp3.util.Convert;
import android.accounts.Account;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

public class SyncAdapter extends rp3.content.SyncAdapter {
	
	public static String SYNC_TYPE_GENERAL = "general";
	public static String SYNC_TYPE_ACT_AGENDA = "actagenda";
	public static String SYNC_TYPE_ENVIAR_UBICACION = "sendlocation";
	public static String SYNC_TYPE_CLIENTE_UPDATE = "clienteupdate";
	public static String SYNC_TYPE_CLIENTE_UPDATE_FULL = "clienteupdatefull";
	public static String SYNC_TYPE_CLIENTE_CREATE = "clientecreate";
	public static String SYNC_TYPE_ENVIAR_AGENDA = "sendagenda";
	public static String SYNC_TYPE_ACTUALIZAR_AGENDA = "actagenda";
	public static String SYNC_TYPE_REPROGRAMAR_AGENDA = "reprogramar";
	public static String SYNC_TYPE_INSERTAR_AGENDA = "insertarAgenda";
	public static String SYNC_TYPE_AGENDA_NO_VISITA = "agendaNoVisita";
	public static String SYNC_TYPE_SOLO_RESUMEN = "resumen";
	public static String SYNC_TYPE_SERVER_CODE = "servidor";
	public static String SYNC_TYPE_BATCH = "batch";
	public static String SYNC_TYPE_TODO = "todo";
	public static String SYNC_TYPE_GEOPOLITICAL = "geopolitical";
    public static String SYNC_TYPE_AGENTES_UBICACION= "agentes_ubicacion";
    public static String SYNC_TYPE_AGENDA_GEOLOCATION = "agenda_geolocation";
    public static String SYNC_TYPE_UPLOAD_AGENDAS = "agenda_upload";
    public static String SYNC_TYPE_UPLOAD_CLIENTES = "cliente_upload";
	
	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);		
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {		
		super.onPerformSync(account, extras, authority, provider, syncResult);	
		
		//android.os.Debug.waitForDebugger();
		String syncType = extras.getString(ARG_SYNC_TYPE);
		
		DataBase db = null;		
		int result = 0;
		
		try{
            db = DataBase.newDataBase(rp3.marketforce.db.DbOpenHelper.class);
            if(TestConnection.executeSync()) {

                if (syncType == null || syncType.equals(SYNC_TYPE_GENERAL)) {
                    db.beginTransaction();

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.sync.GeneralValue.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.sync.IdentificationType.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Cliente.executeSync(db);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_CLIENTE_UPDATE, SYNC_EVENT_SUCCESS);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Rutas.executeSync(db, null, null, false);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_ACT_AGENDA, SYNC_EVENT_SUCCESS);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Agente.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS && PreferenceManager.getBoolean(Contants.KEY_ES_SUPERVISOR)) {
                        result = rp3.marketforce.sync.Agente.executeSyncGetAgente(db);
                        addDefaultMessage(result);

                        result = rp3.marketforce.sync.Agente.executeSyncGetUbicaciones(db);
                        addDefaultMessage(result);

                        result = rp3.marketforce.sync.Agente.executeSyncAgentes(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Tareas.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if(result == SYNC_EVENT_SUCCESS){
                        Tarea tar = Tarea.getTareaActualizacion(db);
                        if(tar != null)
                        {
                            result = rp3.marketforce.sync.Tareas.executeSyncTareaActualizacion(db, tar.getIdTarea());
                            addDefaultMessage(result);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Canal.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.TipoCliente.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Agente.executeSyncParametros(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Calendario.executeSync(db);
                        addDefaultMessage(result);
                    }
				/*
				 * Se comenta carga de fotos ya que se la hara mediante un lazy loader.
				 * Para esto se cargara tambien en el modelo Cliente la url de la foto para poder cargarla
				 * */

                    //if(result == SYNC_EVENT_SUCCESS){
                    //	result = rp3.marketforce.sync.ClienteFoto.executeSync(db,null);
                    //	addDefaultMessage(result);
                    //}

                   // long time = 0;
                    //Date pru = SyncAudit.getLastSyncDate(SYNC_TYPE_GEOPOLITICAL, SYNC_EVENT_SUCCESS);
                    //if (Convert.getTicksFromDate(pru) != 0)
                    //    result = rp3.sync.GeopoliticalStructure.executeSyncLastUpdate(db, Convert.getDotNetTicksFromDate(SyncAudit.getLastSyncDate(SYNC_TYPE_GEOPOLITICAL, SYNC_EVENT_SUCCESS)));
                    //else
                    //    result = rp3.sync.GeopoliticalStructure.executeSync(db);
                    //if (result == SYNC_EVENT_SUCCESS) {
                    //    SyncAudit.insert(SYNC_TYPE_GEOPOLITICAL, SYNC_EVENT_SUCCESS);
                    //}
                    //addDefaultMessage(result);

                    db.commitTransaction();

                } else if (syncType.equals(SYNC_TYPE_ENVIAR_UBICACION)) {
                    try {
                        double latitud = extras.getDouble(EnviarUbicacion.ARG_LATITUD);
                        double longitud = extras.getDouble(EnviarUbicacion.ARG_LONGITUD);

                        EnviarUbicacion.executeSync(longitud, latitud);


                    } catch (Exception e) {
                        Log.e("Sync Adapter", e.getMessage());
                    }
                } else if (syncType.equals(SYNC_TYPE_GEOPOLITICAL)) {
                    //long time = 0;
                    //Date pru = SyncAudit.getLastSyncDate(SYNC_TYPE_GEOPOLITICAL, SYNC_EVENT_SUCCESS);
                    //if(Convert.getTicksFromDate(pru) != 0)
                    //	result = rp3.sync.GeopoliticalStructure.executeSyncLastUpdate(db, Convert.getDotNetTicksFromDate(SyncAudit.getLastSyncDate(SYNC_TYPE_GEOPOLITICAL, SYNC_EVENT_SUCCESS)));
                    //else
                    //	result = rp3.sync.GeopoliticalStructure.executeSync(db);
                    //if(result == SYNC_EVENT_SUCCESS){
                    //	SyncAudit.insert(SYNC_TYPE_GEOPOLITICAL, SYNC_EVENT_SUCCESS);
                    //}
                    //addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_SERVER_CODE)) {
                    String code = extras.getString(ServerActivity.SERVER_CODE);
                    result = Server.executeSync(code);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_AGENTES_UBICACION)) {
                    result = rp3.marketforce.sync.Agente.executeSyncGetUbicaciones(db);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_CLIENTE_UPDATE)) {
                    long id = extras.getLong(ClienteActualizacion.ARG_CLIENTE_ID);
                    result = ClienteActualizacion.executeSync(db, id);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_SOLO_RESUMEN)) {
                    result = Agente.executeSyncGetAgente(db);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_CLIENTE_CREATE)) {
                    long cliente = extras.getLong(CrearClienteFragment.ARG_CLIENTE);
                    result = Cliente.executeSyncCreate(db, cliente);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_CLIENTE_UPDATE_FULL)) {
                    long cliente = extras.getLong(CrearClienteFragment.ARG_CLIENTE);
                    result = Cliente.executeSyncUpdateFull(db, cliente);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_ENVIAR_AGENDA)) {
                    int id = extras.getInt(RutasDetailFragment.ARG_AGENDA_ID);
                    result = Agenda.executeSync(db, id);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_REPROGRAMAR_AGENDA)) {
                    int id = extras.getInt(RutasDetailFragment.ARG_AGENDA_ID);
                    result = Agenda.executeSyncReschedule(db, id);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_INSERTAR_AGENDA)) {
                    long agenda = extras.getLong(CrearVisitaFragment.ARG_AGENDA);
                    result = Agenda.executeSyncInsert(db, agenda);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_AGENDA_NO_VISITA)) {
                    int id = extras.getInt(MotivoNoVisitaFragment.ARG_AGENDA);
                    result = Agenda.executeSyncNoVisita(db, id);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_ACTUALIZAR_AGENDA)) {
                    long inicio = extras.getLong(RutasListFragment.ARG_INICIO);
                    long fin = extras.getLong(RutasListFragment.ARG_FIN);
                    result = rp3.marketforce.sync.Rutas.executeSync(db, inicio, fin, true);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_AGENDA_GEOLOCATION)) {
                    int id = extras.getInt(RutasDetailFragment.ARG_AGENDA_ID);
                    double latitud = extras.getInt(RutasDetailFragment.ARG_LATITUD);
                    double longitud = extras.getInt(RutasDetailFragment.ARG_LONGITUD);
                    result = Agenda.executeSyncGeolocation(db, id, latitud, longitud);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_BATCH)) {
                    result = Cliente.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Cliente.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    result = Agenda.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Agenda.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    result = EnviarUbicacion.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Rutas.executeSync(db, null, null, false);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_ACT_AGENDA, SYNC_EVENT_SUCCESS);
                        }
                    }
                } else if (syncType.equals(SYNC_TYPE_UPLOAD_AGENDAS)) {

                    result = Agenda.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Agenda.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Rutas.executeSync(db, null, null, false);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_ACT_AGENDA, SYNC_EVENT_SUCCESS);
                        }
                    }
                } else if (syncType.equals(SYNC_TYPE_UPLOAD_CLIENTES)) {
                    result = Cliente.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Cliente.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Cliente.executeSync(db);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_CLIENTE_UPDATE, SYNC_EVENT_SUCCESS);
                        }
                    }
                } else if (syncType.equals(SYNC_TYPE_TODO)) {
                    result = Cliente.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Cliente.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    result = Agenda.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Agenda.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    result = EnviarUbicacion.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.sync.GeneralValue.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.sync.IdentificationType.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Cliente.executeSync(db);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_CLIENTE_UPDATE, SYNC_EVENT_SUCCESS);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Cliente.executeSyncDeletes(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Rutas.executeSync(db, null, null, false);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_ACT_AGENDA, SYNC_EVENT_SUCCESS);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Agente.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS && PreferenceManager.getBoolean(Contants.KEY_ES_SUPERVISOR)) {
                        result = rp3.marketforce.sync.Agente.executeSyncGetAgente(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Tareas.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if(result == SYNC_EVENT_SUCCESS){
                        Tarea tar = Tarea.getTareaActualizacion(db);
                        if(tar != null)
                        {
                            result = rp3.marketforce.sync.Tareas.executeSyncTareaActualizacion(db, tar.getIdTarea());
                            addDefaultMessage(result);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.Canal.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.marketforce.sync.TipoCliente.executeSync(db);
                        addDefaultMessage(result);
                    }
                }

                SyncAudit.insert(syncType, result);
            }
            else
            {
                //addDefaultMessage(SYNC_EVENT_CONNECTION_FAILED);
                SyncAudit.insert(syncType, SYNC_EVENT_CONNECTION_FAILED);
            }
				
		}catch (Exception e) {			
			Log.e(TAG, "E: " + e.getMessage());
			addDefaultMessage(SYNC_EVENT_ERROR);
			SyncAudit.insert(syncType, SYNC_EVENT_ERROR);
		} 
		finally{
            db.endTransaction();
			db.close();
			notifySyncFinish();
		}								
	}
	
}