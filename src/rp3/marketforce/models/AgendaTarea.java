package rp3.marketforce.models;

import java.util.ArrayList;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;
import android.database.Cursor;

public class AgendaTarea extends rp3.data.entity.EntityBase<AgendaTarea> {

	private long id;
	private int idRuta;
	private long idAgenda;
	private long _idAgenda;
	private int idTarea;
	private String nombreTarea;
	private String tipoTarea;
	private String estadoTarea;
	private String estadoTareaDescripcion;
	private List<AgendaTareaActividades> actividades;
	
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
		setValue(Contract.AgendaTarea.COLUMN_AGENDA_ID_EXT, this._idAgenda);	
		setValue(Contract.AgendaTarea.COLUMN_TAREA_ID, this.idTarea);		
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
	
	public long get_idAgenda() {
		return _idAgenda;
	}

	public void set_idAgenda(long _idAgenda) {
		this._idAgenda = _idAgenda;
	}

	public List<AgendaTareaActividades> getActividades() {
		return actividades;
	}

	public void setActividades(List<AgendaTareaActividades> actividades) {
		this.actividades = actividades;
	}

	public String getEstadoTareaDescripcion() {
		return estadoTareaDescripcion;
	}

	public void setEstadoTareaDescripcion(String estadoTareaDescripcion) {
		this.estadoTareaDescripcion = estadoTareaDescripcion;
	}	
	
	public static List<AgendaTarea> getAgendaTareas(DataBase db, long idAgenda, int idRuta, boolean interno){
		String query = "";
		if(interno)
			query = QueryDir.getQuery(Contract.AgendaTarea.QUERY_AGENDA_TAREA_INTERNO);
		else
			query = QueryDir.getQuery(Contract.AgendaTarea.QUERY_AGENDA_TAREA);
		
		Cursor c = db.rawQuery(query, new String[] { String.valueOf(idAgenda), String.valueOf(idRuta) });
		
		List<AgendaTarea> list = new ArrayList<AgendaTarea>();
		while(c.moveToNext()){
			
			AgendaTarea agdt = new AgendaTarea();
			agdt.setID(CursorUtils.getInt(c, Contract.AgendaTarea._ID));
			agdt.set_idAgenda(CursorUtils.getInt(c, Contract.AgendaTarea.COLUMN_AGENDA_ID_EXT));
			agdt.setIdRuta(CursorUtils.getInt(c, Contract.AgendaTarea.FIELD_RUTA_ID));	
			agdt.setIdTarea(CursorUtils.getInt(c, Contract.AgendaTarea.FIELD_TAREA_ID));
			agdt.setIdAgenda(CursorUtils.getLong(c, Contract.AgendaTarea.FIELD_AGENDA_ID));
			agdt.setNombreTarea(CursorUtils.getString(c, Contract.Tareas.FIELD_NOMBRE_TAREA));
			agdt.setTipoTarea(CursorUtils.getString(c, Contract.Tareas.FIELD_TIPO_TAREA));
			agdt.setEstadoTarea(CursorUtils.getString(c, Contract.AgendaTarea.FIELD_ESTADO_TAREA));
			agdt.setEstadoTareaDescripcion(CursorUtils.getString(c, Contract.AgendaTarea.FIELD_ESTADO_TAREA_DESCRIPCION));
			agdt.setActividades(AgendaTareaActividades.getActividades(db, agdt.getIdRuta(), agdt.getIdAgenda(), agdt.getIdTarea()));
			list.add(agdt);
		}
		return list;
	}
	
	public static AgendaTarea getTarea(DataBase db, long idAgenda, int idRuta, int idTarea){
				
		Cursor c = db.query(Contract.AgendaTarea.TABLE_NAME, new String[] {Contract.AgendaTarea._ID, Contract.AgendaTarea.COLUMN_AGENDA_ID,
				Contract.AgendaTarea.COLUMN_ESTADO_TAREA, Contract.AgendaTarea.COLUMN_RUTA_ID,
				Contract.AgendaTarea.COLUMN_TAREA_ID}, 
				Contract.AgendaTarea.COLUMN_AGENDA_ID + " = ? AND " + 
				Contract.AgendaTarea.COLUMN_RUTA_ID + " = ? AND " +
				Contract.AgendaTarea.COLUMN_TAREA_ID + " = ? ", new String[] { idAgenda + "", idRuta + "", idTarea + ""});
		
		
		AgendaTarea tarea = new AgendaTarea();
		if(c.moveToFirst()){

			tarea.setID(CursorUtils.getInt(c, Contract.AgendaTarea._ID));
			tarea.setIdRuta(CursorUtils.getInt(c, Contract.AgendaTarea.FIELD_RUTA_ID));	
			tarea.setIdTarea(CursorUtils.getInt(c, Contract.AgendaTarea.FIELD_TAREA_ID));
			tarea.setIdAgenda(CursorUtils.getLong(c, Contract.AgendaTarea.FIELD_AGENDA_ID));
			//tarea.setNombreTarea(CursorUtils.getString(c, Contract.Tareas.FIELD_NOMBRE_TAREA));
			//tarea.setTipoTarea(CursorUtils.getString(c, Contract.Tareas.FIELD_TIPO_TAREA));
			tarea.setEstadoTarea(CursorUtils.getString(c, Contract.AgendaTarea.FIELD_ESTADO_TAREA));
			
		}
		return tarea;
	}
	
	public static void deleteTareas(DataBase db, long idRuta, long idAgenda)
	{
		db.delete(Contract.AgendaTarea.TABLE_NAME, Contract.AgendaTarea.COLUMN_RUTA_ID + " = ? AND " + 
				Contract.AgendaTarea.COLUMN_AGENDA_ID + " = ? ", new String[] {idRuta + "", idAgenda + ""});
	}	

}
