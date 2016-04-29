package rp3.marketforce.models.pedido;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 22/04/2016.
 */
public class TipoDiferido extends EntityBase<TipoDiferido> {

    private long id;
    private int idTipoDiferido;
    private int cuotas;
    private String descripcion;
    private String tipoCredito;

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
        return Contract.TipoDiferido.TABLE_NAME;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdTipoDiferido() {
        return idTipoDiferido;
    }

    public void setIdTipoDiferido(int idTipoDiferido) {
        this.idTipoDiferido = idTipoDiferido;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public String getTipoCredito() {
        return tipoCredito;
    }

    public void setTipoCredito(String tipoCredito) {
        this.tipoCredito = tipoCredito;
    }

    @Override
    public void setValues() {
        setValue(Contract.TipoDiferido.COLUMN_ID_TIPO_DIFERIDO, this.idTipoDiferido);
        setValue(Contract.TipoDiferido.COLUMN_CUOTAS, this.cuotas);
        setValue(Contract.TipoDiferido.COLUMN_TIPO_CREDITO, this.tipoCredito);
        setValue(Contract.TipoDiferido.COLUMN_DESCRIPCION, this.descripcion);
    }

    @Override
    public Object getValue(String key) {
        return idTipoDiferido;
    }

    @Override
    public String getDescription() {
        return descripcion;
    }

    public static List<TipoDiferido> getTipoDiferidos(DataBase db) {
        Cursor c = db.query(Contract.TipoDiferido.TABLE_NAME, new String[] {Contract.MarcaTarjeta._ID, Contract.TipoDiferido.COLUMN_ID_TIPO_DIFERIDO, Contract.TipoDiferido.COLUMN_DESCRIPCION,
                    Contract.TipoDiferido.COLUMN_CUOTAS, Contract.TipoDiferido.COLUMN_TIPO_CREDITO});

        List<TipoDiferido> list = new ArrayList<TipoDiferido>();
        while(c.moveToNext()){
            TipoDiferido tipoDiferido = new TipoDiferido();
            tipoDiferido.setID(CursorUtils.getInt(c, Contract.TipoDiferido._ID));
            tipoDiferido.setIdTipoDiferido(CursorUtils.getInt(c, Contract.TipoDiferido.COLUMN_ID_TIPO_DIFERIDO));
            tipoDiferido.setCuotas(CursorUtils.getInt(c, Contract.TipoDiferido.COLUMN_CUOTAS));
            tipoDiferido.setTipoCredito(CursorUtils.getString(c, Contract.TipoDiferido.COLUMN_TIPO_CREDITO));
            tipoDiferido.setDescripcion(CursorUtils.getString(c, Contract.TipoDiferido.COLUMN_DESCRIPCION));
            list.add(tipoDiferido);
        }
        c.close();
        return list;
    }

    public static List<TipoDiferido> getTipoDiferidosByBancoTarjeta(DataBase db, int idBanco, int idMarcaTarjeta) {
        String query = QueryDir.getQuery(Contract.TipoDiferido.QUERY_TIPOS_DIFERIDOS);
        Cursor c = db.rawQuery(query, new String[]{idBanco + "", idMarcaTarjeta + ""});

        List<TipoDiferido> list = new ArrayList<TipoDiferido>();
        while(c.moveToNext()){
            TipoDiferido tipoDiferido = new TipoDiferido();
            tipoDiferido.setID(CursorUtils.getInt(c, Contract.TipoDiferido._ID));
            tipoDiferido.setIdTipoDiferido(CursorUtils.getInt(c, Contract.TipoDiferido.COLUMN_ID_TIPO_DIFERIDO));
            tipoDiferido.setDescripcion(CursorUtils.getString(c, Contract.TipoDiferido.COLUMN_DESCRIPCION));
            list.add(tipoDiferido);
        }
        c.close();
        return list;
    }
}