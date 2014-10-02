package rp3.marketforce.models;

import java.util.ArrayList;
import java.util.List;

import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;
import android.database.Cursor;
import android.util.Log;

public class AgendaTarea extends rp3.data.entity.EntityBase<AgendaTarea> {

	private long id;
	private int idRuta;
	private long idAgenda;
	private int idTarea;
	private String nombreTarea;
	private String tipoTarea;
	private String estadoTarea;
	private String estadoTareaDescripcion;
	
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
		return Contract.AgendaTarea.TABLE_NAME;
	}
	
	public String getEstadoTarea() {
		return estadoTarea;
	}

	public void setEstadoTarea(String estadoTarea) {
		this.estadoTarea = estadoTarea;
	}	

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}
	
	@Override
	public void setValues() {
				
		setValue(Contract.AgendaTarea.COLUMN_RUTA_ID, this.idRuta);
		setValue(Contract.AgendaTarea.COLUMN_AGENDA_ID, this.idAgenda);		
		setValue(Contract.AgendaTarea.COLUMN_TAREA_ID, this.idTarea);		
		setValue(Contract.AgendaTarea.COLUMN_NOMBRE_TAREA, this.nombreTarea);
		setValue(Contract.AgendaTarea.COLUMN_TIPO_TAREA, this.tipoTarea);
		setValue(Contract.AgendaTarea.COLUMN_ESTADO_TAREA, this.estadoTarea);
	}

	@Override
	public Object getValue(String key) {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	public int getIdRuta() {
		return idRuta;
	}

	public void setIdRuta(int idRuta) {
		this.idRuta = idRuta;
	}

	public long getIdAgenda() {
		return idAgenda;
	}

	public void setIdAgenda(long idAgenda) {
		this.idAgenda = idAgenda;
	}

	public String getNombreTarea() {
		return nombreTarea;
	}

	public void setNombreTarea(String nombreTarea) {
		this.nombreTarea = nombreTarea;
	}

	public String getTipoTarea() {
		return tipoTarea;
	}

	public void setTipoTarea(String tipoTarea) {
		this.tipoTarea = tipoTarea;
	}
	
	public String getEstadoTareaDescripcion() {
		return estadoTareaDescripcion;
	}

	public void setEstadoTareaDescripcion(String estadoTareaDescripcion) {
		this.estadoTareaDescripcion = estadoTareaDescripcion;
	}	
	
	public static List<AgendaTarea> getAgendaTareas(DataBase db, long idAgenda, int idRuta){
		String query = QueryDir.getQuery(Contract.AgendaTarea.QUERY_AGENDA_TAREA);
				
		Cursor c = db.rawQuery(query, new String[] { String.valueOf(idAgenda), String.valueOf(idRuta) });
		
		
		List<AgendaTarea> list = new ArrayList<AgendaTarea>();
		while(c.moveToNext()){
			
			AgendaTarea agdt = new AgendaTarea();
			agdt.setID(CursorUtils.getInt(c, Contract.AgendaTarea._ID));
			agdt.setIdRuta(CursorUtils.getInt(c, Contract.AgendaTarea.FIELD_RUTA_ID));	
			agdt.setIdTarea(CursorUtils.getInt(c, Contract.AgendaTarea.FIELD_TAREA_ID));
			agdt.setIdAgenda(CursorUtils.getLong(c, Contract.AgendaTarea.FIELD_AGENDA_ID));
			agdt.setNombreTarea(CursorUtils.getString(c, Contract.AgendaTarea.FIELD_NOMBRE_TAREA));
			agdt.setTipoTarea(CursorUtils.getString(c, Contract.AgendaTarea.FIELD_TIPO_TAREA));
			agdt.setEstadoTarea(CursorUtils.getString(c, Contract.AgendaTarea.FIELD_ESTADO_TAREA));
			agdt.setEstadoTareaDescripcion(CursorUtils.getString(c, Contract.AgendaTarea.FIELD_ESTADO_TAREA_DESCRIPCION));
			
			list.add(agdt);
		}
		return list;
	}

	

	

}
