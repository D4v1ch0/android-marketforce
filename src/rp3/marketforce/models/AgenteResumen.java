package rp3.marketforce.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;

import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;

public class AgenteResumen extends rp3.data.entity.EntityBase<AgenteResumen>{

	private long id;
	private int idAgente;
	private String nombres;
	private String apellidos;
	private Date fecha;
	private int gestionados;
	private int noGestionados;
	private int pendientes;
	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
		
	}

	public int getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(int idAgente) {
		this.idAgente = idAgente;
	}

	@Override
	public boolean isAutoGeneratedId() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return Contract.AgentesResumen.TABLE_NAME;
	}

	@Override
	public void setValues() {
		setValue(Contract.AgentesResumen.COLUMN_ID_AGENTE, this.idAgente);
		setValue(Contract.AgentesResumen.COLUMN_NOMBRES, this.nombres);
		setValue(Contract.AgentesResumen.COLUMN_APELLIDOS, this.apellidos);
		setValue(Contract.AgentesResumen.COLUMN_FECHA, this.fecha);
		setValue(Contract.AgentesResumen.COLUMN_GESTIONADOS, this.gestionados);
		setValue(Contract.AgentesResumen.COLUMN_NO_GESTIONADOS, this.noGestionados);
		setValue(Contract.AgentesResumen.COLUMN_PENDIENTES, this.pendientes);
		
	}

	@Override
	public Object getValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getGestionados() {
		return gestionados;
	}

	public void setGestionados(int gestionados) {
		this.gestionados = gestionados;
	}

	public int getNoGestionados() {
		return noGestionados;
	}

	public void setNoGestionados(int noGestionados) {
		this.noGestionados = noGestionados;
	}

	public int getPendientes() {
		return pendientes;
	}

	public void setPendientes(int pendientes) {
		this.pendientes = pendientes;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	
	public static List<AgenteResumen> getResumen(DataBase db, long inicio, long fin){
		
        String query = QueryDir.getQuery( Contract.AgentesResumen.QUERY_RESUMEN );
		
		Cursor c = db.rawQuery(query, new String[]{"" + inicio, "" + fin, "" + inicio, "" + fin});
		
		List<AgenteResumen> list = new ArrayList<AgenteResumen>();
		List<Integer> listids = new ArrayList<Integer>();
		if(c.moveToFirst())
		{
			do
			{
				AgenteResumen agd = new AgenteResumen();
                agd.setIdAgente(CursorUtils.getInt(c, Contract.AgentesResumen.FIELD_ID_AGENTE));
				agd.setNombres(CursorUtils.getString(c, Contract.AgentesResumen.FIELD_NOMBRES) + " " + CursorUtils.getString(c, Contract.AgentesResumen.COLUMN_APELLIDOS));
				//agd.setApellidos(CursorUtils.getString(c, Contract.AgentesResumen.FIELD_APELLIDOS));
				agd.setGestionados(CursorUtils.getInt(c, Contract.AgentesResumen.FIELD_GESTIONADOS));
				agd.setNoGestionados((CursorUtils.getInt(c, Contract.AgentesResumen.FIELD_NO_GESTIONADOS)));	
				agd.setPendientes(CursorUtils.getInt(c, Contract.AgentesResumen.FIELD_PENDIENTES));
				if(!listids.contains(agd.getIdAgente()))
					list.add(agd);
				listids.add(agd.getIdAgente());
			}while(c.moveToNext());
		}
        c.close();
		return list;
	}

}
