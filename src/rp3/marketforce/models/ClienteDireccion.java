package rp3.marketforce.models;

import java.util.ArrayList;
import java.util.List;

import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;
import android.database.Cursor;


public class ClienteDireccion extends rp3.data.entity.EntityBase<ClienteDireccion>{

	private long id;
	private long idCliente;
	private int idClienteDireccion;
	private String direccion;
	private boolean esPrincipal;
	private String tipoDireccion;
	private int idCiudad;
	private String telefono1;
	private String telefono2;
	private String referencia;
	private double latitud;
	private double longitud;
	private String ciudadDescripcion;
	
	
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
		return Contract.ClienteDireccion.TABLE_NAME;
	}

	@Override
	public void setValues() {
		if(getAction() == ACTION_INSERT){
			setValue(Contract.ClienteDireccion.COLUMN_CLIENTE_ID, this.idCliente);
			setValue(Contract.ClienteDireccion.COLUMN_CLIENTE_DIRECCION_ID, this.idClienteDireccion);
			setValue(Contract.ClienteDireccion.COLUMN_TIPO_DIRECCION, this.tipoDireccion);
			setValue(Contract.ClienteDireccion.COLUMN_CIUDAD_ID, this.idCiudad);
			setValue(Contract.ClienteDireccion.COLUMN_ES_PRINCIPAL, this.esPrincipal);			
		}
		setValue(Contract.ClienteDireccion.COLUMN_DIRECCION, this.direccion);
		setValue(Contract.ClienteDireccion.COLUMN_TELEFONO1, this.telefono1);
		setValue(Contract.ClienteDireccion.COLUMN_TELEFONO2, this.telefono2);
		setValue(Contract.ClienteDireccion.COLUMN_REFERENCIA, this.referencia);
		setValue(Contract.ClienteDireccion.COLUMN_LATITUD, this.latitud);
		setValue(Contract.ClienteDireccion.COLUMN_LONGITUD, this.longitud);
		
	}

	@Override
	public Object getValue(String key) {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	public long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	public int getIdClienteDireccion() {
		return idClienteDireccion;
	}

	public boolean getEsPrincipal() {
		return esPrincipal;
	}

	public void setEsPrincipal(boolean esPrincipal) {
		this.esPrincipal = esPrincipal;
	}

	public void setIdClienteDireccion(int idClienteDireccion) {
		this.idClienteDireccion = idClienteDireccion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTipoDireccion() {
		return tipoDireccion;
	}

	public void setTipoDireccion(String tipoDireccion) {
		this.tipoDireccion = tipoDireccion;
	}

	public int getIdCiudad() {
		return idCiudad;
	}

	public void setIdCiudad(int idCiudad) {
		this.idCiudad = idCiudad;
	}

	public String getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public String getCiudadDescripcion() {
		return ciudadDescripcion;
	}

	public void setCiudadDescripcion(String ciudadDescripcion) {
		this.ciudadDescripcion = ciudadDescripcion;
	}

	public static List<ClienteDireccion> getClienteDirecciones(DataBase db, long id){
		
		String query = QueryDir.getQuery(Contract.ClienteDireccion.QUERY_CLIENTE_DIRECCION_BY_ID);
    	Cursor c = db.rawQuery(query, id );		
		
		List<ClienteDireccion> list = new ArrayList<ClienteDireccion>();
		while(c.moveToNext()){
			ClienteDireccion tpd = new ClienteDireccion();
			tpd.setID(CursorUtils.getInt(c,Contract.ClienteDireccion._ID));
			tpd.setIdCliente(CursorUtils.getLong(c, Contract.ClienteDireccion.FIELD_CLIENTE_ID));
			tpd.setIdClienteDireccion(CursorUtils.getInt(c, Contract.ClienteDireccion.FIELD_CLIENTE_DIRECCION_ID));
			tpd.setDireccion(CursorUtils.getString(c, Contract.ClienteDireccion.FIELD_DIRECCION));
			tpd.setEsPrincipal(CursorUtils.getBoolean(c, Contract.ClienteDireccion.FIELD_ES_PRINCIPAL));
			tpd.setTipoDireccion(CursorUtils.getString(c, Contract.ClienteDireccion.FIELD_TIPO_DIRECCION));
			tpd.setIdCiudad(CursorUtils.getInt(c, Contract.ClienteDireccion.FIELD_CIUDAD_ID));
			tpd.setTelefono1(CursorUtils.getString(c, Contract.ClienteDireccion.FIELD_TELEFONO1));
			tpd.setTelefono2(CursorUtils.getString(c, Contract.ClienteDireccion.FIELD_TELEFONO2));
			tpd.setReferencia(CursorUtils.getString(c, Contract.ClienteDireccion.FIELD_REFERENCIA));
			tpd.setLatitud(CursorUtils.getDouble(c, Contract.ClienteDireccion.FIELD_LATITUD));
			tpd.setLongitud(CursorUtils.getDouble(c, Contract.ClienteDireccion.FIELD_LONGITUD));
			tpd.setCiudadDescripcion(CursorUtils.getString(c, Contract.ClienteDireccion.FIELD_CIUDAD));
			
			list.add(tpd);
		}
		return list;
	}
	
    public static ClienteDireccion getClienteDireccionIdPrincipal(DataBase db, long id){
    	
		Cursor c = db.query(Contract.ClienteDireccion.TABLE_NAME, new String[]{
				Contract.ClienteDireccion._ID,
				Contract.ClienteDireccion.COLUMN_CLIENTE_ID,
				Contract.ClienteDireccion.COLUMN_CLIENTE_DIRECCION_ID,
				Contract.ClienteDireccion.COLUMN_DIRECCION,
				Contract.ClienteDireccion.COLUMN_ES_PRINCIPAL,
				Contract.ClienteDireccion.COLUMN_CIUDAD_ID,
				Contract.ClienteDireccion.COLUMN_TELEFONO1,
				Contract.ClienteDireccion.COLUMN_TELEFONO2,
		}, Contract.ClienteDireccion.COLUMN_CLIENTE_ID + " = ? and "+Contract.ClienteDireccion.COLUMN_ES_PRINCIPAL+" = 1",
		new String[] { String.valueOf(id) });
		
		ClienteDireccion tpd = null;
		
		while(c.moveToNext()){
			tpd = new ClienteDireccion();
			tpd.setID(CursorUtils.getInt(c,Contract.ClienteDireccion._ID));
			tpd.setIdCliente(CursorUtils.getLong(c, Contract.ClienteDireccion.FIELD_CLIENTE_ID));
			tpd.setDireccion(CursorUtils.getString(c, Contract.ClienteDireccion.FIELD_DIRECCION));
			tpd.setEsPrincipal(CursorUtils.getBoolean(c, Contract.ClienteDireccion.FIELD_ES_PRINCIPAL));
			tpd.setIdCiudad(CursorUtils.getInt(c, Contract.ClienteDireccion.FIELD_CIUDAD_ID));
			tpd.setTelefono1(CursorUtils.getString(c, Contract.ClienteDireccion.FIELD_TELEFONO1));
			tpd.setTelefono2(CursorUtils.getString(c, Contract.ClienteDireccion.FIELD_TELEFONO2));
			
		}
		return tpd;
	}

	
}
