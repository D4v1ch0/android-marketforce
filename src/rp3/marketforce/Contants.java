package rp3.marketforce;

public class Contants {
    public final static int GENERAL_TABLE_ESTADO = 1001;
	public final static int GENERAL_TABLE_ESTADO_CIVIL = 1002;
	public final static int GENERAL_TABLE_GENERO = 1003;
	public final static int GENERAL_TABLE_TIPO_DIRECCION = 1004;
    public final static int GENERAL_TABLE_DURACION_VISITA = 1012;
	public final static int GENERAL_TABLE_TIPO_PERSONA = 1022;
	public final static int GENERAL_TABLE_MOTIVOS_NO_VISITA = 1030;
    public final static int GENERAL_TABLE_MOTIVOS_REPROGRAMACION = 1031;
    public final static int GENERAL_TABLE_ESTADOS_OPORTUNIDAD = 1037;
    public final static int GENERAL_TABLE_ESTADOS_OPORTUNIDAD_ETAPA = 1038;
    public final static int GENERAL_TABLE_ESTADOS_OPORTUNIDAD_TAREA = 1014;
    public final static int GENERAL_TABLE_TIPO_RESPONSABLES = 1040;
    public final static int GENERAL_TABLE_TIPOS_PERMISO = 1041;
    public final static int GENERAL_TABLE_MOTIVO_PERMISO = 1042;
    public final static int GENERAL_TABLE_ESTADO_PERMISO = 1043;
    public final static int GENERAL_TABLE_TIPO_MARCACION = 1044;
    public final static int GENERAL_TABLE_MOTIVOS_ANULACION = 1706;
    public final static int GENERAL_TABLE_TIPOS_TRANSACCION = 1621;
    public final static int GENERAL_TABLE_SERIES_BERLIN = 2015;
	public final static String IMAGE_FOLDER = "imagesFolder";
    public final static String IMAGE_FOLDER_PRODUCTOS = "imagesFolderProducto";
    public final static String IMAGE_FOLDER_OPORTUNIDADES = "imagesFolderOportunidad";
	public final static String KEY_FIRST_TIME = "firstTime";
    public final static String KEY_SECOND_TIME = "secondTime";
    public final static String KEY_DATABASE_RESTORE = "db_restore";
	
	public final static String ESTADO_GESTIONANDO = "G";
    public final static String ESTADO_ELIMINADO = "ELIM";
	public final static String ESTADO_NO_VISITADO = "NV";
	public final static String ESTADO_PENDIENTE = "P";
	public final static String ESTADO_VISITADO = "V";
	public final static String ESTADO_REPROGRAMADO = "R";
	
	public final static String DESC_GESTIONANDO = "Gestionando";
	public final static String DESC_NO_VISITADO = "No Visitado";
	public final static String DESC_PENDIENTE = "Pendiente";
	public final static String DESC_VISITADO = "Visitado";
	public final static String DESC_REPROGRAMADO = "Reprogramado";
	
	public final static String KEY_IDAGENTE = "IdAgente";
	public final static String KEY_IDRUTA = "IdRuta";
    public final static String KEY_APLICA_MARCACION = "AplicaMarcacion";
    public final static String KEY_APLICA_BREAK = "AplicaBreak";
    public final static String KEY_LONGITUD_PARTIDA = "LongitudPuntoPartida";
    public final static String KEY_LATITUD_PARTIDA = "LatitudPuntoPartida";
	public final static String KEY_ES_SUPERVISOR = "EsSupervisor";
	public final static String KEY_ES_AGENTE = "EsAgente";
	public final static String KEY_ES_ADMINISTRADOR = "EsAdministrador";
    public final static String KEY_ID_SUPERVISOR = "IdSupervisor";
    public final static String KEY_DESCUENTO_MAXIMO = "DescuentoMaximo";
	public final static String KEY_CARGO = "Cargo";
    public final static String KEY_FOTO = "Foto";
    public final static String KEY_GPS_NOTIFICATION = "NotificationGPS";
	public final static String KEY_ALARMA_INICIO = "HoraInicioTrackingPositionTicks";
	public final static String KEY_ALARMA_FIN = "HoraFinTrackingPositionTicks";
	public final static String KEY_ALARMA_INTERVALO = "MinutosIntervaloTrackingPosition";
	public final static String KEY_PREFIJO_TELEFONICO = "DefaultInternationalPhoneNumberCode";
    public final static String KEY_AGENTE_UBICACION_1 = "AgenteUbicacion1";
    public final static String KEY_AGENTE_UBICACION_2 = "AgenteUbicacion2";
    public final static String KEY_AGENTE_UBICACION_3 = "AgenteUbicacion3";
    public final static String KEY_MODULO_OPORTUNIDADES = "ModuloOportunidades";
    public final static String KEY_MODULO_MARCACIONES = "ModuloMarcaciones";
    public final static String KEY_MODULO_POS = "ModuloPOS";
    public final static String KEY_PERMITIR_CREACION = "PermitirCreacion";
    public final static String KEY_PERMITIR_MODIFICACION = "PermitirModificacion";
    public final static String KEY_SOLO_FALTANTES = "SoloFaltantesEditarEnGestion";
    public final static String KEY_SIEMPRE_EDITAR = "SiempreEditarEnGestion";
	public final static String KEY_SERVER = "server";
	public final static String KEY_CLIENT = "ClientName";
    public final static String KEY_APP_INSTANCE_ID = "TokenId";
    public final static String KEY_MARACIONES_DISTANCIA = "MarcacionDistance";
    public final static String KEY_GOOGLE_CALENDAR_LOGIN = "GoogleCalendarLogin";
    public final static String KEY_GOOGLE_CALENDAR_PASS = "GoogleCalendarPass";
    public final static String KEY_CLIENTE_DEFAULT = "ClienteDefault";
    public final static String KEY_MONEDA_SIMBOLO = "Simbolo";
    public final static String KEY_ID_MONEDA = "IdMoneda";
    public final static String KEY_ID_CAJA = "IdCaja";
    public final static String KEY_CAJA = "Descripcion";
    public final static String KEY_ID_EMPRESA = "IdEmpresa";
    public final static String KEY_ID_ESTABLECIMIENTO = "IdEstablecimiento";
    public final static String KEY_ID_PUNTO_OPERACION = "IdPuntoOperacion";
    public final static String KEY_AUTORIZACION_SRI = "AutorizacionSRI";
    public final static String KEY_VIGENCIA_AUTORIZACION_SRI_INICIO = "VigenciaAutorizacionSRIInicio";
    public final static String KEY_VIGENCIA_AUTORIZACION_SRI_FIN = "VigenciaAutorizacionSRIFin";
    public final static String KEY_SECUENCIA_FACTURA = "SecuenciaFactura";
    public final static String KEY_SECUENCIA_NOTA_CREDITO = "SecuenciaNotaCredito";
    public final static String KEY_SECUENCIA_PEDIDO = "SecuenciaPedido";
    public final static String KEY_SECUENCIA_COTIZACION = "SecuenciaCotizacion";
    public final static String KEY_EMPRESA = "Empresa";
    public final static String KEY_RUC = "RUC";
    public final static String KEY_DIRECCION = "Dirección";
    public final static String KEY_TELEFONO = "Telefono";
    public final static String KEY_ESTABLECIMIENTO = "Establecimiento";
    public final static String KEY_SERIE = "Serie";
    public final static String KEY_NOMBRE_PUNTO_OPERACION = "NombrePuntoOperacion";
    public final static String KEY_ANDROID_ID = "AndroidID";
    public final static String KEY_TRANSACCION_FACTURA = "FA";
    public final static String KEY_TRANSACCION_NOTA_CREDITO = "NC";
    public final static String KEY_TRANSACCION_COTIZACION = "CT";
    public final static String KEY_TRANSACCION_PEDIDO = "PD";

    public final static String CAMPO_FECHA_NACIMIENTO = "FecNac";
    public final static String CAMPO_GENERO = "Gen";
    public final static String CAMPO_ESTADO_CIVIL = "EstCiv";
    public final static String CAMPO_PRIMER_NOMBRE_NATURAL = "Nom1Natural";
    public final static String CAMPO_SEGUNDO_NOMBRE_NATURAL = "Nom2Natural";
    public final static String CAMPO_PRIMER_APELLIDO_NATURAL = "Ape1Natural";
    public final static String CAMPO_SEGUNDO_APELLIDO_NATURAL = "Ape2Natural";
    public final static String CAMPO_NOMBRE_JURIDICO = "NomJuridico";
    public final static String CAMPO_RAZON_SOCIAL = "RazSoc";
    public final static String CAMPO_PAGINA_WEB = "PagWeb";
    public final static String CAMPO_ACTIVIDAD_ECONOMICA = "ActEco";
    public final static String CAMPO_INICIO_ACTIVIDADES = "IniAct";
    public final static String CAMPO_REPRESENTANTE_LEGAL = "RepLeg";
    public final static String CAMPO_CEDULA_REPRESENTANTE = "CedRep";
    public final static String CAMPO_TIPO_IDENTIFICACION = "TipoId";
    public final static String CAMPO_IDENTIFICACION = "Id";
    public final static String CAMPO_CORREO = "Correo";
    public final static String CAMPO_TIPO_CLI = "TipoCli";
    public final static String CAMPO_CANAL = "Canal";
    public final static String CAMPO_FOTO = "Foto";
    public final static String CAMPO_DIRECCION = "Dir";
    public final static String CAMPO_DIRECCION_DESCRIPCION = "DirDesc";
    public final static String CAMPO_REFERENCIA = "Ref";
    public final static String CAMPO_CIUDAD = "CiuPar";
    public final static String CAMPO_DIRECCION_MOVIL = "DirTelf1";
    public final static String CAMPO_DIRECCION_FIJO = "DirTelf2";
    public final static String CAMPO_POSICION_GEO = "PosGEO";
    public final static String CAMPO_NOMBRE_CONTACTO = "NomCont";
    public final static String CAMPO_APELLIDO_CONTACTO = "ApeCont";
    public final static String CAMPO_DIRECCION_CONTACTO = "DirCont";
    public final static String CAMPO_MOVIL_CONTACTO = "Telf1Cont";
    public final static String CAMPO_FIJO_CONTACTO = "Telf2Cont";
    public final static String CAMPO_CARGO_CONTACTO = "CargoCont";
    public final static String CAMPO_CORREO_CONTACTO = "CorreoCont";
    public final static String CAMPO_FOTO_CONTACTO = "FotoCont";

    public final static int IS_CREACION = 1;
    public final static int IS_MODIFICACION = 2;
    public final static int IS_GESTION = 3;

    public final static String POS_ALLOWDECIM = "ALLOWDECIM"; //Permitir decimales en cantidades
    public final static String POS_CHAPRICPRO = "CHAPRICPRO"; //Cambiar precio a productos solo que sea de tipo servicio = 2
    public final static String POS_FINALCONSU = "FINALCONSU"; //Id del consumidor final
    public final static String POS_FINALMOUNT = "FINALMOUNT"; //Monto máximo para facturación de consumidor final
    public final static String POS_GENERATENC = "GENERATENC"; //Hasta cuando días de hecho un documento puedes realizar la nota de crédito
    public final static String POS_NULLOTHDAY = "NULLOTHDAY"; //Permite anular facturas de días anteriores si no tiene cierre y no se ha sincronizado
    public final static String POS_PRICE0 = "PRICE0"; //Permite productos con precio 0 siempre que la factura tenga un valor > 0
    public final static String POS_PRINTARQ = "PRINTARQ"; //Permite imprimir arqueo de caja
    public final static String POS_PRINTCOPY = "PRINTCOPY"; //Permite imprimir una copia seguida de la original
    public final static String POS_PRINTCTRAP = "PRINTCTRAP"; //Indica si imprime o no al aperturar caja
    public final static String POS_PRINTCTRCC = "PRINTCTRCC"; //Indica si imprime o no al cerrar caja
    public final static String POS_PRINTNULLI = "PRINTNULLI"; //Imprimir al anular
    public final static String POS_REQUEMOTIV = "REQUEMOTIV"; //Requiere ingresar motivo anulación o nota de crédito
    public final static String POS_ROUNDSPECI = "ROUNDSPECI"; //Bit que define el redondeo de Costa Rica
    public final static String POS_ROUNDCAB = "ROUNDCAB"; //Redondeo en los valores de cabecera de transacción
    public final static String POS_ROUNDDET = "ROUNDDET"; //Redondeo en los valores de detalle de la transacción
    public final static String POS_USECASHFUN = "USECASHFUN"; //Usa fondo de caja (Para apertura de caja en 0)
    public final static String POS_USENC = "USENC"; //Días de uso de la nota de crédito
    public final static String POS_VALNCCUST = "VALNCCUST"; //Validar si la nota de crédito puede ser a consumidor final o no
    public final static String POS_DIGITPOS = "DIGITPOS"; //Formato de impresión de la caja
    public final static String POS_DIGISTORE = "DIGISTORE"; //Formato de impresión del establecimiento
    public final static String POS_DIGITRANS = "DIGITRANS"; //Formato de impresión de la transacción
    public final static String POS_IMPREDOC = "IMPREDOC"; //Si emite o no documento para impresión
    public final static String POS_LENPAPER = "LENPAPER"; //Cantidad de letras de papel de factura
    public final static String POS_PRINTMODE = "PRINTMODE"; //0 para no imprimir, 1 para impresora Bluetooth, 2 para impresora en red.
    public final static String POS_USEAUTORTC = "USEAUTORTC"; //Si se debe de mostrar o no el codigo de autorización de tarjeta de crédito al pagar
    public final static String POS_USEBANCOTC = "USEBANCOTC"; //Usa banco en el pago de tarjeta de crédito
    public final static String POS_USEDIFERTC = "USEDIFERTC"; //Usa diferido en el pago de tarjeta de crédito
    public final static String POS_USELOTETC = "USELOTETC"; //Usa el campo de lote de pago de tarjeta de crédito
    public final static String POS_USEMARCATC = "USEMARCATC"; //Usa marca de tarjeta de crédito en pagos
    public final static String POS_USENUMTC = "USENUMTC"; //Usa número de tarjeta de crédito
    public final static String POS_USENUMCHEQ = "USENUMCHEQ"; //Usa número de cheque
    public final static String POS_USEBANCHEQ = "USEBANCHEQ"; //Usa banco en forma de pago cheque
    public final static String POS_USECUECHEQ = "USECUECHEQ"; //Usa número de cuenta en forma de pago cheque
    public final static String POS_ROUNDNUM = "ROUNDNUM"; //Valor a redondear en descuentos especiales
    public final static String POS_PORCIMP = "PORCIMP"; //Valor a mostrar del porcentaje de Impuesto

    public final static String APPLICATION_ID = "AGENDACOMERCial";
    public final static String ULTIMO_VENDEDOR = "UltimoVendedor";

	public final static double LATITUD = -2.1637531;
	public final static double LONGITUD = -79.9623577;
	public final static int ZOOM = 11;
	public final static String DEFAULT_APP = "DEMO";
    public final static float SCALE_IMAGE = 0.9f;

    public final static String SQLITE_VERSION_SEARCH = "3.8.4";

    //GRUPO BERLÍN
    public final static String TIPO_AGENDA_CLIENTE = "C";
    public final static String TIPO_AGENDA_OPORTUNIDAD = "O";
	
}
