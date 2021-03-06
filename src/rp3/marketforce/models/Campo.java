package rp3.marketforce.models;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.db.Contract;
import rp3.util.Convert;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 09/04/2015.
 */
public class Campo extends rp3.data.entity.EntityBase<Campo> {

    private long id;
    private String idCampo;
    private boolean creacion;
    private boolean modificacion;
    private boolean gestion;
    private boolean obligatorio;

    @Override
    public long getID() {
        // TODO Auto-generated method stub
        return this.id;
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
        return Contract.Campos.TABLE_NAME;
    }

    @Override
    public void setValues() {
        setValue(Contract.Campos.COLUMN_ID_CAMPO, this.idCampo);
        setValue(Contract.Campos.COLUMN_CREACION, this.creacion);
        setValue(Contract.Campos.COLUMN_MODIFICACION, this.modificacion);
        setValue(Contract.Campos.COLUMN_GESTION, this.gestion);
        setValue(Contract.Campos.COLUMN_OBLIGATORIO, this.obligatorio);

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

    public String getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(String idCampo) {
        this.idCampo = idCampo;
    }

    public boolean isObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public boolean isCreacion() {
        return creacion;
    }

    public void setCreacion(boolean creacion) {
        this.creacion = creacion;
    }

    public boolean isModificacion() {
        return modificacion;
    }

    public void setModificacion(boolean modificacion) {
        this.modificacion = modificacion;
    }

    public boolean isGestion() {
        return gestion;
    }

    public void setGestion(boolean gestion) {
        this.gestion = gestion;
    }

    public static List<Campo> getCampos(DataBase db, int tipo)
    {
        String condition = null;
        switch (tipo)
        {
            case Contants.IS_CREACION: condition = Contract.Campos.COLUMN_CREACION + " = 0"; break;
            case Contants.IS_MODIFICACION: condition = Contract.Campos.COLUMN_MODIFICACION + " = 0"; break;
            case Contants.IS_GESTION: condition = Contract.Campos.COLUMN_GESTION + " = 0"; break;
            default: condition = Contract.Campos.COLUMN_CREACION + " = 1"; break;
        }
        Cursor c = db.query(Contract.Campos.TABLE_NAME, new String[] { Contract.Campos._ID, Contract.Campos.COLUMN_ID_CAMPO, Contract.Campos.COLUMN_OBLIGATORIO}
                ,condition, new String[] {});

        List<Campo> campos = new ArrayList<Campo>();

        if(c.moveToFirst())
        {
            do
            {
                Campo cp = new Campo();
                cp.setID(CursorUtils.getLong(c, Contract.Campos._ID));
                cp.setIdCampo(CursorUtils.getString(c, Contract.Campos.COLUMN_ID_CAMPO));
                cp.setObligatorio(CursorUtils.getBoolean(c, Contract.Campos.COLUMN_OBLIGATORIO));
                campos.add(cp);
            }while(c.moveToNext());
        }
        c.close();

        return campos;
    }

    public static List<Campo> getCamposObligatorios(DataBase db, int tipo)
    {
        String condition = null;
        switch (tipo)
        {
            case Contants.IS_CREACION: condition = Contract.Campos.COLUMN_CREACION + " = 1"; break;
            case Contants.IS_MODIFICACION: condition = Contract.Campos.COLUMN_MODIFICACION + " = 1"; break;
            case Contants.IS_GESTION: condition = Contract.Campos.COLUMN_GESTION + " = 1"; break;
            default: condition = Contract.Campos.COLUMN_CREACION + " = 1"; break;
        }
        Cursor c = db.query(Contract.Campos.TABLE_NAME, new String[] { Contract.Campos._ID, Contract.Campos.COLUMN_ID_CAMPO, Contract.Campos.COLUMN_OBLIGATORIO}
                ,condition + " AND " + Contract.Campos.COLUMN_OBLIGATORIO + " = 1", new String[] {});

        List<Campo> campos = new ArrayList<Campo>();

        if(c.moveToFirst())
        {
            do
            {
                Campo cp = new Campo();
                cp.setID(CursorUtils.getLong(c, Contract.Campos._ID));
                cp.setIdCampo(CursorUtils.getString(c, Contract.Campos.COLUMN_ID_CAMPO));
                cp.setObligatorio(CursorUtils.getBoolean(c, Contract.Campos.COLUMN_OBLIGATORIO));
                campos.add(cp);
            }while(c.moveToNext());
        }
        c.close();

        return campos;
    }

    public static boolean existsCampo(DataBase db, int tipo, String campo)
    {
        String condition = null;
        switch (tipo)
        {
            case Contants.IS_CREACION: condition = Contract.Campos.COLUMN_CREACION + " = 1"; break;
            case Contants.IS_MODIFICACION: condition = Contract.Campos.COLUMN_MODIFICACION + " = 1"; break;
            case Contants.IS_GESTION: condition = Contract.Campos.COLUMN_GESTION + " = 1"; break;
            default: condition = Contract.Campos.COLUMN_CREACION + " = 1"; break;
        }

        condition = condition + " AND " + Contract.Campos.COLUMN_ID_CAMPO + " = ?";
        Cursor c = db.query(Contract.Campos.TABLE_NAME, new String[] { Contract.Campos._ID, Contract.Campos.COLUMN_ID_CAMPO},condition, new String[] {campo});

        if(c.moveToFirst())
        {
            c.close();
            return true;
        }

        return false;
    }

}


