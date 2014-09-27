package rp3.marketforce.db;

import rp3.db.sqlite.DataBase;
import android.provider.BaseColumns;

public final class Contract {

	public static abstract class TipoCliente implements BaseColumns {
		
		public static final String TABLE_NAME = "tbTipoCliente";
		
		public static final String COLUMN_TIPO_CLIENTE_ID = "IdTipoCliente";
		public static final String COLUMN_DESCRIPCION = "Descripcion";
		
		public static final String FIELD_TIPO_CLIENTE_ID = COLUMN_TIPO_CLIENTE_ID;
		public static final String FIELD_DESCRIPCION = COLUMN_DESCRIPCION;	
	}
	
	public static abstract class Canal implements BaseColumns {
		
		public static final String TABLE_NAME = "tbCanal";
		
		public static final String COLUMN_CANAL_ID = "IdCanal";
		public static final String COLUMN_DESCRIPCION = "Descripcion";
		
		public static final String FIELD_CANAL_ID = COLUMN_CANAL_ID;
		public static final String FIELD_DESCRIPCION = COLUMN_DESCRIPCION;	
	}
	
	
	public static abstract class Cliente implements BaseColumns {
		
		public static final String TABLE_NAME = "tbCliente";
		
		public static final String COLUMN_CLIENTE_ID = "IdCliente";
		public static final String COLUMN_IDENTIFICATION_TYPE_ID = "IdentificationTypeId";
		public static final String COLUMN_IDENTIFICACION = "Identificacion";
		public static final String COLUMN_NOMBRE1 = "Nombre1";
		public static final String COLUMN_NOMBRE2 = "Nombre2";
		public static final String COLUMN_APELLIDO1 = "Apellido1";
		public static final String COLUMN_APELLIDO2 = "Apellido2";
		public static final String COLUMN_NOMBRE_COMPLETO = "NombreCompleto";
		public static final String COLUMN_CORREO_ELECTRONICO = "CorreoElectronico";
		public static final String COLUMN_GENERO = "Genero";
		public static final String COLUMN_ESTADO_CIVIL = "EstadoCivil";
		public static final String COLUMN_FECHA_NACIMIENTO = "FechaNacimiento";
		public static final String COLUMN_TIPO_CLIENTE_ID = "IdTipoCliente";
		public static final String COLUMN_CANAL_ID = "IdCanal";
		public static final String COLUMN_CALIFICACION = "Calificacion";
		public static final String COLUMN_USRING = "UsrIng";
		public static final String COLUMN_FECING = "FecIng";
		public static final String COLUMN_USRMOD = "UsrMod";
		public static final String COLUMN_FECMOD = "FecMod";
		
		public static final String FIELD_CLIENTE_ID = COLUMN_CLIENTE_ID;
		public static final String FIELD_IDENTIFICATION_TYPE_ID = COLUMN_IDENTIFICATION_TYPE_ID;
		public static final String FIELD_IDENTIFICACION = COLUMN_IDENTIFICACION;
		public static final String FIELD_NOMBRE1 = COLUMN_NOMBRE1;
		public static final String FIELD_NOMBRE2 = COLUMN_NOMBRE2;
		public static final String FIELD_APELLIDO1 = COLUMN_APELLIDO1;
		public static final String FIELD_APELLIDO2 = COLUMN_APELLIDO2;
		public static final String FIELD_NOMBRE_COMPLETO = COLUMN_NOMBRE_COMPLETO;
		public static final String FIELD_CORREO_ELECTRONICO = COLUMN_CORREO_ELECTRONICO;
		public static final String FIELD_GENERO = COLUMN_GENERO;
		public static final String FIELD_ESTADO_CIVIL = COLUMN_ESTADO_CIVIL;
		public static final String FIELD_FECHA_NACIMIENTO = COLUMN_FECHA_NACIMIENTO;
		public static final String FIELD_TIPO_CLIENTE_ID = COLUMN_TIPO_CLIENTE_ID;
		public static final String FIELD_CANAL_ID = COLUMN_CANAL_ID;
		public static final String FIELD_CALIFICACION = COLUMN_CALIFICACION;
		public static final String FIELD_USRING = COLUMN_USRING;
		public static final String FIELD_FECING = COLUMN_FECING;
		public static final String FIELD_USRMOD = COLUMN_USRMOD;
		public static final String FIELD_FECMOD = COLUMN_FECMOD;
		
		
		public static final String QUERY_CLIENT_SEARCH = "SimpleClientSearch";
		
		 private static final String QUERY_TRANSACTION_MAINFIELDS = 
	        		"SELECT " + 
	        				TABLE_NAME + "." + _ID + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_CLIENTE_ID + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_IDENTIFICATION_TYPE_ID + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_IDENTIFICACION + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_NOMBRE1 + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_NOMBRE2 + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_APELLIDO1 + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_APELLIDO2 + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_NOMBRE_COMPLETO + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_CORREO_ELECTRONICO + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_GENERO + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_ESTADO_CIVIL + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_FECHA_NACIMIENTO + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_TIPO_CLIENTE_ID + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_CANAL_ID + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_CALIFICACION + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_USRING + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_FECING + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_USRMOD + DataBase.COMMA_SEP +
	        				TABLE_NAME + "." + COLUMN_FECMOD;
		
		 private static final String QUERY_TRANSACTION_MAINFROM = 
	        		" FROM " + TABLE_NAME;
	        		
		 
		private static final String QUERY_FULL_FIELDS = 
				QUERY_TRANSACTION_MAINFIELDS;
		
		 private static final String QUERY_TRANSACTION_FULL = 
	        		QUERY_FULL_FIELDS;
		 
		 private static final String QUERY_FILTER_TRANSACTIONID = " WHERE " +  
	        		Contract.Cliente.TABLE_NAME + "." + Contract.Cliente._ID +
	        		" = ?";
		
		public static final String QUERY_TRANSACTION_BY_ID = 
        		QUERY_TRANSACTION_FULL +
        		QUERY_TRANSACTION_MAINFROM + 
        		QUERY_FILTER_TRANSACTIONID;
		
	}
	
	 public static abstract class ClientExt implements BaseColumns {
	    	public static final String TABLE_NAME = "tbClienteExt";
	    	public static final String COLUMN_ID = "docid";
	    	public static final String COLUMN_NAME = "Nombre1";
	    	public static final String COLUMN_LAST_NAME = "Apellido1";
	        public static final String COLUMN_IDENTIFICATION = "Identificacion";
	        public static final String COLUMN_MAIL = "CorreoElectronico";
	        public static final String COLUMN_FECING = "FecIng";
	        public static final String COLUMN_DIRECCION = "Direccion";
	        public static final String COLUMN_TELEFONO = "Telefono1";
	    }
	
	public static abstract class ClienteDireccion implements BaseColumns {
		
		public static final String TABLE_NAME = "tbClienteDireccion";
		
		public static final String COLUMN_CLIENTE_ID = "IdCliente";
		public static final String COLUMN_CLIENTE_DIRECCION_ID = "IdClienteDireccion";
		public static final String COLUMN_DIRECCION = "Direccion";  
		public static final String COLUMN_PRINCIPAL_ID = "IdPrincipal";
		public static final String COLUMN_TIPO_DIRECCION = "TipoDireccion";
		public static final String COLUMN_CIUDAD_ID = "IdCiudad";
		public static final String COLUMN_TELEFONO1 = "Telefono1";
		public static final String COLUMN_TELEFONO2 = "Telefono2";
		public static final String COLUMN_REFERENCIA = "Referencia";
		public static final String COLUMN_LATITUD = "Latitud";
		public static final String COLUMN_LONGITUD = "Longitud";
		
		public static final String FIELD_CLIENTE_ID = COLUMN_CLIENTE_ID;
		public static final String FIELD_CLIENTE_DIRECCION_ID = COLUMN_CLIENTE_DIRECCION_ID;
		public static final String FIELD_DIRECCION = COLUMN_DIRECCION;
		public static final String FIELD_PRINCIPAL_ID = COLUMN_PRINCIPAL_ID;
		public static final String FIELD_TIPO_DIRECCION = COLUMN_TIPO_DIRECCION;
		public static final String FIELD_CIUDAD_ID = COLUMN_CIUDAD_ID;
		public static final String FIELD_TELEFONO1 = COLUMN_TELEFONO1;
		public static final String FIELD_TELEFONO2 = COLUMN_TELEFONO2;
		public static final String FIELD_REFERENCIA = COLUMN_REFERENCIA;
		public static final String FIELD_LATITUD = COLUMN_LATITUD;
		public static final String FIELD_LONGITUD = COLUMN_LONGITUD;
	}
	
	public static abstract class Agenda implements BaseColumns {
		
		public static final String TABLE_NAME = "tbAgenda";
		
		public static final String COLUMN_RUTA_ID = "IdRuta";
		public static final String COLUMN_AGENDA_ID = "IdAgenda";
		public static final String COLUMN_CLIENTE_ID = "IdCliente";
		public static final String COLUMN_CLIENTE_DIRECCION_ID = "IdClienteDireccion";
		public static final String COLUMN_PROGRAMACION_RUTA_ID = "IdProgramacionRuta";
		public static final String COLUMN_FECHA_INICIO = "FechaInicio";
		public static final String COLUMN_FECHA_FIN = "FechaFin";
		public static final String COLUMN_ESTADO_AGENDA = "EstadoAgenda";
		
		public static final String FIELD_RUTA_ID = COLUMN_RUTA_ID;
		public static final String FIELD_AGENDA_ID = COLUMN_AGENDA_ID;
		public static final String FIELD_CLIENTE_ID = COLUMN_CLIENTE_ID;
		public static final String FIELD_CLIENTE_DIRECCION_ID = COLUMN_CLIENTE_DIRECCION_ID;
		public static final String FIELD_PROGRAMACION_RUTA_ID = COLUMN_PROGRAMACION_RUTA_ID;
		public static final String FIELD_FECHA_INCICIO = COLUMN_FECHA_INICIO;
		public static final String FIELD_FECHA_FIN = COLUMN_FECHA_FIN;
		public static final String FIELD_ESTADO_AGENDA = COLUMN_ESTADO_AGENDA;
		public static final String FIELD_CLIENTE_NOMBRE_CLIENTE = Contract.Cliente.TABLE_NAME + "_" + Contract.Cliente.COLUMN_NOMBRE_COMPLETO;
		public static final String FIELD_CLIENTE_CORREO_CLIENTE = Contract.Cliente.TABLE_NAME + "_" + Contract.Cliente.COLUMN_CORREO_ELECTRONICO;
		public static final String FIELD_CLIENTE_DIRECCION = Contract.ClienteDireccion.TABLE_NAME + "_" + Contract.ClienteDireccion.COLUMN_DIRECCION;
		public static final String FIELD_CLIENTE_TELEFONO = Contract.ClienteDireccion.TABLE_NAME + "_" + Contract.ClienteDireccion.COLUMN_TELEFONO1;
		
		public static final String QUERY_AGENDA = "AgendaByCliente";
		public static final String QUERY_AGENDA_ID = "AgendaByAgendaID";
		public static final String QUERY_AGENDA_SEARCH = "SimpleRutaSearch";
	} 
	
	public static abstract class AgendaExt implements BaseColumns {
    	public static final String TABLE_NAME = "tbAgendaExt";
    	
    	public static final String COLUMN_ID = "docid";
    	public static final String COLUMN_AGENDA_ID = Contract.Agenda.TABLE_NAME+"_"+Contract.Agenda._ID;
    	public static final String COLUMN_NAME = "NombreCompleto";
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
		public static final String COLUMN_CLIENTE_ID = "IdCliente";
		public static final String COLUMN_CLIENTE_DIRECCION_ID = "IdClienteDireccion";
		public static final String COLUMN_PROGRAMACION_RUTA_ID = "IdProgramacionRuta";
		public static final String COLUMN_NOMBRE_TAREA = "NombreTarea";
		public static final String COLUMN_TIPO_TAREA = "TipoTarea";
		
		public static final String FIELD_RUTA_ID = COLUMN_RUTA_ID;
		public static final String FIELD_AGENDA_ID = COLUMN_AGENDA_ID;
		public static final String FIELD_CLIENTE_ID = COLUMN_CLIENTE_ID;
		public static final String FIELD_CLIENTE_DIRECCION_ID = COLUMN_CLIENTE_DIRECCION_ID;
		public static final String FIELD_PROGRAMACION_RUTA_ID = COLUMN_PROGRAMACION_RUTA_ID;
		public static final String FIELD_NOMBRE_TAREA = COLUMN_NOMBRE_TAREA;
		public static final String FIELD_TIPO_TAREA = COLUMN_TIPO_TAREA;
		 
	}
}
