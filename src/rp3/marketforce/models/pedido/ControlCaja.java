package rp3.marketforce.models.pedido;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 22/02/2016.
 */
public class ControlCaja extends EntityBase<Pago> {

    private long id;
    private int idControlCaja;
    private int idAgente;
    private Date fechaApertura;
    private Date fechaCierre;
    private float valorApertura;
    private float valorCierre;


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
        return Contract.ControlCaja.TABLE_NAME;
    }

    public int getIdControlCaja() {
        return idControlCaja;
    }

    public void setIdControlCaja(int idControlCaja) {
        this.idControlCaja = idControlCaja;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public float getValorApertura() {
        return valorApertura;
    }

    public void setValorApertura(float valorApertura) {
        this.valorApertura = valorApertura;
    }

    public float getValorCierre() {
        return valorCierre;
    }

    public void setValorCierre(float valorCierre) {
        this.valorCierre = valorCierre;
    }

    @Override
    public void setValues() {
        setValue(Contract.ControlCaja.COLUMN_ID_CONTROL_CAJA, this.idControlCaja);
        setValue(Contract.ControlCaja.COLUMN_ID_FECHA_APERTURA, this.fechaApertura);
        setValue(Contract.ControlCaja.COLUMN_ID_FECHA_CIERRE, this.fechaCierre);
        setValue(Contract.ControlCaja.COLUMN_ID_AGENTE, this.idAgente);
        setValue(Contract.ControlCaja.COLUMN_VALOR_APERTURA, this.valorApertura);
        setValue(Contract.ControlCaja.COLUMN_VALOR_CIERRE, this.valorCierre);
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public static List<ControlCaja> getControlCajas(DataBase db) {
        Cursor c = db.query(Contract.ControlCaja.TABLE_NAME, new String[]{Contract.ControlCaja._ID, Contract.ControlCaja.COLUMN_ID_CONTROL_CAJA, Contract.ControlCaja.COLUMN_ID_AGENTE, Contract.ControlCaja.COLUMN_ID_FECHA_APERTURA,
                Contract.ControlCaja.COLUMN_ID_FECHA_CIERRE, Contract.ControlCaja.COLUMN_VALOR_APERTURA, Contract.ControlCaja.COLUMN_VALOR_CIERRE});

        List<ControlCaja> list = new ArrayList<ControlCaja>();
        while(c.moveToNext()){
            ControlCaja controlCaja = new ControlCaja();
            controlCaja.setID(CursorUtils.getInt(c, Contract.ControlCaja._ID));
            controlCaja.setIdControlCaja(CursorUtils.getInt(c, Contract.ControlCaja.COLUMN_ID_CONTROL_CAJA));
            controlCaja.setFechaApertura(CursorUtils.getDate(c, Contract.ControlCaja.COLUMN_ID_FECHA_APERTURA));
            controlCaja.setFechaCierre(CursorUtils.getDate(c, Contract.ControlCaja.COLUMN_ID_FECHA_CIERRE));
            controlCaja.setIdAgente(CursorUtils.getInt(c, Contract.ControlCaja.COLUMN_ID_AGENTE));
            controlCaja.setValorApertura(CursorUtils.getFloat(c, Contract.ControlCaja.COLUMN_VALOR_APERTURA));
            controlCaja.setValorCierre(CursorUtils.getFloat(c, Contract.ControlCaja.COLUMN_VALOR_CIERRE));
            list.add(controlCaja);
        }
        c.close();
        return list;
    }

    public static ControlCaja getControlCaja(DataBase db, long id) {
        Cursor c = db.query(Contract.ControlCaja.TABLE_NAME, new String[]{Contract.ControlCaja._ID, Contract.ControlCaja.COLUMN_ID_CONTROL_CAJA, Contract.ControlCaja.COLUMN_ID_AGENTE, Contract.ControlCaja.COLUMN_ID_FECHA_APERTURA,
                Contract.ControlCaja.COLUMN_ID_FECHA_CIERRE, Contract.ControlCaja.COLUMN_VALOR_APERTURA, Contract.ControlCaja.COLUMN_VALOR_CIERRE}, Contract.ControlCaja._ID + " = ?", new String[]{id + ""});

        ControlCaja controlCaja = new ControlCaja();
        while(c.moveToNext()){

            controlCaja.setID(CursorUtils.getInt(c, Contract.ControlCaja._ID));
            controlCaja.setIdControlCaja(CursorUtils.getInt(c, Contract.ControlCaja.COLUMN_ID_CONTROL_CAJA));
            controlCaja.setFechaApertura(CursorUtils.getDate(c, Contract.ControlCaja.COLUMN_ID_FECHA_APERTURA));
            controlCaja.setFechaCierre(CursorUtils.getDate(c, Contract.ControlCaja.COLUMN_ID_FECHA_CIERRE));
            controlCaja.setIdAgente(CursorUtils.getInt(c, Contract.ControlCaja.COLUMN_ID_AGENTE));
            controlCaja.setValorApertura(CursorUtils.getFloat(c, Contract.ControlCaja.COLUMN_VALOR_APERTURA));
            controlCaja.setValorCierre(CursorUtils.getFloat(c, Contract.ControlCaja.COLUMN_VALOR_CIERRE));

        }
        c.close();
        return controlCaja;
    }

    public static ControlCaja getControlCajaActiva(DataBase db) {
        Cursor c = db.query(Contract.ControlCaja.TABLE_NAME, new String[]{Contract.ControlCaja._ID, Contract.ControlCaja.COLUMN_ID_CONTROL_CAJA, Contract.ControlCaja.COLUMN_ID_AGENTE, Contract.ControlCaja.COLUMN_ID_FECHA_APERTURA,
                Contract.ControlCaja.COLUMN_ID_FECHA_CIERRE, Contract.ControlCaja.COLUMN_VALOR_APERTURA, Contract.ControlCaja.COLUMN_VALOR_CIERRE}, Contract.ControlCaja.COLUMN_ID_FECHA_CIERRE + " <= 0 OR FechaCierre IS NULL", new String[]{});

        ControlCaja controlCaja = null;
        while(c.moveToNext()){
            controlCaja = new ControlCaja();
            controlCaja.setID(CursorUtils.getInt(c, Contract.ControlCaja._ID));
            controlCaja.setIdControlCaja(CursorUtils.getInt(c, Contract.ControlCaja.COLUMN_ID_CONTROL_CAJA));
            controlCaja.setFechaApertura(CursorUtils.getDate(c, Contract.ControlCaja.COLUMN_ID_FECHA_APERTURA));
            controlCaja.setFechaCierre(CursorUtils.getDate(c, Contract.ControlCaja.COLUMN_ID_FECHA_CIERRE));
            controlCaja.setIdAgente(CursorUtils.getInt(c, Contract.ControlCaja.COLUMN_ID_AGENTE));
            controlCaja.setValorApertura(CursorUtils.getFloat(c, Contract.ControlCaja.COLUMN_VALOR_APERTURA));
            controlCaja.setValorCierre(CursorUtils.getFloat(c, Contract.ControlCaja.COLUMN_VALOR_CIERRE));

        }
        c.close();
        return controlCaja;
    }
}