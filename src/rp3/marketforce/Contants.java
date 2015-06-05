package rp3.marketforce;

public class Contants {
	public final static int GENERAL_TABLE_ESTADO_CIVIL = 1002;
	public final static int GENERAL_TABLE_GENERO = 1003;
	public final static int GENERAL_TABLE_TIPO_DIRECCION = 1004;
	public final static int GENERAL_TABLE_TIPO_PERSONA = 1022;
	public final static int GENERAL_TABLE_MOTIVOS_NO_VISITA = 1030;
    public final static int GENERAL_TABLE_MOTIVOS_REPROGRAMACION = 1031;
	public final static String IMAGE_FOLDER = "imagesFolder";
	public final static String KEY_FIRST_TIME = "firstTime";
	
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
	public final static String KEY_ES_SUPERVISOR = "EsSupervisor";
	public final static String KEY_ES_AGENTE = "EsAgente";
	public final static String KEY_ES_ADMINISTRADOR = "EsAdministrador";
	public final static String KEY_CARGO = "Cargo";
    public final static String KEY_FOTO = "Foto";
	public final static String KEY_ALARMA_INICIO = "HoraInicioTrackingPositionTicks";
	public final static String KEY_ALARMA_FIN = "HoraFinTrackingPositionTicks";
	public final static String KEY_ALARMA_INTERVALO = "MinutosIntervaloTrackingPosition";
	public final static String KEY_PREFIJO_TELEFONICO = "DefaultInternationalPhoneNumberCode";
    public final static String KEY_PERMITIR_CREACION = "PermitirCreacion";
    public final static String KEY_PERMITIR_MODIFICACION = "PermitirModificacion";
    public final static String KEY_SOLO_FALTANTES = "SoloFaltantesEditarEnGestion";
    public final static String KEY_SIEMPRE_EDITAR = "SiempreEditarEnGestion";
	public final static String KEY_SERVER = "server";
	public final static String KEY_CLIENT = "ClientName";

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
	
	public final static String APPLICATION_ID = "AGENDACOMERCial";
	
	public final static double LATITUD = -2.1637531;
	public final static double LONGITUD = -79.9623577;
	public final static int ZOOM = 11;
	public final static String DEFAULT_APP = "DEMO";

    public final static String SQLITE_VERSION_SEARCH = "3.8.4";
}
