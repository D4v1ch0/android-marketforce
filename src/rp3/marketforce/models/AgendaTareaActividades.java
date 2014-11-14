package rp3.marketforce.models;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;

public class AgendaTareaActividades extends rp3.data.entity.EntityBase<AgendaTareaActividades>{

	private long id;
	private String descripcion;
	private int idAgenda;
	private int idRuta;
	private int idTarea;
	private int idTareaActividad;
	private int idTareaActividadPadre;
	private int IdTareaOpcion;
	private int IdTipoActividad;
	private int orden;
	private String resultado;
	private String tipo;
	private List<AgendaTareaActividades> actividades_hijas;
	
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
		return Contract.AgendaTareaActividades.TABLE_NAME;
	}

	@Override
	public void setValues() {			
		setValue(Contract.AgendaTareaActividades.COLUMN_DESCRIPCION, this.descripcion);						
		setValue(Contract.AgendaTareaActividades.COLUMN_AGENDA_ID, this.idAgenda);
		setValue(Contract.AgendaTareaActividades.COLUMN_RUTA_ID, this.idRuta);
		setValue(Contract.AgendaTareaActividades.COLUMN_TAREA_ID, this.idTarea);
		setValue(Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_ID, this.idTareaActividad);
		setValue(Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_PADRE_ID, this.idTareaActividadPadre);
		setValue(Contract.AgendaTareaActividades.COLUMN_TAREA_OPCION_ID, this.IdTareaOpcion);
		setValue(Contract.AgendaTareaActividades.COLUMN_TIPO_ACTIVIDAD_ID, this.IdTipoActividad);
		setValue(Contract.AgendaTareaActividades.COLUMN_ORDEN, this.orden);
		setValue(Contract.AgendaTareaActividades.COLUMN_RESULTADO, this.resultado);
		setValue(Contract.AgendaTareaActividades.COLUMN_TIPO, this.tipo);
	}

	@Override
	public Object getValue(String key) {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getIdAgenda() {
		return idAgenda;
	}

	public void setIdAgenda(int idAgenda) {
		this.idAgenda = idAgenda;
	}

	public int getIdRuta() {
		return idRuta;
	}

	public void setIdRuta(int idRuta) {
		this.idRuta = idRuta;
	}

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}

	public int getIdTareaActividad() {
		return idTareaActividad;
	}

	public void setIdTareaActividad(int idTareaActividad) {
		this.idTareaActividad = idTareaActividad;
	}

	public int getIdTareaActividadPadre() {
		return idTareaActividadPadre;
	}

	public void setIdTareaActividadPadre(int idTareaActividadPadre) {
		this.idTareaActividadPadre = idTareaActividadPadre;
	}

	public int getIdTareaOpcion() {
		return IdTareaOpcion;
	}

	public void setIdTareaOpcion(int idTareaOpcion) {
		IdTareaOpcion = idTareaOpcion;
	}

	public int getIdTipoActividad() {
		return IdTipoActividad;
	}

	public void setIdTipoActividad(int idTipoActividad) {
		IdTipoActividad = idTipoActividad;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public List<AgendaTareaActividades> getActividades_hijas() {
		return actividades_hijas;
	}

	public void setActividades_hijas(List<AgendaTareaActividades> actividades_hijas) {
		this.actividades_hijas = actividades_hijas;
	}
	
	public static List<AgendaTareaActividades> getActividades(DataBase db, long id_ruta, long id_agenda, long id_tarea)
	{
		List<AgendaTareaActividades> lista_actividades = new ArrayList<AgendaTareaActividades>();
		
		Cursor c = db.query(Contract.AgendaTareaActividades.TABLE_NAME,
				new String[] {Contract.AgendaTareaActividades._ID, Contract.AgendaTareaActividades.COLUMN_AGENDA_ID,
				Contract.AgendaTareaActividades.COLUMN_RESULTADO, Contract.AgendaTareaActividades.COLUMN_RUTA_ID, 
				Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_ID, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_PADRE_ID, 
				Contract.AgendaTareaActividades.COLUMN_TAREA_ID, Contract.AgendaTareaActividades.COLUMN_TIPO,
				Contract.AgendaTareaActividades.COLUMN_TIPO_ACTIVIDAD_ID},
				Contract.AgendaTareaActividades.COLUMN_RUTA_ID + " = ? AND " +
				Contract.AgendaTareaActividades.COLUMN_AGENDA_ID + " = ? AND " +
				Contract.AgendaTareaActividades.COLUMN_TAREA_ID + " = ?", 
				new String[] {id_ruta + "", id_agenda + "", id_tarea + ""} );
		
		if(c.moveToFirst())
		{
			do
			{
				AgendaTareaActividades actividad = new AgendaTareaActividades();
				actividad.setID(CursorUtils.getLong(c, Contract.AgendaTareaActividades._ID));
				actividad.setIdAgenda(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_AGENDA_ID));
				actividad.setResultado(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_RESULTADO));
				actividad.setIdRuta(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_RUTA_ID));
				actividad.setIdTareaActividad(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_ID));
				actividad.setIdTareaActividadPadre(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_PADRE_ID));
				actividad.setIdTarea(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ID));
				actividad.setTipo(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_TIPO));
				actividad.setIdTipoActividad(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TIPO_ACTIVIDAD_ID));
				lista_actividades.add(actividad);
			}while(c.moveToNext());
		}
		
		return lista_actividades;
	}

	public static List<AgendaTareaActividades> getActividadesCompleja(DataBase db, long id_padre, long id_ruta, long id_agenda, long id_tarea)
	{
		List<AgendaTareaActividades> lista_actividades = new ArrayList<AgendaTareaActividades>();
		
		Cursor c = db.query(Contract.AgendaTareaActividades.TABLE_NAME,
				new String[] {Contract.AgendaTareaActividades._ID,Contract.AgendaTareaActividades.COLUMN_AGENDA_ID, Contract.AgendaTareaActividades.COLUMN_DESCRIPCION,
				Contract.AgendaTareaActividades.COLUMN_ORDEN, Contract.AgendaTareaActividades.COLUMN_RESULTADO,
				Contract.AgendaTareaActividades.COLUMN_RUTA_ID, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_ID,
				Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_PADRE_ID, Contract.AgendaTareaActividades.COLUMN_TAREA_ID,
				Contract.AgendaTareaActividades.COLUMN_TAREA_OPCION_ID, Contract.AgendaTareaActividades.COLUMN_TIPO,
				Contract.AgendaTareaActividades.COLUMN_TIPO_ACTIVIDAD_ID},
				Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_PADRE_ID + " = ? AND " +
				Contract.AgendaTareaActividades.COLUMN_RUTA_ID + " = ? AND " +
				Contract.AgendaTareaActividades.COLUMN_AGENDA_ID + " = ? AND " +
				Contract.AgendaTareaActividades.COLUMN_TAREA_ID + " = ?", 
				new String[] {id_padre + "", id_ruta + "", id_agenda + "", id_tarea + ""} );
		
		if(c.moveToFirst())
		{
			do
			{
				AgendaTareaActividades actividad = new AgendaTareaActividades();
				actividad.setID(CursorUtils.getLong(c, Contract.AgendaTareaActividades._ID));
				actividad.setIdAgenda(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_AGENDA_ID));
				actividad.setDescripcion(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_DESCRIPCION));
				actividad.setOrden(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_ORDEN));
				actividad.setResultado(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_RESULTADO));
				actividad.setIdRuta(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_RUTA_ID));
				actividad.setIdTareaActividad(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_ID));
				actividad.setIdTareaActividadPadre(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_PADRE_ID));
				actividad.setIdTarea(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ID));
				actividad.setIdTareaOpcion(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_OPCION_ID));
				actividad.setTipo(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_TIPO));
				actividad.setIdTipoActividad(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TIPO_ACTIVIDAD_ID));
				lista_actividades.add(actividad);
			}while(c.moveToNext());
		}
		
		return lista_actividades;
	}
	
	public static List<AgendaTareaActividades> getActividadesGrupales(DataBase db, long id_ruta, long id_agenda, long id_tarea)
	{
		List<AgendaTareaActividades> lista_actividades = new ArrayList<AgendaTareaActividades>();
		
		Cursor c = db.query(Contract.AgendaTareaActividades.TABLE_NAME,
				new String[] {Contract.AgendaTareaActividades._ID,Contract.AgendaTareaActividades.COLUMN_AGENDA_ID, Contract.AgendaTareaActividades.COLUMN_DESCRIPCION,
				Contract.AgendaTareaActividades.COLUMN_ORDEN, Contract.AgendaTareaActividades.COLUMN_RESULTADO,
				Contract.AgendaTareaActividades.COLUMN_RUTA_ID, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_ID,
				Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_PADRE_ID, Contract.AgendaTareaActividades.COLUMN_TAREA_ID,
				Contract.AgendaTareaActividades.COLUMN_TAREA_OPCION_ID, Contract.AgendaTareaActividades.COLUMN_TIPO,
				Contract.AgendaTareaActividades.COLUMN_TIPO_ACTIVIDAD_ID},
				Contract.AgendaTareaActividades.COLUMN_RUTA_ID + " = ? AND " +
				Contract.AgendaTareaActividades.COLUMN_AGENDA_ID + " = ? AND " +
				Contract.AgendaTareaActividades.COLUMN_TAREA_ID + " = ? AND " +
				Contract.AgendaTareaActividades.COLUMN_TIPO + " = 'G'", 
				new String[] {id_ruta + "", id_agenda + "", id_tarea + ""} );
		
		if(c.moveToFirst())
		{
			do
			{
				AgendaTareaActividades actividad = new AgendaTareaActividades();
				actividad.setID(CursorUtils.getLong(c, Contract.AgendaTareaActividades._ID));
				actividad.setIdAgenda(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_AGENDA_ID));
				actividad.setDescripcion(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_DESCRIPCION));
				actividad.setOrden(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_ORDEN));
				actividad.setResultado(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_RESULTADO));
				actividad.setIdRuta(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_RUTA_ID));
				actividad.setIdTareaActividad(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_ID));
				actividad.setIdTareaActividadPadre(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_PADRE_ID));
				actividad.setIdTarea(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ID));
				actividad.setIdTareaOpcion(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_OPCION_ID));
				actividad.setTipo(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_TIPO));
				actividad.setIdTipoActividad(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TIPO_ACTIVIDAD_ID));
				actividad.setActividades_hijas(getActividadesCompleja(db, actividad.getIdTareaActividad(), id_ruta, id_agenda, id_tarea));
				lista_actividades.add(actividad);
			}while(c.moveToNext());
		}
		
		return lista_actividades;
	}
	
	public static AgendaTareaActividades getActividadSimple(DataBase db, long id_ruta, long id_agenda, long id_tarea)
	{
		AgendaTareaActividades actividad = new AgendaTareaActividades();
		
		Cursor c = db.query(Contract.AgendaTareaActividades.TABLE_NAME,
				new String[] {Contract.AgendaTareaActividades._ID,Contract.AgendaTareaActividades.COLUMN_AGENDA_ID, Contract.AgendaTareaActividades.COLUMN_DESCRIPCION,
				Contract.AgendaTareaActividades.COLUMN_ORDEN, Contract.AgendaTareaActividades.COLUMN_RESULTADO,
				Contract.AgendaTareaActividades.COLUMN_RUTA_ID, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_ID,
				Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_PADRE_ID, Contract.AgendaTareaActividades.COLUMN_TAREA_ID,
				Contract.AgendaTareaActividades.COLUMN_TAREA_OPCION_ID, Contract.AgendaTareaActividades.COLUMN_TIPO,
				Contract.AgendaTareaActividades.COLUMN_TIPO_ACTIVIDAD_ID},
				Contract.AgendaTareaActividades.COLUMN_RUTA_ID + " = ? AND " +
				Contract.AgendaTareaActividades.COLUMN_AGENDA_ID + " = ? AND " +
				Contract.AgendaTareaActividades.COLUMN_TAREA_ID + " = ?"	, new String[] {id_ruta + "", id_agenda + "", id_tarea + ""} );
		
		if(c.moveToFirst())
		{
			actividad.setID(CursorUtils.getLong(c, Contract.AgendaTareaActividades._ID));
			actividad.setIdAgenda(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_AGENDA_ID));
			actividad.setDescripcion(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_DESCRIPCION));
			actividad.setOrden(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_ORDEN));
			actividad.setResultado(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_RESULTADO));
			actividad.setIdRuta(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_RUTA_ID));
			actividad.setIdTareaActividad(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_ID));
			actividad.setIdTareaActividadPadre(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_PADRE_ID));
			actividad.setIdTarea(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ID));
			actividad.setIdTareaOpcion(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_OPCION_ID));
			actividad.setTipo(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_TIPO));
			actividad.setIdTipoActividad(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TIPO_ACTIVIDAD_ID));
		}
		
		return actividad;
	}
	
	public static AgendaTareaActividades getActividadSimpleFromParent(DataBase db, long id_ruta, long id_agenda, long id_tarea, long id_actividad, long id_padre)
	{
		AgendaTareaActividades actividad = new AgendaTareaActividades();
		
		Cursor c = db.query(Contract.AgendaTareaActividades.TABLE_NAME,
				new String[] {Contract.AgendaTareaActividades._ID,Contract.AgendaTareaActividades.COLUMN_AGENDA_ID, Contract.AgendaTareaActividades.COLUMN_DESCRIPCION,
				Contract.AgendaTareaActividades.COLUMN_ORDEN, Contract.AgendaTareaActividades.COLUMN_RESULTADO,
				Contract.AgendaTareaActividades.COLUMN_RUTA_ID, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_ID,
				Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_PADRE_ID, Contract.AgendaTareaActividades.COLUMN_TAREA_ID,
				Contract.AgendaTareaActividades.COLUMN_TAREA_OPCION_ID, Contract.AgendaTareaActividades.COLUMN_TIPO,
				Contract.AgendaTareaActividades.COLUMN_TIPO_ACTIVIDAD_ID},
				Contract.AgendaTareaActividades.COLUMN_RUTA_ID + " = ? AND " +
				Contract.AgendaTareaActividades.COLUMN_AGENDA_ID + " = ? AND " +
				Contract.AgendaTareaActividades.COLUMN_TAREA_ID + " = ? AND " +
				Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_ID + " = ? AND " +	
				Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_PADRE_ID + " = ?" , new String[] {id_ruta + "", id_agenda + "", id_tarea + "", id_actividad + "", id_padre + ""} );
		
		if(c.moveToFirst())
		{
			actividad.setID(CursorUtils.getLong(c, Contract.AgendaTareaActividades._ID));
			actividad.setIdAgenda(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_AGENDA_ID));
			actividad.setDescripcion(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_DESCRIPCION));
			actividad.setOrden(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_ORDEN));
			actividad.setResultado(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_RESULTADO));
			actividad.setIdRuta(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_RUTA_ID));
			actividad.setIdTareaActividad(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_ID));
			actividad.setIdTareaActividadPadre(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ACTIVIDAD_PADRE_ID));
			actividad.setIdTarea(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_ID));
			actividad.setIdTareaOpcion(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TAREA_OPCION_ID));
			actividad.setTipo(CursorUtils.getString(c, Contract.AgendaTareaActividades.COLUMN_TIPO));
			actividad.setIdTipoActividad(CursorUtils.getInt(c, Contract.AgendaTareaActividades.COLUMN_TIPO_ACTIVIDAD_ID));
		}
		
		return actividad;
	}
	
}
