package rp3.marketforce.db;

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
		public static final String FIELD_ESTADO_CIVIL_DESCRIPCION = "tbEstadoCivil_Value";
		public static final String FIELD_GENERO_DESCRIPCION = "tbGenero_Value";
		public static final String FIELD_TIPO_CLIENTE_DESCRIPCION = Contract.TipoCliente.TABLE_NAME + "_" + Contract.TipoCliente.COLUMN_DESCRIPCION;
		public static final String FIELD_CANAL_DESCRIPCION = Contract.Canal.TABLE_NAME + "_" + Contract.Canal.COLUMN_DESCRIPCION;
		public static final String FIELD_TIPOIDENTIFICACION_NOMBRE = rp3.data.models.Contract.IdentificationType.TABLE_NAME + "_" + rp3.data.models.Contract.IdentificationType.COLUMN_NAME ;
		
		public static final String QUERY_CLIENTES = "Clientes";
		public static final String QUERY_CLIENTES_AND_CONTACTS = "ClientesAndContacts";
		public static final String QUERY_CLIENTE_BY_ID = "ClienteById";
		public static final String QUERY_CLIENTE_INSERTS = "ClienteInserts";
		public static final String QUERY_CLIENTE_PENDIENTES = "ClientePendientes";
		public static final String QUERY_CLIENTE_BY_ID_SERVER = "ClienteByIdServer";
		public static final String QUERY_CLIENT_SEARCH = "SimpleClientSearch";
		
//		 private static final String QUERY_TRANSACTION_MAINFIELDS = 
//	        		"SELECT " + 
//	        				TABLE_NAME + "." + _ID + DataBase.COMMA_SEP +	        				
//	        				TABLE_NAME + "." + COLUMN_IDENTIFICATION_TYPE_ID + DataBase.COMMA_SEP +
//	        				TABLE_NAME + "." + COLUMN_IDENTIFICACION + DataBase.COMMA_SEP +
//	        				TABLE_NAME + "." + COLUMN_NOMBRE1 + DataBase.COMMA_SEP +
//	        				TABLE_NAME + "." + COLUMN_NOMBRE2 + DataBase.COMMA_SEP +
//	        				TABLE_NAME + "." + COLUMN_APELLIDO1 + DataBase.COMMA_SEP +
//	        				TABLE_NAME + "." + COLUMN_APELLIDO2 + DataBase.COMMA_SEP +
//	        				TABLE_NAME + "." + COLUMN_NOMBRE_COMPLETO + DataBase.COMMA_SEP +
//	        				TABLE_NAME + "." + COLUMN_CORREO_ELECTRONICO + DataBase.COMMA_SEP +
//	        				TABLE_NAME + "." + COLUMN_GENERO + DataBase.COMMA_SEP +
//	        				TABLE_NAME + "." + COLUMN_ESTADO_CIVIL + DataBase.COMMA_SEP +
//	        				TABLE_NAME + "." + COLUMN_FECHA_NACIMIENTO + DataBase.COMMA_SEP +
//	        				TABLE_NAME + "." + COLUMN_TIPO_CLIENTE_ID + DataBase.COMMA_SEP +
//	        				TABLE_NAME + "." + COLUMN_CANAL_ID + DataBase.COMMA_SEP +
//	        				TABLE_NAME + "." + COLUMN_CALIFICACION;	        				
//		
//		 private static final String QUERY_TRANSACTION_MAINFROM = 
//	        		" FROM " + TABLE_NAME;
//	        		
//		 
//		private static final String QUERY_FULL_FIELDS = 
//				QUERY_TRANSACTION_MAINFIELDS;
//		
//		 private static final String QUERY_TRANSACTION_FULL = 
//	        		QUERY_FULL_FIELDS;
//		 
//		 private static final String QUERY_FILTER_TRANSACTIONID = " WHERE " +  
//	        		Contract.Cliente.TABLE_NAME + "." + Contract.Cliente._ID +
//	        		" = ?";
//		
//		public static final String QUERY_TRANSACTION_BY_ID = 
//        		QUERY_TRANSACTION_FULL +
//        		QUERY_TRANSACTION_MAINFROM + 
//        		QUERY_FILTER_TRANSACTIONID;
		
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
		
		public static final String QUERY_AGENDA = "AgendaByCliente";
        public static final String QUERY_AGENDA_UPLOAD = "AgendaUpload";
        public static final String QUERY_AGENDA_DIAS = "AgendasDias";
        public static final String QUERY_AGENDA_PENDIENTES = "AgendaPendientes";
        public static final String QUERY_AGENDA_NO_CLIENTE = "AgendaByClienteNull";
        public static final String QUERY_AGENDA_DASHBOARD = "AgendaDashboard";
		public static final String QUERY_AGENDA_SEMANAL = "AgendaSemanal";
		public static final String QUERY_AGENDA_ID = "AgendaByAgendaID";
		public static final String QUERY_AGENDA_ID_SERVER = "AgendaByAgendaIDServer";
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
}
