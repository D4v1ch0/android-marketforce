package rp3.marketforce.models.marcacion;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.Actividad;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 08/06/2015.
 */
public class Marcacion extends EntityBase<Marcacion>
{
    private long id;
    private String tipo;
    private Date fecha;
    private Date horaInicio;
    private Date horaFin;
    private int mintutosAtraso;
    private double latitud;
    private double longitud;
    private boolean enUbicacion;
    private boolean pendiente;

    private Permiso permiso;

    @Override
    public long getID() {
        // TODO Auto-generated method stub
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;

    }

    @Override
    public boolean isAutoGeneratedId() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String getTableName() {
        // TODO Auto-generated method stub
        return Contract.Marcacion.TABLE_NAME;
    }

    @Override
    public void setValues() {
        setValue(Contract.Marcacion.COLUMN_EN_UBICACION, this.enUbicacion);
        setValue(Contract.Marcacion.COLUMN_FECHA, this.fecha);
        setValue(Contract.Marcacion.COLUMN_HORA_INICIO, this.horaInicio);
        setValue(Contract.Marcacion.COLUMN_HORA_FIN, this.horaFin);
        setValue(Contract.Marcacion.COLUMN_LATITUD, this.latitud);
        setValue(Contract.Marcacion.COLUMN_LONGITUD, this.longitud);
        setValue(Contract.Marcacion.COLUMN_TIPO, this.tipo);
        setValue(Contract.Marcacion.COLUMN_PENDIENTE, this.pendiente);
        setValue(Contract.Marcacion.COLUMN_MINUTOS_ATRASO, this.mintutosAtraso);
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
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

    public boolean isEnUbicacion() {
        return enUbicacion;
    }

    public void setEnUbicacion(boolean enUbicacion) {
        this.enUbicacion = enUbicacion;
    }

    public boolean isPendiente() {
        return pendiente;
    }

    public void setPendiente(boolean pendiente) {
        this.pendiente = pendiente;
    }

    public Permiso getPermiso() {
        return permiso;
    }

    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }

    public int getMintutosAtraso() {
        return mintutosAtraso;
    }

    public void setMintutosAtraso(int mintutosAtraso) {
        this.mintutosAtraso = mintutosAtraso;
    }

    public static List<Marcacion> getMarcacionesPendientes(DataBase db)
    {
        Cursor c = db.query(Contract.Marcacion.TABLE_NAME, new String[]{Contract.Marcacion._ID, Contract.Marcacion.COLUMN_EN_UBICACION, Contract.Marcacion.COLUMN_FECHA,
                Contract.Marcacion.COLUMN_TIPO, Contract.Marcacion.COLUMN_LATITUD, Contract.Marcacion.COLUMN_LONGITUD, Contract.Marcacion.COLUMN_HORA_INICIO,
                Contract.Marcacion.COLUMN_HORA_FIN, Contract.Marcacion.COLUMN_PENDIENTE, Contract.Marcacion.COLUMN_MINUTOS_ATRASO}, Contract.Marcacion.COLUMN_PENDIENTE + " = ? " , new String[] {"1"});
        List<Marcacion> marcaciones = new ArrayList<Marcacion>();

        if(c.moveToFirst())
        {
            do
            {
                Marcacion marcacion = new Marcacion();
                marcacion.setID(CursorUtils.getLong(c, Contract.Marcacion._ID));
                marcacion.setEnUbicacion(CursorUtils.getBoolean(c, Contract.Marcacion.COLUMN_EN_UBICACION));
                marcacion.setTipo(CursorUtils.getString(c, Contract.Marcacion.COLUMN_TIPO));
                marcacion.setFecha(CursorUtils.getDate(c, Contract.Marcacion.COLUMN_FECHA));
                marcacion.setHoraInicio(CursorUtils.getDate(c, Contract.Marcacion.COLUMN_HORA_INICIO));
                marcacion.setHoraFin(CursorUtils.getDate(c, Contract.Marcacion.COLUMN_HORA_FIN));
                marcacion.setLatitud(CursorUtils.getDouble(c, Contract.Marcacion.COLUMN_LATITUD));
                marcacion.setLongitud(CursorUtils.getDouble(c, Contract.Marcacion.COLUMN_LONGITUD));
                marcacion.setPendiente(CursorUtils.getBoolean(c, Contract.Marcacion.COLUMN_PENDIENTE));
                marcacion.setMintutosAtraso(CursorUtils.getInt(c, Contract.Marcacion.COLUMN_MINUTOS_ATRASO));
                marcacion.setPermiso(Permiso.getPermisoMarcacion(db, marcacion.getID()));
                marcaciones.add(marcacion);

            }while(c.moveToNext());
        }

        return marcaciones;

    }

    public static Marcacion getUltimaMarcacion(DataBase db) {
        Cursor c = db.query(Contract.Marcacion.TABLE_NAME, new String[]{Contract.Marcacion._ID, Contract.Marcacion.COLUMN_EN_UBICACION, Contract.Marcacion.COLUMN_FECHA,
                Contract.Marcacion.COLUMN_TIPO, Contract.Marcacion.COLUMN_LATITUD, Contract.Marcacion.COLUMN_LONGITUD, Contract.Marcacion.COLUMN_HORA_INICIO,
                Contract.Marcacion.COLUMN_HORA_FIN, Contract.Marcacion.COLUMN_PENDIENTE, Contract.Marcacion.COLUMN_MINUTOS_ATRASO}, Contract.Marcacion.COLUMN_EN_UBICACION + " = ? ", new String[]{"1"}, null, null, Contract.Marcacion._ID + " DESC");

        Marcacion marcacion = null;
        if (c.moveToFirst()) {
            marcacion = new Marcacion();
            marcacion.setID(CursorUtils.getLong(c, Contract.Marcacion._ID));
            marcacion.setEnUbicacion(CursorUtils.getBoolean(c, Contract.Marcacion.COLUMN_EN_UBICACION));
            marcacion.setTipo(CursorUtils.getString(c, Contract.Marcacion.COLUMN_TIPO));
            marcacion.setFecha(CursorUtils.getDate(c, Contract.Marcacion.COLUMN_FECHA));
            marcacion.setHoraInicio(CursorUtils.getDate(c, Contract.Marcacion.COLUMN_HORA_INICIO));
            marcacion.setHoraFin(CursorUtils.getDate(c, Contract.Marcacion.COLUMN_HORA_FIN));
            marcacion.setLatitud(CursorUtils.getDouble(c, Contract.Marcacion.COLUMN_LATITUD));
            marcacion.setLongitud(CursorUtils.getDouble(c, Contract.Marcacion.COLUMN_LONGITUD));
            marcacion.setPendiente(CursorUtils.getBoolean(c, Contract.Marcacion.COLUMN_PENDIENTE));
            marcacion.setMintutosAtraso(CursorUtils.getInt(c, Contract.Marcacion.COLUMN_MINUTOS_ATRASO));
            marcacion.setPermiso(Permiso.getPermisoMarcacion(db, marcacion.getID()));
        }

        return marcacion;
    }

    public static Marcacion getMarcacion(DataBase db, long idMarcacion) {
        Cursor c = db.query(Contract.Marcacion.TABLE_NAME, new String[]{Contract.Marcacion._ID, Contract.Marcacion.COLUMN_EN_UBICACION, Contract.Marcacion.COLUMN_FECHA,
                Contract.Marcacion.COLUMN_TIPO, Contract.Marcacion.COLUMN_LATITUD, Contract.Marcacion.COLUMN_LONGITUD, Contract.Marcacion.COLUMN_HORA_INICIO,
                Contract.Marcacion.COLUMN_HORA_FIN, Contract.Marcacion.COLUMN_PENDIENTE, Contract.Marcacion.COLUMN_MINUTOS_ATRASO}, Contract.Marcacion._ID + " = ? ", new String[]{idMarcacion + ""});

        Marcacion marcacion = null;
        if (c.moveToFirst()) {
            marcacion = new Marcacion();
            marcacion.setID(CursorUtils.getLong(c, Contract.Marcacion._ID));
            marcacion.setEnUbicacion(CursorUtils.getBoolean(c, Contract.Marcacion.COLUMN_EN_UBICACION));
            marcacion.setTipo(CursorUtils.getString(c, Contract.Marcacion.COLUMN_TIPO));
            marcacion.setFecha(CursorUtils.getDate(c, Contract.Marcacion.COLUMN_FECHA));
            marcacion.setHoraInicio(CursorUtils.getDate(c, Contract.Marcacion.COLUMN_HORA_INICIO));
            marcacion.setHoraFin(CursorUtils.getDate(c, Contract.Marcacion.COLUMN_HORA_FIN));
            marcacion.setLatitud(CursorUtils.getDouble(c, Contract.Marcacion.COLUMN_LATITUD));
            marcacion.setLongitud(CursorUtils.getDouble(c, Contract.Marcacion.COLUMN_LONGITUD));
            marcacion.setPendiente(CursorUtils.getBoolean(c, Contract.Marcacion.COLUMN_PENDIENTE));
            marcacion.setMintutosAtraso(CursorUtils.getInt(c, Contract.Marcacion.COLUMN_MINUTOS_ATRASO));
            marcacion.setPermiso(Permiso.getPermisoMarcacion(db, marcacion.getID()));
        }

        return marcacion;
    }
}
