package rp3.marketforce.models;

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
public class DiaLaboral extends EntityBase<DiaLaboral>
{
    private long id;
    private int idDia;
    private int orden;
    private boolean esLaboral;
    private String horaInicio1;
    private String horaFin1;
    private String horaInicio2;
    private String horaFin2;

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
        return Contract.DiaLaboral.TABLE_NAME;
    }

    @Override
    public void setValues() {
        setValue(Contract.DiaLaboral.COLUMN_ID_DIA, this.idDia);
        setValue(Contract.DiaLaboral.COLUMN_ES_LABORAL, this.esLaboral);
        setValue(Contract.DiaLaboral.COLUMN_ORDEN, this.orden);
        setValue(Contract.DiaLaboral.COLUMN_HORA_INICIO1, this.horaInicio1);
        setValue(Contract.DiaLaboral.COLUMN_HORA_FIN1, this.horaFin1);
        setValue(Contract.DiaLaboral.COLUMN_HORA_INICIO2, this.horaInicio2);
        setValue(Contract.DiaLaboral.COLUMN_HORA_FIN2, this.horaFin2);

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

    public int getIdDia() {
        return idDia;
    }

    public void setIdDia(int idDia) {
        this.idDia = idDia;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public boolean isEsLaboral() {
        return esLaboral;
    }

    public void setEsLaboral(boolean esLaboral) {
        this.esLaboral = esLaboral;
    }

    public String getHoraInicio1() {
        return horaInicio1;
    }

    public void setHoraInicio1(String horaInicio1) {
        this.horaInicio1 = horaInicio1;
    }

    public String getHoraFin1() {
        return horaFin1;
    }

    public void setHoraFin1(String horaFin1) {
        this.horaFin1 = horaFin1;
    }

    public String getHoraInicio2() {
        return horaInicio2;
    }

    public void setHoraInicio2(String horaInicio2) {
        this.horaInicio2 = horaInicio2;
    }

    public String getHoraFin2() {
        return horaFin2;
    }

    public void setHoraFin2(String horaFin2) {
        this.horaFin2 = horaFin2;
    }

    public static List<DiaLaboral> getDias(DataBase db)
    {
        Cursor c = db.query(Contract.DiaLaboral.TABLE_NAME, new String[]{ Contract.DiaLaboral.COLUMN_ES_LABORAL, Contract.DiaLaboral.COLUMN_ORDEN,
                Contract.DiaLaboral.COLUMN_ID_DIA, Contract.DiaLaboral.COLUMN_HORA_INICIO1, Contract.DiaLaboral.COLUMN_HORA_INICIO2,
                Contract.DiaLaboral.COLUMN_HORA_FIN1, Contract.DiaLaboral.COLUMN_HORA_FIN2});
        List<DiaLaboral> dias = new ArrayList<DiaLaboral>();

        if(c.moveToFirst())
        {
            do
            {
                DiaLaboral dia = new DiaLaboral();
                dia.setIdDia(CursorUtils.getInt(c, Contract.DiaLaboral.COLUMN_ID_DIA));
                dia.setOrden(CursorUtils.getInt(c, Contract.DiaLaboral.COLUMN_ORDEN));
                dia.setHoraInicio1(CursorUtils.getString(c, Contract.DiaLaboral.COLUMN_HORA_INICIO1));
                dia.setHoraFin1(CursorUtils.getString(c, Contract.DiaLaboral.COLUMN_HORA_FIN1));
                dia.setHoraInicio2(CursorUtils.getString(c, Contract.DiaLaboral.COLUMN_HORA_INICIO2));
                dia.setHoraFin2(CursorUtils.getString(c, Contract.DiaLaboral.COLUMN_HORA_FIN2));
                dia.setEsLaboral(CursorUtils.getBoolean(c, Contract.DiaLaboral.COLUMN_ES_LABORAL));
                dias.add(dia);
            }while(c.moveToNext());
        }

        return dias;

    }

    public static DiaLaboral getDia(DataBase db, int i) {
        Cursor c = db.query(Contract.DiaLaboral.TABLE_NAME, new String[]{Contract.DiaLaboral.COLUMN_ES_LABORAL, Contract.DiaLaboral.COLUMN_ORDEN,
                        Contract.DiaLaboral.COLUMN_ID_DIA, Contract.DiaLaboral.COLUMN_HORA_INICIO1, Contract.DiaLaboral.COLUMN_HORA_INICIO2,
                        Contract.DiaLaboral.COLUMN_HORA_FIN1, Contract.DiaLaboral.COLUMN_HORA_FIN2}, Contract.DiaLaboral.COLUMN_ID_DIA + " = ? ",
                new String[] {i + ""});
        DiaLaboral dia = new DiaLaboral();

        if (c.moveToFirst()) {
            dia.setIdDia(CursorUtils.getInt(c, Contract.DiaLaboral.COLUMN_ID_DIA));
            dia.setOrden(CursorUtils.getInt(c, Contract.DiaLaboral.COLUMN_ORDEN));
            dia.setHoraInicio1(CursorUtils.getString(c, Contract.DiaLaboral.COLUMN_HORA_INICIO1));
            dia.setHoraFin1(CursorUtils.getString(c, Contract.DiaLaboral.COLUMN_HORA_FIN1));
            dia.setHoraInicio2(CursorUtils.getString(c, Contract.DiaLaboral.COLUMN_HORA_INICIO2));
            dia.setHoraFin2(CursorUtils.getString(c, Contract.DiaLaboral.COLUMN_HORA_FIN2));
            dia.setEsLaboral(CursorUtils.getBoolean(c, Contract.DiaLaboral.COLUMN_ES_LABORAL));

        }

        return dia;
    }

}
