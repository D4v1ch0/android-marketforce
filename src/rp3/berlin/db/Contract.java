package rp3.berlin.db;

import android.provider.BaseColumns;

public final class Contract {

	public static abstract class TipoCliente implements BaseColumns {
		
		public static final String TABLE_NAME = "tbTipoCliente";
		
		public static final String COLUMN_DESCRIPCION = "Descripcion";
		
		public static final String FIELD_DESCRIPCION = COLUMN_DESCRIPCION;	
	}
	
	public static abstract class Canal implements BaseColumns {
		
		public static final String TABLE_NAME = "tbCanal";
		
		public static final String COLUMN_DESCRIPCION = "Descripcion";
		
		public static final String FIELD_DESCRIPCION = COLUMN_DESCRIPCION;	
	}
	
	
	public static abstract class Cliente implements BaseColumns {
		
		public static final String TABLE_NAME = "tbCliente";
				
		public static final String COLUMN_ID_TIPO_IDENTIFICACION = "IdTipoIdentificacion";
		public static final String COLUMN_ID_CLIENTE = "IdCliente";
		public static final String COLUMN_GENERO = "Genero";
		public static final String COLUMN_ESTADO_CIVIL = "EstadoCivil";
		public static final String COLUMN_FECHA_NACIMIENTO = "FechaNacimiento";
		public static final String COLUMN_TIPO_CLIENTE_ID = "IdTipoCliente";
		public static final String COLUMN_CANAL_ID = "IdCanal";
		public static final String COLUMN_CALIFICACION = "Calificacion";
		public static final String COLUMN_URL_FOTO = "URLFoto";
		public static final String COLUMN_TIPO_PERSONA = "TipoPersona";
		public static final String COLUMN_NUEVO = "Nuevo";
		public static final String COLUMN_PENDIENTE = "Pendiente";
        public static final String COLUMN_FECHA_ULTIMA_VISITA = "FechaUltimaVisita";
        public static final String COLUMN_AGENTE_ULTIMA_VISITA = "AgenteUltimaVisita";
        public static final String COLUMN_FECHA_PROXIMA_VISITA = "FechaProximaVisita";
		public static final String COLUMN_EXENTO_IMPUESTO = "ExentoImpuesto";
		public static final String COLUMN_CIUDADANO_ORO = "CiudadanoOro";
		public static final String COLUMN_TARJETA = "Tarjeta";
		public static final String COLUMN_TIPO_PARTNER = "TipoPartner";
		public static final String COLUMN_CANAL_PARTNER = "CanalPartner";
		public static final String COLUMN_AVISO = "Aviso";
		public static final String COLUMN_INDICE_SOLVENCIA = "IndiceSolvencia";
		public static final String COLUMN_LIMITE_CREDITO = "LimiteCredito";
		public static final String COLUMN_LISTA_PRECIO = "ListaPrecio";
		public static final String COLUMN_CONDICION_PAGO = "CondicionPago";
				
		public static final String FIELD_ID_TIPO_IDENTIFICACION = COLUMN_ID_TIPO_IDENTIFICACION;	
		public static final String FIELD_ID_CLIENTE = COLUMN_ID_CLIENTE;	
		public static final String FIELD_GENERO = COLUMN_GENERO;
		public static final String FIELD_ESTADO_CIVIL = COLUMN_ESTADO_CIVIL;
		public static final String FIELD_FECHA_NACIMIENTO = COLUMN_FECHA_NACIMIENTO;
		public static final String FIELD_TIPO_CLIENTE_ID = COLUMN_TIPO_CLIENTE_ID;
		public static final String FIELD_CANAL_ID = COLUMN_CANAL_ID;
		public static final String FIELD_CALIFICACION = COLUMN_CALIFICACION;	
		public static final String FIELD_URL_FOTO = COLUMN_URL_FOTO;	
		public static final String FIELD_TIPO_PERSONA = COLUMN_TIPO_PERSONA;
		public static final String FIELD_NUEVO = COLUMN_NUEVO;
		public static final String FIELD_PENDIENTE = COLUMN_PENDIENTE;
		public static final String FIELD_EXENTO_IMPUESTO = COLUMN_EXENTO_IMPUESTO;
		public static final String FIELD_CIUDADANO_ORO = COLUMN_CIUDADANO_ORO;
		public static final String FIELD_TARJETA = COLUMN_TARJETA;
		public static final String FIELD_TIPO_PARTNER = COLUMN_TIPO_PARTNER;
		public static final String FIELD_CANAL_PARTNER = COLUMN_CANAL_PARTNER;
		public static final String FIELD_AVISO = COLUMN_AVISO;
		public static final String FIELD_INDICE_SOLVENCIA = COLUMN_INDICE_SOLVENCIA;
		public static final String FIELD_LIMITE_CREDITO = COLUMN_LIMITE_CREDITO;
		public static final String FIELD_LISTA_PRECIO = COLUMN_LISTA_PRECIO;
		public static final String FIELD_CONDICION_PAGO = COLUMN_CONDICION_PAGO;
        public static final String FIELD_FECHA_ULTIMA_VISITA = COLUMN_FECHA_ULTIMA_VISITA;
        public static final String FIELD_AGENTE_ULTIMA_VISITA = COLUMN_AGENTE_ULTIMA_VISITA;
        public static final String FIELD_FECHA_PROXIMA_VISITA = COLUMN_FECHA_PROXIMA_VISITA;

		public static final String FIELD_IDENTIFICACION = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_IDENTIFICACION;
		public static final String FIELD_NOMBRE1 = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_NOMBRE1;
		public static final String FIELD_NOMBRE2 = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_NOMBRE2;
		public static final String FIELD_APELLIDO1 = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_APELLIDO1;
		public static final String FIELD_APELLIDO2 = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_APELLIDO2;
		public static final String FIELD_NOMBRE_COMPLETO = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_NOMBRE_COMPLETO;
		public static final String FIELD_CORREO_ELECTRONICO = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_CORREO_ELECTRONICO;
		public static final String FIELD_DIRECCION = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_DIRECCION;
		public static final String FIELD_TELEFONO = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_TELEFONO;
		public static final String FIELD_PAGINA_WEB = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_PAGINA_WEB;
		public static final String FIELD_RAZON_SOCIAL = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_RAZON_SOCIAL;
		public static final String FIELD_ACTIVIDAD_ECONOMICA = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_ACTIVIDAD_ECONOMICA;
		public static final String FIELD_ID_EXTERNO = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_ID_EXTERNO;
		public static final String FIELD_ESTADO_CIVIL_DESCRIPCION = "tbEstadoCivil_Value";
		public static final String FIELD_GENERO_DESCRIPCION = "tbGenero_Value";
		public static final String FIELD_TIPO_CLIENTE_DESCRIPCION = Contract.TipoCliente.TABLE_NAME + "_" + Contract.TipoCliente.COLUMN_DESCRIPCION;
		public static final String FIELD_CANAL_DESCRIPCION = Contract.Canal.TABLE_NAME + "_" + Contract.Canal.COLUMN_DESCRIPCION;
		public static final String FIELD_TIPOIDENTIFICACION_NOMBRE = rp3.data.models.Contract.IdentificationType.TABLE_NAME + "_" + rp3.data.models.Contract.IdentificationType.COLUMN_NAME ;
		
		public static final String QUERY_CLIENTES = "Clientes";
		public static final String QUERY_CLIENTES_BY_IDENTIFICATION = "ClienteByIdentificacion";
		public static final String QUERY_CLIENTES_AND_CONTACTS = "ClientesAndContacts";
		public static final String QUERY_CLIENTE_BY_ID = "ClienteById";
		public static final String QUERY_CLIENTE_INSERTS = "ClienteInserts";
		public static final String QUERY_CLIENTE_PENDIENTES = "ClientePendientes";
		public static final String QUERY_CLIENTE_BY_ID_SERVER = "ClienteByIdServer";
		public static final String QUERY_CLIENT_SEARCH = "SimpleClientSearch";
		
	}
	
	 public static abstract class ClientExt implements BaseColumns {
		 public static final String TABLE_NAME = "tbClienteExt";
		 public static final String COLUMN_ID = "docid";
		 public static final String COLUMN_IDENTIFICACION = "Identificacion";
		 public static final String COLUMN_NOMBRE1 = "Nombre1";
		 public static final String COLUMN_NOMBRE2 = "Nombre2";
		 public static final String COLUMN_APELLIDO1 = "Apellido1";
		 public static final String COLUMN_APELLIDO2 = "Apellido2";
		 public static final String COLUMN_NOMBRE_COMPLETO = "NombreCompleto";
		 public static final String COLUMN_CORREO_ELECTRONICO = "CorreoElectronico";
		 public static final String COLUMN_MAIL = "CorreoElectronico";
		 public static final String COLUMN_DIRECCION = "Direccion";
		 public static final String COLUMN_TELEFONO = "Telefono";
		 public static final String COLUMN_RAZON_SOCIAL = "RazonSocial";
		 public static final String COLUMN_PAGINA_WEB = "PaginaWeb";
		 public static final String COLUMN_ACTIVIDAD_ECONOMICA = "ActividadEconomica";
		 public static final String COLUMN_ID_EXTERNO= "IdExterno";


	 }
	
	public static abstract class ClienteDireccion implements BaseColumns {
		
		public static final String TABLE_NAME = "tbClienteDireccion";
		
		public static final String COLUMN_CLIENTE_ID_EXT = "_IdCliente";
		public static final String COLUMN_CLIENTE_ID = "IdCliente";
		public static final String COLUMN_CLIENTE_DIRECCION_ID = "IdClienteDireccion";
		public static final String COLUMN_DIRECCION = "Direccion";  
		public static final String COLUMN_ES_PRINCIPAL = "EsPrincipal";
		public static final String COLUMN_TIPO_DIRECCION = "TipoDireccion";
		public static final String COLUMN_CIUDAD_ID = "IdCiudad";
		public static final String COLUMN_TELEFONO1 = "Telefono1";
		public static final String COLUMN_TELEFONO2 = "Telefono2";
		public static final String COLUMN_REFERENCIA = "Referencia";
		public static final String COLUMN_LATITUD = "Latitud";
		public static final String COLUMN_LONGITUD = "Longitud";
        public static final String COLUMN_CIUDAD_DESCRIPCION = "CiudadDescripcion";
		
		public static final String FIELD_CLIENTE_ID = COLUMN_CLIENTE_ID;
		public static final String FIELD_CLIENTE_DIRECCION_ID = COLUMN_CLIENTE_DIRECCION_ID;
		public static final String FIELD_DIRECCION = COLUMN_DIRECCION;
		public static final String FIELD_ES_PRINCIPAL = COLUMN_ES_PRINCIPAL;
		public static final String FIELD_TIPO_DIRECCION = COLUMN_TIPO_DIRECCION;
		public static final String FIELD_CIUDAD_ID = COLUMN_CIUDAD_ID;
		public static final String FIELD_TELEFONO1 = COLUMN_TELEFONO1;
		public static final String FIELD_TELEFONO2 = COLUMN_TELEFONO2;
		public static final String FIELD_REFERENCIA = COLUMN_REFERENCIA;
		public static final String FIELD_LATITUD = COLUMN_LATITUD;
		public static final String FIELD_LONGITUD = COLUMN_LONGITUD;
		public static final String FIELD_CIUDAD = rp3.data.models.Contract.GeopoliticalStructure.TABLE_NAME+"_"+rp3.data.models.Contract.GeopoliticalStructureExt.COLUMN_NAME;
		
		public static final String QUERY_CLIENTE_DIRECCION_BY_ID = "ClienteDireccionById";
		public static final String QUERY_CLIENTE_DIRECCION_BY_ID_INTERNO = "ClienteDireccionByIdInterno";
		public static final String QUERY_CLIENTE_DIRECCION_BY_ID_DIRECCION = "ClienteDireccionByIdDireccion";
		public static final String QUERY_CLIENTE_DIRECCION_BY_ID_DIRECCION_INTERN = "ClienteDireccionByIdDireccionIntern";
	}
	
	public static abstract class Contacto implements BaseColumns {
		
		public static final String TABLE_NAME = "tbContacto";
				
		public static final String COLUMN_ID_CONTACTO = "IdContacto";
		public static final String COLUMN_CLIENTE_ID_EXT = "_IdCliente";
		public static final String COLUMN_ID_CLIENTE = "IdCliente";
		public static final String COLUMN_ID_CLIENTE_DIRECCION = "IdClienteDireccion";
		public static final String COLUMN_URL_FOTO = "URLFoto";
				
		public static final String FIELD_ID_CONTACTO = COLUMN_ID_CONTACTO;		
		public static final String FIELD_ID_CLIENTE = COLUMN_ID_CLIENTE;
		public static final String FIELD_ID_CLIENTE_DIRECCION = COLUMN_ID_CLIENTE_DIRECCION;
		public static final String FIELD_NOMBRE = Contract.ContactoExt.TABLE_NAME + "_" + Contract.ContactoExt.COLUMN_NOMBRE;
		public static final String FIELD_APELLIDO = Contract.ContactoExt.TABLE_NAME + "_" + Contract.ContactoExt.COLUMN_APELLIDO;
		public static final String FIELD_TELEFONO1 = Contract.ContactoExt.TABLE_NAME + "_" + Contract.ContactoExt.COLUMN_TELEFONO1;
		public static final String FIELD_TELEFONO2 = Contract.ContactoExt.TABLE_NAME + "_" + Contract.ContactoExt.COLUMN_TELEFONO2;
		public static final String FIELD_CARGO = Contract.ContactoExt.TABLE_NAME + "_" + Contract.ContactoExt.COLUMN_CARGO;
		public static final String FIELD_CORREO = Contract.ContactoExt.TABLE_NAME + "_" + Contract.ContactoExt.COLUMN_CORREO;
		public static final String FIELD_URL_FOTO = COLUMN_URL_FOTO;	
		
		public static final String QUERY_CONTACTOS = "ContactosByCliente";
		public static final String QUERY_CONTACTOS_INTERNO = "ContactosByClienteInterno";
		public static final String QUERY_CONTACTOS_ID = "ContactoById";
		public static final String QUERY_CONTACTOS_ID_CLIENTE = "ContactoByIdCliente";
		
	}
	
	public static abstract class ContactoExt implements BaseColumns {
		public static final String TABLE_NAME = "tbContactoExt";
		public static final String COLUMN_ID = "docid";
		public static final String COLUMN_NOMBRE = "Nombre";
		public static final String COLUMN_APELLIDO = "Apellido";
		public static final String COLUMN_TELEFONO1 = "Telefono1";
		public static final String COLUMN_TELEFONO2 = "Telefono2";
		public static final String COLUMN_CARGO = "Cargo";
		public static final String COLUMN_CORREO = "Correo";
	}
	
	public static abstract class Agenda implements BaseColumns {
		
		public static final String TABLE_NAME = "tbAgenda";
		
		public static final String COLUMN_RUTA_ID = "IdRuta";
		public static final String COLUMN_AGENDA_ID = "IdAgenda";
		public static final String COLUMN_CLIENTE_ID = "IdCliente";
		public static final String COLUMN_CLIENTE_ID_EXT = "_IdCliente";
		public static final String COLUMN_CLIENTE_DIRECCION_ID = "IdClienteDireccion";
		public static final String COLUMN_CLIENTE_DIRECCION_ID_EXT = "_IdClienteDireccion";
		public static final String COLUMN_PROGRAMACION_RUTA_ID = "IdProgramacionRuta";
		public static final String COLUMN_FECHA_INICIO = "FechaInicio";
		public static final String COLUMN_FECHA_FIN = "FechaFin";
		public static final String COLUMN_ESTADO_AGENDA = "EstadoAgenda";
		public static final String COLUMN_FECHA_INICIO_REAL = "FechaInicioReal";
		public static final String COLUMN_FECHA_FIN_REAL = "FechaFinReal";
		public static final String COLUMN_ENVIADO = "Enviado";
		public static final String COLUMN_CONTACTO_ID = "IdContacto";
		public static final String COLUMN_CONTACTO_ID_EXT = "_IdContacto";
		public static final String COLUMN_OBSERVACIONES = "Observaciones";
		public static final String COLUMN_FOTO1_EXT = "Foto1Ext";
		public static final String COLUMN_FOTO1_INT = "Foto1Int";
		public static final String COLUMN_FOTO2_EXT = "Foto2Ext";
		public static final String COLUMN_FOTO2_INT = "Foto2Int";
		public static final String COLUMN_FOTO3_EXT = "Foto3Ext";
		public static final String COLUMN_FOTO3_INT = "Foto3Int";
		public static final String COLUMN_MOTIVO_NO_VISITA_ID = "IdMotivoNoVisita";
		public static final String COLUMN_LONGITUD = "Longitud";
		public static final String COLUMN_LATITUD = "Latitud";
        public static final String COLUMN_DISTANCIA = "Distancia";
        public static final String COLUMN_DURACION = "Duracion";
        public static final String COLUMN_TIEMPO_VIAJE= "TiempoViaje";
        public static final String COLUMN_MOTIVO_REPROGRAMACION= "IdMotivoReprogramar";
		public static final String COLUMN_FECHA_CREACION= "FechaCreacion";
		public static final String COLUMN_VALOR_VENTA= "ValorVenta";
		
		public static final String FIELD_RUTA_ID = COLUMN_RUTA_ID;
		public static final String FIELD_AGENDA_ID = COLUMN_AGENDA_ID;
		public static final String FIELD_CLIENTE_ID = COLUMN_CLIENTE_ID;
		public static final String FIELD_CLIENTE_DIRECCION_ID = COLUMN_CLIENTE_DIRECCION_ID;
		public static final String FIELD_PROGRAMACION_RUTA_ID = COLUMN_PROGRAMACION_RUTA_ID;
		public static final String FIELD_FECHA_INCICIO = COLUMN_FECHA_INICIO;
		public static final String FIELD_FECHA_FIN = COLUMN_FECHA_FIN;
		public static final String FIELD_ESTADO_AGENDA = COLUMN_ESTADO_AGENDA;
		public static final String FIELD_FECHA_INICIO_REAL = COLUMN_FECHA_INICIO_REAL;
		public static final String FIELD_FECHA_FIN_REAL = COLUMN_FECHA_FIN_REAL;
		public static final String FIELD_ENVIADO = COLUMN_ENVIADO;
		public static final String FIELD_CONTACTO_ID = COLUMN_CONTACTO_ID;
		public static final String FIELD_OBSERVACIONES = COLUMN_OBSERVACIONES;
		public static final String FIELD_FOTO1_EXT = COLUMN_FOTO1_EXT;
		public static final String FIELD_FOTO1_INT = COLUMN_FOTO1_INT;
		public static final String FIELD_FOTO2_EXT = COLUMN_FOTO2_EXT;
		public static final String FIELD_FOTO2_INT = COLUMN_FOTO2_INT;
		public static final String FIELD_FOTO3_EXT = COLUMN_FOTO3_EXT;
		public static final String FIELD_FOTO3_INT = COLUMN_FOTO3_INT;
		public static final String FIELD_MOTIVO_NO_VISITA_ID = COLUMN_MOTIVO_NO_VISITA_ID;
		public static final String FIELD_LONGITUD = COLUMN_LONGITUD;
		public static final String FIELD_LATITUD = COLUMN_LATITUD;
        public static final String FIELD_DISTANCIA = COLUMN_DISTANCIA;
        public static final String FIELD_DURACION = COLUMN_DURACION;
        public static final String FIELD_TIEMPO_VIAJE = COLUMN_TIEMPO_VIAJE;
        public static final String FIELD_MOTIVO_REPROGRAMACION = COLUMN_MOTIVO_REPROGRAMACION;
		public static final String FIELD_CLIENTE_URL_FOTO = Contract.Cliente.TABLE_NAME + "_" + Contract.Cliente.COLUMN_URL_FOTO;
		public static final String FIELD_CLIENTE_NOMBRE = Contract.AgendaExt.TABLE_NAME + "_" + Contract.AgendaExt.COLUMN_NOMBRE;		
		public static final String FIELD_CLIENTE_DIRECCION = Contract.AgendaExt.TABLE_NAME + "_" + Contract.AgendaExt.COLUMN_DIRECCION;
		public static final String FIELD_CLIENTE_DIRECCION_TELEFONO = Contract.ClienteDireccion.TABLE_NAME + "_" + Contract.ClienteDireccion.COLUMN_TELEFONO1;
		public static final String FIELD_CLIENTE_CIUDAD = Contract.AgendaExt.TABLE_NAME + "_" + Contract.AgendaExt.COLUMN_CIUDAD;
		public static final String FIELD_CLIENTE_CORREO_ELECTRONICO = Contract.ClientExt.TABLE_NAME + "_" + Contract.ClientExt.COLUMN_CORREO_ELECTRONICO;
		public static final String FIELD_ESTADO_AGENDA_DESCRIPCION = "tbEstadoAgenda_" + rp3.data.models.Contract.GeneralValue.COLUMN_VALUE;
		public static final String FIELD_TIPO_AGENDA = "TipoAgenda";
		
		public static final String QUERY_AGENDA = "AgendaByCliente";
        public static final String QUERY_AGENDA_UPLOAD = "AgendaUpload";
        public static final String QUERY_AGENDA_DIAS = "AgendasDias";
        public static final String QUERY_AGENDA_PENDIENTES = "AgendaPendientes";
        public static final String QUERY_AGENDA_NO_CLIENTE = "AgendaByClienteNull";
        public static final String QUERY_AGENDA_DASHBOARD = "AgendaDashboard";
		public static final String QUERY_AGENDA_SEMANAL = "AgendaSemanal";
		public static final String QUERY_AGENDA_ID = "AgendaByAgendaID";
		public static final String QUERY_AGENDA_ID_SERVER = "AgendaByAgendaIDServer";
		public static final String QUERY_AGENDA_TICKS = "AgendaByTicks";
		public static final String QUERY_AGENDA_SEARCH = "SimpleRutaSearch";
		public static final String QUERY_CONTEO = "AgendaConteo";
	} 
	
	public static abstract class AgendaExt implements BaseColumns {
    	public static final String TABLE_NAME = "tbAgendaExt";
    	
    	public static final String COLUMN_ID = "docid";
    	public static final String COLUMN_AGENDA_ID = Contract.Agenda.TABLE_NAME+"_"+Contract.Agenda._ID;
    	public static final String COLUMN_NOMBRE = "NombreCompleto";
    	public static final String COLUMN_DIRECCION = "Direccion";
        public static final String COLUMN_CIUDAD = "Ciudad";
        public static final String COLUMN_ESTADO_AGENDA = Contract.Agenda.TABLE_NAME+"_"+Contract.Agenda.COLUMN_ESTADO_AGENDA;
        public static final String COLUMN_FECHA_INICIO= Contract.Agenda.TABLE_NAME+"_"+Contract.Agenda.COLUMN_FECHA_INICIO;
        public static final String COLUMN_FECHA_FIN = Contract.Agenda.TABLE_NAME+"_"+Contract.Agenda.COLUMN_FECHA_FIN;
    }

    public static abstract class AgendaTarea implements BaseColumns {
		
		public static final String TABLE_NAME = "tbAgendaTarea";
				
		public static final String COLUMN_RUTA_ID = "IdRuta";
		public static final String COLUMN_AGENDA_ID = "IdAgenda";
		public static final String COLUMN_AGENDA_ID_EXT = "_IdAgenda";
		public static final String COLUMN_TAREA_ID = "IdTarea";
		public static final String COLUMN_ESTADO_TAREA = "EstadoTarea";		
		
		public static final String FIELD_RUTA_ID = COLUMN_RUTA_ID;
		public static final String FIELD_AGENDA_ID = COLUMN_AGENDA_ID;
		public static final String FIELD_ESTADO_TAREA = COLUMN_ESTADO_TAREA;
		public static final String FIELD_TAREA_ID = COLUMN_TAREA_ID;
		public static final String FIELD_ESTADO_TAREA_DESCRIPCION = "tbEstadoTarea_Value_" + rp3.data.models.Contract.GeneralValue.COLUMN_VALUE;
		
		public static final String QUERY_AGENDA_TAREA = "AgendaTarea";
		public static final String QUERY_AGENDA_TAREA_INTERNO = "AgendaTareaIdInterno";
	}
    
   public static abstract class AgendaTareaActividades implements BaseColumns {
		
		public static final String TABLE_NAME = "tbAgendaTareaActividades";
		
		public static final String COLUMN_AGENDA_ID = "IdAgenda";
        public static final String COLUMN_AGENDA_ID_EXT = "_IdAgenda";
		public static final String COLUMN_RUTA_ID = "IdRuta";
		public static final String COLUMN_TAREA_ID = "IdTarea";
		public static final String COLUMN_TAREA_ACTIVIDAD_ID = "IdTareaActividad";
		public static final String COLUMN_RESULTADO = "Resultado";
        public static final String COLUMN_IDS_RESULTADO = "IdsResultados";
		
		public static final String FIELD_AGENDA_ID = COLUMN_AGENDA_ID;
        public static final String FIELD_AGENDA_ID_EXT = COLUMN_AGENDA_ID_EXT;
		public static final String FIELD_RUTA_ID = COLUMN_RUTA_ID;
		public static final String FIELD_TAREA_ID = COLUMN_TAREA_ID;
		public static final String FIELD_TAREA_ACTIVIDAD_ID = COLUMN_TAREA_ACTIVIDAD_ID;
		public static final String FIELD_RESULTADO = COLUMN_RESULTADO;
        public static final String FIELD_IDS_RESULTADO = COLUMN_IDS_RESULTADO;
	}
   
   public static abstract class AgendaTareaOpciones implements BaseColumns {
		
		public static final String TABLE_NAME = "tbAgendaTareaOpciones";
		
		public static final String COLUMN_DESCRIPCION = "Descripcion";
		public static final String COLUMN_AGENDA_ID = "IdAgenda";
		public static final String COLUMN_RUTA_ID = "IdRuta";
		public static final String COLUMN_TAREA_ID = "IdTarea";
		public static final String COLUMN_TAREA_ACTIVIDAD_ID = "IdTareaActividad";
		public static final String COLUMN_ORDEN = "Orden";
		
		public static final String FIELD_DESCRIPCION = COLUMN_DESCRIPCION;
		public static final String FIELD_AGENDA_ID = COLUMN_AGENDA_ID;
		public static final String FIELD_RUTA_ID = COLUMN_RUTA_ID;
		public static final String FIELD_TAREA_ID = COLUMN_TAREA_ID;
		public static final String FIELD_TAREA_ACTIVIDAD_ID = COLUMN_TAREA_ACTIVIDAD_ID;
		public static final String FIELD_ORDEN = COLUMN_ORDEN;
	}
   
   public static abstract class Tareas implements BaseColumns {
		
		public static final String TABLE_NAME = "tbTarea";
		
		public static final String COLUMN_TAREA_ID = "IdTarea";
		public static final String COLUMN_NOMBRE_TAREA = "NombreTarea";
		public static final String COLUMN_TIPO_TAREA = "TipoTarea";
		public static final String COLUMN_ESTADO_TAREA = "EstadoTarea";
        public static final String COLUMN_FECHA_VIGENCIA_DESDE = "FechaVigenciaDesde";
        public static final String COLUMN_FECHA_VIGENCIA_HASTA = "FechaVigenciaHasta";

        public static final String FIELD_NOMBRE_TAREA = COLUMN_NOMBRE_TAREA;
		public static final String FIELD_TIPO_TAREA = COLUMN_TIPO_TAREA;
		public static final String FIELD_ESTADO_TAREA = COLUMN_ESTADO_TAREA;
		public static final String FIELD_TAREA_ID = COLUMN_TAREA_ID;
		public static final String FIELD_ESTADO_TAREA_DESCRIPCION = "tbEstadoTarea_Value_" + rp3.data.models.Contract.GeneralValue.COLUMN_VALUE;
        public static final String FIELD_FECHA_VIGENCIA_DESDE = COLUMN_FECHA_VIGENCIA_DESDE;
        public static final String FIELD_FECHA_VIGENCIA_HASTA = COLUMN_FECHA_VIGENCIA_HASTA;
	}
   
   public static abstract class Actividades implements BaseColumns {
		
		public static final String TABLE_NAME = "tbActividad";
		
		public static final String COLUMN_DESCRIPCION = "Descripcion";
		public static final String COLUMN_TAREA_ID = "IdTarea";
		public static final String COLUMN_TAREA_ACTIVIDAD_ID = "IdTareaActividad";
		public static final String COLUMN_TAREA_ACTIVIDAD_PADRE_ID = "IdTareaActividadPadre";
		public static final String COLUMN_TIPO_ACTIVIDAD_ID = "IdTipoActividad";
		public static final String COLUMN_ORDEN = "Orden";
		public static final String COLUMN_TIPO = "Tipo";
	   	public static final String COLUMN_LIMITE = "Limite";
		
		public static final String FIELD_DESCRIPCION = COLUMN_DESCRIPCION;
		public static final String FIELD_TAREA_ID = COLUMN_TAREA_ID;
		public static final String FIELD_TAREA_ACTIVIDAD_ID = COLUMN_TAREA_ACTIVIDAD_ID;
		public static final String FIELD_TAREA_ACTIVIDAD_PADRE_ID = COLUMN_TAREA_ACTIVIDAD_PADRE_ID;
		public static final String FIELD_TIPO_ACTIVIDAD_ID = COLUMN_TIPO_ACTIVIDAD_ID;
		public static final String FIELD_ORDEN = COLUMN_ORDEN;
		public static final String FIELD_TIPO = COLUMN_TIPO;
	   	public static final String FIELD_LIMITE = COLUMN_LIMITE;
		
		public static final String QUERY_ACTIVIDADES_GRUPALES = "ActividadesGrupales";
        public static final String QUERY_ACTIVIDADES_NO_GRUPALES = "ActividadesSinGrupo";
		public static final String QUERY_ACTIVIDADES_HIJAS = "ActividadesHijas";
		public static final String QUERY_ACTIVIDAD_SIMPLE = "ActividadSimple";
		public static final String QUERY_ACTIVIDAD_SIMPLE_NO_GRUPAL = "ActividadSimpleNoGrupal";
	}
   
   public static abstract class AgentesResumen implements BaseColumns {
		
		public static final String TABLE_NAME = "tbAgentesResumen";
		
		public static final String COLUMN_ID_AGENTE = "IdAgente";
		public static final String COLUMN_NOMBRES= "Nombres";
		public static final String COLUMN_APELLIDOS= "Apellidos";
		public static final String COLUMN_FECHA = "Fecha";
		public static final String COLUMN_GESTIONADOS = "Gestionados";
		public static final String COLUMN_NO_GESTIONADOS = "NoGestionados";
		public static final String COLUMN_PENDIENTES = "Pendientes";
		
		public static final String FIELD_ID_AGENTE = COLUMN_ID_AGENTE;
		public static final String FIELD_NOMBRES = COLUMN_NOMBRES;
		public static final String FIELD_APELLIDOS = COLUMN_APELLIDOS;
		public static final String FIELD_FECHA = COLUMN_FECHA;
		public static final String FIELD_GESTIONADOS = COLUMN_GESTIONADOS;
		public static final String FIELD_NO_GESTIONADOS = COLUMN_NO_GESTIONADOS;
		public static final String FIELD_PENDIENTES = COLUMN_PENDIENTES;
		
		public static final String QUERY_RESUMEN = "ResumenAgentes";
	   public static final String QUERY_RESUMEN_ALL = "ResumenAgentesAll";
	}
   
   public static abstract class Ubicacion implements BaseColumns {
		
		public static final String TABLE_NAME = "tbUbicacion";
		
		public static final String COLUMN_LATITUD = "Latitud";
		public static final String COLUMN_LONGITUD= "Longitud";
		public static final String COLUMN_FECHA= "Fecha";
		public static final String COLUMN_PENDIENTE= "Pendiente";

		public static final String FIELD_LATITUD = COLUMN_LATITUD;
		public static final String FIELD_LONGITUD = COLUMN_LONGITUD;
		public static final String FIELD_FECHA = COLUMN_FECHA;

	}

    public static abstract class AgentesUbicacion implements BaseColumns {

        public static final String TABLE_NAME = "tbAgentesUbicacion";

        public static final String COLUMN_ID_AGENTE = "IdAgente";
        public static final String COLUMN_NOMBRES= "Nombres";
        public static final String COLUMN_APELLIDOS= "Apellidos";
        public static final String COLUMN_FECHA = "Fecha";
        public static final String COLUMN_LATITUD = "Latitud";
        public static final String COLUMN_LONGITUD= "Longitud";

        public static final String FIELD_ID_AGENTE = COLUMN_ID_AGENTE;
        public static final String FIELD_NOMBRES = COLUMN_NOMBRES;
        public static final String FIELD_APELLIDOS = COLUMN_APELLIDOS;
        public static final String FIELD_FECHA = COLUMN_FECHA;
        public static final String FIELD_LATITUD = COLUMN_LATITUD;
        public static final String FIELD_LONGITUD = COLUMN_LONGITUD;

    }

    public static abstract class Campos implements BaseColumns {

        public static final String TABLE_NAME = "tbCampos";

        public static final String COLUMN_ID_CAMPO = "IdCampo";
        public static final String COLUMN_CREACION = "Creacion";
        public static final String COLUMN_MODIFICACION = "Modificacion";
        public static final String COLUMN_GESTION = "Gestion";
        public static final String COLUMN_OBLIGATORIO = "Obligatorio";

        public static final String FIELD_ID_CAMPO = COLUMN_ID_CAMPO;
        public static final String FIELD_CREACION = COLUMN_CREACION;
        public static final String FIELD_MODIFICACION = COLUMN_MODIFICACION;
        public static final String FIELD_GESTION = COLUMN_GESTION;
        public static final String FIELD_OBLIGATORIO = COLUMN_OBLIGATORIO;

    }

    public static abstract class DiaLaboral implements BaseColumns {

        public static final String TABLE_NAME = "tbDiaLaboral";

        public static final String COLUMN_ID_DIA = "IdDia";
        public static final String COLUMN_ORDEN = "Orden";
        public static final String COLUMN_ES_LABORAL = "EsLaboral";
        public static final String COLUMN_HORA_INICIO1 = "HoraInicio1";
        public static final String COLUMN_HORA_FIN1 = "HoraFin1";
        public static final String COLUMN_HORA_INICIO2 = "HoraInicio2";
        public static final String COLUMN_HORA_FIN2 = "HoraFin2";

    }

    public static abstract class DiaNoLaboral implements BaseColumns {

        public static final String TABLE_NAME = "tbDiaNoLaboral";

        public static final String COLUMN_FECHA = "Fecha";
        public static final String COLUMN_DIA_PARCIAL = "DiaParcial";
        public static final String COLUMN_ESTE_ANIO = "EsteAno";
        public static final String COLUMN_HORA_INICIO = "HoraInicio";
        public static final String COLUMN_HORA_FIN = "HoraFin";

    }

    public static abstract class Agente implements BaseColumns {

        public static final String TABLE_NAME = "tbAgente";

        public static final String COLUMN_ID_AGENTE = "IdAgente";
        public static final String COLUMN_NOMBRE = "Nombre";
        public static final String COLUMN_TELEFONO = "Telefono";
        public static final String COLUMN_EMAIL = "Email";

        public static final String FIELD_NOMBRE = COLUMN_NOMBRE;
        public static final String FIELD_ID_AGENTE = COLUMN_ID_AGENTE;

    }

    /*
    Aqui empieza modulo de oportunidades
     */

    public static abstract class Oportunidad implements BaseColumns {

        public static final String TABLE_NAME = "tbOportunidad";

        public static final String COLUMN_ID_OPORTUNIDAD = "IdOportunidad";
        public static final String COLUMN_PROBABILIDAD = "Probabilidad";
        public static final String COLUMN_IMPORTE = "Importe";
        public static final String COLUMN_ID_AGENTE = "IdAgente";
        public static final String COLUMN_FECHA_ULTIMA_GESTION = "FechaUltimaGestion";
        public static final String COLUMN_CALIFICACION = "Calificacion";
        public static final String COLUMN_OBSERVACION = "Observacion";
        public static final String COLUMN_LATITUD = "Latitud";
        public static final String COLUMN_LONGITUD = "Longitud";
        public static final String COLUMN_ID_ETAPA = "IdEtapa";
        public static final String COLUMN_ESTADO = "Estado";
        public static final String COLUMN_PENDIENTE = "Pendiente";
        public static final String COLUMN_FECHA_CREACION = "FechaCreacion";
		public static final String COLUMN_ID_OPORTUNIDAD_TIPO = "IdOportunidadTipo";
		public static final String COLUMN_ID_CANAL = "IdCanal";
		public static final String COLUMN_TIPO_PERSONA = "TipoPersona";
		public static final String COLUMN_CANAL_INFOR = "CanalInfor";

        public static final String FIELD_ID_OPORTUNIDAD = COLUMN_ID_OPORTUNIDAD;
        public static final String FIELD_PROBABILIDAD = COLUMN_PROBABILIDAD;
        public static final String FIELD_IMPORTE = COLUMN_IMPORTE;
        public static final String FIELD_ID_AGENTE = COLUMN_ID_AGENTE;
        public static final String FIELD_FECHA_ULTIMA_GESTION = COLUMN_FECHA_ULTIMA_GESTION;
        public static final String FIELD_OBSERVACION = COLUMN_OBSERVACION;
        public static final String FIELD_LATITUD = COLUMN_LATITUD;
        public static final String FIELD_CALIFICACION = COLUMN_CALIFICACION;
        public static final String FIELD_LONGITUD = COLUMN_LONGITUD;
        public static final String FIELD_ID_ETAPA = COLUMN_ID_ETAPA;
        public static final String FIELD_ESTADO = COLUMN_ESTADO;
        public static final String FIELD_PENDIENTE = COLUMN_PENDIENTE;
        public static final String FIELD_FECHA_CREACION = COLUMN_FECHA_CREACION;
		public static final String FIELD_ID_OPORTUNIDAD_TIPO = COLUMN_ID_OPORTUNIDAD_TIPO;
		public static final String FIELD_ID_CANAL = COLUMN_ID_CANAL;
		public static final String FIELD_TIPO_PERSONA = COLUMN_TIPO_PERSONA;
		public static final String FIELD_CANAL_INFOR = COLUMN_CANAL_INFOR;

        public static final String FIELD_DESCRIPCION = Contract.OportunidadExt.TABLE_NAME + "_" + Contract.OportunidadExt.COLUMN_DESCRIPCION;
        public static final String FIELD_DIRECCION = Contract.OportunidadExt.TABLE_NAME + "_" + Contract.OportunidadExt.COLUMN_DIRECCION;
        public static final String FIELD_REFERENCIA = Contract.OportunidadExt.TABLE_NAME + "_" + Contract.OportunidadExt.COLUMN_REFERENCIA;
        public static final String FIELD_DIRECCION_REFERENCIA = Contract.OportunidadExt.TABLE_NAME + "_" + Contract.OportunidadExt.COLUMN_DIRECCION_REFERENCIA;
        public static final String FIELD_TELEFONO1 = Contract.OportunidadExt.TABLE_NAME + "_" + Contract.OportunidadExt.COLUMN_TELEFONO1;
        public static final String FIELD_TELEFONO2 = Contract.OportunidadExt.TABLE_NAME + "_" + Contract.OportunidadExt.COLUMN_TELEFONO2;
        public static final String FIELD_PAGINA_WEB = Contract.OportunidadExt.TABLE_NAME + "_" + Contract.OportunidadExt.COLUMN_PAGINA_WEB;
        public static final String FIELD_CORREO = Contract.OportunidadExt.TABLE_NAME + "_" + Contract.OportunidadExt.COLUMN_CORREO;
        public static final String FIELD_TIPO_EMPRESA = Contract.OportunidadExt.TABLE_NAME + "_" + Contract.OportunidadExt.COLUMN_TIPO_EMPRESA;
		public static final String FIELD_CANAL = Contract.OportunidadExt.TABLE_NAME + "_" + Contract.OportunidadExt.COLUMN_CANAL;

        public static final String QUERY_LIST_NO_FILTER= "OportunidadesNoFilter";
        public static final String QUERY_LIST_BY_IDS= "OportunidadesByIds";
        public static final String QUERY_SEARCH= "OportunidadSearch";
        public static final String QUERY_OPORTUNIDAD_BY_ID= "OportunidadById";
        public static final String QUERY_OPORTUNIDADES_PENDIENTES= "OportunidadPendientes";
        public static final String QUERY_OPORTUNIDADES_INSERTS= "OportunidadInserts";

    }

    public static abstract class OportunidadExt implements BaseColumns {
        public static final String TABLE_NAME = "tbOportunidadExt";
        public static final String COLUMN_ID = "docid";
        public static final String COLUMN_DESCRIPCION = "Descripcion";
        public static final String COLUMN_DIRECCION = "Direccion";
        public static final String COLUMN_REFERENCIA = "Referencia";
        public static final String COLUMN_DIRECCION_REFERENCIA = "DireccionReferencia";
        public static final String COLUMN_TELEFONO1 = "Telefono1";
        public static final String COLUMN_TELEFONO2 = "Telefono2";
        public static final String COLUMN_PAGINA_WEB = "PaginaWeb";
        public static final String COLUMN_CORREO = "Correo";
        public static final String COLUMN_TIPO_EMPRESA = "TipoEmpresa";
		public static final String COLUMN_CANAL = "Canal";
    }

    public static abstract class OportunidadFoto implements BaseColumns {

        public static final String TABLE_NAME = "tbOportunidadFoto";

        public static final String COLUMN_ID_OPORTUNIDAD_FOTO = "IdOportunidadFoto";
        public static final String COLUMN_ID_OPORTUNIDAD = "IdOportunidad";
        public static final String COLUMN_ID_OPORTUNIDAD_INT = "_IdOportunidad";
        public static final String COLUMN_URL_FOTO = "URLFoto";

        public static final String FIELD_ID_OPORTUNIDAD_FOTO = COLUMN_ID_OPORTUNIDAD_FOTO;
        public static final String FIELD_ID_OPORTUNIDAD = COLUMN_ID_OPORTUNIDAD;
        public static final String FIELD_ID_OPORTUNIDAD_INT = COLUMN_ID_OPORTUNIDAD_INT;
        public static final String FIELD_URL_FOTO = COLUMN_URL_FOTO;

    }

    public static abstract class OportunidadContacto implements BaseColumns {

        public static final String TABLE_NAME = "tbOportunidadContacto";

        public static final String COLUMN_ID_OPORTUNIDAD_CONTACTO = "IdOportunidadContacto";
        public static final String COLUMN_ID_OPORTUNIDAD = "IdOportunidad";
        public static final String COLUMN_ID_OPORTUNIDAD_INT = "_IdOportunidad";
        public static final String COLUMN_NOMBRE = "Nombre";
        public static final String COLUMN_CARGO = "Cargo";
        public static final String COLUMN_URL_FOTO = "URLFoto";
		public static final String COLUMN_TELEFONO = "Telefono";
		public static final String COLUMN_EMAIL = "Email";

        public static final String FIELD_ID_OPORTUNIDAD_CONTACTO = COLUMN_ID_OPORTUNIDAD_CONTACTO;
        public static final String FIELD_ID_OPORTUNIDAD = COLUMN_ID_OPORTUNIDAD;
        public static final String FIELD_ID_OPORTUNIDAD_INT = COLUMN_ID_OPORTUNIDAD_INT;
        public static final String FIELD_URL_FOTO = COLUMN_URL_FOTO;
        public static final String FIELD_NOMBRE = COLUMN_NOMBRE;
        public static final String FIELD_CARGO = COLUMN_CARGO;
		public static final String FIELD_TELEFONO = COLUMN_TELEFONO;
		public static final String FIELD_EMAIL = COLUMN_EMAIL;

    }

    public static abstract class Etapa implements BaseColumns {

        public static final String TABLE_NAME = "tbEtapa";

        public static final String COLUMN_ID_ETAPA = "IdEtapa";
        public static final String COLUMN_ID_ETAPA_PADRE = "IdEtapaPadre";
        public static final String COLUMN_DESCRIPCION = "Descripcion";
        public static final String COLUMN_ORDEN = "Orden";
        public static final String COLUMN_ESTADO = "Estado";
		public static final String COLUMN_ID_OPORTUNIDAD_TIPO = "IdOportunidadTipo";
		public static final String COLUMN_DIAS = "Dias";
		public static final String COLUMN_ES_VARIABLE = "EsVariable";

        public static final String FIELD_ID_ETAPA = COLUMN_ID_ETAPA;
        public static final String FIELD_ID_ETAPA_PADRE = COLUMN_ID_ETAPA_PADRE;
        public static final String FIELD_DESCRIPCION = COLUMN_DESCRIPCION;
        public static final String FIELD_ORDEN = COLUMN_ORDEN;
        public static final String FIELD_ESTADO = COLUMN_ESTADO;
		public static final String FIELD_ID_OPORTUNIDAD_TIPO = COLUMN_ID_OPORTUNIDAD_TIPO;
		public static final String FIELD_DIAS = COLUMN_DIAS;

    }

    public static abstract class EtapaTarea implements BaseColumns {

        public static final String TABLE_NAME = "tbEtapaTarea";

        public static final String COLUMN_ID_ETAPA = "IdEtapa";
        public static final String COLUMN_ID_TAREA = "IdTarea";
        public static final String COLUMN_ORDEN = "Orden";

        public static final String FIELD_ID_ETAPA = COLUMN_ID_ETAPA;
        public static final String FIELD_ID_TAREA = COLUMN_ID_TAREA;
        public static final String FIELD_ORDEN = COLUMN_ORDEN;

        public static final String QUERY_ETAPAS= "EtapaTareasQuery";

    }

    public static abstract class OportunidadTarea implements BaseColumns {

        public static final String TABLE_NAME = "tbOportunidadTarea";

        public static final String COLUMN_ID_OPORTUNIDAD = "IdOportunidad";
        public static final String COLUMN_ID_OPORTUNIDAD_INT = "_IdOportunidad";
        public static final String COLUMN_ID_ETAPA = "IdEtapa";
        public static final String COLUMN_ID_TAREA = "IdTarea";
        public static final String COLUMN_ORDEN = "Orden";
        public static final String COLUMN_OBSERVACION = "Observacion";
        public static final String COLUMN_ESTADO = "Estado";

        public static final String FIELD_ID_OPORTUNIDAD = COLUMN_ID_OPORTUNIDAD;
        public static final String FIELD_ID_OPORTUNIDAD_INT = COLUMN_ID_OPORTUNIDAD_INT;
        public static final String FIELD_ID_ETAPA = COLUMN_ID_ETAPA;
        public static final String FIELD_ID_TAREA = COLUMN_ID_TAREA;
        public static final String FIELD_ORDEN = COLUMN_ORDEN;
        public static final String FIELD_OBSERVACION = COLUMN_OBSERVACION;
        public static final String FIELD_ESTADO = COLUMN_ESTADO;

    }

    public static abstract class OportunidadEtapa implements BaseColumns {

        public static final String TABLE_NAME = "tbOportunidadEtapa";

        public static final String COLUMN_ID_OPORTUNIDAD = "IdOportunidad";
        public static final String COLUMN_ID_OPORTUNIDAD_INT = "_IdOportunidad";
        public static final String COLUMN_ID_ETAPA = "IdEtapa";
        public static final String COLUMN_FECHA_INICIO= "FechaInicio";
        public static final String COLUMN_FECHA_FIN = "FechaFin";
        public static final String COLUMN_OBSERVACION = "Observacion";
        public static final String COLUMN_ESTADO = "Estado";
        public static final String COLUMN_ID_ETAPA_PADRE = "IdEtapaPadre";
		public static final String COLUMN_FECHA_FIN_PLAN = "FechaFinPlan";
		public static final String COLUMN_ID_AGENDA = "IdAgenda";

        public static final String FIELD_ID_OPORTUNIDAD = COLUMN_ID_OPORTUNIDAD;
        public static final String FIELD_ID_OPORTUNIDAD_INT = COLUMN_ID_OPORTUNIDAD_INT;
        public static final String FIELD_ID_ETAPA = COLUMN_ID_ETAPA;
        public static final String FIELD_FECHA_INICIO = COLUMN_FECHA_INICIO;
        public static final String FIELD_FECHA_FIN = COLUMN_FECHA_FIN;
        public static final String FIELD_OBSERVACION = COLUMN_OBSERVACION;
        public static final String FIELD_ESTADO = COLUMN_ESTADO;
        public static final String FIELD_ID_ETAPA_PADRE = COLUMN_ID_ETAPA_PADRE;

    }

    public static abstract class OportunidadTareaActividad implements BaseColumns {

        public static final String TABLE_NAME = "tbOportunidadTareaActividad";

        public static final String COLUMN_ID_OPORTUNIDAD = "IdOportunidad";
        public static final String COLUMN_ID_OPORTUNIDAD_INT = "_IdOportunidad";
        public static final String COLUMN_ID_ETAPA = "IdEtapa";
        public static final String COLUMN_ID_TAREA = "IdTarea";
        public static final String COLUMN_TAREA_ACTIVIDAD_ID = "IdTareaActividad";
        public static final String COLUMN_RESULTADO = "Resultado";
        public static final String COLUMN_IDS_RESULTADO = "IdsResultados";

        public static final String FIELD_ID_OPORTUNIDAD = COLUMN_ID_OPORTUNIDAD;
        public static final String FIELD_ID_OPORTUNIDAD_INT = COLUMN_ID_OPORTUNIDAD_INT;
        public static final String FIELD_ID_ETAPA = COLUMN_ID_ETAPA;
        public static final String FIELD_ID_TAREA = COLUMN_ID_TAREA;
        public static final String FIELD_TAREA_ACTIVIDAD_ID = COLUMN_TAREA_ACTIVIDAD_ID;
        public static final String FIELD_RESULTADO = COLUMN_RESULTADO;
        public static final String FIELD_IDS_RESULTADO = COLUMN_IDS_RESULTADO;

    }

    public static abstract class OportunidadResponsable implements BaseColumns {

        public static final String TABLE_NAME = "tbOportunidadResponsable";

        public static final String COLUMN_ID_OPORTUNIDAD = "IdOportunidad";
        public static final String COLUMN_ID_OPORTUNIDAD_INT = "_IdOportunidad";
        public static final String COLUMN_ID_AGENTE = "IdAgente";
        public static final String COLUMN_TIPO = "Tipo";

        public static final String FIELD_ID_OPORTUNIDAD = COLUMN_ID_OPORTUNIDAD;
        public static final String FIELD_ID_OPORTUNIDAD_INT = COLUMN_ID_OPORTUNIDAD_INT;
        public static final String FIELD_ID_AGENTE = COLUMN_ID_AGENTE;
        public static final String FIELD_TIPO = COLUMN_TIPO;

    }

	public static abstract class OportunidadBitacora implements BaseColumns {

		public static final String TABLE_NAME = "tbOportunidadBitacora";

		public static final String COLUMN_ID_OPORTUNIDAD = "IdOportunidad";
		public static final String COLUMN_ID_OPORTUNIDAD_INT = "_IdOportunidad";
		public static final String COLUMN_ID_OPORTUNIDAD_BITACORA = "IdOportunidadBitacora";
		public static final String COLUMN_ID_AGENTE = "IdAgente";
		public static final String COLUMN_DETALLE = "Detalle";
		public static final String COLUMN_FECHA = "FechaInicio";
		public static final String COLUMN_ID_AGENDA = "IdAgenda";

		public static final String FIELD_ID_OPORTUNIDAD = COLUMN_ID_OPORTUNIDAD;
		public static final String FIELD_ID_OPORTUNIDAD_INT = COLUMN_ID_OPORTUNIDAD_INT;
		public static final String FIELD_ID_OPORTUNIDAD_BITACORA = COLUMN_ID_OPORTUNIDAD_BITACORA;
		public static final String FIELD_ID_AGENTE = COLUMN_ID_AGENTE;
		public static final String FIELD_DETALLE= COLUMN_DETALLE;
		public static final String FIELD_FECHA = COLUMN_FECHA;

	}

	public static abstract class OportunidadTipo implements BaseColumns {

		public static final String TABLE_NAME = "tbOportunidadTipo";

		public static final String COLUMN_DESCRIPCION = "Descripcion";
		public static final String COLUMN_ID_OPORTUNIDAD_TIPO = "IdOportunidadTipo";

		public static final String FIELD_DESCRIPCION = COLUMN_DESCRIPCION;
		public static final String FIELD_ID_OPORTUNIDAD_TIPO = COLUMN_ID_OPORTUNIDAD_TIPO;

	}
    //MODULO MARCACIONES

    public static abstract class Marcacion implements BaseColumns {

        public static final String TABLE_NAME = "tbMarcacion";

        public static final String COLUMN_TIPO = "Tipo";
        public static final String COLUMN_FECHA = "Fecha";
        public static final String COLUMN_HORA_INICIO = "HoraInicio";
        public static final String COLUMN_HORA_FIN = "HoraFin";
        public static final String COLUMN_LATITUD = "Latitud";
        public static final String COLUMN_LONGITUD = "Longitud";
        public static final String COLUMN_EN_UBICACION = "EnUbicacion";
        public static final String COLUMN_MINUTOS_ATRASO = "MinutosAtraso";
        public static final String COLUMN_PENDIENTE = "Pendiente";

        public static final String FIELD_TIPO = COLUMN_TIPO;
        public static final String FIELD_FECHA = COLUMN_FECHA;
        public static final String FIELD_HORA_INICIO = COLUMN_HORA_INICIO;
        public static final String FIELD_HORA_FIN = COLUMN_HORA_FIN;
        public static final String FIELD_LATITUD = COLUMN_LATITUD;
        public static final String FIELD_LONGITUD = COLUMN_LONGITUD;
        public static final String FIELD_EN_UBICACION = COLUMN_EN_UBICACION;
        public static final String FIELD_PENDIENTE = COLUMN_PENDIENTE;
        public static final String FIELD_MINUTOS_ATRASO = COLUMN_MINUTOS_ATRASO;

    }

    public static abstract class Incidencia implements BaseColumns {

        public static final String TABLE_NAME = "tbIncidencia";

        public static final String COLUMN_TIPO = "Tipo";
        public static final String COLUMN_OBSERVACION = "Observacion";

        public static final String FIELD_TIPO = COLUMN_TIPO;
        public static final String FIELD_OBSERVACION = COLUMN_OBSERVACION;

    }

    public static abstract class Permiso implements BaseColumns {

        public static final String TABLE_NAME = "tbPermiso";

        public static final String COLUMN_TIPO = "Tipo";
        public static final String COLUMN_FECHA = "Fecha";
        public static final String COLUMN_OBSERVACION = "Observacion";
        public static final String COLUMN_ID_MARCACION = "IdMarcacion";
        public static final String COLUMN_ID_PERMISO = "IdPermiso";

        public static final String FIELD_TIPO = COLUMN_TIPO;
        public static final String FIELD_OBSERVACION = COLUMN_OBSERVACION;
        public static final String FIELD_FECHA = COLUMN_FECHA;
        public static final String FIELD_ID_MARCACION= COLUMN_ID_MARCACION;
    }

    public static abstract class Justificaciones implements BaseColumns {

        public static final String TABLE_NAME = "tbJustificacion";

        public static final String COLUMN_TIPO = "Tipo";
        public static final String COLUMN_FECHA = "Fecha";
        public static final String COLUMN_OBSERVACION = "Observacion";
        public static final String COLUMN_OBSERVACION_SUPERVISOR = "ObservacionSupervisor";
        public static final String COLUMN_ID_AGENTE = "IdAgente";
        public static final String COLUMN_ID_PERMISO = "IdPermiso";
        public static final String COLUMN_AUSENCIA = "Ausencia";
        public static final String COLUMN_ESTADO = "Estado";
        public static final String COLUMN_PENDIENTE = "Pendiente";
        public static final String COLUMN_PROPIA = "Propia";
        public static final String COLUMN_JORNADA = "Jornada";

        public static final String FIELD_TIPO = COLUMN_TIPO;
        public static final String FIELD_OBSERVACION = COLUMN_OBSERVACION;
        public static final String FIELD_FECHA = COLUMN_FECHA;

        public static final String QUERY_PERMISOS_POR_APROBAR = "PermisosAgentes";
		public static final String QUERY_PERMISOS_POR_APROBAR_COUNT = "PermisosAgentesCount";
        public static final String QUERY_PERMISO_POR_AGENTE = "PermisoPorAgente";
		public static final String QUERY_PERMISO_POR_AGENTE_SERVER = "PermisoPorAgenteServer";
    }

	/* Modulo Pedidos */

	public static abstract class Pedido implements BaseColumns {

		public static final String TABLE_NAME = "tbPedido";

		public static final String COLUMN_ID_PEDIDO = "IdPedido";
		public static final String COLUMN_ID_AGENDA = "IdAgenda";
		public static final String COLUMN_ID_CLIENTE = "IdCliente";
		public static final String COLUMN_VALOR_TOTAL = "ValorTotal";
		public static final String COLUMN_EMAIL = "Email";
		public static final String COLUMN_ESTADO = "Estado";
		public static final String COLUMN_FECHA_CREACION = "FechaCreacion";
		public static final String COLUMN_ID_AGENDA_INT = "_IdAgenda";
		public static final String COLUMN_ID_CLIENTE_INT = "_IdCliente";
		public static final String COLUMN_TOTAL_DESCUENTOS = "TotalDescuentos";
		public static final String COLUMN_TOTAL_IMPUESTOS = "TotalImpuestos";
		public static final String COLUMN_REDONDEO = "Redondeo";
		public static final String COLUMN_SUBTOTAL = "Subtotal";
		public static final String COLUMN_SUBTOTAL_SIN_DESCUENTO = "SubtotalSinDescuento";
		public static final String COLUMN_EXCEDENTE = "Excedente";
		public static final String COLUMN_TIPO_DOCUMENTO = "TipoDocumento";
		public static final String COLUMN_BASE_IMPONIBLE = "BaseImponible";
		public static final String COLUMN_BASE_IMPONIBLE_CERO = "BaseImponibleCero";
		public static final String COLUMN_OBSERVACIONES = "Observaciones";
		public static final String COLUMN_MOTIVO_ANULACION = "MotivoAnulacion";
		public static final String COLUMN_OBSERVACION_ANULACION = "ObservacionAnulacion";
		public static final String COLUMN_ID_DOCUMENTO_REF_INT = "_IdDocumentoRef";
		public static final String COLUMN_ID_DOCUMENTO_REF = "IdDocumentoRef";
		public static final String COLUMN_FECHA_ANULACION = "FechaAnulacion";
		public static final String COLUMN_TOTAL_IMPUESTO2 = "TotalImpuesto2";
		public static final String COLUMN_TOTAL_IMPUESTO3 = "TotalImpuesto3";
		public static final String COLUMN_TOTAL_IMPUESTO4 = "TotalImpuesto4";
		public static final String COLUMN_ID_CONTROL_CAJA_INT = "_IdControlCaja";
		public static final String COLUMN_PENDIENTE = "Pendiente";
		public static final String COLUMN_TIENE_NOTA_CREDITO_RP3 = "TieneNotaCreditoPOSRP3";
		public static final String COLUMN_SERIE = "Serie";
		public static final String COLUMN_TIPO_ORDEN = "TipoOrden";
		public static final String COLUMN_CIUDAD_DESPACHO = "Ciudad";
		public static final String COLUMN_ID_DIRECCION = "IdDireccion";

		public static final String FIELD_NUMERO_DOCUMENTO = Contract.PedidoExt.TABLE_NAME + "_" + Contract.PedidoExt.COLUMN_NUMERO_DOCUMENTO;
		public static final String FIELD_NOMBRE = Contract.PedidoExt.TABLE_NAME + "_" + Contract.PedidoExt.COLUMN_NOMBRE;

		public static final String QUERY_PEDIDOS_SEARCH = "SimplePedidoSearch";
		public static final String QUERY_PEDIDOS = "PedidosAll";
		public static final String QUERY_PEDIDOS_BY_ID = "PedidoById";
		public static final String QUERY_PEDIDOS_REPETIDO = "PedidoSecuenciaRepetida";
		public static final String QUERY_PEDIDOS_BY_ID_SERVER = "PedidoByIdServer";
		public static final String QUERY_PEDIDOS_BY_DOCUMENTO = "PedidoByDocumento";
		public static final String QUERY_PEDIDOS_PENDIENTES = "PedidosPendientes";
		public static final String QUERY_PEDIDO_BY_AGENDA = "PedidoByAgenda";

	}

	public static abstract class PedidoExt implements BaseColumns {

		public static final String TABLE_NAME = "tbPedidoExt";
		public static final String COLUMN_ID = "docid";
		public static final String COLUMN_NOMBRE = "Nombre";
		public static final String COLUMN_NUMERO_DOCUMENTO = "NumeroDocumento";

	}

	public static abstract class PedidoDetalle implements BaseColumns {

		public static final String TABLE_NAME = "tbPedidoDetalle";

		public static final String COLUMN_ID_PEDIDO = "IdPedido";
		public static final String COLUMN_ID_PEDIDO_INT = "_IdPedido";
		public static final String COLUMN_ID_PEDIDO_DETALLE = "IdPedidoDetalle";
		public static final String COLUMN_ID_PRODUCTO = "IdProducto";
		public static final String COLUMN_DESCRIPCION = "Descripcion";
		public static final String COLUMN_VALOR_UNITARIO = "ValorUnitario";
		public static final String COLUMN_CANTIDAD = "Cantidad";
		public static final String COLUMN_VALOR_TOTAL = "ValorTotal";
		public static final String COLUMN_URL_FOTO = "URLFoto";
		public static final String COLUMN_SUBTOTAL = "Subtotal";
		public static final String COLUMN_SUBTOTAL_SIN_DESCUENTO = "SubtotalSinDescuento";
		public static final String COLUMN_SUBTOTAL_SIN_IMPUESTO = "SubtotalSinImpuesto";
		public static final String COLUMN_BASE_IMPONIBLE = "BaseImponible";
		public static final String COLUMN_BASE_IMPONIBLE_CERO= "BaseImponibleCero";
		public static final String COLUMN_PORCENTAJE_DESCUENTO_ORO = "PorcDescOro";
		public static final String COLUMN_VALOR_DESCUENTO_ORO = "ValorDescOro";
		public static final String COLUMN_VALOR_DESCUENTO_ORO_TOTAL = "ValorDescOroTotal";
		public static final String COLUMN_PORCENTAJE_DESCUENTO_MANUAL = "PorcDescManual";
		public static final String COLUMN_VALOR_DESCUENTO_MANUAL = "ValorDescManual";
		public static final String COLUMN_VALOR_DESCUENTO_MANUAL_TOTAL = "ValorDescManualTotal";
		public static final String COLUMN_VALOR_DESC_AUTOMATICO = "ValorDescAutomatico";
		public static final String COLUMN_VALOR_DESC_AUTOMATICO_TOTAL = "ValorDescAutomaticoTotal";
		public static final String COLUMN_PORCENTAJE_DESCUENTO_AUTOMATICO = "PorcDescAutomatico";
		public static final String COLUMN_PORCENTAJE_IMPUESTO = "PorcImpuestoIvaVenta";
		public static final String COLUMN_VALOR_IMPUESTO = "ValorImpuestoIvaVenta";
		public static final String COLUMN_VALOR_IMPUESTO_TOTAL = "ValorImpuestoIvaVentaTotal";
		public static final String COLUMN_CANTIDAD_DEVOLUCION = "CantidadDevolucion";
		public static final String COLUMN_BASE_ICE = "BaseICE";
		public static final String COLUMN_ID_BENEFICIO = "IdBeneficio";
		public static final String COLUMN_USR_DESC_MANUAL = "UsrAutorizaDescManual";
		public static final String COLUMN_ID_VENDEDOR = "IdVendedor";
		public static final String COLUMN_LIBRO_PRECIO = "LibroPrecio";
		public static final String COLUMN_PARAR_DESCUENTO = "PararDescuento";
	}

	public static abstract class Producto implements BaseColumns {

		public static final String TABLE_NAME = "tbProducto";

		public static final String COLUMN_ID_PRODUCTO = "IdProducto";
		public static final String COLUMN_DESCRIPCION = "Descripcion";
		public static final String COLUMN_VALOR_UNITARIO = "ValorUnitario";
		public static final String COLUMN_URL_FOTO = "URLFoto";
		public static final String COLUMN_ID_SUBCATEGORIA = "IdSubCategoria";
		public static final String COLUMN_CODIGO_EXTERNO = "CodigoExterno";
		public static final String COLUMN_PRECIO_IMPUESTO = "PrecioConImpuesto";
		public static final String COLUMN_PRECIO_DESCUENTO = "PrecioConDescuento";
		public static final String COLUMN_PORCENTAJE_DESCUENTO_ORO = "PorcentajeDescuentoOro";
		public static final String COLUMN_PORCENTAJE_IMPUESTO = "PorcentajeImpuesto";
		public static final String COLUMN_PORCENTAJE_DESCUENTO = "PorcentajeDescuento";
		public static final String COLUMN_ID_BENEFICIO = "IdBeneficio";
		public static final String COLUMN_FAMILIA = "Familia";
		public static final String COLUMN_LINEA = "Linea";
		public static final String COLUMN_GRUPO_COMISION= "GrupoComision";
		public static final String COLUMN_APLICACION = "Aplicacion";
		public static final String COLUMN_AVISO = "Aviso";

		public static final String FIELD_DESCRIPCION = Contract.ProductoExt.TABLE_NAME + "_" + Contract.ProductoExt.COLUMN_DESCRIPCION;
		public static final String FIELD_EXTERNO_1 = Contract.ProductoExt.TABLE_NAME + "_" + Contract.ProductoExt.COLUMN_ID_EXTERNO_1;
		public static final String FIELD_EXTERNO_2 = Contract.ProductoExt.TABLE_NAME + "_" + Contract.ProductoExt.COLUMN_ID_EXTERNO_2;
		public static final String FIELD_GRUPO_ESTADISTICO = Contract.ProductoExt.TABLE_NAME + "_" + ProductoExt.COLUMN_GRUPO_ESTADISTICO;

		public static final String QUERY_PRODUCTOS = "Productos";
		public static final String QUERY_PRODUCTOS_BY_CATEGORIA = "ProductosByCategoria";
		public static final String QUERY_PRODUCTO_BY_ID = "ProductoByID";
		public static final String QUERY_PRODUCTO_BY_ID_SERVER = "ProductoByIdServer";
		public static final String QUERY_SEARCH = "SimpleProductoSearch";
		public static final String QUERY_SEARCH_BY_CATEGORIA = "SimpleProductoSearchByCategoria";
		public static final String QUERY_SEARCH_BY_CODIGO_EXTERNO = "ProductoSearchCodigo";
		public static final String QUERY_SEARCH_BY_CODIGO_EXTERNO_NO_SERIE = "ProductoSearchCodigoSinSerie";
		public static final String QUERY_SEARCH_NO_SERIE = "SimpleProductoSearchNoSerie";
		public static final String BULK_INSERT = "ProductoBulkInsert";
	}

	public static abstract class ProductoExt implements BaseColumns {

		public static final String TABLE_NAME = "tbProductoExt";
		public static final String COLUMN_ID = "docid";
		public static final String COLUMN_DESCRIPCION = "Descripcion";
		public static final String COLUMN_ID_EXTERNO_1 = "IdExterno1";
		public static final String COLUMN_ID_EXTERNO_2 = "IdExterno2";
		public static final String COLUMN_GRUPO_ESTADISTICO = "GrupoEstadistico";

		public static final String BULK_INSERT = "ProductoSearchBulkInsert";

	}

	public static abstract class Categoria implements BaseColumns {

		public static final String TABLE_NAME = "tbCategoria";

		public static final String COLUMN_ID_CATEGORIA = "IdCategoria";
		public static final String COLUMN_DESCRIPCION = "Descripcion";

	}

	public static abstract class SubCategoria implements BaseColumns {

		public static final String TABLE_NAME = "tbSubCategoria";

		public static final String COLUMN_ID_SUBCATEGORIA = "IdSubCategoria";
		public static final String COLUMN_ID_CATEGORIA = "IdCategoria";
		public static final String COLUMN_DESCRIPCION = "Descripcion";

	}

	public static abstract class FormaPago implements BaseColumns {

		public static final String TABLE_NAME = "tbFormaPago";

		public static final String COLUMN_ID_FORMA_PAGO = "IdFormaPago";
		public static final String COLUMN_DESCRIPCION = "Descripcion";
		public static final String COLUMN_ESTADO = "Estado";

	}

	public static abstract class Pago implements BaseColumns {

		public static final String TABLE_NAME = "tbPago";

		public static final String COLUMN_ID_PAGO = "IdPago";
		public static final String COLUMN_ID_PEDIDO = "IdPedido";
		public static final String COLUMN_ID_PEDIDO_INT = "_IdPedido";
		public static final String COLUMN_ID_FORMA_PAGO = "IdFormaPago";
		public static final String COLUMN_OBSERVACION = "Observacion";
		public static final String COLUMN_VALOR = "Valor";
		public static final String COLUMN_ID_BANCO = "IdBanco";
		public static final String COLUMN_ID_MARCA_TARJETA = "IdMarcaTarjeta";
		public static final String COLUMN_NUMERO_CUENTA = "NumeroCuenta";
		public static final String COLUMN_NUMERO_DOCUMENTO = "NumeroDocumento";
		public static final String COLUMN_AUTORIZADOR_TARJETA = "AutorizadorTarjeta";
		public static final String COLUMN_ID_TIPO_DIFERIDO = "IdTipoDiferido";
		public static final String COLUMN_CODIGO_SEGURIDAD = "CodigoSeguridad";

		public static final String QUERY_ARQUEO = "ArqueoCajaQuery";

	}

	public static abstract class ControlCaja implements BaseColumns {

		public static final String TABLE_NAME = "tbControlCaja";

		public static final String COLUMN_ID_CONTROL_CAJA = "IdControlCaja";
		public static final String COLUMN_ID_AGENTE = "IdAgente";
		public static final String COLUMN_ID_FECHA_APERTURA = "FechaApertura";
		public static final String COLUMN_ID_FECHA_CIERRE = "FechaCierre";
		public static final String COLUMN_VALOR_APERTURA = "ValorApertura";
		public static final String COLUMN_VALOR_CIERRE = "ValorCierre";
		public static final String COLUMN_ID_CAJA = "IdCaja";

	}

	public static abstract class ProductoCodigo implements BaseColumns {

		public static final String TABLE_NAME = "tbProductoCodigo";

		public static final String COLUMN_CODIGO_EXTERNO = "CodigoExterno";
		public static final String COLUMN_CODIGO = "Codigo";


		public static final String BULK_INSERT = "ProductoCodigoBulkInsert";

	}

	public static abstract class Banco implements BaseColumns {

		public static final String TABLE_NAME = "tbBanco";

		public static final String COLUMN_ID_BANCO = "IdBanco";
		public static final String COLUMN_DESCRIPCION = "Descripcion";

	}

	public static abstract class MarcaTarjeta implements BaseColumns {

		public static final String TABLE_NAME = "tbMarcaTarjeta";

		public static final String COLUMN_ID_MARCA_TARJETA = "IdMarcaTarjeta";
		public static final String COLUMN_DESCRIPCION = "Descripcion";

		public static final String QUERY_TARJETAS_POR_BANCO = "TarjetasPorBanco";

	}

	public static abstract class Tarjeta implements BaseColumns {

		public static final String TABLE_NAME = "tbTarjeta";

		public static final String COLUMN_ID_BANCO = "IdBanco";
		public static final String COLUMN_ID_MARCA_TARJETA = "IdMarcaTarjeta";
		public static final String COLUMN_INTERNA = "Interna";

	}

	public static abstract class TipoDiferido implements BaseColumns {

		public static final String TABLE_NAME = "tbTipoDiferido";

		public static final String COLUMN_ID_TIPO_DIFERIDO = "IdTipoDiferido";
		public static final String COLUMN_DESCRIPCION = "Descripcion";
		public static final String COLUMN_CUOTAS = "Cuotas";
		public static final String COLUMN_TIPO_CREDITO = "TipoCredito";

		public static final String QUERY_TIPOS_DIFERIDOS = "TiposDiferidosPorBancoTarjeta";

	}

	public static abstract class TarjetaComision implements BaseColumns {

		public static final String TABLE_NAME = "tbTarjetaComision";

		public static final String COLUMN_ID_EMPRESA = "IdEmpresa";
		public static final String COLUMN_ID_BANCO = "IdBanco";
		public static final String COLUMN_ID_MARCA_TARJETA = "IdMarcaTarjeta";
		public static final String COLUMN_ID_TIPO_DIFERIDO = "IdTipoDiferido";

	}

	public static abstract class ProductoPromocion implements BaseColumns {

		public static final String TABLE_NAME = "tbProductoPromocion";

		public static final String COLUMN_ID_PRODUCTO = "IdProducto";
		public static final String COLUMN_ID_ESTABLECIMIENTO = "IdEstablecimiento";
		public static final String COLUMN_ID_PUNTO_OPERACION = "IdPuntoOperacion";
		public static final String COLUMN_ID_BENEFICIO = "IdBeneficio";
		public static final String COLUMN_FECHA_DESDE = "FechaDesde";
		public static final String COLUMN_FECHA_HASTA = "FechaHasta";
		public static final String COLUMN_PORCENTAJE_DESCUENTO = "PorcentajeDescuento";
		public static final String COLUMN_FORMA_PAGO_APLICA = "FormaPagoAplica";

	}

	public static abstract class Vendedor implements BaseColumns {

		public static final String TABLE_NAME = "tbVendedor";

		public static final String COLUMN_ID_VENDEDOR = "IdVendedor";

	}

	//GRUPO BERLIN
	public static abstract class AgendaOportunidad implements BaseColumns {

		public static final String TABLE_NAME = "tbAgendaOportunidad";

		public static final String COLUMN_ID_OPORTUNIDAD = "IdOportunidad";
		public static final String COLUMN_ID_AGENDA = "IdAgenda";
		public static final String COLUMN_ESTADO = "EstadoAgenda";
		public static final String COLUMN_FECHA_INICIO = "FechaInicio";
		public static final String COLUMN_FECHA_FIN = "FechaFin";
		public static final String COLUMN_LATITUD = "Latitud";
		public static final String COLUMN_LONGITUD = "Longitud";
		public static final String COLUMN_PENDIENTE = "Pendiente";
		public static final String COLUMN_ID_OPORTUNIDAD_INT = "_IdOportunidad";

		public static final String FIELD_ID_OPORTUNIDAD = COLUMN_ID_OPORTUNIDAD;
		public static final String FIELD_ID_AGENDA= COLUMN_ID_AGENDA;
		public static final String FIELD_ESTADO = COLUMN_ESTADO;
		public static final String FIELD_FECHA_INICIO = COLUMN_FECHA_INICIO;
		public static final String FIELD_FECHA_FIN = COLUMN_FECHA_FIN;
		public static final String FIELD_LATITUD = COLUMN_LATITUD;
		public static final String FIELD_LONGITUD = COLUMN_LONGITUD;
		public static final String FIELD_PENDIENTE = COLUMN_PENDIENTE;
		public static final String FIELD_ID_OPORTUNIDAD_INT = COLUMN_ID_OPORTUNIDAD_INT;

		public static final String FIELD_DESCRIPCION = Contract.AgendaOportunidadExt.TABLE_NAME + "_" + Contract.AgendaOportunidadExt.COLUMN_DESCRIPCION;
		public static final String FIELD_DIRECCION = Contract.AgendaOportunidadExt.TABLE_NAME + "_" + Contract.AgendaOportunidadExt.COLUMN_DIRECCION;
		public static final String FIELD_CORREO = Contract.AgendaOportunidadExt.TABLE_NAME + "_" + Contract.AgendaOportunidadExt.COLUMN_CORREO;

		public static final String QUERY_GET_BY_ID = "AgendaOportunidadByID";
		public static final String QUERY_GET_PENDIENTES= "AgendaOportunidadPendientes";
	}

	public static abstract class AgendaOportunidadExt implements BaseColumns {
		public static final String TABLE_NAME = "tbAgendaOportunidadExt";
		public static final String COLUMN_ID = "docid";
		public static final String COLUMN_DESCRIPCION = "Descripcion";
		public static final String COLUMN_DIRECCION = "Direccion";
		public static final String COLUMN_CORREO = "Correo";
	}

	public static abstract class LibroPrecio implements BaseColumns {

		public static final String TABLE_NAME = "tbLibroPrecio";

		public static final String COLUMN_ID_LIBRO = "IdLibro";
		public static final String COLUMN_ITEM = "Item";
		public static final String COLUMN_DIVISA = "Divisa";
		public static final String COLUMN_PRECIO = "Precio";
		public static final String COLUMN_MEDIDA = "Medida";
		public static final String COLUMN_FECHA_EFECTIVA = "FechaEfectiva";
		public static final String COLUMN_FECHA_VENCIMIENTO = "FechaVencimiento";
		public static final String COLUMN_VALOR_ESCALADO = "ValorEscalado";
		public static final String COLUMN_TIPO_ESCALADO = "TipoEscalado";
		public static final String COLUMN_LIBRO_ESTANDAR = "LibroEstandar";

		public static final String BULK_INSERT = "LibroPrecioBulkInsert";
        public static final String QUERY_LIBRO_PRECIO = "LibroPrecio";
        public static final String QUERY_LIBRO_PRECIO_ESTANDAR = "LibroPrecioEstandar";
        public static final String QUERY_CONSULTA_REMATE = "ConsultaPrecioRemate";
        public static final String QUERY_CONSULTA_STANDARD = "ConsultaPrecioStandart";
        public static final String QUERY_CONSULTA_CLIENTE = "ConsultaPrecioCliente";

	}

	public static abstract class MatrizPrecio implements BaseColumns {

		public static final String TABLE_NAME = "tbMatrizPrecio";

		public static final String COLUMN_ID_MATRIZ = "IdMatriz";
		public static final String COLUMN_ID_LIBRO = "IdLibro";
		public static final String COLUMN_ID_CLIENTE = "IdCliente";
		public static final String COLUMN_ID_SECUENCIA = "Secuencia";
		public static final String COLUMN_ID_LISTA_PRECIO = "IdListaPrecio";
		public static final String COLUMN_PARAMETRO_DESC = "ParametroDesc";
		public static final String COLUMN_FECHA_EFECTIVA = "FechaEfectiva";
		public static final String COLUMN_FECHA_VENCIMIENTO = "FechaVencimiento";

	}

	public static abstract class SecuenciaMatriz implements BaseColumns {

		public static final String TABLE_NAME = "tbSecuenciaMatriz";

		public static final String COLUMN_ID_JERARQUIA = "IdJerarquia";
		public static final String COLUMN_MATRIZ = "Matriz";
		public static final String COLUMN_FECHA_EFECTIVA = "FechaEfectiva";
		public static final String COLUMN_FECHA_VENCIMIENTO = "FechaVencimiento";

	}

	public static abstract class Serie implements BaseColumns {

		public static final String TABLE_NAME = "tbSerie";

		public static final String COLUMN_ID_SERIE = "IdSerie";
		public static final String COLUMN_GRUPO_ESTADISTICO = "GrupoEstadistico";

	}

	public static abstract class Alternativo implements BaseColumns {

		public static final String TABLE_NAME = "tbAlternativo";

		public static final String COLUMN_ITEM = "Item";
		public static final String COLUMN_ALTERNO = "Alterno";
		public static final String COLUMN_FECHA_INGRESO = "FechaIngreso";
		public static final String COLUMN_FECHA_VENCIMIENTO = "FechaVencimiento";

	}

	public static abstract class AgenteDescuento implements BaseColumns {

		public static final String TABLE_NAME = "tbAgenteDescuento";

		public static final String COLUMN_CANAL = "Canal";
		public static final String COLUMN_LINEA = "Linea";
		public static final String COLUMN_TOPE = "Tope";
	}

	public static abstract class Stock implements BaseColumns {

		public static final String TABLE_NAME = "tbStock";

		public static final String COLUMN_ID_BODEGA = "IdBodega";
		public static final String COLUMN_ITEM = "Item";
		public static final String COLUMN_STOCK_DISPONIBLE = "StockDisponible";
		public static final String COLUMN_STOCK_FISICO = "StockFisico";
		public static final String COLUMN_FECHA = "Fecha";

		public static final String FIELD_BODEGA_DESCRIPCION = "tbBodega_Value";

		public static final String QUERY_GET_STOCK = "GetStock";
	}

	public static abstract class VentaPerdida implements BaseColumns {

		public static final String TABLE_NAME = "tbVentaPerdida";

		public static final String COLUMN_CODIGO_PRODUCTO= "CodigoProducto";
		public static final String COLUMN_FECHA = "Fecha";
		public static final String COLUMN_PENDIENTE = "Pendiente";
	}
}
