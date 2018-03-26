package rp3.auna.sync;

import rp3.accounts.ServerAuthenticate;
import rp3.auna.Main2Activity;
import rp3.auna.actividades.ActualizacionFragment;
import rp3.auna.actividades.CotizacionActivity;
import rp3.auna.fragments.AgendaFragment;
import rp3.auna.sync.ventanueva.AgendaVta;
import rp3.auna.sync.ventanueva.ApplicationParameterSync;
import rp3.auna.sync.ventanueva.LlamadaVta;
import rp3.auna.sync.ventanueva.ProspectoVta;
import rp3.auna.sync.ventanueva.VisitaVta;
import rp3.auna.utils.Utils;
import rp3.configuration.PreferenceManager;
import rp3.db.sqlite.DataBase;
import rp3.auna.Contants;
import rp3.auna.ServerActivity;
import rp3.auna.cliente.CrearClienteFragment;
import rp3.auna.cliente.SignInFragment;
import rp3.auna.marcaciones.JustificacionFragment;
import rp3.auna.models.Tarea;
import rp3.auna.pedido.AgregarPagoFragment;
import rp3.auna.pedido.ControlCajaFragment;
import rp3.auna.pedido.CrearPedidoFragment;
import rp3.auna.resumen.AgenteDetalleFragment;
import rp3.auna.ruta.CrearVisitaFragment;
import rp3.auna.ruta.MotivoNoVisitaFragment;
import rp3.auna.ruta.RutasDetailFragment;
import rp3.auna.ruta.RutasListFragment;
import rp3.sync.SyncAudit;
import rp3.sync.TestConnection;
import rp3.util.Convert;

import android.accounts.Account;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

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
    public static String SYNC_TYPE_SEND_NOTIFICATION = "send_notification";

    public static String SYNC_TYPE_UPLOAD_OPORTUNIDADES = "oportunidades_upload";
    public static String SYNC_TYPE_PENDIENTES_OPORTUNIDADES = "oportunidades_pendientes";
    public static String SYNC_TYPE_UPLOAD_OPORTUNIDAD = "oportunidad_upload";
    public static String SYNC_TYPE_NOTIFICATION_OPORTUNIDAD = "oportunidad_notificacion";

    public static String SYNC_TYPE_UPLOAD_MARCACION = "marcacion";
    public static String SYNC_TYPE_UPLOAD_PERMISO = "permiso";
    public static String SYNC_TYPE_UPLOAD_PENDIENTES_PERMISO = "permiso_pendientes";
    public static String SYNC_TYPE_PERMISO_PREVIO = "permiso_previo";
    public static String SYNC_TYPE_JUSTIFICACIONES = "justificaciones";
    public static String SYNC_TYPE_JUSTIFICACIONES_UPLOAD = "justificaciones_upload";

    public static String SYNC_TYPE_UPDATE_PEDIDO = "update_pedido";
    public static String SYNC_TYPE_PEDIDO_PENDIENTES = "pedido_pendiente";
    public static String SYNC_TYPE_DOC_REF = "doc_ref";
    public static String SYNC_TYPE_PRODUCTOS = "get_productos";
    public static String SYNC_TYPE_ANULAR_PEDIDO = "anular_pedido";
    public static String SYNC_TYPE_UPDATE_CAJA = "update_caja";
    public static String SYNC_TYPE_CERRAR_CAJA = "cerrar_caja";
    public static String SYNC_TYPE_AUTORIZAR_CIUD_ORO = "ciud_oro";
    public static String SYNC_TYPE_AUTORIZAR_DESC = "autorizar_desc";
    public static String SYNC_TYPE_VALIDAR_NC = "validar_nc";
    public static String SYNC_TYPE_CANCELAR_NC = "cancelar_nc";

    /***
     * Auna Servicios
     */
    public static String SYNC_TYPE_CONSULTA_COTIZACION = "cotizacion";
    public static String SYNC_TYPE_VALIDA_SOLICITUD = "solicitud";
    public static String SYNC_TYPE_REGISTRAR_PAGO = "registro_pago";
    /**
     * Venta nueva
     */
    public static String SYNC_TYPE_INSERTAR_AGENDAVTA = "insertarAgendaVta";
    public static String SYNC_TYPE_UPDATE_AGENDAVTA = "agendaVtaUpdate";
    public static String SYNC_TYPE_GET_PROSPECTO = "prospectoVtaGet";
    public static String SYNC_TYPE_INSERTAR_PROSPECTOVTA = "insertarProspectoVta";
    public static String SYNC_TYPE_UPDATE_VISITA = "actualizarVisitaVta";
    public static String SYNC_TYPE_UPDATE_LLAMADAVTA = "actualizarLlamadaVta";
    public static String SYNC_TYPE_UPDATE_PROSPECTO = "actualizarProspectos";
    public static String SYNC_TYPE_REFRESH_LLAMADA = "refreshLlamadas";
    public static String SYNC_TYPE_REFRESH_VISITA = "refreshVisita";
    public static String SYNC_TYPE_REFRESH_AGENDA = "refreshAgenda";
    public static final String SYNC_TYPE_REFRESH_REPROGRAM_CANCELED_VISITA = "reprogramcanceledvisita";
    public static final String SYNC_TYPE_VENTA_NUEVA = "VentaNueva";

    private Context context;

    /**
     *
     * @param context
     * @param autoInitialize
     */
	
	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
        this.context = context;
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {		
		super.onPerformSync(account, extras, authority, provider, syncResult);	

		String syncType = extras.getString(ARG_SYNC_TYPE);
		
		DataBase db = null;		
		int result = 0;
		
		try{
            db = DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class);
            if(db == null)
                Log.e("Error:","db is null");
            if(TestConnection.executeSync()) {
                Log.d(TAG,"TestConnection.executeSync()...");
                Log.d(TAG,"SyncType:"+syncType);
                if (syncType == null || syncType.equals(SYNC_TYPE_GENERAL)) {

                    db.beginTransaction();
                    Log.d(TAG,"syncType == null || syncType.equals(SYNC_TYPE_GENERAL)...");
                    Log.d(TAG,"Sincronizar general...");

                    //region TODOS LOS SERVICIOS


                    /*
                    if (result == SYNC_EVENT_SUCCESS) {
                        Log.d(TAG,"GeneralValue...");
                        result = rp3.sync.GeneralValue.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        Log.d(TAG,"IdentificationType...");
                        result = rp3.sync.IdentificationType.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"ApplicationParameters...");
                        result = ApplicationParameterSync.executeSync(db);
                        addDefaultMessage(result);
                    }
                    */
                    //region Sync Others
                    //result = Cliente.executeSyncInserts(db);
                    //addDefaultMessage(result);

                    //result = Cliente.executeSyncPendientes(db);
                    //addDefaultMessage(result);

                    /*result = Agenda.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Agenda.executeSyncPendientes(db);
                    addDefaultMessage(result);*/

                    /*if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Cliente.executeSync(db);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_CLIENTE_UPDATE, SYNC_EVENT_SUCCESS);
                        }
                    }*/
                    //endregion

                    /*
                    if (result == SYNC_EVENT_SUCCESS) {
                        Log.d(TAG,"Agente.executeSync...");
                        result = rp3.auna.sync.Agente.executeSync(db);
                        addDefaultMessage(result);
                    }
                    */
                    /*if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Rutas.executeSync(db, null, null, false);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_ACT_AGENDA, SYNC_EVENT_SUCCESS);
                        }
                    }*/


                    /*

                    if (result == SYNC_EVENT_SUCCESS && PreferenceManager.getBoolean(Contants.KEY_ES_SUPERVISOR)) {
                        //result = rp3.auna.sync.Agente.executeSyncGetAgente(db);
                        //addDefaultMessage(result);

                        /*result = rp3.auna.sync.Agente.executeSyncGetUbicaciones(db);
                        addDefaultMessage(result);*/

                        //result = rp3.auna.sync.Agente.executeSyncAgentes(db);
                        //addDefaultMessage(result);

                        //result = rp3.auna.sync.Marcaciones.executeSyncPermisosPorAprobar(db);
                        //addDefaultMessage(result);


                   /* if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Tareas.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if(result == SYNC_EVENT_SUCCESS){
                        Tarea tar = Tarea.getTareaActualizacion(db);
                        if(tar != null)
                        {
                            result = rp3.auna.sync.Tareas.executeSyncTareaActualizacion(db, tar.getIdTarea());
                            addDefaultMessage(result);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Canal.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.TipoCliente.executeSync(db);
                        addDefaultMessage(result);
                    }*/
                   /*
                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Agente.executeSyncParametros(db);
                        addDefaultMessage(result);
                    }*/

                    /*if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Calendario.executeSync(db);
                        addDefaultMessage(result);
                    }*/
                    /*
                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Agente.executeSyncGetDeviceId(getContext());
                        addDefaultMessage(result);
                    }
                    if (result == SYNC_EVENT_SUCCESS){
                        result = rp3.auna.sync.Agente.executeSyncAgentes(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS && !TextUtils.isEmpty(PreferenceManager.getString(Contants.KEY_APP_INSTANCE_ID))) {
                        result = rp3.auna.sync.Agente.executeSyncDeviceId();
                        addDefaultMessage(result);
                    }

                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar obteniendo las comisiones...");
                        result = Agente.executeSyncComisiones(db);
                        addDefaultMessage(result);
                    }*/

                    //region Others Sync
                    //Modulo Oportunidades
                    /*if(PreferenceManager.getBoolean(Contants.KEY_MODULO_OPORTUNIDADES, true)) {
                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Etapa.executeSyncTipos(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Etapa.executeSync(db);
                            addDefaultMessage(result);
                        }
                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Agente.executeSyncAgentes(db);
                            addDefaultMessage(result);
                        }
                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Oportunidad.executeSync(db);
                            addDefaultMessage(result);
                        }
                    }*/

                    // MODULO DE MARCACIONES

                    /*
                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Marcaciones.executeSyncGrupo(db);
                        addDefaultMessage(result);
                    }

                    if(PreferenceManager.getBoolean(Contants.KEY_MODULO_MARCACIONES, true)) {
                        if (result == SYNC_EVENT_SUCCESS) {
                            result = Marcaciones.executeSyncPermisoHoy(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = Marcaciones.executeSyncMarcacionesHoy(db);
                            addDefaultMessage(result);
                        }
                    }

                    if(PreferenceManager.getBoolean(Contants.KEY_MODULO_POS, false)) {

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSync(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncMoneda(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncFormasPago(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncBanco(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncMarcaTarjetas(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncTarjetas(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncTipoDiferidos(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncTarjetaComision(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncGetControl(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncTransacciones(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncGetVendedores(db);
                            addDefaultMessage(result);
                        }
                    }

                    */
				/*
				 * Se comenta carga de fotos ya que se la hara mediante un lazy loader.
				 * Para esto se cargara tambien en el modelo Cliente la url de la foto para poder cargarla
				 * */

                    //if(result == SYNC_EVENT_SUCCESS){
                    //	result = rp3.auna.sync.ClienteFoto.executeSync(db,null);
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
                    /**
                     * Sincronizar Agendas del Agente
                     * */
                   /* if (result == SYNC_EVENT_SUCCESS){
                        result = AgendaVta.executeSync(db);
                        addDefaultMessage(result);
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        result = ProspectoVta.executeSync(db);
                        addDefaultMessage(result);
                    }*/

                   //endregion

                    //endregion

                    /***
                     * Sincronizar tokio al inicio
                     */
                    /**
                     * Sincronizar Agendas del Agente
                     * */

                    //region Verificar Si hay Data pendiente
                    Calendar calendar = Calendar.getInstance();
                    //if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD temp...");
                        result = ProspectoVta.executeSyncInserts(db);
                        //addDefaultMessage(result);
                    //}
                    //if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD sinocronizados...");
                        result = ProspectoVta.executeSyncSincronizada(db);
                        //addDefaultMessage(result);
                    //}
                    //region Anterior GetProspecto
                    /*if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar Obteniendo los prospectos...");
                        result = ProspectoVta.executeSync(db);
                        addDefaultMessage(result);
                    }*/
                    //endregion
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar llamadas pendientes en BD...");
                        result = LlamadaVta.executeSyncInsert(db);
                        //addDefaultMessage(result);
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las llamadas sincronizadas...");
                        result = LlamadaVta.executeSyncUpdate(db);
                        //addDefaultMessage(result);
                    }
                    //region Anterior GetLlamada
                    /*if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar obteniendo las llamadas...");
                        result = LlamadaVta.executeSync(db,Convert.getDotNetTicksFromDate(calendar.getTime()),context);
                        addDefaultMessage(result);
                    }*/
                    //endregion
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar visitas pendientes en BD...");
                        result = VisitaVta.executeSyncInserts(db);
                        //addDefaultMessage(result);
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las visitas sincronizadas...");
                        result = VisitaVta.executeSyncUpdate(db);
                        //addDefaultMessage(result);
                    }
                    //region Anterior GetVisita
                    /*if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar obteniendo las visitas...");
                        result = VisitaVta.executeSync(db,Convert.getDotNetTicksFromDate(calendar.getTime()),context);
                        addDefaultMessage(result);
                    }*/
                    //endregion

                    //endregion

                    //region SINCRONIZAR TODO
                    result = SyncServicio.executeSync(db,context);
                    //addDefaultMessageAuna(result,null);
                    //endregion

                    //Main2Activity.pruebaAlarm(context);
                    db.commitTransaction();



                } else if (syncType.equals(SYNC_TYPE_ENVIAR_UBICACION)) {
                    try {
                        double latitud = extras.getDouble(EnviarUbicacion.ARG_LATITUD);
                        double longitud = extras.getDouble(EnviarUbicacion.ARG_LONGITUD);

                        EnviarUbicacion.executeSync(longitud, latitud);


                    } catch (Exception e) {
                        Log.e("Sync Adapter", e.getMessage());
                    }
                }

                //region RESOURCES NO USADOS
                else if (syncType.equals(SYNC_TYPE_GEOPOLITICAL)) {
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
                } else if (syncType.equals(SYNC_TYPE_SEND_NOTIFICATION)) {
                    int idAgente = extras.getInt(AgenteDetalleFragment.ARG_AGENTE);
                    String title = extras.getString(AgenteDetalleFragment.ARG_TITLE);
                    String message = extras.getString(AgenteDetalleFragment.ARG_MESSAGE);
                    result = Agente.executeSyncSendNotification(idAgente, title, message);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_AGENTES_UBICACION)) {
                    result = rp3.auna.sync.Agente.executeSyncGetUbicaciones(db);
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
                } else if (syncType.equals(SYNC_TYPE_PERMISO_PREVIO)) {
                    result = Marcaciones.executeSyncPermisoPrevio(db);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_JUSTIFICACIONES)) {

                    result = Marcaciones.executeSyncPermisosRevisados(db);
                    addDefaultMessage(result);
                    result = Marcaciones.executeSyncPermisosPorAprobar(db);
                    addDefaultMessage(result);

                } else if (syncType.equals(SYNC_TYPE_JUSTIFICACIONES_UPLOAD)) {
                    result = Marcaciones.executeSyncPermisosRevisados(db);
                    addDefaultMessage(result);
                }/* else if (syncType.equals(SYNC_TYPE_ENVIAR_AGENDA)) {
                    int id = extras.getInt(RutasDetailFragment.ARG_AGENDA_ID);
                    result = Agenda.executeSync(db, id);
                    if(result != SYNC_EVENT_SUCCESS)
                    {
                        rp3.auna.models.Agenda agenda = rp3.auna.models.Agenda.getAgenda(db, id);
                        agenda.setEnviado(false);
                        rp3.auna.models.Agenda.update(db, agenda);
                    }
                    addDefaultMessage(result);
                }*//* else if (syncType.equals(SYNC_TYPE_REPROGRAMAR_AGENDA)) {
                    int id = extras.getInt(RutasDetailFragment.ARG_AGENDA_ID);
                    result = Agenda.executeSyncReschedule(db, id);
                    addDefaultMessage(result);
                }*/ else if (syncType.equals(SYNC_TYPE_PRODUCTOS)) {

                    /*Bundle bundle = Productos.executeSync(db);
                    putData("Productos", bundle.getString("Productos"));
                    addDefaultMessage(bundle.getInt(ARG_SYNC_TYPE));

                    result = Productos.executeSyncCategorias(db);
                    addDefaultMessage(result);

                    result = Productos.executeSyncSubCategorias(db);
                    addDefaultMessage(result);*/

                } else if (syncType.equals(SYNC_TYPE_UPDATE_PEDIDO)) {
                    long id = extras.getLong(CrearPedidoFragment.ARG_PEDIDO);
                    result = Pedido.executeSync(db, id);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_PEDIDO_PENDIENTES)) {
                    result = Pedido.executeSyncPendientes(db);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_ANULAR_PEDIDO)) {
                    long id = extras.getLong(CrearPedidoFragment.ARG_PEDIDO);
                    result = Pedido.executeSyncAnular(db, id);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_CANCELAR_NC)) {
                    long id = extras.getLong(CrearPedidoFragment.ARG_PEDIDO);
                    result = Pedido.executeSyncCancelar(db, id);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_AUTORIZAR_CIUD_ORO)) {
                    String user = extras.getString(SignInFragment.ARG_USER);
                    String pass = extras.getString(SignInFragment.ARG_PASS);
                    Bundle bundle = Agente.executeSyncSignIn(user, pass);
                    putData(ServerAuthenticate.KEY_SUCCESS, bundle.getBoolean(ServerAuthenticate.KEY_SUCCESS, false));
                    addDefaultMessage(bundle.getInt(Agente.KEY_MESSAGE));
                } else if (syncType.equals(SYNC_TYPE_AUTORIZAR_DESC)) {
                    String user = extras.getString(SignInFragment.ARG_USER);
                    String pass = extras.getString(SignInFragment.ARG_PASS);
                    Bundle bundle = Agente.executeSyncAuthDescuento(user, pass);
                    addDefaultMessage(bundle.getInt(Agente.KEY_MESSAGE));
                    putData(ServerAuthenticate.KEY_SUCCESS, bundle.getBoolean(ServerAuthenticate.KEY_SUCCESS, false));
                    putData(Agente.KEY_DESCUENTO, bundle.getInt(Agente.KEY_DESCUENTO));
                } else if (syncType.equals(SYNC_TYPE_UPDATE_CAJA)) {
                    long id = extras.getLong(ControlCajaFragment.ARG_CONTROL);
                    result = Caja.executeSyncInsertControl(db, id);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_CERRAR_CAJA)) {
                    long id = extras.getLong(ControlCajaFragment.ARG_CONTROL);
                    result = Caja.executeSyncCerrarCaja(db, id);
                    addDefaultMessage(result);
                } /*else if (syncType.equals(SYNC_TYPE_INSERTAR_AGENDA)) {
                    long agenda = extras.getLong(CrearVisitaFragment.ARG_AGENDA);
                    result = Agenda.executeSyncInsert(db, agenda);
                    addDefaultMessage(result);
                }*/
                    else if (syncType.equals(SYNC_TYPE_INSERTAR_AGENDAVTA)){
                    Log.d(TAG,"else if (syncType.equals(SYNC_TYPE_INSERTAR_AGENDAVTA)...");
                    result = AgendaVta.executeSyncInserts(db);
                    addDefaultMessage(result);
                    result = AgendaVta.executeSync(db);
                    addDefaultMessage(result);
                }
                else if (syncType.equals(SYNC_TYPE_UPLOAD_PENDIENTES_PERMISO)) {
                    result = Marcaciones.executeSync(db);
                    addDefaultMessage(result);

                    result = Marcaciones.executeSyncPermisoHoy(db);
                    addDefaultMessage(result);

                    result = Marcaciones.executeSyncPermisoPrevio(db);
                    addDefaultMessage(result);

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = Marcaciones.executeSyncMarcacionesHoy(db);
                        addDefaultMessage(result);
                    }
                } /*else if (syncType.equals(SYNC_TYPE_AGENDA_NO_VISITA)) {
                    int id = extras.getInt(MotivoNoVisitaFragment.ARG_AGENDA);
                    result = Agenda.executeSyncNoVisita(db, id);
                    addDefaultMessage(result);
                }*/ else if (syncType.equals(SYNC_TYPE_VALIDAR_NC)) {
                    String nc = extras.getString(AgregarPagoFragment.ARG_NC);
                    String pago = extras.getString(AgregarPagoFragment.ARG_PAGO);
                    Bundle bundle = Caja.executeSyncGetNC(nc, pago);
                    addDefaultMessage(bundle.getInt("Status"));
                    putData(AgregarPagoFragment.ARG_NC, bundle.getBoolean(AgregarPagoFragment.ARG_NC));
                } else if (syncType.equals(SYNC_TYPE_UPLOAD_PERMISO)) {
                    long id = extras.getLong(JustificacionFragment.ARG_PERMISO);
                    result = Marcaciones.executeSyncPermiso(db, id);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_UPLOAD_MARCACION)) {
                    result = Marcaciones.executeSync(db);
                    addDefaultMessage(result);
                }/* else if (syncType.equals(SYNC_TYPE_ACTUALIZAR_AGENDA)) {
                    long inicio = extras.getLong(RutasListFragment.ARG_INICIO);
                    long fin = extras.getLong(RutasListFragment.ARG_FIN);
                    result = rp3.auna.sync.Rutas.executeSync(db, inicio, fin, true);
                    addDefaultMessage(result);
                } *//*else if (syncType.equals(SYNC_TYPE_AGENDA_GEOLOCATION)) {
                    int id = extras.getInt(RutasDetailFragment.ARG_AGENDA_ID);
                    double latitud = extras.getInt(RutasDetailFragment.ARG_LATITUD);
                    double longitud = extras.getInt(RutasDetailFragment.ARG_LONGITUD);
                    result = Agenda.executeSyncGeolocation(db, id, latitud, longitud);
                    addDefaultMessage(result);
                } */else if (syncType.equals(SYNC_TYPE_CONSULTA_COTIZACION)) {
                    String params = extras.getString(CotizacionActivity.ARG_PARAMS);
                    Bundle bundle = Auna.executeCotizacion(params);
                    addDefaultMessage(bundle.getInt("Status"));
                    putData(CotizacionActivity.ARG_RESPONSE, bundle.getString(CotizacionActivity.ARG_RESPONSE));
                } else if (syncType.equals(SYNC_TYPE_VALIDA_SOLICITUD)) {
                    String params = extras.getString(ActualizacionFragment.ARG_PARAMS);
                    Bundle bundle = Auna.executeSolicitud(params);
                    addDefaultMessage(bundle.getInt("Status"));
                    putData(ActualizacionFragment.ARG_RESPONSE, bundle.getString(ActualizacionFragment.ARG_RESPONSE));
                } else if (syncType.equals(SYNC_TYPE_REGISTRAR_PAGO)) {
                    String params = extras.getString(ActualizacionFragment.ARG_PARAMS);
                    Bundle bundle = Auna.executePago(params);
                    addDefaultMessage(bundle.getInt("Status"));
                    putData(ActualizacionFragment.ARG_RESPONSE, bundle.getString(ActualizacionFragment.ARG_RESPONSE));
                }
                //endregion

                else if (syncType.equals(SYNC_TYPE_BATCH)) {
                    //result = Cliente.executeSyncInserts(db);
                    //addDefaultMessage(result);
                    //result = Cliente.executeSyncPendientes(db);
                    //addDefaultMessage(result);
                    /*
                    result = Agenda.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Agenda.executeSyncPendientes(db);
                    addDefaultMessage(result);
                    */
                    result = EnviarUbicacion.executeSyncPendientes(db);
                    addDefaultMessage(result);
                    //Aquí Enviar los prospecto a la Lista Negra
                    result = ProspectoVta.executeSyncRobinson(db);
                    addDefaultMessage(result);
                    //if (PreferenceManager.getBoolean(Contants.KEY_ES_SUPERVISOR)) {
                        //result = rp3.auna.sync.Agente.executeSyncAgentes(db);
                        //addDefaultMessage(result);
                        //result = Marcaciones.executeSyncPermisosRevisados(db);
                        //addDefaultMessage(result);
                        //result = Marcaciones.executeSyncPermisosPorAprobar(db);
                        //addDefaultMessage(result);
                    //}

                    //result = Marcaciones.executeSync(db);
                    //addDefaultMessage(result);

                    /*if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Rutas.executeSync(db, null, null, false);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_ACT_AGENDA, SYNC_EVENT_SUCCESS);
                        }
                    }*/
                }/* else if (syncType.equals(SYNC_TYPE_UPLOAD_AGENDAS)) {

                    result = Agenda.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Agenda.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Rutas.executeSync(db, null, null, false);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_ACT_AGENDA, SYNC_EVENT_SUCCESS);
                        }
                    }
                } */else if (syncType.equals(SYNC_TYPE_NOTIFICATION_OPORTUNIDAD)) {
                    int idOportunidad = extras.getInt(AgenteDetalleFragment.ARG_AGENTE);
                    String title = extras.getString(AgenteDetalleFragment.ARG_TITLE);
                    String message = extras.getString(AgenteDetalleFragment.ARG_MESSAGE);
                    result = Oportunidad.executeSyncSendNotification(idOportunidad, title, message);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_UPLOAD_OPORTUNIDAD)) {
                    result = Oportunidad.executeSyncInserts(db);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_UPLOAD_OPORTUNIDADES)) {
                    result = Oportunidad.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Oportunidad.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    result = Oportunidad.executeSync(db);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_PENDIENTES_OPORTUNIDADES)) {
                    result = Oportunidad.executeSyncPendientes(db);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_UPLOAD_CLIENTES)) {
                    result = Cliente.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Cliente.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Cliente.executeSync(db);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_CLIENTE_UPDATE, SYNC_EVENT_SUCCESS);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Cliente.executeSyncDeletes(db);
                        addDefaultMessage(result);
                    }
                } else if (syncType.equals(SYNC_TYPE_TODO)) {
                    result = Cliente.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Cliente.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    /*result = Agenda.executeSyncInserts(db);
                    addDefaultMessage(result);*/

                    /*result = Agenda.executeSyncPendientes(db);
                    addDefaultMessage(result);*/

                    result = EnviarUbicacion.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    result = Marcaciones.executeSync(db);
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
                        result = rp3.auna.sync.Cliente.executeSync(db);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_CLIENTE_UPDATE, SYNC_EVENT_SUCCESS);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Cliente.executeSyncDeletes(db);
                        addDefaultMessage(result);
                    }

                    /*if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Rutas.executeSync(db, null, null, false);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_ACT_AGENDA, SYNC_EVENT_SUCCESS);
                        }
                    }*/

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Agente.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS && PreferenceManager.getBoolean(Contants.KEY_ES_SUPERVISOR)) {
                        result = rp3.auna.sync.Agente.executeSyncGetAgente(db);
                        addDefaultMessage(result);

                        result = rp3.auna.sync.Agente.executeSyncAgentes(db);
                        addDefaultMessage(result);

                        result = rp3.auna.sync.Marcaciones.executeSyncPermisosPorAprobar(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Tareas.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if(result == SYNC_EVENT_SUCCESS){
                        Tarea tar = Tarea.getTareaActualizacion(db);
                        if(tar != null)
                        {
                            result = rp3.auna.sync.Tareas.executeSyncTareaActualizacion(db, tar.getIdTarea());
                            addDefaultMessage(result);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Canal.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.TipoCliente.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.auna.sync.Marcaciones.executeSyncGrupo(db);
                        addDefaultMessage(result);
                    }

                    if(PreferenceManager.getBoolean(Contants.KEY_MODULO_POS, true)) {
                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Pedido.executeSyncPendientes(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSync(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncMoneda(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncFormasPago(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncBanco(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncMarcaTarjetas(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncTarjetas(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncTipoDiferidos(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncTarjetaComision(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncGetControl(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncTransacciones(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Caja.executeSyncGetVendedores(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Pedido.executeSyncDocRef(db);
                            if (result == SYNC_EVENT_SUCCESS)
                                SyncAudit.insert(SYNC_TYPE_DOC_REF, SYNC_EVENT_SUCCESS);
                            addDefaultMessage(result);
                        }
                    }

                    //Modulo Oportunidades
                    if(PreferenceManager.getBoolean(Contants.KEY_MODULO_OPORTUNIDADES, true)) {
                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Etapa.executeSyncTipos(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Etapa.executeSync(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Oportunidad.executeSyncInserts(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Oportunidad.executeSyncPendientes(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Oportunidad.executeSync(db);
                            addDefaultMessage(result);
                        }
                    }

                    if(PreferenceManager.getBoolean(Contants.KEY_MODULO_MARCACIONES, true)) {
                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.auna.sync.Marcaciones.executeSyncGrupo(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = Marcaciones.executeSyncMarcacionesHoy(db);
                            addDefaultMessage(result);
                        }
                    }

                    /**
                     * Sincronizar Agendas del Agente
                     * */
                    result = ProspectoVta.executeSyncInserts(db);
                    addDefaultMessage(result);
                    result = ProspectoVta.executeSyncSincronizada(db);
                    addDefaultMessage(result);
                    result = ProspectoVta.executeSync(db);
                    addDefaultMessage(result);
                    //result = AgendaVta.executeSyncInserts(db);
                    //addDefaultMessage(result);
                    //result = AgendaVta.executeSync(db);
                    //addDefaultMessage(result);
                }
                else if (syncType.equals(SYNC_TYPE_VENTA_NUEVA)){
                    Calendar calendar = Calendar.getInstance();

                    //region ANTERIOR INICIO
                    /*
                    Log.d(TAG,"ApplicationParameters...");
                    result = ApplicationParameterSync.executeSync(db);
                    addDefaultMessage(result);
                    Log.d(TAG,"Parametros...");
                    result = rp3.auna.sync.Agente.executeSyncParametros(db);
                    addDefaultMessage(result);
                    Log.d(TAG,"GeneralValues...");
                    result = rp3.sync.GeneralValue.executeSync(db);
                    addDefaultMessage(result);
                    Log.d(TAG,"GCM...");
                    if (!TextUtils.isEmpty(PreferenceManager.getString(Contants.KEY_APP_INSTANCE_ID))) {
                        result = rp3.auna.sync.Agente.executeSyncDeviceId();
                        addDefaultMessage(result);
                    }
                    Log.d(TAG,"Iniciar obteniendo las comisiones...");
                    result = Agente.executeSyncComisiones(db);
                    addDefaultMessage(result);
                    */

                    //endregion

                    /**
                     * Sincronizar Agendas del Agente
                     * */
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD temp...");
                        result = ProspectoVta.executeSyncInserts(db);
                        addDefaultMessageAuna(result," Prospectos pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD sinocronizados...");
                        result = ProspectoVta.executeSyncSincronizada(db);
                        addDefaultMessageAuna(result," Prospectos modificados enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar llamadas pendientes en BD...");
                        result = LlamadaVta.executeSyncInsert(db);
                        addDefaultMessageAuna(result," Llamadas pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las llamadas sincronizadas...");
                        result = LlamadaVta.executeSyncUpdate(db);
                        addDefaultMessageAuna(result," Llamadas modificadas enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar visitas pendientes en BD...");
                        result = VisitaVta.executeSyncInserts(db);
                        addDefaultMessageAuna(result," Visitas pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las visitas sincronizadas...");
                        result = VisitaVta.executeSyncUpdate(db);
                        addDefaultMessageAuna(result," Visitas modificadas enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar obteniendo las agendas:Prospectos, Llamadas y Visitas...");
                        result = SyncServicio.executeSyncAgenda(db,context,Convert.getDotNetTicksFromDate(calendar.getTime()));
                    }

                    //region Anterior obtencion
                    //Log.d(TAG,"Iniciar Obteniendo los prospectos...");
                    //result = ProspectoVta.executeSync(db);
                    //addDefaultMessageAuna(result);


                    //Log.d(TAG,"Iniciar obteniendo las llamadas...");
                    //result = LlamadaVta.executeSync(db,Convert.getDotNetTicksFromDate(calendar.getTime()),context);
                    //addDefaultMessageAuna(result);


                    //Log.d(TAG,"Iniciar obteniendo las visitas...");
                    //result = VisitaVta.executeSync(db,Convert.getDotNetTicksFromDate(calendar.getTime()),context);
                    //addDefaultMessageAuna(result);
                    //endregion
                }

                //Actualizar los prospectos modificados
                else if(syncType.equals(SYNC_TYPE_UPDATE_PROSPECTO)){
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD temp...");
                        result = ProspectoVta.executeSyncInserts(db);
                        addDefaultMessage(result);
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD sinocronizados...");
                        result = ProspectoVta.executeSyncSincronizada(db);
                        addDefaultMessage(result);
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar Obteniendo los prospectos...");
                        result = ProspectoVta.executeSync(db);
                        addDefaultMessage(result);
                    }
                }
                else if (syncType.equals(SYNC_TYPE_INSERTAR_PROSPECTOVTA)){
                    /*result =ProspectoVta.executeSyncInserts(db);
                    addDefaultMessage(result);*/
                    if(result==SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD temp...");
                        result = ProspectoVta.executeSyncInserts(db);
                        addDefaultMessage(result);
                    }
                    if(result==SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD sinocronizados...");
                        result = ProspectoVta.executeSyncSincronizada(db);
                        addDefaultMessage(result);
                    }
                    if(result==SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar Obteniendo los prospectos...");
                        result = ProspectoVta.executeSync(db);
                        addDefaultMessage(result);
                    }
                }
                else if(syncType.equals(SYNC_TYPE_UPDATE_LLAMADAVTA)){
                    Calendar calendar = Calendar.getInstance();
                    /**
                     * Sincronizar Agendas del Agente
                     * */
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD temp...");
                        result = ProspectoVta.executeSyncInserts(db);
                        addDefaultMessageAuna(result," Prospectos pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD sinocronizados...");
                        result = ProspectoVta.executeSyncSincronizada(db);
                        addDefaultMessageAuna(result," Prospectos modificados enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar llamadas pendientes en BD...");
                        result = LlamadaVta.executeSyncInsert(db);
                        addDefaultMessageAuna(result," Llamadas pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las llamadas sincronizadas...");
                        result = LlamadaVta.executeSyncUpdate(db);
                        addDefaultMessageAuna(result," Llamadas modificadas enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar visitas pendientes en BD...");
                        result = VisitaVta.executeSyncInserts(db);
                        addDefaultMessageAuna(result," Visitas pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las visitas sincronizadas...");
                        result = VisitaVta.executeSyncUpdate(db);
                        addDefaultMessageAuna(result," Visitas modificadas enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar obteniendo las agendas:Prospectos, Llamadas y Visitas...");
                        result = SyncServicio.executeSyncAgenda(db,context,Convert.getDotNetTicksFromDate(calendar.getTime()));
                    }
                }

                else if(syncType.equals(SYNC_TYPE_UPDATE_VISITA)){
                    Calendar calendar = Calendar.getInstance();
                    /**
                     * Sincronizar Agendas del Agente
                     * */
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD temp...");
                        result = ProspectoVta.executeSyncInserts(db);
                        addDefaultMessageAuna(result," Prospectos pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD sinocronizados...");
                        result = ProspectoVta.executeSyncSincronizada(db);
                        addDefaultMessageAuna(result," Prospectos modificados enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar llamadas pendientes en BD...");
                        result = LlamadaVta.executeSyncInsert(db);
                        addDefaultMessageAuna(result," Llamadas pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las llamadas sincronizadas...");
                        result = LlamadaVta.executeSyncUpdate(db);
                        addDefaultMessageAuna(result," Llamadas modificadas enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar visitas pendientes en BD...");
                        result = VisitaVta.executeSyncInserts(db);
                        addDefaultMessageAuna(result," Visitas pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las visitas sincronizadas...");
                        result = VisitaVta.executeSyncUpdate(db);
                        addDefaultMessageAuna(result," Visitas modificadas enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar obteniendo las agendas:Prospectos, Llamadas y Visitas...");
                        result = SyncServicio.executeSyncAgenda(db,context,Convert.getDotNetTicksFromDate(calendar.getTime()));
                    }
                }
                //refresh llamada
                else if(syncType.equals(SYNC_TYPE_REFRESH_LLAMADA)){
                    Long fecha = extras.getLong(AgendaFragment.FECHA);
                    /**
                     * Sincronizar Agendas del Agente
                     * */
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD temp...");
                        result = ProspectoVta.executeSyncInserts(db);
                        addDefaultMessageAuna(result," Prospectos pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD sinocronizados...");
                        result = ProspectoVta.executeSyncSincronizada(db);
                        addDefaultMessageAuna(result," Prospectos modificados enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar llamadas pendientes en BD...");
                        result = LlamadaVta.executeSyncInsert(db);
                        addDefaultMessageAuna(result," Llamadas pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las llamadas sincronizadas...");
                        result = LlamadaVta.executeSyncUpdate(db);
                        addDefaultMessageAuna(result," Llamadas modificadas enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar visitas pendientes en BD...");
                        result = VisitaVta.executeSyncInserts(db);
                        addDefaultMessageAuna(result," Visitas pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las visitas sincronizadas...");
                        result = VisitaVta.executeSyncUpdate(db);
                        addDefaultMessageAuna(result," Visitas modificadas enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar obteniendo las agendas:Prospectos, Llamadas y Visitas...");
                        result = SyncServicio.executeSyncAgenda(db,context,fecha);
                    }
                }
                //refresh visita
                else if(syncType.equals(SYNC_TYPE_REFRESH_VISITA)){
                    Log.d(TAG,"insertar visitas pendientes en BD...");
                    Long fecha = extras.getLong(AgendaFragment.FECHA);
                    /**
                     * Sincronizar Agendas del Agente
                     * */
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD temp...");
                        result = ProspectoVta.executeSyncInserts(db);
                        addDefaultMessageAuna(result," Prospectos pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD sinocronizados...");
                        result = ProspectoVta.executeSyncSincronizada(db);
                        addDefaultMessageAuna(result," Prospectos modificados enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar llamadas pendientes en BD...");
                        result = LlamadaVta.executeSyncInsert(db);
                        addDefaultMessageAuna(result," Llamadas pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las llamadas sincronizadas...");
                        result = LlamadaVta.executeSyncUpdate(db);
                        addDefaultMessageAuna(result," Llamadas modificadas enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar visitas pendientes en BD...");
                        result = VisitaVta.executeSyncInserts(db);
                        addDefaultMessageAuna(result," Visitas pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las visitas sincronizadas...");
                        result = VisitaVta.executeSyncUpdate(db);
                        addDefaultMessageAuna(result," Visitas modificadas enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar obteniendo las agendas:Prospectos, Llamadas y Visitas...");
                        result = SyncServicio.executeSyncAgenda(db,context,fecha);
                    }
                }
                //refresh visita reprogramming
                else if(syncType.equals(SYNC_TYPE_REFRESH_REPROGRAM_CANCELED_VISITA)){
                    Log.d(TAG,"SYNC_TYPE_REFRESH_REPROGRAM_CANCELED_VISITA");
                    Calendar calendar = Calendar.getInstance();
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD temp...");
                        result = ProspectoVta.executeSyncInserts(db);
                        addDefaultMessageAuna(result," Prospectos pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar enviando los prospectos en BD sinocronizados...");
                        result = ProspectoVta.executeSyncSincronizada(db);
                        addDefaultMessageAuna(result," Prospectos modificados enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar llamadas pendientes en BD...");
                        result = LlamadaVta.executeSyncInsert(db);
                        addDefaultMessageAuna(result," Llamadas pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las llamadas sincronizadas...");
                        result = LlamadaVta.executeSyncUpdate(db);
                        addDefaultMessageAuna(result," Llamadas modificadas enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"insertar visitas pendientes en BD...");
                        result = VisitaVta.executeSyncInserts(db);
                        addDefaultMessageAuna(result," Visitas pendientes enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar actualizando las visitas sincronizadas...");
                        result = VisitaVta.executeSyncUpdate(db);
                        addDefaultMessageAuna(result," Visitas modificadas enviar al servidor.");
                    }
                    if(result == SYNC_EVENT_SUCCESS){
                        Log.d(TAG,"Iniciar obteniendo las agendas:Prospectos, Llamadas y Visitas...");
                        result = SyncServicio.executeSyncAgenda(db,context,Convert.getDotNetTicksFromDate(calendar.getTime()));
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
			e.printStackTrace();
			addDefaultMessage(SYNC_EVENT_ERROR);
			SyncAudit.insert(syncType, SYNC_EVENT_ERROR);
		} 
		finally{
            Log.d(TAG,"syncType:"+syncType);
            if(!syncType.equals(SYNC_TYPE_BATCH) || !syncType.equalsIgnoreCase(SYNC_TYPE_SEND_NOTIFICATION)){
                Log.d(TAG,"Buscar Alertas en SyncAdapter...");
                Main2Activity.pruebaAlarm(db,context);
            }else{
                Log.d(TAG,"Se activo un batch o send notification...");
            }
            db.endTransaction();
			db.close();

			notifySyncFinish();
		}								
	}
	
}