package rp3.marketforce.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.db.Contract;
import rp3.util.Convert;
import rp3.util.CursorUtils;
import android.database.Cursor;

public class Cliente extends rp3.data.entity.EntityBase<Cliente>{
	
	public static int ACTION_SYNC = 6;

	private long id;	
	private long idCliente;	
	private int idTipoIdentificacion;
	private String identificacion;
	private String nombre1;
	private String nombre2;
	private String apellido1;
	private String apellido2;
	private String nombreCompleto;
	private String correoElectronico;
	private String genero;
	private String estadoCivil;
	private String paginaWeb;
	private String razonSocial;
	private String actividadEconomica;
	private Date fechaNacimiento;
	private int idTipoCliente;
	private int idCanal;
	private int Calificacion;
	private boolean nuevo;
	private boolean pendiente;
    private Date fechaUltimaVisita;
    private Date fechaProximaVisita;
    private String agenteUltimaVisita;
	
	private String direccion;
	private String telefono;
	private String estadoCivilDescripcion;
	private String generoDescripcion;
	private String tipoClienteDescripcion;
	private String canalDescripcion;
	private String tipoIdentificacionDescripcion;
	private String URLFoto;
	private String tipoPersona;

	private List<Contacto> contactos;
	private List<ClienteDireccion> clienteDirecciones;
	private ClienteDireccion clienteDireccionPrincipal;

    public Date getFechaUltimaVisita() {
        return fechaUltimaVisita;
    }

    public void setFechaUltimaVisita(Date fechaUltimaVisita) {
        this.fechaUltimaVisita = fechaUltimaVisita;
    }

    public Date getFechaProximaVisita() {
        return fechaProximaVisita;
    }

    public void setFechaProximaVisita(Date fechaProximaVisita) {
        this.fechaProximaVisita = fechaProximaVisita;
    }

    public String getAgenteUltimaVisita() {
        return agenteUltimaVisita;
    }

    public void setAgenteUltimaVisita(String agenteUltimaVisita) {
        this.agenteUltimaVisita = agenteUltimaVisita;
    }

    public static String getFotoFileNameFormat(Long id){
		return "PCL" + String.valueOf(id);
	}
	
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
		return true;
	}

	@Override
	public String getTableName() {
		return Contract.Cliente.TABLE_NAME;
	}

	@Override
	public void setValues() {		
		if(this.getAction() == ACTION_INSERT){			
			setValue(Contract.Cliente.COLUMN_ID_TIPO_IDENTIFICACION, this.idTipoIdentificacion);						
			setValue(Contract.Cliente.COLUMN_TIPO_CLIENTE_ID, this.idTipoCliente);
			setValue(Contract.Cliente.COLUMN_CANAL_ID, this.idCanal);
			setValue(Contract.Cliente.COLUMN_CALIFICACION, this.Calificacion);
		}
		setValue(Contract.Cliente.COLUMN_GENERO, this.genero);						
		setValue(Contract.Cliente.COLUMN_FECHA_NACIMIENTO, this.fechaNacimiento);		
		setValue(Contract.Cliente.COLUMN_ESTADO_CIVIL, this.estadoCivil);	
		setValue(Contract.Cliente.COLUMN_URL_FOTO, this.URLFoto);
		setValue(Contract.Cliente.COLUMN_TIPO_PERSONA, this.tipoPersona);
		setValue(Contract.Cliente.COLUMN_NUEVO, this.nuevo);
		setValue(Contract.Cliente.COLUMN_ID_CLIENTE, this.idCliente);
		setValue(Contract.Cliente.COLUMN_PENDIENTE, this.pendiente);
        setValue(Contract.Cliente.COLUMN_FECHA_ULTIMA_VISITA, this.fechaUltimaVisita);
        setValue(Contract.Cliente.COLUMN_FECHA_PROXIMA_VISITA, this.fechaProximaVisita);
        setValue(Contract.Cliente.COLUMN_AGENTE_ULTIMA_VISITA, this.agenteUltimaVisita);
	}

	@Override
	public Object getValue(String key) {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}
	
	public String getFotoFileName(){
		return getFotoFileNameFormat(this.id);
	}
	
	public int getTipoIdentificacionId() {
		return idTipoIdentificacion;
	}

	public void setIdTipoIdentificacion(int idTipoIdentificacion) {
		this.idTipoIdentificacion = idTipoIdentificacion;
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
	
	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}


	public List<ClienteDireccion> getClienteDirecciones() {
		
		if(clienteDirecciones == null)
			clienteDirecciones = new ArrayList<ClienteDireccion>();  
			
		return clienteDirecciones;
	}

	public void setClienteDirecciones(List<ClienteDireccion> clienteDirecciones) {
		this.clienteDirecciones = clienteDirecciones;
	}
	
	public ClienteDireccion getClienteDireccionPrincipal() {
		if(clienteDireccionPrincipal == null || !clienteDireccionPrincipal.getEsPrincipal()){
			for(ClienteDireccion d : getClienteDirecciones()){
				if(d.getEsPrincipal()){
					clienteDireccionPrincipal = d;
					break;
				}
			}
		}
		return clienteDireccionPrincipal;
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
	
	public String getTipoIdentificacionDescripcion() {
		return tipoIdentificacionDescripcion;
	}

	public void setTipoIdentificacionDescripcion(
			String tipoIdentificacionDescripcion) {
		this.tipoIdentificacionDescripcion = tipoIdentificacionDescripcion;
	}
	
	public String getURLFoto() {
		if(URLFoto != null)
			return URLFoto;
		else
			return "";
	}

	public void setURLFoto(String URLFoto) {
		this.URLFoto = URLFoto;
	}

	
	
	public String getPaginaWeb() {
		return paginaWeb;
	}

	public void setPaginaWeb(String paginaWeb) {
		this.paginaWeb = paginaWeb;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getActividadEconomica() {
		return actividadEconomica;
	}

	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	public static List<Long> getIDSCliente(DataBase db){
		Cursor c = db.query(Contract.Cliente.TABLE_NAME, Contract.Cliente._ID);
		List<Long> result = new ArrayList<Long>();
		while(c.moveToNext()){
			result.add(CursorUtils.getLong(c,Contract.Cliente._ID));
		}
		return result;
	}
	
	public static boolean getClienteNuevo(DataBase db, long id){
		Cursor c = db.query(Contract.Cliente.TABLE_NAME, new String[]{Contract.Cliente.COLUMN_NUEVO},
				Contract.Cliente._ID + " = ?", id);
		boolean rest = false;
		while(c.moveToNext()){
			rest = CursorUtils.getBoolean(c, Contract.Cliente.COLUMN_NUEVO);
		}
		return rest;
	}
	
	public static List<Cliente> getCliente(DataBase db){
		
		String query = QueryDir.getQuery(Contract.Cliente.QUERY_CLIENTES);
		
		Cursor c = db.rawQuery(query);
		
		List<Cliente> list = new ArrayList<Cliente>();
		while(c.moveToNext()){
			Cliente cl = new Cliente();
			cl.setID(CursorUtils.getInt(c, Contract.Cliente._ID));
			cl.setIdCliente(CursorUtils.getInt(c, Contract.Cliente.FIELD_ID_CLIENTE));
			//cl.setIdentificationTypeId(CursorUtils.getInt(c, Contract.Cliente.FIELD_IDENTIFICATION_TYPE_ID));
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
			cl.setURLFoto(CursorUtils.getString(c, Contract.Cliente.FIELD_URL_FOTO));
			cl.setTipoPersona(CursorUtils.getString(c, Contract.Cliente.FIELD_TIPO_PERSONA));
			cl.setClienteDirecciones(ClienteDireccion.getClienteDirecciones(db, cl.getIdCliente(), false));
			if(cl.getIdCliente() == 0)
				cl.setContactos(Contacto.getContactoIdCliente(db, cl.getID(), true));
			else
				cl.setContactos(Contacto.getContactoIdCliente(db, cl.getIdCliente(), false));
			
			
			list.add(cl);
		}
		return list;
	}
	
	public static List<Cliente> getClientAndContacts(DataBase db)
	{		
		String query = QueryDir.getQuery( Contract.Cliente.QUERY_CLIENTES_AND_CONTACTS );
		
		Cursor c = db.rawQuery(query);
		
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
			cl.setTipoPersona(CursorUtils.getString(c, Contract.Cliente.COLUMN_TIPO_PERSONA));
			cl.setIdCliente(CursorUtils.getInt(c, Contract.Cliente.COLUMN_ID_CLIENTE));
			if(cl.getTipoPersona().equalsIgnoreCase("C"))
				cl.setNombreCompleto(cl.getApellido1() + " " + cl.getNombre1() + " - " + CursorUtils.getString(c, Contract.ContactoExt.COLUMN_CARGO)
						+ " de " + getClienteIDServer(db, cl.getIdCliente(), false).getNombreCompleto().trim());
			else
				cl.setNombreCompleto(CursorUtils.getString(c, Contract.ClientExt.COLUMN_NOMBRE_COMPLETO));
			
			if(cl.getIdCliente() == 0)
				cl.setClienteDirecciones(ClienteDireccion.getClienteDirecciones(db, cl.getID(), true));
			else
				cl.setClienteDirecciones(ClienteDireccion.getClienteDirecciones(db, cl.getIdCliente(), false));
			
			
			list.add(cl);
		}
		return list;

	}

	
	public static Cliente getClienteID(DataBase db, long clientId , boolean incluirDirecciones)
	{		
		String query = QueryDir.getQuery(Contract.Cliente.QUERY_CLIENTE_BY_ID);
		 
		Cursor c = db.rawQuery(query, clientId );
		Cliente client = null;
		
		if(c.moveToFirst()){
			client =  new Cliente();
			client.setID(CursorUtils.getInt(c,Contract.Cliente._ID));
			client.setIdCliente(CursorUtils.getInt(c,Contract.Cliente.FIELD_ID_CLIENTE));
			client.setIdTipoIdentificacion(CursorUtils.getInt(c,Contract.Cliente.FIELD_ID_TIPO_IDENTIFICACION) );
			client.setTipoIdentificacionDescripcion(CursorUtils.getString(c,Contract.Cliente.FIELD_TIPOIDENTIFICACION_NOMBRE) );
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
			client.setURLFoto(CursorUtils.getString(c, Contract.Cliente.FIELD_URL_FOTO));
			client.setTipoPersona(CursorUtils.getString(c, Contract.Cliente.FIELD_TIPO_PERSONA));
			client.setActividadEconomica(CursorUtils.getString(c, Contract.Cliente.FIELD_ACTIVIDAD_ECONOMICA));
			client.setPaginaWeb(CursorUtils.getString(c, Contract.Cliente.FIELD_PAGINA_WEB));
			client.setRazonSocial(CursorUtils.getString(c, Contract.Cliente.FIELD_RAZON_SOCIAL));
			client.setNuevo(CursorUtils.getBoolean(c, Contract.Cliente.FIELD_NUEVO));
			
			if(client.getIdCliente() == 0)
				client.setContactos(Contacto.getContactoIdCliente(db, client.getID(), true));
			else
				client.setContactos(Contacto.getContactoIdCliente(db, client.getIdCliente(), false));
			
			if(incluirDirecciones)
				if(client.getIdCliente() == 0)
					client.setClienteDirecciones(ClienteDireccion.getClienteDirecciones(db, client.getID(), true));
				else
					client.setClienteDirecciones(ClienteDireccion.getClienteDirecciones(db, client.getIdCliente(), false));
			
		}
		
		return client;
	}
	
	public static Cliente getClienteIDServer(DataBase db, long clientId , boolean incluirDirecciones)
	{		
		String query = QueryDir.getQuery(Contract.Cliente.QUERY_CLIENTE_BY_ID_SERVER);
		 
		Cursor c = db.rawQuery(query, clientId );
		Cliente client = null;
		
		if(c.moveToFirst()){
			client =  new Cliente();
			client.setID(CursorUtils.getInt(c,Contract.Cliente._ID));
			client.setIdCliente(CursorUtils.getInt(c,Contract.Cliente.FIELD_ID_CLIENTE));
			client.setIdTipoIdentificacion(CursorUtils.getInt(c,Contract.Cliente.FIELD_ID_TIPO_IDENTIFICACION) );
			client.setTipoIdentificacionDescripcion(CursorUtils.getString(c,Contract.Cliente.FIELD_TIPOIDENTIFICACION_NOMBRE) );
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
			client.setURLFoto(CursorUtils.getString(c, Contract.Cliente.FIELD_URL_FOTO));
			client.setTipoPersona(CursorUtils.getString(c, Contract.Cliente.FIELD_TIPO_PERSONA));
			client.setActividadEconomica(CursorUtils.getString(c, Contract.Cliente.FIELD_ACTIVIDAD_ECONOMICA));
			client.setPaginaWeb(CursorUtils.getString(c, Contract.Cliente.FIELD_PAGINA_WEB));
			client.setRazonSocial(CursorUtils.getString(c, Contract.Cliente.FIELD_RAZON_SOCIAL));
			client.setNuevo(CursorUtils.getBoolean(c, Contract.Cliente.FIELD_NUEVO));
			
			if(client.getIdCliente() == 0)
				client.setContactos(Contacto.getContactoIdCliente(db, client.getID(), true));
			else
				client.setContactos(Contacto.getContactoIdCliente(db, client.getIdCliente(), false));
			
			if(incluirDirecciones)
				if(client.getIdCliente() == 0)
					client.setClienteDirecciones(ClienteDireccion.getClienteDirecciones(db, client.getID(), true));
				else
					client.setClienteDirecciones(ClienteDireccion.getClienteDirecciones(db, client.getIdCliente(), false));
			
		}
		
		return client;
	}
	
	public static List<Cliente> getClienteInserts(DataBase db, boolean incluirDirecciones)
	{		
		String query = QueryDir.getQuery(Contract.Cliente.QUERY_CLIENTE_INSERTS);
		 
		Cursor c = db.rawQuery(query);
		List<Cliente> list_clientes = new ArrayList<Cliente>();
		
		if(c.moveToFirst()){
			do
			{
				Cliente client =  new Cliente();
				client.setID(CursorUtils.getInt(c,Contract.Cliente._ID));
				client.setIdCliente(CursorUtils.getInt(c,Contract.Cliente.FIELD_ID_CLIENTE));
				client.setIdTipoIdentificacion(CursorUtils.getInt(c,Contract.Cliente.FIELD_ID_TIPO_IDENTIFICACION) );
				client.setTipoIdentificacionDescripcion(CursorUtils.getString(c,Contract.Cliente.FIELD_TIPOIDENTIFICACION_NOMBRE) );
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
				client.setPendiente(CursorUtils.getBoolean(c,Contract.Cliente.FIELD_PENDIENTE));
				
				client.setTelefono(CursorUtils.getString(c, Contract.Cliente.FIELD_TELEFONO));
				client.setDireccion(CursorUtils.getString(c, Contract.Cliente.FIELD_DIRECCION));
				client.setTelefono(CursorUtils.getString(c, Contract.Cliente.FIELD_TELEFONO));
				client.setEstadoCivilDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_ESTADO_CIVIL_DESCRIPCION));
				client.setGeneroDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_GENERO_DESCRIPCION));
				client.setTipoClienteDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_TIPO_CLIENTE_DESCRIPCION));
				client.setCanalDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_CANAL_DESCRIPCION));
				client.setURLFoto(CursorUtils.getString(c, Contract.Cliente.FIELD_URL_FOTO));
				client.setTipoPersona(CursorUtils.getString(c, Contract.Cliente.FIELD_TIPO_PERSONA));
				client.setActividadEconomica(CursorUtils.getString(c, Contract.Cliente.FIELD_ACTIVIDAD_ECONOMICA));
				client.setPaginaWeb(CursorUtils.getString(c, Contract.Cliente.FIELD_PAGINA_WEB));
				client.setRazonSocial(CursorUtils.getString(c, Contract.Cliente.FIELD_RAZON_SOCIAL));
				client.setNuevo(CursorUtils.getBoolean(c, Contract.Cliente.FIELD_NUEVO));
				
				if(client.getIdCliente() == 0)
					client.setContactos(Contacto.getContactoIdCliente(db, client.getID(), true));
				else
					client.setContactos(Contacto.getContactoIdCliente(db, client.getIdCliente(), false));
				
				if(incluirDirecciones)
					if(client.getIdCliente() == 0)
						client.setClienteDirecciones(ClienteDireccion.getClienteDirecciones(db, client.getID(), true));
					else
						client.setClienteDirecciones(ClienteDireccion.getClienteDirecciones(db, client.getIdCliente(), false));
				
				list_clientes.add(client);
			}while(c.moveToNext());
		}
		
		return list_clientes;
	}
	
	public static List<Cliente> getClientePendientes(DataBase db, boolean incluirDirecciones)
	{		
		String query = QueryDir.getQuery(Contract.Cliente.QUERY_CLIENTE_PENDIENTES);
		 
		Cursor c = db.rawQuery(query);
		List<Cliente> list_clientes = new ArrayList<Cliente>();
		
		if(c.moveToFirst()){
			do
			{
				Cliente client =  new Cliente();
				client.setID(CursorUtils.getInt(c,Contract.Cliente._ID));
				client.setIdCliente(CursorUtils.getInt(c,Contract.Cliente.FIELD_ID_CLIENTE));
				client.setIdTipoIdentificacion(CursorUtils.getInt(c,Contract.Cliente.FIELD_ID_TIPO_IDENTIFICACION) );
				client.setTipoIdentificacionDescripcion(CursorUtils.getString(c,Contract.Cliente.FIELD_TIPOIDENTIFICACION_NOMBRE) );
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
				client.setPendiente(CursorUtils.getBoolean(c,Contract.Cliente.FIELD_PENDIENTE));
				
				client.setTelefono(CursorUtils.getString(c, Contract.Cliente.FIELD_TELEFONO));
				client.setDireccion(CursorUtils.getString(c, Contract.Cliente.FIELD_DIRECCION));
				client.setTelefono(CursorUtils.getString(c, Contract.Cliente.FIELD_TELEFONO));
				client.setEstadoCivilDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_ESTADO_CIVIL_DESCRIPCION));
				client.setGeneroDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_GENERO_DESCRIPCION));
				client.setTipoClienteDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_TIPO_CLIENTE_DESCRIPCION));
				client.setCanalDescripcion(CursorUtils.getString(c, Contract.Cliente.FIELD_CANAL_DESCRIPCION));
				client.setURLFoto(CursorUtils.getString(c, Contract.Cliente.FIELD_URL_FOTO));
				client.setTipoPersona(CursorUtils.getString(c, Contract.Cliente.FIELD_TIPO_PERSONA));
				client.setActividadEconomica(CursorUtils.getString(c, Contract.Cliente.FIELD_ACTIVIDAD_ECONOMICA));
				client.setPaginaWeb(CursorUtils.getString(c, Contract.Cliente.FIELD_PAGINA_WEB));
				client.setRazonSocial(CursorUtils.getString(c, Contract.Cliente.FIELD_RAZON_SOCIAL));
				client.setNuevo(CursorUtils.getBoolean(c, Contract.Cliente.FIELD_NUEVO));
				
				if(client.getIdCliente() == 0)
					client.setContactos(Contacto.getContactoIdCliente(db, client.getID(), true));
				else
					client.setContactos(Contacto.getContactoIdCliente(db, client.getIdCliente(), false));
				
				if(incluirDirecciones)
					if(client.getIdCliente() == 0)
						client.setClienteDirecciones(ClienteDireccion.getClienteDirecciones(db, client.getID(), true));
					else
						client.setClienteDirecciones(ClienteDireccion.getClienteDirecciones(db, client.getIdCliente(), false));
				
				list_clientes.add(client);
			}while(c.moveToNext());
		}
		
		return list_clientes;
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
			result = super.updateDb(db);
			
			if(result){
				ClientExt cl_ex = new ClientExt();
				result = ClientExt.update(db, cl_ex);
			}
			
			if(result){	
				for(ClienteDireccion d : this.getClienteDirecciones()){
					d.setIdCliente(this.idCliente);
					if(d.getID() == 0)
						result = ClienteDireccion.insert(db, d);
					else
						result = ClienteDireccion.update(db, d);
						
					if(!result) break;					
				}			
			}
																												
		}catch(Exception ex){
			result = false;
		}finally{		
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
			if(getAction() == ACTION_INSERT || getAction() == ACTION_UPDATE){
				setValue(Contract.ClientExt.COLUMN_ID ,id);
				setValue(Contract.ClientExt.COLUMN_IDENTIFICACION, identificacion);
				setValue(Contract.ClientExt.COLUMN_NOMBRE1, nombre1);
				setValue(Contract.ClientExt.COLUMN_NOMBRE2, nombre2);
				setValue(Contract.ClientExt.COLUMN_NOMBRE_COMPLETO, nombreCompleto);
				setValue(Contract.ClientExt.COLUMN_APELLIDO1, apellido1);
				setValue(Contract.ClientExt.COLUMN_APELLIDO2, apellido2);				
				setValue(Contract.ClientExt.COLUMN_TELEFONO, telefono);							
			}
			setValue(Contract.ClientExt.COLUMN_DIRECCION, direccion);
			setValue(Contract.ClientExt.COLUMN_TELEFONO, telefono);
			setValue(Contract.ClientExt.COLUMN_CORREO_ELECTRONICO, correoElectronico);
			setValue(Contract.ClientExt.COLUMN_RAZON_SOCIAL, razonSocial);
			setValue(Contract.ClientExt.COLUMN_ACTIVIDAD_ECONOMICA, actividadEconomica);
			setValue(Contract.ClientExt.COLUMN_PAGINA_WEB, paginaWeb);
		}

		@Override
		public String getWhere() {			
			return Contract.ClientExt.COLUMN_ID + " = ?";
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
        //String version = db.getSQLiteVersion();
        //int compare = Convert.versionCompare(version, Contants.SQLITE_VERSION_SEARCH);
        Cursor c = null;

        c = db.rawQuery(query, new String[]{termSearch + "*", termSearch + "*"});

		
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
			cl.setTipoPersona(CursorUtils.getString(c, Contract.Cliente.COLUMN_TIPO_PERSONA));
			
			ClienteDireccion cd = new ClienteDireccion();
			cd.setDireccion(CursorUtils.getString(c,Contract.ClientExt.COLUMN_DIRECCION));
			cd.setTelefono1(CursorUtils.getString(c,Contract.ClientExt.COLUMN_TELEFONO));
			
			
			list.add(cl);
		}
		return list;

	}

    public static void deleteClientes(DataBase db, List<Integer> originalIds)
    {
        String idsNotDelete = "";
        for(int i : originalIds)
            idsNotDelete = idsNotDelete + i + ",";

        if(idsNotDelete.length() > 0)
        {
            db.delete(Contract.Cliente.TABLE_NAME, Contract.Cliente.COLUMN_ID_CLIENTE + " NOT IN (" + idsNotDelete + "?)",0);
        }
    }

	public List<Contacto> getContactos() {
		return contactos;
	}

	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}

	public boolean isNuevo() {
		return nuevo;
	}

	public void setNuevo(boolean nuevo) {
		this.nuevo = nuevo;
	}

	public long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	public boolean isPendiente() {
		return pendiente;
	}

	public void setPendiente(boolean pendiente) {
		this.pendiente = pendiente;
	}
	
}
