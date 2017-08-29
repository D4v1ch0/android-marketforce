package rp3.berlin.sync;

import rp3.accounts.ServerAuthenticate;
import rp3.berlin.dashboard.MetasFragment;
import rp3.berlin.tracking.TrackingDetailFragment;
import rp3.berlin.tracking.TrackingListFragment;
import rp3.configuration.PreferenceManager;
import rp3.db.sqlite.DataBase;
import rp3.berlin.Contants;
import rp3.berlin.ServerActivity;
import rp3.berlin.cliente.ComprasClienteFragment;
import rp3.berlin.cliente.CrearClienteFragment;
import rp3.berlin.cliente.EstadoCuentaFragment;
import rp3.berlin.cliente.SignInFragment;
import rp3.berlin.marcaciones.JustificacionFragment;
import rp3.berlin.models.Tarea;
import rp3.berlin.pedido.AgregarPagoFragment;
import rp3.berlin.pedido.ControlCajaFragment;
import rp3.berlin.pedido.CrearPedidoFragment;
import rp3.berlin.pedido.ProductoListFragment;
import rp3.berlin.resumen.AgenteDetalleFragment;
import rp3.berlin.ruta.CrearVisitaFragment;
import rp3.berlin.ruta.MotivoNoVisitaFragment;
import rp3.berlin.ruta.RutasDetailFragment;
import rp3.berlin.ruta.RutasListFragment;
import rp3.sync.SyncAudit;
import rp3.sync.TestConnection;

import android.accounts.Account;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.util.Date;

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

    public static String SYNC_TYPE_AGENDA_OPORTUNIDAD = "agenda_oportunidad";
    public static String SYNC_TYPE_GET_DESCUENTO = "get_descuento";
    public static String SYNC_TYPE_GET_STOCK= "get_stock";
    public static String SYNC_TYPE_GET_STOCK_CONSULTA= "get_stock_consulta";
    public static String SYNC_TYPE_GET_IMPORTACIONES= "get_importaciones";
    public static String SYNC_TYPE_ESTADO_CUENTA= "estado_cuenta";
    public static String SYNC_TYPE_COMPRAS_CLIENTE= "compras_cliente";
    public static String SYNC_TYPE_METAS = "metas";
    public static String SYNC_TYPE_SEND_ESTADO_CUENTA= "send_estado_cuenta";
    public static String SYNC_TYPE_GET_PEDIDOS= "get_pedidos";
    public static String SYNC_TYPE_GET_INFO_PEDIDOS= "get_info_pedidos";
	
	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);		
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {		
		super.onPerformSync(account, extras, authority, provider, syncResult);	

		String syncType = extras.getString(ARG_SYNC_TYPE);
		
		DataBase db = null;		
		int result = 0;
		
		try{
            db = DataBase.newDataBase(rp3.berlin.db.DbOpenHelper.class);
            if(db == null)
                Log.e("Error:","db is null");
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

                    result = Cliente.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Cliente.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    result = Agenda.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Agenda.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Cliente.executeSync(db);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_CLIENTE_UPDATE, SYNC_EVENT_SUCCESS);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Agente.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Rutas.executeSync(db, null, null, false);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_ACT_AGENDA, SYNC_EVENT_SUCCESS);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS && PreferenceManager.getBoolean(Contants.KEY_ES_SUPERVISOR)) {
                        result = rp3.berlin.sync.Agente.executeSyncGetAgente(db);
                        addDefaultMessage(result);

                        result = rp3.berlin.sync.Agente.executeSyncGetUbicaciones(db);
                        addDefaultMessage(result);

                        result = rp3.berlin.sync.Agente.executeSyncAgentes(db);
                        addDefaultMessage(result);

                        result = rp3.berlin.sync.Marcaciones.executeSyncPermisosPorAprobar(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Tareas.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if(result == SYNC_EVENT_SUCCESS){
                        Tarea tar = Tarea.getTareaActualizacion(db);
                        if(tar != null)
                        {
                            result = rp3.berlin.sync.Tareas.executeSyncTareaActualizacion(db, tar.getIdTarea());
                            addDefaultMessage(result);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Canal.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.TipoCliente.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Agente.executeSyncParametros(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Calendario.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Agente.executeSyncGetDeviceId(getContext());
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS && !TextUtils.isEmpty(PreferenceManager.getString(Contants.KEY_APP_INSTANCE_ID))) {
                        result = rp3.berlin.sync.Agente.executeSyncDeviceId();
                        addDefaultMessage(result);
                    }

                    //Modulo Oportunidades
                    if(PreferenceManager.getBoolean(Contants.KEY_MODULO_OPORTUNIDADES, true)) {
                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Etapa.executeSyncTipos(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Etapa.executeSync(db);
                            addDefaultMessage(result);
                        }
                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Agente.executeSyncAgentes(db);
                            addDefaultMessage(result);
                        }
                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Oportunidad.executeSync(db);
                            addDefaultMessage(result);
                        }
                    }

                    // MODULO DE MARCACIONES

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Marcaciones.executeSyncGrupo(db);
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
                            result = rp3.berlin.sync.Agente.executeSyncGetAgenteDescuento(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSync(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncMoneda(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncFormasPago(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncBanco(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncMarcaTarjetas(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncTarjetas(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncTipoDiferidos(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncTarjetaComision(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncGetControl(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncTransacciones(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncGetVendedores(db);
                            addDefaultMessage(result);
                        }
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
                } else if (syncType.equals(SYNC_TYPE_SEND_NOTIFICATION)) {
                    int idAgente = extras.getInt(AgenteDetalleFragment.ARG_AGENTE);
                    String title = extras.getString(AgenteDetalleFragment.ARG_TITLE);
                    String message = extras.getString(AgenteDetalleFragment.ARG_MESSAGE);
                    result = Agente.executeSyncSendNotification(idAgente, title, message);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_AGENTES_UBICACION)) {
                    result = rp3.berlin.sync.Agente.executeSyncGetUbicaciones(db);
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
                } else if (syncType.equals(SYNC_TYPE_ENVIAR_AGENDA)) {
                    int id = extras.getInt(RutasDetailFragment.ARG_AGENDA_ID);
                    result = Agenda.executeSync(db, id);
                    if(result != SYNC_EVENT_SUCCESS)
                    {
                        rp3.berlin.models.Agenda agenda = rp3.berlin.models.Agenda.getAgenda(db, id);
                        agenda.setEnviado(false);
                        rp3.berlin.models.Agenda.update(db, agenda);
                    }
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_REPROGRAMAR_AGENDA)) {
                    int id = extras.getInt(RutasDetailFragment.ARG_AGENDA_ID);
                    result = Agenda.executeSyncReschedule(db, id);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_PRODUCTOS)) {

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

                    result = Pedido.executeSyncVentasPerdidas(db);
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
                } else if (syncType.equals(SYNC_TYPE_INSERTAR_AGENDA)) {
                    long agenda = extras.getLong(CrearVisitaFragment.ARG_AGENDA);
                    result = Agenda.executeSyncInsert(db, agenda);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_UPLOAD_PENDIENTES_PERMISO)) {
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
                } else if (syncType.equals(SYNC_TYPE_AGENDA_NO_VISITA)) {
                    int id = extras.getInt(MotivoNoVisitaFragment.ARG_AGENDA);
                    result = Agenda.executeSyncNoVisita(db, id);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_VALIDAR_NC)) {
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
                } else if (syncType.equals(SYNC_TYPE_AGENDA_OPORTUNIDAD)) {
                    result = Oportunidad.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Oportunidad.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    result = Oportunidad.executeSyncAgendas(db);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_ACTUALIZAR_AGENDA)) {
                    long inicio = extras.getLong(RutasListFragment.ARG_INICIO);
                    long fin = extras.getLong(RutasListFragment.ARG_FIN);
                    result = rp3.berlin.sync.Rutas.executeSync(db, inicio, fin, true);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_AGENDA_GEOLOCATION)) {
                    int id = extras.getInt(RutasDetailFragment.ARG_AGENDA_ID);
                    double latitud = extras.getInt(RutasDetailFragment.ARG_LATITUD);
                    double longitud = extras.getInt(RutasDetailFragment.ARG_LONGITUD);
                    result = Agenda.executeSyncGeolocation(db, id, latitud, longitud);
                    addDefaultMessage(result);
                } else if (syncType.equals(SYNC_TYPE_GET_DESCUENTO)) {
                    String cliente = extras.getString(ProductoListFragment.ARG_CLIENTE).trim();
                    String tipoOrden = extras.getString(ProductoListFragment.ARG_TIPO_ORDEN).trim();
                    String linea = extras.getString(ProductoListFragment.ARG_LINEA).trim();
                    String familia = extras.getString(ProductoListFragment.ARG_FAMILIA).trim();
                    String item = extras.getString(ProductoListFragment.ARG_LISTA_PRECIO).trim();
                    Bundle bundle = Productos.executeSyncDescuento(linea, cliente, tipoOrden, familia, item);
                    addDefaultMessage(bundle.getInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE));
                    putData(Agente.KEY_DESCUENTO, bundle.getString(Agente.KEY_DESCUENTO));
                } else if (syncType.equals(SYNC_TYPE_GET_STOCK)) {
                    String item = extras.getString(ProductoListFragment.ARG_ITEM).trim();
                    Bundle bundle = Productos.executeSyncStock(item);
                    addDefaultMessage(bundle.getInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE));
                    putData(ProductoListFragment.ARG_ITEM, bundle.getString(ProductoListFragment.ARG_ITEM));
                } else if (syncType.equals(SYNC_TYPE_GET_STOCK_CONSULTA)) {
                    String item = extras.getString(ProductoListFragment.ARG_ITEM).trim();
                    Bundle bundle = Productos.executeSyncStock(item);
                    addDefaultMessage(bundle.getInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE));
                    putData(ProductoListFragment.ARG_ITEM, bundle.getString(ProductoListFragment.ARG_ITEM));
                } else if (syncType.equals(SYNC_TYPE_GET_IMPORTACIONES)) {
                    String item = extras.getString(ProductoListFragment.ARG_ITEM).trim();
                    Bundle bundle = Productos.executeSyncImportacion(item);
                    addDefaultMessage(bundle.getInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE));
                    putData(ProductoListFragment.ARG_ITEM, bundle.getString(ProductoListFragment.ARG_ITEM));
                } else if (syncType.equals(SYNC_TYPE_ESTADO_CUENTA)) {
                    String cliente = extras.getString(EstadoCuentaFragment.ARG_CLIENTE).trim();
                    Bundle bundle = Cliente.executeSyncEstadoCuenta(cliente);
                    addDefaultMessage(bundle.getInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE));
                    putData(EstadoCuentaFragment.ARG_CLIENTE, bundle.getString(EstadoCuentaFragment.ARG_CLIENTE));
                } else if (syncType.equals(SYNC_TYPE_SEND_ESTADO_CUENTA)) {
                    String cliente = extras.getString(EstadoCuentaFragment.ARG_CLIENTE).trim();
                    Bundle bundle = Cliente.executeSyncSendEstadoCuenta(cliente);
                    addDefaultMessage(bundle.getInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE));
                    //putData(EstadoCuentaFragment.ARG_CLIENTE, bundle.getString(EstadoCuentaFragment.ARG_CLIENTE));
                } else if (syncType.equals(SYNC_TYPE_METAS)) {
                    Bundle bundle = Agente.executeSyncGetMetas();
                    addDefaultMessage(bundle.getInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE));
                    putData(MetasFragment.ARG_RESP, bundle.getString(MetasFragment.ARG_RESP));
                } else if (syncType.equals(SYNC_TYPE_COMPRAS_CLIENTE)) {
                    String cliente = extras.getString(ComprasClienteFragment.ARG_CLIENTE).trim();
                    Bundle bundle = Cliente.executeSyncCompras(cliente);
                    addDefaultMessage(bundle.getInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE));
                    putData(ComprasClienteFragment.ARG_CLIENTE, bundle.getString(ComprasClienteFragment.ARG_CLIENTE));
                } else if (syncType.equals(SYNC_TYPE_GET_PEDIDOS)) {
                    long inicio = extras.getLong(TrackingListFragment.ARG_INICIO);
                    long fin = extras.getLong(TrackingListFragment.ARG_FIN);
                    String cliente = extras.getString(TrackingListFragment.ARG_CLIENTE).trim();
                    String estado = extras.getString(TrackingListFragment.ARG_ESTADO).trim();
                    String infor = extras.getString(TrackingListFragment.ARG_INFOR).trim();
                    Bundle bundle = Pedido.executeSyncGetPedido(inicio, fin, cliente, estado,infor);
                    addDefaultMessage(bundle.getInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE));
                    putData(TrackingListFragment.ARG_PEDIDOS, bundle.getString(TrackingListFragment.ARG_PEDIDOS));
                } else if (syncType.equals(SYNC_TYPE_GET_INFO_PEDIDOS)) {
                    long idPedido = extras.getLong(TrackingDetailFragment.ARG_ITEM_ID);
                    Bundle bundle = Pedido.executeSyncGetInfoPedido(idPedido);
                    addDefaultMessage(bundle.getInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE));
                    putData(TrackingDetailFragment.ARG_ITEM_ID, bundle.getString(TrackingDetailFragment.ARG_ITEM_ID));
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

                    if (PreferenceManager.getBoolean(Contants.KEY_ES_SUPERVISOR)) {
                        result = rp3.berlin.sync.Agente.executeSyncAgentes(db);
                        addDefaultMessage(result);
                        result = Marcaciones.executeSyncPermisosRevisados(db);
                        addDefaultMessage(result);
                        result = Marcaciones.executeSyncPermisosPorAprobar(db);
                        addDefaultMessage(result);
                    }

                    //result = Marcaciones.executeSync(db);
                    //addDefaultMessage(result);

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Rutas.executeSync(db, null, null, false);
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
                        result = rp3.berlin.sync.Rutas.executeSync(db, null, null, false);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_ACT_AGENDA, SYNC_EVENT_SUCCESS);
                        }
                    }

                    result = Oportunidad.executeSyncInserts(db);
                    addDefaultMessage(result);

                    result = Oportunidad.executeSyncPendientes(db);
                    addDefaultMessage(result);

                    result = Oportunidad.executeSyncAgendas(db);
                    addDefaultMessage(result);

                } else if (syncType.equals(SYNC_TYPE_NOTIFICATION_OPORTUNIDAD)) {
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
                        result = rp3.berlin.sync.Cliente.executeSync(db);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_CLIENTE_UPDATE, SYNC_EVENT_SUCCESS);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Cliente.executeSyncDeletes(db);
                        addDefaultMessage(result);
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
                        result = rp3.berlin.sync.Cliente.executeSync(db);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_CLIENTE_UPDATE, SYNC_EVENT_SUCCESS);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Cliente.executeSyncDeletes(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Rutas.executeSync(db, null, null, false);
                        addDefaultMessage(result);
                        if (result == SYNC_EVENT_SUCCESS) {
                            SyncAudit.insert(SYNC_TYPE_ACT_AGENDA, SYNC_EVENT_SUCCESS);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Agente.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS && PreferenceManager.getBoolean(Contants.KEY_ES_SUPERVISOR)) {
                        result = rp3.berlin.sync.Agente.executeSyncGetAgente(db);
                        addDefaultMessage(result);

                        result = rp3.berlin.sync.Agente.executeSyncAgentes(db);
                        addDefaultMessage(result);

                        result = rp3.berlin.sync.Marcaciones.executeSyncPermisosPorAprobar(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Tareas.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if(result == SYNC_EVENT_SUCCESS){
                        Tarea tar = Tarea.getTareaActualizacion(db);
                        if(tar != null)
                        {
                            result = rp3.berlin.sync.Tareas.executeSyncTareaActualizacion(db, tar.getIdTarea());
                            addDefaultMessage(result);
                        }
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Canal.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.TipoCliente.executeSync(db);
                        addDefaultMessage(result);
                    }

                    if (result == SYNC_EVENT_SUCCESS) {
                        result = rp3.berlin.sync.Marcaciones.executeSyncGrupo(db);
                        addDefaultMessage(result);
                    }

                    if(PreferenceManager.getBoolean(Contants.KEY_MODULO_POS, true)) {
                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Agente.executeSyncGetAgenteDescuento(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Pedido.executeSyncPendientes(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSync(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncMoneda(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncFormasPago(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncBanco(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncMarcaTarjetas(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncTarjetas(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncTipoDiferidos(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncTarjetaComision(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncGetControl(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncTransacciones(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Caja.executeSyncGetVendedores(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Pedido.executeSyncDocRef(db);
                            if (result == SYNC_EVENT_SUCCESS)
                                SyncAudit.insert(SYNC_TYPE_DOC_REF, SYNC_EVENT_SUCCESS);
                            addDefaultMessage(result);
                        }
                    }

                    //Modulo Oportunidades
                    if(PreferenceManager.getBoolean(Contants.KEY_MODULO_OPORTUNIDADES, true)) {
                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Etapa.executeSyncTipos(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Etapa.executeSync(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Oportunidad.executeSyncInserts(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Oportunidad.executeSyncPendientes(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Oportunidad.executeSync(db);
                            addDefaultMessage(result);
                        }
                    }

                    if(PreferenceManager.getBoolean(Contants.KEY_MODULO_MARCACIONES, true)) {
                        if (result == SYNC_EVENT_SUCCESS) {
                            result = rp3.berlin.sync.Marcaciones.executeSyncGrupo(db);
                            addDefaultMessage(result);
                        }

                        if (result == SYNC_EVENT_SUCCESS) {
                            result = Marcaciones.executeSyncMarcacionesHoy(db);
                            addDefaultMessage(result);
                        }
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