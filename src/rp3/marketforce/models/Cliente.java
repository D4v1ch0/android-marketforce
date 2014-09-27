package rp3.marketforce.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;
import android.database.Cursor;

public class Cliente extends rp3.data.entity.EntityBase<Cliente>{

	private long id;	
	private int identificationTypeId;
	private String identificacion;
	private String nombre1;
	private String nombre2;
	private String apellido1;
	private String apellido2;
	private String nombreCompleto;
	private String correoElectronico;
	private String genero;
	private String estadoCivil;
	private Date fechaNacimiento;
	private int idTipoCliente;
	private int idCanal;
	private int Calificacion;
	
	private String direccion;
	private String telefono;
	private String estadoCivilDescripcion;
	private String generoDescripcion;
	private String tipoClienteDescripcion;
	private String canalDescripcion;

	private List<ClienteDireccion> clienteDirecciones;
	private ClienteDireccion clienteDireccionesIDPrincipal;	
	
	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public boolean isAutoGeneratedId() {
		return false;
	}

	@Override
	public String getTableName() {
		return Contract.Cliente.TABLE_NAME;
	}

	@Override
	public void setValues() {
		setValue(Contract.Cliente._ID, this.id);		
		setValue(Contract.Cliente.COLUMN_IDENTIFICATION_TYPE_ID, this.identificationTypeId);		
		setValue(Contract.Cliente.COLUMN_GENERO, this.genero);
		setValue(Contract.Cliente.COLUMN_ESTADO_CIVIL, this.estadoCivil);
		setValue(Contract.Cliente.COLUMN_FECHA_NACIMIENTO, this.fechaNacimiento);
		setValue(Contract.Cliente.COLUMN_TIPO_CLIENTE_ID, this.idTipoCliente);
		setValue(Contract.Cliente.COLUMN_CANAL_ID, this.idCanal);
		setValue(Contract.Cliente.COLUMN_CALIFICACION, this.Calificacion);		
	}

	@Override
	public Object getValue(String key) {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}
	
	public int getIdentificationTypeId() {
		return identificationTypeId;
	}

	public void setIdentificationTypeId(int identificationTypeId) {
		this.identificationTypeId = identificationTypeId;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getNombre1() {
		return nombre1;
	}

	public void setNombre1(String nombre1) {
		this.nombre1 = nombre1;
	}

	public String getNombre2() {
		return nombre2;
	}

	public void setNombre2(String nombre2) {
		this.nombre2 = nombre2;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public int getIdTipoCliente() {
		return idTipoCliente;
	}

	public void setIdTipoCliente(int idTipoCliente) {
		this.idTipoCliente = idTipoCliente;
	}

	public int getIdCanal() {
		return idCanal;
	}

	public void setIdCanal(int idCanal) {
		this.idCanal = idCanal;
	}

	public int getCalificacion() {
		return Calificacion;
	}

	public void setCalificacion(int calificacion) {
		Calificacion = calificacion;
	}


	public List<ClienteDireccion> getClienteDirecciones() {
		
		if(clienteDirecciones == null)
			clienteDirecciones = new ArrayList<ClienteDireccion>();  
			
		return clienteDirecciones;
	}

	public void setClienteDirecciones(List<ClienteDireccion> clienteDirecciones) {
		this.clienteDirecciones = clienteDirecciones;
	}
	
	public ClienteDireccion getClienteDireccionesIDPrincipal() {
		return clienteDireccionesIDPrincipal;
	}

	public void setClienteDireccionesIDPrincipal(
			ClienteDireccion clienteDireccionesIDPrincipal) {
		this.clienteDireccionesIDPrincipal = clienteDireccionesIDPrincipal;
	}
	
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEstadoCivilDescripcion() {
		return estadoCivilDescripcion;
	}

	public void setEstadoCivilDescripcion(String estadoCivilDescripcion) {
		this.estadoCivilDescripcion = estadoCivilDescripcion;
	}

	public String getGeneroDescripcion() {
		return generoDescripcion;
	}

	public void setGeneroDescripcion(String generoDescripcion) {
		this.generoDescripcion = generoDescripcion;
	}

	public String getTipoClienteDescripcion() {
		return tipoClienteDescripcion;
	}

	public void setTipoClienteDescripcion(String tipoClienteDescripcion) {
		this.tipoClienteDescripcion = tipoClienteDescripcion;
	}

	public String getCanalDescripcion() {
		return canalDescripcion;
	}

	public void setCanalDescripcion(String canalDescripcion) {
		this.canalDescripcion = canalDescripcion;
	}

	public static List<Cliente> getCliente(DataBase db){
		
		String query = QueryDir.getQuery(Contract.Cliente.QUERY_CLIENTES);
		
		Cursor c = db.rawQuery(query);
		
		List<Cliente> list = new ArrayList<Cliente>();
		while(c.moveToNext()){
			Cliente cl = new Cliente();
			cl.setID(CursorUtils.getInt(c, Contract.Cliente._ID));			
//			cl.setIdentificationTypeId(CursorUtils.getInt(c, Contract.Cliente.FIELD_IDENTIFICATION_TYPE_ID));
			cl.setIdentificacion(CursorUtils.getString(c, Contract.Cliente.FIELD_IDENTIFICACION));
			cl.setNombre1(CursorUtils.getString(c, Contract.Cliente.FIELD_NOMBRE1));
			cl.setNombre2(CursorUtils.getString(c, Contract.Cliente.FIELD_NOMBRE2));
			cl.setApellido1(CursorUtils.getString(c, Contract.Cliente.FIELD_APELLIDO1));
			cl.setApellido2(CursorUtils.getString(c, Contract.Cliente.FIELD_APELLIDO2));
			cl.setNombreCompleto(CursorUtils.getString(c, Contract.Cliente.FIELD_NOMBRE_COMPLETO));
			cl.setCorreoElectronico(CursorUtils.getString(c, Contract.Cliente.FIELD_CORREO_ELECTRONICO));
			cl.setGenero(CursorUtils.getString(c, Contract.Cliente.FIELD_GENERO));
			cl.setEstadoCivil(CursorUtils.getString(c, Contract.Cliente.FIELD_ESTADO_CIVIL));
			cl.setFechaNacimiento(CursorUtils.getDate(c, Contract.Cliente.FIELD_FECHA_NACIMIENTO));
			cl.setIdTipoCliente(CursorUtils.getInt(c, Contract.Cliente.FIELD_TIPO_CLIENTE_ID));
			cl.setIdCanal(CursorUtils.getInt(c, Contract.Cliente.FIELD_CANAL_ID));
			cl.setCalificacion(CursorUtils.getInt(c, Contract.Cliente.FIELD_CALIFICACION));		
			
			cl.setTelefono(CursorUtils.getString(c, Contract.Cliente.FIELD_TELEFONO));
			cl.setDireccion(CursorUtils.getString(c, Contract.Cliente.FIELD_DIRECCION));
			cl.setEstadoCivilDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_ESTADO_CIVIL_DESCRIPCION));
			cl.setGeneroDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_GENERO_DESCRIPCION));
			cl.setTipoClienteDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_TIPO_CLIENTE_DESCRIPCION));
			cl.setCanalDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_CANAL_DESCRIPCION));
			
			
			list.add(cl);
		}
		return list;
	}

	
	public static Cliente getClienteID(DataBase db, long clientId , boolean incluirDirecciones)
	{		
		String query = QueryDir.getQuery(Contract.Cliente.QUERY_CLIENTE_BY_ID);
		 
		Cursor c = db.rawQuery(query, clientId );
		Cliente client = null;
		
		if(c.moveToFirst())
		{
			client =  new Cliente();
			client.setID(CursorUtils.getInt(c,Contract.Cliente._ID));			
//			client.setIdentificationTypeId(CursorUtils.getInt(c,Contract.Cliente.FIELD_IDENTIFICATION_TYPE_ID) );
			client.setIdentificacion( CursorUtils.getString(c,Contract.Cliente.FIELD_IDENTIFICACION) );
			client.setNombre1(CursorUtils.getString(c,Contract.Cliente.FIELD_NOMBRE1) );
			client.setNombre2(CursorUtils.getString(c,Contract.Cliente.FIELD_NOMBRE2) );
			client.setApellido1(CursorUtils.getString(c,Contract.Cliente.FIELD_APELLIDO1) );
			client.setApellido2(CursorUtils.getString(c,Contract.Cliente.FIELD_APELLIDO2) );
			client.setNombreCompleto(CursorUtils.getString(c,Contract.Cliente.FIELD_NOMBRE_COMPLETO) );
			client.setCorreoElectronico(CursorUtils.getString(c,Contract.Cliente.FIELD_CORREO_ELECTRONICO) );
			client.setGenero(CursorUtils.getString(c,Contract.Cliente.FIELD_GENERO) );
			client.setEstadoCivil(CursorUtils.getString(c,Contract.Cliente.FIELD_ESTADO_CIVIL) );
			client.setFechaNacimiento(CursorUtils.getDate(c,Contract.Cliente.FIELD_FECHA_NACIMIENTO) );
			client.setIdTipoCliente(CursorUtils.getInt(c,Contract.Cliente.FIELD_TIPO_CLIENTE_ID) );
			client.setIdCanal(CursorUtils.getInt(c,Contract.Cliente.FIELD_CANAL_ID) );
			client.setCalificacion(CursorUtils.getInt(c,Contract.Cliente.FIELD_CALIFICACION) );			
			
			client.setTelefono(CursorUtils.getString(c, Contract.Cliente.FIELD_TELEFONO));
			client.setDireccion(CursorUtils.getString(c, Contract.Cliente.FIELD_DIRECCION));
			client.setTelefono(CursorUtils.getString(c, Contract.Cliente.FIELD_TELEFONO));
			client.setEstadoCivilDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_ESTADO_CIVIL_DESCRIPCION));
			client.setGeneroDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_GENERO_DESCRIPCION));
			client.setTipoClienteDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_TIPO_CLIENTE_DESCRIPCION));
			client.setCanalDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_CANAL_DESCRIPCION));
						
			if(incluirDirecciones)
				client.setClienteDirecciones(ClienteDireccion.getClienteDirecciones(db, client.getID()));
			else
				client.setClienteDireccionesIDPrincipal(ClienteDireccion.getClienteDireccionIdPrincipal(db, client.getID()));
			
		}
		
		return client;
	}
	
	@Override
	protected boolean insertDb(DataBase db) {
		boolean result = false;
		
		try
		{
//			db.beginTransaction();
			result = super.insertDb(db);
			
			if(result){
				for(ClienteDireccion d : this.getClienteDirecciones()){
					d.setIdCliente(this.id);
					if(d.getID() == 0)
						result = ClienteDireccion.insert(db, d);
					else
						result = ClienteDireccion.update(db, d);
						
					if(!result) break;					
				}								
				
//				if(result)
//					db.commitTransaction();
				
				if(result)
				{
					ClientExt cl_ex = new ClientExt();
					result = ClientExt.insert(db, cl_ex);
				}
			}
			
			
						
		}catch(Exception ex){
			result = false;			
		}finally{						
		}
		return result;	
	}
	
	
	@Override
	protected boolean updateDb(DataBase db) {
		boolean result = false;
		
		try
		{
			db.beginTransaction();
			result = super.updateDb(db);
			
			if(result){
				for(ClienteDireccion d : this.getClienteDirecciones()){
					d.setIdCliente(this.id);
					if(d.getID() == 0)
						result = ClienteDireccion.insert(db, d);
					else
						result = ClienteDireccion.update(db, d);
						
					if(!result) break;					
				}								
				
				if(result)
					db.commitTransaction();								
			}					
						
		}catch(Exception ex){
			result = false;
//			db.rollBackTransaction();
		}finally{
//			db.endTransaction();			
		}
		return result;	
	}
	
	public class ClientExt extends EntityBase<ClientExt>{

		@Override
		public long getID() {
			return id;
		}

		@Override
		public void setID(long idext) {
			id = idext;
		}

		@Override
		public boolean isAutoGeneratedId() {
			return false;
		}

		@Override
		public String getTableName() {
			return Contract.ClientExt.TABLE_NAME;
		}

		@Override
		public void setValues() {
			setValue(Contract.ClientExt.COLUMN_ID , id);
			setValue(Contract.ClientExt.COLUMN_IDENTIFICACION, identificacion);
			setValue(Contract.ClientExt.COLUMN_NOMBRE1, nombre1);
			setValue(Contract.ClientExt.COLUMN_NOMBRE2, nombre2);
			setValue(Contract.ClientExt.COLUMN_APELLIDO1, apellido1);
			setValue(Contract.ClientExt.COLUMN_APELLIDO2, apellido2);
			setValue(Contract.ClientExt.COLUMN_DIRECCION, direccion);
			setValue(Contract.ClientExt.COLUMN_TELEFONO, telefono);
			setValue(Contract.ClientExt.COLUMN_NOMBRE_COMPLETO, nombreCompleto);
			setValue(Contract.ClientExt.COLUMN_CORREO_ELECTRONICO, correoElectronico);			
		}

		@Override
		public Object getValue(String key) {
			return null;
		}

		@Override
		public String getDescription() {
			return null;
		}
	}
	
	public static List<Cliente> getClientSearch(DataBase db, String termSearch)
	{		
		String query = QueryDir.getQuery( Contract.Cliente.QUERY_CLIENT_SEARCH );
		
		Cursor c = db.rawQuery(query, "*" + termSearch + "*" );
		
		List<Cliente> list = new ArrayList<Cliente>();
		while(c.moveToNext()){
			Cliente cl = new Cliente();
			cl.setID(c.getInt(0));
			cl.setNombre1(CursorUtils.getString(c,Contract.ClientExt.COLUMN_NOMBRE1));
			cl.setApellido1(CursorUtils.getString(c,Contract.ClientExt.COLUMN_APELLIDO1));
			cl.setIdentificacion(CursorUtils.getString(c,Contract.ClientExt.COLUMN_IDENTIFICACION));
			cl.setCorreoElectronico(CursorUtils.getString(c,Contract.ClientExt.COLUMN_CORREO_ELECTRONICO));	
			cl.setDireccion(CursorUtils.getString(c,Contract.ClientExt.COLUMN_DIRECCION));
			cl.setTelefono(CursorUtils.getString(c,Contract.ClientExt.COLUMN_TELEFONO));
			
			ClienteDireccion cd = new ClienteDireccion();
			cd.setDireccion(CursorUtils.getString(c,Contract.ClientExt.COLUMN_DIRECCION));
			cd.setTelefono1(CursorUtils.getString(c,Contract.ClientExt.COLUMN_TELEFONO));
			cl.setClienteDireccionesIDPrincipal(cd);
			
			list.add(cl);
		}
		return list;

	}
	
}
