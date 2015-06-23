package rp3.marketforce.models.marcacion;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 08/06/2015.
 */
public class Permiso extends EntityBase<Permiso>
{
    private long id;
    private long idMarcacion;
    private Date fecha;
    private String tipo;
    private String observacion;
    private int idPermiso;


    @Override
    public long getID() {
        // TODO Auto-generated method stub
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;

    }

    public long getIdMarcacion() {
        return idMarcacion;
    }

    public void setIdMarcacion(long idMarcacion) {
        this.idMarcacion = idMarcacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(int idPermiso) {
        this.idPermiso = idPermiso;
    }

    @Override
    public boolean isAutoGeneratedId() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String getTableName() {
        // TODO Auto-generated method stub
        return Contract.Permiso.TABLE_NAME;
    }

    @Override
    public void setValues() {
        setValue(Contract.Permiso.COLUMN_OBSERVACION, this.observacion);
        setValue(Contract.Permiso.COLUMN_FECHA, this.fecha);
        setValue(Contract.Permiso.COLUMN_ID_MARCACION, this.idMarcacion);
        setValue(Contract.Permiso.COLUMN_TIPO, this.tipo);
        setValue(Contract.Permiso.COLUMN_ID_PERMISO, this.idPermiso);
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

    public static Permiso getPermisoMarcacion(DataBase db, long idMarcacion) {
        Cursor c = db.query(Contract.Permiso.TABLE_NAME, new String[]{Contract.Permiso._ID, Contract.Permiso.COLUMN_FECHA, Contract.Permiso.COLUMN_ID_PERMISO,
                        Contract.Permiso.COLUMN_TIPO, Contract.Permiso.COLUMN_OBSERVACION, Contract.Permiso.COLUMN_ID_MARCACION}, Contract.Permiso.COLUMN_ID_MARCACION + " = ? AND "
                + Contract.Permiso.COLUMN_TIPO + " <> 'FALTA' ", new String[]{idMarcacion + ""});
        Permiso permiso = null;

        if (c.moveToFirst()) {
            permiso = new Permiso();
            permiso.setID(CursorUtils.getLong(c, Contract.Permiso._ID));
            permiso.setIdPermiso(CursorUtils.getInt(c, Contract.Permiso.COLUMN_ID_PERMISO));
            permiso.setIdMarcacion(CursorUtils.getLong(c, Contract.Permiso.COLUMN_ID_MARCACION));
            permiso.setTipo(CursorUtils.getString(c, Contract.Permiso.COLUMN_TIPO));
            permiso.setFecha(CursorUtils.getDate(c, Contract.Permiso.COLUMN_FECHA));
            permiso.setObservacion(CursorUtils.getString(c, Contract.Permiso.COLUMN_OBSERVACION));
        }

        return permiso;

    }

    public static Permiso getPermisoById(DataBase db, long id) {
        Cursor c = db.query(Contract.Permiso.TABLE_NAME, new String[]{Contract.Permiso._ID, Contract.Permiso.COLUMN_FECHA, Contract.Permiso.COLUMN_ID_PERMISO,
                        Contract.Permiso.COLUMN_TIPO, Contract.Permiso.COLUMN_OBSERVACION, Contract.Permiso.COLUMN_ID_MARCACION}, Contract.Permiso._ID + " = ? ",
                new String[]{id + ""});
        Permiso permiso = null;

        if (c.moveToFirst()) {
            permiso = new Permiso();
            permiso.setID(CursorUtils.getLong(c, Contract.Permiso._ID));
            permiso.setIdPermiso(CursorUtils.getInt(c, Contract.Permiso.COLUMN_ID_PERMISO));
            permiso.setIdMarcacion(CursorUtils.getLong(c, Contract.Permiso.COLUMN_ID_MARCACION));
            permiso.setTipo(CursorUtils.getString(c, Contract.Permiso.COLUMN_TIPO));
            permiso.setFecha(CursorUtils.getDate(c, Contract.Permiso.COLUMN_FECHA));
            permiso.setObservacion(CursorUtils.getString(c, Contract.Permiso.COLUMN_OBSERVACION));
        }

        return permiso;
    }


    public static Permiso getAusencia(DataBase db) {
        Calendar cs = Calendar.getInstance();
        cs.set(Calendar.HOUR_OF_DAY, 0);
        cs.set(Calendar.MINUTE, 0);
        cs.set(Calendar.SECOND, 0);
        cs.set(Calendar.MILLISECOND, 0);
        Cursor c = db.query(Contract.Permiso.TABLE_NAME, new String[]{Contract.Permiso._ID, Contract.Permiso.COLUMN_FECHA, Contract.Permiso.COLUMN_ID_PERMISO,
                        Contract.Permiso.COLUMN_TIPO, Contract.Permiso.COLUMN_OBSERVACION, Contract.Permiso.COLUMN_ID_MARCACION}, Contract.Permiso.COLUMN_TIPO + " = 'FALTA' AND " +
                Contract.Permiso.COLUMN_FECHA + " = ?",
                new String[]{cs.getTimeInMillis() + ""});
        Permiso permiso = null;

        if (c.moveToFirst()) {
            permiso = new Permiso();
            permiso.setID(CursorUtils.getLong(c, Contract.Permiso._ID));
            permiso.setIdPermiso(CursorUtils.getInt(c, Contract.Permiso.COLUMN_ID_PERMISO));
            permiso.setIdMarcacion(CursorUtils.getLong(c, Contract.Permiso.COLUMN_ID_MARCACION));
            permiso.setTipo(CursorUtils.getString(c, Contract.Permiso.COLUMN_TIPO));
            permiso.setFecha(CursorUtils.getDate(c, Contract.Permiso.COLUMN_FECHA));
            permiso.setObservacion(CursorUtils.getString(c, Contract.Permiso.COLUMN_OBSERVACION));
        }

        return permiso;
    }
}
