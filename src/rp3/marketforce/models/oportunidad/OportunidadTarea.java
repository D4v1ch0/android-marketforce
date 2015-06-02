package rp3.marketforce.models.oportunidad;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.Tarea;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class OportunidadTarea extends EntityBase<OportunidadTarea> {

    private long id;
    private int idOportunidad;
    private int _idOportunidad;
    private int idEtapa;
    private int idTarea;
    private int orden;
    private String observacion;
    private String estado;
    private List<OportunidadTareaActividad> oportunidadTareaActividades;
    private Tarea tarea;

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }

    public int getIdOportunidad() {
        return idOportunidad;
    }

    public void setIdOportunidad(int idOportunidad) {
        this.idOportunidad = idOportunidad;
    }

    public int get_idOportunidad() {
        return _idOportunidad;
    }

    public void set_idOportunidad(int _idOportunidad) {
        this._idOportunidad = _idOportunidad;
    }

    public int getIdEtapa() {
        return idEtapa;
    }

    public void setIdEtapa(int idEtapa) {
        this.idEtapa = idEtapa;
    }

    public int getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(int idTarea) {
        this.idTarea = idTarea;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<OportunidadTareaActividad> getOportunidadTareaActividades() {
        return oportunidadTareaActividades;
    }

    public void setOportunidadTareaActividades(List<OportunidadTareaActividad> oportunidadTareaActividades) {
        this.oportunidadTareaActividades = oportunidadTareaActividades;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    @Override
    public boolean isAutoGeneratedId() {
        return true;
    }

    @Override
    public String getTableName() {
        return Contract.OportunidadTarea.TABLE_NAME;
    }

    @Override
    public void setValues() {
        setValue(Contract.OportunidadTarea.COLUMN_ID_ETAPA, this.idEtapa);
        setValue(Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD, this.idOportunidad);
        setValue(Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD_INT, this._idOportunidad);
        setValue(Contract.OportunidadTarea.COLUMN_ID_TAREA, this.idTarea);
        setValue(Contract.OportunidadTarea.COLUMN_OBSERVACION, this.observacion);
        setValue(Contract.OportunidadTarea.COLUMN_ORDEN, this.orden);
        setValue(Contract.OportunidadTarea.COLUMN_ESTADO, this.estado);
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public static List<OportunidadTarea> getTareasOportunidad(DataBase db, int idOportunidad) {
        Cursor c = db.query(Contract.OportunidadTarea.TABLE_NAME, new String[] {Contract.OportunidadTarea._ID, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD,
                        Contract.OportunidadTarea.COLUMN_ID_ETAPA, Contract.OportunidadTarea.COLUMN_ID_TAREA, Contract.OportunidadTarea.COLUMN_OBSERVACION,
                        Contract.OportunidadTarea.COLUMN_ORDEN, Contract.OportunidadTarea.COLUMN_ESTADO}, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD + " = ?",
                new String[] {idOportunidad + ""});

        List<OportunidadTarea> list = new ArrayList<OportunidadTarea>();
        while(c.moveToNext()){
            OportunidadTarea cont = new OportunidadTarea();
            cont.setID(CursorUtils.getInt(c, Contract.OportunidadTarea._ID));
            cont.setIdOportunidad(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD));
            cont.setIdEtapa(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_ETAPA));
            cont.setIdTarea(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_TAREA));
            cont.setObservacion(CursorUtils.getString(c, Contract.OportunidadTarea.COLUMN_OBSERVACION));
            cont.setOrden(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ORDEN));
            cont.setEstado(CursorUtils.getString(c, Contract.OportunidadTarea.COLUMN_ESTADO));

            cont.setTarea(Tarea.getTareaId(db, cont.getIdTarea()));
            cont.setOportunidadTareaActividades(OportunidadTareaActividad.getActividades(db, cont.getIdOportunidad(), cont.getIdTarea(), cont.getIdEtapa()));

            list.add(cont);
        }
        c.close();
        return list;
    }

    public static List<OportunidadTarea> getTareasOportunidadInt(DataBase db, long id) {
        Cursor c = db.query(Contract.OportunidadTarea.TABLE_NAME, new String[] {Contract.OportunidadTarea._ID, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD,
                        Contract.OportunidadTarea.COLUMN_ID_ETAPA, Contract.OportunidadTarea.COLUMN_ID_TAREA, Contract.OportunidadTarea.COLUMN_OBSERVACION,
                        Contract.OportunidadTarea.COLUMN_ORDEN, Contract.OportunidadTarea.COLUMN_ESTADO}, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD_INT + " = ?",
                new String[] {id + ""});

        List<OportunidadTarea> list = new ArrayList<OportunidadTarea>();
        while(c.moveToNext()){
            OportunidadTarea cont = new OportunidadTarea();
            cont.setID(CursorUtils.getInt(c, Contract.OportunidadTarea._ID));
            cont.setIdOportunidad(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD));
            cont.setIdEtapa(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_ETAPA));
            cont.setIdTarea(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_TAREA));
            cont.setObservacion(CursorUtils.getString(c, Contract.OportunidadTarea.COLUMN_OBSERVACION));
            cont.setOrden(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ORDEN));
            cont.setEstado(CursorUtils.getString(c, Contract.OportunidadTarea.COLUMN_ESTADO));

            cont.setTarea(Tarea.getTareaId(db, cont.getIdTarea()));
            cont.setOportunidadTareaActividades(OportunidadTareaActividad.getActividadesInt(db, cont.getID(), cont.getIdTarea(), cont.getIdEtapa()));

            list.add(cont);
        }
        c.close();
        return list;

    }

    public static List<OportunidadTarea> getTareasOportunidadIntByEtapa(DataBase db, long id, int idEtapa) {
        Cursor c = db.query(Contract.OportunidadTarea.TABLE_NAME, new String[] {Contract.OportunidadTarea._ID, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD,
                        Contract.OportunidadTarea.COLUMN_ID_ETAPA, Contract.OportunidadTarea.COLUMN_ID_TAREA, Contract.OportunidadTarea.COLUMN_OBSERVACION,
                        Contract.OportunidadTarea.COLUMN_ORDEN, Contract.OportunidadTarea.COLUMN_ESTADO}, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD_INT + " = ? AND "
                        + Contract.OportunidadTarea.COLUMN_ID_ETAPA + " = ? ",
                new String[] {id + "", idEtapa + ""});

        List<OportunidadTarea> list = new ArrayList<OportunidadTarea>();
        while(c.moveToNext()){
            OportunidadTarea cont = new OportunidadTarea();
            cont.setID(CursorUtils.getInt(c, Contract.OportunidadTarea._ID));
            cont.setIdOportunidad(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD));
            cont.setIdEtapa(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_ETAPA));
            cont.setIdTarea(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_TAREA));
            cont.setObservacion(CursorUtils.getString(c, Contract.OportunidadTarea.COLUMN_OBSERVACION));
            cont.setOrden(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ORDEN));
            cont.setEstado(CursorUtils.getString(c, Contract.OportunidadTarea.COLUMN_ESTADO));

            cont.setTarea(Tarea.getTareaId(db, cont.getIdTarea()));
            cont.setOportunidadTareaActividades(OportunidadTareaActividad.getActividadesInt(db, cont.getID(), cont.getIdTarea(), cont.getIdEtapa()));

            list.add(cont);
        }
        c.close();
        return list;

    }

    public static List<OportunidadTarea> getTareasOportunidadByEtapa(DataBase db, int idOportunidad, int idEtapa) {
        Cursor c = db.query(Contract.OportunidadTarea.TABLE_NAME, new String[] {Contract.OportunidadTarea._ID, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD,
                        Contract.OportunidadTarea.COLUMN_ID_ETAPA, Contract.OportunidadTarea.COLUMN_ID_TAREA, Contract.OportunidadTarea.COLUMN_OBSERVACION,
                        Contract.OportunidadTarea.COLUMN_ORDEN, Contract.OportunidadTarea.COLUMN_ESTADO}, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD + " = ? AND "
                        + Contract.OportunidadTarea.COLUMN_ID_ETAPA + " = ? ",
                new String[] {idOportunidad + "", idEtapa + ""});

        List<OportunidadTarea> list = new ArrayList<OportunidadTarea>();
        while(c.moveToNext()){
            OportunidadTarea cont = new OportunidadTarea();
            cont.setID(CursorUtils.getInt(c, Contract.OportunidadTarea._ID));
            cont.setIdOportunidad(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD));
            cont.setIdEtapa(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_ETAPA));
            cont.setIdTarea(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_TAREA));
            cont.setObservacion(CursorUtils.getString(c, Contract.OportunidadTarea.COLUMN_OBSERVACION));
            cont.setOrden(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ORDEN));
            cont.setEstado(CursorUtils.getString(c, Contract.OportunidadTarea.COLUMN_ESTADO));

            cont.setTarea(Tarea.getTareaId(db, cont.getIdTarea()));
            cont.setOportunidadTareaActividades(OportunidadTareaActividad.getActividades(db, cont.getIdOportunidad(), cont.getIdTarea(), cont.getIdEtapa()));

            list.add(cont);
        }
        c.close();
        return list;
    }


    public static OportunidadTarea getTarea(DataBase db, int id_oportunidad, int id_etapa, int id_actividad) {

        Cursor c = db.query(Contract.OportunidadTarea.TABLE_NAME, new String[] {Contract.OportunidadTarea._ID, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD,
                        Contract.OportunidadTarea.COLUMN_ID_ETAPA, Contract.OportunidadTarea.COLUMN_ID_TAREA, Contract.OportunidadTarea.COLUMN_OBSERVACION,
                        Contract.OportunidadTarea.COLUMN_ORDEN, Contract.OportunidadTarea.COLUMN_ESTADO}, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD + " = ? AND "
                        + Contract.OportunidadTarea.COLUMN_ID_ETAPA + " = ? AND " + Contract.OportunidadTarea.COLUMN_ID_TAREA + " = ? ",
                new String[] {id_oportunidad + "", id_etapa + "", id_actividad + ""});

        OportunidadTarea cont = new OportunidadTarea();
        while(c.moveToNext()){

            cont.setID(CursorUtils.getInt(c, Contract.OportunidadTarea._ID));
            cont.setIdOportunidad(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_OPORTUNIDAD));
            cont.setIdEtapa(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_ETAPA));
            cont.setIdTarea(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ID_TAREA));
            cont.setObservacion(CursorUtils.getString(c, Contract.OportunidadTarea.COLUMN_OBSERVACION));
            cont.setOrden(CursorUtils.getInt(c, Contract.OportunidadTarea.COLUMN_ORDEN));
            cont.setEstado(CursorUtils.getString(c, Contract.OportunidadTarea.COLUMN_ESTADO));

            cont.setTarea(Tarea.getTareaId(db, cont.getIdTarea()));
            cont.setOportunidadTareaActividades(OportunidadTareaActividad.getActividades(db, cont.getIdOportunidad(), cont.getIdTarea(), cont.getIdEtapa()));

        }
        c.close();
        return cont;
    }
}
