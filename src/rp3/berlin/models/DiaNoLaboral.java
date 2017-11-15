package rp3.berlin.models;

import java.util.Date;

import rp3.data.entity.EntityBase;
import rp3.berlin.db.Contract;

/**
 * Created by magno_000 on 08/06/2015.
 */
public class DiaNoLaboral extends EntityBase<DiaNoLaboral>
{
    private long id;
    private Date fecha;
    private boolean esParcial;
    private boolean esteAnio;
    private String horaInicio;
    private String horaFin;

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
        return Contract.DiaNoLaboral.TABLE_NAME;
    }

    @Override
    public void setValues() {
        setValue(Contract.DiaNoLaboral.COLUMN_ESTE_ANIO, this.esteAnio);
        setValue(Contract.DiaNoLaboral.COLUMN_FECHA, this.fecha);
        setValue(Contract.DiaNoLaboral.COLUMN_DIA_PARCIAL, this.esParcial);
        setValue(Contract.DiaNoLaboral.COLUMN_HORA_INICIO, this.horaInicio);
        setValue(Contract.DiaNoLaboral.COLUMN_HORA_FIN, this.horaFin);

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

    public boolean isEsParcial() {
        return esParcial;
    }

    public void setEsParcial(boolean esParcial) {
        this.esParcial = esParcial;
    }

    public boolean isEsteAnio() {
        return esteAnio;
    }

    public void setEsteAnio(boolean esteAnio) {
        this.esteAnio = esteAnio;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

}