package rp3.marketforce.models;

import rp3.marketforce.db.Contract;

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
		return Contract.AgendaTareaActividades.TABLE_NAME;
	}

	@Override
	public void setValues() {
		setValue(Contract.AgendaTareaActividades._ID, this.id);				
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
	
}
