package rp3.auna.util.constants;

import android.os.Environment;

/**
 * Created by jvilla on 12/12/2016.
 */

public class Constants {

    public static final String KEY_IP = "UsserIpApp";
    public static final String USER_PRIVATE_PREFERENCES = "userPrivPrefUser";
    public static final String USER_SESSION_KEY = "UserSessionKeyUser";
    public static final String SINCRONIZACION_OK="Sincronizacion satisfactoriamente";
    public static final String DATA_PRIVATE_PREFERENCES = "dataPrivPrefUser";
    public static final String DATA_PRIVATE_PREFERENCES_PROSPECTO_EDIT = "dataPrivPrefProspectoEdit";
    public static final String DATA_PRIVATE_PREFERENCES_PROSPECTO_LLAMADA = "dataPrivPrefProspectoLlamada";
    public static final String DATA_PRIVATE_PREFERENCES_LLAMADA = "dataPrivPrefLlamada";
    public static final String DATA_PRIVATE_PREFERENCES_VISITA = "dataPrivPrefUserVisita";
    public static final String DATA_PRIVATE_PREFERENCES_SOLICITUD = "dataPrivPrefUserSolicitud";
    public static final String DATA_PRIVATE_PREFERENCES_VISITAS= "dataPrivPrefUserSVisitas";
    public static final String DATA_PRIVATE_PREFERENCES_VISITA_REPROGRAMADA = "dataPrivPrefUserSVisitaReprogramada";
    public static final String DATA_PRIVATE_PREFERENCES_LLAMADAGESTION = "dataPrivPrefUserSLlamadaGestion";
    public static final String DATA_PRIVATE_PREFERENCES_VISITA_DETALLE = "dataPrivPrefUserSVisitaDetalle";

    public static final String DATA_PRIVATE_PREFERENCES_REGISTROPAGO = "dataPrivPrefUserSVisitaRegistroPago";
    public static final String DATA_PRIVATE_PREFERENCES_VISITAREGULAR = "dataPrivPrefUserSVisitaRegular";
    public static final String DATA_PRIVATE_PREFERENCES_VISITAFISICA = "dataPrivPrefUserSVisitaFisica";
    public static final String DATA_PRIVATE_PREFERENCES_SESION_INICIADA = "dataPrivPrefUserSessionInit";

    public static final String DATA_PROYECTO_PRIVATE_PREFERENCES = "dataProyectoPrivPrefUser";
    public static final String DATA_KEY="DataSessionKeyUser";
    public static final String DATA_KEY_PROYECTO="DataProyectoKeyUser";
    public static final String COLOR_PRIVATE_PREFERENCES = "colorPrivPrefuser";
    public static final String COLOR_KEY="ColorSessionkeyUser";
    public static final String DB_TEMP="DB_TEMP";
    public static final String DATE_TIME_FORMAT_FECHA_VENCIMIENTO_TARJETA = "MM/yyyy";
    public static final String DATE_TIME_FORMAT_HH_MM="HH:mm";
    public static final String DATE_TIME_FORMAT="dd/MM/yyyy HH:mm:ss";
    public static final String DATE_FORMAT="dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT_SYNC="dd/MM/yyyy_HH:mm:ss";
    public static final String DATE_TIME_FORMAT_FOTO="yyyyMMddHHmmss";
    public static final String DATE_TIME_FORMAT_OPERATION_NUMBER="ssmmHHddMMyyyy";
    public static final String TAG_MESSAGE_CODE = "code";
    public static final String TAG_MESSAGE_MESSAGE = "message";

    public static final String GOOGLE_API_KEY="";
    public static final String VISITADO="Visitado";
    public static final String SIN_DATA_GRABADA="No tiene ninguna informacion grabada...";
    public static final String NO_FINALIZADO="NoFinalizado";
    public static final String DATA_USER_VISIT="dataUserVisit";
    public static final String DATA_USER_INICIO_FIN="dataUserInicioFin";
    public static final String FORMAT_JPEG =".jpg" ;
    public static final String FORMAT_PDF =".pdf" ;
    public static final String FOTOGRAFICO = "Fotografico";
    public static final String PATH_IMAGENES=Environment.getExternalStorageDirectory().getAbsolutePath()+"/TestImages/";
    public static final String PATH_FOTOGRAFICO= Environment.getExternalStorageDirectory().getAbsolutePath()+"/TestImages/Fotografico/";


    /**
     * Constantes para los ParameterID y Label respectivos
     */

    //PARAMETERID
    public static final String  PARAMETERID_ALERTALLAMADA ="ALERTALLAMADA";
    public static final String  PARAMETERID_ALERTASUPERVISOR ="ALERTASUPERVISOR";
    public static final String  PARAMETERID_ALERTASUPERVISORVISITA ="ALERTASUPERVISORVISITA";
    public static final String  PARAMETERID_ALERTAVISITA ="ALERTAVISITA";
    public static final String  PARAMETERID_CODCARDHOLDERCOMMERCE ="CODCARDHOLDERCOMMERCE";
    public static final String  PARAMETERID_COMISIONAJUSTEMETA ="COMISIONAJUSTEMETA";
    public static final String  PARAMETERID_COMMERCECOLOR ="COMMERCECOLOR";
    public static final String  PARAMETERID_COMMERCELOGO ="COMMERCELOGO";
    public static final String  PARAMETERID_COMMERCENAME ="COMMERCENAME";
    public static final String  PARAMETERID_CURRENCYCODE ="CURRENCYCODE";
    public static final String  PARAMETERID_CUT ="CUT";
    public static final String  PARAMETERID_DURACIONLLAMADA ="DURACIONLLAMADA";
    public static final String  PARAMETERID_HORAFINTRACKINGPOSITION ="HORAFINTRACKINGPOSITION";
    public static final String  PARAMETERID_HORAINICIOTRACKINGPOSITION ="HORAINICIOTRACKINGPOSITION";
    public static final String  PARAMETERID_IDACQUIRER ="IDACQUIRER";
    public static final String  PARAMETERID_IDENTCOMMERCE ="IDENTCOMMERCE";
    public static final String  PARAMETERID_IDOPERACIONCOTIZACION ="IDOPERACIONCOTIZACION";
    public static final String  PARAMETERID_IDOPERACIONFRACCIONAMIENTO ="IDOPERACIONFRACCIONAMIENTO";
    public static final String  PARAMETERID_IPADDRESS ="IPADDRESS";
    public static final String  PARAMETERID_ORIGENSOLICITUD ="ORIGENSOLICITUD";
    public static final String  PARAMETERID_PAYBUTTONICON ="PAYBUTTONICON";
    public static final String  PARAMETERID_PROMEDIOP100 ="PROMEDIOP100";
    public static final String  PARAMETERID_TERMINALCODE ="TERMINALCODE";
    public static final String  PARAMETERID_TIPOOPERACION ="TIPOOPERACION";
    public static final String  PARAMETERID_USER ="USER";
    public static final String  PARAMETERID_USUARIO ="USUARIO";
    public static final String  PARAMETERID_REFERIDOCOUNT = "REFERIDOCOUNT";
    public static final String  PARAMETERID_TIEMPO_CREAR_LLAMADA = "CREACIONLLAMADAID";
    public static final String  PARAMETERID_TIEMPO_CREAR_VISITA = "CREACIONVISITAID";
    public static final String  PAMARETERID_ANO_MINIMO_FECHA ="ANOMINIMAFILIADO";
    public static final String  PARAMETERID_ANO_MAX_FECHA = "ANOMAXAFILIADO";
    public static final String  PARAMETERID_ANO_MAX_FILTER_LLAMADA = "FILTERDATEMAXLLAMADAID";
    public static final String  PARAMETERID_ANO_MIN_FILTER_LLAMADA = "FILTERDATEMINLLAMADAID";
    public static final String  PARAMETERID_ANO_MAX_FILTER_VISITA = "FILTERDATEMAXVISITAID";
    public static final String  PARAMETERID_ANO_MIN_FILTER_VISITA = "FILTERDATEMINVISITAID";

    //LABEL
    public static final String  LABEL_TARJETAVALIDAR ="TarjetaValidar";
    public static final String  LABEL_AFILIADOMOVIL="AfiliadoMovil";
    public static final String  LABEL_PURCHASEINFORMATION ="PurchaseInformation";
    public static final String  LABEL_COMMERCE ="Commerce";
    public static final String  LABEL_COTIZACIONAFILIADO ="CotizacionAfiliado";
    public static final String  LABEL_TRANSACTIONINFORMATION ="TransactionInformation";
    public static final String  LABEL_TRACKING ="TRACKING";
    public static final String  LABEL_LLAMADAS ="Llamadas";
    public static final String  LABEL_VISITAS ="Visitas";
    public static final String  LABEL_PROSPECTO = "PROSPECTO";
    public static final String  LABEL_COTIZACION_AFILIADO = "AfiliadoCotizacion";


    public static final String APPLICATIONID = "AGENDACOMERCIAL";
}

